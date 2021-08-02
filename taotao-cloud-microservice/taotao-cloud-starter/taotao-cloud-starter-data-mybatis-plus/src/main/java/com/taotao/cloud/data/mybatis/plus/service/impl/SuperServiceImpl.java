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
package com.taotao.cloud.data.mybatis.plus.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.exception.IdempotencyException;
import com.taotao.cloud.common.exception.LockException;
import com.taotao.cloud.data.mybatis.plus.service.ISuperService;
import com.taotao.cloud.common.lock.DistributedLock;
import com.taotao.cloud.common.lock.ZLock;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * service实现父类
 *
 * @author shuigedeng
 * @since 2020/4/30 10:27
 * @version 1.0.0
 */
public class SuperServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ISuperService<T> {
	private Class<T> entityClass = null;

	@Override
	public Class<T> getEntityClass() {
		if (entityClass == null) {
			this.entityClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		}
		return this.entityClass;
	}

	@Override
	public boolean saveIdempotency(T entity, DistributedLock locker, String lockKey, Wrapper<T> countWrapper, String msg) throws Exception {
		if (locker == null) {
			throw new LockException("DistributedLock is null");
		}
		if (StrUtil.isEmpty(lockKey)) {
			throw new LockException("lockKey is null");
		}
		try (
			ZLock lock = locker.tryLock(lockKey, 10, 60, TimeUnit.SECONDS);
		) {
			if (lock != null) {
				//判断记录是否已存在
				int count = super.count(countWrapper);
				if (count == 0) {
					return super.save(entity);
				} else {
					if (StrUtil.isEmpty(msg)) {
						msg = "已存在";
					}
					throw new IdempotencyException(msg);
				}
			} else {
				throw new LockException("锁等待超时");
			}
		}
	}

	@Override
	public boolean saveIdempotency(T entity, DistributedLock lock, String lockKey, Wrapper<T> countWrapper) throws Exception {
		return saveIdempotency(entity, lock, lockKey, countWrapper, null);
	}

	@Override
	public boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey, Wrapper<T> countWrapper, String msg) throws Exception {
		if (null != entity) {
			Class<?> cls = entity.getClass();
			TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
			if (null != tableInfo && StrUtil.isNotEmpty(tableInfo.getKeyProperty())) {
				Object idVal = ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
				if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
					if (StrUtil.isEmpty(msg)) {
						msg = "已存在";
					}
					return this.saveIdempotency(entity, lock, lockKey, countWrapper, msg);
				} else {
					return updateById(entity);
				}
			} else {
				throw ExceptionUtils.mpe("Error:  Can not execute. Could not find @TableId.");
			}
		}
		return false;
	}

	@Override
	public boolean saveOrUpdateIdempotency(T entity, DistributedLock lock, String lockKey, Wrapper<T> countWrapper) throws Exception {
		return this.saveOrUpdateIdempotency(entity, lock, lockKey, countWrapper, null);
	}
}
