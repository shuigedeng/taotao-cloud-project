/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.realtime.datalake.mall.app.dwm;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.realtime.mall.utils.MyKafkaUtil;
import java.util.List;
import java.util.Map;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternFlatSelectFunction;
import org.apache.flink.cep.PatternFlatTimeoutFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * Date: 2021/2/4 Desc:  用户跳出行为过滤
 */
public class UserJumpDetailApp {

    public static void main(String[] args) throws Exception {
        // TODO 1.基本环境准备
        // 1.1  准备本地测试流环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 1.2 设置并行度
        env.setParallelism(4);

        // 1.3 设置Checkpoint
        // env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        // env.getCheckpointConfig().setCheckpointTimeout(60000);
        // env.setStateBackend(new
        // FsStateBackend("hdfs://hadoop202:8020/gmall/checkpoint/uniquevisit"))

        // TODO 2.从kafka中读取数据
        String sourceTopic = "dwd_page_log";
        String groupId = "user_jump_detail_group";
        String sinkTopic = "dwm_user_jump_detail";

        FlinkKafkaConsumer<String> kafkaSource = MyKafkaUtil.getKafkaSource(sourceTopic, groupId);
        DataStreamSource<String> dataStream = env.addSource(kafkaSource);

        /*DataStream<String> dataStream = env
        .fromElements(
            "{\"common\":{\"mid\":\"101\"},\"page\":{\"page_id\":\"home\"},\"ts\":10000} ",
            "{\"common\":{\"mid\":\"102\"},\"page\":{\"page_id\":\"home\"},\"ts\":12000}",
            "{\"common\":{\"mid\":\"102\"},\"page\":{\"page_id\":\"good_list\",\"last_page_id\":" +
                "\"home\"},\"ts\":150000} ",
            "{\"common\":{\"mid\":\"102\"},\"page\":{\"page_id\":\"good_list\",\"last_page_id\":" +
                "\"detail\"},\"ts\":300000} "
        );*/

        // TODO 3.对读取到的数据进行结构的换换
        SingleOutputStreamOperator<JSONObject> jsonObjDS =
                dataStream.map(jsonStr -> JSON.parseObject(jsonStr));

        // jsonObjDS.print("json>>>>>");
        // 注意：从Flink1.12开始，默认的时间语义就是事件时间，不需要额外指定；如果是之前的版本，需要通过如下语句指定事件时间语义
        // env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // TODO 4. 指定事件时间字段
        SingleOutputStreamOperator<JSONObject> jsonObjWithTSDS =
                jsonObjDS.assignTimestampsAndWatermarks(
                        WatermarkStrategy.<JSONObject>forMonotonousTimestamps()
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<JSONObject>() {
                                            @Override
                                            public long extractTimestamp(
                                                    JSONObject jsonObj, long recordTimestamp) {
                                                return jsonObj.getLong("ts");
                                            }
                                        }));

        // TODO 5.按照mid进行分组
        KeyedStream<JSONObject, String> keyByMidDS =
                jsonObjWithTSDS.keyBy(jsonObj -> jsonObj.getJSONObject("common").getString("mid"));

        /*
            计算页面跳出明细，需要满足两个条件
                1.不是从其它页面跳转过来的页面，是一个首次访问页面
                        last_page_id == null
                2.距离首次访问结束后10秒内，没有对其它的页面再进行访问
        */
        // TODO 6.配置CEP表达式
        Pattern<JSONObject, JSONObject> pattern =
                Pattern.<JSONObject>begin("first")
                        .where(
                                // 模式1:不是从其它页面跳转过来的页面，是一个首次访问页面
                                new SimpleCondition<JSONObject>() {
                                    @Override
                                    public boolean filter(JSONObject jsonObj) throws Exception {
                                        // 获取last_page_id
                                        String lastPageId =
                                                jsonObj.getJSONObject("page")
                                                        .getString("last_page_id");
                                        // 判断是否为null 将为空的保留，非空的过滤掉
                                        if (lastPageId == null || lastPageId.length() == 0) {
                                            return true;
                                        }
                                        return false;
                                    }
                                })
                        .next("next")
                        .where(
                                // 模式2. 判读是否对页面做了访问
                                new SimpleCondition<JSONObject>() {
                                    @Override
                                    public boolean filter(JSONObject jsonObj) throws Exception {
                                        // 获取当前页面的id
                                        String pageId =
                                                jsonObj.getJSONObject("page").getString("page_id");
                                        // 判断当前访问的页面id是否为null
                                        if (pageId != null && pageId.length() > 0) {
                                            return true;
                                        }
                                        return false;
                                    }
                                })
                        // 3.时间限制模式
                        .within(Time.milliseconds(10000));

        // TODO 7.根据：CEP表达式筛选流
        PatternStream<JSONObject> patternStream = CEP.pattern(keyByMidDS, pattern);

        // TODO 8.从筛选之后的流中，提取数据   将超时数据  放到侧输出流中
        OutputTag<String> timeoutTag = new OutputTag<String>("timeout") {};

        SingleOutputStreamOperator<String> filterDS =
                patternStream.flatSelect(
                        timeoutTag,
                        // 处理超时数据
                        new PatternFlatTimeoutFunction<JSONObject, String>() {
                            @Override
                            public void timeout(
                                    Map<String, List<JSONObject>> pattern,
                                    long timeoutTimestamp,
                                    Collector<String> out)
                                    throws Exception {
                                // 获取所有符合first的json对象
                                List<JSONObject> jsonObjectList = pattern.get("first");
                                // 注意：在timeout方法中的数据都会被参数1中的标签标记
                                for (JSONObject jsonObject : jsonObjectList) {
                                    out.collect(jsonObject.toJSONString());
                                }
                            }
                        },
                        // 处理的没有超时数据
                        new PatternFlatSelectFunction<JSONObject, String>() {
                            @Override
                            public void flatSelect(
                                    Map<String, List<JSONObject>> pattern, Collector<String> out)
                                    throws Exception {
                                // 没有超时的数据，不在我们的统计范围之内 ，所以这里不需要写什么代码
                            }
                        });

        // TODO 9.从侧输出流中获取超时数据
        DataStream<String> jumpDS = filterDS.getSideOutput(timeoutTag);

        // jumpDS.print(">>>>>");

        // TODO 10.将跳出数据写回到kafka的DWM层
        jumpDS.addSink(MyKafkaUtil.getKafkaSink(sinkTopic));

        env.execute();
    }
}
