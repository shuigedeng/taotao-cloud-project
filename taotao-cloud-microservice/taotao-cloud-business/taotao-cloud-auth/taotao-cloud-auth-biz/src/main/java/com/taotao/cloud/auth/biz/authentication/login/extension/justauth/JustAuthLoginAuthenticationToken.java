/*
 * MIT License
 * Copyright (c) 2020-2029 YongWu zheng (dcenter.top and gitee.com/pcore and github.com/ZeroOrInfinity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.taotao.cloud.auth.biz.authentication.login.extension.justauth;

import com.taotao.cloud.security.justauth.justauth.request.Auth2DefaultRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.util.Collections;

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
