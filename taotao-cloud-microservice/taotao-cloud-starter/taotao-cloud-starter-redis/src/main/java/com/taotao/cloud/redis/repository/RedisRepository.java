/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.redis.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.model.CacheHashKey;
import com.taotao.cloud.redis.model.CacheKey;
import com.taotao.cloud.redis.val.NullVal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Redis Repository redis 基本操作 可扩展
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:56:38
 */
public class RedisRepository {

	/**
	 * 默认编码
	 */
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/**
	 * key序列化
	 */
	//private static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();

	/**
	 * value 序列化
	 */
	//private static final JdkSerializationRedisSerializer OBJECT_SERIALIZER = new JdkSerializationRedisSerializer();

	/**
	 * KEY_LOCKS
	 */
	private static final Map<String, Object> KEY_LOCKS = new ConcurrentHashMap<>();

	/**
	 * Spring Redis Template
	 */
	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 全局配置是否缓存null值
	 */
	private final boolean defaultCacheNullVal;

	public RedisRepository(RedisTemplate<String, Object> redisTemplate, boolean cacheNullVal) {
		this.redisTemplate = redisTemplate;
		//this.redisTemplate.setKeySerializer(STRING_SERIALIZER);
		//this.redisTemplate.setValueSerializer(OBJECT_SERIALIZER);
		this.defaultCacheNullVal = cacheNullVal;
	}

	/**
	 * 获取链接工厂
	 */
	public RedisConnectionFactory getConnectionFactory() {
		return this.redisTemplate.getConnectionFactory();
	}

	/**
	 * 获取 RedisTemplate对象
	 */
	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	/**
	 * 清空DB
	 *
	 * @param node redis 节点
	 * @author shuigedeng
	 * @since 2021-09-07 20:59:47
	 */
	public void flushDB(RedisClusterNode node) {
		this.redisTemplate.opsForCluster().flushDb(node);
	}


	/**
	 * 发送数据
	 *
	 * @author shuigedeng
	 * @since 2021-09-07 20:59:30
	 */
	public void send(String channel, Object data) {
		redisTemplate.convertAndSend(channel, data);
	}

	/**
	 * 设置超时
	 *
	 * @param key key
	 * @author shuigedeng
	 * @since 2021-09-07 20:59:30
	 */
	public void setExpire(CacheKey key) {
		if (key != null && key.getExpire() != null) {
			redisTemplate.expire(key.getKey(), key.getExpire());
		}
	}

	/**
	 * 判断缓存值是否为空对象
	 *
	 * @param value value
	 * @param <T>   T
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-07 20:59:47
	 */
	public static <T> boolean isNullVal(T value) {
		boolean isNull = value == null || NullVal.class.equals(value.getClass());
		return isNull || value.getClass().equals(Object.class) || (value instanceof Map
			&& ((Map<?, ?>) value).isEmpty());
	}

	/**
	 * new 一个空值
	 *
	 * @return {@link com.taotao.cloud.redis.val.NullVal }
	 * @author shuigedeng
	 * @since 2021-09-07 21:01:20
	 */
	public NullVal newNullVal() {
		return new NullVal();
	}

	/**
	 * 返回正常值 or null
	 *
	 * @param value value
	 * @param <T>   T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-07 21:01:35
	 */
	public <T> T returnVal(T value) {
		return isNullVal(value) ? null : value;
	}

	/**
	 * 删除给定的一个 key 或 多个key 不存在的 key 会被忽略。
	 *
	 * @param keys 一定不能为 {@literal null}.
	 * @return key 被删除返回true
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/del">Redis Documentation: DEL</a>
	 * @since 2021-09-07 21:01:35
	 */
	public Long del(@NonNull CacheKey... keys) {
		return redisTemplate
			.delete(Arrays.stream(keys).map(CacheKey::getKey).collect(Collectors.toList()));
	}

	/**
	 * 从当前数据库中随机返回(不删除)一个 key 。
	 *
	 * @return 当数据库不为空时，返回一个 key 。 当数据库为空时，返回 nil 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/randomkey">Redis Documentation: RANDOMKEY</a>
	 * @since 2021-09-07 21:01:35
	 */
	public String randomKey() {
		return redisTemplate.randomKey();
	}

	/**
	 * 将 key 改名为 newkey 。 当 key 和 newkey 相同，或者 key 不存在时，返回一个错误。 当 newkey 已经存在时， RENAME 命令将覆盖旧值。
	 *
	 * @param oldKey 一定不能为 {@literal null}.
	 * @param newKey 一定不能为 {@literal null}.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/rename">Redis Documentation: RENAME</a>
	 * @since 2021-09-07 21:01:35
	 */
	public void rename(@NonNull String oldKey, @NonNull String newKey) {
		redisTemplate.rename(oldKey, newKey);
	}

	/**
	 * 删除给定的一个 key 或 多个key 不存在的 key 会被忽略。
	 *
	 * @param keys 一定不能为 {@literal null}.
	 * @return key 被删除返回true
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/del">Redis Documentation: DEL</a>
	 * @since 2021-09-07 21:01:35
	 */
	public Long del(@NonNull Collection<CacheKey> keys) {
		return redisTemplate
			.delete(keys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
	}

	/**
	 * 当且仅当 newkey 不存在时，将 key 改名为 newkey 。
	 *
	 * @param oldKey 一定不能为 {@literal null}.
	 * @param newKey 一定不能为 {@literal null}.
	 * @return 是否成功
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/renamenx">Redis Documentation: RENAMENX</a>
	 * @since 2021-09-07 21:01:46
	 */
	public Boolean renameNx(@NonNull String oldKey, String newKey) {
		return redisTemplate.renameIfAbsent(oldKey, newKey);
	}

	/**
	 * 将当前数据库的 key 移动到给定的数据库 db 当中。 如果当前数据库(源数据库)和给定数据库(目标数据库)有相同名字的给定 key ，或者 key 不存在于当前数据库，那么 MOVE
	 * 没有任何效果。 因此，也可以利用这一特性，将 MOVE 当作锁(locking)原语(primitive)。
	 *
	 * @param key     一定不能为 {@literal null}.
	 * @param dbIndex 数据库索引
	 * @return 是否成功
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/move">Redis Documentation: MOVE</a>
	 * @since 2021-09-07 21:01:46
	 */
	public Boolean move(@NonNull String key, int dbIndex) {
		return redisTemplate.move(key, dbIndex);
	}

	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。 在 Redis 中，带有生存时间的 key 被称为『易失的』(volatile)。
	 * <p>
	 * 生存时间可以通过使用 DEL 命令来删除整个 key 来移除，或者被 SET 和 GETSET 命令覆写(overwrite)，这意味着，如果一个命令只是修改(alter)一个带生存时间的
	 * key 的值而不是用一个新的 key 值来代替(replace)它的话，那么生存时间不会被改变。
	 * <p>
	 * 比如说，对一个 key 执行 INCR 命令，对一个列表进行 LPUSH 命令，或者对一个哈希表执行 HSET 命令，这类操作都不会修改 key 本身的生存时间。
	 * <p>
	 * 另一方面，如果使用 RENAME 对一个 key 进行改名，那么改名后的 key 的生存时间和改名前一样。
	 * <p>
	 * RENAME 命令的另一种可能是，尝试将一个带生存时间的 key 改名成另一个带生存时间的 another_key ，这时旧的 another_key
	 * (以及它的生存时间)会被删除，然后旧的 key 会改名为 another_key ，因此，新的 another_key 的生存时间也和原本的 key 一样。
	 * <p>
	 * 使用 PERSIST 命令可以在不删除 key 的情况下，移除 key 的生存时间，让 key 重新成为一个『持久的』(persistent) key 。
	 *
	 * @param key     一定不能为 {@literal null}.
	 * @param seconds 过期时间 单位：秒
	 * @return 是否成功
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
	 * @since 2021-09-07 21:01:46
	 */
	public Boolean expire(@NonNull String key, long seconds) {
		return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
	}

	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。 在 Redis 中，带有生存时间的 key 被称为『易失的』(volatile)。
	 * <p>
	 * 生存时间可以通过使用 DEL 命令来删除整个 key 来移除，或者被 SET 和 GETSET 命令覆写(overwrite)，这意味着，如果一个命令只是修改(alter)一个带生存时间的
	 * key 的值而不是用一个新的 key 值来代替(replace)它的话，那么生存时间不会被改变。
	 * <p>
	 * 比如说，对一个 key 执行 INCR 命令，对一个列表进行 LPUSH 命令，或者对一个哈希表执行 HSET 命令，这类操作都不会修改 key 本身的生存时间。
	 * <p>
	 * 另一方面，如果使用 RENAME 对一个 key 进行改名，那么改名后的 key 的生存时间和改名前一样。
	 * <p>
	 * RENAME 命令的另一种可能是，尝试将一个带生存时间的 key 改名成另一个带生存时间的 another_key ，这时旧的 another_key
	 * (以及它的生存时间)会被删除，然后旧的 key 会改名为 another_key ，因此，新的 another_key 的生存时间也和原本的 key 一样。
	 * <p>
	 * 使用 PERSIST 命令可以在不删除 key 的情况下，移除 key 的生存时间，让 key 重新成为一个『持久的』(persistent) key 。
	 *
	 * @param key     一定不能为 {@literal null}.
	 * @param timeout 过期时间
	 * @return 是否成功
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
	 * @since 2021-09-07 21:01:46
	 */
	public Boolean expire(@NonNull String key, @NonNull Duration timeout) {
		return expire(key, timeout.getSeconds());
	}

	/**
	 * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间。不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
	 *
	 * @param key  一定不能为 {@literal null}.
	 * @param date 过期时间
	 * @return 是否成功
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/expireat">Redis Documentation: EXPIREAT</a>
	 * @since 2021-09-07 21:01:46
	 */
	public Boolean expireAt(@NonNull String key, @NonNull Date date) {
		return redisTemplate.expireAt(key, date);
	}

	/**
	 * 这个命令和 EXPIRE 命令的作用类似，但是它以毫秒为单位设置 key 的生存时间，而不像 EXPIRE 命令那样，以秒为单位。
	 *
	 * @param key          一定不能为 {@literal null}.
	 * @param milliseconds 过期时间 单位： 毫米
	 * @return 是否成功
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/pexpire">Redis Documentation: PEXPIRE</a>
	 * @since 2021-09-07 21:01:46
	 */
	public Boolean pExpire(@NonNull String key, long milliseconds) {
		return redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);
	}

	/**
	 * 移除给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的 key )。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 是否成功
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/persist">Redis Documentation: PERSIST</a>
	 * @since 2021-09-07 21:01:46
	 */
	public Boolean persist(@NonNull String key) {
		return redisTemplate.persist(key);
	}


	/**
	 * 返回 key 所储存的值的类型。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return none (key不存在)、string (字符串)、list (列表)、set (集合)、zset (有序集)、hash (哈希表) 、stream （流）
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/type">Redis Documentation: TYPE</a>
	 * @since 2021-09-07 21:01:46
	 */
	public String typeCode(@NonNull String key) {
		DataType type = redisTemplate.type(key);
		return type == null ? DataType.NONE.code() : type.code();
	}

	public DataType type(@NonNull String key) {
		return redisTemplate.type(key);
	}

	/**
	 * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/ttl">Redis Documentation: TTL</a>
	 * @since 2021-09-07 21:01:46
	 */
	public Long ttl(@NonNull String key) {
		return redisTemplate.getExpire(key);
	}


	/**
	 * 这个命令类似于 TTL 命令，但它以毫秒为单位返回 key 的剩余生存时间，而不是像 TTL 命令那样，以秒为单位。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 当 key 不存在时，返回 -2 。当 key 存在但没有设置剩余生存时间时，返回 -1 。否则，以毫秒为单位，返回 key 的剩余生存时间
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/pttl">Redis Documentation: PTTL</a>
	 * @since 2021-09-07 21:01:46
	 */
	public Long pTtl(@NonNull String key) {
		return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
	}

	/**
	 * 将字符串值 value 存放到 key 。
	 * <p>
	 * 如果 key 已经持有其他值， SET 就覆写旧值， 无视类型。 当 SET 命令对一个带有生存时间（TTL）的键进行设置之后， 该键原有的 TTL 将被清除.
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param value           值
	 * @param cacheNullValues 是否缓存null对象
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
	 * @since 2021-09-07 21:01:46
	 */
	public void set(@NonNull String key, Object value, boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		if (!cacheNullVal && value == null) {
			return;
		}

		redisTemplate.opsForValue().set(key, value == null ? newNullVal() : value);
	}

