/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.jetcache.stamp;

import cn.hutool.crypto.SecureUtil;
import com.alicp.jetcache.anno.CacheType;
import com.taotao.cloud.jetcache.exception.MaximumLimitExceededException;
import java.time.Duration;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <p>Description: 计数类型的缓存 </p>
 * <p>
 * 这里的泛型使用了 Long 主要是为了兼顾存储 System.currentTimeMillis()。否则类型不一致，还要建两个 Stamp
 *
 * @author : gengwei.zheng
 * @date : 2022/7/6 22:59
 */
public abstract class AbstractCountStampManager extends AbstractStampManager<String, Long> {

	private static final Logger log = LoggerFactory.getLogger(AbstractCountStampManager.class);

	public AbstractCountStampManager(String cacheName) {
		super(cacheName);
	}

	public AbstractCountStampManager(String cacheName, CacheType cacheType) {
		super(cacheName, cacheType);
	}

	public AbstractCountStampManager(String cacheName, CacheType cacheType, Duration expire) {
		super(cacheName, cacheType, expire);
	}

	/**
	 * 在缓存有效期内进行计数
	 *
	 * @param identity 缓存 Key 的区分标识
	 * @param maxTimes 允许的最大限制次数
	 * @return 当前错误次数
	 * @throws MaximumLimitExceededException 超出最大限制次数错误
	 */
	public int counting(String identity, int maxTimes) throws MaximumLimitExceededException {
		return counting(identity, maxTimes, null);
	}

	/**
	 * 在缓存有效期内进行计数
	 *
	 * @param identity 缓存 Key 的区分标识
	 * @param maxTimes 允许的最大限制次数
	 * @param expire   过期时间
	 * @return 当前错误次数
	 * @throws MaximumLimitExceededException 超出最大限制次数错误
	 */
	public int counting(String identity, int maxTimes, Duration expire)
		throws MaximumLimitExceededException {
		return counting(identity, maxTimes, expire, false);
	}

	/**
	 * 在缓存有效期内进行计数
	 *
	 * @param identity 缓存 Key 的区分标识
	 * @param maxTimes 允许的最大限制次数
	 * @param expire   过期时间
	 * @param function 用于在日志中区分是哪个功能在调用。
	 * @return 当前错误次数
	 * @throws MaximumLimitExceededException 超出最大限制次数错误
	 */
	public int counting(String identity, int maxTimes, Duration expire, String function)
		throws MaximumLimitExceededException {
		return counting(identity, maxTimes, expire, false, function);
	}

	/**
	 * 在缓存有效期内进行计数
	 *
	 * @param identity 缓存 Key 的区分标识
	 * @param maxTimes 允许的最大限制次数
	 * @param expire   过期时间
	 * @param useMd5   是否用 MD5 对区分标识进行混淆加密
	 * @return 当前错误次数
	 * @throws MaximumLimitExceededException 超出最大限制次数错误
	 */
	public int counting(String identity, int maxTimes, Duration expire, boolean useMd5)
		throws MaximumLimitExceededException {
		return counting(identity, maxTimes, expire, useMd5, "AbstractCountStampManager");
	}

	/**
	 * 在缓存有效期内进行计数
	 *
	 * @param identity 缓存 Key 的区分标识
	 * @param maxTimes 允许的最大限制次数
	 * @param expire   过期时间
	 * @param useMd5   是否用 MD5 对区分标识进行混淆加密
	 * @param function 用于在日志中区分是哪个功能在调用。
	 * @return 当前错误次数
	 * @throws MaximumLimitExceededException 超出最大限制次数错误
	 */
	public int counting(String identity, int maxTimes, Duration expire, boolean useMd5,
		String function) throws MaximumLimitExceededException {
		Assert.notNull(identity, "identity cannot be null");

		String key = useMd5 ? SecureUtil.md5(identity) : identity;
		String expireKey = key + "_expire";

		Long index = get(key);

		if (ObjectUtils.isEmpty(index)) {
			index = 0L;
		}

		if (index == 0) {
			// 第一次读取剩余次数，因为缓存中还没有值，所以先创建缓存，同时缓存中计数为1。
			if (ObjectUtils.isNotEmpty(expire) && !expire.isZero()) {
				// 如果传入的 expire 不为零，那么就用 expire 参数值
				create(key, expire);
				put(expireKey, System.currentTimeMillis(), expire);
			} else {
				// 如果没有传入 expire 值，那么就默认使用 StampManager 自身配置的过期时间
				create(key);
				put(expireKey, System.currentTimeMillis());
			}
		} else {
			// 不管是注解上配置Duration值还是StampProperties中配置的Duration值，是不会变的
			// 所以第一次存入expireKey对应的System.currentTimeMillis()时间后，这个值也不应该变化。
			// 因此，这里只更新访问次数的标记值
			Duration newDuration = calculateRemainingTime(expire, expireKey, function);
			put(key, index + 1L, newDuration);

			// times 计数相当于数组的索引是 从0~n，所以需要
			if (index == maxTimes - 1) {
				throw new MaximumLimitExceededException(
					"Requests are too frequent. Please try again later!");
			}
		}

		int times = Long.valueOf(index + 1L).intValue();
		log.debug("[Herodotus] |- {} has been recorded [{}] times.", function, times);
		return times;
	}

	/**
	 * 计算剩余过期时间
	 * <p>
	 * 每次create或者put，缓存的过期时间都会被覆盖。（注意：Jetcache put 方法的参数名：expireAfterWrite）。
	 * 因为Jetcache没有Redis的incr之类的方法，那么每次放入Times值，都会更新过期时间，实际操作下来是变相的延长了过期时间。
	 *
	 * @param configuredDuration 注解上配置的、且可以正常解析的Duration值
	 * @param expireKey          时间标记存储Key值。
	 * @return 还剩余的过期时间 {@link Duration}
	 */
	private Duration calculateRemainingTime(Duration configuredDuration, String expireKey,
		String function) {
		Long begin = get(expireKey);
		Long current = System.currentTimeMillis();
		long interval = current - begin;

		log.debug("[Herodotus] |- {} operation interval [{}] millis.", function, interval);

		Duration duration;
		if (!configuredDuration.isZero()) {
			duration = configuredDuration.minusMillis(interval);
		} else {
			duration = getExpire().minusMillis(interval);
		}

		return duration;
	}
}
