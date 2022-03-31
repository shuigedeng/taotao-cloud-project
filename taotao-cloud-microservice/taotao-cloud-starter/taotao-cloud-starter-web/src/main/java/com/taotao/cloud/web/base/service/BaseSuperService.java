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

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.querydsl.core.types.Predicate;
import com.taotao.cloud.common.support.lock.DistributedLock;
import com.taotao.cloud.redis.model.CacheKey;
import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

/**
 * BaseSuperService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/10 12:53
 */
public interface BaseSuperService<T extends SuperEntity<T, I>, I extends Serializable> extends
	IService<T> {

	/**
	 * 获取mapper类型
	 *
	 * @return mapper类型
	 * @since 2021-10-11 15:50:43
	 */
	BaseSuperMapper<T, I> im();

	/**
	 * 获取repository类型
	 *
	 * @return repository类型
	 * @since 2021-10-11 18:54:30
	 */
	BaseSuperRepository<T, I> cr();

	/**
	 * 获取jpa repository类型
	 *
	 * @return jpa repository类型
	 * @since 2021-10-11 18:54:30
	 */
	JpaRepository<T, I> ir();

	/**
	 * 刷新缓存
	 *
	 * @since 2021-09-02 21:20:51
	 */
	void refreshCache();

	/**
	 * 清理缓存
	 *
	 * @since 2021-09-02 21:20:55
	 */
	void clearCache();

	/**
	 * 获取缓存数据
	 *
	 * @param id id
	 * @return 缓存数据
	 * @since 2021-10-15 16:51:28
	 */
	T getByIdCache(I id);

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
	List<T> findByIds(@NonNull Collection<? extends Serializable> ids, Function<Collection<? extends Serializable>, Collection<T>> loader);


	/**
	 * 幂等性新增记录
	 *
	 * @param entity       实体对象
	 * @param lock         锁实例
	 * @param lockKey      锁的key
	 * @param predicate    判断是否存在的条件 不为空则用jpa判断
	 * @param countWrapper 判断是否存在的条件 不用空则用mybatis判断
	 * @param msg          对象已存在提示信息
	 * @return 新增结果
	 * @since 2021-09-04 07:32:26
	 */
	boolean saveIdempotency(T entity, DistributedLock lock, String lockKey, Predicate predicate,
		Wrapper<T> countWrapper,
		String msg);

	/**
	 * 幂等性新增记录
	 *
	 * @param entity       实体对象
	 * @param lock         锁实例
	 * @param lockKey      锁的key
	 * @param predicate    判断是否存在的条件 不为空则用jpa判断
	 * @param countWrapper 判断是否存在的条件 不用空则用mybatis判断
	 * @return 结果
	 * @since 2021-09-04 07:32:26
	 */
	boolean saveIdempotency(T entity, DistributedLock lock, String lockKey, Predicate predicate,
		Wrapper<T> countWrapper);

	/**
	 * 幂等性新增或更新记录
	 *
	 * @param entity       实体对象
	 * @param lock         锁实例
	 * @param lockKey      锁的key
	 * @param predicate    判断是否存在的条件 不为空则用jpa判断
	 * @param countWrapper 判断是否存在的条件 不用空则用mybatis判断
	 * @param msg          对象已存在提示信息
	 * @return 结果
	 * @since 2021-09-04 07:32:26
	 */
	boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey,
		Predicate predicate, Wrapper<T> countWrapper, String msg);

	/**
	 * 幂等性新增或更新记录
	 *
	 * @param entity       实体对象
	 * @param lock         锁实例
	 * @param lockKey      锁的key
	 * @param predicate    判断是否存在的条件 不为空则用jpa判断
	 * @param countWrapper 判断是否存在的条件 不用空则用mybatis判断
	 * @return 结果
	 * @since 2021-09-04 07:32:26
	 */
	boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey,
		Predicate predicate, Wrapper<T> countWrapper);

}
