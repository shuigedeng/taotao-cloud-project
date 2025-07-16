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

package com.taotao.cloud.auth.biz.authentication.context;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.ObjectUtils;

/**
 * 基于redis存储认证信息
 */
// @Component
@RequiredArgsConstructor
public class RedisSecurityContextRepository implements SecurityContextRepository {

    private final RedisRepository redisRepository;

    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    /**
     * 默认过期时间，默认五分钟
     */
    public static final long DEFAULT_TIMEOUT_SECONDS = 60L * 5;

    /**
     * 随机字符串请求头名字
     */
    public static final String NONCE_HEADER_NAME = "nonce";

    /**
     * 认证信息存储前缀
     */
    public static final String SECURITY_CONTEXT_PREFIX_KEY = "security_context:";

    @Override
    @Deprecated
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        //        HttpServletRequest request = requestResponseHolder.getRequest();
        //        return readSecurityContextFromRedis(request);
        // 方法已过时，使用 loadDeferredContext 方法
        throw new UnsupportedOperationException("Method deprecated.");
    }

    @Override
    public void saveContext(
            SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        String nonce = request.getHeader(NONCE_HEADER_NAME);
        if (ObjectUtils.isEmpty(nonce)) {
            nonce = request.getParameter(NONCE_HEADER_NAME);
            if (ObjectUtils.isEmpty(nonce)) {
                return;
            }
        }

        // 如果当前的context是空的，则移除
        SecurityContext emptyContext = this.securityContextHolderStrategy.createEmptyContext();
        if (emptyContext.equals(context)) {
            redisRepository.del((SECURITY_CONTEXT_PREFIX_KEY + nonce));
        } else {
            // 保存认证信息
            redisRepository.set(
                    (SECURITY_CONTEXT_PREFIX_KEY + nonce), context, DEFAULT_TIMEOUT_SECONDS);
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String nonce = request.getHeader(NONCE_HEADER_NAME);
        if (ObjectUtils.isEmpty(nonce)) {
            nonce = request.getParameter(NONCE_HEADER_NAME);
            if (ObjectUtils.isEmpty(nonce)) {
                return false;
            }
        }
        // 检验当前请求是否有认证信息
        return redisRepository.get((SECURITY_CONTEXT_PREFIX_KEY + nonce)) != null;
    }

    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        Supplier<SecurityContext> supplier = () -> readSecurityContextFromRedis(request);
        return new SupplierDeferredSecurityContext(supplier, this.securityContextHolderStrategy);
    }

    /**
     * 从redis中获取认证信息
     *
     * @param request 当前请求
     * @return 认证信息
     */
    private SecurityContext readSecurityContextFromRedis(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String nonce = request.getHeader(NONCE_HEADER_NAME);
        if (ObjectUtils.isEmpty(nonce)) {
            nonce = request.getParameter(NONCE_HEADER_NAME);
            if (ObjectUtils.isEmpty(nonce)) {
                return null;
            }
        }

        // 根据缓存id获取认证信息
        Object o = redisRepository.get((SECURITY_CONTEXT_PREFIX_KEY + nonce));
        if (o instanceof SecurityContext context) {
            return context;
        }
        return null;
    }
}
