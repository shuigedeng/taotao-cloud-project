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
package com.taotao.cloud.data.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.data.mybatis.plus.entity.MpSuperEntity;
import com.taotao.cloud.data.mybatis.plus.mapper.MpSuperMapper;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import org.springframework.transaction.annotation.Transactional;

/**
 * 不含缓存的Service实现
 * <p>
 * 2，removeById：重写 ServiceImpl 类的方法，删除db 3，removeByIds：重写 ServiceImpl 类的方法，删除db 4，updateAllById：
 * 新增的方法： 修改数据（所有字段） 5，updateById：重写 ServiceImpl 类的方法，修改db后
 * </p>
 *
 * @param <M> Mapper
 * @param <T> 实体
 * @param <I> 实体主键类型
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:22:34
 */
public class MpSuperServiceImpl<M extends MpSuperMapper<T, I>, T extends MpSuperEntity<I>, I extends Serializable> extends
	ServiceImpl<M, T> implements MpSuperService<T, I> {

	private Class<T> entityClass = null;

	public MpSuperMapper<T, I> getSuperMapper() {
		if (baseMapper != null) {
			return baseMapper;
		}
		throw new BusinessException("未查询到mapper");
	}

	@Override
	public Class<T> getEntityClass() {
		if (entityClass == null) {
			this.entityClass = (Class) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
		}
		return this.entityClass;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(T model) {
		return super.save(model);
	}

	/**
	 * 处理新增相关处理
	 *
	 * @param model 实体
	 * @return {@link Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:22:52
	 */
	protected Result<T> handlerSave(T model) {
		return Result.success(model);
	}

	/**
	 * 处理修改相关处理
	 *
	 * @param model 实体
	 * @return {@link Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:23:00
	 */
	protected Result<T> handlerUpdateAllById(T model) {
		return Result.success(model);
	}

	/**
	 * 处理修改相关处理
	 *
	 * @param model 实体
	 * @return {@link Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:23:06
	 */
	protected Result<T> handlerUpdateById(T model) {
		return Result.success(model);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateAllById(T model) {
		return SqlHelper.retBool(getSuperMapper().updateAllById(model));
	}

	@Override
	public boolean deleteById(I id) {
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(T model) {
		return super.updateById(model);
	}
}
