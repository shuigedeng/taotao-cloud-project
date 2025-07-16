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

import io.airlift.slice.SizeOf;
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
import io.trino.spi.type.VarcharType;
import java.util.ArrayList;
import java.util.List;

/**
 * RouteUserGroupAggregationFunctions
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 18:15
 */
@AggregationFunction("函数名")
public class RouteUserGroupAggregationFunctions extends RouteUserAggregationBase {

    /**
     * 缓存 Buffer Body 的初始字节容量
     **/
    private static final int STORED_DATA_BODY_INIT_BYTE_SIZE = 64;

    /**
     * 缓存 Buffer 头部元信息定义
     **/
    private static final int VALUES_OFFSET_HEADER_BYTE_LEN = 0;

    private static final int VALUES_OFFSET_BODY_BYTE_SIZE = 4;
    private static final int VALUES_OFFSET_BODY_BYTE_USED = 8;

    private static final int VALUES_OFFSET_CONTAIN_TARGET_EVENT = 12;

    private static final int VALUES_OFFSET_TARGET_EVENT_TYPE = 13;
    private static final int VALUES_OFFSET_ROUTE_INTERVAL = 17;
    private static final int VALUES_OFFSET_TARGET_EVENT_LEN = 21;
    private static final int VALUES_OFFSET_TARGET_EVENT_BYTES = 25;

    @InputFunction
    public static void input(
            SliceState state,
            // 目标事件
            @SqlType(StandardTypes.VARCHAR) Slice targetEvent,
            // 目标事件类型
            @SqlType(StandardTypes.BIGINT) long targetType,
            // 事件间隔
            @SqlType(StandardTypes.BIGINT) long eventInterval,
            // 当前事件名
            @SqlType(StandardTypes.VARCHAR) Slice currEvent,
            // 当前事件时间
            @SqlType(StandardTypes.BIGINT) long eventTime) {

        handleInput(
                state,
                targetEvent,
                (int) targetType,
                (int) eventInterval,
                currEvent,
                (int) eventTime,
                null,
                null);
    }

