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

package com.taotao.cloud.workflow.biz.flowable.flowable.core.web;

import com.taotao.cloud.flowable.biz.flowable.core.util.FlowableUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * flowable Web 过滤器，将 userId 设置到 {@link org.flowable.common.engine.impl.identity.Authentication} 中
 *
 * @author jason
 */
public class FlowableWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            // 设置工作流的用户
            Long userId = SecurityFrameworkUtils.getLoginUserId();
            if (userId != null) {
                FlowableUtils.setAuthenticatedUserId(userId);
            }
            // 过滤
            chain.doFilter(request, response);
        } finally {
            // 清理
            FlowableUtils.clearAuthenticatedUserId();
        }
    }
}
