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

package com.taotao.cloud.jetcache.enhance;

import com.taotao.cloud.common.constant.SymbolConstants;
import com.taotao.cloud.jetcache.properties.Expire;
import com.taotao.cloud.jetcache.properties.JetCacheProperties;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * 自定义 缓存管理器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-25 08:53:34
 */
public class JetcacheCacheManager extends JetCacheSpringCacheManager {

	private static final Logger log = LoggerFactory.getLogger(JetcacheCacheManager.class);

	private final JetCacheCreateCacheFactory jetCacheCreateCacheFactory;
	private final JetCacheProperties cacheProperties;

	public JetcacheCacheManager(JetCacheCreateCacheFactory jetCacheCreateCacheFactory,
		JetCacheProperties cacheProperties) {
		super(jetCacheCreateCacheFactory);
		this.jetCacheCreateCacheFactory = jetCacheCreateCacheFactory;
		this.cacheProperties = cacheProperties;
		this.setAllowNullValues(cacheProperties.getAllowNullValues());
		this.setDesensitization(cacheProperties.getDesensitization());
	}

	public JetcacheCacheManager(JetCacheCreateCacheFactory jetCacheCreateCacheFactory,
		JetCacheProperties cacheProperties, String... cacheNames) {
		super(jetCacheCreateCacheFactory, cacheNames);
		this.jetCacheCreateCacheFactory = jetCacheCreateCacheFactory;
		this.cacheProperties = cacheProperties;
	}

	@Override
	protected Cache createJetCache(String name) {
		Map<String, Expire> expires = cacheProperties.getExpires();
		if (MapUtils.isNotEmpty(expires)) {
			String key = StringUtils.replace(name, SymbolConstants.COLON,
				cacheProperties.getSeparator());
			if (expires.containsKey(key)) {
				Expire expire = expires.get(key);
				log.debug("CACHE - Cache [{}] is setted to use CUSTEM exprie.",
					name);
				com.alicp.jetcache.Cache<Object, Object> cache = jetCacheCreateCacheFactory.create(
					name, expire.getDuration().intValue(), expire.getUnit());
				return new JetCacheSpringCache(name, cache, isAllowNullValues(),
					isDesensitization());
			}
		}
		return super.createJetCache(name);
	}
}
