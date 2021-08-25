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
import com.taotao.cloud.web.base.service.SuperCacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.Serializable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * SuperCacheController
 * <p>
 * 继承该类，在SuperController类的基础上扩展了以下方法： 1，get ： 根据ID查询缓存，若缓存不存在，则查询DB
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/25 08:23
 */
public abstract class SuperCacheController<S extends SuperCacheService<Entity>, Id extends Serializable, Entity, PageQuery, SaveDTO, UpdateDTO>
	extends SuperController<S, Id, Entity, PageQuery, SaveDTO, UpdateDTO> {

	/**
	 * 查询
	 *
	 * @param id 主键id
	 * @return 查询结果
	 */
	@Override
	@Operation(summary = "查询结果", description = "查询结果", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog("'查询:' + #id")
	@PreAuthorize("hasAnyPermission('{}view')")
	public Result<Entity> get(@PathVariable Id id) {
		return success(baseService.getByIdCache(id));
	}


	/**
	 * 刷新缓存
	 *
	 * @return 是否成功
	 */
	@Operation(summary = "刷新缓存", description = "刷新缓存", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@PutMapping("/refreshCache")
	@RequestOperateLog("'刷新缓存'")
	@PreAuthorize("hasAnyPermission('{}add')")
	public Result<Boolean> refreshCache() {
		baseService.refreshCache();
		return success(true);
	}

	/**
	 * 清理缓存
	 *
	 * @return 是否成功
	 */
	@Operation(summary = "清理缓存", description = "清理缓存", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@PutMapping("/clearCache")
	@RequestOperateLog("'清理缓存'")
	@PreAuthorize("hasAnyPermission('{}add')")
	public Result<Boolean> clearCache() {
		baseService.clearCache();
		return success(true);
	}
}
