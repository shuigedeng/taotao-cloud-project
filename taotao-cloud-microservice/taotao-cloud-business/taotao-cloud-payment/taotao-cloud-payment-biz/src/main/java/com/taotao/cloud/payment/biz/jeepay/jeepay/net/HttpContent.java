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

package com.taotao.cloud.payment.biz.jeepay.jeepay.net;

import static java.util.Objects.requireNonNull;

import com.alibaba.fastjson2.JSON;
import java.util.Map;

/**
 * Http请求内容
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public class HttpContent {

    byte[] byteArrayContent;

    String contentType;

    private HttpContent(byte[] byteArrayContent, String contentType) {
        this.byteArrayContent = byteArrayContent;
        this.contentType = contentType;
    }

    public String stringContent() {
        return new String(this.byteArrayContent, APIResource.CHARSET);
    }

    public static HttpContent buildJSONContent(Map<String, Object> params) {
        requireNonNull(params);

        return new HttpContent(
                createJSONString(params).getBytes(APIResource.CHARSET),
                String.format("application/json; charset=%s", APIResource.CHARSET));
    }

    private static String createJSONString(Map<String, Object> params) {
        return JSON.toJSONString(params);
    }
}
