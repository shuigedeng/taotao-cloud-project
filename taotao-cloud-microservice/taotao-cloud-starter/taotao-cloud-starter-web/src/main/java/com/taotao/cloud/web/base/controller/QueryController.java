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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.data.mybatis.plus.conditions.Wraps;
import com.taotao.cloud.data.mybatis.plus.conditions.query.QueryWrap;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.web.base.request.PageParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.Serializable;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * QueryController
 *
 * @param <Entity>    Entity
 * @param <PageQuery> PageQuery
 * @param <Id>        Id
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:11:18
 */
public interface QueryController<Entity, Id extends Serializable, PageQuery> extends
		PageController<Entity, PageQuery> {

	/**
	 * 查询
	 *
	 * @param id 主键id
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:11:48
	 */
	@Operation(summary = "单体查询", description = "单体查询", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@GetMapping("/{id}")
	@RequestOperateLog("'查询:' + #id")
	@PreAuthorize("hasAnyPermission('{}view')")
	default Result<Entity> get(@PathVariable Id id) {
		return success(getBaseService().getById(id));
	}

	/**
	 * 分页查询
	 *
	 * @param params 分页参数
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:11:55
	 */
	@Operation(summary = "分页列表查询", description = "分页列表查询", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@PutMapping("/page")
	@RequestOperateLog(value = "'分页列表查询:第' + #params?.current + '页, 显示' + #params?.size + '行'", response = false)
	@PreAuthorize("hasAnyPermission('{}view')")
	default Result<IPage<Entity>> page(@RequestBody @Validated PageParams<PageQuery> params) {
		return success(query(params));
	}

	/**
	 * 批量查询
	 *
	 * @param data 批量查询
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:12:04
	 */
	@Operation(summary = "批量查询", description = "批量查询", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@PutMapping("/query")
	@RequestOperateLog(value = "批量查询")
	@PreAuthorize("hasAnyPermission('{}view')")
	default Result<List<Entity>> query(@RequestBody Entity data) {
		QueryWrap<Entity> wrapper = Wraps.q(data);
		return success(getBaseService().list(wrapper));
	}

}
