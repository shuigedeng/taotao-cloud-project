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
package com.taotao.cloud.web.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.querydsl.core.types.Predicate;
import com.taotao.cloud.lock.support.DistributedLock;
import com.taotao.cloud.redis.model.CacheKey;
import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
	List<T> findByIds(@NonNull Collection<? extends Serializable> ids,
		Function<Collection<? extends Serializable>, Collection<T>> loader);


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


	/**
	 * 获取行名称
	 *
	 * @param function Lambda表达式
	 * @return 字段名
	 */
	String getColumnName(SFunction<T, ?> function);

	/**
	 * 根据指定字段值集合进行删除
	 *
	 * @param field       字段
	 * @param fieldValues 字段数据集合
	 * @return boolean
	 */
	boolean deleteByFields(SFunction<T, ?> field, Collection<?> fieldValues);

	/**
	 * 根据指定字段值进行删除
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @return boolean
	 */
	boolean deleteByField(SFunction<T, ?> field, Object fieldValue);


	/**
	 * 根据主键集合进行删除
	 */
	boolean deleteByIds(Collection<? extends Serializable> idList);

	/**
	 * 根据主键进行删除
	 */
	boolean deleteById(Serializable id);

	/**
	 * 根据指定字段查询存在的数据数量
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @return 数量
	 */
	Long countByField(SFunction<T, ?> field, Object fieldValue);

	/**
	 * 根据指定字段查询是否存在数据 不包括传入指定ID的对象
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @param id         主键值
	 * @return 是否存在
	 */
	boolean existedByField(SFunction<T, ?> field, Object fieldValue, Serializable id);

	/**
	 * 根据指定字段查询是否存在数据
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @return 是否存在
	 */
	boolean existedByField(SFunction<T, ?> field, Object fieldValue);

	/**
	 * /** 判断指定id对象是否存在
	 */
	boolean existedById(Serializable id);

	/**
	 * 根据字段集合查询列表
	 *
	 * @param field       字段
	 * @param fieldValues 字段数据集合
	 * @return 对象列表
	 */
	List<T> findAllByFields(SFunction<T, ?> field,
		Collection<? extends Serializable> fieldValues);

	/**
	 * 根据字段查询列表
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @return 对象列表
	 */
	List<T> findAllByField(SFunction<T, ?> field, Object fieldValue);

	/**
	 * idList 为空不报错
	 *
	 * @param idList is集合
	 * @return list
	 */
	List<T> findAllByIds(Collection<? extends Serializable> idList);

	/**
	 * 根据字段查询唯一值
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @return 对象
	 */
	Optional<T> findByField(SFunction<T, ?> field, Object fieldValue);

	/**
	 * 根据主键查询
	 */
	Optional<T> findById(Serializable id);

	/**
	 * 查询全部
	 */
	List<T> findAll();

	/**
	 * 根据指定字段进行更新
	 */
	boolean updateByField(T t, SFunction<T, ?> field, Object fieldValue);

	/**
	 * 批量更新
	 */
	boolean updateAllById(Collection<T> entityList);

	/**
	 * 批量保存
	 */
	List<T> saveAll(List<T> list);
}
