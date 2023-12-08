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

package com.taotao.cloud.workflow.biz.common.util;

/**
 * 通用常量信息
 *
 */
public class Constants {

    /** token */
    public static final String AUTHORIZATION = "Authorization";

    /** UTF-8 字符集 */
    public static final String UTF8 = "UTF-8";

    /** GBK 字符集 */
    public static final String GBK = "GBK";

    /** http请求 */
    public static final String HTTP = "http://";

    /** https请求 */
    public static final String HTTPS = "https://";

    /** 成功标记 */
    public static final Integer SUCCESS = 200;

    /** 失败标记 */
    public static final Integer FAIL = 500;

    /** 登录成功 */
    public static final String LOGIN_SUCCESS = "Success";

    /** 注销 */
    public static final String LOGOUT = "Logout";

    /** 注册 */
    public static final String REGISTER = "Register";

    /** 验证码 redis key */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /** 验证码有效期（分钟） */
    public static final long CAPTCHA_EXPIRATION = 2;

    /** 令牌有效期（分钟） */
    public static final long TOKEN_EXPIRE = 720;

    /** swagger版本号 */
    public static final String SWAGGER_VERSION = "3.1.0";

    /** swagger版本号 */
    public static final String USER_AGENT = "User-Agent";
}
