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

package com.taotao.cloud.flink.cep;

import static com.taotao.cloud.flink.cep.Constants.*;

import com.taotao.cloud.flink.cep.dynamic.JDBCPeriodicPatternProcessorDiscovererFactory;
import com.taotao.cloud.flink.cep.event.Event;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.MultipleParameterTool;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.TimeBehaviour;
import org.apache.flink.cep.dynamic.impl.json.util.CepJsonUtils;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JacksonException;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * CepDemo
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class CepDemo {

    public static void printTestPattern( Pattern<?, ?> pattern ) throws JacksonException {
        System.out.println(CepJsonUtils.convertPatternToJSONString(pattern));
    }

    public static void checkArg( String argName, MultipleParameterTool params ) {
        if (!params.has(argName)) {
            throw new IllegalArgumentException(argName + " must be set!");
        }
    }

    public static void main( String[] args ) throws Exception {
        // Process args
        //		final MultipleParameterTool params = MultipleParameterTool.fromArgs(args);

        //		checkArg(KAFKA_BROKERS_ARG, params);
        //		checkArg(INPUT_TOPIC_ARG, params);
        //		checkArg(INPUT_TOPIC_GROUP_ARG, params);
        //		checkArg(JDBC_URL_ARG, params);
        //		checkArg(TABLE_NAME_ARG, params);
        //		checkArg(JDBC_INTERVAL_MILLIS_ARG, params);
        String jdbc_url = "jdbc:mysql://127.0.0.1:3306/flink_cep?user=root&password=123456";
        String table_name = "rds_demo";
        String jdbcIntervalMs = "3000";

        // Set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Build Kafka source with new Source API based on FLIP-27
        //		KafkaSource<Event> kafkaSource =
        //			KafkaSource.<Event>builder()
        //				.setBootstrapServers(params.get(KAFKA_BROKERS_ARG))
        //				.setTopics(params.get(INPUT_TOPIC_ARG))
        //				.setStartingOffsets(OffsetsInitializer.latest())
        //				.setGroupId(params.get(INPUT_TOPIC_GROUP_ARG))
        //				.setDeserializer(new EventDeSerializationSchema())
        //				.build();
        //		// DataStream Source
        //		DataStreamSource<Event> source =
        //			env.fromSource(
        //				kafkaSource,
        //				WatermarkStrategy.<Event>forMonotonousTimestamps()
        //					.withTimestampAssigner((event, ts) -> event.getEventTime()),
        //				"Kafka Source");

        DataStream<String> socketTextStream = env.socketTextStream("127.0.0.1", 8888);
        SingleOutputStreamOperator<Event> source =
                socketTextStream.map(
                        new RichMapFunction<String, Event>() {
                            @Override
                            public Event map( String value ) throws Exception {
                                // private final int id;
                                // private final String name;
                                // private final int productionId;
                                // private final int action;
                                // private final long eventTime;
                                // 1,aa,1,1,1000
                                // 1,aa,1,0,1000

                                String[] split = value.split(",");
                                return new Event(
                                        Integer.valueOf(split[0]),
                                        split[1],
                                        Integer.valueOf(split[2]),
                                        Integer.valueOf(split[3]),
                                        Integer.valueOf(split[4]));
                            }
                        });

        // keyBy userId and productionId
        // Notes, only events with the same key will be processd to see if there is a match
        KeyedStream<Event, Tuple2<Integer, Integer>> keyedStream =
                source.keyBy(
                        new KeySelector<Event, Tuple2<Integer, Integer>>() {

                            @Override
                            public Tuple2<Integer, Integer> getKey( Event value ) throws Exception {
                                return Tuple2.of(value.getId(), value.getProductionId());
                            }
                        });

        // show how to print test pattern in json format
        //		Pattern<Event, Event> pattern =
        //			Pattern.<Event>begin("start", AfterMatchSkipStrategy.skipPastLastEvent())
        //				.where(new StartCondition("action == 0"))
        //				.timesOrMore(3)
        ////				.followedBy("middle")
        ////				.where(
        ////					new CustomMiddleCondition(
        ////						new String[]{"eventArgs.detail.price > 10000", "A"}))
        //				.followedBy("end")
        //				.where(new EndCondition());
        //		printTestPattern(pattern);

        // Dynamic CEP patterns
        SingleOutputStreamOperator<String> output =
                CEP.dynamicPatterns(
                        keyedStream,
                        new JDBCPeriodicPatternProcessorDiscovererFactory<>(
                                jdbc_url,
                                JDBC_DRIVE,
                                table_name,
                                null,
                                Long.parseLong(jdbcIntervalMs)),
                        TimeBehaviour.ProcessingTime,
                        TypeInformation.of(new TypeHint<String>() {
                        }));
        // Print output stream in taskmanager's stdout
        output.print();

        // Compile and submit the job
        env.execute("CEPDemo");
    }
}
