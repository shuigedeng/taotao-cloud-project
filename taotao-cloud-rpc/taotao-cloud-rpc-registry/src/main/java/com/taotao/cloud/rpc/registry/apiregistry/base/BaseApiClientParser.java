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

package com.taotao.cloud.rpc.registry.apiregistry.base;

import com.taotao.cloud.rpc.registry.apiregistry.RequestInfo;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @ApiClient解析器
 */
public class BaseApiClientParser {

    /**
     * ApiClientParserInfo
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
    public static class ApiClientParserInfo {

        String appName;
        String url;
        Method method;
        ProceedingJoinPoint joinPoint;

        public ApiClientParserInfo(
                String appName, String url, Method method, ProceedingJoinPoint joinPoint ) {
            this.appName = appName;
            this.url = url;
            this.method = method;
            this.joinPoint = joinPoint;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName( String appName ) {
            this.appName = appName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl( String url ) {
            this.url = url;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod( Method method ) {
            this.method = method;
        }

        public ProceedingJoinPoint getJoinPoint() {
            return joinPoint;
        }

        public void setJoinPoint( ProceedingJoinPoint joinPoint ) {
            this.joinPoint = joinPoint;
        }
    }

    public RequestInfo parse( ApiClientParserInfo info ) {
        return null;
    }
}
