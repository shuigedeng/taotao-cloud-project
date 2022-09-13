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
 * <p>Description: 自定义 缓存管理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/23 17:02
 */
public class HerodotusCacheManager extends JetCacheSpringCacheManager {

	private static final Logger log = LoggerFactory.getLogger(HerodotusCacheManager.class);

	private final JetCacheProperties cacheProperties;

	public HerodotusCacheManager(JetCacheCreateCacheFactory jetCacheCreateCacheFactory,
		JetCacheProperties cacheProperties) {
		super(jetCacheCreateCacheFactory);
		this.cacheProperties = cacheProperties;
		this.setAllowNullValues(cacheProperties.getAllowNullValues());
	}

	public HerodotusCacheManager(JetCacheCreateCacheFactory jetCacheCreateCacheFactory,
		JetCacheProperties cacheProperties, String... cacheNames) {
		super(jetCacheCreateCacheFactory, cacheNames);
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
				log.debug("[Herodotus] |- CACHE - Cache [{}] is set to use CUSTOM expire.", name);
				return super.createJetCache(name, expire.getTtl());
			}
		}
		return super.createJetCache(name);
	}
}
