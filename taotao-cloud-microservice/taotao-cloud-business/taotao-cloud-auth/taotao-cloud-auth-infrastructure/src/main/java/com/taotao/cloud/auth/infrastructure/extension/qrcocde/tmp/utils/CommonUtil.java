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

package com.taotao.cloud.auth.infrastructure.extension.qrcocde.tmp.utils;

import java.util.UUID;

public class CommonUtil {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String buildTicketKey(String uuid) {
        return LoginConstant.PREFIX_TICKET + LoginConstant.SPLIT + uuid;
    }

    public static String buildUserKey(String userId) {
        return LoginConstant.PREFIX_USER + LoginConstant.SPLIT + userId;
    }

    public static String buildAccessTokenKey(String token) {
        return LoginConstant.ACCESS_TOKEN__PREFIX + token;
    }

    public static String buildOnceTokenKey(String token) {
        return LoginConstant.ONCE_TOKEN__PREFIX + token;
    }
}
