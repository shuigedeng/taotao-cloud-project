package com.taotao.cloud.gateway.authentication;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import com.taotao.cloud.gateway.exception.InvalidTokenException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * json服务器身份验证入口点
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-05 11:30:47
 */
public class JsonServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
		LogUtils.error(e, "user authentication error : {}", e.getMessage());

		if (e instanceof InvalidBearerTokenException) {
			return ResponseUtils.fail(exchange, "无效的token");
		}

		if (e instanceof InvalidTokenException) {
			return ResponseUtils.fail(exchange, e.getMessage());
		}

		return ResponseUtils.fail(exchange, ResultEnum.UNAUTHORIZED);
	}
}
