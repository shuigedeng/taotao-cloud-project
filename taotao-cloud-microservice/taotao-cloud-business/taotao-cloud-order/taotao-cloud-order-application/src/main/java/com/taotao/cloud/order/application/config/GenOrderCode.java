package com.taotao.cloud.order.application.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GenOrderCode {

	// 创建Redisson客户端实例
	private final RedissonClient redissonClient;

	public GenOrderCode(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	/**
	 * 生成指定长度的订单号
	 *
	 * {@snippet :
	 * public ResultResponse<String> orderNo() {
	 * 		ResultResponse<String> resultResponse = ResultResponse.newSuccessInstance();
	 * 		LocalDate currentDate = LocalDate.now();
	 * 		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
	 * 		String orderPrefix = currentDate.format(formatter);
	 * 		//redisKey = Enum + ":" + orderPrefix;=> order:231201，每天的key都不一样，生成的订单号以天区分
	 * 		String key = "com" + orderPrefix;
	 * 		String orderCode = genOrderCode.genOrderCode(25, orderPrefix, key);
	 * }
	 * }
	 * @param length   订单总长度
	 * @param prefix   前缀 当天日期-yyMMdd
	 * @param lockKey 分布式锁id
	 * @return 订单号
	 */
	public String genOrderCode(int length, String prefix, String lockKey) {
		// 检查参数合法性
		if (length <= 0) {
			log.warn("获取订单号：订单总长度不能小于0");
			throw new RuntimeException("订单总长度或随机码长度不能小于0");
		}
		if (length <= prefix.length()) {
			log.warn("获取订单号：订单总长度长度小于前缀长度");
			throw new RuntimeException("订单总长度长度小于前缀长度");
		}

		// 获取分布式锁
		RLock lock = redissonClient.getLock(lockKey);
		lock.lock();
		try {
			// 从Redis中获取递增的序列号
			RAtomicLong counter = redissonClient.getAtomicLong("counter");
			// 递增计数器
			long incrementedValue = counter.incrementAndGet();
			String counterValue = String.valueOf(incrementedValue);
			int incrLength = counterValue.length();

			// 如果前缀长度加数字自增长度大于指定位数，则直接使用自增数据
			if (incrLength + prefix.length() > length) {
				return prefix + counterValue;
			}

			// 生成随机码
			int randomLength = length - incrLength;
			String randomAlphabetic = RandomStringUtils.randomAlphabetic(randomLength);
			// 格式化订单号
			String orderCode = prefix + randomAlphabetic + counterValue;
			log.info("根据规则生成的订单号:{}", orderCode);
			return orderCode;
		} finally {
			// 释放锁
			lock.unlock();
		}
	}
}
