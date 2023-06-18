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

package com.taotao.cloud.auth.biz.utils;

import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;

/**
 */
public class RedirectLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache;
    private static final String defaultTargetUrl = "/";
    private final String redirect;

    public RedirectLoginAuthenticationSuccessHandler() {
        this(defaultTargetUrl, new HttpSessionRequestCache());
    }

    public RedirectLoginAuthenticationSuccessHandler(String redirect, RequestCache requestCache) {
        Assert.notNull(requestCache, "requestCache must not be null");
        this.redirect = redirect;
        this.requestCache = requestCache;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);

        String targetUrl = savedRequest == null ? this.redirect : savedRequest.getRedirectUrl();
        clearAuthenticationAttributes(request);

        ResponseUtils.success(response, targetUrl);
    }

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
