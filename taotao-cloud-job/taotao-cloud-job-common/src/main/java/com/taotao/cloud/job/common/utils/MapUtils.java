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

package com.taotao.cloud.job.common.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;

/**
 * MapUtils
 *
 * @author shuigedeng
 * @since 2024/2/24
 */
public class MapUtils {

    public static <K> Long getLong(Map<? super K, ?> map, K key, Long defaultValue) {
        Long answer = getLong(map, key);
        if (answer == null) {
            answer = defaultValue;
        }

        return answer;
    }

    public static <K> long getLongValue(Map<? super K, ?> map, K key) {
        Long longObject = getLong(map, key);
        return longObject == null ? 0L : longObject;
    }

    public static <K> Long getLong(Map<? super K, ?> map, K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else {
            return answer instanceof Long ? (Long) answer : answer.longValue();
        }
    }

    public static <K> Number getNumber(Map<? super K, ?> map, K key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Number) {
                    return (Number) answer;
                }

                if (answer instanceof String) {
                    try {
                        String text = (String) answer;
                        return NumberFormat.getInstance().parse(text);
                    } catch (ParseException var4) {
                    }
                }
            }
        }

        return null;
    }
}