	/**
	 * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间。不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
	 *
	 * @param key           一定不能为 {@literal null}.
	 * @param unixTimestamp 过期时间戳
	 * @return 是否成功
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/expireat">Redis Documentation: EXPIREAT</a>
	 * @since 2021-09-07 21:01:46
	 */
	public Boolean expireAt(@NonNull String key, long unixTimestamp) {
		return expireAt(key, new Date(unixTimestamp));
	}

	/**
	 * 设置缓存
	 *
	 * @param cacheKey        缓存key 一定不能为 {@literal null}.
	 * @param value           缓存value
	 * @param cacheNullValues 是否缓存null对象
	 * @author shuigedeng
	 * @since 2021-09-07 21:01:46
	 */
	public void set(@NonNull CacheKey cacheKey, Object value, boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		String key = cacheKey.getKey();
		Duration expire = cacheKey.getExpire();
		if (expire == null) {
			set(key, value, cacheNullVal);
		} else {
			setEx(key, value, expire, cacheNullVal);
		}
	}

	/**
	 * 将键 key 的值设置为 value ， 并将键 key 的生存时间设置为 seconds 秒钟。 如果键 key 已经存在， 那么 SETEX 命令将覆盖已有的值。
	 * <p>
	 * SETEX 命令的效果和以下两个命令的效果类似： SET key value EXPIRE key seconds  # 设置生存时间
	 * <p>
	 * SETEX 和这两个命令的不同之处在于 SETEX 是一个原子（atomic）操作， 它可以在同一时间内完成设置值和设置过期时间这两个操作， 因此 SETEX
	 * 命令在储存缓存的时候非常实用。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param value           值
	 * @param timeout         一定不能为 {@literal null}.
	 * @param cacheNullValues 是否缓存null对象
	 * @throws IllegalArgumentException if either {@code key}, {@code value} or {@code timeout} is
	 *                                  not present.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/setex">Redis Documentation: SETEX</a>
	 * @since 2021-09-07 21:01:46
	 */
	public void setEx(@NonNull String key, Object value, Duration timeout,
		boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		if (!cacheNullVal && value == null) {
			return;
		}
		redisTemplate.opsForValue().set(key, value == null ? newNullVal() : value, timeout);
	}


	/**
	 * 添加到带有 过期时间的  缓存
	 *
	 * @param key   redis主键
	 * @param value 值
	 * @param time  过期时间(单位秒)
	 * @author shuigedeng
	 * @since 2021-09-07 21:01:46
	 */
	public void setExpire(final byte[] key, final byte[] value, final long time) {
		redisTemplate.execute((RedisCallback<Long>) connection -> {
			connection.setEx(key, time, value);
			LogUtil.info("[redisTemplate redis]放入 缓存  url:{} ========缓存时间为{}秒", key, time);
			return 1L;
		});
	}

	/**
	 * 将键 key 的值设置为 value ， 并将键 key 的生存时间设置为 seconds 秒钟。 如果键 key 已经存在， 那么 SETEX 命令将覆盖已有的值。
	 * <p>
	 * SETEX 命令的效果和以下两个命令的效果类似： SET key value EXPIRE key seconds  # 设置生存时间
	 * <p>
	 * SETEX 和这两个命令的不同之处在于 SETEX 是一个原子（atomic）操作， 它可以在同一时间内完成设置值和设置过期时间这两个操作， 因此 SETEX
	 * 命令在储存缓存的时候非常实用。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param value           值
	 * @param cacheNullValues 是否缓存null对象
	 * @param seconds         过期时间 单位秒
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/setex">Redis Documentation: SETEX</a>
	 * @since 2021-09-07 21:01:46
	 */
	public void setEx(@NonNull String key, Object value, long seconds, boolean... cacheNullValues) {
		setEx(key, value, Duration.ofSeconds(seconds), cacheNullValues);
	}

	/**
	 * 如果存在key，则设置key以保存字符串值。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param value 一定不能为 {@literal null}.
	 * @return 设置成功返回true。
	 * @throws IllegalArgumentException 如果{@code key} 或 {@code value} 不存在
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
	 * @since 2021-09-07 21:01:46
	 */
	@Nullable
	public Boolean setXx(@NonNull String key, String value, boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		return redisTemplate.opsForValue()
			.setIfPresent(key, cacheNullVal && value == null ? newNullVal() : value);
	}

	/**
	 * 如果存在key，则设置key以保存字符串值。
	 *
	 * @param key     一定不能为 {@literal null}.
	 * @param value   一定不能为 {@literal null}.
	 * @param seconds 过期时间 单位秒
	 * @return 设置成功返回true。
	 * @throws IllegalArgumentException 如果{@code key} 或 {@code value} 不存在
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
	 * @since 2021-09-07 21:01:46
	 */
	@Nullable
	public Boolean setXx(@NonNull String key, String value, long seconds,
		boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		return redisTemplate.opsForValue()
			.setIfPresent(key, cacheNullVal && value == null ? newNullVal() : value, seconds,
				TimeUnit.SECONDS);
	}

	/**
	 * 添加到带有 过期时间的  缓存
	 *
	 * @param key   redis主键
	 * @param value 值
	 * @param time  过期时间(单位秒)
	 * @author shuigedeng
	 * @since 2021-09-07 21:01:46
	 */
	public void setExpire(final String key, final Object value, final long time) {
		redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
		//return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
		//	RedisSerializer<String> serializer = getRedisSerializer();
		//	byte[] keys = serializer.serialize(key);
		//	byte[] values = OBJECT_SERIALIZER.serialize(value);
		//	return connection.setEx(keys, time, values);
		//});
	}

	/**
	 * 移除并返回列表 key 的头元素
	 *
	 * @param key 一定不能为  {@literal null}.
	 * @return 列表的头元素。 当 key 不存在时，返回 nil 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/lpop">Redis Documentation: LPOP</a>
	 * @since 2021-09-07 21:01:46
	 */
	@Nullable
	public <T> T lPop(@NonNull String key) {
		return (T) redisTemplate.opsForList().leftPop(key);
	}


	/**
	 * 移除并返回列表 key 的尾元素。
	 *
	 * @param key 一定不能为  {@literal null}.
	 * @return 列表的尾元素。 当 key 不存在时，返回 nil 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/rpop">Redis Documentation: RPOP</a>
	 * @since 2021-09-07 21:01:46
	 */
	public <T> T rPop(@NonNull String key) {
		return (T) redisTemplate.opsForList().rightPop(key);
	}


	/**
	 * 命令 RPOPLPUSH 在一个原子时间内，执行以下两个动作： 1.将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端。 2. 将 source 弹出的元素插入到列表
	 * destination ，作为 destination 列表的的头元素。
	 * <p>
	 * 举个例子，你有两个列表 source 和 destination ， source 列表有元素 a, b, c ， destination 列表有元素 x, y, z ， 执行
	 * RPOPLPUSH source destination 之后， source 列表包含元素 a, b ， destination 列表包含元素 c, x, y, z ，并且元素 c
	 * 会被返回给客户端。
	 * <p>
	 * 如果 source 不存在，值 nil 被返回，并且不执行其他动作。 如果 source 和 destination 相同，则列表中的表尾元素被移动到表头，并返回该元素，可以把这种特殊情况视作列表的旋转(rotation)操作。
	 *
	 * @param sourceKey      一定不能为 {@literal null}.
	 * @param destinationKey 一定不能为 {@literal null}.
	 * @return 被弹出的元素
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/rpoplpush">Redis Documentation: RPOPLPUSH</a>
	 * @since 2021-09-07 21:01:46
	 */
	public <T> T rPoplPush(String sourceKey, String destinationKey) {
		return (T) redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
	}

	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
	 * <p>
	 * count 的值可以是以下几种： count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。 count < 0 :
	 * 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。 count = 0 : 移除表中所有与 value 相等的值。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param count 数量
	 * @param value 值
	 * @return 被移除元素的数量。 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回 0 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/lrem">Redis Documentation: LREM</a>
	 * @since 2021-09-07 21:01:46
	 */
	@Nullable
	public Long lRem(@NonNull String key, long count, Object value) {
		return redisTemplate.opsForList().remove(key, count, value);
	}

