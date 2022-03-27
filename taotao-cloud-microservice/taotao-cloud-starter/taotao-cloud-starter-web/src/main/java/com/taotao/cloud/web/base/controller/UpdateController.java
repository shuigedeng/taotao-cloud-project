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

import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.reflect.ReflectionUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.web.base.entity.SuperEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * UpdateController
 *
 * @param <T>         实体
 * @param <I>         id
 * @param <UpdateDTO> 修改参数
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:15:51
 */
public interface UpdateController<T extends SuperEntity<T, I>, I extends Serializable, UpdateDTO> extends
	BaseController<T, I> {

	/**
	 * 通用单体更新
	 *
	 * @param id        id
	 * @param updateDTO 更新对象
	 * @return 更新结果
	 * @since 2021-10-11 17:00:12
	 */
	@Operation(summary = "通用单体更新", description = "通用单体更新")
	@PutMapping("/{id:[0-9]*}")
	@RequestLogger(value = "通用单体更新", request = false)
	//@PreAuthorize("@permissionVerifier.hasPermission('update')")
	default Result<Boolean> update(
		@Parameter(description = "id", required = true) @NotNull(message = "id不能为空")
		@PathVariable(value = "id") I id,
		@Parameter(description = "通用单体更新DTO", required = true)
		@RequestBody @Validated UpdateDTO updateDTO) {
		if (handlerUpdate(updateDTO)) {
			T t = service().getById(id);
			if (Objects.isNull(t)) {
				throw new BusinessException("未查询到数据");
			}

			if (ReflectionUtil.checkField(updateDTO.getClass(), getEntityClass())) {
				return success(
					service().updateById(ReflectionUtil.copyPropertiesIfRecord(t, updateDTO)));
			}
		}
		throw new BusinessException("通用单体更新失败");
	}

	/**
	 * 自定义更新
	 *
	 * @param model 更新对象
	 * @return 更新结果
	 * @since 2021-09-02 21:16:25
	 */
	default Boolean handlerUpdate(UpdateDTO model) {
		if (Objects.isNull(model)) {
			throw new BusinessException("更新DTO不能为空");
		}
		return true;
	}
}