    private static void handleInput(
            SliceState state,
            Slice targetEvent,
            int targetType,
            int eventInterval,
            Slice currEvent,
            int eventTime,
            Slice groupByEvent,
            Slice groupByProp) {
        // 获取缓存的数据
        Slice storedData = state.getSlice();

        // 初始化缓存的元信息 不会变化的值，如：目标事件，目标类型，时间间隔
        if (storedData == null) {
            /*
              Header byte大小
              Body 总字节大小
              Body 已使用字节大小
              是否包含目标事件
              目标事件类型
              事件时间间隔
            */
            int headerByteLen =
                    SizeOf.SIZE_OF_INT
                            + SizeOf.SIZE_OF_INT
                            + SizeOf.SIZE_OF_INT
                            + SizeOf.SIZE_OF_BYTE
                            + SizeOf.SIZE_OF_INT
                            + SizeOf.SIZE_OF_INT;
            int targetLength = SizeOf.SIZE_OF_INT + targetEvent.length();
            headerByteLen += targetLength;

            storedData = Slices.allocate(headerByteLen + STORED_DATA_BODY_INIT_BYTE_SIZE);
            storedData.setInt(VALUES_OFFSET_HEADER_BYTE_LEN, headerByteLen);
            storedData.setInt(VALUES_OFFSET_BODY_BYTE_SIZE, STORED_DATA_BODY_INIT_BYTE_SIZE);
            storedData.setInt(VALUES_OFFSET_BODY_BYTE_USED, 0);
            // 是否包含目标事件
            storedData.setByte(VALUES_OFFSET_CONTAIN_TARGET_EVENT, 0);
            // 缓存 不变的参数
            storedData.setInt(VALUES_OFFSET_TARGET_EVENT_TYPE, targetType);
            storedData.setInt(VALUES_OFFSET_ROUTE_INTERVAL, eventInterval);

            storedData.setInt(VALUES_OFFSET_TARGET_EVENT_LEN, targetEvent.length());
            storedData.setBytes(VALUES_OFFSET_TARGET_EVENT_BYTES, targetEvent);
        }

        int headerByteLen = storedData.getInt(VALUES_OFFSET_HEADER_BYTE_LEN);
        int bodyByteSize = storedData.getInt(VALUES_OFFSET_BODY_BYTE_SIZE);
        int bodyByteUsed = storedData.getInt(VALUES_OFFSET_BODY_BYTE_USED);

        // 标记包含目标事件
        if (currEvent.toStringUtf8().equals(targetEvent.toStringUtf8())) {
            storedData.setByte(VALUES_OFFSET_CONTAIN_TARGET_EVENT, 1);
        }
        // 直接判断，如果存在分组，判断当前事件就是分组事件，那么直接将分组值和事件拼接在一起
        if (groupByEvent != null && groupByEvent.toStringUtf8().equals(currEvent.toStringUtf8())) {
            // String newEventKey = currEvent.toStringUtf8() + EVENT_CONCAT_GROUP_VALUE +
            // groupByProp.toStringUtf8();
            // currEvent = Slices.utf8Slice(newEventKey);
            currEvent = Slices.utf8Slice("newEventKey");
        }

        // 扩展的长度，eventTime int , current length的int bytes内容
        int entryByteLen = SizeOf.SIZE_OF_INT * 2 + currEvent.length();
        if (bodyByteUsed + entryByteLen > bodyByteSize) {
            // 扩容 byteSize * 2
            int newBodyByteSize = bodyByteSize * 2;
            Slice newStoredData = Slices.allocate(headerByteLen + newBodyByteSize);
            // 将storeData的数据copy到new的Slice中，然后重新设置容量
            newStoredData.setBytes(0, storedData.getBytes());
            newStoredData.setInt(VALUES_OFFSET_BODY_BYTE_SIZE, newBodyByteSize);
            storedData = newStoredData;
        }

        // 写入位置的定位
        int writePos = headerByteLen + bodyByteUsed;
        storedData.setInt(writePos, entryByteLen);
        writePos += SizeOf.SIZE_OF_INT;
        storedData.setInt(writePos, eventTime);
        writePos += SizeOf.SIZE_OF_INT;
        storedData.setBytes(writePos, currEvent);
        storedData.setInt(VALUES_OFFSET_BODY_BYTE_USED, bodyByteUsed + entryByteLen);

        // 更新缓存的数据
        state.setSlice(storedData);
    }

    @CombineFunction
    public static void combine(SliceState state, SliceState other) {
        // 获取缓存的数据
        Slice storedData = state.getSlice();
        Slice otherStoredData = other.getSlice();

        // 合并缓存
        if (storedData == null) {
            state.setSlice(otherStoredData);
        } else {
            int headerByteLen = storedData.getInt(VALUES_OFFSET_HEADER_BYTE_LEN);
            int bodyByteSize = storedData.getInt(VALUES_OFFSET_BODY_BYTE_SIZE);
            int bodyByteUsed = storedData.getInt(VALUES_OFFSET_BODY_BYTE_USED);
            int otherHeaderByteLen = otherStoredData.getInt(VALUES_OFFSET_HEADER_BYTE_LEN);
            int otherBodyByteSize = otherStoredData.getInt(VALUES_OFFSET_BODY_BYTE_SIZE);
            int otherBodyByteUsed = otherStoredData.getInt(VALUES_OFFSET_BODY_BYTE_USED);
            byte containTargetEvent = 0;
            if (storedData.getByte(VALUES_OFFSET_CONTAIN_TARGET_EVENT) == 1
                    || otherStoredData.getByte(VALUES_OFFSET_CONTAIN_TARGET_EVENT) == 1) {
                containTargetEvent = 1;
            }
            Slice finalStoredData;
            int finalBodyByteUsed = bodyByteUsed + otherBodyByteUsed;
            if (bodyByteSize >= finalBodyByteUsed) {
                // 左容量足够  这里只copy header之外的数据，就是当前事件和time
                storedData.setBytes(
                        headerByteLen + bodyByteUsed,
                        otherStoredData,
                        otherHeaderByteLen,
                        otherBodyByteUsed);
                storedData.setInt(VALUES_OFFSET_BODY_BYTE_USED, finalBodyByteUsed);
                finalStoredData = storedData;
            } else if (otherBodyByteSize >= finalBodyByteUsed) {
                // 右容量足够
                otherStoredData.setBytes(
                        otherHeaderByteLen + otherBodyByteUsed,
                        storedData,
                        headerByteLen,
                        bodyByteUsed);
                otherStoredData.setInt(VALUES_OFFSET_BODY_BYTE_USED, finalBodyByteUsed);
                finalStoredData = otherStoredData;
            } else {
                // 扩容
                int newBodyByteSize = bodyByteSize;
                while (newBodyByteSize < finalBodyByteUsed) {
                    newBodyByteSize *= 2;
                }
                Slice newStoredData = Slices.allocate(headerByteLen + newBodyByteSize);
                newStoredData.setBytes(VALUES_OFFSET_HEADER_BYTE_LEN, storedData.getBytes());
                newStoredData.setInt(VALUES_OFFSET_BODY_BYTE_SIZE, newBodyByteSize);
                storedData = newStoredData;

                storedData.setBytes(
                        headerByteLen + bodyByteUsed,
                        otherStoredData,
                        otherHeaderByteLen,
                        otherBodyByteUsed);
                storedData.setInt(VALUES_OFFSET_BODY_BYTE_USED, finalBodyByteUsed);
                finalStoredData = storedData;
            }
            // 是否包含目标事件
            finalStoredData.setByte(VALUES_OFFSET_CONTAIN_TARGET_EVENT, containTargetEvent);
            state.setSlice(finalStoredData);
        }
    }