	/**
	 * 返回列表 key 的长度。 如果 key 不存在，则 key 被解释为一个空列表，返回 0 . 如果 key 不是列表类型，返回一个错误。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return {列表 key 的长度
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/llen">Redis Documentation: LLEN</a>
	 * @since 2021-09-07 21:01:46
	 */
	@Nullable
	public Long lLen(@NonNull String key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * 返回列表 key 中，下标为 index 的元素。 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1
	 * 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。 如果 key 不是列表类型，返回一个错误。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param index 索引
	 * @return 列表中下标为 index 的元素。 如果 index 参数的值不在列表的区间范围内(out of range)，返回 nil 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/lindex">Redis Documentation: LINDEX</a>
	 * @since 2021-09-07 21:01:46
	 */
	@Nullable
	public <T> T lIndex(@NonNull String key, long index) {
		return (T) redisTemplate.opsForList().index(key, index);
	}


	/**
	 * 将值 value 插入到列表 key 当中，位于值 pivot 之前。 当 pivot 不存在于列表 key 时，不执行任何操作。 当 key 不存在时， key
	 * 被视为空列表，不执行任何操作。 如果 key 不是列表类型，返回一个错误。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param pivot 对比值
	 * @param value 值
	 * @return 如果命令执行成功，返回插入操作完成之后，列表的长度。 如果没有找到 pivot ，返回 -1 。 如果 key 不存在或为空列表，返回 0 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/linsert">Redis Documentation: LINSERT</a>
	 * @since 2021-09-07 21:01:46
	 */
	@Nullable
	public Long lInsert(@NonNull String key, Object pivot, Object value) {
		return redisTemplate.opsForList().leftPush(key, pivot, value);
	}

	/**
	 * 将值 value 插入到列表 key 当中，位于值 pivot 之后。 当 pivot 不存在于列表 key 时，不执行任何操作。 当 key 不存在时， key
	 * 被视为空列表，不执行任何操作。 如果 key 不是列表类型，返回一个错误。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param pivot 对比值
	 * @param value 值
	 * @return 如果命令执行成功，返回插入操作完成之后，列表的长度。 如果没有找到 pivot ，返回 -1 。 如果 key 不存在或为空列表，返回 0 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/linsert">Redis Documentation: LINSERT</a>
	 * @since 2021-09-07 21:01:46
	 */
	@Nullable
	public Long rInsert(@NonNull String key, Object pivot, Object value) {
		return redisTemplate.opsForList().rightPush(key, pivot, value);
	}

	/**
	 * 将列表 key 下标为 index 的元素的值设置为 value 。 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误。
	 * 关于列表下标的更多信息，请参考 LINDEX 命令。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param index 下标
	 * @param value 值
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/lset">Redis Documentation: LSET</a>
	 * @since 2021-09-07 21:01:46
	 */
	public void lSet(@NonNull String key, long index, Object value) {
		redisTemplate.opsForList().set(key, index, value);
	}

	/**
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0
	 * 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 *
	 * <pre>
	 * 例子：
	 * 获取 list 中所有数据：lRange(key, 0, -1);
	 * 获取 list 中下标 1 到 3 的数据： lRange(key, 1, 3);
	 * </pre>
	 * <p>
	 * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表。 如果 stop 下标比 end 下标还要大，Redis将
	 * stop 的值设置为 end 。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param start 开始索引
	 * @param end   结束索引
	 * @return 一个列表，包含指定区间内的元素。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
	 * @since 2021-09-07 21:01:46
	 */
	@Nullable
	public List<Object> lRange(@NonNull String key, long start, long end) {
		return redisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list
	 * 的前三个元素，其余元素全部删除。 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。 当 key 不是列表类型时，返回一个错误。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param start 开始索引
	 * @param end   结束索引
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
	 * @since 2021-09-07 21:01:46
	 */
	public void lTrim(@NonNull String key, long start, long end) {
		redisTemplate.opsForList().trim(key, start, end);
	}

	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。 当
	 * key 不是集合类型时，返回一个错误。
	 *
	 * @param key     一定不能为 {@literal null}.
	 * @param members 元素
	 * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sadd">Redis Documentation: SADD</a>
	 * @since 2021-09-07 21:01:58
	 */
	public <V> Long sAdd(@NonNull CacheKey key, V... members) {
		Long count = redisTemplate.opsForSet().add(key.getKey(), members);
		setExpire(key);
		return count;
	}

	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。 当
	 * key 不是集合类型时，返回一个错误。
	 *
	 * @param key     一定不能为 {@literal null}.
	 * @param members 元素
	 * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sadd">Redis Documentation: SADD</a>
	 * @since 2021-09-07 21:01:58
	 */
	public <V> Long sAdd(@NonNull CacheKey key, Collection<V> members) {
		Long count = redisTemplate.opsForSet().add(key.getKey(), members.toArray());
		setExpire(key);
		return count;
	}

	/**
	 * 判断 member 元素是否集合 key 的成员。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param member 元素
	 * @return 如果 member 元素是集合的成员，返回 1 。 如果 member 元素不是集合的成员，或 key 不存在，返回 0 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sismember">Redis Documentation: SISMEMBER</a>
	 * @since 2021-09-07 21:01:58
	 */
	public Boolean sIsMember(@NonNull CacheKey key, Object member) {
		return redisTemplate.opsForSet().isMember(key.getKey(), member);
	}

	/**
	 * 移除并返回集合中的一个随机元素。 如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用 SRANDMEMBER 命令。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 被移除的随机元素。 当 key 不存在或 key 是空集时，返回 nil 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/spop">Redis Documentation: SPOP</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public <T> T sPop(@NonNull CacheKey key) {
		return (T) redisTemplate.opsForSet().pop(key.getKey());
	}

	/**
	 * 返回集合中的一个随机元素。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 只提供 key 参数时，返回一个元素；如果集合为空，返回 nil
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public <T> T sRandMember(@NonNull CacheKey key) {
		return (T) redisTemplate.opsForSet().randomMember(key.getKey());
	}

	/**
	 * 返回集合中的count个随机元素。
	 * <p>
	 * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count 大于等于集合基数，那么返回整个集合。 如果 count
	 * 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param count 数量
	 * @return 只提供 key 参数时，返回一个元素；如果集合为空，返回 nil
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public <V> Set<V> sRandMember(@NonNull CacheKey key, long count) {
		return (Set<V>) redisTemplate.opsForSet().distinctRandomMembers(key.getKey(), count);
	}


	/**
	 * 返回集合中的count个随机元素。
	 * <p>
	 * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count 大于等于集合基数，那么返回整个集合。 如果 count
	 * 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param count 数量
	 * @return 只提供 key 参数时，返回一个元素；如果集合为空，返回 nil
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public <V> List<V> sRandMembers(@NonNull CacheKey key, long count) {
		return (List<V>) redisTemplate.opsForSet().randomMembers(key.getKey(), count);
	}

	/**
	 * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。 当 key 不是集合类型，返回一个错误。
	 *
	 * @param key     一定不能为 {@literal null}.
	 * @param members 元素
	 * @return 被成功移除的元素的数量，不包括被忽略的元素
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/srem">Redis Documentation: SREM</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public Long sRem(@NonNull CacheKey key, Object... members) {
		return redisTemplate.opsForSet().remove(key.getKey(), members);
	}

	/**
	 * 将 member 元素从 source 集合移动到 destination 集合。 SMOVE 是原子性操作。 如果 source 集合不存在或不包含指定的 member 元素，则
	 * SMOVE 命令不执行任何操作，仅返回 0 。否则， member 元素从 source 集合中被移除，并添加到 destination 集合中去。 当 destination
	 * 集合已经包含 member 元素时， SMOVE 命令只是简单地将 source 集合中的 member 元素删除。 当 source 或 destination
	 * 不是集合类型时，返回一个错误。
	 *
	 * @param sourceKey      源key
	 * @param destinationKey 目的key
	 * @param value          值
	 * @return 是否成功
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/smove">Redis Documentation: SMOVE</a>
	 * @since 2021-09-07 21:01:58
	 */
	public <V> Boolean sMove(@NonNull CacheKey sourceKey, CacheKey destinationKey, V value) {
		return redisTemplate.opsForSet().move(sourceKey.getKey(), value, destinationKey.getKey());
	}

	/**
	 * 返回集合 key 的基数(集合中元素的数量)。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 集合的基数。 当 key 不存在时，返回 0 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/scard">Redis Documentation: SCARD</a>
	 * @since 2021-09-07 21:01:58
	 */
	public Long sCard(@NonNull CacheKey key) {
		return redisTemplate.opsForSet().size(key.getKey());
	}

	/**
	 * 返回集合 key 中的所有成员。 不存在的 key 被视为空集合。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 集合中的所有成员。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/smembers">Redis Documentation: SMEMBERS</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public <V> Set<V> sMembers(@NonNull CacheKey key) {
		return (Set<V>) redisTemplate.opsForSet().members(key.getKey());
	}


	/**
	 * 返回一个集合的全部成员，该集合是所有给定集合的交集。
	 * <p>
	 * 不存在的 key 被视为空集。
	 * <p>
	 * 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
	 *
	 * @param key      一定不能为{@literal null}.
	 * @param otherKey 一定不能为 {@literal null}.
	 * @return 交集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sinter">Redis Documentation: SINTER</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public <V> Set<V> sInter(@NonNull CacheKey key, @NonNull CacheKey otherKey) {
		return (Set<V>) redisTemplate.opsForSet().intersect(key.getKey(), otherKey.getKey());
	}

	/**
	 * 返回一个集合的全部成员，该集合是所有给定集合的交集。
	 * <p>
	 * 不存在的 key 被视为空集。
	 * <p>
	 * 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
	 *
	 * @param key       一定不能为{@literal null}.
	 * @param otherKeys 一定不能为 {@literal null}.
	 * @return 交集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sinter">Redis Documentation: SINTER</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public Set<Object> sInter(@NonNull CacheKey key, Collection<CacheKey> otherKeys) {
		return redisTemplate.opsForSet().intersect(key.getKey(),
			otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
	}

	/**
	 * 返回一个集合的全部成员，该集合是所有给定集合的交集。
	 * <p>
	 * 不存在的 key 被视为空集。
	 * <p>
	 * 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
	 *
	 * @param otherKeys 一定不能为 {@literal null}.
	 * @return 交集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sinter">Redis Documentation: SINTER</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public <V> Set<V> sInter(Collection<CacheKey> otherKeys) {
		return (Set<V>) redisTemplate.opsForSet()
			.intersect(otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
	}


	/**
	 * 这个命令类似于 SINTER key [key …] 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集。 如果 destination
	 * 集合已经存在，则将其覆盖。 destination 可以是 key 本身。
	 *
	 * @param key      一定不能为{@literal null}.
	 * @param otherKey 一定不能为 {@literal null}.
	 * @param destKey  一定不能为{@literal null}.
	 * @return 结果集中的成员数量。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public Long sInterStore(@NonNull CacheKey key, @NonNull CacheKey otherKey,
		@NonNull CacheKey destKey) {
		return redisTemplate.opsForSet()
			.intersectAndStore(key.getKey(), otherKey.getKey(), destKey.getKey());
	}

	/**
	 * 这个命令类似于 SINTER key [key …] 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集。 如果 destination
	 * 集合已经存在，则将其覆盖。 destination 可以是 key 本身。
	 *
	 * @param key       一定不能为{@literal null}.
	 * @param otherKeys 一定不能为 {@literal null}.
	 * @param destKey   一定不能为{@literal null}.
	 * @return 结果集中的成员数量。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public Long sInterStore(@NonNull CacheKey key, @NonNull Collection<CacheKey> otherKeys,
		@NonNull CacheKey destKey) {
		return redisTemplate.opsForSet().intersectAndStore(key.getKey(),
			otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()),
			destKey.getKey());
	}

	/**
	 * 这个命令类似于 SINTER key [key …] 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集。 如果 destination
	 * 集合已经存在，则将其覆盖。 destination 可以是 key 本身。
	 *
	 * @param otherKeys 一定不能为 {@literal null}.
	 * @param destKey   一定不能为{@literal null}.
	 * @return 结果集中的成员数量。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public Long sInterStore(Collection<CacheKey> otherKeys, @NonNull CacheKey destKey) {
		return redisTemplate.opsForSet().intersectAndStore(
			otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()),
			destKey.getKey());
	}


	/**
	 * 返回多个集合的并集，多个集合由 keys 指定 不存在的 key 被视为空集。
	 *
	 * @param key      一定不能为 {@literal null}.
	 * @param otherKey 一定不能为 {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sunion">Redis Documentation: SUNION</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public <V> Set<V> sUnion(@NonNull CacheKey key, @NonNull CacheKey otherKey) {
		return (Set<V>) redisTemplate.opsForSet().union(key.getKey(), otherKey.getKey());
	}

	/**
	 * 返回多个集合的并集，多个集合由 keys 指定 不存在的 key 被视为空集。
	 *
	 * @param key       一定不能为 {@literal null}.
	 * @param otherKeys 一定不能为 {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sunion">Redis Documentation: SUNION</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public <V> Set<V> sUnion(@NonNull CacheKey key, Collection<CacheKey> otherKeys) {
		return (Set<V>) redisTemplate.opsForSet().union(key.getKey(),
			otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
	}

	/**
	 * 返回多个集合的并集，多个集合由 keys 指定 不存在的 key 被视为空集。
	 *
	 * @param otherKeys 一定不能为 {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sunion">Redis Documentation: SUNION</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public <V> Set<V> sUnion(Collection<CacheKey> otherKeys) {
		return (Set<V>) redisTemplate.opsForSet()
			.union(otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
	}

	/**
	 * 这个命令类似于 SUNION key [key …] 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集。 如果 destination 已经存在，则将其覆盖。
	 * destination 可以是 key 本身。
	 *
	 * @param key      一定不能为 {@literal null}.
	 * @param otherKey 一定不能为 {@literal null}.
	 * @param distKey  一定不能为 {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sunionstore">Redis Documentation: SUNIONSTORE</a>
	 * @since 2021-09-07 21:01:58
	 */
	public Long sUnionStore(@NonNull CacheKey key, @NonNull CacheKey otherKey,
		@NonNull CacheKey distKey) {
		return redisTemplate.opsForSet()
			.unionAndStore(key.getKey(), otherKey.getKey(), distKey.getKey());
	}

	/**
	 * 这个命令类似于 SUNION key [key …] 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集。 如果 destination 已经存在，则将其覆盖。
	 * destination 可以是 key 本身。
	 *
	 * @param otherKeys 一定不能为 {@literal null}.
	 * @param distKey   一定不能为 {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sunionstore">Redis Documentation: SUNIONSTORE</a>
	 * @since 2021-09-07 21:01:58
	 */
	public Long sUnionStore(Collection<CacheKey> otherKeys, @NonNull CacheKey distKey) {
		return redisTemplate.opsForSet()
			.unionAndStore(otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()),
				distKey.getKey());
	}

	/**
	 * 返回一个集合的全部成员，该集合是所有给定集合之间的差集。 不存在的 key 被视为空集。
	 *
	 * @param key      一定不能为 {@literal null}.
	 * @param otherKey 一定不能为 {@literal null}.
	 * @return 一个包含差集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sdiff">Redis Documentation: SDIFF</a>
	 * @since 2021-09-07 21:01:58
	 */
	@Nullable
	public <V> Set<V> sDiff(@NonNull CacheKey key, @NonNull CacheKey otherKey) {
		return (Set<V>) redisTemplate.opsForSet().difference(key.getKey(), otherKey.getKey());
	}

	/**
	 * 返回一个集合的全部成员，该集合是所有给定集合之间的差集。 不存在的 key 被视为空集。
	 *
	 * @param otherKeys 一定不能为 {@literal null}.
	 * @return 一个包含差集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sdiff">Redis Documentation: SDIFF</a>
	 * @since 2021-09-07 21:01:58
	 */
	public <V> Set<V> sDiff(Collection<CacheKey> otherKeys) {
		return (Set<V>) redisTemplate.opsForSet()
			.difference(otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()));
	}

	/**
	 * 这个命令的作用和 SDIFF key [key …] 类似，但它将结果保存到 destination 集合，而不是简单地返回结果集。 如果 destination
	 * 集合已经存在，则将其覆盖。 destination 可以是 key 本身。
	 *
	 * @param key      一定不能为 {@literal null}.
	 * @param distKey  一定不能为 {@literal null}.
	 * @param otherKey 一定不能为 {@literal null}.
	 * @return 结果集中的元素数量。。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sdiffstore">Redis Documentation: sdiffstore</a>
	 * @since 2021-09-07 21:01:58
	 */
	public Long sDiffStore(@NonNull CacheKey key, @NonNull CacheKey otherKey,
		@NonNull CacheKey distKey) {
		return redisTemplate.opsForSet()
			.differenceAndStore(key.getKey(), otherKey.getKey(), distKey.getKey());
	}

	/**
	 * 返回一个集合的全部成员，该集合是所有给定集合之间的差集。 不存在的 key 被视为空集。
	 *
	 * @param otherKeys 一定不能为 {@literal null}.
	 * @return 结果集中的元素数量。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/sdiffstore">Redis Documentation: sdiffstore</a>
	 * @since 2021-09-07 21:01:58
	 */
	public Long sDiffStore(Collection<CacheKey> otherKeys, @NonNull CacheKey distKey) {
		return redisTemplate.opsForSet().differenceAndStore(
			otherKeys.stream().map(CacheKey::getKey).collect(Collectors.toList()),
			distKey.getKey());
	}

