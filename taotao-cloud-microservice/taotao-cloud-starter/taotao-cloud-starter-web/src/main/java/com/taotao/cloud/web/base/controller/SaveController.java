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
import com.taotao.cloud.log.annotation.RequestOperateLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * SaveController
 *
 * @param <Entity>  Entity
 * @param <SaveDTO> SaveDTO
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:12:22
 */
public interface SaveController<Entity, SaveDTO> extends BaseController<Entity> {

	/**
	 * 新增
	 *
	 * @param saveDTO 保存参数
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 21:12:44
	 */
	@Operation(summary = "新增", description = "新增", method = CommonConstant.POST, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@PutMapping("/all")
	@RequestOperateLog(value = "新增", request = false)
	@PreAuthorize("hasAnyPermission('{}add')")
	default Result<Entity> save(@RequestBody @Validated SaveDTO saveDTO) {
		Result<Entity> result = handlerSave(saveDTO);
		if (result.getData() != null) {
			Entity model = BeanUtil.toBean(saveDTO, getEntityClass());
			getBaseService().save(model);
			result.setData(model);
		}
		return result;
	}

	/**
	 * 自定义新增
	 *
	 * @param model 保存对象
	 * @return {@link com.taotao.cloud.common.model.Result } 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
	 * @author shuigedeng
	 * @since 2021-09-02 21:12:52
	 */
	default Result<Entity> handlerSave(SaveDTO model) {
		return Result.success();
	}

}
