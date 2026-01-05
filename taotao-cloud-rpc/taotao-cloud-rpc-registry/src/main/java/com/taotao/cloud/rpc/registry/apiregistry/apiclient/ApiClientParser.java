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

package com.taotao.cloud.rpc.registry.apiregistry.apiclient;

import com.taotao.cloud.rpc.registry.apiregistry.RequestInfo;
import com.taotao.cloud.rpc.registry.apiregistry.base.BaseApiClientParser;
import com.taotao.cloud.rpc.registry.apiregistry.code.CodeFactory;
import com.taotao.cloud.rpc.registry.apiregistry.code.Code;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @ApiClient注解解析器实现通用java springmvc http写法方式解析
 */
public class ApiClientParser extends BaseApiClientParser {
    @Override
    public RequestInfo parse(ApiClientParserInfo info) {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAppName(info.getAppName());
        buildHeader(requestInfo);
        buildMethod(requestInfo, info);
        buildBody(requestInfo, info);
        return requestInfo;
    }

    private void buildHeader(RequestInfo requestInfo) {
        if (!requestInfo.getHeader().containsKey("Content-Type")) {
            requestInfo.getHeader().put("Content-Type", CodeFactory.getHeader());
        }
    }

    private void buildMethod(RequestInfo requestInfo, ApiClientParserInfo info) {
        Method method = info.getMethod();
        PostMapping postMapping = AnnotationUtils.getAnnotation(method, PostMapping.class);
        if (postMapping != null) {
            requestInfo.setMethod("POST");
            buildUrl(
                    requestInfo,
                    info,
                    postMapping.value().length > 0 ? postMapping.value()[0] : "");
            return;
        }
        GetMapping getMapping = AnnotationUtils.getAnnotation(method, GetMapping.class);
        if (getMapping != null) {
            requestInfo.setMethod("GET");
            buildUrl(requestInfo, info, getMapping.value().length > 0 ? getMapping.value()[0] : "");
            return;
        }
        RequestMapping reqMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
        if (reqMapping != null) {
            requestInfo.setMethod(
                    reqMapping.method().length > 0 ? reqMapping.method()[0].name() : "");
            buildUrl(requestInfo, info, reqMapping.value().length > 0 ? reqMapping.value()[0] : "");
            return;
        }
    }

    private void buildUrl(RequestInfo requestInfo, ApiClientParserInfo info, String httpPath) {
        var urlBuilder = UriComponentsBuilder.fromUriString(info.getUrl());
        Parameter[] ps = info.getMethod().getParameters();
        for (Integer i = 0; i < ps.length; i++) {
            var p = ps[i];
            RequestParam param = AnnotationUtils.getAnnotation(p, RequestParam.class);
            if (param != null) {
                Object value = info.getJoinPoint().getArgs()[i];
                if (value == null && !ValueConstants.DEFAULT_NONE.equals(param.defaultValue())) {
                    value = param.defaultValue();
                }
                if (value != null) {
                    if (value instanceof Collection) {
                        urlBuilder = urlBuilder.queryParam(param.value(), (Collection) value);
                    } else if (value.getClass().isArray()) {
                        urlBuilder = urlBuilder.queryParam(param.value(), Arrays.asList(value));
                    } else {
                        urlBuilder = urlBuilder.queryParam(param.value(), value);
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(httpPath)) {
            urlBuilder.path(httpPath);
        }
        requestInfo.setUrl(urlBuilder.build().toUri().toString());
    }

    private void buildBody(RequestInfo requestInfo, ApiClientParserInfo info) {
        Parameter[] ps = info.getMethod().getParameters();
        for (Integer i = 0; i < ps.length; i++) {
            var p = ps[i];
            RequestBody requestBody = AnnotationUtils.getAnnotation(p, RequestBody.class);
            if (requestBody != null) {
                Object value = info.getJoinPoint().getArgs()[i];
                Code code = CodeFactory.create(requestInfo.getHeader().get("Content-Type"));
                requestInfo.setBody(code.encode(value));
                return;
            }
        }
    }
}
