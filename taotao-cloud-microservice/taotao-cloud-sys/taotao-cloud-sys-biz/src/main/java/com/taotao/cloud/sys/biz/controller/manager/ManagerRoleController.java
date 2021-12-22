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
package com.taotao.cloud.sys.biz.controller.manager;

import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.netty.annotation.PathVariable;
import com.taotao.cloud.sys.api.bo.role.RoleBO;
import com.taotao.cloud.sys.api.dto.role.RoleSaveDTO;
import com.taotao.cloud.sys.api.dto.role.RoleUpdateDTO;
import com.taotao.cloud.sys.biz.entity.Role;
import com.taotao.cloud.sys.api.vo.role.RoleQueryVO;
import com.taotao.cloud.sys.biz.mapstruct.IRoleMapStruct;
import com.taotao.cloud.sys.biz.service.IRoleService;
import com.taotao.cloud.web.base.controller.SuperController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台管理端-角色管理API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:09:56
 */
@Validated
@RestController
@RequestMapping("/manager/role")
@Tag(name = "平台管理端-角色管理API", description = "平台管理端-角色管理API")
public class ManagerRoleController extends
	SuperController<IRoleService, Role, Long, BaseQuery, RoleSaveDTO, RoleUpdateDTO, RoleQueryVO> {

	/**
	 * 根据用户id获取角色列表
	 *
	 * @param userId 用户id
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.sys.api.vo.role.RoleVO&gt;&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 15:12:23
	 */
	@Operation(summary = "根据用户id获取角色列表", description = "根据用户id获取角色列表")
	@RequestLogger(description = "根据用户id获取角色列表")
	@PreAuthorize("hasAuthority('sys:role:info:userId')")
	@GetMapping("/userId/{userId}")
	public Result<List<RoleQueryVO>> findRoleByUserId(
		@Parameter(description = "用户id", required = true) @NotNull(message = "用户id不能为空")
		@PathVariable(name = "userId") Long userId) {
		List<RoleBO> roles = service().findRoleByUserIds(Set.of(userId));
		List<RoleQueryVO> result = IRoleMapStruct.INSTANCE.bosToVos(roles);
		return success(result);
	}

	/**
	 * 根据用户id列表获取角色列表
	 *
	 * @param userIds 用户id列表
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.sys.api.vo.role.RoleVO&gt;&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 15:12:32
	 */
	@Operation(summary = "根据用户id列表获取角色列表", description = "根据用户id列表获取角色列表")
	@RequestLogger(description = "根据用户id列表获取角色列表")
	@PreAuthorize("hasAuthority('sys:role:info:userIds')")
	@GetMapping("/userId")
	public Result<List<RoleQueryVO>> findRoleByUserIds(
		@Parameter(description = "用户id列表", required = true) @NotEmpty(message = "用户id列表不能为空")
		@RequestParam Set<Long> userIds) {
		List<RoleBO> roles = service().findRoleByUserIds(userIds);
		List<RoleQueryVO> result = IRoleMapStruct.INSTANCE.bosToVos(roles);
		return success(result);
	}

	/**
	 * 根据角色id更新菜单信息(角色分配菜单)
	 *
	 * @param roleId      角色id
	 * @param menuIds 菜单id列表
	 * @return {@link Result&lt;java.lang.Boolean&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 15:12:46
	 */
	@Operation(summary = "根据角色id更新菜单信息(角色分配菜单)", description = "根据角色id更新菜单信息(角色分配菜单)")
	@RequestLogger(description = "根据角色id更新菜单信息(角色分配菜单)")
	@PreAuthorize("hasAuthority('sys:role:menu')")
	@PutMapping("/resources/{roleId}")
	public Result<Boolean> saveRoleMenus(
		@Parameter(description = "角色id", required = true) @NotNull(message = "角色id不能为空")
		@PathVariable(name = "roleId") Long roleId,
		@Parameter(description = "菜单id列表", required = true) @NotEmpty(message = "菜单id列表不能为空")
		@RequestBody Set<Long> menuIds) {
		return success(service().saveRoleMenus(roleId, menuIds));
	}
}
