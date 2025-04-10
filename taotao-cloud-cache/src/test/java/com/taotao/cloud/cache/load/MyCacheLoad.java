package com.taotao.cloud.cache.load;


import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.api.ICacheLoad;

/**
 * @author shuigedeng
 * @since 2024.06
 */
public class MyCacheLoad implements ICacheLoad<String, String> {

	@Override
	public void load(ICache<String, String> cache) {
		cache.put("1", "1");
		cache.put("2", "2");
	}

}
