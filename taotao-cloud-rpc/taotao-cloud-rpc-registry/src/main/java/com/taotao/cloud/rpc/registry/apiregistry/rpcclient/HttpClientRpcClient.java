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
import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;
import java.lang.reflect.Type;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;

/**
 * httpClient实现
 */
public class HttpClientRpcClient implements RpcClient {
    public <T> T execute(RequestInfo requestInfo, Type cls) {
        HttpUriRequest r = httpClientRequest(requestInfo);
        try {
            // try (val response = HttpClientUtils.system().getClient().execute(r)) {
            // 	if (response.getStatusLine().getStatusCode() != 200) {
            // 		throw new ApiRegistryHttpStateException(requestInfo.getAppName(),
            // StringUtils.nullToEmpty(requestInfo.getUrl()),
            // response.getStatusLine().getStatusCode());
            // 	}
            // 	val code =
            // CodeFactory.create(JsonUtils.serialize(response.getHeaders("Content-Type")));
            // 	return code.decode(EntityUtils.toByteArray(response.getEntity()), cls);
            // }
            return null;
        } catch (Exception e) {
            throw new ApiRegistryException(e);
        }
    }

    protected HttpUriRequest httpClientRequest(RequestInfo requestInfo) {
        // RequestBuilder requestBuilder = RequestBuilder.create(requestInfo.getMethod());
        // requestBuilder.setUri(requestInfo.getUrl());
        // if (requestInfo.getBody() != null) {
        // 	requestBuilder.setEntity(new ByteArrayEntity(requestInfo.getBody()));
        // }
        // for (val h : requestInfo.getHeader().entrySet()) {
        // 	requestBuilder.addHeader(h.getKey(), h.getValue());
        // }
        // return requestBuilder.build();
        return null;
    }
}
