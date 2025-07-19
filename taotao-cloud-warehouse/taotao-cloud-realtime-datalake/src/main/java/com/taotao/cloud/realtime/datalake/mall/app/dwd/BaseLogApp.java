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

package com.taotao.cloud.realtime.datalake.mall.app.dwd;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.realtime.mall.utils.MyKafkaUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * Date: 2021/1/30 Desc: 准备用户行为日志的DWD层
 */
public class BaseLogApp {

    private static final String TOPIC_START = "dwd_start_log";
    private static final String TOPIC_DISPLAY = "dwd_display_log";
    private static final String TOPIC_PAGE = "dwd_page_log";

    public static void main(String[] args) throws Exception {
        // TODO 1.准备环境
        // 1.1 创建Flink流执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 1.2设置并行度
        env.setParallelism(1);

        // 1.3设置Checkpoint
        // 每5000ms开始一次checkpoint，模式是EXACTLY_ONCE（默认）
        // env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        // env.getCheckpointConfig().setCheckpointTimeout(60000);
        // env.setStateBackend(new
        // FsStateBackend("hdfs://hadoop202:8020/gmall/checkpoint/baselogApp"));

        // System.setProperty("HADOOP_USER_NAME","atguigu");

        // TODO 2.从Kafka中读取数据
        String topic = "ods_base_log";
        String groupId = "base_log_app_group";

        // 2.1 调用Kafka工具类，获取FlinkKafkaConsumer
        FlinkKafkaConsumer<String> kafkaSource = MyKafkaUtil.getKafkaSource(topic, groupId);
        DataStreamSource<String> kafkaDS = env.addSource(kafkaSource);

        // TODO 3.对读取到的数据格式进行转换         String->json
        SingleOutputStreamOperator<JSONObject> jsonObjDS =
                kafkaDS.map(
                        new MapFunction<String, JSONObject>() {
                            @Override
                            public JSONObject map(String value) throws Exception {
                                JSONObject jsonObject = JSON.parseObject(value);
                                return jsonObject;
                            }
                        });
        // jsonObjDS.print("json>>>>>>>>");
        /*
        TODO 4.识别新老访客     前端也会对新老状态进行记录，有可能会不准，咱们这里是再次做一个确认
            保存mid某天方法情况（将首次访问日期作为状态保存起来），等后面该设备在有日志过来的时候，从状态中获取日期
            和日志产生日志进行对比。如果状态不为空，并且状态日期和当前日期不相等，说明是老访客，如果is_new标记是1，那么对其状态进行修复
        */
        // 4.1 根据mid对日志进行分组
        KeyedStream<JSONObject, String> midKeyedDS =
                jsonObjDS.keyBy(data -> data.getJSONObject("common").getString("mid"));
        // 4.2 新老方法状态修复   状态分为算子状态和键控状态，我们这里要记录某一个设备的访问，使用键控状态比较合适
        SingleOutputStreamOperator<JSONObject> jsonDSWithFlag =
                midKeyedDS.map(
                        new RichMapFunction<JSONObject, JSONObject>() {
                            // 定义该mid访问状态
                            private ValueState<String> firstVisitDateState;
                            // 定义日期格式化对象
                            private SimpleDateFormat sdf;

                            @Override
                            public void open(Configuration parameters) throws Exception {
                                // 对状态以及日期格式进行初始化
                                firstVisitDateState =
                                        getRuntimeContext()
                                                .getState(
                                                        new ValueStateDescriptor<String>(
                                                                "newMidDateState", String.class));
                                sdf = new SimpleDateFormat("yyyyMMdd");
                            }

                            @Override
                            public JSONObject map(JSONObject jsonObj) throws Exception {
                                // 获取当前日志标记状态
                                String isNew = jsonObj.getJSONObject("common").getString("is_new");

                                // 获取当前日志访问时间戳
                                Long ts = jsonObj.getLong("ts");

                                if ("1".equals(isNew)) {
                                    // 获取当前mid对象的状态
                                    String stateDate = firstVisitDateState.value();
                                    // 对当前条日志的日期格式进行抓换
                                    String curDate = sdf.format(new Date(ts));
                                    // 如果状态不为空，并且状态日期和当前日期不相等，说明是老访客
                                    if (stateDate != null && stateDate.length() != 0) {
                                        // 判断是否为同一天数据
                                        if (!stateDate.equals(curDate)) {
                                            isNew = "0";
                                            jsonObj.getJSONObject("common").put("is_new", isNew);
                                        }
                                    } else {
                                        // 如果还没记录设备的状态，将当前访问日志作为状态值
                                        firstVisitDateState.update(curDate);
                                    }
                                }
                                return jsonObj;
                            }
                        });

        // jsonDSWithFlag.print(">>>>>>>>>>>");

        // TODO 5 .分流  根据日志数据内容,将日志数据分为3类, 页面日志、启动日志和曝光日志。
        // 页面日志输出到主流,启动日志输出到启动侧输出流,曝光日志输出到曝光日志侧输出流
        // 侧输出流：1)接收迟到数据    2)分流

        // 定义启动侧输出流标签
        OutputTag<String> startTag = new OutputTag<String>("start") {};
        // 定义曝光侧输出流标签
        OutputTag<String> displayTag = new OutputTag<String>("display") {};

        SingleOutputStreamOperator<String> pageDS =
                jsonDSWithFlag.process(
                        new ProcessFunction<JSONObject, String>() {
                            @Override
                            public void processElement(
                                    JSONObject jsonObj, Context ctx, Collector<String> out)
                                    throws Exception {
                                // 获取启动日志标记
                                JSONObject startJsonObj = jsonObj.getJSONObject("start");
                                // 将json格式转换为字符串，方便向侧输出流输出以及向kafka中写入
                                String dataStr = jsonObj.toString();

                                // 判断是否为启动日志
                                if (startJsonObj != null && startJsonObj.size() > 0) {
                                    // 如果是启动日志，输出到启动侧输出流
                                    ctx.output(startTag, dataStr);
                                } else {
                                    // 如果不是启动日志  说明是页面日志 ，输出到主流
                                    out.collect(dataStr);

                                    // 如果不是启动日志，获取曝光日志标记（曝光日志中也携带了页面）
                                    JSONArray displays = jsonObj.getJSONArray("displays");
                                    // 判断是否为曝光日志
                                    if (displays != null && displays.size() > 0) {
                                        // 如果是曝光日志，遍历输出到侧输出流
                                        for (int i = 0; i < displays.size(); i++) {
                                            // 获取每一条曝光事件
                                            JSONObject displaysJsonObj = displays.getJSONObject(i);
                                            // 获取页面id
                                            String pageId =
                                                    jsonObj.getJSONObject("page")
                                                            .getString("page_id");
                                            // 给每一条曝光事件加pageId
                                            displaysJsonObj.put("page_id", pageId);
                                            ctx.output(displayTag, displaysJsonObj.toString());
                                        }
                                    }
                                }
                            }
                        });

        // 获取侧输出流
        DataStream<String> startDS = pageDS.getSideOutput(startTag);
        DataStream<String> displayDS = pageDS.getSideOutput(displayTag);

        // 打印输出
        pageDS.print("page>>>>");
        startDS.print("start>>>>");
        displayDS.print("display>>>>");

        // TODO 6.将不同流的数据写回到kafka的不同topic中
        FlinkKafkaProducer<String> startSink = MyKafkaUtil.getKafkaSink(TOPIC_START);
        startDS.addSink(startSink);

        FlinkKafkaProducer<String> displaySink = MyKafkaUtil.getKafkaSink(TOPIC_DISPLAY);
        displayDS.addSink(displaySink);

        FlinkKafkaProducer<String> pageSink = MyKafkaUtil.getKafkaSink(TOPIC_PAGE);
        pageDS.addSink(pageSink);

        env.execute();
    }
}
