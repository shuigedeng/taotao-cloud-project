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

package com.taotao.cloud.realtime.datalake.behavior.orderpay_detect;

import com.taotao.cloud.realtime.behavior.analysis.orderpay_detect.beans.OrderEvent;
import com.taotao.cloud.realtime.behavior.analysis.orderpay_detect.beans.OrderResult;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.PatternTimeoutFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

/**
 * OrderPayTimeout
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class OrderPayTimeout {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        // 读取数据并转换成POJO类型
        URL resource = OrderPayTimeout.class.getResource("/OrderLog.csv");
        DataStream<OrderEvent> orderEventStream =
                env.readTextFile(resource.getPath())
                        .map(
                                line -> {
                                    String[] fields = line.split(",");
                                    return new OrderEvent(
                                            new Long(fields[0]),
                                            fields[1],
                                            fields[2],
                                            new Long(fields[3]));
                                })
                        .assignTimestampsAndWatermarks(
                                new AscendingTimestampExtractor<OrderEvent>() {
                                    @Override
                                    public long extractAscendingTimestamp( OrderEvent element ) {
                                        return element.getTimestamp() * 1000L;
                                    }
                                });

        // 1. 定义一个带时间限制的模式
        Pattern<OrderEvent, OrderEvent> orderPayPattern =
                Pattern.<OrderEvent>begin("create")
                        .where(
                                new SimpleCondition<OrderEvent>() {
                                    @Override
                                    public boolean filter( OrderEvent value ) throws Exception {
                                        return "create".equals(value.getEventType());
                                    }
                                })
                        .followedBy("pay")
                        .where(
                                new SimpleCondition<OrderEvent>() {
                                    @Override
                                    public boolean filter( OrderEvent value ) throws Exception {
                                        return "pay".equals(value.getEventType());
                                    }
                                })
                        .within(Time.minutes(15));

        // 2. 定义侧输出流标签，用来表示超时事件
        OutputTag<OrderResult> orderTimeoutTag = new OutputTag<OrderResult>("order-timeout") {
        };

        // 3. 将pattern应用到输入数据流上，得到pattern stream
        PatternStream<OrderEvent> patternStream =
                CEP.pattern(orderEventStream.keyBy(OrderEvent::getOrderId), orderPayPattern);

        // 4. 调用select方法，实现对匹配复杂事件和超时复杂事件的提取和处理
        SingleOutputStreamOperator<OrderResult> resultStream =
                patternStream.select(
                        orderTimeoutTag, new OrderTimeoutSelect(), new OrderPaySelect());

        resultStream.print("payed normally");
        resultStream.getSideOutput(orderTimeoutTag).print("timeout");

        env.execute("order timeout detect job");
    }

    // 实现自定义的超时事件处理函数
    public static class OrderTimeoutSelect
            implements PatternTimeoutFunction<OrderEvent, OrderResult> {

        @Override
        public OrderResult timeout( Map<String, List<OrderEvent>> pattern, long timeoutTimestamp )
                throws Exception {
            Long timeoutOrderId = pattern.get("create").iterator().next().getOrderId();
            return new OrderResult(timeoutOrderId, "timeout " + timeoutTimestamp);
        }
    }

    // 实现自定义的正常匹配事件处理函数
    public static class OrderPaySelect implements PatternSelectFunction<OrderEvent, OrderResult> {

        @Override
        public OrderResult select( Map<String, List<OrderEvent>> pattern ) throws Exception {
            Long payedOrderId = pattern.get("pay").iterator().next().getOrderId();
            return new OrderResult(payedOrderId, "payed");
        }
    }
}
