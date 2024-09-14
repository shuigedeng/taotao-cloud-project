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

package com.taotao.cloud.auth.infrastructure.extension.qrcocde.tmp.controller.interceptor;

import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.utils.CommonUtil;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class ConfirmInterceptor implements HandlerInterceptor {

    private RedisRepository cacheStore;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String onceToken = request.getHeader("once_token");
        if (StringUtils.isEmpty(onceToken)) {
            return false;
        }

        if (StringUtils.isNoneEmpty(onceToken)) {
            String onceTokenKey = CommonUtil.buildOnceTokenKey(onceToken);
            //            String allowedUri = (String) cacheStore.get(onceTokenKey);
            //            String requestUri = request.getRequestURI();
            //            requestUri = requestUri
            //                    + "?uuid="
            //                    + request.getParameter("uuid");
            //            if (!StringUtils.equals(requestUri, allowedUri)) {
            //                throw new RuntimeException("一次性 token 与请求的 uri 不对应");
            //            }
            String uuidFromCache = (String) cacheStore.get(onceTokenKey);
            String uuidFromRequest = request.getParameter("uuid");
            if (!StringUtils.equals(uuidFromCache, uuidFromRequest)) {
                throw new RuntimeException("非法的一次性 token");
            }

            // 一次性 token 检查完成后将其删除
            cacheStore.del(onceTokenKey);
        }
        return true;
    }
}
