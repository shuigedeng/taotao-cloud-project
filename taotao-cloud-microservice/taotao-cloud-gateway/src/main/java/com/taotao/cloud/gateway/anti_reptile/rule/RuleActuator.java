package com.taotao.cloud.gateway.anti_reptile.rule;

import java.util.List;
import org.springframework.web.server.ServerWebExchange;

public class RuleActuator {

	private List<AntiReptileRule> ruleList;

	public RuleActuator(List<AntiReptileRule> rules) {
		ruleList = rules;
	}

	/**
	 * 是否允许通过请求
	 *
	 * @param exchange 请求
	 * @return 请求是否允许通过
	 */
	public boolean isAllowed(ServerWebExchange exchange) {
		for (AntiReptileRule rule : ruleList) {
			if (rule.execute(exchange)) {
				return false;
			}
		}
		return true;
	}

	public void reset(ServerWebExchange exchange, String realRequestUri) {
		ruleList.forEach(rule -> rule.reset(exchange, realRequestUri));
	}
}
