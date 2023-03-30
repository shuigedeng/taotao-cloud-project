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

package com.taotao.cloud.payment.biz.kit.core.enums;

/** HTTP 请求的方法 */
public enum RequestMethodEnums {
    /** 上传实质是 post 请求 */
    UPLOAD("POST"),
    /** post 请求 */
    POST("POST"),
    /** get 请求 */
    GET("GET"),
    /** put 请求 */
    PUT("PUT"),
    /** delete 请求 */
    DELETE("DELETE"),
    /** options 请求 */
    OPTIONS("OPTIONS"),
    /** head 请求 */
    HEAD("HEAD"),
    /** trace 请求 */
    TRACE("TRACE"),
    /** connect 请求 */
    CONNECT("CONNECT");

    private final String method;

    RequestMethodEnums(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return this.method;
    }
}
