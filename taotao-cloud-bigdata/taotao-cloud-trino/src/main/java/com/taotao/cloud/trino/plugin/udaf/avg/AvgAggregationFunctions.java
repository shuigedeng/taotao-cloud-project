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

package com.taotao.cloud.trino.plugin.udaf.avg;

import static io.trino.spi.type.DoubleType.DOUBLE;

import io.trino.spi.block.BlockBuilder;
import io.trino.spi.function.AggregationFunction;
import io.trino.spi.function.AggregationState;
import io.trino.spi.function.CombineFunction;
import io.trino.spi.function.Description;
import io.trino.spi.function.InputFunction;
import io.trino.spi.function.OutputFunction;
import io.trino.spi.function.SqlType;
import io.trino.spi.type.StandardTypes;

/**
 * TaoTaoCloudAvgAggregationFunctions
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2021/1/25 下午3:35
 */
@AggregationFunction("my_avg")
@Description("聚合求平均值函数")
public class AvgAggregationFunctions {

    @InputFunction
    public static void input(
            @AggregationState LongAndDoubleState state, @SqlType(StandardTypes.BIGINT) long value) {
        state.setLong(state.getLong() + 1);
        state.setDouble(state.getDouble() + value);
    }

    @InputFunction
    public static void input(
            @AggregationState LongAndDoubleState state,
            @SqlType(StandardTypes.DOUBLE) double value) {
        state.setLong(state.getLong() + 1);
        state.setDouble(state.getDouble() + value);
    }

    @CombineFunction
    public static void combine(
            @AggregationState LongAndDoubleState state,
            @AggregationState LongAndDoubleState otherState) {
        state.setLong(state.getLong() + otherState.getLong());
        state.setDouble(state.getDouble() + otherState.getDouble());
    }

    @OutputFunction(StandardTypes.DOUBLE)
    public static void output(@AggregationState LongAndDoubleState state, BlockBuilder out) {
        long count = state.getLong();
        if (count == 0) {
            out.appendNull();
        } else {
            double value = state.getDouble();
            double tmp = PrecisionUtil.getPrecision(value / count);
            DOUBLE.writeDouble(out, tmp);
        }
    }
}
