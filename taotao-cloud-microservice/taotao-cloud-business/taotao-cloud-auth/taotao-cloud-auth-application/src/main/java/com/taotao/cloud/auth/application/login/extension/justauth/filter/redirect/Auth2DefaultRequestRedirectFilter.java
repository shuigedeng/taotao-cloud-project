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

package com.taotao.cloud.auth.application.login.extension.justauth.filter.redirect;

import com.taotao.cloud.auth.application.login.extension.justauth.service.Auth2StateCoder;
import com.taotao.cloud.auth.biz.exception.Auth2Exception;
import com.taotao.cloud.security.justauth.justauth.request.Auth2DefaultRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 构建第三方授权链接并重定向
 *
 * @author Joe Grandja
 * @author Rob Winch
 * @author YongWu zheng
 * @see OAuth2AuthorizationRequest
 * @see Auth2DefaultRequestResolver
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc6749#section-4.1">Section
 * 4.1 Authorization Code Grant</a>
 * @see <a target="_blank" href=
 * "https://tools.ietf.org/html/rfc6749#section-4.1.1">Section 4.1.1 Authorization Request
 * (Authorization Code)</a>
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc6749#section-4.2">Section
 * 4.2 Implicit Grant</a>
 * @see <a target="_blank" href=
 * "https://tools.ietf.org/html/rfc6749#section-4.2.1">Section 4.2.1 Authorization Request
 * (Implicit)</a>
 * @since 5.0
 */
@SuppressWarnings({"unused", "JavaDoc"})
public class Auth2DefaultRequestRedirectFilter extends OncePerRequestFilter {

    private final ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    private final RedirectStrategy authorizationRedirectStrategy = new DefaultRedirectStrategy();

    private final Auth2DefaultRequestResolver authorizationRequestResolver;

    private final Auth2StateCoder auth2StateCoder;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * Constructs an {@code Auth2DefaultRequestRedirectFilter} using the provided
     * parameters.
     *
     * @param authorizationRequestBaseUri the base {@code URI} used for authorization
     *                                    requests
     * @param auth2StateCoder             state 的编解码器
     */
    public Auth2DefaultRequestRedirectFilter(
            @NonNull String authorizationRequestBaseUri,
            @Nullable Auth2StateCoder auth2StateCoder,
            @NonNull AuthenticationFailureHandler authenticationFailureHandler) {
        Assert.hasText(authorizationRequestBaseUri, "authorizationRequestBaseUri cannot be empty");
        this.authorizationRequestResolver = new Auth2DefaultRequestResolver(authorizationRequestBaseUri);
        this.auth2StateCoder = auth2StateCoder;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    /**
     * Constructs an {@code Auth2DefaultRequestRedirectFilter} using the provided
     * parameters.
     *
     * @param authorizationRequestResolver the resolver used for resolving authorization requests
     * @param auth2StateCoder              state 的编解码器
     * @since 5.1
     */
    public Auth2DefaultRequestRedirectFilter(
            Auth2DefaultRequestResolver authorizationRequestResolver,
            @Nullable Auth2StateCoder auth2StateCoder,
            @NonNull AuthenticationFailureHandler authenticationFailureHandler) {
        Assert.notNull(authorizationRequestResolver, "authorizationRequestResolver cannot be null");
        this.authorizationRequestResolver = authorizationRequestResolver;
        this.auth2StateCoder = auth2StateCoder;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    /**
     * Sets the {@link RequestCache} used for storing the current request before
     * redirecting the OAuth 2.0 Authorization Request.
     *
     * @param requestCache the cache used for storing the current request
     */
    public final void setRequestCache(RequestCache requestCache) {
        Assert.notNull(requestCache, "requestCache cannot be null");
        this.requestCache = requestCache;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Auth2DefaultRequest authorizationRequest = this.authorizationRequestResolver.resolve(request);
            if (authorizationRequest != null) {
                this.sendRedirectForAuthorization(request, response, authorizationRequest);
                return;
            }
        } catch (Auth2Exception ex) {
            this.authenticationFailureHandler.onAuthenticationFailure(request, response, ex);
            return;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            this.unsuccessfulRedirectForAuthorization(request, response, ex);
            return;
        }
        try {
            filterChain.doFilter(request, response);
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            // Check to see if we need to handle ClientAuthorizationRequiredException
            Throwable[] causeChain = this.throwableAnalyzer.determineCauseChain(ex);
            AuthenticationException authzEx = (AuthenticationException)
                    this.throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);
            if (authzEx != null) {
                throw authzEx;
            }
            if (ex instanceof ServletException) {
                throw (ServletException) ex;
            }
            //noinspection ConstantConditions
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            }
            throw new RuntimeException(ex);
        }
    }

    private void sendRedirectForAuthorization(
            HttpServletRequest request, HttpServletResponse response, Auth2DefaultRequest authorizationRequest)
            throws IOException {
        String state = authorizationRequest.generateState();
        if (this.auth2StateCoder != null) {
            // 对 state 进行自定义编码 https://gitee.com/pcore/just-auth-spring-security-starter/issues/I22JC7
            state = this.auth2StateCoder.encode(state, request);
        }
        String authorize = authorizationRequest.authorize(state);

        //		if (isAjaxOrJson(request)) {
        //			responseWithJson(response, HttpStatus.OK.value(), toJsonString(redirect(authorize)));
        //			return;
        //		}

        this.authorizationRedirectStrategy.sendRedirect(request, response, authorize);
    }

    private void unsuccessfulRedirectForAuthorization(
            HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        this.logger.error(LogMessage.format("Authorization Request failed: %s", ex, ex));
        response.sendError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    private static final class DefaultThrowableAnalyzer extends ThrowableAnalyzer {

        @Override
        protected void initExtractorMap() {
            super.initExtractorMap();
            registerExtractor(ServletException.class, (throwable) -> {
                ThrowableAnalyzer.verifyThrowableHierarchy(throwable, ServletException.class);
                return ((ServletException) throwable).getRootCause();
            });
        }
    }
}
