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

package com.taotao.cloud.front.biz.util;

/**
 * 常量类
 */
public interface Constants {
    /**
     * 响应code
     */
    String HTTP_RES_CODE_NAME = "code";
    /**
     * 响应msg
     */
    String HTTP_RES_CODE_MSG = "message";
    /**
     * 响应data
     */
    String HTTP_RES_CODE_DATA = "data";
    /**
     * 响应请求成功
     */
    String HTTP_RES_CODE_200_VALUE = "success";
    /**
     * 系统错误
     */
    String HTTP_RES_CODE_500_VALUE = "fail";
    /**
     * 响应请求成功code
     */
    Integer HTTP_RES_CODE_200 = 200;
    /**
     * 系统错误
     */
    Integer HTTP_RES_CODE_500 = 500;

    Integer HTTP_RES_CODE_201 = 201;

    String SMS_MAIL = "sms_email";

    String MEMBER_TOKEN = "member-token";

    /**
     * cookie 会员 totoken 名称
     */
    String COOKIE_MEMBER_TOKEN = "cookie_member_token";

    int COOKIE_TOKEN_MEMBER_TIME = (60 * 60 * 24 * 90);
}
