package com.taotao.cloud.gateway.service;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 安全规则业务类
 *
 * @author shuigedeng
 */
public interface ISafeRuleService {

	/**
	 * 黑名单过滤
	 */
	Mono<Void> filterBlackList(ServerWebExchange exchange);
}
