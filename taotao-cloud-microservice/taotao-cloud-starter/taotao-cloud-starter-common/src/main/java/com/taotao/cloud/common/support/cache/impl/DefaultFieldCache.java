package com.taotao.cloud.common.support.cache.impl;


import com.taotao.cloud.common.support.cache.ICache;
import com.taotao.cloud.common.support.reflect.api.IField;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单个字段的缓存
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:07:45
 */
public class DefaultFieldCache implements ICache<Class, IField> {

	/**
	 * 存放信息的 map
	 */
	private static final Map<Class, IField> MAP = new ConcurrentHashMap<>();

	@Override
	public IField get(Class key) {
		return MAP.get(key);
	}

	@Override
	public void set(Class key, IField value) {
		MAP.put(key, value);
	}

}
