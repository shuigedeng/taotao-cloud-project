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

package com.taotao.cloud.realtime.datalake.behavior.market_analysis;

import com.taotao.cloud.realtime.behavior.analysis.market_analysis.AppMarketingByChannel;
import com.taotao.cloud.realtime.behavior.analysis.market_analysis.beans.ChannelPromotionCount;
import com.taotao.cloud.realtime.behavior.analysis.market_analysis.beans.MarketingUserBehavior;

import java.sql.Timestamp;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * AppMarketingStatistics
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class AppMarketingStatistics {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // 1. 从自定义数据源中读取数据
        DataStream<MarketingUserBehavior> dataStream =
                env.addSource(new AppMarketingByChannel.SimulatedMarketingUserBehaviorSource())
                        .assignTimestampsAndWatermarks(
                                new AscendingTimestampExtractor<MarketingUserBehavior>() {
                                    @Override
                                    public long extractAscendingTimestamp(
                                            MarketingUserBehavior element ) {
                                        return element.getTimestamp();
                                    }
                                });

        // 2. 开窗统计总量
        SingleOutputStreamOperator<ChannelPromotionCount> resultStream =
                dataStream
                        .filter(data -> !"UNINSTALL".equals(data.getBehavior()))
                        .map(
                                new MapFunction<MarketingUserBehavior, Tuple2<String, Long>>() {
                                    @Override
                                    public Tuple2<String, Long> map( MarketingUserBehavior value )
                                            throws Exception {
                                        return new Tuple2<>("total", 1L);
                                    }
                                })
                        .keyBy(0)
                        .timeWindow(Time.hours(1), Time.seconds(5)) // 定义滑窗
                        .aggregate(new MarketingStatisticsAgg(), new MarketingStatisticsResult());

        resultStream.print();

        env.execute("app marketing by channel job");
    }

    public static class MarketingStatisticsAgg
            implements AggregateFunction<Tuple2<String, Long>, Long, Long> {

        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add( Tuple2<String, Long> value, Long accumulator ) {
            return accumulator + 1;
        }

        @Override
        public Long getResult( Long accumulator ) {
            return accumulator;
        }

        @Override
        public Long merge( Long a, Long b ) {
            return a + b;
        }
    }

    public static class MarketingStatisticsResult
            implements WindowFunction<Long, ChannelPromotionCount, Tuple, TimeWindow> {

        @Override
        public void apply(
                Tuple tuple,
                TimeWindow window,
                Iterable<Long> input,
                Collector<ChannelPromotionCount> out )
                throws Exception {
            String windowEnd = new Timestamp(window.getEnd()).toString();
            Long count = input.iterator().next();

            out.collect(new ChannelPromotionCount("total", "total", windowEnd, count));
        }
    }
}
