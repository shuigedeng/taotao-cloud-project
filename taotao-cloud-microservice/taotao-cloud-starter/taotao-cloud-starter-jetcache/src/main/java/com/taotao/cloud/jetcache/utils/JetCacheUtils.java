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

package com.taotao.cloud.jetcache.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.taotao.cloud.jetcache.enhance.JetCacheCreateCacheFactory;
import java.time.Duration;
import org.apache.commons.lang3.ObjectUtils;

/**
 * <p>Description: JetCache 单例工具类 </p>
 */
public class JetCacheUtils {

	private static volatile JetCacheUtils instance;
	private JetCacheCreateCacheFactory jetCacheCreateCacheFactory;

	private JetCacheUtils() {

	}

	private void init(JetCacheCreateCacheFactory jetCacheCreateCacheFactory) {
		this.jetCacheCreateCacheFactory = jetCacheCreateCacheFactory;
	}

	private JetCacheCreateCacheFactory getJetCacheCreateCacheFactory() {
		return jetCacheCreateCacheFactory;
	}

	public static JetCacheUtils getInstance() {
		if (ObjectUtils.isEmpty(instance)) {
			synchronized (JetCacheUtils.class) {
				if (ObjectUtils.isEmpty(instance)) {
					instance = new JetCacheUtils();
				}
			}
		}
		return instance;
	}

	public static void setJetCacheCreateCacheFactory(
		JetCacheCreateCacheFactory jetCacheCreateCacheFactory) {
		getInstance().init(jetCacheCreateCacheFactory);
	}

	public static <K, V> Cache<K, V> create(String name, Duration expire) {
		return create(name, expire, true);
	}

	public static <K, V> Cache<K, V> create(String name, Duration expire, Boolean cacheNullValue) {
		return create(name, expire, cacheNullValue, null);
	}

	public static <K, V> Cache<K, V> create(String name, Duration expire, Boolean cacheNullValue,
		Boolean syncLocal) {
		return create(name, CacheType.BOTH, expire, cacheNullValue, syncLocal);
	}

	public static <K, V> Cache<K, V> create(String name, CacheType cacheType) {
		return create(name, cacheType, null);
	}

	public static <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire) {
		return create(name, cacheType, expire, true);
	}

	public static <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire,
		Boolean cacheNullValue) {
		return create(name, cacheType, expire, cacheNullValue, null);
	}

	public static <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire,
		Boolean cacheNullValue, Boolean syncLocal) {
		return getInstance().getJetCacheCreateCacheFactory()
			.create(name, cacheType, expire, cacheNullValue, syncLocal);
	}
}
