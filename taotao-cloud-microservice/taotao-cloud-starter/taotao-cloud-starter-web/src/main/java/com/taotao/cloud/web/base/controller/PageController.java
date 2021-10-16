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
package com.taotao.cloud.web.base.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.data.mybatis.plus.conditions.Wraps;
import com.taotao.cloud.data.mybatis.plus.conditions.query.QueryWrap;
import com.taotao.cloud.log.annotation.RequestOperateLog;
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
public interface PageController<T extends SuperEntity<I>, I extends Serializable, QueryDTO, QueryVO> extends
	BaseController<T, I> {

	/**
	 * 通用分页查询
	 *
	 * @param params 分页参数
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:11:55
	 */
	@Operation(summary = "通用分页查询", description = "通用分页查询", method = CommonConstant.POST)
	@PostMapping("/page")
	@RequestOperateLog(value = "通用分页查询")
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
	 * @param params params
	 * @author shuigedeng
	 * @since 2021-09-02 21:07:14
	 */
	default void handlerQueryParams(PageQuery<QueryDTO> params) {
	}

	/**
	 * 执行分页查询
	 * <p>
	 * 子类可以覆盖后重写查询逻辑
	 *
	 * @param params 分页参数
	 * @return {@link com.baomidou.mybatisplus.core.metadata.IPage }
	 * @author shuigedeng
	 * @since 2021-09-02 21:07:20
	 */
	default IPage<T> pageQuery(PageQuery<QueryDTO> params) {
		handlerQueryParams(params);

		IPage<T> page = params.buildMpPage();
		QueryWrap<T> wrapper = handlerWrapper(params.getQuery());
		IPage<T> data = service().page(page, wrapper);
		handlerResult(data);
		return data;
	}

	/**
	 * 处理对象中的非空参数和扩展字段中的区间参数，可以覆盖后处理组装查询条件
	 *
	 * @param params 分页参数
	 * @return {@link com.taotao.cloud.data.mybatis.plus.conditions.query.QueryWrap }
	 * @author shuigedeng
	 * @since 2021-09-02 21:07:30
	 */
	default QueryWrap<T> handlerWrapper(QueryDTO params) {
		QueryWrap<T> wrapper = Wraps.q(getEntityClass());

		if (params instanceof BaseQuery baseQuery) {
			Optional.ofNullable(baseQuery.getEqQuery())
				.orElse(new ArrayList<>())
				.stream()
				.filter(Objects::nonNull)
				.filter(eqDTO -> StrUtil.isNotBlank(eqDTO.getFiled()))
				.filter(eqDTO -> checkField(eqDTO.getFiled()))
				.forEach(eqDTO -> {
					wrapper.eq(StrUtil.toUnderlineCase(eqDTO.getFiled()), eqDTO.getValue());
				});

			Optional.ofNullable(baseQuery.getDateTimeBetweenQuery())
				.orElse(new ArrayList<>())
				.stream()
				.filter(Objects::nonNull)
				.filter(dateTimeBetweenDTO -> StrUtil.isNotBlank(dateTimeBetweenDTO.getFiled()))
				.filter(dateTimeBetweenDTO -> checkField(dateTimeBetweenDTO.getFiled()))
				.forEach(dateTimeBetweenDTO -> {
					wrapper.between(StrUtil.toUnderlineCase(dateTimeBetweenDTO.getFiled()),
						dateTimeBetweenDTO.getStartTime(),
						dateTimeBetweenDTO.getEndTime());
				});

			Optional.ofNullable(baseQuery.getLikeQuery())
				.orElse(new ArrayList<>())
				.stream()
				.filter(Objects::nonNull)
				.filter(likeDTO -> StrUtil.isNotBlank(likeDTO.getFiled()))
				.filter(likeDTO -> checkField(likeDTO.getFiled()))
				.forEach(likeDTO -> {
					wrapper.like(StrUtil.toUnderlineCase(likeDTO.getFiled()), likeDTO.getValue());
				});

			Optional.ofNullable(baseQuery.getInQuery())
				.orElse(new ArrayList<>())
				.stream()
				.filter(Objects::nonNull)
				.filter(inDTO -> StrUtil.isNotBlank(inDTO.getFiled()))
				.filter(inDTO -> checkField(inDTO.getFiled()))
				.forEach(inDTO -> {
					wrapper.in(StrUtil.toUnderlineCase(inDTO.getFiled()), inDTO.getValues());
				});

			Optional.ofNullable(baseQuery.getNotInQuery())
				.orElse(new ArrayList<>())
				.stream()
				.filter(Objects::nonNull)
				.filter(notInDTO -> StrUtil.isNotBlank(notInDTO.getFiled()))
				.filter(notInDTO -> checkField(notInDTO.getFiled()))
				.forEach(notInDTO -> {
					wrapper.notIn(StrUtil.toUnderlineCase(notInDTO.getFiled()),
						notInDTO.getValues());
				});
		}
		wrapper.isNotNull("id");
		return wrapper;
	}

	/**
	 * 处理查询后的数据
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 21:07:37
	 */
	default void handlerResult(IPage<T> page) {
	}

	Class<QueryVO> getQueryVOClass();
}
