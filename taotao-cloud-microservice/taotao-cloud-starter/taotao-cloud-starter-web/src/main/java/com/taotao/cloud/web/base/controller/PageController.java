/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.web.base.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.reflect.ReflectionUtil;
import com.taotao.cloud.data.mybatis.plus.conditions.Wraps;
import com.taotao.cloud.data.mybatis.plus.conditions.query.QueryWrap;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.web.base.entity.SuperEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * PageController
 *
 * @param <T>        实体
 * @param <I>        id
 * @param <QueryDTO> 查询参数
 * @param <QueryVO>  查询返回参数
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:06:58
 */
public interface PageController<T extends SuperEntity<T, I>, I extends Serializable, QueryDTO, QueryVO> extends
	BaseController<T, I> {

	/**
	 * 通用分页查询
	 *
	 * @param params 分页参数
	 * @return 分页数据对象
	 * @since 2021-09-02 21:11:55
	 */
	@Operation(summary = "通用分页查询", description = "通用分页查询")
	@PostMapping("/page")
	@RequestLogger("通用分页查询")
	//@PreAuthorize("@permissionVerifier.hasPermission('page')")
	default Result<PageModel<QueryVO>> page(
		@Parameter(description = "分页查询DTO", required = true)
		@RequestBody @Validated PageQuery<QueryDTO> params) {
		IPage<T> page = pageQuery(params);
		PageModel<QueryVO> tPageModel = PageModel.convertMybatisPage(page, getQueryVOClass());
		return Result.success(tPageModel);
	}

	/**
	 * 处理查询参数
	 *
	 * @param params 查询参数
	 * @since 2021-09-02 21:07:14
	 */
	default void handlerQueryParams(PageQuery<QueryDTO> params) {
	}

	/**
	 * 执行分页查询 子类可以覆盖后重写查询逻辑
	 *
	 * @param params 分页参数
	 * @return 分页查询条件
	 * @since 2021-09-02 21:07:20
	 */
	default IPage<T> pageQuery(PageQuery<QueryDTO> params) {
		handlerQueryParams(params);

		IPage<T> page = params.buildMpPage();
		QueryWrap<T> wrapper = handlerWrapper(params.query());
		IPage<T> data = service().page(page, wrapper);
		handlerResult(data);
		return data;
	}

	/**
	 * 处理对象中的非空参数和扩展字段中的区间参数，可以覆盖后处理组装查询条件
	 *
	 * @param params 分页参数
	 * @return 查询条件
	 * @since 2021-09-02 21:07:30
	 */
	default QueryWrap<T> handlerWrapper(QueryDTO params) {
		QueryWrap<T> wrapper = Wraps.q(getEntityClass());

		if (params instanceof BaseQuery baseQuery) {
			Optional.ofNullable(baseQuery.eqQuery())
				.orElse(new ArrayList<>())
				.stream()
				.filter(Objects::nonNull)
				.filter(eqDTO -> StrUtil.isNotBlank(eqDTO.filed()))
				.filter(eqDTO -> ReflectionUtil.checkField(eqDTO.filed(), getEntityClass()))
				.forEach(eqDTO -> {
					wrapper.eq(StrUtil.toUnderlineCase(eqDTO.filed()), eqDTO.value());
				});

			Optional.ofNullable(baseQuery.dateTimeBetweenQuery())
				.orElse(new ArrayList<>())
				.stream()
				.filter(Objects::nonNull)
				.filter(dateTimeBetweenDTO -> StrUtil.isNotBlank(dateTimeBetweenDTO.filed()))
				.filter(
					dateTimeBetweenDTO -> ReflectionUtil.checkField(dateTimeBetweenDTO.getClass(),
						getEntityClass()))
				.forEach(dateTimeBetweenDTO -> {
					wrapper.between(StrUtil.toUnderlineCase(dateTimeBetweenDTO.filed()),
						dateTimeBetweenDTO.startTime(),
						dateTimeBetweenDTO.endTime());
				});

			Optional.ofNullable(baseQuery.likeQuery())
				.orElse(new ArrayList<>())
				.stream()
				.filter(Objects::nonNull)
				.filter(likeDTO -> StrUtil.isNotBlank(likeDTO.filed()))
				.filter(likeDTO -> ReflectionUtil.checkField(likeDTO.getClass(), getEntityClass()))
				.forEach(likeDTO -> {
					wrapper.like(StrUtil.toUnderlineCase(likeDTO.filed()), likeDTO.value());
				});

			Optional.ofNullable(baseQuery.inQuery())
				.orElse(new ArrayList<>())
				.stream()
				.filter(Objects::nonNull)
				.filter(inDTO -> StrUtil.isNotBlank(inDTO.filed()))
				.filter(inDTO -> ReflectionUtil.checkField(inDTO.getClass(), getEntityClass()))
				.forEach(inDTO -> {
					wrapper.in(StrUtil.toUnderlineCase(inDTO.filed()), inDTO.values());
				});

			Optional.ofNullable(baseQuery.notInQuery())
				.orElse(new ArrayList<>())
				.stream()
				.filter(Objects::nonNull)
				.filter(notInDTO -> StrUtil.isNotBlank(notInDTO.filed()))
				.filter(
					notInDTO -> ReflectionUtil.checkField(notInDTO.getClass(), getEntityClass()))
				.forEach(notInDTO -> {
					wrapper.notIn(StrUtil.toUnderlineCase(notInDTO.filed()),
						notInDTO.values());
				});
		}
		wrapper.isNotNull("id");
		return wrapper;
	}

	/**
	 * 处理查询后的数据
	 *
	 * @since 2021-09-02 21:07:37
	 */
	default void handlerResult(IPage<T> page) {
	}

	/**
	 * 获取查询类型
	 *
	 * @return 查询类型
	 * @since 2021-10-20 08:47:25
	 */
	Class<QueryVO> getQueryVOClass();
}
