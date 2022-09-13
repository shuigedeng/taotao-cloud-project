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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	 * @return {@link BaseSuperMapper }<{@link T }, {@link I }>
	 * @since 2022-09-13 09:30:42
	 */
	BaseSuperMapper<T, I> im();

	/**
	 * 获取repository类型
	 *
	 * @return {@link BaseSuperRepository }<{@link T }, {@link I }>
	 * @since 2022-09-13 09:30:42
	 */
	BaseSuperRepository<T, I> cr();

	/**
	 * 获取jpa repository类型
	 *
	 * @return {@link JpaRepository }<{@link T }, {@link I }>
	 * @since 2022-09-13 09:30:42
	 */
	JpaRepository<T, I> ir();

	/**
	 * 刷新缓存
	 *
	 * @since 2022-09-13 09:30:42
	 */
	void refreshCache();

	/**
	 * 清理缓存
	 *
	 * @since 2022-09-13 09:30:42
	 */
	void clearCache();

	/**
	 * 获取缓存数据
	 *
	 * @param id id
	 * @return {@link T }
	 * @since 2022-09-13 09:30:42
	 */
	T getByIdCache(I id);

	/**
	 * 根据 key 查询缓存中存放的id，缓存不存在根据loader加载并写入数据，然后根据查询出来的id查询 实体
	 *
	 * @param key    缓存key
	 * @param loader 加载器
	 * @return {@link T }
	 * @since 2022-09-13 09:30:42
	 */
	T getByKey(CacheKey key, Function<CacheKey, Object> loader);

	/**
	 * 可能会缓存穿透
	 *
	 * @param ids    主键id
	 * @param loader 回调
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-13 09:30:42
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
	 * @return boolean
	 * @since 2022-09-13 09:30:42
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
	 * @return boolean
	 * @since 2022-09-13 09:30:42
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
	 * @return boolean
	 * @since 2022-09-13 09:30:42
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
	 * @return boolean
	 * @since 2022-09-13 09:30:42
	 */
	boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey,
		Predicate predicate, Wrapper<T> countWrapper);


	/**
	 * 获取行名称
	 *
	 * @param function Lambda表达式
	 * @return {@link String }
	 * @since 2022-09-13 09:30:43
	 */
	String getColumnName(SFunction<T, ?> function);

	/**
	 * 根据指定字段值集合进行删除
	 *
	 * @param field       字段
	 * @param fieldValues 字段数据集合
	 * @return boolean
	 * @since 2022-09-13 09:30:43
	 */
	boolean deleteByFields(SFunction<T, ?> field, Collection<?> fieldValues);

	/**
	 * 根据指定字段值进行删除
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @return boolean
	 * @since 2022-09-13 09:30:43
	 */
	boolean deleteByField(SFunction<T, ?> field, Object fieldValue);


	/**
	 * 根据主键集合进行删除
	 *
	 * @param idList id列表
	 * @return boolean
	 * @since 2022-09-13 09:30:43
	 */
	boolean deleteByIds(Collection<? extends Serializable> idList);

	/**
	 * 根据主键进行删除
	 *
	 * @param id id
	 * @return boolean
	 * @since 2022-09-13 09:30:44
	 */
	boolean deleteById(Serializable id);

	/**
	 * 根据指定字段查询存在的数据数量
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @return {@link Long }
	 * @since 2022-09-13 09:30:44
	 */
	Long countByField(SFunction<T, ?> field, Object fieldValue);

	/**
	 * 根据指定字段查询是否存在数据 不包括传入指定ID的对象
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @param id         主键值
	 * @return boolean
	 * @since 2022-09-13 09:30:44
	 */
	boolean existedByField(SFunction<T, ?> field, Object fieldValue, Serializable id);

	/**
	 * 根据指定字段查询是否存在数据
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @return boolean
	 * @since 2022-09-13 09:30:44
	 */
	boolean existedByField(SFunction<T, ?> field, Object fieldValue);

	/**
	 * /** 判断指定id对象是否存在
	 *
	 * @param id id
	 * @return boolean
	 * @since 2022-09-13 09:30:44
	 */
	boolean existedById(Serializable id);

	/**
	 * 根据字段集合查询列表
	 *
	 * @param field       字段
	 * @param fieldValues 字段数据集合
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-13 09:30:44
	 */
	List<T> findAllByFields(SFunction<T, ?> field,
		Collection<? extends Serializable> fieldValues);

	/**
	 * 根据字段查询列表
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-13 09:30:44
	 */
	List<T> findAllByField(SFunction<T, ?> field, Object fieldValue);

	/**
	 * idList 为空不报错
	 *
	 * @param idList is集合
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-13 09:30:44
	 */
	List<T> findAllByIds(Collection<? extends Serializable> idList);

	/**
	 * 根据字段查询唯一值
	 *
	 * @param field      字段
	 * @param fieldValue 字段数据
	 * @return {@link Optional }<{@link T }>
	 * @since 2022-09-13 09:30:44
	 */
	Optional<T> findByField(SFunction<T, ?> field, Object fieldValue);

	/**
	 * 根据主键查询
	 *
	 * @param id id
	 * @return {@link Optional }<{@link T }>
	 * @since 2022-09-13 09:30:44
	 */
	Optional<T> findById(Serializable id);

	/**
	 * 查询全部
	 *
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-13 09:30:44
	 */
	List<T> findAll();

	/**
	 * 根据指定字段进行更新
	 *
	 * @param t          t
	 * @param field      场
	 * @param fieldValue 字段值
	 * @return boolean
	 * @since 2022-09-13 09:30:45
	 */
	boolean updateByField(T t, SFunction<T, ?> field, Object fieldValue);

	/**
	 * 批量更新
	 *
	 * @param entityList 实体列表
	 * @return boolean
	 * @since 2022-09-13 09:30:45
	 */
	boolean updateAllById(Collection<T> entityList);

	/**
	 * 批量保存
	 *
	 * @param list 列表
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-13 09:30:45
	 */
	List<T> saveAll(List<T> list);

	//********************************************

	///**
	// * 获取Repository
	// *
	// * @return {@link BaseRepository}
	// */
	//BaseRepository<E, ID> getRepository();

	/**
	 * 根据ID查询数据
	 *
	 * @param id 数据ID
	 * @return {@link T }
	 * @since 2022-09-13 09:30:46
	 */
	default T jpaFindById(I id) {
		return ir().findById(id).orElse(null);
	}

	/**
	 * 数据是否存在
	 *
	 * @param id 数据ID
	 * @return boolean
	 * @since 2022-09-13 09:30:46
	 */
	default boolean jpaExistsById(I id) {
		return ir().existsById(id);
	}

	/**
	 * 查询数量
	 *
	 * @return long
	 * @since 2022-09-13 09:30:46
	 */
	default long jpaCount() {
		return ir().count();
	}

	/**
	 * 查询数量
	 *
	 * @param example 例子
	 * @return long
	 * @since 2022-09-13 09:30:46
	 */
	default long jpaCount(Example<T> example) {
		return ir().count(example);
	}

	/**
	 * 查询全部数据
	 *
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-13 09:30:46
	 */
	default List<T> jpaFindAll() {
		return ir().findAll();
	}

	/**
	 * 查询全部数据
	 *
	 * @param sort {@link Sort}
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-13 09:30:46
	 */
	default List<T> jpaFindAll(Sort sort) {
		return ir().findAll(sort);
	}

	/**
	 * 查询全部数据
	 *
	 * @param example 例子
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-13 09:30:46
	 */
	default List<T> jpaFindAll(Example<T> example) {
		return ir().findAll(example);
	}

	/**
	 * 查询全部数据
	 *
	 * @param example 例子
	 * @param sort    {@link Sort}
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-13 09:30:46
	 */
	default List<T> jpaFindAll(Example<T> example, Sort sort) {
		return ir().findAll(example, sort);
	}

	/**
	 * 查询分页数据
	 *
	 * @param pageable {@link Pageable}
	 * @return {@link Page }<{@link T }>
	 * @since 2022-09-13 09:30:46
	 */
	default Page<T> jpaFindByPage(Pageable pageable) {
		return ir().findAll(pageable);
	}

	/**
	 * 查询分页数据
	 *
	 * @param pageNumber 当前页码, 起始页码 0
	 * @param pageSize   每页显示的数据条数
	 * @return {@link Page }<{@link T }>
	 * @since 2022-09-13 09:30:46
	 */
	default Page<T> jpaFindByPage(int pageNumber, int pageSize) {
		return jpaFindByPage(PageRequest.of(pageNumber, pageSize));
	}

	/**
	 * 查询分页数据
	 *
	 * @param pageNumber 当前页码, 起始页码 0
	 * @param pageSize   每页显示的数据条数
	 * @param sort       排序
	 * @return {@link Page }<{@link T }>
	 * @since 2022-09-13 09:30:46
	 */
	default Page<T> jpaFindByPage(int pageNumber, int pageSize, Sort sort) {
		return jpaFindByPage(PageRequest.of(pageNumber, pageSize, sort));
	}

	/**
	 * 查询分页数据
	 *
	 * @param pageNumber 当前页码, 起始页码 0
	 * @param pageSize   每页显示的数据条数
	 * @param direction  {@link Sort.Direction}
	 * @param properties 排序的属性名称
	 * @return {@link Page }<{@link T }>
	 * @since 2022-09-13 09:30:46
	 */
	default Page<T> jpaFindByPage(int pageNumber, int pageSize, Sort.Direction direction,
		String... properties) {
		return jpaFindByPage(PageRequest.of(pageNumber, pageSize, direction, properties));
	}

	/**
	 * 查询分页数据
	 *
	 * @param example  例子
	 * @param pageable {@link Pageable}
	 * @return {@link Page }<{@link T }>
	 * @since 2022-09-13 09:30:46
	 */
	default Page<T> jpaFindByPage(Example<T> example, Pageable pageable) {
		return ir().findAll(example, pageable);
	}

	/**
	 * 查询分页数据
	 *
	 * @param example    例子
	 * @param pageNumber 当前页码, 起始页码 0
	 * @param pageSize   每页显示的数据条数
	 * @return {@link Page }<{@link T }>
	 * @since 2022-09-13 09:30:46
	 */
	default Page<T> jpaFindByPage(Example<T> example, int pageNumber, int pageSize) {
		return ir().findAll(example, PageRequest.of(pageNumber, pageSize));
	}

	/**
	 * 查询分页数据
	 *
	 * @param pageNumber 当前页码, 起始页码 0
	 * @param pageSize   每页显示的数据条数
	 * @param direction  {@link Sort.Direction}
	 * @return {@link Page }<{@link T }>
	 * @since 2022-09-13 09:30:46
	 */
	default Page<T> jpaFindByPage(int pageNumber, int pageSize, Sort.Direction direction) {
		return jpaFindByPage(PageRequest.of(pageNumber, pageSize, direction));
	}


	/**
	 * 删除数据
	 *
	 * @param entity 数据对应实体
	 */
	default void jpaDelete(T entity) {
		ir().delete(entity);
	}

	/**
	 * 批量全部删除
	 */
	default void jpaDeleteAllInBatch() {
		ir().deleteAllInBatch();
	}

	/**
	 * 删除指定多个数据
	 *
	 * @param entities 数据对应实体集合
	 */
	default void jpaDeleteAll(Iterable<T> entities) {
		ir().deleteAll(entities);
	}

	/**
	 * 删除全部数据
	 */
	default void jpaDeleteAll() {
		ir().deleteAll();
	}

	/**
	 * 根据ID删除数据
	 *
	 * @param id 数据对应ID
	 */
	default void jpaDeleteById(I id) {
		ir().deleteById(id);
	}

	/**
	 * 保存数据
	 *
	 * @param domain 数据对应实体
	 * @return 已保存数据
	 */
	default T jpaSave(T domain) {
		return ir().save(domain);
	}

	/**
	 * 批量保存
	 *
	 * @param entities 实体集合
	 * @return 已经保存的实体集合
	 */
	default <S extends T> List<S> jpaSaveAll(Iterable<S> entities) {
		return ir().saveAll(entities);
	}

	/**
	 * 保存并且刷新
	 *
	 * @param entity 实体
	 * @return 保存后实体
	 */
	default T jpaSaveAndFlush(T entity) {
		return ir().saveAndFlush(entity);
	}

	/**
	 * 保存或者更新
	 *
	 * @param entity 实体
	 * @return 保存后实体
	 */
	default T jpaSaveOrUpdate(T entity) {
		return jpaSaveAndFlush(entity);
	}

	/**
	 * 刷新实体状态
	 */
	default void jpaFlush() {
		ir().flush();
	}
}
