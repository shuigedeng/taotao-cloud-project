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

import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.entity.User;
import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.service.UserService;
import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.utils.CommonUtil;
import com.taotao.cloud.auth.application.login.extension.qrcocde.tmp.utils.HostHolder;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    private HostHolder hostHolder;

    private RedisRepository cacheStore;

    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String accessToken = request.getHeader("access_token");
        // access_token 存在
        if (StringUtils.isNotEmpty(accessToken)) {
            String userId = (String) cacheStore.get(CommonUtil.buildAccessTokenKey(accessToken));
            User user = userService.getCurrentUser(userId);
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        hostHolder.clear();
    }
}
