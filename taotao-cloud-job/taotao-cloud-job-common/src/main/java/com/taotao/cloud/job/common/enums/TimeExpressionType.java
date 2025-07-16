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

package com.taotao.cloud.job.common.enums;

import com.google.common.collect.Lists;
import com.taotao.cloud.job.remote.protos.MqCausa;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Scheduling time strategies
 *
 * @author shuigedeng
 * @since 2020/3/30
 */
@Getter
@AllArgsConstructor
@ToString
public enum TimeExpressionType {
    CRON(0),
    FIXED_RATE(1),
    FIXED_DELAY(2),

    DAILY_TIME_INTERVAL(3);

    private final int v;

    public static final List<Integer> FREQUENT_TYPES =
            Collections.unmodifiableList(Lists.newArrayList(FIXED_RATE.v, FIXED_DELAY.v));

    /**
     * 首次计算触发时间时必须计算出一个有效值
     */
    public static final List<Integer> INSPECT_TYPES =
            Collections.unmodifiableList(Lists.newArrayList(CRON.v, DAILY_TIME_INTERVAL.v));

    public static HashMap<TimeExpressionType, MqCausa.TimeExpressionType> map1 = new HashMap<>();
    public static HashMap<MqCausa.TimeExpressionType, TimeExpressionType> map2 = new HashMap<>();

    static {
        map1.put(CRON, MqCausa.TimeExpressionType.CRON);
        map2.put(MqCausa.TimeExpressionType.CRON, CRON);
    }

    public static MqCausa.TimeExpressionType getProtoBufTimeExpressionType(
            TimeExpressionType type) {
        return map1.get(type);
    }

    public static TimeExpressionType getTimeExpressionTypeByProtoBuf(
            MqCausa.TimeExpressionType type) {
        return map2.get(type);
    }

    public static TimeExpressionType of(int v) {
        for (TimeExpressionType type : values()) {
            if (type.v == v) {
                return type;
            }
        }
        throw new IllegalArgumentException("unknown TimeExpressionType of " + v);
    }
}
