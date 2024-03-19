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

package com.taotao.cloud.trino.udaf.sum_double;

import io.trino.operator.aggregation.state.NullableDoubleState;
import io.trino.spi.block.BlockBuilder;
import io.trino.spi.function.AggregationFunction;
import io.trino.spi.function.AggregationState;
import io.trino.spi.function.CombineFunction;
import io.trino.spi.function.Description;
import io.trino.spi.function.InputFunction;
import io.trino.spi.function.OutputFunction;
import io.trino.spi.function.SqlType;
import io.trino.spi.type.DoubleType;
import io.trino.spi.type.StandardTypes;

/**
 * 开发聚合函数 TaoTaoCloudSumDoubleAggregationsFunctions
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2021/1/25 下午3:19
 */
@AggregationFunction("my_sum_double")
@Description("这是一个聚合函数")
public class SumDoubleAggregationsFunctions {

    // 输入函数
    @InputFunction
    public static void input(
            // 输入数据状态类型
            @AggregationState NullableDoubleState state,
            // 输入类型
            @SqlType(StandardTypes.DOUBLE) double input) {

        state.setNull(false);
        state.setValue(state.getValue() + input);
    }

    // 聚合函数
    @CombineFunction
    public static void combine(
            // 表示第一条数据 也可以用作中间聚合集合
            @AggregationState NullableDoubleState state1,
            // 表示每次进来的状态数据
            @AggregationState NullableDoubleState state2) {

        if (state1.isNull()) {
            state1.setNull(false);
            state1.setValue(state2.getValue());
            return;
        }

        state1.setValue(state1.getValue() + state2.getValue());
    }

    // 输出函数
    @OutputFunction(StandardTypes.DOUBLE)
    public static void output(
            // 聚合后的结果状态
            @AggregationState NullableDoubleState state,
            // 设置返回结果
            BlockBuilder out) {

        // 将结果以状态返回 每次数据的获取都是状态存储的 最后结果也是已状态返回
        NullableDoubleState.write(DoubleType.DOUBLE, state, out);
    }
}
