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

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The util class to (de)serialize {@link IterativeCondition } of a specific class in json format.
 */
public class ClassConditionSpec extends ConditionSpec {
    private final String className;

    public ClassConditionSpec(@JsonProperty("className") String className) {
        super(ConditionType.CLASS);
        this.className = className;
    }

    public ClassConditionSpec(IterativeCondition<?> condition) {
        super(ConditionType.CLASS);
        this.className = condition.getClass().getCanonicalName();
    }

    @Override
    public IterativeCondition<?> toIterativeCondition(ClassLoader classLoader) throws Exception {

        if (this.className.equals("null")) {
            throw new IllegalAccessException(
                    "It is not supported to save/load a local or anonymous class as a IterativeCondition for dynamic patterns.");
        }
        return (IterativeCondition<?>) classLoader.loadClass(this.getClassName()).newInstance();
    }

    public String getClassName() {
        return className;
    }
}
