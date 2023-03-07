package com.taotao.cloud.gateway.filter.global;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.gateway.properties.FilterProperties;
import com.taotao.cloud.gateway.service.ISafeRuleService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(prefix = FilterProperties.PREFIX, name = "blacklist", havingValue = "true", matchIfMissing = true)
public class BlacklistFilter implements WebFilter {

	private final ISafeRuleService safeRuleService;

	public BlacklistFilter(ISafeRuleService safeRuleService) {
		this.safeRuleService = safeRuleService;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		//是否开启黑名单
		//从redis里查询黑名单是否存在
		LogUtils.debug("进入黑名单模式");

		// 检查黑名单
		Mono<Void> result = safeRuleService.filterBlackList(exchange);
		if (result != null) {
			return result;
		}

		//增加CORS
		//解决前端登录跨域的问题
		ServerHttpRequest request = exchange.getRequest();
		if (CorsUtils.isCorsRequest(request)) {
			ServerHttpResponse response = exchange.getResponse();
			HttpHeaders headers = response.getHeaders();
			headers.add("Access-Control-Allow-Origin", "*");
			headers.add("Access-Control-Allow-Methods", "*");
			headers.add("Access-Control-Max-Age", "3600");
			headers.add("Access-Control-Allow-Headers", "*");
			if (request.getMethod() == HttpMethod.OPTIONS) {
				response.setStatusCode(HttpStatus.OK);
				return Mono.empty();
			}
		}
		return chain.filter(exchange);
	}
}
