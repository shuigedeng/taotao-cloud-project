package com.taotao.cloud.security.taox.oauth.resource;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.ResponseUtil;
import com.taotao.cloud.security.taox.annotation.NotAuth;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Log4j2
@Configuration
@Order(101)
public class Oauth2ResourceSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			// 白名单,不需要登录也可以访问
			.authorizeRequests(registry -> {
				permitAllUrls(registry, http.getSharedObject(ApplicationContext.class));
				registry.anyRequest().authenticated(); // 其他需要先登录再访问
			})
			// 资源服务器
			.oauth2ResourceServer(config -> config.authenticationEntryPoint(this::commence)
				.bearerTokenResolver(bearerTokenResolver())
				.jwt());
	}

	private void permitAllUrls(
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry,
		ApplicationContext ac) {
		List<String> permitAllUrls = new ArrayList<>(Arrays.asList(
			"/swagger-ui.html",
			"/v2/**",
			"/swagger-resources/**",
			"/webjars/**",
			"/resources/**"));

		RequestMappingHandlerMapping mapping = ac.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		// 收集 NotAuth 注解的接口
		map.keySet().forEach(info -> {
			HandlerMethod handlerMethod = map.get(info);

			Set<NotAuth> set = new HashSet<>();
			set.add(AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), NotAuth.class));
			set.add(AnnotationUtils.findAnnotation(handlerMethod.getMethod(), NotAuth.class));
			set.forEach(annotation -> {
				Optional.ofNullable(annotation).ifPresent(
					inner -> permitAllUrls.addAll(info.getPatternsCondition().getPatterns()));
			});
		});

		permitAllUrls.forEach(url -> registry.antMatchers(url).permitAll());

		log.info("permit all urls: {}", permitAllUrls);
	}

	/**
	 * 启用参数传递token
	 */
	private DefaultBearerTokenResolver bearerTokenResolver() {
		DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
		defaultBearerTokenResolver.setAllowFormEncodedBodyParameter(true);
		defaultBearerTokenResolver.setAllowUriQueryParameter(true);
		return defaultBearerTokenResolver;
	}

	private void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException)
		throws IOException {

		if (!response.isCommitted()) {

			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpServletResponse.SC_OK);

			Result<String> r = Result.fail(authException.getMessage());

			ResponseUtil.fail(response, r);
		}
	}


}
