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

package com.taotao.cloud.realtime.datalake.mall.app.dws;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.realtime.mall.bean.VisitorStats;
import com.taotao.cloud.realtime.mall.utils.ClickHouseUtil;
import com.taotao.cloud.realtime.mall.utils.MyKafkaUtil;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

/**
 * Date: 2021/2/22 Desc:  访客主题统计 需要启动的服务 -logger.sh(Nginx以及日志处理服务)、zk、kafka
 * -BaseLogApp、UniqueVisitApp、UserJumpDetailApp、VisitorStatsApp 执行流程分析 -模拟生成日志数据 -交给Nginx进行反向代理
 * -交给日志处理服务 将日志发送到kafka的ods_base_log -BaseLogApp从ods层读取数据，进行分流，将分流的数据发送到kakfa的dwd(dwd_page_log)
 * -UniqueVisitApp从dwd_page_log读取数据，将独立访客明细发送到dwm_unique_visit -UserJumpDetailApp从dwd_page_log读取数据，将页面跳出明细发送到dwm_user_jump_detail
 * -VisitorStatsApp >从dwd_page_log读取数据，计算pv、持续访问时间、session_count >从dwm_unique_visit读取数据，计算uv
 * >从dwm_user_jump_detail读取数据，计算页面跳出 >输出 >统一格式 合并 >分组、开窗、聚合 >将聚合统计的结果保存到ClickHouse OLAP数据库
 */
public class VisitorStatsApp {

