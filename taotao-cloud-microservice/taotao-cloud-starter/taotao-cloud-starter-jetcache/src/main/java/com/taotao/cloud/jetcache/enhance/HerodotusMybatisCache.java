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

import cn.hutool.extra.spring.SpringUtil;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 扩展的Mybatis二级缓存 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/26 17:02
 */
public class HerodotusMybatisCache implements Cache {

	private static final Logger log = LoggerFactory.getLogger(HerodotusMybatisCache.class);

	private final String id;
	private final com.alicp.jetcache.Cache<Object, Object> cache;
	private final AtomicInteger counter = new AtomicInteger(0);

	public HerodotusMybatisCache(String id) {
		this.id = id;
		JetCacheCreateCacheFactory jetCacheCreateCacheFactory = SpringUtil.getBean(
			"jetCacheCreateCacheFactory");
		this.cache = jetCacheCreateCacheFactory.create(this.id);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void putObject(Object key, Object value) {
		cache.put(key, value);
		counter.incrementAndGet();
		log.debug("[Herodotus] |- CACHE - Put data into Mybatis Cache, with key: [{}]", key);
	}

	@Override
	public Object getObject(Object key) {
		Object obj = cache.get(key);
		log.debug("[Herodotus] |- CACHE - Get data from Mybatis Cache, with key: [{}]", key);
		return obj;
	}

	@Override
	public Object removeObject(Object key) {
		Object obj = cache.remove(key);
		counter.decrementAndGet();
		log.debug("[Herodotus] |- CACHE - Remove data from Mybatis Cache, with key: [{}]", key);
		return obj;
	}

	@Override
	public void clear() {
		cache.close();
		log.debug("[Herodotus] |- CACHE - Clear Mybatis Cache.");
	}

	@Override
	public int getSize() {
		return counter.get();
	}
}
