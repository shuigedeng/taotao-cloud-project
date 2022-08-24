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
package com.taotao.cloud.data.jpa.service;

import com.querydsl.core.types.Predicate;
import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
import com.taotao.cloud.lock.support.DistributedLock;

import java.io.Serializable;

/**
 * service接口父类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:33:18
 */
public interface JpaSuperService<T extends JpaSuperEntity<I>, I extends Serializable> {

	/**
	 * 幂等性新增记录
	 *
	 * @param entity    实体对象
	 * @param lock      锁实例
	 * @param lockKey   锁的key
	 * @param predicate 判断是否存在的条件
	 * @param msg       对象已存在提示信息
	 * @return boolean
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
	 * @since 2021-09-04 07:32:26
	 */
	boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey,
		Predicate predicate);
}