	/**
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score
	 * 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。 score 值可以是整数值或双精度浮点数。 如果 key 不存在，则创建一个空的有序集并执行
	 * ZADD 操作。 当 key 存在但不是有序集类型时，返回一个错误。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param score  得分
	 * @param member 值
	 * @return 是否成功
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD</a>
	 * @since 2021-09-07 21:01:58
	 */
	public Boolean zAdd(@NonNull String key, Object member, double score) {
		return redisTemplate.opsForZSet().add(key, member, score);
	}

	/**
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score
	 * 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。 score 值可以是整数值或双精度浮点数。 如果 key 不存在，则创建一个空的有序集并执行
	 * ZADD 操作。 当 key 存在但不是有序集类型时，返回一个错误。
	 *
	 * @param key          一定不能为 {@literal null}.
	 * @param scoreMembers 一定不能为 {@literal null}.
	 * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zadd">Redis Documentation: ZADD</a>
	 * @since 2021-09-07 21:01:58
	 */
	public Long zAdd(@NonNull String key, Map<Object, Double> scoreMembers) {
		Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
		scoreMembers.forEach((score, member) -> tuples.add(new DefaultTypedTuple<>(score, member)));
		return redisTemplate.opsForSet().add(key, tuples);
	}

	/**
	 * 返回有序集 key 中，成员 member 的 score 值。 如果 member 元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param member the value.
	 * @return member 成员的 score 值，以字符串形式表示
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zscore">Redis Documentation: ZSCORE</a>
	 * @since 2021-09-07 21:01:58
	 */
	public Double zScore(@NonNull String key, Object member) {
		return redisTemplate.opsForZSet().score(key, member);
	}

