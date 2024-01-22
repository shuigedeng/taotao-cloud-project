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

import org.apache.flink.cep.pattern.conditions.IterativeCondition;

import java.util.HashMap;
import java.util.Map;

/** The type of {@link IterativeCondition } for dynamic cep. */
public enum ConditionType {
    CLASS("CLASS"),
    AVIATOR("AVIATOR");

    private final String type;
    private static final Map<String, ConditionType> TYPE_MAP;

    ConditionType(String type) {
        this.type = type;
    }

    static {
        TYPE_MAP = new HashMap<>();
        for (ConditionType instance : ConditionType.values()) {
            TYPE_MAP.put(instance.type, instance);
        }
    }

    public static ConditionType get(String type) {
        return TYPE_MAP.get(type);
    }
}