    public static void main(String[] args) throws Exception {
        // TODO 1.基本环境准备
        // 1.1 设置流式处理环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 1.2 设置并行度
        env.setParallelism(1);
        /*
        //1.3 检查点CK相关设置
        env.enableCheckpointing(5000, CheckpointingMode.AT_LEAST_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        StateBackend fsStateBackend = new FsStateBackend(
                "hdfs://hadoop202:8020/gmall/flink/checkpoint/VisitorStatsApp");
        env.setStateBackend(fsStateBackend);
        System.setProperty("HADOOP_USER_NAME","atguigu");
        */

        // TODO 2.从kafka主题中读取数据
        // 2.1 声明读取的主题名以及消费者组
        String pageViewSourceTopic = "dwd_page_log";
        String uniqueVisitSourceTopic = "dwm_unique_visit";
        String userJumpDetailSourceTopic = "dwm_user_jump_detail";
        String groupId = "visitor_stats_app";

        // 2.2 从dwd_page_log主题中读取日志数据
        FlinkKafkaConsumer<String> pageViewSource =
                MyKafkaUtil.getKafkaSource(pageViewSourceTopic, groupId);
        DataStreamSource<String> pvJsonStrDS = env.addSource(pageViewSource);

        // 2.3 从dwm_unique_visit主题中读取uv数据
        FlinkKafkaConsumer<String> uvSource =
                MyKafkaUtil.getKafkaSource(uniqueVisitSourceTopic, groupId);
        DataStreamSource<String> uvJsonStrDS = env.addSource(uvSource);

        // 2.4 从dwm_user_jump_detail主题中读取跳出数据
        FlinkKafkaConsumer<String> userJumpSource =
                MyKafkaUtil.getKafkaSource(userJumpDetailSourceTopic, groupId);
        DataStreamSource<String> userJumpJsonStrDS = env.addSource(userJumpSource);

        // 2.5 输出各流中的数据
        // pvJsonStrDS.print("pv>>>>>");
        // uvJsonStrDS.print("uv>>>>>");
        // userJumpJsonStrDS.print("userJump>>>>>");

        // TODO 3.对各个流的数据进行结构的转换  jsonStr->VisitorStats
        // 3.1 转换pv流
        SingleOutputStreamOperator<VisitorStats> pvStatsDS =
                pvJsonStrDS.map(
                        new MapFunction<String, VisitorStats>() {
                            @Override
                            public VisitorStats map(String jsonStr) throws Exception {
                                // 将json格式字符串转换为json对象
                                JSONObject jsonObj = JSON.parseObject(jsonStr);
                                VisitorStats visitorStats =
                                        new VisitorStats(
                                                "",
                                                "",
                                                jsonObj.getJSONObject("common").getString("vc"),
                                                jsonObj.getJSONObject("common").getString("ch"),
                                                jsonObj.getJSONObject("common").getString("ar"),
                                                jsonObj.getJSONObject("common").getString("is_new"),
                                                0L,
                                                1L,
                                                0L,
                                                0L,
                                                jsonObj.getJSONObject("page")
                                                        .getLong("during_time"),
                                                jsonObj.getLong("ts"));
                                return visitorStats;
                            }
                        });
        // 3.2 转换uv流
        SingleOutputStreamOperator<VisitorStats> uvStatsDS =
                uvJsonStrDS.map(
                        new MapFunction<String, VisitorStats>() {
                            @Override
                            public VisitorStats map(String jsonStr) throws Exception {
                                // 将json格式字符串转换为json对象
                                JSONObject jsonObj = JSON.parseObject(jsonStr);
                                VisitorStats visitorStats =
                                        new VisitorStats(
                                                "",
                                                "",
                                                jsonObj.getJSONObject("common").getString("vc"),
                                                jsonObj.getJSONObject("common").getString("ch"),
                                                jsonObj.getJSONObject("common").getString("ar"),
                                                jsonObj.getJSONObject("common").getString("is_new"),
                                                1L,
                                                0L,
                                                0L,
                                                0L,
                                                0L,
                                                jsonObj.getLong("ts"));
                                return visitorStats;
                            }
                        });
        // 3.3 转换sv流（Session_count）  其实还是从dwd_page_log中获取数据
        SingleOutputStreamOperator<VisitorStats> svStatsDS =
                pvJsonStrDS.process(
                        new ProcessFunction<String, VisitorStats>() {
                            @Override
                            public void processElement(
                                    String jsonStr, Context ctx, Collector<VisitorStats> out)
                                    throws Exception {
                                // 将json格式字符串转换为json对象
                                JSONObject jsonObj = JSON.parseObject(jsonStr);
                                // 获取当前页面的lastPageId
                                String lastPageId =
                                        jsonObj.getJSONObject("page").getString("last_page_id");
                                if (lastPageId == null || lastPageId.length() == 0) {
                                    VisitorStats visitorStats =
                                            new VisitorStats(
                                                    "",
                                                    "",
                                                    jsonObj.getJSONObject("common").getString("vc"),
                                                    jsonObj.getJSONObject("common").getString("ch"),
                                                    jsonObj.getJSONObject("common").getString("ar"),
                                                    jsonObj.getJSONObject("common")
                                                            .getString("is_new"),
                                                    0L,
                                                    0L,
                                                    1L,
                                                    0L,
                                                    0L,
                                                    jsonObj.getLong("ts"));
                                    out.collect(visitorStats);
                                }
                            }
                        });

        // 3.4 转换跳出流
        SingleOutputStreamOperator<VisitorStats> userJumpStatsDS =
                userJumpJsonStrDS.map(
                        new MapFunction<String, VisitorStats>() {
                            @Override
                            public VisitorStats map(String jsonStr) throws Exception {
                                // 将json格式字符串转换为json对象
                                JSONObject jsonObj = JSON.parseObject(jsonStr);
                                VisitorStats visitorStats =
                                        new VisitorStats(
                                                "",
                                                "",
                                                jsonObj.getJSONObject("common").getString("vc"),
                                                jsonObj.getJSONObject("common").getString("ch"),
                                                jsonObj.getJSONObject("common").getString("ar"),
                                                jsonObj.getJSONObject("common").getString("is_new"),
                                                0L,
                                                0L,
                                                0L,
                                                1L,
                                                0L,
                                                jsonObj.getLong("ts"));
                                return visitorStats;
                            }
                        });

        // TODO 4. 将4条流合并到一起   注意：只能合并结构相同的流
        DataStream<VisitorStats> unionDS = pvStatsDS.union(uvStatsDS, svStatsDS, userJumpStatsDS);

        // TODO 5.设置Watermmark以及提取事件时间
        SingleOutputStreamOperator<VisitorStats> visitorStatsWithWatermarkDS =
                unionDS.assignTimestampsAndWatermarks(
                        WatermarkStrategy.<VisitorStats>forBoundedOutOfOrderness(
                                        Duration.ofSeconds(3))
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<VisitorStats>() {
                                            @Override
                                            public long extractTimestamp(
                                                    VisitorStats visitorStats,
                                                    long recordTimestamp) {
                                                return visitorStats.getTs();
                                            }
                                        }));

