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

package com.taotao.cloud.auth.biz.authentication.login.extension;

import com.taotao.cloud.security.springsecurity.core.response.SecurityAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

/**
 * 认证过滤器{@link AbstractAuthenticationProcessingFilter}s
 *
 * @author shuigedeng
 * @version 2023.07
 * @see AbstractHttpConfigurer
 * @since 2023-07-10 17:41:35
 */
public abstract class AbstractExtensionLoginFilterConfigurer<
	H extends HttpSecurityBuilder<H>,
	C extends AbstractExtensionLoginFilterConfigurer<H, C, F, A>,
	F extends AbstractAuthenticationProcessingFilter,
	A extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H>>
	extends AbstractHttpConfigurer<AbstractExtensionLoginFilterConfigurer<H, C, F, A>, H> {

	/**
	 * 配置器适配器
	 */
	private final A configurerAdapter;

	/**
	 * 授权过滤器
	 */
	private F authFilter;

	/**
	 * 身份验证详细信息来源
	 */
	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

	/**
	 * 成功处理程序
	 */
	private AuthenticationSuccessHandler successHandler;

	/**
	 * 认证入口点
	 */
	private AuthenticationEntryPoint authenticationEntryPoint;

	/**
	 * 登录处理url
	 */
	private String loginProcessingUrl;

	/**
	 * 故障处理程序
	 */
	private AuthenticationFailureHandler failureHandler;

	/**
	 * 允许所有
	 */
	private boolean permitAll;

	/**
	 * 失效网址
	 */
	private String failureUrl;

	/**
	 * 抽象扩展登录过滤器配置程序
	 *
	 * @param configurerAdapter         配置器适配器
	 * @param authenticationFilter      身份验证过滤器
	 * @param defaultLoginProcessingUrl 默认登录处理url
	 * @return
	 * @since 2023-07-10 17:41:36
	 */
	public AbstractExtensionLoginFilterConfigurer(
		A configurerAdapter, F authenticationFilter, String defaultLoginProcessingUrl) {
		this.configurerAdapter = configurerAdapter;
		this.authFilter = authenticationFilter;
		if (defaultLoginProcessingUrl != null) {
			loginProcessingUrl(defaultLoginProcessingUrl);
		}
	}

	/**
	 * 默认成功url
	 *
	 * @param defaultSuccessUrl 默认成功url
	 * @return {@link C }
	 * @since 2023-07-10 17:41:37
	 */
	public final C defaultSuccessUrl(String defaultSuccessUrl) {
		return defaultSuccessUrl(defaultSuccessUrl, false);
	}

	/**
	 * 默认成功url
	 *
	 * @param defaultSuccessUrl 默认成功url
	 * @param alwaysUse         始终使用
	 * @return {@link C }
	 * @since 2023-07-10 17:41:37
	 */
	public final C defaultSuccessUrl(String defaultSuccessUrl, boolean alwaysUse) {
		SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
		handler.setDefaultTargetUrl(defaultSuccessUrl);
		handler.setAlwaysUseDefaultTargetUrl(alwaysUse);
		return successHandler(handler);
	}

	/**
	 * 登录处理url
	 *
	 * @param loginProcessingUrl 登录处理url
	 * @return {@link C }
	 * @since 2023-07-10 17:41:37
	 */
	public C loginProcessingUrl(String loginProcessingUrl) {
		this.loginProcessingUrl = loginProcessingUrl;
		this.authFilter.setRequiresAuthenticationRequestMatcher(
			createLoginProcessingUrlMatcher(loginProcessingUrl));
		return getSelf();
	}

	/**
	 * 创建登录处理url匹配器
	 *
	 * @param loginProcessingUrl 登录处理url
	 * @return {@link RequestMatcher }
	 * @since 2023-07-10 17:41:38
	 */
	protected abstract RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl);

	/**
	 * 身份验证详细信息来源
	 *
	 * @param authenticationDetailsSource 身份验证详细信息来源
	 * @return {@link C }
	 * @since 2023-07-10 17:41:38
	 */
	public final C authenticationDetailsSource(
		AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
		this.authenticationDetailsSource = authenticationDetailsSource;
		return getSelf();
	}

	/**
	 * 成功处理程序
	 *
	 * @param successHandler 成功处理程序
	 * @return {@link C }
	 * @since 2023-07-10 17:41:38
	 */
	public final C successHandler(AuthenticationSuccessHandler successHandler) {
		this.successHandler = successHandler;
		return getSelf();
	}

	/**
	 * 认证入口点
	 *
	 * @param authenticationEntryPoint 认证入口点
	 * @return {@link C }
	 * @since 2023-07-10 17:41:38
	 */
	public final C authenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
		return getSelf();
	}

	/**
	 * 允许所有
	 *
	 * @return {@link C }
	 * @since 2023-07-10 17:41:39
	 */
	public final C permitAll() {
		return permitAll(true);
	}

	/**
	 * 允许所有
	 *
	 * @param permitAll 允许所有
	 * @return {@link C }
	 * @since 2023-07-10 17:41:39
	 */
	public final C permitAll(boolean permitAll) {
		this.permitAll = permitAll;
		return getSelf();
	}

	/**
	 * 失效网址
	 *
	 * @param authenticationFailureUrl 认证失败url
	 * @return {@link C }
	 * @since 2023-07-10 17:41:39
	 */
	public final C failureUrl(String authenticationFailureUrl) {
		C result = failureHandler(
			new SimpleUrlAuthenticationFailureHandler(authenticationFailureUrl));
		this.failureUrl = authenticationFailureUrl;
		return result;
	}

	/**
	 * 故障处理程序
	 *
	 * @param authenticationFailureHandler 身份验证失败处理程序
	 * @return {@link C }
	 * @since 2023-07-10 17:41:40
	 */
	public final C failureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.failureUrl = null;
		this.failureHandler = authenticationFailureHandler;
		return getSelf();
	}

	/**
	 * 与
	 *
	 * @return {@link A }
	 * @since 2023-07-10 17:41:40
	 */
	public A with() {
		return this.configurerAdapter;
	}

	/**
	 * init
	 *
	 * @param http http
	 * @since 2023-07-10 17:41:40
	 */
	@Override
	public void init(H http) {
		updateAccessDefaults(http);
		// updateAuthenticationDefaults();
		registerDefaultAuthenticationEntryPoint(http);

		AuthenticationProvider authenticationProvider = authenticationProvider(http);
		http.authenticationProvider(postProcess(authenticationProvider));

		if (this.successHandler == null) {
			successHandler(defaultSuccessHandler(http));
		}
		if (this.failureHandler == null) {
			failureHandler(defaultFailureHandler(http));
		}
	}

	/**
	 * 身份验证提供程序
	 *
	 * @param http http
	 * @return {@link AuthenticationProvider }
	 * @since 2023-07-10 17:41:40
	 */
	protected abstract AuthenticationProvider authenticationProvider(H http);

	/**
	 * 默认成功处理程序
	 *
	 * @param http http
	 * @return {@link AuthenticationSuccessHandler }
	 * @since 2023-07-10 17:41:41
	 */
	protected abstract AuthenticationSuccessHandler defaultSuccessHandler(H http);

	/**
	 * 默认故障处理程序
	 *
	 * @param http http
	 * @return {@link AuthenticationFailureHandler }
	 * @since 2023-07-10 17:41:41
	 */
	protected abstract AuthenticationFailureHandler defaultFailureHandler(H http);

	/**
	 * 更新身份验证默认值
	 *
	 * @since 2023-07-10 17:41:41
	 */
	protected final void updateAuthenticationDefaults() {
		if (this.failureHandler == null) {
			failureHandler(
				new AuthenticationEntryPointFailureHandler(new SecurityAuthenticationEntryPoint()));
		}
	}

	/**
	 * 注册默认身份验证入口点
	 *
	 * @param http http
	 * @since 2023-07-10 17:41:42
	 */
	protected final void registerDefaultAuthenticationEntryPoint(H http) {
		if (authenticationEntryPoint != null) {
			this.authenticationEntryPoint = new SecurityAuthenticationEntryPoint();
		}
		registerAuthenticationEntryPoint(http, this.authenticationEntryPoint);
	}

	/**
	 * 注册认证入口点
	 *
	 * @param http                     http
	 * @param authenticationEntryPoint 认证入口点
	 * @since 2023-07-10 17:41:42
	 */
	@SuppressWarnings("unchecked")
	protected final void registerAuthenticationEntryPoint(H http,
		AuthenticationEntryPoint authenticationEntryPoint) {
		ExceptionHandlingConfigurer<H> exceptionHandling = http.getConfigurer(
			ExceptionHandlingConfigurer.class);
		if (exceptionHandling == null) {
			return;
		}
		exceptionHandling.defaultAuthenticationEntryPointFor(
			postProcess(authenticationEntryPoint), getAuthenticationEntryPointMatcher(http));
	}

	/**
	 * 获取身份验证入口点匹配器
	 *
	 * @param http http
	 * @return {@link RequestMatcher }
	 * @since 2023-07-10 17:41:43
	 */
	protected final RequestMatcher getAuthenticationEntryPointMatcher(H http) {
		ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(
			ContentNegotiationStrategy.class);
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
			new NegatedRequestMatcher(
				new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
		return new AndRequestMatcher(Arrays.asList(notXRequestedWith, mediaMatcher));
	}

	/**
	 * 配置
	 *
	 * @param http http
	 * @since 2023-07-10 17:41:43
	 */
	@Override
	public void configure(H http) throws Exception {
		PortMapper portMapper = http.getSharedObject(PortMapper.class);
		if (portMapper != null
			&& this.authenticationEntryPoint instanceof LoginUrlAuthenticationEntryPoint) {
			((LoginUrlAuthenticationEntryPoint) this.authenticationEntryPoint).setPortMapper(
				portMapper);
		}

		RequestCache requestCache = http.getSharedObject(RequestCache.class);
		if (requestCache != null
			&& this.successHandler instanceof SavedRequestAwareAuthenticationSuccessHandler) {
			((SavedRequestAwareAuthenticationSuccessHandler) this.successHandler).setRequestCache(
				requestCache);
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

	/**
	 * 获取bean或null
	 *
	 * @param applicationContext 应用程序上下文
	 * @param beanType           bean类型
	 * @return {@link T }
	 * @since 2023-07-10 17:41:44
	 */
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
	 * @return {@link F }
	 * @since 2023-07-10 17:41:44
	 */
	protected final F getAuthenticationFilter() {
		return this.authFilter;
	}

	/**
	 * Sets the Authentication Filter
	 *
	 * @param authFilter the Authentication Filter
	 * @since 2023-07-10 17:41:44
	 */
	protected final void setAuthenticationFilter(F authFilter) {
		this.authFilter = authFilter;
	}

	/**
	 * Gets the Authentication Entry Point
	 *
	 * @return {@link AuthenticationEntryPoint }
	 * @since 2023-07-10 17:41:44
	 */
	protected final AuthenticationEntryPoint getAuthenticationEntryPoint() {
		return this.authenticationEntryPoint;
	}

	/**
	 * Gets the URL to submit an authentication request to (i.e. where username/password must be
	 * submitted)
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:41:44
	 */
	protected final String getLoginProcessingUrl() {
		return this.loginProcessingUrl;
	}

	/**
	 * Gets the URL to send users to if authentication fails
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:41:44
	 */
	protected final String getFailureUrl() {
		return this.failureUrl;
	}

	/**
	 * Updates the default values for access.
	 *
	 * @param http http
	 * @since 2023-07-10 17:41:44
	 */
	protected final void updateAccessDefaults(H http) {
		if (this.permitAll) {
			PermitAllSupport.permitAll(http, this.loginProcessingUrl, this.failureUrl);
		}
	}

	/**
	 * 获取自我
	 *
	 * @return {@link C }
	 * @since 2023-07-10 17:41:44
	 */
	@SuppressWarnings("unchecked")
	private C getSelf() {
		return (C) this;
	}

	/**
	 * 允许所有支持
	 *
	 * @author shuigedeng
	 * @version 2023.07
	 * @since 2023-07-10 17:41:45
	 */
	static final class PermitAllSupport {

		/**
		 * 允许所有支持
		 *
		 * @return
		 * @since 2023-07-10 17:41:45
		 */
		private PermitAllSupport() {
		}

		/**
		 * 允许所有
		 *
		 * @param http http
		 * @param urls 网址
		 * @since 2023-07-10 17:41:46
		 */
		private static void permitAll(HttpSecurityBuilder<? extends HttpSecurityBuilder<?>> http,
			String... urls) {
			for (String url : urls) {
				if (url != null) {
					permitAll(http, new ExactUrlRequestMatcher(url));
				}
			}
		}

		/**
		 * 允许所有
		 *
		 * @param http            http
		 * @param requestMatchers 请求匹配器
		 * @since 2023-07-10 17:41:46
		 */
		@SuppressWarnings("unchecked")
		static void permitAll(
			HttpSecurityBuilder<? extends HttpSecurityBuilder<?>> http,
			RequestMatcher... requestMatchers) {
			AuthorizeHttpRequestsConfigurer<?> configurer = http.getConfigurer(
				AuthorizeHttpRequestsConfigurer.class);
			Assert.state(configurer != null,
				"permitAll only works with HttpSecurity.authorizeRequests()");
			configurer.getRegistry().requestMatchers(requestMatchers).permitAll();
		}

		/**
		 * 精确网址请求匹配器
		 *
		 * @author shuigedeng
		 * @version 2023.07
		 * @see RequestMatcher
		 * @since 2023-07-10 17:41:46
		 */
		private static final class ExactUrlRequestMatcher implements RequestMatcher {

			private final String processUrl;

			/**
			 * 精确网址请求匹配器
			 *
			 * @param processUrl 进程url
			 * @return
			 * @since 2023-07-10 17:41:47
			 */
			private ExactUrlRequestMatcher(String processUrl) {
				this.processUrl = processUrl;
			}

			/**
			 * 匹配
			 *
			 * @param request 请求
			 * @return boolean
			 * @since 2023-07-10 17:41:47
			 */
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
