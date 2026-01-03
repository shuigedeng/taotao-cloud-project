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

package com.taotao.cloud.flink.flink.part1_flink_apis;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;

import org.apache.flink.util.Collector;

/**
 * ProcessFunctionDemo
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class ProcessFunctionDemo {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1); // 设置并行度为1，便于调试

        // 使用内联数据创建DataStream
        DataStream<Event> events =
                env.fromElements(
                                new Event("id1", 1L, 1),
                                new Event("id1", 2L, 2),
                                new Event("id1", 3L, 3),
                                new Event("id2", 10L, 10),
                                new Event("id2", 20L, 20),
                                new Event("id2", 30L, 30))
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy.<Event>forMonotonousTimestamps()
                                        .withTimestampAssigner(
                                                ( event, timestamp ) -> event.timestamp));

        events.keyBy(Event::getId).process(new MyProcessFunction()).print();

        env.execute("ProcessFunction Demo");
    }

    static class Event {

        String id;
        long timestamp;
        int value;

        public Event( String id, long timestamp, int value ) {
            this.id = id;
            this.timestamp = timestamp;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Event{"
                    + "id='"
                    + id
                    + '\''
                    + ", timestamp="
                    + timestamp
                    + ", value="
                    + value
                    + '}';
        }
    }

    static class MyProcessFunction extends KeyedProcessFunction<String, Event, String> {

        private ValueState<Integer> lastValue;

        @Override
        public void open( OpenContext openContext ) throws Exception {
            super.open(openContext);
            lastValue =
                    getRuntimeContext()
                            .getState(new ValueStateDescriptor<>("lastValue", Integer.class));
        }

        @Override
        public void processElement( Event value, Context ctx, Collector<String> out )
                throws Exception {
            if (lastValue.value() == null) {
                lastValue.update(value.getValue());
            } else {
                int diff = Math.abs(lastValue.value() - value.getValue());
                out.collect("Element: " + value + ", Difference from last: " + diff);
                lastValue.update(value.getValue());
            }

            ctx.timerService()
                    .registerEventTimeTimer(
                            value.getTimestamp() + Duration.ofSeconds(5).toMilliseconds());
        }

        @Override
        public void onTimer( long timestamp, OnTimerContext ctx, Collector<String> out )
                throws Exception {
            out.collect("Timer fired at " + timestamp + " for key " + ctx.getCurrentKey());
        }
    }
}
