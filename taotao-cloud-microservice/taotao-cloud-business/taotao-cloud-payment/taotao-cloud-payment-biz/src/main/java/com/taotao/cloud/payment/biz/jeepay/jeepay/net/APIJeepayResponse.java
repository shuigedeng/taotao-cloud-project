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

/**
 * API响应
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public class APIJeepayResponse {

    private int responseCode;
    private String responseBody;
    private HttpHeaders responseHeaders;

    private int numRetries;

    public APIJeepayResponse(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.responseHeaders = null;
    }

    public APIJeepayResponse(int responseCode, String responseBody, HttpHeaders responseHeaders) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.responseHeaders = responseHeaders;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    public int getNumRetries() {
        return numRetries;
    }

    public void setNumRetries(int numRetries) {
        this.numRetries = numRetries;
    }
}
