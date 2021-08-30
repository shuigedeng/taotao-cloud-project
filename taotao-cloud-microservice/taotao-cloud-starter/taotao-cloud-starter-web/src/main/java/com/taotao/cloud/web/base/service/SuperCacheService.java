/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.web.base.service;

import com.taotao.cloud.redis.model.CacheKey;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.springframework.lang.NonNull;

/**
 * 基于MP的 IService 新增了3个方法： getByIdCache 其中： 1，getByIdCache 方法 会先从缓存查询，后从DB查询 （取决于实现类） 2、SuperService
 * 上的方法
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/25 08:18
 */
public interface SuperCacheService<T> extends SuperService<T> {

	/**
	 * 根据id 先查缓存，再查db
	 *
	 * @param id 主键
	 * @return 对象
	 */
	T getByIdCache(Serializable id);

	/**
	 * 根据 key 查询缓存中存放的id，缓存不存在根据loader加载并写入数据，然后根据查询出来的id查询 实体
	 *
	 * @param key    缓存key
	 * @param loader 加载器
	 * @return 对象
	 */
	T getByKey(CacheKey key, Function<CacheKey, Object> loader);

	/**
	 * 可能会缓存穿透
	 *
	 * @param ids    主键id
	 * @param loader 回调
	 * @return 对象集合
	 */
	List<T> findByIds(@NonNull Collection<? extends Serializable> ids,
		Function<Collection<? extends Serializable>, Collection<T>> loader);

	/**
	 * 刷新缓存
	 */
	void refreshCache();

	/**
	 * 清理缓存
	 */
	void clearCache();
}
