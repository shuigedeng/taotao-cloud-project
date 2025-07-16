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

import org.apache.commons.lang3.StringUtils;

/**
 * PropertyUtils
 *
 * @author shuigedeng
 * @since 2023/7/15
 */
public class PropertyUtils {

    public static String readProperty(String key, String defaultValue) {
        // 从启动参数读取
        String property = System.getProperty(key);
        if (StringUtils.isNotEmpty(property)) {
            return property;
        }

        // 从 ENV 读取
        property = System.getenv(key);
        if (StringUtils.isNotEmpty(property)) {
            return property;
        }
        // 部分操作系统不兼容 a.b.c 的环境变量，转换为 a_b_c 再取一次，即 PowerJob 支持 2 种类型的环境变量 key
        property = System.getenv(key.replaceAll("\\.", "_"));
        if (StringUtils.isNotEmpty(property)) {
            return property;
        }
        return defaultValue;
    }
}
