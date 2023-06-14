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

package com.taotao.cloud.auth.biz.authentication.authentication;

import com.taotao.cloud.auth.biz.utils.SimpleAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.PortMapper;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.*;
import org.springframework.util.Assert;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import java.util.Arrays;
import java.util.Collections;

/**
 * 认证过滤器{@link AbstractAuthenticationProcessingFilter}s
 *
 * @param <H>
 * @param <C>
 * @param <F>
 */
public abstract class AbstractLoginFilterConfigurer<
                H extends HttpSecurityBuilder<H>,
                C extends AbstractLoginFilterConfigurer<H, C, F, A>,
                F extends AbstractAuthenticationProcessingFilter,
                A extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H>>
        extends AbstractHttpConfigurer<AbstractLoginFilterConfigurer<H, C, F, A>, H> {

    private final A configurerAdapter;

    private F authFilter;

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    private AuthenticationSuccessHandler successHandler;

    private AuthenticationEntryPoint authenticationEntryPoint;

    private String loginProcessingUrl;

    private AuthenticationFailureHandler failureHandler;

    private boolean permitAll;

    private String failureUrl;

    public AbstractLoginFilterConfigurer(
            A configurerAdapter, F authenticationFilter, String defaultLoginProcessingUrl) {
        this.configurerAdapter = configurerAdapter;
        this.authFilter = authenticationFilter;
        if (defaultLoginProcessingUrl != null) {
            loginProcessingUrl(defaultLoginProcessingUrl);
        }
    }

    public final C defaultSuccessUrl(String defaultSuccessUrl) {
        return defaultSuccessUrl(defaultSuccessUrl, false);
    }

    public final C defaultSuccessUrl(String defaultSuccessUrl, boolean alwaysUse) {
        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl(defaultSuccessUrl);
        handler.setAlwaysUseDefaultTargetUrl(alwaysUse);
        return successHandler(handler);
    }

    public C loginProcessingUrl(String loginProcessingUrl) {
        this.loginProcessingUrl = loginProcessingUrl;
        this.authFilter.setRequiresAuthenticationRequestMatcher(createLoginProcessingUrlMatcher(loginProcessingUrl));
        return getSelf();
    }

    protected abstract RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl);

    public final C authenticationDetailsSource(
            AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
        return getSelf();
    }

    public final C successHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return getSelf();
    }

    public final C authenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        return getSelf();
    }

    public final C permitAll() {
        return permitAll(true);
    }

    public final C permitAll(boolean permitAll) {
        this.permitAll = permitAll;
        return getSelf();
    }

    public final C failureUrl(String authenticationFailureUrl) {
        C result = failureHandler(new SimpleUrlAuthenticationFailureHandler(authenticationFailureUrl));
        this.failureUrl = authenticationFailureUrl;
        return result;
    }

    public final C failureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.failureUrl = null;
        this.failureHandler = authenticationFailureHandler;
        return getSelf();
    }

    public A with() {
        return this.configurerAdapter;
    }

    @Override
    public void init(H http) {
        updateAccessDefaults(http);
        updateAuthenticationDefaults();
        registerDefaultAuthenticationEntryPoint(http);
        AuthenticationProvider authenticationProvider = authenticationProvider(http);
        http.authenticationProvider(postProcess(authenticationProvider));
        if (this.successHandler == null) {
            successHandler(defaultSuccessHandler(http));
        }
    }

    protected abstract AuthenticationProvider authenticationProvider(H http);

    protected abstract AuthenticationSuccessHandler defaultSuccessHandler(H http);

    protected final void updateAuthenticationDefaults() {
        if (this.failureHandler == null) {
            failureHandler(new AuthenticationEntryPointFailureHandler(new SimpleAuthenticationEntryPoint()));
        }
    }

    protected final void registerDefaultAuthenticationEntryPoint(H http) {
        if (authenticationEntryPoint != null) {
            this.authenticationEntryPoint = new SimpleAuthenticationEntryPoint();
        }
        registerAuthenticationEntryPoint(http, this.authenticationEntryPoint);
    }

    @SuppressWarnings("unchecked")
    protected final void registerAuthenticationEntryPoint(H http, AuthenticationEntryPoint authenticationEntryPoint) {
        ExceptionHandlingConfigurer<H> exceptionHandling = http.getConfigurer(ExceptionHandlingConfigurer.class);
        if (exceptionHandling == null) {
            return;
        }
        exceptionHandling.defaultAuthenticationEntryPointFor(
                postProcess(authenticationEntryPoint), getAuthenticationEntryPointMatcher(http));
    }

    protected final RequestMatcher getAuthenticationEntryPointMatcher(H http) {
        ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(ContentNegotiationStrategy.class);
        if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }
        MediaTypeRequestMatcher mediaMatcher = new MediaTypeRequestMatcher(
                contentNegotiationStrategy,
                MediaType.APPLICATION_XHTML_XML,
                new MediaType("image", "*"),
                MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN);
        mediaMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        RequestMatcher notXRequestedWith =
                new NegatedRequestMatcher(new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
        return new AndRequestMatcher(Arrays.asList(notXRequestedWith, mediaMatcher));
    }

    @Override
    public void configure(H http) throws Exception {
        PortMapper portMapper = http.getSharedObject(PortMapper.class);
        if (portMapper != null && this.authenticationEntryPoint instanceof LoginUrlAuthenticationEntryPoint) {
            ((LoginUrlAuthenticationEntryPoint) this.authenticationEntryPoint).setPortMapper(portMapper);
        }

        RequestCache requestCache = http.getSharedObject(RequestCache.class);
        if (requestCache != null && this.successHandler instanceof SavedRequestAwareAuthenticationSuccessHandler) {
            ((SavedRequestAwareAuthenticationSuccessHandler) this.successHandler).setRequestCache(requestCache);
        }

        this.authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        this.authFilter.setAuthenticationSuccessHandler(this.successHandler);
        this.authFilter.setAuthenticationFailureHandler(this.failureHandler);
        if (this.authenticationDetailsSource != null) {
            this.authFilter.setAuthenticationDetailsSource(this.authenticationDetailsSource);
        }

        SessionAuthenticationStrategy sessionAuthenticationStrategy =
                http.getSharedObject(SessionAuthenticationStrategy.class);
        if (sessionAuthenticationStrategy != null) {
            this.authFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        }

        RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            this.authFilter.setRememberMeServices(rememberMeServices);
        }

        F filter = postProcess(this.authFilter);
        http.addFilterBefore(filter, LogoutFilter.class);
    }

    public final <T> T getBeanOrNull(ApplicationContext applicationContext, Class<T> beanType) {
        String[] beanNames = applicationContext.getBeanNamesForType(beanType);
        if (beanNames.length == 1) {
            return applicationContext.getBean(beanNames[0], beanType);
        }
        return null;
    }

    /**
     * Gets the Authentication Filter
     *
     * @return the Authentication Filter
     */
    protected final F getAuthenticationFilter() {
        return this.authFilter;
    }

    /**
     * Sets the Authentication Filter
     *
     * @param authFilter the Authentication Filter
     */
    protected final void setAuthenticationFilter(F authFilter) {
        this.authFilter = authFilter;
    }

    /**
     * Gets the Authentication Entry Point
     *
     * @return the Authentication Entry Point
     */
    protected final AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return this.authenticationEntryPoint;
    }

    /**
     * Gets the URL to submit an authentication request to (i.e. where username/password must be
     * submitted)
     *
     * @return the URL to submit an authentication request to
     */
    protected final String getLoginProcessingUrl() {
        return this.loginProcessingUrl;
    }

    /**
     * Gets the URL to send users to if authentication fails
     *
     * @return the URL to send users if authentication fails (e.g. "/login?error").
     */
    protected final String getFailureUrl() {
        return this.failureUrl;
    }

    /** Updates the default values for access. */
    protected final void updateAccessDefaults(H http) {
        if (this.permitAll) {
            PermitAllSupport.permitAll(http, this.loginProcessingUrl, this.failureUrl);
        }
    }

    @SuppressWarnings("unchecked")
    private C getSelf() {
        return (C) this;
    }

    static final class PermitAllSupport {

        private PermitAllSupport() {}

        private static void permitAll(HttpSecurityBuilder<? extends HttpSecurityBuilder<?>> http, String... urls) {
            for (String url : urls) {
                if (url != null) {
                    permitAll(http, new ExactUrlRequestMatcher(url));
                }
            }
        }

        @SuppressWarnings("unchecked")
        static void permitAll(
                HttpSecurityBuilder<? extends HttpSecurityBuilder<?>> http, RequestMatcher... requestMatchers) {
			AuthorizeHttpRequestsConfigurer<?> configurer =
                    http.getConfigurer(AuthorizeHttpRequestsConfigurer.class);
            Assert.state(configurer != null, "permitAll only works with HttpSecurity.authorizeRequests()");
            configurer.getRegistry().requestMatchers(requestMatchers).permitAll();
        }

        private static final class ExactUrlRequestMatcher implements RequestMatcher {

            private final String processUrl;

            private ExactUrlRequestMatcher(String processUrl) {
                this.processUrl = processUrl;
            }

            @Override
            public boolean matches(HttpServletRequest request) {
                String uri = request.getRequestURI();
                String query = request.getQueryString();
                if (query != null) {
                    uri += "?" + query;
                }
                if ("".equals(request.getContextPath())) {
                    return uri.equals(this.processUrl);
                }
                return uri.equals(request.getContextPath() + this.processUrl);
            }

            @Override
            public String toString() {
                return "ExactUrl [processUrl='" + this.processUrl + "']";
            }
        }
    }
}
