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
import io.trino.spi.type.StandardTypes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算漏斗深度(用户深度)
 * <p>
 * taotao_cloud_funnel(ctime, 86400*1000*3, event, 'SingUp,AppPageView,AppClick,NewsAction')
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2021/1/25 下午3:52
 */
@AggregationFunction("taotao_cloud_funnel")
public class FunnelAggregationsFunctions extends FunnelBase {

    // 表示放2个int
    private static final int COUNT_FLAG_LENGTH = 8;

    // 每个事件所占的位数 表示要存入的时间戳和下标
    private static final int COUNT_PER_LENGTH = 5;

    @InputFunction
    public static void input(
            @AggregationState FunnelSliceState state,
            // 事件发生时间
            @SqlType(StandardTypes.BIGINT) long ctime,
            // 窗口期
            @SqlType(StandardTypes.BIGINT) long windows,
            // 事件名称
            @SqlType(StandardTypes.VARCHAR) Slice event,
            // 所有事件,中间用逗号分割
            @SqlType(StandardTypes.VARCHAR) Slice events) {

        if (!event_pos_dict.containsKey(events)) {
            initEvents(events);
        }

        Slice slice = state.getSlice();
        if (null == slice) {
            // 需要分配空间
            // 设计空间大小 窗口大小[4byte] 事件个数[4byte] 事件时间[4byte] 事件位置[1byte]
            slice = Slices.allocate(COUNT_FLAG_LENGTH + COUNT_PER_LENGTH);
            slice.setInt(0, (int) windows);
            slice.setInt(4, event_pos_dict.get(events).size());
            slice.setInt(COUNT_PER_LENGTH, (int) ctime);
            slice.setByte(COUNT_PER_LENGTH + 4, event_pos_dict.get(events).get(event));
            state.setSlice(slice);
        } else {
            // 之前不为空 追加数据
            int sliceLength = slice.length();
            Slice newSlice = Slices.allocate(sliceLength + COUNT_PER_LENGTH);
            newSlice.setBytes(0, slice.getBytes());
            newSlice.setInt(sliceLength, (int) ctime);
            newSlice.setByte(sliceLength + 4, event_pos_dict.get(events).get(event));
            state.setSlice(newSlice);
        }
    }

    @CombineFunction
    public static void combine(
            @AggregationState FunnelSliceState state1, @AggregationState FunnelSliceState state2) {
        Slice slice1 = state1.getSlice();
        Slice slice2 = state2.getSlice();
        if (null == slice1) {
            state1.setSlice(slice2);
        } else {
            int length1 = slice1.length();
            int length2 = slice2.length();
            Slice slice = Slices.allocate(length1 + length2 - COUNT_FLAG_LENGTH);
            slice.setBytes(0, slice1.getBytes());
            slice.setBytes(
                    length1, slice2.getBytes(), COUNT_FLAG_LENGTH, length2 - COUNT_FLAG_LENGTH);
            state1.setSlice(slice);
        }
    }

    @OutputFunction(StandardTypes.INTEGER)
    public static void output(@AggregationState FunnelSliceState state, BlockBuilder out) {
        Slice slice = state.getSlice();
        if (null == slice) {
            out.writeInt(0);
            out.closeEntry();
            return;
        }

        boolean flag = false;
        List<Integer> timeArray = new ArrayList<>();
        Map<Integer, Byte> timeEventMap = new HashMap<>();

        for (int index = COUNT_FLAG_LENGTH; index < slice.length(); index += COUNT_PER_LENGTH) {
            int timestamp = slice.getInt(index);
            byte event = slice.getByte(index + 4);

            if ((!flag) && event == 0) {
                flag = true;
            }

            timeArray.add(timestamp);
            timeEventMap.put(timestamp, event);
        }

        if (!flag) {
            out.writeInt(0);
            out.closeEntry();
            return;
        }

        Collections.sort(timeArray);

        int windows = slice.getInt(0);
        int eventCount = slice.getInt(4);

        int maxEventIndex = 0;
        List<int[]> temp = new ArrayList<>();

        for (Integer timestamp : timeArray) {
            Byte event = timeEventMap.get(timestamp);
            if (0 == event) {
                int[] tempTimestampEvent = {timestamp, event};
                temp.add(tempTimestampEvent);
            } else {
                for (int i = temp.size() - 1; i >= 0; --i) {
                    int[] flags = temp.get(i);

                    if ((timestamp - flags[0]) >= windows) {
                        break;
                    } else if (event == (flags[1] + 1)) {
                        flags[1] = event;
                        if (maxEventIndex < event) {
                            maxEventIndex = event;
                        }
                        break;
                    }
                }

                if ((maxEventIndex + 1) == eventCount) {
                    break;
                }
            }
        }
        out.writeInt(maxEventIndex + 1);
        out.closeEntry();
    }
}
