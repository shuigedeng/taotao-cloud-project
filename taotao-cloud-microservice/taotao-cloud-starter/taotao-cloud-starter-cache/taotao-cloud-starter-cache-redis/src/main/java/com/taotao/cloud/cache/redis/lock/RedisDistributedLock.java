/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.cache.redis.lock;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁实现
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 21:16:14
 * @deprecated 建议使用Redisson的实现方式 {@link RedissonDistributedLock}
 */
@ConditionalOnClass(RedisTemplate.class)
@Deprecated
public class RedisDistributedLock {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private ThreadLocal<String> lockFlag = new ThreadLocal<>();

	private static final String UNLOCK_LUA;

	/*
	 * 通过lua脚本释放锁,来达到释放锁的原子操作
	 */
	static {
		UNLOCK_LUA = "if redis.call(\"get\",KEYS[1]) == ARGV[1] " +
			"then " +
			"    return redis.call(\"del\",KEYS[1]) " +
			"else " +
			"    return 0 " +
			"end ";
	}

	public RedisDistributedLock(RedisTemplate<String, Object> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 获取锁
	 *
	 * @param key         锁的key
	 * @param expire      获取锁超时时间
	 * @param retryTimes  重试次数
	 * @param sleepMillis 获取锁失败的重试间隔
	 * @return 成功/失败
	 */
	public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
		boolean result = setRedis(key, expire);
		// 如果获取锁失败，按照传入的重试次数进行重试
		while ((!result) && retryTimes-- > 0) {
			try {
				LogUtils.debug("get redisDistributeLock failed, retrying..." + retryTimes);
				Thread.sleep(sleepMillis);
			} catch (InterruptedException e) {
				LogUtils.error("Interrupted!", e);
				Thread.currentThread().interrupt();
			}
			result = setRedis(key, expire);
		}
		return result;
	}

	private boolean setRedis(final String key, final long expire) {
		try {
			boolean status = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
				String uuid = UUID.randomUUID().toString();
				lockFlag.set(uuid);
				byte[] keyByte = redisTemplate.getStringSerializer().serialize(key);
				byte[] uuidByte = redisTemplate.getStringSerializer().serialize(uuid);
				boolean result = connection
					.set(keyByte, uuidByte, Expiration.from(expire, TimeUnit.MILLISECONDS),
						RedisStringCommands.SetOption.ifAbsent());
				return result;
			});
			return status;
		} catch (Exception e) {
			LogUtils.error("set redisDistributeLock occured an exception", e);
		}
		return false;
	}

	/**
	 * 释放锁
	 *
	 * @param key 锁的key
	 * @return 成功/失败
	 */
	public boolean releaseLock(String key) {
		// 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
		try {
			// 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
			// spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
			return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
				byte[] scriptByte = redisTemplate.getStringSerializer().serialize(UNLOCK_LUA);
				return connection.eval(scriptByte, ReturnType.BOOLEAN, 1
					, redisTemplate.getStringSerializer().serialize(key)
					, redisTemplate.getStringSerializer().serialize(lockFlag.get()));
			});
		} catch (Exception e) {
			LogUtils.error("release redisDistributeLock occured an exception", e);
		} finally {
			lockFlag.remove();
		}
		return false;
	}
}
