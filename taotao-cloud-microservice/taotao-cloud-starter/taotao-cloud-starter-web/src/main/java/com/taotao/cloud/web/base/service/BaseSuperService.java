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

import com.baomidou.mybatisplus.extension.service.IService;
import com.querydsl.core.types.Predicate;
import com.taotao.cloud.core.lock.DistributedLock;
import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BaseSuperService
 *
 * @author shuigedeng
 * @version v1.0
 * @since 2021/10/10 12:53
 */
public interface BaseSuperService<T extends SuperEntity<T, I>, I extends Serializable> extends
	IService<T> {

	/**
	 * mapper
	 *
	 * @return {@link BaseSuperMapper&lt;T,I&gt; }
	 * @author shuigedeng
	 * @since 2021-10-11 15:50:43
	 */
	BaseSuperMapper<T, I> im();

	/**
	 * repository
	 *
	 * @return {@link com.taotao.cloud.web.base.repository.BaseSuperRepository&lt;T,I&gt; }
	 * @author shuigedeng
	 * @since 2021-10-11 18:54:30
	 */
	BaseSuperRepository<T, I> cr();

	/**
	 * repository
	 *
	 * @return {@link com.taotao.cloud.web.base.repository.BaseSuperRepository&lt;T,I&gt; }
	 * @author shuigedeng
	 * @since 2021-10-11 18:54:30
	 */
	JpaRepository<T, I> ir();

	/**
	 * 刷新缓存
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 21:20:51
	 */
	void refreshCache();

	/**
	 * 清理缓存
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 21:20:55
	 */
	void clearCache();

	/**
	 * getByIdCache
	 *
	 * @param id id
	 * @return {@link T }
	 * @author shuigedeng
	 * @since 2021-10-15 16:51:28
	 */
	T getByIdCache(I id);

	/**
	 * 幂等性新增记录
	 *
	 * @param entity    实体对象
	 * @param lock      锁实例
	 * @param lockKey   锁的key
	 * @param predicate 判断是否存在的条件
	 * @param msg       对象已存在提示信息
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-04 07:32:26
	 */
	boolean saveIdempotency(T entity, DistributedLock lock, String lockKey, Predicate predicate,
		String msg);

	/**
	 * 幂等性新增记录
	 *
	 * @param entity    实体对象
	 * @param lock      锁实例
	 * @param lockKey   锁的key
	 * @param predicate 判断是否存在的条件
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-04 07:32:26
	 */
	boolean saveIdempotency(T entity, DistributedLock lock, String lockKey, Predicate predicate);

	/**
	 * 幂等性新增或更新记录
	 *
	 * @param entity    实体对象
	 * @param lock      锁实例
	 * @param lockKey   锁的key
	 * @param predicate 判断是否存在的条件
	 * @param msg       对象已存在提示信息
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-04 07:32:26
	 */
	boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey,
		Predicate predicate, String msg);

	/**
	 * 幂等性新增或更新记录
	 *
	 * @param entity    实体对象
	 * @param lock      锁实例
	 * @param lockKey   锁的keyø
	 * @param predicate 判断是否存在的条件
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-04 07:32:26
	 */
	boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey,
		Predicate predicate);

}
