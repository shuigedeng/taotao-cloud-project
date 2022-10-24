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
package com.taotao.cloud.cache.redis.model;

import static com.taotao.cloud.common.constant.StrPool.COLON;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.StrPool;
import java.time.Duration;
import java.util.ArrayList;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * CacheKeyBuilder
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 21:15:53
 */
@FunctionalInterface
public interface CacheKeyBuilder {

	/**
	 * 租户编码
	 * <p>
	 * 非租户模式设置成空字符串
	 *
	 * @return 租户编码
	 */
	// todo  return ContextUtil.getTenant();
	@NonNull
	default String getTenant() {
		return "";
	}

	/**
	 * key 前缀
	 *
	 * @return key 前缀
	 */
	@NonNull
	String getPrefix();

	/**
	 * 超时时间
	 *
	 * @return 超时时间
	 */
	@Nullable
	default Duration getExpire() {
		return null;
	}

	/**
	 * 构建通用KV模式 的 cache key 兼容 redis caffeine
	 *
	 * @param suffix 参数
	 * @return cache key
	 */
	default CacheKey key(Object... suffix) {
		String field = suffix.length > 0 ? Convert.toStr(suffix[0], StrPool.EMPTY) : StrPool.EMPTY;
		return hashFieldKey(field, suffix);
	}

	/**
	 * 构建 redis 类型的 hash cache key
	 *
	 * @param field  field
	 * @param suffix 动态参数
	 * @return cache key
	 */
	default CacheHashKey hashFieldKey(@NonNull Object field, Object... suffix) {
		String key = getKey(suffix);

		return new CacheHashKey(key, field, getExpire());
	}

	/**
	 * 构建 redis 类型的 hash cache key （无field)
	 *
	 * @param suffix 动态参数
	 * @return
	 */
	default CacheHashKey hashKey(Object... suffix) {
		String key = getKey(suffix);

		return new CacheHashKey(key, null, getExpire());
	}

	/**
	 * 根据动态参数 拼接参数
	 *
	 * @param suffix 动态参数
	 */
	default String getKey(Object... suffix) {
		ArrayList<String> regionList = new ArrayList<>();
		String tenant = this.getTenant();
		if (StrUtil.isNotEmpty(tenant)) {
			regionList.add(tenant);
		}
		String prefix = this.getPrefix();
		regionList.add(prefix);

		for (Object s : suffix) {
			if (ObjectUtil.isNotEmpty(s)) {
				regionList.add(String.valueOf(s));
			}
		}
		return CollUtil.join(regionList, COLON);
	}
}