	/**
	 * 为有序集 key 的成员 member 的 score 值加上增量 increment 。 可以通过传递一个负数值 increment ，让 score 减去相应的值，比如
	 * ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。 当 key 不存在，或 member 不是 key 的成员时， ZINCRBY key
	 * increment member 等同于 ZADD key increment member 。 当 key 不是有序集类型时，返回一个错误。 score
	 * 值可以是整数值或双精度浮点数。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param score  得分
	 * @param member the value.
	 * @return member 成员的新 score 值
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zincrby">Redis Documentation: ZINCRBY</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Double zIncrBy(@NonNull String key, Object member, double score) {
		return redisTemplate.opsForZSet().incrementScore(key, member, score);
	}

	/**
	 * 返回有序集 key 的基数。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zcard">Redis Documentation: ZCARD</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long zCard(@NonNull String key) {
		return redisTemplate.opsForZSet().zCard(key);
	}

	/**
	 * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @param min 最小值
	 * @param max 最大值
	 * @return {@literal null} when used in pipeline / transaction.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zcount">Redis Documentation: ZCOUNT</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long zCount(@NonNull String key, double min, double max) {
		return redisTemplate.opsForZSet().count(key, min, max);
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。 其中成员的位置按 score 值递增(从小到大)来排序。 具有相同 score 值的成员按字典序(lexicographical order
	 * )来排列。
	 * <p>
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。 你也可以使用负数下标，以 -1 表示最后一个成员，
	 * -2 表示倒数第二个成员，以此类推。
	 * <p>
	 * 超出范围的下标并不会引起错误。 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表。 另一方面，假如
	 * stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理。
	 * <p>
	 * 可以通过使用 WITHSCORES 选项，来让成员和它的 score 值一并返回，返回列表以 value1,score1, ..., valueN,scoreN 的格式表示。
	 * 客户端库可能会返回一些更复杂的数据类型，比如数组、元组等
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param start 索引
	 * @param end   索引
	 * @return 指定区间内，不带有 score 值(可选)的有序集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Set<Object> zRange(@NonNull String key, long start, long end) {
		return redisTemplate.opsForZSet().range(key, start, end);
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。 其中成员的位置按 score 值递增(从小到大)来排序。 具有相同 score 值的成员按字典序(lexicographical order
	 * )来排列。
	 * <p>
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。 你也可以使用负数下标，以 -1 表示最后一个成员，
	 * -2 表示倒数第二个成员，以此类推。
	 * <p>
	 * 超出范围的下标并不会引起错误。 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表。 另一方面，假如
	 * stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理。
	 * <p>
	 * 可以通过使用 WITHSCORES 选项，来让成员和它的 score 值一并返回，返回列表以 value1,score1, ..., valueN,scoreN 的格式表示。
	 * 客户端库可能会返回一些更复杂的数据类型，比如数组、元组等
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param start 索引
	 * @param end   索引
	 * @return 指定区间内，带有 score 值(可选)的有序集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(@NonNull String key, long start,
		long end) {
		return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。 其中成员的位置按 score 值递减(从大到小)来排列。 具有相同 score 值的成员按字典序的逆序(reverse
	 * lexicographical order)排列。 除了成员按 score 值递减的次序排列这一点外， ZREVRANGE 命令的其他方面和 ZRANGE key start stop
	 * [WITHSCORES] 命令一样。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param start 索引
	 * @param end   索引
	 * @return 指定区间内，不带有 score 值(可选)的有序集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Set<Object> zRevrange(@NonNull String key, long start, long end) {
		return redisTemplate.opsForZSet().reverseRange(key, start, end);
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。 其中成员的位置按 score 值递减(从大到小)来排列。 具有相同 score 值的成员按字典序的逆序(reverse
	 * lexicographical order)排列。 除了成员按 score 值递减的次序排列这一点外， ZREVRANGE 命令的其他方面和 ZRANGE key start stop
	 * [WITHSCORES] 命令一样。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param start 索引
	 * @param end   索引
	 * @return 指定区间内，不带有 score 值(可选)的有序集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Set<ZSetOperations.TypedTuple<Object>> zRevrangeWithScores(@NonNull String key,
		long start, long end) {
		return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。 有序集成员按 score 值递增(从小到大)次序排列。 具有相同
	 * score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @param min 最小得分
	 * @param max 最大得分
	 * @return 指定区间内 不带有 score 值(可选)的有序集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Set<Object> zRangeByScore(@NonNull String key, double min, double max) {
		return redisTemplate.opsForZSet().rangeByScore(key, min, max);
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。 有序集成员按 score 值递增(从小到大)次序排列。 具有相同
	 * score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @param min 最小得分
	 * @param max 最大得分
	 * @return 指定区间内，带有 score 值(可选)的有序集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Set<ZSetOperations.TypedTuple<Object>> zRangeByScoreWithScores(@NonNull String key,
		double min, double max) {
		return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
	}

	/**
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。
	 * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order )排列。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @param min 最小得分
	 * @param max 最大得分
	 * @return 指定区间内 不带有 score 值(可选)的有序集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zrevrange">Redis Documentation: ZRANGEBYSCORE</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Set<Object> zReverseRange(@NonNull String key, double min, double max) {
		return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
	}

	/**
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。
	 * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order )排列。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @param min 最小得分
	 * @param max 最大得分
	 * @return 指定区间内，带有 score 值(可选)的有序集成员的列表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zrevrangebyscore">Redis Documentation:
	 * ZRANGEBYSCORE</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeByScoreWithScores(
		@NonNull String key, double min, double max) {
		return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。 使用
	 * ZREVRANK key member 命令可以获得成员按 score 值递减(从大到小)排列的排名。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param member the value.
	 * @return 如果 member 是有序集 key 的成员，返回 member 的排名。 如果 member 不是有序集 key 的成员，返回 nil 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zrank">Redis Documentation: ZRANK</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Long zRank(@NonNull String key, Object member) {
		return redisTemplate.opsForZSet().rank(key, member);
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。 排名以 0 为底，也就是说， score 值最大的成员排名为 0 。 使用
	 * ZRANK 命令可以获得成员按 score 值递增(从小到大)排列的排名。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param member the value.
	 * @return 如果 member 是有序集 key 的成员，返回 member 的排名。 如果 member 不是有序集 key 的成员，返回 nil 。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zrevrank">Redis Documentation: ZREVRANK</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long zRevrank(@NonNull String key, Object member) {
		return redisTemplate.opsForZSet().reverseRank(key, member);
	}

	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。 当 key 存在但不是有序集类型时，返回一个错误。
	 *
	 * @param key     一定不能为 {@literal null}.
	 * @param members 一定不能为 {@literal null}.
	 * @return 被成功移除的成员的数量，不包括被忽略的成员
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zrem">Redis Documentation: ZREM</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long zRem(@NonNull String key, Object... members) {
		return redisTemplate.opsForSet().remove(key, members);
	}

	/**
	 * 移除有序集 key 中，指定排名(rank)区间内的所有成员。 区间分别以下标参数 start 和 stop 指出，包含 start 和 stop 在内。 下标参数 start 和
	 * stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。 你也可以使用负数下标，以 -1 表示最后一个成员， -2
	 * 表示倒数第二个成员，以此类推。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param start 下标
	 * @param end   下标
	 * @return 被移除成员的数量。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zremrangebyrank">Redis Documentation:
	 * ZREMRANGEBYRANK</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long zRem(@NonNull String key, long start, long end) {
		return redisTemplate.opsForZSet().removeRange(key, start, end);
	}

	/**
	 * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。 自版本2.1.6开始， score 值等于 min 或 max
	 * 的成员也可以不包括在内
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @param min 最小得分
	 * @param max 最大得分
	 * @return 被移除成员的数量。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/zremrangebyscore">Redis Documentation:
	 * ZREMRANGEBYSCORE</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long zRemRangeByScore(@NonNull String key, double min, double max) {
		return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
	}

	/**
	 * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
	 *
	 * @param keys   redis主键数组
	 * @param values 值数组
	 * @param time   过期时间(单位秒)
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public void setExpire(final String[] keys, final Object[] values, final long time) {
		for (int i = 0; i < keys.length; i++) {
			redisTemplate.opsForValue().set(keys[i], values[i], time, TimeUnit.SECONDS);
		}
		//redisTemplate.execute((RedisCallback<Long>) connection -> {
		//	RedisSerializer<String> serializer = getRedisSerializer();
		//	for (int i = 0; i < keys.length; i++) {
		//		byte[] bKeys = serializer.serialize(keys[i]);
		//		byte[] bValues = OBJECT_SERIALIZER.serialize(values[i]);
		//		connection.setEx(bKeys, time, bValues);
		//	}
		//	return 1L;
		//});
	}

	/**
	 * 获取key中存放的Long值
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return key中存储的的数字
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long getCounter(@NonNull CacheKey key, Long... defaultValue) {
		Object val = redisTemplate.opsForValue().get(key.getKey());
		if (isNullVal(val)) {
			return defaultValue.length > 0 ? defaultValue[0] : null;
		}
		return Convert.toLong(val);
	}

	/**
	 * 获取key中存放的Long值
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return key中存储的的数字
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long getCounter(@NonNull CacheKey key, Function<CacheKey, Long> loader) {
		Object val = redisTemplate.opsForValue().get(key.getKey());
		if (isNullVal(val)) {
			return loader.apply(key);
		}
		return Convert.toLong(val);
	}

	/**
	 * 为键 key 储存的数字值减去一。 如果键 key 不存在， 那么键 key 的值会先被初始化为 0 ， 然后再执行 DECR 操作。 如果键 key 储存的值不能被解释为数字， 那么
	 * DECR 命令将返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 在减去增量 1 之后， 键 key 的值。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/decr">Redis Documentation: DECR</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long decr(@NonNull CacheKey key) {
		Long decr = redisTemplate.opsForValue().decrement(key.getKey());
		setExpire(key);
		return decr;
	}

	/**
	 * 将 key 所储存的值减去减量 decrement 。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 在减去增量 decrement 之后， 键 key 的值。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/decr">Redis Documentation: DECR</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long decrBy(@NonNull CacheKey key, long decrement) {
		Long decr = redisTemplate.opsForValue().decrement(key.getKey(), decrement);
		setExpire(key);
		return decr;
	}
	// ---------------------------- string end ----------------------------

	// ---------------------------- hash start ----------------------------

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。 如果域 field
	 * 已经存在于哈希表中，旧值将被覆盖。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param field           一定不能为 {@literal null}.
	 * @param cacheNullValues 是否缓存空对象
	 * @param value           值
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hset">Redis Documentation: HSET</a>
	 * @since 2021-09-07 21:04:53
	 */
	public void hSet(@NonNull String key, @NonNull Object field, Object value,
		boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;

		if (!cacheNullVal && value == null) {
			return;
		}
		redisTemplate.opsForHash().put(key, field, value == null ? newNullVal() : value);
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。 如果域 field
	 * 已经存在于哈希表中，旧值将被覆盖。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param value           值
	 * @param cacheNullValues 是否缓存空对象
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hset">Redis Documentation: HSET</a>
	 * @since 2021-09-07 21:04:53
	 */
	public void hSet(@NonNull CacheHashKey key, Object value, boolean... cacheNullValues) {
		this.hSet(key.getKey(), key.getField(), value, cacheNullValues);
		setExpire(key);
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param field           一定不能为 {@literal null}.
	 * @param cacheNullValues 是否缓存空值
	 * @return 默认情况下返回给定域的值, 如果给定域不存在于哈希表中， 又或者给定的哈希表并不存在， 那么命令返回 nil
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hget">Redis Documentation: HGET</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public <T> T hGet(@NonNull String key, @NonNull Object field, boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		T value = (T) redisTemplate.opsForHash().get(key, field);
		if (value == null && cacheNullVal) {
			hSet(key, field, newNullVal(), true);
		}
		return returnVal(value);
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param field           一定不能为 {@literal null}.
	 * @param loader          加载器
	 * @param cacheNullValues 是否缓存空值
	 * @return 默认情况下返回给定域的值, 如果给定域不存在于哈希表中， 又或者给定的哈希表并不存在， 那么命令返回 nil
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hget">Redis Documentation: HGET</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public <T> T hGet(@NonNull String key, @NonNull Object field,
		BiFunction<String, Object, T> loader, boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		T value = (T) redisTemplate.opsForHash().get(key, field);
		if (value != null) {
			return returnVal(value);
		}

		String lockKey = key + "@" + field;
		synchronized (KEY_LOCKS.computeIfAbsent(lockKey, v -> new Object())) {
			value = (T) redisTemplate.opsForHash().get(key, field);
			if (value != null) {
				return returnVal(value);
			}

			try {
				value = loader.apply(key, field);
				this.hSet(key, field, value, cacheNullVal);
			} finally {
				KEY_LOCKS.remove(lockKey);
			}
		}
		return returnVal(value);
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param cacheNullValues 是否缓存空值
	 * @return 默认情况下返回给定域的值, 如果给定域不存在于哈希表中， 又或者给定的哈希表并不存在， 那么命令返回 nil
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hget">Redis Documentation: HGET</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public <T> T hGet(@NonNull CacheHashKey key, boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;

		T value = (T) redisTemplate.opsForHash().get(key.getKey(), key.getField());
		if (value == null && cacheNullVal) {
			hSet(key, newNullVal(), true);
		}
		// NullVal 值
		return returnVal(value);
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param cacheNullValues 是否缓存空值
	 * @param loader          加载器
	 * @return 默认情况下返回给定域的值, 如果给定域不存在于哈希表中， 又或者给定的哈希表并不存在， 那么命令返回 nil
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hget">Redis Documentation: HGET</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public <T> T hGet(@NonNull CacheHashKey key, Function<CacheHashKey, T> loader,
		boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		T value = (T) redisTemplate.opsForHash().get(key.getKey(), key.getField());
		if (value != null) {
			return returnVal(value);
		}
		String lockKey = key.getKey() + "@" + key.getField();
		synchronized (KEY_LOCKS.computeIfAbsent(lockKey, v -> new Object())) {
			value = (T) redisTemplate.opsForHash().get(key.getKey(), key.getField());
			if (value != null) {
				return returnVal(value);
			}
			try {
				value = loader.apply(key);
				this.hSet(key, value, cacheNullVal);
			} finally {
				KEY_LOCKS.remove(key.getKey());
			}
		}
		return returnVal(value);
	}

	/**
	 * 检查给定域 field 是否存在于哈希表 hash 当中
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param field 一定不能为 {@literal null}.
	 * @return 是否存在
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hexists">Redis Documentation: HEXISTS</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean hExists(@NonNull String key, @NonNull Object field) {
		return redisTemplate.opsForHash().hasKey(key, field);
	}

	/**
	 * 检查给定域 field 是否存在于哈希表 hash 当中
	 *
	 * @param cacheHashKey 一定不能为 {@literal null}.
	 * @return 是否存在
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hexists">Redis Documentation: HEXISTS</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean hExists(@NonNull CacheHashKey cacheHashKey) {
		return redisTemplate.opsForHash().hasKey(cacheHashKey.getKey(), cacheHashKey.getField());
	}

	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param fields 一定不能为 {@literal null}.
	 * @return 删除的数量
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hdel">Redis Documentation: HDEL</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long hDel(@NonNull String key, Object... fields) {
		return redisTemplate.opsForHash().delete(key, fields);
	}

	/**
	 * hDel
	 *
	 * @param key key
	 * @return {@link java.lang.Long }
	 * @author shuigedeng
	 * @since 2021-09-07 21:09:14
	 */
	public Long hDel(@NonNull CacheHashKey key) {
		return redisTemplate.opsForHash().delete(key.getKey(), key.getField());
	}


	/**
	 * 返回哈希表 key 中域的数量。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 哈希表中域的数量。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hlen">Redis Documentation: HLEN</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long hLen(@NonNull String key) {
		return redisTemplate.opsForHash().size(key);
	}

	/**
	 * 返回哈希表 key 中， 与给定域 field 相关联的值的字符串长度
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param field 一定不能为 {@literal null}.
	 * @return 返回哈希表 key 中， 与给定域 field 相关联的值的字符串长度。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hstrlen">Redis Documentation: HSTRLEN</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long hStrLen(@NonNull String key, @NonNull Object field) {
		return redisTemplate.opsForHash().lengthOfValue(key, field);
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment 。 增量也可以为负数，相当于对给定域进行减法操作。 如果 key 不存在，一个新的哈希表被创建并执行
	 * HINCRBY 命令。 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
	 * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
	 *
	 * @param key       一定不能为 {@literal null}.
	 * @param increment 增量
	 * @return 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hincrby">Redis Documentation: HINCRBY</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long hIncrBy(@NonNull CacheHashKey key, long increment) {
		Long hIncrBy = redisTemplate.opsForHash()
			.increment(key.getKey(), key.getField(), increment);
		if (key.getExpire() != null) {
			redisTemplate.expire(key.getKey(), key.getExpire());
		}
		return hIncrBy;
	}

	/**
	 * 为哈希表 key 中的域 field 加上浮点数增量 increment 。 如果哈希表中没有域 field ，那么 HINCRBYFLOAT 会先将域 field 的值设为 0
	 * ，然后再执行加法操作。 如果键 key 不存在，那么 HINCRBYFLOAT 会先创建一个哈希表，再创建域 field ，最后再执行加法操作。 当以下任意一个条件发生时，返回一个错误：
	 * 1:域 field 的值不是字符串类型(因为 redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型） 2:域 field 当前的值或给定的增量 increment
	 * 不能解释(parse)为双精度浮点数(double precision floating point number) HINCRBYFLOAT 命令的详细功能和 INCRBYFLOAT
	 * 命令类似，请查看 INCRBYFLOAT 命令获取更多相关信息。
	 *
	 * @param key       一定不能为 {@literal null}.
	 * @param increment 增量
	 * @return 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hincrbyfloat">Redis Documentation: HINCRBYFLOAT</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Double hIncrByFloat(@NonNull CacheHashKey key, double increment) {
		Double hIncrBy = redisTemplate.opsForHash()
			.increment(key.getKey(), key.getField(), increment);
		if (key.getExpire() != null) {
			redisTemplate.expire(key.getKey(), key.getExpire());
		}
		return hIncrBy;
	}

	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中。 此命令会覆盖哈希表中已存在的域。 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param hash            一定不能为 {@literal null}.
	 * @param cacheNullValues 是否缓存空值
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hmset">Redis Documentation: hmset</a>
	 * @since 2021-09-07 21:04:53
	 */
	public <K, V> void hmSet(@NonNull String key, @NonNull Map<K, V> hash,
		boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		Map<Object, Object> newMap = new HashMap<>(hash.size());

		hash.forEach((k, v) -> {
			if (v == null && cacheNullVal) {
				newMap.put(k, newNullVal());
			} else {
				newMap.put(k, v);
			}
		});

		redisTemplate.opsForHash().putAll(key, newMap);
	}

	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。 如果给定的域不存在于哈希表，那么返回一个 nil 值。 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行
	 * HMGET 操作将返回一个只带有 nil 值的表。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param fields 一定不能为 {@literal null}.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hmget">Redis Documentation: hmget</a>
	 * @since 2021-09-07 21:04:53
	 */
	public List<Object> hmGet(@NonNull String key, @NonNull Object... fields) {
		return hmGet(key, Arrays.asList(fields));
	}

	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。 如果给定的域不存在于哈希表，那么返回一个 nil 值。 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行
	 * HMGET 操作将返回一个只带有 nil 值的表。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param fields 一定不能为 {@literal null}.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hmget">Redis Documentation: hmget</a>
	 * @since 2021-09-07 21:04:53
	 */
	public List<Object> hmGet(@NonNull String key, @NonNull Collection<Object> fields) {
		List<Object> list = redisTemplate.opsForHash().multiGet(key, fields);
		return list.stream().map(this::returnVal).collect(Collectors.toList());
	}

	/**
	 * 返回哈希表 key 中的所有域。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 所有的 filed
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hkeys">Redis Documentation: hkeys</a>
	 * @since 2021-09-07 21:04:53
	 */
	public <HK> Set<HK> hKeys(@NonNull String key) {
		return (Set<HK>) redisTemplate.opsForHash().keys(key);
	}


	/**
	 * 返回哈希表 key 中所有域的值。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 一个包含哈希表中所有值的表。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hvals">Redis Documentation: hvals</a>
	 * @since 2021-09-07 21:04:53
	 */
	public <HV> List<HV> hVals(@NonNull String key) {
		return (List<HV>) redisTemplate.opsForHash().values(key);
	}


	/**
	 * 返回哈希表 key 中，所有的域和值。 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 以列表形式返回哈希表的域和域的值
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hgetall">Redis Documentation: hgetall</a>
	 * @since 2021-09-07 21:04:53
	 */
	public <K, V> Map<K, V> hGetAll(@NonNull String key) {
		Map<K, V> map = (Map<K, V>) redisTemplate.opsForHash().entries(key);
		return returnMapVal(map);
	}

	/**
	 * hGetAll
	 *
	 * @param key key
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-07 21:09:47
	 */
	public <K, V> Map<K, V> hGetAll(@NonNull CacheHashKey key) {
		Map<K, V> map = (Map<K, V>) redisTemplate.opsForHash().entries(key.getKey());
		return returnMapVal(map);
	}

	/**
	 * returnMapVal
	 *
	 * @param map map
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-07 21:09:51
	 */
	private <K, V> Map<K, V> returnMapVal(Map<K, V> map) {
		Map<K, V> newMap = new HashMap<>(map.size());
		if (CollUtil.isNotEmpty(map)) {
			map.forEach((k, v) -> {
				if (!isNullVal(v)) {
					newMap.put(k, v);
				}
			});
		}
		return newMap;
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param cacheNullValues 是否缓存空值
	 * @param loader          加载器
	 * @return 默认情况下返回给定域的值, 如果给定域不存在于哈希表中， 又或者给定的哈希表并不存在， 那么命令返回 nil
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/hget">Redis Documentation: HGET</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public <K, V> Map<K, V> hGetAll(@NonNull CacheHashKey key,
		Function<CacheHashKey, Map<K, V>> loader, boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		Map<K, V> map = (Map<K, V>) redisTemplate.opsForHash().entries(key.getKey());
		if (CollUtil.isNotEmpty(map)) {
			return returnMapVal(map);
		}
		String lockKey = key.getKey();
		synchronized (KEY_LOCKS.computeIfAbsent(lockKey, v -> new Object())) {
			map = (Map<K, V>) redisTemplate.opsForHash().entries(key.getKey());
			if (CollUtil.isNotEmpty(map)) {
				return returnMapVal(map);
			}
			try {
				map = loader.apply(key);
				this.hmSet(key.getKey(), map, cacheNullVal);
			} finally {
				KEY_LOCKS.remove(key.getKey());
			}
		}
		return returnMapVal(map);
	}
	// ---------------------------- hash end ----------------------------

	// ---------------------------- list start ----------------------------

	/**
	 * 将一个或多个值 value 插入到列表 key 的表头 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令
	 * LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c
	 * 三个命令。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param values 值
	 * @return 返回列表的长度
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Long lPush(@NonNull String key, Object... values) {
		return redisTemplate.opsForList().leftPushAll(key, values);
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表头 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令
	 * LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c
	 * 三个命令。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param values 值
	 * @return 返回列表的长度
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Long lPush(@NonNull String key, Collection<Object> values) {
		return redisTemplate.opsForList().leftPushAll(key, values);
	}

	/**
	 * 将值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表。 和 LPUSH key value [value …] 命令相反，当 key 不存在时，
	 * LPUSHX 命令什么也不做
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param values 值
	 * @return 返回列表的长度
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/lpushx">Redis Documentation: LPUSHX</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Long lPushX(@NonNull String key, Object values) {
		return redisTemplate.opsForList().leftPushIfPresent(key, values);
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist
	 * 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist
	 * c 。 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param values 值
	 * @return 返回列表的长度
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Long rPush(@NonNull String key, Object... values) {
		return redisTemplate.opsForList().rightPushAll(key, values);
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist
	 * 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist
	 * c 。 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param values 值
	 * @return 返回列表的长度
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Long rPush(@NonNull String key, Collection<Object> values) {
		return redisTemplate.opsForList().rightPushAll(key, values);
	}

	/**
	 * 将值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。
	 * <p>
	 * 和 RPUSH key value [value …] 命令相反，当 key 不存在时， RPUSHX 命令什么也不做。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param value 值
	 * @return 返回列表的长度
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/rpushx">Redis Documentation: RPUSHX</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Long rPushX(@NonNull String key, Object value) {
		return redisTemplate.opsForList().rightPushIfPresent(key, value);
	}

	/**
	 * 如果存在key，则设置key以保存字符串值。
	 *
	 * @param key     一定不能为 {@literal null}.
	 * @param value   一定不能为 {@literal null}.
	 * @param timeout 一定不能为 {@literal null}.
	 * @return 设置成功返回true。
	 * @throws IllegalArgumentException 如果{@code key} 或 {@code value} 不存在
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Boolean setXx(@NonNull String key, String value, Duration timeout,
		boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		return redisTemplate.opsForValue()
			.setIfPresent(key, cacheNullVal && value == null ? newNullVal() : value, timeout);
	}

	/**
	 * 只在键 key 不存在的情况下， 将键 key 的值设置为 value 。
	 * <p>
	 * 若键 key 已经存在， 则 SETNX 命令不做任何动作。
	 * <p>
	 * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param value 一定不能为 {@literal null}.
	 * @return 设置成功返回true
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/setnx">Redis Documentation: SETNX</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Boolean setNx(@NonNull String key, String value, boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		return redisTemplate.opsForValue()
			.setIfAbsent(key, cacheNullVal && value == null ? newNullVal() : value);
	}

	/**
	 * 返回与键 key 相关联的 value 值
	 * <p>
	 * 如果键 key 不存在， 那么返回特殊值 null ； 否则， 返回键 key 的值。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param cacheNullValues 是否缓存空值
	 * @return 如果键 key 不存在， 那么返回特殊值 null ； 否则， 返回键 key 的值。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/get">Redis Documentation: GET</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public <T> T get(@NonNull String key, boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		T value = (T) redisTemplate.opsForValue().get(key);
		if (value == null && cacheNullVal) {
			set(key, newNullVal(), true);
		}
		// NullVal 值
		return returnVal(value);
	}

	/**
	 * 返回与键 key 相关联的 value 值
	 * <p>
	 * 如果值不存在， 那么调用 loader 方法获取数据后，set 到缓存
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param loader          缓存加载器
	 * @param cacheNullValues 是否缓存空值
	 * @return 如果redis中没值，先加载loader 的数据，若加载loader 的值为null，直接返回， 否则 设置后loader值后返回。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/get">Redis Documentation: GET</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public <T> T get(@NonNull String key, Function<String, T> loader, boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		T value = (T) redisTemplate.opsForValue().get(key);
		if (value != null) {
			return returnVal(value);
		}

		// 加锁解决缓存击穿
		synchronized (KEY_LOCKS.computeIfAbsent(key, v -> new Object())) {
			value = (T) redisTemplate.opsForValue().get(key);
			if (value != null) {
				return returnVal(value);
			}

			try {
				value = loader.apply(key);
				this.set(key, value, cacheNullVal);
			} finally {
				KEY_LOCKS.remove(key);
			}
		}
		// NullVal 值
		return returnVal(value);
	}

	/**
	 * 将键 key 的值设为 value ， 并返回键 key 在被设置之前的旧值。
	 * <p>
	 * 返回给定键 key 的旧值。 如果键 key 没有旧值， 也即是说， 键 key 在被设置之前并不存在， 那么命令返回 nil 。 当键 key 存在但不是字符串类型时，
	 * 命令返回一个错误。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param value 值
	 * @return 如果键 key 不存在， 那么返回特殊值 null ； 否则， 返回给定键 key 的旧值
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/getset">Redis Documentation: GETSET</a>
	 * @since 2021-09-07 21:04:53
	 */
	public <T> T getSet(@NonNull String key, Object value) {
		T val = (T) redisTemplate.opsForValue()
			.getAndSet(key, value == null ? newNullVal() : value);
		return returnVal(val);
	}

	/**
	 * 返回与键 key 相关联的 value 值
	 * <p>
	 * 如果键 key 不存在， 那么返回特殊值 null ； 否则， 返回键 key 的值。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param cacheNullValues 是否缓存空值
	 * @return 如果键 key 不存在， 那么返回特殊值 null ； 否则， 返回键 key 的值。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/get">Redis Documentation: GET</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public <T> T get(@NonNull CacheKey key, boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		T value = (T) redisTemplate.opsForValue().get(key.getKey());
		if (value == null && cacheNullVal) {
			set(key, newNullVal(), true);
		}
		// NullVal 值
		return returnVal(value);
	}

	/**
	 * 返回与键 key 相关联的 value 值
	 * <p>
	 * 如果键 key 不存在， 那么返回特殊值 null ； 否则， 返回键 key 的值。
	 *
	 * @param key             一定不能为 {@literal null}.
	 * @param loader          加载器
	 * @param cacheNullValues 是否缓存空值
	 * @return 如果键 key 不存在， 那么返回特殊值 null ； 否则， 返回键 key 的值。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/get">Redis Documentation: GET</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public <T> T get(@NonNull CacheKey key, Function<CacheKey, T> loader,
		boolean... cacheNullValues) {
		boolean cacheNullVal =
			cacheNullValues.length > 0 ? cacheNullValues[0] : defaultCacheNullVal;
		T value = (T) redisTemplate.opsForValue().get(key.getKey());

		if (value != null) {
			return returnVal(value);
		}
		synchronized (KEY_LOCKS.computeIfAbsent(key.getKey(), v -> new Object())) {
			value = (T) redisTemplate.opsForValue().get(key.getKey());
			if (value != null) {
				return returnVal(value);
			}

			try {
				value = loader.apply(key);
				this.set(key, value, cacheNullVal);
			} finally {
				KEY_LOCKS.remove(key.getKey());
			}
		}
		return returnVal(value);
	}

	/**
	 * 返回键 key 储存的字符串值的长度
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 字符串值的长度。 当键 key 不存在时， 命令返回 0
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/strlen">Redis Documentation: STRLEN</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Long strLen(@NonNull String key) {
		return redisTemplate.opsForValue().size(key);
	}

	/**
	 * 如果键 key 已经存在并且它的值是一个字符串， APPEND 命令将把 value 追加到键 key 现有值的末尾。 如果 key 不存在， APPEND 就简单地将键 key
	 * 的值设为 value ， 就像执行 SET key value 一样。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 追加 value 之后， 键 key 的值的长度
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/append">Redis Documentation: APPEND</a>
	 * @since 2021-09-07 21:04:53
	 */
	@Nullable
	public Integer append(@NonNull String key, String value) {
		return redisTemplate.opsForValue().append(key, value);
	}

	/**
	 * 从偏移量 offset 开始， 用 value 参数覆写(overwrite)键 key 储存的字符串值。
	 * <p>
	 * 不存在的键 key 当作空白字符串处理。
	 * <p>
	 * SETRANGE 命令会确保字符串足够长以便将 value 设置到指定的偏移量上， 如果键 key 原来储存的字符串长度比偏移量小(比如字符串只有 5 个字符长，但你设置的 offset
	 * 是 10 )， 那么原字符和偏移量之间的空白将用零字节(zerobytes, "\x00" )进行填充。
	 * <p>
	 * 因为 Redis 字符串的大小被限制在 512 兆(megabytes)以内， 所以用户能够使用的最大偏移量为 2^29-1(536870911) ， 如果你需要使用比这更大的空间，
	 * 请使用多个 key 。
	 *
	 * @param key    一定不能为 {@literal null}.
	 * @param value  字符串
	 * @param offset 最大不能超过 536870911
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/setrange">Redis Documentation: SETRANGE</a>
	 * @since 2021-09-07 21:04:53
	 */
	public void setRange(@NonNull String key, String value, long offset) {
		//TODO 序列化hug
		redisTemplate.opsForValue().set(key, value, offset);
	}

	/**
	 * 返回键 key 储存的字符串值的指定部分， 字符串的截取范围由 start 和 end 两个偏移量决定 (包括 start 和 end 在内)。 负数偏移量表示从字符串的末尾开始计数，
	 * -1 表示最后一个字符， -2 表示倒数第二个字符， 以此类推。 GETRANGE 通过保证子字符串的值域(range)不超过实际字符串的值域来处理超出范围的值域请求。
	 *
	 * @param key   一定不能为 {@literal null}.
	 * @param start 开始偏移量
	 * @param end   结束偏移量
	 * @return GETRANGE 命令会返回字符串值的指定部分。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/getrange">Redis Documentation: GETRANGE</a>
	 * @since 2021-09-07 21:04:53
	 */
	public String getRange(@NonNull String key, long start, long end) {
		//TODO 序列化hug
		return redisTemplate.opsForValue().get(key, start, end);
	}

	/**
	 * mSetMap
	 *
	 * @param map          map
	 * @param cacheNullVal cacheNullVal
	 * @return {@link Map }
	 * @author shuigedeng
	 * @since 2021-09-07 21:10:56
	 */
	private Map<String, Object> mSetMap(@NonNull Map<String, Object> map, boolean cacheNullVal) {
		Map<String, Object> mSetMap = new HashMap<>(map.size());
		map.forEach((k, v) -> {
			if (v == null && cacheNullVal) {
				mSetMap.put(k, newNullVal());
			} else {
				mSetMap.put(k, v);
			}
		});
		return mSetMap;
	}

	/**
	 * 同时为一个或多个键设置值。 如果某个给定键已经存在， 那么 MSET 将使用新值去覆盖旧值， 如果这不是你所希望的效果， 请考虑使用 MSETNX 命令，
	 * 这个命令只会在所有给定键都不存在的情况下进行设置。 MSET 是一个原子性(atomic)操作， 所有给定键都会在同一时间内被设置，
	 * 不会出现某些键被设置了但是另一些键没有被设置的情况。
	 *
	 * @param map          一定不能为 {@literal null}.
	 * @param cacheNullVal 是否缓存空值
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/mset">Redis Documentation: MSET</a>
	 * @since 2021-09-07 21:04:53
	 */
	public void mSet(@NonNull Map<String, Object> map, boolean cacheNullVal) {
		Map<String, Object> mSetMap = mSetMap(map, cacheNullVal);

		redisTemplate.opsForValue().multiSet(mSetMap);
	}

	/**
	 * 同时为一个或多个键设置值。 如果某个给定键已经存在， 那么 MSET 将使用新值去覆盖旧值， 如果这不是你所希望的效果， 请考虑使用 MSETNX 命令，
	 * 这个命令只会在所有给定键都不存在的情况下进行设置。 MSET 是一个原子性(atomic)操作， 所有给定键都会在同一时间内被设置，
	 * 不会出现某些键被设置了但是另一些键没有被设置的情况。
	 *
	 * @param map 一定不能为 {@literal null}.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/mset">Redis Documentation: MSET</a>
	 * @since 2021-09-07 21:04:53
	 */
	public void mSet(@NonNull Map<String, Object> map) {
		mSet(map, defaultCacheNullVal);
	}

	/**
	 * 当且仅当所有给定键都不存在时， 为所有给定键设置值。 即使只有一个给定键已经存在， MSETNX 命令也会拒绝执行对所有键的设置操作。 MSETNX 是一个原子性(atomic)操作，
	 * 所有给定键要么就全部都被设置， 要么就全部都不设置， 不可能出现第三种状态。
	 *
	 * @param map          一定不能为 {@literal null}.
	 * @param cacheNullVal 是否缓存空值
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/msetnx">Redis Documentation: MSET</a>
	 * @since 2021-09-07 21:04:53
	 */
	public void mSetNx(@NonNull Map<String, Object> map, boolean cacheNullVal) {
		Map<String, Object> mSetMap = mSetMap(map, cacheNullVal);

		redisTemplate.opsForValue().multiSetIfAbsent(mSetMap);
	}

	/**
	 * 当且仅当所有给定键都不存在时， 为所有给定键设置值。 即使只有一个给定键已经存在， MSETNX 命令也会拒绝执行对所有键的设置操作。 MSETNX 是一个原子性(atomic)操作，
	 * 所有给定键要么就全部都被设置， 要么就全部都不设置， 不可能出现第三种状态。
	 *
	 * @param map 一定不能为 {@literal null}.
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/msetnx">Redis Documentation: MSET</a>
	 * @since 2021-09-07 21:04:53
	 */
	public void mSetNx(@NonNull Map<String, Object> map) {
		mSetNx(map, defaultCacheNullVal);
	}


	/**
	 * 返回所有(一个或多个)给定 key 的值, 值按请求的键的顺序返回。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil
	 *
	 * @param keys 一定不能为 {@literal null}.
	 * @return 返回一个列表， 列表中包含了所有给定键的值,并按给定key的顺序排列
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/mget">Redis Documentation: MGET</a>
	 * @since 2021-09-07 21:04:53
	 */
	public <T> List<T> mGet(@NonNull String... keys) {
		return mGet(Arrays.asList(keys));
	}

	/**
	 * 返回所有(一个或多个)给定 key 的值, 值按请求的键的顺序返回。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil
	 *
	 * @param keys 一定不能为 {@literal null}.
	 * @return 返回一个列表， 列表中包含了所有给定键的值,并按给定key的顺序排列
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/mget">Redis Documentation: MGET</a>
	 * @since 2021-09-07 21:04:53
	 */
	public <T> List<T> mGet(@NonNull CacheKey... keys) {
		return mGetByCacheKey(Arrays.asList(keys));
	}

	/**
	 * 返回所有(一个或多个)给定 key 的值, 值按请求的键的顺序返回。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil
	 *
	 * @param keys 一定不能为 {@literal null}.
	 * @return 返回一个列表， 列表中包含了所有给定键的值,并按给定key的顺序排列
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/mget">Redis Documentation: MGET</a>
	 * @since 2021-09-07 21:04:53
	 */
	public <T> List<T> mGet(@NonNull Collection<String> keys) {
		List<T> list = (List<T>) redisTemplate.opsForValue().multiGet(keys);
		return list == null ? Collections.emptyList()
			: list.stream().map(this::returnVal).collect(Collectors.toList());
	}

	/**
	 * 返回所有(一个或多个)给定 key 的值, 值按请求的键的顺序返回。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil
	 *
	 * @param cacheKeys 一定不能为 {@literal null}.
	 * @return 返回一个列表， 列表中包含了所有给定键的值,并按给定key的顺序排列
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/mget">Redis Documentation: MGET</a>
	 * @since 2021-09-07 21:04:53
	 */
	public <T> List<T> mGetByCacheKey(@NonNull Collection<CacheKey> cacheKeys) {
		List<String> keys = cacheKeys.stream().map(CacheKey::getKey).collect(Collectors.toList());
		List<T> list = (List<T>) redisTemplate.opsForValue().multiGet(keys);
		return list == null ? Collections.emptyList()
			: list.stream().map(this::returnVal).collect(Collectors.toList());
	}

	/**
	 * 为键 key 储存的数字值加上一。 如果键 key 不存在， 那么它的值会先被初始化为 0 ， 然后再执行 INCR 命令。 如果键 key 储存的值不能被解释为数字， 那么 INCR
	 * 命令将返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * <p>
	 * 提示： INCR 命令是一个针对字符串的操作。 因为 Redis 并没有专用的整数类型， 所以键 key 储存的值在执行 INCR 命令时会被解释为十进制 64 位有符号整数。
	 *
	 * @param key 一定不能为 {@literal null}.
	 * @return 返回键 key 在执行加一操作之后的值。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/incr">Redis Documentation: INCR</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long incr(@NonNull CacheKey key) {
		Long increment = redisTemplate.opsForValue().increment(key.getKey());
		setExpire(key);
		return increment;
	}

	/**
	 * 为键 key 储存的数字值加上增量 increment 。 如果键 key 不存在， 那么键 key 的值会先被初始化为 0 ， 然后再执行 INCRBY 命令。 如果键 key
	 * 储存的值不能被解释为数字， 那么 INCRBY 命令将返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。 关于递增(increment) /
	 * 递减(decrement)操作的更多信息， 请参见 INCR 命令的文档。
	 *
	 * @param key       一定不能为 {@literal null}.
	 * @param increment 增量值
	 * @return 返回键 key 在执行加上 increment ，操作之后的值。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/incrby">Redis Documentation: INCRBY</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Long incrBy(@NonNull CacheKey key, long increment) {
		Long incrBy = redisTemplate.opsForValue().increment(key.getKey(), increment);
		setExpire(key);
		return incrBy;
	}

	/**
	 * 为键 key 储存的值加上浮点数增量 increment 。 如果键 key 不存在， 那么 INCRBYFLOAT 会先将键 key 的值设为 0 ， 然后再执行加法操作。
	 * 如果命令执行成功， 那么键 key 的值会被更新为执行加法计算之后的新值， 并且新值会以字符串的形式返回给调用者。
	 * <p>
	 * 无论是键 key 的值还是增量 increment ， 都可以使用像 2.0e7 、 3e5 、 90e-2 那样的指数符号(exponential notation)来表示， 但是，
	 * 执行 INCRBYFLOAT 命令之后的值总是以同样的形式储存， 也即是， 它们总是由一个数字， 一个（可选的）小数点和一个任意长度的小数部分组成（比如 3.14 、 69.768
	 * ，诸如此类)， 小数部分尾随的 0 会被移除， 如果可能的话， 命令还会将浮点数转换为整数（比如 3.0 会被保存成 3 ）。 此外， 无论加法计算所得的浮点数的实际精度有多长，
	 * INCRBYFLOAT 命令的计算结果最多只保留小数点的后十七位。
	 * <p>
	 * 当以下任意一个条件发生时， 命令返回一个错误： 键 key 的值不是字符串类型(因为 Redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型）； 键 key
	 * 当前的值或者给定的增量 increment 不能被解释(parse)为双精度浮点数。
	 *
	 * @param key       一定不能为 {@literal null}.
	 * @param increment 增量值
	 * @return 在加上增量 increment 之后， 键 key 的值。
	 * @author shuigedeng
	 * @see <a href="https://redis.io/commands/incrbyfloat">Redis Documentation: INCRBYFLOAT</a>
	 * @since 2021-09-07 21:04:53
	 */
	public Double incrByFloat(@NonNull CacheKey key, double increment) {
		Double incrByFloat = redisTemplate.opsForValue().increment(key.getKey(), increment);
		setExpire(key);
		return incrByFloat;
	}

	/**
	 * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
	 *
	 * @param keys   the keys
	 * @param values the values
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public void set(final String[] keys, final Object[] values) {
		redisTemplate.execute((RedisCallback<Long>) connection -> {
			RedisSerializer<String> serializer = getRedisSerializer();
			for (int i = 0; i < keys.length; i++) {
				set(keys[i], values[i]);
				//byte[] bKeys = serializer.serialize(keys[i]);
				//byte[] bValues = OBJECT_SERIALIZER.serialize(values[i]);
				//connection.set(bKeys, bValues);
			}
			return 1L;
		});
	}


	/**
	 * 添加到缓存
	 *
	 * @param key   the key
	 * @param value the value
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public void set(final String key, final Object value) {
		redisTemplate.opsForValue().set(key, value);

		//redisTemplate.execute((RedisCallback<Long>) connection -> {
		//	RedisSerializer<String> serializer = getRedisSerializer();
		//	byte[] keys = serializer.serialize(key);
		//	byte[] values = OBJECT_SERIALIZER.serialize(value);
		//	connection.set(keys, values);
		//	LogUtil.info("[redisTemplate redis]放入 缓存  url:{}", key);
		//	return 1L;
		//});
	}

	/**
	 * 查询在这个时间段内即将过期的key
	 *
	 * @param key  the key
	 * @param time the time
	 * @return the list
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public List<String> willExpire(final String key, final long time) {
		final List<String> keysList = new ArrayList<>();
		redisTemplate.execute((RedisCallback<List<String>>) connection -> {
			Set<String> keys = redisTemplate.keys(key + "*");
			assert keys != null;
			for (String key1 : keys) {
				Long ttl = connection.ttl(key1.getBytes(DEFAULT_CHARSET));
				if (0 <= ttl && ttl <= 2 * time) {
					keysList.add(key1);
				}
			}
			return keysList;
		});
		return keysList;
	}

	/**
	 * 查询在以keyPatten的所有  key
	 *
	 * @param keyPatten the key patten
	 * @return the set
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Set<String> keys(final String keyPatten) {
		return redisTemplate.execute(
			(RedisCallback<Set<String>>) connection -> redisTemplate.keys(keyPatten + "*"));
	}

	/**
	 * 根据key获取对象
	 *
	 * @param key the key
	 * @return the byte [ ]
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public byte[] get(final byte[] key) {
		byte[] result = redisTemplate
			.execute((RedisCallback<byte[]>) connection -> connection.get(key));
		LogUtil.info("[redisTemplate redis]取出 缓存  url:{} ", key);
		return result;
	}

	/**
	 * 根据key获取对象
	 *
	 * @param key the key
	 * @return the string
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Object get(final String key) {
		return redisTemplate.opsForValue().get(key);

		//try {
		//	Object resultStr = redisTemplate.execute((RedisCallback<Object>) connection -> {
		//		RedisSerializer<String> serializer = getRedisSerializer();
		//		byte[] keys = serializer.serialize(key);
		//		assert keys != null;
		//		byte[] values = connection.get(keys);
		//		return OBJECT_SERIALIZER.deserialize(values);
		//	});
		//	LogUtil.info("[redisTemplate redis]取出 缓存  url:{} ", key);
		//	return resultStr;
		//} catch (Exception e) {
		//	return null;
		//}
	}


	/**
	 * 根据key获取对象
	 *
	 * @param keyPatten the key patten
	 * @return the keys values
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Map<String, Object> getKeysValues(final String keyPatten) {
		LogUtil.info("[redisTemplate redis]  getValues()  patten={} ", keyPatten);
		return redisTemplate.execute((RedisCallback<Map<String, Object>>) connection -> {
			RedisSerializer<String> serializer = getRedisSerializer();
			Map<String, Object> maps = new HashMap<>(16);
			Set<String> keys = redisTemplate.keys(keyPatten + "*");
			if (CollectionUtils.isNotEmpty(keys)) {
				for (String key : keys) {
					//byte[] bKeys = serializer.serialize(key);
					//byte[] bValues = connection.get(bKeys);
					//Object value = OBJECT_SERIALIZER.deserialize(bValues);
					maps.put(key, get(key));
				}
			}
			return maps;
		});
	}

	/**
	 * Ops for hash hash operations.
	 *
	 * @return the hash operations
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public HashOperations<String, String, Object> opsForHash() {
		return redisTemplate.opsForHash();
	}

	/**
	 * 对HashMap操作
	 *
	 * @param key       the key
	 * @param hashKey   the hash key
	 * @param hashValue the hash value
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public void putHashValue(String key, String hashKey, Object hashValue) {
		LogUtil.info("[redisTemplate redis]  putHashValue()  key={},hashKey={},hashValue={} ", key,
			hashKey, hashValue);
		opsForHash().put(key, hashKey, hashValue);
	}

	/**
	 * 获取单个field对应的值
	 *
	 * @param key     the key
	 * @param hashKey the hash key
	 * @return the hash values
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Object getHashValues(String key, String hashKey) {
		LogUtil.info("[redisTemplate redis]  getHashValues()  key={},hashKey={}", key, hashKey);
		return opsForHash().get(key, hashKey);
	}

	/**
	 * 根据key值删除
	 *
	 * @param key      the key
	 * @param hashKeys the hash keys
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public void delHashValues(String key, Object... hashKeys) {
		LogUtil.info("[redisTemplate redis]  delHashValues()  key={}", key);
		opsForHash().delete(key, hashKeys);
	}

	/**
	 * key只匹配map
	 *
	 * @param key the key
	 * @return the hash value
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Map<String, Object> getHashValue(String key) {
		LogUtil.info("[redisTemplate redis]  getHashValue()  key={}", key);
		return opsForHash().entries(key);
	}

	/**
	 * 批量添加
	 *
	 * @param key the key
	 * @param map the map
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public void putHashValues(String key, Map<String, Object> map) {
		opsForHash().putAll(key, map);
	}

	/**
	 * 集合数量
	 *
	 * @return the long
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public long dbSize() {
		return redisTemplate.execute(RedisServerCommands::dbSize);
	}

	/**
	 * 清空redis存储的数据
	 *
	 * @return the string
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public String flushDB() {
		return redisTemplate.execute((RedisCallback<String>) connection -> {
			connection.flushDb();
			return "ok";
		});
	}

	/**
	 * 判断某个主键是否存在
	 *
	 * @param key the key
	 * @return the boolean
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean exists(final String key) {
		return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection
			.exists(key.getBytes(DEFAULT_CHARSET)));
	}


	/**
	 * 删除key
	 *
	 * @param keys the keys
	 * @return the long
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long del(final String... keys) {
		return redisTemplate.execute((RedisCallback<Long>) connection -> {
			Long result = 0L;
			for (String key : keys) {
				result = connection.del(key.getBytes(DEFAULT_CHARSET));
			}
			return result;
		});
	}

	/**
	 * 获取 RedisSerializer
	 *
	 * @return the redis serializer
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	protected RedisSerializer<String> getRedisSerializer() {
		return redisTemplate.getStringSerializer();
	}

	/**
	 * 对某个主键对应的值加一,value值必须是全数字的字符串
	 *
	 * @param key the key
	 * @return the long
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public long incr(final String key) {
		return redisTemplate.execute((RedisCallback<Long>) connection -> {
			RedisSerializer<String> redisSerializer = getRedisSerializer();
			return connection.incr(redisSerializer.serialize(key));
		});
	}

	/**
	 * redis List 引擎
	 *
	 * @return the list operations
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public ListOperations<String, Object> opsForList() {
		return redisTemplate.opsForList();
	}

	/**
	 * redis List数据结构 : 将一个或多个值 value 插入到列表 key 的表头
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the long
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long leftPush(String key, Object value) {
		return opsForList().leftPush(key, value);
	}

	/**
	 * redis List数据结构 : 移除并返回列表 key 的头元素
	 *
	 * @param key the key
	 * @return the string
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Object leftPop(String key) {
		return opsForList().leftPop(key);
	}

	/**
	 * redis List数据结构 :将一个或多个值 value 插入到列表 key 的表尾(最右边)。
	 *
	 * @param key   the key
	 * @param value the value
	 * @return the long
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long in(String key, Object value) {
		return opsForList().rightPush(key, value);
	}

	/**
	 * redis List数据结构 : 移除并返回列表 key 的末尾元素
	 *
	 * @param key the key
	 * @return the string
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Object rightPop(String key) {
		return opsForList().rightPop(key);
	}


	/**
	 * redis List数据结构 : 返回列表 key 的长度 ; 如果 key 不存在，则 key 被解释为一个空列表，返回 0 ; 如果 key 不是列表类型，返回一个错误。
	 *
	 * @param key the key
	 * @return the long
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long length(String key) {
		return opsForList().size(key);
	}


	/**
	 * redis List数据结构 : 根据参数 i 的值，移除列表中与参数 value 相等的元素
	 *
	 * @param key   the key
	 * @param i     the
	 * @param value the value
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public void remove(String key, long i, Object value) {
		opsForList().remove(key, i, value);
	}

	/**
	 * redis List数据结构 : 将列表 key 下标为 index 的元素的值设置为 value
	 *
	 * @param key   the key
	 * @param index the index
	 * @param value the value
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public void set(String key, long index, Object value) {
		opsForList().set(key, index, value);
	}

	/**
	 * redis List数据结构 : 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。
	 *
	 * @param key   the key
	 * @param start the start
	 * @param end   the end
	 * @return the list
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public List<Object> getList(String key, int start, int end) {
		return opsForList().range(key, start, end);
	}

	/**
	 * redis List数据结构 : 批量存储
	 *
	 * @param key  the key
	 * @param list the list
	 * @return the long
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long leftPushAll(String key, List<String> list) {
		return opsForList().leftPushAll(key, list);
	}

	/**
	 * redis List数据结构 : 将值 value 插入到列表 key 当中，位于值 index 之前或之后,默认之后。
	 *
	 * @param key   the key
	 * @param index the index
	 * @param value the value
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public void insert(String key, long index, Object value) {
		opsForList().set(key, index, value);
	}


	/**
	 * 指定缓存失效时间
	 *
	 * @param key  键
	 * @param time 时间(秒)
	 * @return Boolean
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean expire(String key, Long time) {
		try {
			if (time > 0) {
				redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 添加到带有 过期时间的  缓存
	 *
	 * @param key      redis主键
	 * @param value    值
	 * @param time     过期时间
	 * @param timeUnit 过期时间单位
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public void setExpire(final String key, final Object value, final long time,
		final TimeUnit timeUnit) {
		redisTemplate.opsForValue().set(key, value, time, timeUnit);
	}

	/**
	 * setExpire
	 *
	 * @param key             key
	 * @param value           value
	 * @param time            time
	 * @param timeUnit        timeUnit
	 * @param valueSerializer valueSerializer
	 * @author shuigedeng
	 * @since 2021-09-07 21:13:21
	 */
	public void setExpire(final String key, final Object value, final long time,
		final TimeUnit timeUnit, RedisSerializer<Object> valueSerializer) {
		byte[] rawKey = rawKey(key);
		byte[] rawValue = rawValue(value, valueSerializer);

		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				potentiallyUsePsetEx(connection);
				return null;
			}

			public void potentiallyUsePsetEx(RedisConnection connection) {
				if (!TimeUnit.MILLISECONDS.equals(timeUnit) || !failsafeInvokePsetEx(connection)) {
					connection.setEx(rawKey, TimeoutUtils.toSeconds(time, timeUnit), rawValue);
				}
			}

			private boolean failsafeInvokePsetEx(RedisConnection connection) {
				boolean failed = false;
				try {
					connection.pSetEx(rawKey, time, rawValue);
				} catch (UnsupportedOperationException e) {
					failed = true;
				}
				return !failed;
			}
		}, true);
	}

	/**
	 * 根据key获取过期时间
	 *
	 * @param key 键 不能为 null
	 * @return 时间(秒) 返回 0代表为永久有效
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 判断 key是否存在
	 *
	 * @param key 键
	 * @return true 存在 false不存在
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}


	/**
	 * 根据key获取对象
	 *
	 * @param key             the key
	 * @param valueSerializer 序列化
	 * @return the string
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Object get(final String key, RedisSerializer<Object> valueSerializer) {
		byte[] rawKey = rawKey(key);
		return redisTemplate
			.execute(connection -> deserializeValue(connection.get(rawKey), valueSerializer), true);
	}

	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean set(String key, Object value, Long time) {
		try {
			if (time > 0) {
				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key   键
	 * @param value 值
	 * @return true成功 false 失败
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean set(String key, Object value, Duration timeout) {
		try {
			Assert.notNull(timeout, "Timeout must not be null!");
			if (TimeoutUtils.hasMillis(timeout)) {
				redisTemplate.opsForValue()
					.set(key, value, timeout.toMillis(), TimeUnit.MILLISECONDS);
			} else {
				redisTemplate.opsForValue().set(key, value, timeout.getSeconds(), TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 递增
	 *
	 * @param key   键
	 * @param delta 要增加几(大于0)
	 * @return Long
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long incr(String key, Long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 递减
	 *
	 * @param key   键
	 * @param delta 要减少几(小于0)
	 * @return Long
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long decr(String key, Long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, -delta);
	}

	/**
	 * HashGet
	 *
	 * @param key  键 不能为 null
	 * @param item 项 不能为 null
	 * @return 值
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Object hget(String key, String item) {
		return redisTemplate.opsForHash().get(key, item);
	}

	/**
	 * 获取 hashKey对应的所有键值
	 *
	 * @param key 键
	 * @return 对应的多个键值
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Map<Object, Object> hmget(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * HashSet
	 *
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean hmset(String key, Map<String, Object> map) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * HashSet 并设置时间
	 *
	 * @param key  键
	 * @param map  对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean hmset(String key, Map<String, Object> map, Long time) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @return true 成功 false失败
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean hset(String key, String item, Object value) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean hset(String key, String item, Object value, Long time) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 删除hash表中的值
	 *
	 * @param key  键 不能为 null
	 * @param item 项 可以使多个不能为 null
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public void hdel(String key, Object... item) {
		redisTemplate.opsForHash().delete(key, item);
	}

	/**
	 * 判断hash表中是否有该项的值
	 *
	 * @param key  键 不能为 null
	 * @param item 项 不能为 null
	 * @return true 存在 false不存在
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean hHasKey(String key, String item) {
		return redisTemplate.opsForHash().hasKey(key, item);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 *
	 * @param key  键
	 * @param item 项
	 * @param by   要增加几(大于0)
	 * @return Double
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Double hincr(String key, String item, Double by) {
		return redisTemplate.opsForHash().increment(key, item, by);
	}

	/**
	 * hash递减
	 *
	 * @param key  键
	 * @param item 项
	 * @param by   要减少记(小于0)
	 * @return Double
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Double hdecr(String key, String item, Double by) {
		return redisTemplate.opsForHash().increment(key, item, -by);
	}

	/**
	 * 根据 key获取 Set中的所有值
	 *
	 * @param key 键
	 * @return Set
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Set<Object> sGet(String key) {
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 *
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean sHasKey(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 将数据放入set缓存
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long sSet(String key, Object... values) {
		try {
			return redisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return 0L;
		}
	}

	/**
	 * 将set数据放入缓存
	 *
	 * @param key    键
	 * @param time   时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long sSetAndTime(String key, Long time, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().add(key, values);
			if (time > 0) {
				expire(key, time);
			}
			return count;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return 0L;
		}
	}

	/**
	 * 获取set缓存的长度
	 *
	 * @param key 键
	 * @return Long
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long sGetSetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return 0L;
		}
	}

	/**
	 * 移除值为value的
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long setRemove(String key, Object... values) {
		try {
			return redisTemplate.opsForSet().remove(key, values);
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return 0L;
		}
	}

	/**
	 * 获取list缓存的内容
	 *
	 * @param key   键
	 * @param start 开始
	 * @param end   结束 0 到 -1代表所有值
	 * @return List
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public List<Object> lGet(String key, Long start, Long end) {
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 *
	 * @param key 键
	 * @return Long
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long lGetListSize(String key) {
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return 0L;
		}
	}

	/**
	 * 通过索引 获取list中的值
	 *
	 * @param key   键
	 * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推； index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return Object
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Object lGetIndex(String key, Long index) {
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return null;
		}
	}


	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return Boolean
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean lSet(String key, Object value, Long time) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return Boolean
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean lSet(String key, List<Object> value) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return Boolean
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean lSet(String key, List<Object> value, Long time) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 根据索引修改list中的某条数据
	 *
	 * @param key   键
	 * @param index 索引
	 * @param value 值
	 * @return Boolean
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Boolean lUpdateIndex(String key, Long index, Object value) {
		try {
			redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 移除N个值为value
	 *
	 * @param key   键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53
	 */
	public Long lRemove(String key, Long count, Object value) {
		try {
			return redisTemplate.opsForList().remove(key, count, value);
		} catch (Exception e) {
			LogUtil.error("Exception: {}", e.getMessage());
			return 0L;
		}
	}

	/**
	 * redis List数据结构 : 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。
	 *
	 * @param key             the key
	 * @param start           the start
	 * @param end             the end
	 * @param valueSerializer 序列化
	 * @return the list
	 * @author shuigedeng
	 * @since 2021-09-07 21:04:53l
	 */
	public List<Object> getList(String key, int start, int end,
		RedisSerializer<Object> valueSerializer) {
		byte[] rawKey = rawKey(key);
		return redisTemplate.execute(
			connection -> deserializeValues(connection.lRange(rawKey, start, end), valueSerializer),
			true);
	}

	private byte[] rawKey(Object key) {
		Assert.notNull(key, "non null key required");

		if (key instanceof byte[]) {
			return (byte[]) key;
		}
		RedisSerializer<Object> redisSerializer = (RedisSerializer<Object>) redisTemplate
			.getKeySerializer();
		return redisSerializer.serialize(key);
	}

	private byte[] rawValue(Object value, RedisSerializer valueSerializer) {
		if (value instanceof byte[]) {
			return (byte[]) value;
		}

		return valueSerializer.serialize(value);
	}

	private List deserializeValues(List<byte[]> rawValues,
		RedisSerializer<Object> valueSerializer) {
		if (valueSerializer == null) {
			return rawValues;
		}
		return SerializationUtils.deserialize(rawValues, valueSerializer);
	}

	private Object deserializeValue(byte[] value, RedisSerializer<Object> valueSerializer) {
		if (valueSerializer == null) {
			return value;
		}
		return valueSerializer.deserialize(value);
	}

	public Object findByListCacheKey(List<CacheKey> ks) {
		 return mGetByCacheKey(ks);
	}

}
