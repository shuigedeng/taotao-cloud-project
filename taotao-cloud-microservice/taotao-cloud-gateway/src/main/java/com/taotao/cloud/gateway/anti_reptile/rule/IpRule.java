package com.taotao.cloud.gateway.anti_reptile.rule;

import com.taotao.cloud.common.utils.servlet.RequestUtil;
import com.taotao.cloud.gateway.anti_reptile.AntiReptileProperties;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author kl @kailing.pub
 * @since 2019/7/8
 */
public class IpRule extends AbstractRule {

	private final static Logger LOGGER = LoggerFactory.getLogger(IpRule.class);

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private AntiReptileProperties properties;

	private static final String RATELIMITER_COUNT_PREFIX = "ratelimiter_request_count";
	private static final String RATELIMITER_EXPIRATIONTIME_PREFIX = "ratelimiter_expirationtime";
	private static final String RATELIMITER_HIT_CRAWLERSTRATEGY = "ratelimiter_hit_crawlerstrategy";

	@Override
	protected boolean doExecute(ServerWebExchange exchange) {
		String ipAddress = RequestUtil.getServerHttpRequestIpAddress(exchange.getRequest());
		List<String> ignoreIpList = properties.getIpRule().getIgnoreIp();
		if (ignoreIpList != null && ignoreIpList.size() > 0) {
			for (String ignoreIp : ignoreIpList) {
				if (ignoreIp.endsWith("*")) {
					ignoreIp = ignoreIp.substring(0, ignoreIp.length() - 1);
				}
				if (ipAddress.startsWith(ignoreIp)) {
					return false;
				}
			}
		}

		String requestUrl = exchange.getRequest().getURI().getRawPath();
		//毫秒，默认5000
		int expirationTime = properties.getIpRule().getExpirationTime();
		//最高expirationTime时间内请求数
		int requestMaxSize = properties.getIpRule().getRequestMaxSize();
		RAtomicLong rRequestCount = redissonClient.getAtomicLong(
			RATELIMITER_COUNT_PREFIX.concat(requestUrl).concat(ipAddress));
		RAtomicLong rExpirationTime = redissonClient.getAtomicLong(
			RATELIMITER_EXPIRATIONTIME_PREFIX.concat(requestUrl).concat(ipAddress));
		if (!rExpirationTime.isExists()) {
			rRequestCount.set(0L);
			rExpirationTime.set(0L);
			rExpirationTime.expire(expirationTime, TimeUnit.MILLISECONDS);
		} else {
			RMap rHitMap = redissonClient.getMap(RATELIMITER_HIT_CRAWLERSTRATEGY);
			if ((rRequestCount.incrementAndGet() > requestMaxSize) || rHitMap.containsKey(
				ipAddress)) {
				//触发爬虫策略 ，默认10天后可重新访问
				long lockExpire = properties.getIpRule().getLockExpire();
				rExpirationTime.expire(lockExpire, TimeUnit.SECONDS);
				//保存触发来源
				rHitMap.put(ipAddress, requestUrl);
				LOGGER.info(
					"Intercepted request, uri: {}, ip：{}, request :{}, times in {} ms。Automatically unlock after {} seconds",
					requestUrl, ipAddress, requestMaxSize, expirationTime, lockExpire);
				return true;
			}
		}
		return false;
	}

	/**
	 * 重置已记录规则
	 *
	 * @param exchange       请求
	 * @param realRequestUri 原始请求uri
	 */
	@Override
	public void reset(ServerWebExchange exchange, String realRequestUri) {
		String ipAddress = RequestUtil.getServerHttpRequestIpAddress(exchange.getRequest());
		String requestUrl = realRequestUri;
		/**
		 * 重置计数器
		 */
		int expirationTime = properties.getIpRule().getExpirationTime();
		RAtomicLong rRequestCount = redissonClient.getAtomicLong(
			RATELIMITER_COUNT_PREFIX.concat(requestUrl).concat(ipAddress));
		RAtomicLong rExpirationTime = redissonClient.getAtomicLong(
			RATELIMITER_EXPIRATIONTIME_PREFIX.concat(requestUrl).concat(ipAddress));
		rRequestCount.set(0L);
		rExpirationTime.set(0L);
		rExpirationTime.expire(expirationTime, TimeUnit.MILLISECONDS);
		/**
		 * 清除记录
		 */
		RMap rHitMap = redissonClient.getMap(RATELIMITER_HIT_CRAWLERSTRATEGY);
		rHitMap.remove(ipAddress);
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
