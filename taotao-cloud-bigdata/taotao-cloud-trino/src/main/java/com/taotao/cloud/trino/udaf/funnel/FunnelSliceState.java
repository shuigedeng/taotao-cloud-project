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

package com.taotao.cloud.trino.udaf.funnel;

import io.airlift.slice.Slice;
import io.trino.spi.block.BlockBuilder;
import io.trino.spi.function.AccumulatorState;
import io.trino.spi.type.Type;

/**
 * FunnelSliceState
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2021/1/25 下午3:57
 */
public interface FunnelSliceState extends AccumulatorState {

    Slice getSlice();

    void setSlice(Slice value);

    static void write(Type type, FunnelSliceState sliceState, BlockBuilder out) {
        if (sliceState.getSlice() == null) {
            out.appendNull();
        } else {
            type.writeSlice(out, sliceState.getSlice());
        }
    }
}
