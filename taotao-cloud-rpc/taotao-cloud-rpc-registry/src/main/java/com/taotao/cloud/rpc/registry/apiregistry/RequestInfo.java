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

package com.taotao.cloud.rpc.registry.apiregistry;

import java.util.HashMap;
import java.util.Map;

/**
 * RequestInfo
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class RequestInfo {

    String appName;
    Map<String, String> header = new HashMap<>();
    String url;
    byte[] body;
    String method;

    public RequestInfo() {
    }

    public RequestInfo(
            String appName, Map<String, String> header, String url, byte[] body, String method ) {
        this.appName = appName;
        this.header = header;
        this.url = url;
        this.body = body;
        this.method = method;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName( String appName ) {
        this.appName = appName;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader( Map<String, String> header ) {
        this.header = header;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody( byte[] body ) {
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod( String method ) {
        this.method = method;
    }
}
