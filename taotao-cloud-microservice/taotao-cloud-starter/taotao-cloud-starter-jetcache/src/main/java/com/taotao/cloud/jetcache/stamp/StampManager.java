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

import com.alicp.jetcache.AutoReleaseLock;
import com.taotao.cloud.jetcache.exception.StampDeleteFailedException;
import com.taotao.cloud.jetcache.exception.StampHasExpiredException;
import com.taotao.cloud.jetcache.exception.StampMismatchException;
import com.taotao.cloud.jetcache.exception.StampParameterIllegalException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>Description: Stamp 服务接口 </p>
 * <p>
 * 此Stamp非OAuth2 Stamp。而是用于在特定条件下生成后，在一定时间就会消除的标记性Stamp。 例如，幂等、短信验证码、Auth
 * State等，用时生成，然后进行验证，之后再删除的标记Stamp。
 *
 * @param <K> 签章缓存对应Key值的类型。
 * @param <V> 签章缓存存储数据，对应的具体存储值的类型，
 * @author : gengwei.zheng
 * @date : 2021/8/22 15:00
 */
public interface StampManager<K, V> extends InitializingBean {

	/**
	 * 过期时间
	 *
	 * @return {@link Duration}
	 */
	Duration getExpire();

	/**
	 * 保存与Key对应的Stamp签章值
	 *
	 * @param key              存储Key
	 * @param value            与Key对应的Stamp
	 * @param expireAfterWrite 过期时间
	 * @param timeUnit         过期时间单位
	 */
	void put(K key, V value, long expireAfterWrite, TimeUnit timeUnit);

	/**
	 * 保存与Key对应的Stamp签章值
	 *
	 * @param key    存储Key
	 * @param value  与Key对应的Stamp值
	 * @param expire 过期时间{@link Duration}
	 */
	default void put(K key, V value, Duration expire) {
		put(key, value, expire.toMillis(), TimeUnit.MILLISECONDS);
	}

	/**
	 * 保存与Key对应的Stamp签章值
	 *
	 * @param key   存储Key
	 * @param value 与Key对应的Stamp值
	 */
	default void put(K key, V value) {
		put(key, value, getExpire());
	}

	/**
	 * 生成缓存值策略方法，该方法负责生成具体存储的值。
	 *
	 * @param key 签章存储Key值
	 * @return {@link String}
	 */
	V nextStamp(K key);

	/**
	 * 创建具体的Stamp签章值，并存储至本地缓存
	 *
	 * @param key              签章存储Key值
	 * @param expireAfterWrite 写入之后过期时间。注意：该值每次写入都会覆盖。如果有一个时间周期内的反复存取操作，需要手动计算时间差。
	 * @param timeUnit         时间单位
	 * @return 创建的签章值
	 */
	default V create(K key, long expireAfterWrite, TimeUnit timeUnit) {
		V value = this.nextStamp(key);
		this.put(key, value, expireAfterWrite, timeUnit);
		return value;
	}

	/**
	 * 创建具体的Stamp签章值，并存储至本地缓存
	 *
	 * @param key    签章存储Key值
	 * @param expire 过期时间{@link Duration}
	 * @return 创建的签章值
	 */
	default V create(K key, Duration expire) {
		return create(key, expire.toMillis(), TimeUnit.MILLISECONDS);
	}

	/**
	 * 创建具体的Stamp签章值，并存储至本地缓存
	 *
	 * @param key 与签章存储Key值
	 * @return 创建的签章值
	 */
	default V create(K key) {
		return create(key, getExpire());
	}

	/**
	 * 校验Stamp值，与本地存储的Stamp 是否匹配
	 *
	 * @param key   与Stamp对应的Key值
	 * @param value 外部传入的Stamp值
	 * @return ture 匹配，false 不匹配
	 * @throws StampParameterIllegalException 传入Stamp错误
	 * @throws StampHasExpiredException       本地数据中没有Stamp或者Stamp已经过期。
	 * @throws StampMismatchException         Stamp与本地存储值不匹配
	 */
	boolean check(K key, V value)
		throws StampParameterIllegalException, StampHasExpiredException, StampMismatchException;

	/**
	 * 根据key读取Stamp
	 *
	 * @param key 存储数据Key值
	 * @return 存储的Stamp值
	 */
	V get(K key);

	/**
	 * 删除与Key对应的Stamp
	 *
	 * @param key 存储数据Key值
	 * @throws StampDeleteFailedException Stamp删除错误
	 */
	void delete(K key) throws StampDeleteFailedException;

	default boolean containKey(K key) {
		V value = get(key);
		return ObjectUtils.isNotEmpty(value);
	}

