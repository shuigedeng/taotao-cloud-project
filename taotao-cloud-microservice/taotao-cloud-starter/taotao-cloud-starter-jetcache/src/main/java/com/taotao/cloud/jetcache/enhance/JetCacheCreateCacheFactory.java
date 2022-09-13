/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
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

package com.taotao.cloud.jetcache.enhance;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import java.time.Duration;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: JetCache 手动创建Cache 工厂 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/23 10:49
 */
public class JetCacheCreateCacheFactory {

	private final CacheManager cacheManager;

	public JetCacheCreateCacheFactory(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public <K, V> Cache<K, V> create(String name) {
		return create(name, Duration.ofHours(2L));
	}

	public <K, V> Cache<K, V> create(String name, Duration expire) {
		return create(name, expire, true);
	}

	public <K, V> Cache<K, V> create(String name, Duration expire, Boolean cacheNullValue) {
		return create(name, expire, cacheNullValue, null);
	}

	public <K, V> Cache<K, V> create(String name, Duration expire, Boolean cacheNullValue,
		Boolean syncLocal) {
		return create(name, CacheType.BOTH, expire, cacheNullValue, syncLocal);
	}

	public <K, V> Cache<K, V> create(String name, CacheType cacheType) {
		return create(name, cacheType, null);
	}

	public <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire) {
		return create(name, cacheType, expire, true);
	}

	public <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire,
		Boolean cacheNullValue) {
		return create(name, cacheType, expire, cacheNullValue, null);
	}

	public <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire,
		Boolean cacheNullValue, Boolean syncLocal) {
		return create(null, name, cacheType, expire, cacheNullValue, syncLocal);
	}

	public <K, V> Cache<K, V> create(String area, String name, CacheType cacheType, Duration expire,
		Boolean cacheNullValue, Boolean syncLocal) {
		return create(area, name, cacheType, expire, cacheNullValue, syncLocal, null);
	}

	public <K, V> Cache<K, V> create(String area, String name, CacheType cacheType, Duration expire,
		Boolean cacheNullValue, Boolean syncLocal, Duration localExpire) {
		return create(area, name, cacheType, expire, cacheNullValue, syncLocal, localExpire, null);
	}

	public <K, V> Cache<K, V> create(String area, String name, CacheType cacheType, Duration expire,
		Boolean cacheNullValue, Boolean syncLocal, Duration localExpire, Integer localLimit) {
		return create(area, name, cacheType, expire, cacheNullValue, syncLocal, localExpire,
			localLimit, false);
	}

	public <K, V> Cache<K, V> create(String area, String name, CacheType cacheType, Duration expire,
		Boolean cacheNullValue, Boolean syncLocal, Duration localExpire, Integer localLimit,
		Boolean useAreaInPrefix) {
		return create(area, name, cacheType, expire, cacheNullValue, syncLocal, localExpire,
			localLimit, useAreaInPrefix, false, null);
	}

	public <K, V> Cache<K, V> create(String area, String name, CacheType cacheType, Duration expire,
		Boolean cacheNullValue, Boolean syncLocal, Duration localExpire, Integer localLimit,
		Boolean useAreaInPrefix, Boolean penetrationProtect, Duration penetrationProtectTimeout) {
		QuickConfig.Builder builder = StringUtils.isEmpty(area) ? QuickConfig.newBuilder(name)
			: QuickConfig.newBuilder(area, name);
		builder.cacheType(cacheType);
		builder.expire(expire);
		if (cacheType == CacheType.BOTH) {
			builder.syncLocal(syncLocal);
		}
		builder.localExpire(localExpire);
		builder.localLimit(localLimit);
		builder.cacheNullValue(cacheNullValue);
		builder.useAreaInPrefix(useAreaInPrefix);
		if (ObjectUtils.isNotEmpty(penetrationProtect)) {
			builder.penetrationProtect(penetrationProtect);
			if (BooleanUtils.isTrue(penetrationProtect) && ObjectUtils.isNotEmpty(
				penetrationProtectTimeout)) {
				builder.penetrationProtectTimeout(penetrationProtectTimeout);
			}
		}

		QuickConfig quickConfig = builder.build();
		return create(quickConfig);
	}


	@SuppressWarnings("unchecked")
	private <K, V> Cache<K, V> create(QuickConfig quickConfig) {
		return cacheManager.getOrCreateCache(quickConfig);
	}
}
