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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * AbstractBaseSuperService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/10 12:53
 */
public abstract class AbstractBaseSuperService<M extends BaseSuperMapper<T, I>, T extends SuperEntity<T, I>, I extends Serializable>
	extends ServiceImpl<M, T> implements BaseSuperService<T, I> {

	/**
	 * 发现通过id列
	 *
	 * @param id      id
	 * @param columns 列
	 * @return {@link Optional }<{@link T }>
	 * @since 2022-09-22 10:00:54
	 */
	@SafeVarargs
	public final Optional<T> findByIdWithColumns(Serializable id, SFunction<T, ?>... columns) {
		LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(columns);
		queryWrapper.eq(SuperEntity::getId, id);
		return Optional.ofNullable(im().selectOne(queryWrapper));
	}

	/**
	 * 发现通过id列
	 *
	 * @param ids     id
	 * @param columns 列
	 * @return {@link List }<{@link T }>
	 * @since 2022-09-22 10:00:57
	 */
	@SafeVarargs
	public final List<T> findByIdsWithColumns(List<Serializable> ids, SFunction<T, ?>... columns) {
		LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(columns);
		queryWrapper.in(SuperEntity::getId, ids);
		return im().selectList(queryWrapper);
	}

}