	/**
	 * 锁定值
	 * <p>
	 * 非堵塞的尝试获取一个锁，如果对应的key还没有锁，返回一个AutoReleaseLock，否则立即返回空。如果Cache实例是本地的，它是一个本地锁，在本JVM中有效；如果是redis等远程缓存，它是一个不十分严格的分布式锁。锁的超时时间由expire和timeUnit指定。多级缓存的情况会使用最后一级做tryLock操作。
	 *
	 * @param key      存储Key
	 * @param expire   过期时间
	 * @param timeUnit 过期时间单位
	 * @return {@link AutoReleaseLock}
	 * @see <a href="https://github.com/alibaba/jetcache/wiki/CacheAPI_CN">JetCache Wiki</a>
	 */
	AutoReleaseLock lock(K key, long expire, TimeUnit timeUnit);

	/**
	 * 锁定值
	 * <p>
	 * 非堵塞的尝试获取一个锁，如果对应的key还没有锁，返回一个AutoReleaseLock，否则立即返回空。如果Cache实例是本地的，它是一个本地锁，在本JVM中有效；如果是redis等远程缓存，它是一个不十分严格的分布式锁。锁的超时时间由expire和timeUnit指定。多级缓存的情况会使用最后一级做tryLock操作。
	 *
	 * @param key    存储Key
	 * @param expire 过期时间{@link Duration}
	 * @return {@link AutoReleaseLock}
	 * @see <a href="https://github.com/alibaba/jetcache/wiki/CacheAPI_CN">JetCache Wiki</a>
	 */
	default AutoReleaseLock lock(K key, Duration expire) {
		return lock(key, expire.toMillis(), TimeUnit.MILLISECONDS);
	}

	/**
	 * 锁定值
	 * <p>
	 * 非堵塞的尝试获取一个锁，如果对应的key还没有锁，返回一个AutoReleaseLock，否则立即返回空。如果Cache实例是本地的，它是一个本地锁，在本JVM中有效；如果是redis等远程缓存，它是一个不十分严格的分布式锁。锁的超时时间由expire和timeUnit指定。多级缓存的情况会使用最后一级做tryLock操作。
	 * *
	 *
	 * @param key 存储Key
	 * @return {@link AutoReleaseLock}
	 * @see <a href="https://github.com/alibaba/jetcache/wiki/CacheAPI_CN">JetCache Wiki</a>
	 */
	default AutoReleaseLock lock(K key) {
		return lock(key, getExpire());
	}

	/**
	 * 锁定并执行操作
	 * <p>
	 * 非堵塞的尝试获取一个锁，如果对应的key还没有锁，返回一个AutoReleaseLock，否则立即返回空。如果Cache实例是本地的，它是一个本地锁，在本JVM中有效；如果是redis等远程缓存，它是一个不十分严格的分布式锁。锁的超时时间由expire和timeUnit指定。多级缓存的情况会使用最后一级做tryLock操作。
	 *
	 * @param key      存储Key
	 * @param expire   过期时间
	 * @param timeUnit 过期时间单位
	 * @param action   需要执行的操作 {@link Runnable}
	 * @return 是否执行成功
	 * @see <a href="https://github.com/alibaba/jetcache/wiki/CacheAPI_CN">JetCache Wiki</a>
	 */
	boolean lockAndRun(K key, long expire, TimeUnit timeUnit, Runnable action);

	/**
	 * 锁定并执行操作
	 * <p>
	 * 非堵塞的尝试获取一个锁，如果对应的key还没有锁，返回一个AutoReleaseLock，否则立即返回空。如果Cache实例是本地的，它是一个本地锁，在本JVM中有效；如果是redis等远程缓存，它是一个不十分严格的分布式锁。锁的超时时间由expire和timeUnit指定。多级缓存的情况会使用最后一级做tryLock操作。
	 *
	 * @param key    存储Key
	 * @param expire 过期时间{@link Duration}
	 * @param action 需要执行的操作 {@link Runnable}
	 * @return 是否执行成功
	 * @see <a href="https://github.com/alibaba/jetcache/wiki/CacheAPI_CN">JetCache Wiki</a>
	 */
	default boolean lockAndRun(K key, Duration expire, Runnable action) {
		return lockAndRun(key, expire.toMillis(), TimeUnit.MILLISECONDS, action);
	}

	/**
	 * 锁定并执行操作
	 * <p>
	 * 非堵塞的尝试获取一个锁，如果对应的key还没有锁，返回一个AutoReleaseLock，否则立即返回空。如果Cache实例是本地的，它是一个本地锁，在本JVM中有效；如果是redis等远程缓存，它是一个不十分严格的分布式锁。锁的超时时间由expire和timeUnit指定。多级缓存的情况会使用最后一级做tryLock操作。
	 *
	 * @param key    存储Key
	 * @param action 需要执行的操作 {@link Runnable}
	 * @return 是否执行成功
	 * @see <a href="https://github.com/alibaba/jetcache/wiki/CacheAPI_CN">JetCache Wiki</a>
	 */
	default boolean lockAndRun(K key, Runnable action) {
		return lockAndRun(key, getExpire(), action);
	}
}
