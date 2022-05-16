package com.taotao.cloud.elasticsearch.esearchx.model;

import java.util.LinkedHashMap;

/**
 * es地图
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:07:56
 */
public class EsMap extends LinkedHashMap<String, Object> {

	public EsMap set(String key, Object value) {
		put(key, value);
		return this;
	}
}
