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

package org.apache.flink.cep.dynamic.impl.json.spec;

import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.WithinType;
import org.apache.flink.streaming.api.windowing.time.Time;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/** This class is to (de)serialize WindowTime of a {@link Pattern} in json format. */
public class WindowSpec {
    private final WithinType type;

    private final Time time;

    public WindowSpec(@JsonProperty("type") WithinType type, @JsonProperty("time") Time time) {
        this.type = type;
        this.time = time;
    }

    public static WindowSpec fromWindowTime(Map<WithinType, Time> window) {
        if (window.containsKey(WithinType.FIRST_AND_LAST)) {
            return new WindowSpec(WithinType.FIRST_AND_LAST, window.get(WithinType.FIRST_AND_LAST));
        } else {
            return new WindowSpec(
                    WithinType.PREVIOUS_AND_CURRENT, window.get(WithinType.FIRST_AND_LAST));
        }
    }

    public Time getTime() {
        return time;
    }

    public WithinType getType() {
        return type;
    }
}
