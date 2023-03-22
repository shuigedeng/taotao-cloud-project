package com.taotao.cloud.log.biz.shortlink.component;

import com.taotao.cloud.log.biz.shortlink.adapter.ShortLinkGeneratorAdapter;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * 短链生成器 - 发号器方式生成短链
 * <p>
 * 1.发号器（10进制）转为62进制，生成短链。
 * <p>
 * 2.转62进制的原因：缩短长度，如值1000000000，转62进制后为6位长度
 * <p>
 * 3.发号器的起始值应为1000000000，低于此值无法生成6位短码
 * <p>
 * 6.依赖于redis，存在单点问题，且多了网络请求。适合业务规模小的场景
 * <p>
 * 7.当redis数据丢失，存在重复问题
 * <p>
 * 8.数据是自增的，暴露业务增长情况
 *
 * @since 2022/05/04
 */
@Slf4j
@Component
public class NumberSenderShortLinkGenerator implements ShortLinkGeneratorAdapter {

	@Resource
	private RedissonClient redissonClient;

	private static final String KEY_SHORT_LINK_GENERATOR = "KEY_SHORT_LINK_NUMBER_SENDER";

	@Override
	public String createShortLinkCode(String originUrl) {
		Long generateId = generateId();

//        CommonBizUtil.encodeToBase62(generateId);
		// 优化：发号器递增导致生成的值是有序的，暴露业务信息,将生成62进制的字符打乱，
		return CommonBizUtil.encodeToBase62OutOrder(generateId);
	}

	private Long generateId() {
		// AtomicLong单机性能不错，如果并发量特别大，就不适合了
		// 可用LongAdder，在高并发下表现优秀
		RAtomicLong atomicLong = redissonClient.getAtomicLong(KEY_SHORT_LINK_GENERATOR);
		if (!atomicLong.isExists()) {
			atomicLong.set(1000000000L);
			return 1000000000L;
		}

		return atomicLong.incrementAndGet();
	}
}
