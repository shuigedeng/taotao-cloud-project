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

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth;

import com.taotao.cloud.security.justauth.justauth.request.Auth2DefaultRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

/**
 * An {@link AbstractAuthenticationToken} for OAuth 2.0 Login, which leverages the OAuth
 * 2.0 Authorization Code Grant Flow.
 *
 * @author YongWu zheng
 * @see AbstractAuthenticationToken
 * @see Auth2DefaultRequest
 * @since 2.0.0
 */
public class JustAuthLoginAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Getter
    private final Auth2DefaultRequest auth2DefaultRequest;

    @Getter
    private final HttpServletRequest request;

    /**
     * This constructor should be used when the auth2DefaultRequest callback is
     * complete.
     *
     * @param auth2DefaultRequest the auth2DefaultRequest
     * @param request             the request
     */
    public JustAuthLoginAuthenticationToken(Auth2DefaultRequest auth2DefaultRequest, HttpServletRequest request) {
        super(Collections.emptyList());
        Assert.notNull(auth2DefaultRequest, "auth2DefaultRequest cannot be null");
        Assert.notNull(request, "request cannot be null");
        this.auth2DefaultRequest = auth2DefaultRequest;
        this.setAuthenticated(false);
        this.request = request;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
