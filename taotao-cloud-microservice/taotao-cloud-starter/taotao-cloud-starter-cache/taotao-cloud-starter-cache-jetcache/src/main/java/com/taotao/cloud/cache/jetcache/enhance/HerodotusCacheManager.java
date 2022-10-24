package com.taotao.cloud.cache.jetcache.enhance;

import com.taotao.cloud.common.constant.SymbolConstants;
import com.taotao.cloud.cache.jetcache.properties.Expire;
import com.taotao.cloud.cache.jetcache.properties.JetCacheProperties;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.util.Map;

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
