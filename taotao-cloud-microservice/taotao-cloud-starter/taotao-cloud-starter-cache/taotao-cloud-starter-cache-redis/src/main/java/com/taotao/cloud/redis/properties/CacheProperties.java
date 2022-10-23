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
package com.taotao.cloud.redis.properties;

import com.taotao.cloud.redis.enums.CacheType;
import com.taotao.cloud.redis.enums.SerializerType;
import java.time.Duration;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * CustomCacheProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 21:15:21
 */
@RefreshScope
@ConfigurationProperties(prefix = CacheProperties.PREFIX)
public class CacheProperties {

	public static final String PREFIX = "taotao.cloud.redis.cache";

	private boolean enabled = true;

	/**
	 * 目前只支持 REDIS 和 CAFFEINE ！ CAFFEINE 只用于项目的开发环境或者演示环境使用，  生产环境请用redis！！！
	 */
	private CacheType type = CacheType.REDIS;
	/**
	 * 序列化类型
	 */
	private SerializerType serializerType = SerializerType.JACK_SON;
	/**
	 * 是否缓存 null 值
	 */
	private Boolean cacheNullVal = true;

	/**
	 * 通过 @Cacheable 注解标注的方法的缓存策略
	 */
	private Cache def = new Cache();
	/**
	 * 针对某几个具体的key特殊配置
	 * <p>
	 * 改属性只对 redis 有效！！！ configs的key需要配置成@Cacheable注解的value
	 */
	private Map<String, Cache> configs;

	/**
	 * stream
	 */
	private Stream stream = new Stream();

	public static class Stream {

		public static final String PREFIX = CacheProperties.PREFIX + ".stream";
		/**
		 * 是否开启 stream
		 */
		boolean enable = false;
		/**
		 * consumer group，默认：服务名 + 环境
		 */
		String consumerGroup;
		/**
		 * 消费者名称，默认：ip + 端口
		 */
		String consumerName;
		/**
		 * poll 批量大小
		 */
		Integer pollBatchSize;
		/**
		 * poll 超时时间
		 */
		Duration pollTimeout;

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}

		public String getConsumerGroup() {
			return consumerGroup;
		}

		public void setConsumerGroup(String consumerGroup) {
			this.consumerGroup = consumerGroup;
		}

		public String getConsumerName() {
			return consumerName;
		}

		public void setConsumerName(String consumerName) {
			this.consumerName = consumerName;
		}

		public Integer getPollBatchSize() {
			return pollBatchSize;
		}

		public void setPollBatchSize(Integer pollBatchSize) {
			this.pollBatchSize = pollBatchSize;
		}

		public Duration getPollTimeout() {
			return pollTimeout;
		}

		public void setPollTimeout(Duration pollTimeout) {
			this.pollTimeout = pollTimeout;
		}
	}

	public static class Cache {

		/**
		 * key 的过期时间 默认1天过期 eg: timeToLive: 1d
		 */
		private Duration timeToLive = Duration.ofDays(1);

		/**
		 * 是否允许缓存null值
		 */
		private boolean cacheNullValues = true;

		/**
		 * key 的前缀 最后的key格式： keyPrefix + @Cacheable.value + @Cacheable.key
		 * <p>
		 * 使用场景： 开发/测试环境 或者 演示/生产 环境，为了节省资源，往往会共用一个redis，即可以根据key前缀来区分(当然，也能用切换 database 来实现)
		 */
		private String keyPrefix;

		/**
		 * 写入redis时，是否使用key前缀
		 */
		private boolean useKeyPrefix = true;

		/**
		 * Caffeine 的最大缓存个数
		 */
		private int maxSize = 1000;

		public Duration getTimeToLive() {
			return timeToLive;
		}

		public void setTimeToLive(Duration timeToLive) {
			this.timeToLive = timeToLive;
		}

		public boolean isCacheNullValues() {
			return cacheNullValues;
		}

		public void setCacheNullValues(boolean cacheNullValues) {
			this.cacheNullValues = cacheNullValues;
		}

		public String getKeyPrefix() {
			return keyPrefix;
		}

		public void setKeyPrefix(String keyPrefix) {
			this.keyPrefix = keyPrefix;
		}

		public boolean isUseKeyPrefix() {
			return useKeyPrefix;
		}

		public void setUseKeyPrefix(boolean useKeyPrefix) {
			this.useKeyPrefix = useKeyPrefix;
		}

		public int getMaxSize() {
			return maxSize;
		}

		public void setMaxSize(int maxSize) {
			this.maxSize = maxSize;
		}
	}

	public CacheType getType() {
		return type;
	}

	public void setType(CacheType type) {
		this.type = type;
	}

	public SerializerType getSerializerType() {
		return serializerType;
	}

	public void setSerializerType(SerializerType serializerType) {
		this.serializerType = serializerType;
	}

	public Boolean getCacheNullVal() {
		return cacheNullVal;
	}

	public void setCacheNullVal(Boolean cacheNullVal) {
		this.cacheNullVal = cacheNullVal;
	}

	public Cache getDef() {
		return def;
	}

	public void setDef(Cache def) {
		this.def = def;
	}

	public Map<String, Cache> getConfigs() {
		return configs;
	}

	public void setConfigs(
		Map<String, Cache> configs) {
		this.configs = configs;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
	}
}
