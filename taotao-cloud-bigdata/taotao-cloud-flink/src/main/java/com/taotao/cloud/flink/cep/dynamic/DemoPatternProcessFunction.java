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

package com.taotao.cloud.flink.cep.dynamic;

import java.util.List;
import java.util.Map;

import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.util.Collector;

/**
 * DemoPatternProcessFunction
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class DemoPatternProcessFunction<IN> extends PatternProcessFunction<IN, String> {

    String id;
    int version;

    public DemoPatternProcessFunction( String id, int version ) {
        this.id = id;
        this.version = version;
    }

    @Override
    public void processMatch(
            final Map<String, List<IN>> match, final Context ctx, final Collector<String> out ) {
        StringBuilder sb = new StringBuilder();
        sb.append("A match for Pattern of (id, version): (")
                .append(id)
                .append(", ")
                .append(version)
                .append(") is found. The event sequence: ");
        for (Map.Entry<String, List<IN>> entry : match.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue());
        }
        out.collect(sb.toString());
    }
}
