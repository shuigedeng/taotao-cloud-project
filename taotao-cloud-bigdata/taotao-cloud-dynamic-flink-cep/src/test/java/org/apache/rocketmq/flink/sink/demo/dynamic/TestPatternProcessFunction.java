/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.flink.sink.demo.dynamic;

import com.alibaba.ververica.cep.demo.event.Event;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.util.Collector;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/** Test PatternProcessFunction for {@link TestPatternProcessor}. */
public class TestPatternProcessFunction<IN> extends PatternProcessFunction<IN, String>
        implements Serializable {

    private static final long serialVersionUID = 1L;

    public void processMatch(
            final Map<String, List<IN>> match, final Context ctx, final Collector<String> out) {
        StringBuilder sb = new StringBuilder();

        sb.append(((Event) match.get("start").get(0)).getName())
                .append(",")
                .append(((Event) match.get("middle").get(0)).getName())
                .append(",")
                .append(((Event) match.get("end").get(0)).getName());
        out.collect(sb.toString());
    }
}