        // TODO 6.分组  按照地区、渠道、版本、新老访客维度进行分组，因为我们这里有4个维度，所以将它们封装为一个Tuple4
        KeyedStream<VisitorStats, Tuple4<String, String, String, String>> keyedDS =
                visitorStatsWithWatermarkDS.keyBy(
                        new KeySelector<VisitorStats, Tuple4<String, String, String, String>>() {
                            @Override
                            public Tuple4<String, String, String, String> getKey(
                                    VisitorStats visitorStats) throws Exception {
                                return Tuple4.of(
                                        visitorStats.getAr(),
                                        visitorStats.getCh(),
                                        visitorStats.getVc(),
                                        visitorStats.getIs_new());
                            }
                        });

        // TODO 7.开窗
        WindowedStream<VisitorStats, Tuple4<String, String, String, String>, TimeWindow> windowDS =
                keyedDS.window(TumblingEventTimeWindows.of(Time.seconds(10)));

        // TODO 8.对窗口的数据进行聚合   聚合结束之后，需要补充统计的起止时间
        SingleOutputStreamOperator<VisitorStats> reduceDS =
                windowDS.reduce(
                        new ReduceFunction<VisitorStats>() {
                            @Override
                            public VisitorStats reduce(VisitorStats stats1, VisitorStats stats2)
                                    throws Exception {
                                stats1.setPv_ct(stats1.getPv_ct() + stats2.getPv_ct());
                                stats1.setUv_ct(stats1.getUv_ct() + stats2.getUv_ct());
                                stats1.setSv_ct(stats1.getSv_ct() + stats2.getSv_ct());
                                stats1.setDur_sum(stats1.getDur_sum() + stats2.getDur_sum());
                                return stats1;
                            }
                        },
                        new ProcessWindowFunction<
                                VisitorStats,
                                VisitorStats,
                                Tuple4<String, String, String, String>,
                                TimeWindow>() {
                            @Override
                            public void process(
                                    Tuple4<String, String, String, String> tuple4,
                                    Context context,
                                    Iterable<VisitorStats> elements,
                                    Collector<VisitorStats> out)
                                    throws Exception {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                for (VisitorStats visitorStats : elements) {
                                    // 获取窗口的开始时间
                                    String startDate =
                                            sdf.format(new Date(context.window().getStart()));
                                    // 获取窗口的结束时间
                                    String endDate =
                                            sdf.format(new Date(context.window().getEnd()));
                                    visitorStats.setStt(startDate);
                                    visitorStats.setEdt(endDate);
                                    visitorStats.setTs(System.currentTimeMillis());
                                    out.collect(visitorStats);
                                }
                            }
                        });

        // reduceDS.print(">>>>>");

        // TODO 9.向Clickhouse中插入数据
        reduceDS.addSink(
                ClickHouseUtil.getJdbcSink(
                        "insert into visitor_stats_0820 values(?,?,?,?,?,?,?,?,?,?,?,?)"));

        env.execute();
    }
}
