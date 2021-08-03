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
 * @author zuihou
 * @date 2020年03月06日11:06:46
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
