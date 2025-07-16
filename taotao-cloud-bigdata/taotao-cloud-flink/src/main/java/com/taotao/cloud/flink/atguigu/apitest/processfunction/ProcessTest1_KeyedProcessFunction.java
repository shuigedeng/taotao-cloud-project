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

package com.taotao.cloud.flink.atguigu.apitest.processfunction;

import com.taotao.cloud.flink.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class ProcessTest1_KeyedProcessFunction {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // socket文本流
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);

        // 转换成SensorReading类型
        DataStream<SensorReading> dataStream =
                inputStream.map(
                        line -> {
                            String[] fields = line.split(",");
                            return new SensorReading(
                                    fields[0], new Long(fields[1]), new Double(fields[2]));
                        });

        // 测试KeyedProcessFunction，先分组然后自定义处理
        dataStream.keyBy("id").process(new MyProcess()).print();

        env.execute();
    }

    // 实现自定义的处理函数
    public static class MyProcess extends KeyedProcessFunction<Tuple, SensorReading, Integer> {
        ValueState<Long> tsTimerState;

        @Override
        public void open(Configuration parameters) throws Exception {
            tsTimerState =
                    getRuntimeContext()
                            .getState(new ValueStateDescriptor<Long>("ts-timer", Long.class));
        }

        @Override
        public void processElement(SensorReading value, Context ctx, Collector<Integer> out)
                throws Exception {
            out.collect(value.getId().length());

            // context
            ctx.timestamp();
            ctx.getCurrentKey();
            //            ctx.output();
            ctx.timerService().currentProcessingTime();
            ctx.timerService().currentWatermark();
            ctx.timerService()
                    .registerProcessingTimeTimer(
                            ctx.timerService().currentProcessingTime() + 5000L);
            tsTimerState.update(ctx.timerService().currentProcessingTime() + 1000L);
            //            ctx.timerService().registerEventTimeTimer((value.getTimestamp() + 10) *
            // 1000L);
            //            ctx.timerService().deleteProcessingTimeTimer(tsTimerState.value());
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<Integer> out)
                throws Exception {
            System.out.println(timestamp + " 定时器触发");
            ctx.getCurrentKey();
            //            ctx.output();
            ctx.timeDomain();
        }

        @Override
        public void close() throws Exception {
            tsTimerState.clear();
        }
    }
}
