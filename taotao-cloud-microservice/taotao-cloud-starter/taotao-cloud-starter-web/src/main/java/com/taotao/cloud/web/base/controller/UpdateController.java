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

import cn.hutool.core.bean.BeanUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.data.mybatis.plus.entity.SuperEntity;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * UpdateController
 *
 * @param <Entity>    实体
 * @param <UpdateDTO> 修改参数
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:15:51
 */
public interface UpdateController<Entity, UpdateDTO> extends BaseController<Entity> {

	/**
	 * 修改
	 *
	 * @param updateDTO 修改DTO
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:16:08
	 */
	@Operation(summary = "修改", description = "修改UpdateDTO中不为空的字段", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@PutMapping
	@RequestOperateLog(value = "'修改:' + #updateDTO?.id", request = false)
	@PreAuthorize("hasAnyPermission('{}edit')")
	default Result<Entity> update(
			@RequestBody @Validated(SuperEntity.Update.class) UpdateDTO updateDTO) {
		Result<Entity> result = handlerUpdate(updateDTO);
		if (result.getData() != null) {
			Entity model = BeanUtil.toBean(updateDTO, getEntityClass());
			getBaseService().updateById(model);
			result.setData(model);
		}
		return result;
	}

	/**
	 * 修改所有字段
	 *
	 * @param entity entity
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:16:16
	 */
	@Operation(summary = "修改所有字段", description = "修改所有字段，没有传递的字段会被置空", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@PutMapping("/all")
	@RequestOperateLog(value = "'修改所有字段:' + #entity?.id", request = false)
	@PreAuthorize("hasAnyPermission('{}edit')")
	default Result<Entity> updateAll(
			@RequestBody @Validated(SuperEntity.Update.class) Entity entity) {
		getBaseService().updateAllById(entity);
		return Result.success(entity);
	}

	/**
	 * 自定义更新
	 *
	 * @param model 修改DTO
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:16:25
	 */
	default Result<Entity> handlerUpdate(UpdateDTO model) {
		return Result.success();
	}
}
