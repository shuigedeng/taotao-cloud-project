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

package com.taotao.cloud.trino.plugin.udaf.decode_bit_set;

import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import io.trino.spi.block.BlockBuilder;
import io.trino.spi.function.AggregationFunction;
import io.trino.spi.function.AggregationState;
import io.trino.spi.function.CombineFunction;
import io.trino.spi.function.InputFunction;
import io.trino.spi.function.OutputFunction;
import io.trino.spi.function.SqlType;
import io.trino.spi.type.StandardTypes;

/**
 * DecodeBitSetAggregationFunctions
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 17:47
 */
@AggregationFunction("tapdb_bitset_decode")
public class DecodeBitSetAggregationFunctions {

    @InputFunction
    public static void input(
            @AggregationState RouteUserAggregationBase.SliceState state,
            @SqlType(StandardTypes.BIGINT) long mask,
            @SqlType(StandardTypes.BIGINT) long value) {
        if (state.getSlice() == null) {
            long capacity = 30 * Long.BYTES;
            Slice slice = Slices.allocate((int) capacity);
            state.setSlice(slice);
        }

        Slice slice = state.getSlice();
        long retainValue = mask <= 0 ? value ^ mask : value & mask;
        int tmpIndex = 0;
        for (int i = 29; i >= 0; i--) {
            int offset = Long.BYTES * tmpIndex;
            slice.setLong(offset, Long.lowestOneBit(retainValue) == 1 ? 1L : 0L);
            retainValue = retainValue >> 1;
            tmpIndex++;
        }
    }

    @CombineFunction
    public static void combine(
            @AggregationState RouteUserAggregationBase.SliceState state,
            @AggregationState RouteUserAggregationBase.SliceState otherState) {
        Slice otherSlice = otherState.getSlice();
        Slice slice = state.getSlice();

        if (otherSlice == null) {
            return;
        }
        if (slice == null) {
            state.setSlice(otherSlice);
            return;
        }

        int indexNum = slice.length() / Long.BYTES;
        for (int i = 0; i < indexNum; ++i) {
            int offset = i * Long.BYTES;
            slice.setLong(offset, slice.getLong(offset) + otherSlice.getLong(offset) > 0 ? 1L : 0L);
        }
    }

    @OutputFunction("array(bigint)")
    public static void output(
            @AggregationState RouteUserAggregationBase.SliceState state, BlockBuilder out) {
        Slice slice = state.getSlice();
        if (slice == null) {
            out.appendNull();
            return;
        }

        int byteArrayNum = slice.length() / Long.BYTES;
        BlockBuilder blockBuilder = out.buildValueBlock();

        for (int i = 0; i < byteArrayNum; ++i) {
            int offset = i * Long.BYTES;
            long value = slice.getLong(offset);
            blockBuilder.writeLong(value).closeEntry();
        }
        out.closeEntry();
    }
}
