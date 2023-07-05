package com.taotao.cloud.gateway.authentication;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * json服务器拒绝访问处理程序
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-05 11:30:52
 */
public class JsonServerAccessDeniedHandler implements ServerAccessDeniedHandler {
	@Override
	public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
		LogUtils.error(e, "user access denied error : {}", e.getMessage());
		return ResponseUtils.fail(exchange, ResultEnum.FORBIDDEN);
	}
}
