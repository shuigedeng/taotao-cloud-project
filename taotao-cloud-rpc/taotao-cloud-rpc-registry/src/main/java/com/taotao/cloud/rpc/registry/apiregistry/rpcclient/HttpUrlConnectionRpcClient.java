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

package com.taotao.cloud.rpc.registry.apiregistry.rpcclient;

import com.taotao.cloud.rpc.registry.apiregistry.RequestInfo;
import java.lang.reflect.Type;

/**
 * HttpUrlConnection实现
 */
public class HttpUrlConnectionRpcClient implements IRpcClient {
    public <T> T execute(RequestInfo requestInfo, Type cls) {
        // val response = HttpUtils.request(new HttpUtils.HttpRequest(
        // 	requestInfo.getUrl(),
        // 	requestInfo.getMethod(),
        // 	requestInfo.getHeader(),
        // 	requestInfo.getBody(),
        // 	ApiRegistryProperties.getHttpUrlConnectionConnectTimeOut(),
        // 	ApiRegistryProperties.getHttpUrlConnectionReadTimeOut(),
        // 	ApiRegistryProperties.getHttpUrlConnectionPoolEnabled()));
        // if (!response.isSuccess()) {
        // 	throw new ApiRegistryHttpStateException(requestInfo.getAppName(),
        // StringUtils.nullToEmpty(requestInfo.getUrl()), response.getCode());
        // }
        // val code = CodeFactory.create(response.getHeader().get("Content-Type"));
        // return code.decode(response.getBody(), cls);
        return null;
    }
}
