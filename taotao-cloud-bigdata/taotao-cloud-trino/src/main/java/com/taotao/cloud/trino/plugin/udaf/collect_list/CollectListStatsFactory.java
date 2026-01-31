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

package com.taotao.cloud.trino.plugin.udaf.collect_list;

import io.trino.array.ObjectBigArray;
import io.trino.spi.function.AccumulatorStateFactory;
import io.trino.spi.function.GroupedAccumulatorState;

/**
 * CollectListStatsFactory
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 18:06
 */
public class CollectListStatsFactory
        implements AccumulatorStateFactory<CollectListAggregationFunctions.CollectState> {

    @Override
    public CollectListAggregationFunctions.CollectState createSingleState() {
        return new SingleState();
    }

    // @Override
    public Class<? extends CollectListAggregationFunctions.CollectState> getSingleStateClass() {
        return SingleState.class;
    }

    @Override
    public CollectListAggregationFunctions.CollectState createGroupedState() {
        return new GroupState();
    }

    // @Override
    public Class<? extends CollectListAggregationFunctions.CollectState> getGroupedStateClass() {
        return GroupState.class;
    }

    /**
     * GroupState
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    public static class GroupState implements GroupedAccumulatorState, CollectListAggregationFunctions.CollectState {

        private final ObjectBigArray<CollectListStats> collectStatsList = new ObjectBigArray<>();
        private int size;
        private int groupId;

        @Override
        public void setGroupId( int groupId ) {
            this.groupId = groupId;
        }

        @Override
        public void ensureCapacity( int size ) {
            collectStatsList.ensureCapacity(size);
        }

        @Override
        public CollectListStats get() {
            return collectStatsList.get(groupId);
        }

        @Override
        public void set( CollectListStats value ) {
            CollectListStats previous = get();
            if (previous != null) {
                size -= previous.estimatedInMemorySize();
            }
            // collectStatsList.set(groupId, value);
            size += value.estimatedInMemorySize();
        }

        @Override
        public long getEstimatedSize() {
            return size + collectStatsList.sizeOf();
        }
    }

    /**
     * SingleState
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    public static class SingleState implements CollectListAggregationFunctions.CollectState {

        private CollectListStats stats;

        @Override
        public CollectListStats get() {
            return stats;
        }

        @Override
        public void set( CollectListStats value ) {
            stats = value;
        }

        @Override
        public long getEstimatedSize() {
            if (stats == null) {
                return 0;
            }
            return stats.estimatedInMemorySize();
        }
    }
}
