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

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.Serializable;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 删除Controller
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/25 08:22
 */
public interface DeleteController<Entity, Id extends Serializable> extends BaseController<Entity> {

	/**
	 * 删除方法
	 *
	 * @param ids id
	 * @return 是否成功
	 */
	@Operation(summary = "删除", description = "删除", method = CommonConstant.DELETE, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@DeleteMapping
	@RequestOperateLog(description = "删除数据")
	@PreAuthorize("hasAnyPermission('{}delete')")
	default Result<Boolean> delete(@RequestBody List<Id> ids) {
		Result<Boolean> result = handlerDelete(ids);
		if (result.getData()) {
			getBaseService().removeByIds(ids);
		}
		return result;
	}

	/**
	 * 自定义删除
	 *
	 * @param ids id
	 * @return 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
	 */
	default Result<Boolean> handlerDelete(List<Id> ids) {
		return Result.success(true);
	}

}
