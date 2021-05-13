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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

/**
 * Redis Repository redis 基本操作 可扩展,基本够用了
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 10:15
 */
@Slf4j
public class RedisRepository {

	/**
	 * 默认编码
	 */
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/**
	 * key序列化
	 */
	private static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();

	/**
	 * value 序列化
	 */
	private static final JdkSerializationRedisSerializer OBJECT_SERIALIZER = new JdkSerializationRedisSerializer();

	/**
	 * Spring Redis Template
	 */
	private final RedisTemplate<String, Object> redisTemplate;

	public RedisRepository(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.redisTemplate.setKeySerializer(STRING_SERIALIZER);
		this.redisTemplate.setValueSerializer(OBJECT_SERIALIZER);
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
	 */
	public void flushDB(RedisClusterNode node) {
		this.redisTemplate.opsForCluster().flushDb(node);
	}

	/**
	 * 添加到带有 过期时间的  缓存
	 *
	 * @param key   redis主键
	 * @param value 值
	 * @param time  过期时间(单位秒)
	 */
	public void setExpire(final byte[] key, final byte[] value, final long time) {
		redisTemplate.execute((RedisCallback<Long>) connection -> {
			connection.setEx(key, time, value);
			log.info("[redisTemplate redis]放入 缓存  url:{} ========缓存时间为{}秒", key, time);
			return 1L;
		});
	}

	/**
	 * 添加到带有 过期时间的  缓存
	 *
	 * @param key   redis主键
	 * @param value 值
	 * @param time  过期时间(单位秒)
	 */
	public void setExpire(final String key, final Object value, final long time) {
		redisTemplate.execute((RedisCallback<Long>) connection -> {
			RedisSerializer<String> serializer = getRedisSerializer();
			byte[] keys = serializer.serialize(key);
			byte[] values = OBJECT_SERIALIZER.serialize(value);
			connection.setEx(keys, time, values);
			return 1L;
		});
	}

	/**
	 * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
	 *
	 * @param keys   redis主键数组
	 * @param values 值数组
	 * @param time   过期时间(单位秒)
	 */
	public void setExpire(final String[] keys, final Object[] values, final long time) {
		redisTemplate.execute((RedisCallback<Long>) connection -> {
			RedisSerializer<String> serializer = getRedisSerializer();
			for (int i = 0; i < keys.length; i++) {
				byte[] bKeys = serializer.serialize(keys[i]);
				byte[] bValues = OBJECT_SERIALIZER.serialize(values[i]);
				connection.setEx(bKeys, time, bValues);
			}
			return 1L;
		});
	}


	/**
	 * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
	 *
	 * @param keys   the keys
	 * @param values the values
	 */
	public void set(final String[] keys, final Object[] values) {
		redisTemplate.execute((RedisCallback<Long>) connection -> {
			RedisSerializer<String> serializer = getRedisSerializer();
			for (int i = 0; i < keys.length; i++) {
				byte[] bKeys = serializer.serialize(keys[i]);
				byte[] bValues = OBJECT_SERIALIZER.serialize(values[i]);
				connection.set(bKeys, bValues);
			}
			return 1L;
		});
	}


	/**
	 * 添加到缓存
	 *
	 * @param key   the key
	 * @param value the value¬
	 */
	public void set(final String key, final Object value) {
		redisTemplate.execute((RedisCallback<Long>) connection -> {
			RedisSerializer<String> serializer = getRedisSerializer();
			byte[] keys = serializer.serialize(key);
			byte[] values = OBJECT_SERIALIZER.serialize(value);
			connection.set(keys, values);
			log.info("[redisTemplate redis]放入 缓存  url:{}", key);
			return 1L;
		});
	}

	/**
	 * 查询在这个时间段内即将过期的key
	 *
	 * @param key  the key
	 * @param time the time
	 * @return the list
	 */
	public List<String> willExpire(final String key, final long time) {
		final List<String> keysList = new ArrayList<>();
		redisTemplate.execute((RedisCallback<List<String>>) connection -> {
			Set<String> keys = redisTemplate.keys(key + "*");
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
	 */
	public byte[] get(final byte[] key) {
		byte[] result = redisTemplate
			.execute((RedisCallback<byte[]>) connection -> connection.get(key));
		log.info("[redisTemplate redis]取出 缓存  url:{} ", key);
		return result;
	}

	/**
	 * 根据key获取对象
	 *
	 * @param key the key
	 * @return the string
	 */
	public Object get(final String key) {
		Object resultStr = redisTemplate.execute((RedisCallback<Object>) connection -> {
			RedisSerializer<String> serializer = getRedisSerializer();
			byte[] keys = serializer.serialize(key);
			byte[] values = connection.get(keys);
			return OBJECT_SERIALIZER.deserialize(values);
		});
		log.info("[redisTemplate redis]取出 缓存  url:{} ", key);
		return resultStr;
	}


	/**
	 * 根据key获取对象
	 *
	 * @param keyPatten the key patten
	 * @return the keys values
	 */
	public Map<String, Object> getKeysValues(final String keyPatten) {
		log.info("[redisTemplate redis]  getValues()  patten={} ", keyPatten);
		return redisTemplate.execute((RedisCallback<Map<String, Object>>) connection -> {
			RedisSerializer<String> serializer = getRedisSerializer();
			Map<String, Object> maps = new HashMap<>(16);
			Set<String> keys = redisTemplate.keys(keyPatten + "*");
			if (CollectionUtils.isNotEmpty(keys)) {
				for (String key : keys) {
					byte[] bKeys = serializer.serialize(key);
					byte[] bValues = connection.get(bKeys);
					Object value = OBJECT_SERIALIZER.deserialize(bValues);
					maps.put(key, value);
				}
			}
			return maps;
		});
	}

	/**
	 * Ops for hash hash operations.
	 *
	 * @return the hash operations
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
	 */
	public void putHashValue(String key, String hashKey, Object hashValue) {
		log.info("[redisTemplate redis]  putHashValue()  key={},hashKey={},hashValue={} ", key,
			hashKey, hashValue);
		opsForHash().put(key, hashKey, hashValue);
	}

	/**
	 * 获取单个field对应的值
	 *
	 * @param key     the key
	 * @param hashKey the hash key
	 * @return the hash values
	 */
	public Object getHashValues(String key, String hashKey) {
		log.info("[redisTemplate redis]  getHashValues()  key={},hashKey={}", key, hashKey);
		return opsForHash().get(key, hashKey);
	}

	/**
	 * 根据key值删除
	 *
	 * @param key      the key
	 * @param hashKeys the hash keys
	 */
	public void delHashValues(String key, Object... hashKeys) {
		log.info("[redisTemplate redis]  delHashValues()  key={}", key);
		opsForHash().delete(key, hashKeys);
	}

	/**
	 * key只匹配map
	 *
	 * @param key the key
	 * @return the hash value
	 */
	public Map<String, Object> getHashValue(String key) {
		log.info("[redisTemplate redis]  getHashValue()  key={}", key);
		return opsForHash().entries(key);
	}

	/**
	 * 批量添加
	 *
	 * @param key the key
	 * @param map the map
	 */
	public void putHashValues(String key, Map<String, Object> map) {
		opsForHash().putAll(key, map);
	}

	/**
	 * 集合数量
	 *
	 * @return the long
	 */
	public long dbSize() {
		return redisTemplate.execute(RedisServerCommands::dbSize);
	}

	/**
	 * 清空redis存储的数据
	 *
	 * @return the string
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
	 */
	protected RedisSerializer<String> getRedisSerializer() {
		return redisTemplate.getStringSerializer();
	}

	/**
	 * 对某个主键对应的值加一,value值必须是全数字的字符串
	 *
	 * @param key the key
	 * @return the long
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
	 */
	public Long leftPush(String key, Object value) {
		return opsForList().leftPush(key, value);
	}

	/**
	 * redis List数据结构 : 移除并返回列表 key 的头元素
	 *
	 * @param key the key
	 * @return the string
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
	 */
	public Long in(String key, Object value) {
		return opsForList().rightPush(key, value);
	}

	/**
	 * redis List数据结构 : 移除并返回列表 key 的末尾元素
	 *
	 * @param key the key
	 * @return the string
	 */
	public Object rightPop(String key) {
		return opsForList().rightPop(key);
	}


	/**
	 * redis List数据结构 : 返回列表 key 的长度 ; 如果 key 不存在，则 key 被解释为一个空列表，返回 0 ; 如果 key 不是列表类型，返回一个错误。
	 *
	 * @param key the key
	 * @return the long
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
	 */
	public Boolean expire(String key, Long time) {
		try {
			if (time > 0) {
				redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
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
	 */
	public void setExpire(final String key, final Object value, final long time,
		final TimeUnit timeUnit) {
		redisTemplate.opsForValue().set(key, value, time, timeUnit);
	}

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
	 */
	public Long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 判断 key是否存在
	 *
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public Boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
			return false;
		}
	}


	/**
	 * 根据key获取对象
	 *
	 * @param key             the key
	 * @param valueSerializer 序列化
	 * @return the string
	 */
	public Object get(final String key, RedisSerializer<Object> valueSerializer) {
		byte[] rawKey = rawKey(key);
		return redisTemplate
			.execute(connection -> deserializeValue(connection.get(rawKey), valueSerializer), true);
	}

//	/**
//	 * 普通缓存放入
//	 *
//	 * @param key   键
//	 * @param value 值
//	 * @return true成功 false失败
//	 */
//	public Boolean set(String key, Object value) {
//		try {
//			redisTemplate.opsForValue().set(key, value);
//			return true;
//		} catch (Exception e) {
//			log.error("Exception: {}", e.getMessage());
//			return false;
//		}
//	}

	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
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
			log.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key   键
	 * @param value 值
	 * @return true成功 false 失败
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
			log.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 递增
	 *
	 * @param key   键
	 * @param delta 要增加几(大于0)
	 * @return Long
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
	 */
	public Object hget(String key, String item) {
		return redisTemplate.opsForHash().get(key, item);
	}

	/**
	 * 获取 hashKey对应的所有键值
	 *
	 * @param key 键
	 * @return 对应的多个键值
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
	 */
	public Boolean hmset(String key, Map<String, Object> map) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
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
	 */
	public Boolean hmset(String key, Map<String, Object> map, Long time) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
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
	 */
	public Boolean hset(String key, String item, Object value) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
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
	 */
	public Boolean hset(String key, String item, Object value, Long time) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 删除hash表中的值
	 *
	 * @param key  键 不能为 null
	 * @param item 项 可以使多个不能为 null
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
	 */
	public Double hdecr(String key, String item, Double by) {
		return redisTemplate.opsForHash().increment(key, item, -by);
	}

	/**
	 * 根据 key获取 Set中的所有值
	 *
	 * @param key 键
	 * @return Set
	 */
	public Set<Object> sGet(String key) {
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 *
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public Boolean sHasKey(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 将数据放入set缓存
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public Long sSet(String key, Object... values) {
		try {
			return redisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
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
	 */
	public Long sSetAndTime(String key, Long time, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().add(key, values);
			if (time > 0) {
				expire(key, time);
			}
			return count;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
			return 0L;
		}
	}

	/**
	 * 获取set缓存的长度
	 *
	 * @param key 键
	 * @return Long
	 */
	public Long sGetSetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
			return 0L;
		}
	}

	/**
	 * 移除值为value的
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public Long setRemove(String key, Object... values) {
		try {
			return redisTemplate.opsForSet().remove(key, values);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
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
	 */
	public List<Object> lGet(String key, Long start, Long end) {
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 *
	 * @param key 键
	 * @return Long
	 */
	public Long lGetListSize(String key) {
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
			return 0L;
		}
	}

	/**
	 * 通过索引 获取list中的值
	 *
	 * @param key   键
	 * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推； index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return Object
	 */
	public Object lGetIndex(String key, Long index) {
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
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
	 */
	public Boolean lSet(String key, Object value, Long time) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return Boolean
	 */
	public Boolean lSet(String key, List<Object> value) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
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
	 */
	public Boolean lSet(String key, List<Object> value, Long time) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
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
	 */
	public Boolean lUpdateIndex(String key, Long index, Object value) {
		try {
			redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
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
	 */
	public Long lRemove(String key, Long count, Object value) {
		try {
			return redisTemplate.opsForList().remove(key, count, value);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage());
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

}
