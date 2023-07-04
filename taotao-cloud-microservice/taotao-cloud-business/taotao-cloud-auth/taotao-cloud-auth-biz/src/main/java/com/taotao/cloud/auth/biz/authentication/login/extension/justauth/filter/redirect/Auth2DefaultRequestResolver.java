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

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.filter.redirect;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.STATE;

import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.JustAuthRequestHolder;
import com.taotao.cloud.auth.biz.uaa.enums.ErrorCodeEnum;
import com.taotao.cloud.auth.biz.exception.Auth2Exception;
import com.taotao.cloud.security.justauth.justauth.request.Auth2DefaultRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * An implementation of an {@link Auth2AuthorizationRequestResolver} that attempts to
 * resolve an {@link Auth2DefaultRequest} from the provided
 * {@code HttpServletRequest} using the default request {@code URI} pattern
 * {@code /auth2/authorization/{registrationId}}.
 *
 * <p>
 * {@link #Auth2DefaultRequestResolver(String)}.
 *
 * @author YongWu zheng
 * @version V2.0  Created by 2020/10/5 10:54
 * @see Auth2DefaultRequestResolver
 * @see Auth2DefaultRequestRedirectFilter
 */
public final class Auth2DefaultRequestResolver implements Auth2AuthorizationRequestResolver {

    private static final String REGISTRATION_ID_URI_VARIABLE_NAME = "registrationId";

    private final AntPathRequestMatcher authorizationRequestMatcher;

    /**
     * Constructs a {@code Auth2DefaultRequestResolver} using the provided
     * parameters.
     *
     * @param authorizationRequestBaseUri the base {@code URI} used for resolving
     *                                    authorization requests
     */
    public Auth2DefaultRequestResolver(String authorizationRequestBaseUri) {
        Assert.hasText(authorizationRequestBaseUri, "authorizationRequestBaseUri cannot be empty");
        this.authorizationRequestMatcher =
                new AntPathRequestMatcher(authorizationRequestBaseUri + "/{" + REGISTRATION_ID_URI_VARIABLE_NAME + "}");
    }

    @Override
    public Auth2DefaultRequest resolve(HttpServletRequest request) throws Auth2Exception {
        if (StringUtils.hasText(request.getParameter(STATE))) {
            return null;
        }
        String registrationId = this.resolveRegistrationId(request);

        return getAuth2DefaultRequest(registrationId);
    }

    @Override
    public Auth2DefaultRequest resolve(HttpServletRequest request, String registrationId) throws Auth2Exception {
        if (StringUtils.hasText(request.getParameter(STATE))) {
            return null;
        }

        return getAuth2DefaultRequest(registrationId);
    }

    public String resolveRegistrationId(HttpServletRequest request) {
        if (this.authorizationRequestMatcher.matches(request)) {
            return this.authorizationRequestMatcher
                    .matcher(request)
                    .getVariables()
                    .get(REGISTRATION_ID_URI_VARIABLE_NAME);
        }
        return null;
    }

    @Nullable
    private Auth2DefaultRequest getAuth2DefaultRequest(@Nullable String registrationId) throws Auth2Exception {
        if (registrationId == null) {
            return null;
        }

        final Auth2DefaultRequest auth2DefaultRequest = JustAuthRequestHolder.getAuth2DefaultRequest(registrationId);
        if (auth2DefaultRequest == null) {
            throw new Auth2Exception(ErrorCodeEnum.AUTH2_PROVIDER_NOT_SUPPORT, registrationId);
        }
        return auth2DefaultRequest;
    }
}
