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

import java.util.*;

/**
 * Http请求头
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public class HttpHeaders {
    private CaseInsensitiveMap<List<String>> headerMap;

    private HttpHeaders(CaseInsensitiveMap<List<String>> headerMap) {
        this.headerMap = headerMap;
    }

    public static HttpHeaders of(Map<String, List<String>> headerMap) {
        requireNonNull(headerMap);
        return new HttpHeaders(CaseInsensitiveMap.of(headerMap));
    }

    public HttpHeaders withAdditionalHeader(String name, String value) {
        requireNonNull(name);
        requireNonNull(value);
        return this.withAdditionalHeader(name, Arrays.asList(value));
    }

    public HttpHeaders withAdditionalHeader(String name, List<String> values) {
        requireNonNull(name);
        requireNonNull(values);
        Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.put(name, values);
        return this.withAdditionalHeaders(headerMap);
    }

    public HttpHeaders withAdditionalHeaders(Map<String, List<String>> headerMap) {
        requireNonNull(headerMap);
        Map<String, List<String>> newHeaderMap = new HashMap<>(this.map());
        newHeaderMap.putAll(headerMap);
        return HttpHeaders.of(newHeaderMap);
    }

    public List<String> allValues(String name) {
        if (this.headerMap.containsKey(name)) {
            List<String> values = this.headerMap.get(name);
            if ((values != null) && (values.size() > 0)) {
                return Collections.unmodifiableList(values);
            }
        }
        return Collections.emptyList();
    }

    public Optional<String> firstValue(String name) {
        if (this.headerMap.containsKey(name)) {
            List<String> values = this.headerMap.get(name);
            if ((values != null) && (values.size() > 0)) {
                return Optional.of(values.get(0));
            }
        }
        return Optional.empty();
    }

    public Map<String, List<String>> map() {
        return Collections.unmodifiableMap(this.headerMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" { ");
        sb.append(map());
        sb.append(" }");
        return sb.toString();
    }
}
