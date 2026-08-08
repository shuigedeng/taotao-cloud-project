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
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * Date: 2021/2/3 Desc:  独立访客UV的计算
 * <p>
 * 前期准备： -启动ZK、Kafka、Logger.sh、BaseLogApp、UniqueVisitApp 执行流程: 模式生成日志的jar->nginx->日志采集服务->kafka(ods)
 * ->BaseLogApp(分流)->kafka(dwd) dwd_page_log ->UniqueVisitApp(独立访客)->kafka(dwm_unique_visit)
 */
public class UniqueVisitApp {

    public static void main(String[] args) throws Exception {
        // TODO 1.基本环境准备
        // 1.1  准备本地测试流环境
        // StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());

        // 1.2 设置并行度
        env.setParallelism(4);

        // 1.3 设置Checkpoint
        // env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        // env.getCheckpointConfig().setCheckpointTimeout(60000);
        // env.setStateBackend(new
        // FsStateBackend("hdfs://hadoop202:8020/gmall/checkpoint/uniquevisit"))

        // TODO 2.从kafka中读取数据
        String sourceTopic = "dwd_page_log";
        String groupId = "unique_visit_app_group";
        String sinkTopic = "dwm_unique_visit";
        FlinkKafkaConsumer<String> kafkaSource = MyKafkaUtil.getKafkaSource(sourceTopic, groupId);
        DataStreamSource<String> jsonStrDS = env.addSource(kafkaSource);

        // TODO 3.对读取到的数据进行结构的换换
        SingleOutputStreamOperator<JSONObject> jsonObjDS =
                jsonStrDS.map(jsonStr -> JSON.parseObject(jsonStr));

        // TODO 4.按照设备id进行分组
        KeyedStream<JSONObject, String> keybyWithMidDS =
                jsonObjDS.keyBy(jsonObj -> jsonObj.getJSONObject("common").getString("mid"));
        // TODO 5.过滤得到UV
        SingleOutputStreamOperator<JSONObject> filteredDS =
                keybyWithMidDS.filter(
                        new RichFilterFunction<JSONObject>() {
                            // 定义状态
                            ValueState<String> lastVisitDateState = null;
                            // 定义日期工具类
                            SimpleDateFormat sdf = null;

                            @Override
                            public void open(Configuration parameters) throws Exception {
                                // 初始化日期工具类
                                sdf = new SimpleDateFormat("yyyyMMdd");
                                // 初始化状态
                                ValueStateDescriptor<String> lastVisitDateStateDes =
                                        new ValueStateDescriptor<>(
                                                "lastVisitDateState", String.class);
                                // 因为我们统计的是日活DAU，所以状态数据只在当天有效 ，过了一天就可以失效掉
                                StateTtlConfig stateTtlConfig =
                                        StateTtlConfig.newBuilder(Time.days(1)).build();
                                lastVisitDateStateDes.enableTimeToLive(stateTtlConfig);
                                this.lastVisitDateState =
                                        getRuntimeContext().getState(lastVisitDateStateDes);
                            }

                            @Override
                            public boolean filter(JSONObject jsonObj) throws Exception {
                                // 首先判断当前页面是否从别的页面跳转过来的
                                String lastPageId =
                                        jsonObj.getJSONObject("page").getString("last_page_id");
                                if (lastPageId != null && lastPageId.length() > 0) {
                                    return false;
                                }

                                // 获取当前访问时间
                                Long ts = jsonObj.getLong("ts");
                                // 将当前访问时间戳转换为日期字符串
                                String logDate = sdf.format(new Date(ts));
                                // 获取状态日期
                                String lastVisitDate = lastVisitDateState.value();

                                // 用当前页面的访问时间和状态时间进行对比
                                if (lastVisitDate != null
                                        && lastVisitDate.length() > 0
                                        && lastVisitDate.equals(logDate)) {
                                    System.out.println(
                                            "已访问：lastVisitDate-"
                                                    + lastVisitDate
                                                    + ",||logDate:"
                                                    + logDate);
                                    return false;
                                } else {
                                    System.out.println(
                                            "未访问：lastVisitDate-"
                                                    + lastVisitDate
                                                    + ",||logDate:"
                                                    + logDate);
                                    lastVisitDateState.update(logDate);
                                    return true;
                                }
                            }
                        });

        // filteredDS.print(">>>>>");

        // TODO 6. 向kafka中写回，需要将json转换为String
        // 6.1 json->string
        SingleOutputStreamOperator<String> kafkaDS =
                filteredDS.map(jsonObj -> jsonObj.toJSONString());

        // 6.2 写回到kafka的dwm层
        kafkaDS.addSink(MyKafkaUtil.getKafkaSink(sinkTopic));

        env.execute();
    }
}
