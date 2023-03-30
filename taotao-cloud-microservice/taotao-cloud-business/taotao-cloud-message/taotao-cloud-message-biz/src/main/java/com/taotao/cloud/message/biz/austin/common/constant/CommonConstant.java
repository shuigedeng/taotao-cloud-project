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

package com.taotao.cloud.message.biz.austin.common.constant;

/**
 * @author 3y
 */
public class CommonConstant {

    public static final String PERIOD = ".";
    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String SEMICOLON = ";";
    public static final String POUND = "#";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String EMPTY_STRING = "";
    public static final String RADICAL = "|";

    public static final String QM_STRING = "?";
    public static final String EQUAL_STRING = "=";
    public static final String AND_STRING = "&";

    public static final String ONE = "1";
    public static final String ZERO = "0";
    public static final String MINUS_ONE = "-1";
    public static final String YES = "Y";
    public static final String NO = "N";

    public static final char QM = '?';

    /** boolean转换 */
    public static final Integer TRUE = 1;

    public static final Integer FALSE = 0;

    /** 加密算法 */
    public static final String HMAC_SHA256_ENCRYPTION_ALGO = "HmacSHA256";

    /** 编码格式 */
    public static final String CHARSET_NAME = "UTF-8";

    /** HTTP请求内容格式 */
    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";

    public static final String CONTENT_TYPE_TEXT = "text/html;charset=utf-8";
    public static final String CONTENT_TYPE_XML = "application/xml; charset=UTF-8";
    public static final String CONTENT_TYPE_FORM_URL_ENCODE =
            "application/x-www-form-urlencoded;charset=utf-8;";
    public static final String CONTENT_TYPE_MULTIPART_FORM_DATA = "multipart/form-data";

    /** HTTP 请求方法 */
    public static final String REQUEST_METHOD_GET = "GET";

    public static final String REQUEST_METHOD_POST = "POST";

    /** JSON默认值 */
    public static final String EMPTY_JSON_OBJECT = "{}";

    public static final String EMPTY_VALUE_JSON_ARRAY = "[]";

    /** cron时间格式 */
    public static final String CRON_FORMAT = "ss mm HH dd MM ? yyyy-yyyy";

    /** 环境常量 */
    public static final String ENV_DEV = "dev";

    public static final String ENV_TEST = "test";
}
