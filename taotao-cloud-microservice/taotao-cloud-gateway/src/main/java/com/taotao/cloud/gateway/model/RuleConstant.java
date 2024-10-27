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

package com.taotao.cloud.gateway.model;

/**
 * 规则常量
 *
 * @author shuigedeng
 * @version 2023.06
 * @since 2023-06-02 17:54:15
 */
public class RuleConstant {

    public static final String LOCALHOST = "localhost";
    public static final String LOCALHOST_IP = "192.168.218.2";

    public static final String ALL = "all";
    public static final String BLACKLIST_OPEN = "0";
    public static final String BLACKLIST_CLOSE = "1";

    private static final String BLACKLIST_CACHE_KEY_PREFIX = "taotao:cloud:rule:blacklist:";

    public static String getBlackListCacheKey(String ip) {
        if (LOCALHOST.equalsIgnoreCase(ip)) {
            ip = LOCALHOST_IP;
        }
        return String.format("%s%s", BLACKLIST_CACHE_KEY_PREFIX, ip);
    }

    public static String getBlackListCacheKey() {
        return String.format("%s" + ALL, BLACKLIST_CACHE_KEY_PREFIX);
    }
}
