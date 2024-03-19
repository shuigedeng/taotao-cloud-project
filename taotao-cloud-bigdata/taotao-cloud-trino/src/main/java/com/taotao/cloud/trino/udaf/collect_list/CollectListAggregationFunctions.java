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

package com.taotao.cloud.trino.udaf.collect_list;

import io.airlift.slice.Slice;
import io.trino.spi.block.BlockBuilder;
import io.trino.spi.function.AccumulatorState;
import io.trino.spi.function.AccumulatorStateMetadata;
import io.trino.spi.function.AggregationState;
import io.trino.spi.function.CombineFunction;
import io.trino.spi.function.InputFunction;
import io.trino.spi.function.OutputFunction;
import io.trino.spi.function.SqlType;
import io.trino.spi.type.StandardTypes;
import io.trino.spi.type.VarcharType;

/**
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 17:52
 */
public class CollectListAggregationFunctions {

    @AccumulatorStateMetadata(
            stateSerializerClass = CollectListStatsSerializer.class,
            stateFactoryClass = CollectListStatsFactory.class)
    public interface CollectState extends AccumulatorState {

        CollectListStats get();

        void set(CollectListStats value);
    }

    @InputFunction
    public static void input(
            @AggregationState CollectState state,
            @SqlType(StandardTypes.VARCHAR) Slice id,
            @SqlType(StandardTypes.VARCHAR) Slice key) {
        try {
            CollectListStats stats = state.get();
            if (stats == null) {
                stats = new CollectListStats();
                state.set(stats);
            }
            int inputId = Integer.parseInt(id.toStringUtf8());
            String inputKey = key.toStringUtf8();
            stats.addCollectList(inputId, inputKey, 1);
        } catch (Exception e) {
            throw new RuntimeException(e + " ---------  input err");
        }
    }

    @CombineFunction
    public static void combine(@AggregationState CollectState state, CollectState otherState) {
        try {
            CollectListStats collectListStats = state.get();
            CollectListStats oCollectListStats = otherState.get();
            if (collectListStats == null) {
                state.set(oCollectListStats);
            } else {
                collectListStats.mergeWith(oCollectListStats);
            }
        } catch (Exception e) {
            throw new RuntimeException(e + " --------- combine err");
        }
    }

    @OutputFunction(StandardTypes.VARCHAR)
    public static void output(@AggregationState CollectState state, BlockBuilder out) {
        try {
            CollectListStats stats = state.get();
            if (stats == null) {
                out.appendNull();
                return;
            }
            // 统计
            Slice result = stats.getCollectResult();
            if (result == null) {
                out.appendNull();
            } else {
                VarcharType.VARCHAR.writeSlice(out, result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e + " -------- output err");
        }
    }
}
