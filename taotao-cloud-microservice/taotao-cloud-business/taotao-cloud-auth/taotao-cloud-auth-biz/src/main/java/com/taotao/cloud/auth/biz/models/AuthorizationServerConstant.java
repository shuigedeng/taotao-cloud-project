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

package com.taotao.cloud.auth.biz.models;

import com.taotao.cloud.common.enums.UserTypeEnum;

/**
 * AuthorizationServerConstant
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-22 14:51:16
 */
public class AuthorizationServerConstant {

    public static final String UNDERSCORE = "_";
    public static final String SLASH = "/";
    public static final String BACK_SLASH = "\\";
    public static final String COMMA = ",";
    public static final String COLON = ":";

    public static final String PREFIX_AUTHORIZATION = "oauth2:authorization:";
    public static final String PREFIX_AUTHORIZATION_ID_ATTR = "oauth2:authorization:id:";

    public static final String REDIS_SET_RESULT_OK = "OK";

    /**
     * 用户类型
     *
     * @see UserTypeEnum
     */
    public static final String PARAM_TYPE = "type";

    /** 图形验证码 / 手机验证码 */
    public static final String VERIFICATION_CODE = "verification_code";

    /** 手机号码参数 */
    public static final String PARAM_MOBILE = "mobile";
}
