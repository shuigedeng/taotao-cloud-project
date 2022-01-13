package com.taotao.cloud.gateway.anti_reptile.rule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.server.ServerWebExchange;

public interface AntiReptileRule {

	/**
	 * 反爬规则具体实现
	 *
	 * @param exchange  请求
	 * @return true为击中反爬规则
	 */
	boolean execute(ServerWebExchange exchange);

	/**
	 * 重置已记录规则
	 *
	 * @param exchange        请求
	 * @param realRequestUri 原始请求uri
	 */
	void reset(ServerWebExchange exchange, String realRequestUri);

	/**
	 * 规则优先级
	 *
	 * @return 优先级
	 */
	int getOrder();
}
