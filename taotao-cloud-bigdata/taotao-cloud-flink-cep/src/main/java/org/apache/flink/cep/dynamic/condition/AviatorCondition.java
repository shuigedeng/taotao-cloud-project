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

package org.apache.flink.cep.dynamic.condition;

import org.apache.flink.annotation.Internal;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.util.StringUtils;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import javax.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/** Condition that accepts aviator expression. */
@Internal
public class AviatorCondition<T> extends SimpleCondition<T> {

    private static final long serialVersionUID = 1L;

    /** The filter expression of the condition. */
    private final String expression;

    private transient Expression compiledExpression;

    public AviatorCondition(String expression) {
        this(expression, null);
    }

    public AviatorCondition(String expression, @Nullable String filterField) {
        this.expression =
                StringUtils.isNullOrWhitespaceOnly(filterField)
                        ? requireNonNull(expression)
                        : filterField + requireNonNull(expression);
        checkExpression(this.expression);
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public boolean filter(T eventBean) throws Exception {
        if (compiledExpression == null) {
            // Compile the expression when it is null to allow static CEP to use AviatorCondition.
            compiledExpression = AviatorEvaluator.compile(expression, false);
        }
        try {
            List<String> variableNames = compiledExpression.getVariableNames();
            if (variableNames.isEmpty()) {
                return true;
            }

            Map<String, Object> variables = new HashMap<>();
            for (String variableName : variableNames) {
                Object variableValue = getVariableValue(eventBean, variableName);
                if (!Objects.isNull(variableValue)) {
                    variables.put(variableName, variableValue);
                }
            }
            return (Boolean) compiledExpression.execute(variables);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            // If we find that some fields reside in the expression but does not appear in the
            // eventBean, we directly return false. Because we would consider the existence of the
            // field is an implicit condition (i.e. AviatorCondition("a > 10") is equivalent to
            // AviatorCondition("a exists && a > 10").
            return false;
        }
    }

    private void checkExpression(String expression) {
        try {
            AviatorEvaluator.validate(expression);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "The expression of AviatorCondition is invalid: " + e.getMessage());
        }
    }

    private Object getVariableValue(T propertyBean, String variableName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = propertyBean.getClass().getDeclaredField(variableName);
        field.setAccessible(true);
        return field.get(propertyBean);
    }
}
