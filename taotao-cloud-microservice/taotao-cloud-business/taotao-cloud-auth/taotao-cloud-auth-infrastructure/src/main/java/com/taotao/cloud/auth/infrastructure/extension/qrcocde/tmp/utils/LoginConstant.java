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

public class LoginConstant {

    public static final String SPLIT = ":";
    public static final String PREFIX_TICKET = "ticket";
    public static final String PREFIX_USER = "user";

    public static final String ACCESS_TOKEN__PREFIX = "access_token_";
    public static final String ONCE_TOKEN__PREFIX = "once_token_";

    // access_token 有效时间
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 24 * 3600;

    // 一次性 token 有效时间
    public static final long ONCE_TOKEN_EXPIRE_TIME = 5 * 60;

    // 二维码有效时间
    public static final long WAIT_EXPIRED_SECONDS = 60 * 5;

    public static final String CONFIRM_URI = "/login/confirm";

    public static final String ONCE_TOKEN_QUERY_NAME = "uuid";

    // 轮询的阻塞时间
    public static final long POLL_WAIT_TIME = 30;
}