    @OutputFunction(StandardTypes.VARCHAR)
    public static void output(@AggregationState SliceState state, BlockBuilder out) {
        // 获取缓存数据
        Slice storedData = state.getSlice();

        // 数据为空，或者没有起始事件
        if ((storedData == null) || (storedData.getByte(VALUES_OFFSET_CONTAIN_TARGET_EVENT) == 0)) {
            out.appendNull();
            return;
        }
        // 匹配
        Slice makeRoute = makeRoute(storedData);
        if (makeRoute == null) {
            out.appendNull();
        } else {
            VarcharType.VARCHAR.writeSlice(out, makeRoute);
        }
    }

    private static Slice makeRoute(Slice storedData) {
        // 获取 Header 信息
        int interval = storedData.getInt(VALUES_OFFSET_ROUTE_INTERVAL);
        int targetType = storedData.getInt(VALUES_OFFSET_TARGET_EVENT_TYPE);
        int targetLength = storedData.getInt(VALUES_OFFSET_TARGET_EVENT_LEN);
        String targetEvent =
                new String(storedData.getBytes(VALUES_OFFSET_TARGET_EVENT_BYTES, targetLength));
        List<Slice> timeEventSeries = new ArrayList<>();
        int headerByteLen = storedData.getInt(VALUES_OFFSET_HEADER_BYTE_LEN);
        int bodyByteUsed = storedData.getInt(VALUES_OFFSET_BODY_BYTE_USED);
        int bound = headerByteLen + bodyByteUsed;
        int idx = headerByteLen;
        while (idx < bound) {
            // 获取每个事件数据 time，事件名
            int entryByteLen = storedData.getInt(idx);
            Slice entry =
                    storedData.slice(idx + SizeOf.SIZE_OF_INT, entryByteLen - SizeOf.SIZE_OF_INT);
            idx += entryByteLen;
            timeEventSeries.add(entry);
        }
        // 处理逻辑
        // 构造返回结果
        Slice result = null;
        // if (routes.size() > 0) {
        // 	for (String route : routes) {
        // 		Slice routeSlice = Slices.utf8Slice(route);
        // 		Slice routeInfo = Slices.allocate(SizeOf.SIZE_OF_INT + routeSlice.length());
        // 		routeInfo.setInt(0, routeSlice.length());
        // 		routeInfo.setBytes(4, routeSlice);
        // 		if (result == null) {
        // 			result = routeInfo;
        // 		} else {
        // 			Slice newSlice = Slices.allocate(result.length() + routeInfo.length());
        // 			newSlice.setBytes(0, result);
        // 			newSlice.setBytes(result.length(), routeInfo, 0, routeInfo.length());
        // 			result = newSlice;
        // 		}
        // 	}
        // }
        return result;
    }
}
