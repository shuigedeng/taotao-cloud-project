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
package com.taotao.cloud.data.jpa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
import com.taotao.cloud.data.jpa.repository.JpaSuperRepository;
import com.taotao.cloud.data.jpa.service.JpaSuperCacheService;
import com.taotao.cloud.redis.model.CacheKey;
import com.taotao.cloud.redis.model.CacheKeyBuilder;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基于 CacheOps 实现的 缓存实现 默认的key规则： #{CacheKeyBuilder#key()}:id
 * <p>
 * 1，getByIdCache：新增的方法： 先查缓存，在查db 2，removeById：重写 ServiceImpl 类的方法，删除db后，淘汰缓存 3，removeByIds：重写
 * ServiceImpl 类的方法，删除db后，淘汰缓存 4，updateAllById： 新增的方法： 修改数据（所有字段）后，淘汰缓存 5，updateById：重写 ServiceImpl
 * 类的方法，修改db后，淘汰缓存
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:21:08
 */
public abstract class JpaSuperCacheServiceImpl<M extends JpaSuperRepository<T, I>, T extends JpaSuperEntity<I>, I
	extends Serializable> implements JpaSuperCacheService<T, I> {

	@Autowired
	protected M jpaRepository;

	@Autowired
	protected RedisRepository redisRepository;

	protected static final int MAX_BATCH_KEY_SIZE = 20;

	protected abstract CacheKeyBuilder cacheKeyBuilder();

	@Override
	@Transactional(readOnly = true)
	public T getByIdCache(I id) {
		CacheKey cacheKey = cacheKeyBuilder().key(id);
		return redisRepository.get(cacheKey, k -> jpaRepository.getById(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findByIds(@NonNull Collection<I> ids, Function<Collection<I>, Collection<T>> loader) {
		if (ids.isEmpty()) {
			return Collections.emptyList();
		}
		// 拼接keys
		List<CacheKey> keys = ids.stream().map(cacheKeyBuilder()::key).collect(Collectors.toList());
		// 切割
		List<List<CacheKey>> partitionKeys = Lists.partition(keys, MAX_BATCH_KEY_SIZE);

		// 用切割后的 partitionKeys 分批去缓存查， 返回的是缓存中存在的数据
		//List<T> valueList = partitionKeys.stream().map(ks -> (List<T>) redisRepository.find(ks))
		//.flatMap(Collection::stream).collect(Collectors.toList());

		List<T> valueList = new ArrayList<>();
		// 所有的key
		List<I> keysList = Lists.newArrayList(ids);
		// 缓存不存在的key
		Set<I> missedKeys = Sets.newLinkedHashSet();

		List<T> allList = new ArrayList<>();
		for (int i = 0; i < valueList.size(); i++) {
			T v = valueList.get(i);
			I k = keysList.get(i);
			if (v == null) {
				missedKeys.add(k);
			} else {
				allList.add(v);
			}
		}
		// 加载miss 的数据，并设置到缓存
		if (CollUtil.isNotEmpty(missedKeys)) {
			//if (loader == null) {
			//	loader = this::listByIds;
			//}
			Collection<T> missList = loader.apply(missedKeys);
			missList.forEach(this::setCache);
			allList.addAll(missList);
		}
		return allList;
	}

	@Override
	@Transactional(readOnly = true)
	public T getByKey(CacheKey key, Function<CacheKey, I> loader) {
		I id = redisRepository.get(key, loader);
		return id == null ? null : getByIdCache(id);
	}


	@Override
	public void refreshCache() {
		jpaRepository.findAll().forEach(this::setCache);
	}

	@Override
	public void clearCache() {
		jpaRepository.findAll().forEach(this::delCache);
	}

	protected void delCache(Serializable... ids) {
		delCache(Arrays.asList(ids));
	}

	protected void delCache(Collection<? extends Serializable> idList) {
		CacheKey[] keys = idList.stream().map(id -> cacheKeyBuilder().key(id))
			.toArray(CacheKey[]::new);
		redisRepository.del(keys);
	}

	protected void delCache(T model) {
		Object id = getId(model);
		if (id != null) {
			CacheKey key = cacheKeyBuilder().key(id);
			redisRepository.del(key);
		}
	}

	protected void setCache(T model) {
		Object id = getId(model);
		if (id != null) {
			CacheKey key = cacheKeyBuilder().key(id);
			redisRepository.set(key, model);
		}
	}

	protected Object getId(T model) {
		if (model instanceof JpaSuperEntity) {
			return ((JpaSuperEntity) model).getId();
		} else {
			// 实体没有继承 Entity 和 SuperEntity
			TableInfo tableInfo = TableInfoHelper.getTableInfo((Class) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1]);
			if (tableInfo == null) {
				return null;
			}
			// 主键类型
			Class<?> keyType = tableInfo.getKeyType();
			if (keyType == null) {
				return null;
			}
			// id 字段名
			String keyProperty = tableInfo.getKeyProperty();

			// 反射得到 主键的值
			Field idField = ReflectUtil.getField((Class) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1], keyProperty);
			return ReflectUtil.getFieldValue(model, idField);
		}
	}

}
