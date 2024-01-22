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

import org.apache.flink.cep.dynamic.condition.AviatorCondition;
import org.apache.flink.cep.dynamic.condition.CustomArgsCondition;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.RichAndCondition;
import org.apache.flink.cep.pattern.conditions.RichCompositeIterativeCondition;
import org.apache.flink.cep.pattern.conditions.RichNotCondition;
import org.apache.flink.cep.pattern.conditions.RichOrCondition;
import org.apache.flink.cep.pattern.conditions.SubtypeCondition;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * The util class to (de)serialize {@link IterativeCondition } of a specific class in json format.
 */
public abstract class ConditionSpec {
    private final ConditionType type;

    ConditionSpec(@JsonProperty("type") ConditionType type) {
        this.type = type;
    }

    // TODO: rethink how to make adding a custom condition easier and cleaner
    public static ConditionSpec fromCondition(IterativeCondition<?> condition) {
        if (condition instanceof SubtypeCondition) {
            return new SubTypeConditionSpec(condition);
        } else if (condition instanceof AviatorCondition) {
            return new AviatorConditionSpec(((AviatorCondition<?>) condition).getExpression());
        } else if (condition instanceof CustomArgsCondition) {
            return new CustomArgsConditionSpec((CustomArgsCondition<?>) condition);
        } else if (condition instanceof RichCompositeIterativeCondition) {
            IterativeCondition<?>[] nestedConditions =
                    ((RichCompositeIterativeCondition<?>) condition).getNestedConditions();
            if (condition instanceof RichOrCondition) {
                List<ConditionSpec> iterativeConditionSpecs = new ArrayList<>();
                for (IterativeCondition<?> iterativeCondition : nestedConditions) {
                    iterativeConditionSpecs.add(fromCondition(iterativeCondition));
                }
                return new RichOrConditionSpec(iterativeConditionSpecs);
            } else if (condition instanceof RichAndCondition) {
                List<ConditionSpec> iterativeConditionSpecs = new ArrayList<>();
                for (IterativeCondition<?> iterativeCondition : nestedConditions) {
                    iterativeConditionSpecs.add(fromCondition(iterativeCondition));
                }
                return new RichAndConditionSpec(iterativeConditionSpecs);
            } else if (condition instanceof RichNotCondition) {
                List<ConditionSpec> iterativeConditionSpecs = new ArrayList<>();
                for (IterativeCondition<?> iterativeCondition : nestedConditions) {
                    iterativeConditionSpecs.add(fromCondition(iterativeCondition));
                }
                return new RichNotConditionSpec(iterativeConditionSpecs);
            }
        }
        return new ClassConditionSpec(condition);
    }

    public abstract IterativeCondition toIterativeCondition(ClassLoader classLoader)
            throws Exception;

    public ConditionType getType() {
        return type;
    }
}
