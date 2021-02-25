/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.common.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

/**
 * NumberUtils
 *
 * @author dengtao
 * @date 2020/6/2 16:39
 * @since v1.0
 */
@UtilityClass
public class NumberUtil {
    /**
     * 数字转double
     *
     * @param number number
     * @param scale  scale
     * @return double
     * @author dengtao
     * @date 2020/10/15 15:27
     * @since v1.0
     */
    public double scale(Number number, int scale) {
        BigDecimal bg = BigDecimal.valueOf(number.doubleValue());
        return bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
