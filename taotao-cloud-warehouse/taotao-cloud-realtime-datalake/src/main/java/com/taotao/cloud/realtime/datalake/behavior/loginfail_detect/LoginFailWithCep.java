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

package com.taotao.cloud.realtime.datalake.behavior.loginfail_detect;

import java.net.URL;
import java.util.List;
import java.util.Map;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class LoginFailWithCep {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // 1. 从文件中读取数据
        URL resource = LoginFailWithCep.class.getResource("/LoginLog.csv");
        DataStream<LoginEvent> loginEventStream =
                env.readTextFile(resource.getPath())
                        .map(
                                line -> {
                                    String[] fields = line.split(",");
                                    return new LoginEvent(
                                            new Long(fields[0]),
                                            fields[1],
                                            fields[2],
                                            new Long(fields[3]));
                                })
                        .assignTimestampsAndWatermarks(
                                new BoundedOutOfOrdernessTimestampExtractor<LoginEvent>(
                                        Time.seconds(3)) {
                                    @Override
                                    public long extractTimestamp(LoginEvent element) {
                                        return element.getTimestamp() * 1000L;
                                    }
                                });

        // 1. 定义一个匹配模式
        // firstFail -> secondFail, within 2s
        Pattern<LoginEvent, LoginEvent> loginFailPattern0 =
                Pattern.<LoginEvent>begin("firstFail")
                        .where(
                                new SimpleCondition<LoginEvent>() {
                                    @Override
                                    public boolean filter(LoginEvent value) throws Exception {
                                        return "fail".equals(value.getLoginState());
                                    }
                                })
                        .next("secondFail")
                        .where(
                                new SimpleCondition<LoginEvent>() {
                                    @Override
                                    public boolean filter(LoginEvent value) throws Exception {
                                        return "fail".equals(value.getLoginState());
                                    }
                                })
                        .next("thirdFail")
                        .where(
                                new SimpleCondition<LoginEvent>() {
                                    @Override
                                    public boolean filter(LoginEvent value) throws Exception {
                                        return "fail".equals(value.getLoginState());
                                    }
                                })
                        .within(Time.seconds(3));

        Pattern<LoginEvent, LoginEvent> loginFailPattern =
                Pattern.<LoginEvent>begin("failEvents")
                        .where(
                                new SimpleCondition<LoginEvent>() {
                                    @Override
                                    public boolean filter(LoginEvent value) throws Exception {
                                        return "fail".equals(value.getLoginState());
                                    }
                                })
                        .times(3)
                        .consecutive()
                        .within(Time.seconds(5));

        // 2. 将匹配模式应用到数据流上，得到一个pattern stream
        PatternStream<LoginEvent> patternStream =
                CEP.pattern(loginEventStream.keyBy(LoginEvent::getUserId), loginFailPattern);

        // 3. 检出符合匹配条件的复杂事件，进行转换处理，得到报警信息
        SingleOutputStreamOperator<LoginFailWarning> warningStream =
                patternStream.select(new LoginFailMatchDetectWarning());

        warningStream.print();

        env.execute("login fail detect with cep job");
    }

    // 实现自定义的PatternSelectFunction
    public static class LoginFailMatchDetectWarning
            implements PatternSelectFunction<LoginEvent, LoginFailWarning> {
        @Override
        public LoginFailWarning select(Map<String, List<LoginEvent>> pattern) throws Exception {
            //            LoginEvent firstFailEvent = pattern.get("firstFail").iterator().next();
            //            LoginEvent lastFailEvent = pattern.get("thirdFail").get(0);
            //            return new LoginFailWarning(firstFailEvent.getUserId(),
            // firstFailEvent.getTimestamp(), lastFailEvent.getTimestamp(), "login fail 2 times");
            LoginEvent firstFailEvent = pattern.get("failEvents").get(0);
            LoginEvent lastFailEvent =
                    pattern.get("failEvents").get(pattern.get("failEvents").size() - 1);
            return new LoginFailWarning(
                    firstFailEvent.getUserId(),
                    firstFailEvent.getTimestamp(),
                    lastFailEvent.getTimestamp(),
                    "login fail " + pattern.get("failEvents").size() + " times");
        }
    }
}
