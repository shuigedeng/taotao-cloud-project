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

package com.taotao.cloud.flink.flink.part8_flink_checkpoint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.common.typeutils.base.VoidSerializer;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.TwoPhaseCommitSinkFunction;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;

import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.bigdatatechcir.learn_flink.util.DruidUtil;

/**
 * TwoPhaseCommitSinkFunctionDemo
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class TwoPhaseCommitSinkFunctionDemo {

    public static void main( String[] args ) throws Exception {
        Configuration conf = new Configuration();
        conf.set(RestOptions.BIND_PORT, "8081");
        final StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.setStateBackend(new HashMapStateBackend());
        env.getCheckpointConfig().setCheckpointStorage("file:///D:/flink-state");
        // 开启 checkpoint，并设置间隔 ms
        env.enableCheckpointing(1000);
        // 模式 Exactly-Once、At-Least-Once
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 模式 At-Least-Once
        // env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.AT_LEAST_ONCE);

        // 两个 checkpoint 之间最小间隔
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        // 超时时间
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        // 同时执行的 checkpoint 数量（比如上一个还没执行完，下一个已经触发开始了）
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        // 当用户取消了作业后，是否保留远程存储上的Checkpoint数据
        env.getCheckpointConfig()
                .enableExternalizedCheckpoints(
                        CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        // 设置自动生成Watermark的间隔时间
        // env.getConfig().setAutoWatermarkInterval(100000);
        env.setParallelism(1);

        //env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<String> text =
                env.addSource(
                        new RichParallelSourceFunction<String>() {
                            private volatile boolean running = true;
                            private volatile long count = 0; // 计数器用于跟踪已生成的数据条数
                            private final Random random = new Random();

                            @Override
                            public void run( SourceContext<String> ctx ) throws Exception {
                                while (running) {
                                    int randomNum = random.nextInt(5) + 1;
                                    long timestamp = System.currentTimeMillis();

                                    // 如果生成的是 key2，则在一个新线程中处理延迟
                                    if (randomNum == 2) {
                                        new Thread(
                                                () -> {
                                                    try {
                                                        int delay =
                                                                random.nextInt(10)
                                                                        + 1; // 随机数范围从1到10
                                                        Thread.sleep(
                                                                delay * 1000); // 增加1到10秒的延迟
                                                        ctx.collectWithTimestamp(
                                                                "key" + randomNum + "," + 1
                                                                        + "," + timestamp,
                                                                timestamp);
                                                    } catch (InterruptedException e) {
                                                        Thread.currentThread().interrupt();
                                                    }
                                                })
                                                .start();
                                    } else {
                                        ctx.collectWithTimestamp(
                                                "key" + randomNum + "," + 1 + "," + timestamp,
                                                timestamp);
                                    }

                                    if (++count % 200 == 0) {
                                        ctx.emitWatermark(new Watermark(timestamp));
                                        System.out.println(
                                                "Manual Watermark emitted: " + timestamp);
                                    }

                                    ZonedDateTime generateDataDateTime =
                                            Instant.ofEpochMilli(timestamp)
                                                    .atZone(ZoneId.systemDefault());
                                    DateTimeFormatter formatter =
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                                    String formattedGenerateDataDateTime =
                                            generateDataDateTime.format(formatter);
                                    System.out.println(
                                            "Generated data: "
                                                    + "key"
                                                    + randomNum
                                                    + ","
                                                    + 1
                                                    + ","
                                                    + timestamp
                                                    + " at "
                                                    + formattedGenerateDataDateTime);

                                    Thread.sleep(1000); // 每次循环后等待1秒
                                }
                            }

                            @Override
                            public void cancel() {
                                running = false;
                            }
                        });

        DataStream<Tuple3<String, Integer, Long>> tuplesWithTimestamp =
                text.map(
                                new MapFunction<String, Tuple3<String, Integer, Long>>() {
                                    @Override
                                    public Tuple3<String, Integer, Long> map( String value ) {
                                        String[] words = value.split(",");
                                        return new Tuple3<>(
                                                words[0],
                                                Integer.parseInt(words[1]),
                                                Long.parseLong(words[2]));
                                    }
                                })
                        .returns(Types.TUPLE(Types.STRING, Types.INT, Types.LONG));

        // 设置 Watermark 策略
        DataStream<Tuple3<String, Integer, Long>> withWatermarks =
                tuplesWithTimestamp.assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple3<String, Integer, Long>>forBoundedOutOfOrderness(
                                        Duration.ofSeconds(0))
                                .withTimestampAssigner(( element, recordTimestamp ) -> element.f2));

        // 窗口逻辑
        DataStream<Tuple2<String, Integer>> keyedStream =
                withWatermarks
                        .keyBy(value -> value.f0)
                        .window(TumblingEventTimeWindows.of(Duration.ofSeconds(5)))
                        .process(
                                new ProcessWindowFunction<
                                        Tuple3<String, Integer, Long>,
                                        Tuple2<String, Integer>,
                                        String,
                                        TimeWindow>() {
                                    private ValueState<Integer> countState;

                                    @Override
                                    public void open( OpenContext openContext ) {
                                        countState =
                                                getRuntimeContext()
                                                        .getState(
                                                                new ValueStateDescriptor<>(
                                                                        "count", Types.INT));
                                    }

                                    @Override
                                    public void process(
                                            String s,
                                            Context context,
                                            Iterable<Tuple3<String, Integer, Long>> elements,
                                            Collector<Tuple2<String, Integer>> out )
                                            throws Exception {
                                        int count = 0;
                                        for (Tuple3<String, Integer, Long> element : elements) {
                                            count++;
                                        }

                                        // 在更新状态之前获取当前状态值
                                        Integer previousCount = countState.value();

                                        // 更新状态
                                        countState.update(count);

                                        // 获取更新后的状态值
                                        Integer updatedCount = countState.value();

                                        // 打印状态更新前后的值
                                        if (previousCount != null) {
                                            System.out.println(
                                                    "Key: "
                                                            + s
                                                            + ", Previous Count: "
                                                            + previousCount
                                                            + ", Updated Count: "
                                                            + updatedCount);
                                        } else {
                                            System.out.println(
                                                    "Key: "
                                                            + s
                                                            + ", Initial Count: "
                                                            + updatedCount);
                                        }

                                        long start = context.window().getStart();
                                        long end = context.window().getEnd();

                                        ZonedDateTime startDateTime =
                                                Instant.ofEpochMilli(start)
                                                        .atZone(ZoneId.systemDefault());
                                        ZonedDateTime endDateTime =
                                                Instant.ofEpochMilli(end)
                                                        .atZone(ZoneId.systemDefault());

                                        DateTimeFormatter formatter =
                                                DateTimeFormatter.ofPattern(
                                                        "yyyy-MM-dd HH:mm:ss.SSS");
                                        String formattedStart = startDateTime.format(formatter);
                                        String formattedEnd = endDateTime.format(formatter);

                                        System.out.println(
                                                "Tumbling Window [start "
                                                        + formattedStart
                                                        + ", end "
                                                        + formattedEnd
                                                        + ") for key "
                                                        + s);

                                        // 输出窗口结束时的Watermark
                                        long windowEndWatermark = context.currentWatermark();
                                        ZonedDateTime windowEndDateTime =
                                                Instant.ofEpochMilli(windowEndWatermark)
                                                        .atZone(ZoneId.systemDefault());
                                        String formattedWindowEndWatermark =
                                                windowEndDateTime.format(formatter);
                                        System.out.println(
                                                "Watermark at the end of window: "
                                                        + formattedWindowEndWatermark);

                                        // 读取状态并输出
                                        Integer currentCount = countState.value();
                                        if (currentCount != null) {
                                            out.collect(new Tuple2<>(s, currentCount));
                                        }
                                    }
                                });

        // 输出结果
        keyedStream.print();
        // 将结果数据写入mysql
        // resultData.addSink(new YRCanbusTwoPhaseCommitSinkFunction()).name("MySQL Sink");

        // 执行任务
        env.execute("Periodic Watermark Demo");
    }

    public class YRCanbusTwoPhaseCommitSinkFunction
            extends TwoPhaseCommitSinkFunction<YRcanbusStore, YRCanbusTransaction, Void> {

        public YRCanbusTwoPhaseCommitSinkFunction() {
            super(
                    new KryoSerializer<>(YRCanbusTransaction.class, new ExecutionConfig()),
                    VoidSerializer.INSTANCE);
        }

        @Override
        protected void invoke(
                YRCanbusTransaction yrCanbusTransaction,
                YRcanbusStore yRcanbusStore,
                Context context )
                throws Exception {
            yrCanbusTransaction.store(yRcanbusStore);
        }

        @Override
        protected YRCanbusTransaction beginTransaction() throws Exception {
            return new YRCanbusTransaction();
        }

        @Override
        protected void preCommit( YRCanbusTransaction yrCanbusTransaction ) throws Exception {
        }

        @Override
        protected void commit( YRCanbusTransaction yrCanbusTransaction ) {
            try {
                yrCanbusTransaction.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void abort( YRCanbusTransaction yrCanbusTransaction ) {
            try {
                yrCanbusTransaction.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class YRCanbusTransaction {

        private transient Connection connection;
        private List<YRcanbusStore> list = new ArrayList<>();

        public void store( YRcanbusStore yrcanbusStore ) {
            list.add(yrcanbusStore);
        }

        public void commit() throws SQLException {
            connection = DruidUtil.getConnection();
            connection.setAutoCommit(false);
            for (YRcanbusStore yrcanbusStore : list) {
                String platform = yrcanbusStore.getPlatform();
                String type = yrcanbusStore.getType();
                String day = yrcanbusStore.getDay();
                String hour = yrcanbusStore.getHour();
                String sql = "";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, platform);
                preparedStatement.setString(2, type);
                preparedStatement.setString(3, day);
                preparedStatement.setString(4, hour);
                preparedStatement.execute();
            }
            connection.commit();
            connection.close();
        }

        public void rollback() throws SQLException {
            connection.rollback();
            connection.close();
        }
    }

    public class YRcanbusStore {

        private String platform;
        private String type;
        private String day;
        private String hour;

        public YRcanbusStore( String platform, String type, String day, String hour ) {
            this.platform = platform;
            this.type = type;
            this.day = day;
            this.hour = hour;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform( String platform ) {
            this.platform = platform;
        }

        public String getType() {
            return type;
        }

        public void setType( String type ) {
            this.type = type;
        }

        public String getDay() {
            return day;
        }

        public void setDay( String day ) {
            this.day = day;
        }

        public String getHour() {
            return hour;
        }

        public void setHour( String hour ) {
            this.hour = hour;
        }
    }
}
