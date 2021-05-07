package com.taotao.cloud.security.taox.oauth.login;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Log4j2
@AllArgsConstructor
@Order(99)
public class Oauth2LoginSecurityConfigurer extends WebSecurityConfigurerAdapter {

	private final CustomOAuth2AuthenticationSuccessHandler successHandler;

	private final ClientRegistrationRepository repository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			// 授权登录
			.oauth2Login(config -> config.successHandler(successHandler)
				.authorizationEndpoint(this::authorizationEndpoint))
			// 退出登录
			.logout(config -> config.logoutSuccessHandler(new CustomLogoutSuccessHandler()));
	}

	/**
	 * 保存和恢复 REDIRECT_URI, 这里的 REDIRECT_URI 不是 oauth2的, 而是前后端域名独立时, 最终通过该域名回到前端 并且 携带上 access_token
	 * 参数
	 */
	private void authorizationEndpoint(
		OAuth2LoginConfigurer<HttpSecurity>.AuthorizationEndpointConfig authorization) {
		// 处理 /{registrationId}/oauth2/authorization/{registrationId}
		// 由于网关服务, 跳转需加前缀 spring.application.name, 将 name 和 registrationId 取名相同解决
		DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
			repository,
			OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);

		// 保存 redirect_url 参数
		resolver.setAuthorizationRequestCustomizer(build -> build.attributes(attributesConsumer -> {
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
			HttpServletRequest request = Objects.requireNonNull(attrs).getRequest();
			String redirectUri = request.getParameter(OAuth2ParameterNames.REDIRECT_URI);
			// todo 可以再这里做判断redirectUri,走白名单, 或者写死redirectUri
			assert StringUtils.isEmpty(redirectUri);
			attributesConsumer.put(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
		}));
		authorization.authorizationRequestResolver(resolver);

		// 恢复 redirect_url 参数
		authorization
			.authorizationRequestRepository(new CustomOAuth2AuthorizationRequestRepository());
	}
}
