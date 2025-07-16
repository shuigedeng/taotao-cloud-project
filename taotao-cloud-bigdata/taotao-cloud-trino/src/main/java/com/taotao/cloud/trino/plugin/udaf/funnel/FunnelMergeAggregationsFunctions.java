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

package com.taotao.cloud.trino.plugin.udaf.funnel;

import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import io.trino.spi.block.BlockBuilder;
import io.trino.spi.function.AggregationFunction;
import io.trino.spi.function.AggregationState;
import io.trino.spi.function.CombineFunction;
import io.trino.spi.function.InputFunction;
import io.trino.spi.function.OutputFunction;
import io.trino.spi.function.SqlType;
import io.trino.spi.type.BigintType;
import io.trino.spi.type.StandardTypes;

/**
 * 漏斗深度聚合
 * <p>
 * taotao_cloud_funnel_merge(user_depth, 4)
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2021/1/25 下午3:52
 */
@AggregationFunction("taotao_cloud_funnel_merge")
public class FunnelMergeAggregationsFunctions {

    @InputFunction
    public static void input(
            @AggregationState FunnelSliceState state,
            // 漏斗深度
            @SqlType(StandardTypes.INTEGER) long userState,
            // 事件个数
            @SqlType(StandardTypes.INTEGER) long eventsCount) {

        Slice slice = state.getSlice();
        if (null == slice) {
            slice = Slices.allocate((int) eventsCount + 4);
        }

        for (int status = 0; status < userState; ++status) {
            int index = status + 4;
            slice.setInt(index, slice.getInt(status) + 1);
        }

        state.setSlice(slice);
    }

    @CombineFunction
    public static void combine(
            @AggregationState FunnelSliceState state1, @AggregationState FunnelSliceState state2) {
        Slice slice1 = state1.getSlice();
        Slice slice2 = state2.getSlice();

        if (null == slice1) {
            state1.setSlice(slice2);
        } else {
            for (int index = 0; index < slice1.length(); index += 4) {
                slice1.setInt(index, slice1.getInt(index) + slice2.getInt(index));
            }
            state1.setSlice(slice1);
        }
    }

    @OutputFunction("array<bigint>")
    public static void output(@AggregationState FunnelSliceState state, BlockBuilder out) {
        Slice slice = state.getSlice();
        if (null == slice) {
            out.closeEntry();
            return;
        }

        for (int index = 0; index < slice.length(); index += 4) {
            BigintType.BIGINT.writeLong(out.beginBlockEntry(), slice.getInt(index));
        }

        out.closeEntry();
    }
}
