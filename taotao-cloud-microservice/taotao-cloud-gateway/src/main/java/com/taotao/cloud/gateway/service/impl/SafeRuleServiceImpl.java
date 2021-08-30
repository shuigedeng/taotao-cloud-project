package com.taotao.cloud.gateway.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.DateUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import com.taotao.cloud.gateway.rule.BlackList;
import com.taotao.cloud.gateway.rule.RuleConstant;
import com.taotao.cloud.gateway.service.IRuleCacheService;
import com.taotao.cloud.gateway.service.ISafeRuleService;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 安全规则业务实现类
 *
 * @author shuigedeng
 */
@Service
public class SafeRuleServiceImpl implements ISafeRuleService {

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Autowired
	private IRuleCacheService ruleCacheService;

	@Override
	public Mono<Void> filterBlackList(ServerWebExchange exchange) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();

		try {
			URI originUri = getOriginRequestUri(exchange);
			String requestIp = RequestUtil.getRemoteAddr(request);
			String requestMethod = request.getMethodValue();
			AtomicBoolean forbid = new AtomicBoolean(false);
			// 从缓存中获取黑名单信息
			Set<Object> blackLists = ruleCacheService.getBlackList(requestIp);
			blackLists.addAll(ruleCacheService.getBlackList());
			// 检查是否在黑名单中
			checkBlackLists(forbid, blackLists, originUri, requestMethod);
			LogUtil.debug("黑名单检查完成 - {0}", stopwatch.stop());
			if (forbid.get()) {
				LogUtil.info("属于黑名单地址 - {0}", originUri.getPath());
				return ResponseUtil.fail(exchange, "已列入黑名单，访问受限");
			}
		} catch (Exception e) {
			LogUtil.error("黑名单检查异常: {0} - {1}", e.getMessage(), stopwatch.stop());
		}
		return null;
	}

	/**
	 * 获取网关请求URI
	 *
	 * @param exchange ServerWebExchange
	 * @return URI
	 */
	private URI getOriginRequestUri(ServerWebExchange exchange) {
		return exchange.getRequest().getURI();
	}

	/**
	 * 检查是否满足黑名单的条件
	 *
	 * @param forbid        是否黑名单判断
	 * @param blackLists    黑名列表
	 * @param uri           资源
	 * @param requestMethod 请求方法
	 */
	private void checkBlackLists(AtomicBoolean forbid, Set<Object> blackLists, URI uri,
		String requestMethod) {
		for (Object bl : blackLists) {
			BlackList blackList = JSONObject.parseObject(bl.toString(), BlackList.class);
			if (antPathMatcher.match(blackList.getRequestUri(), uri.getPath())
				&& RuleConstant.BLACKLIST_OPEN.equals(blackList.getStatus())) {
				if (RuleConstant.ALL.equalsIgnoreCase(blackList.getRequestMethod())
					|| StringUtils.equalsIgnoreCase(requestMethod, blackList.getRequestMethod())) {
					if (StringUtil.isNotBlank(blackList.getStartTime()) && StringUtil
						.isNotBlank(blackList.getEndTime())) {
						if (DateUtil.between(
							DateUtil.parseLocalTime(blackList.getStartTime(),
								CommonConstant.DATETIME_FORMAT),
							DateUtil.parseLocalTime(blackList.getEndTime(),
								CommonConstant.DATETIME_FORMAT))) {
							forbid.set(Boolean.TRUE);
						}
					} else {
						forbid.set(Boolean.TRUE);
					}
				}
			}
			if (forbid.get()) {
				break;
			}
		}
	}
}
