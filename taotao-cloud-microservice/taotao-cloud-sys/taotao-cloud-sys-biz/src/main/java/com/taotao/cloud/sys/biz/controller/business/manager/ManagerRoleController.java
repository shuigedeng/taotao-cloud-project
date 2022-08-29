/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.sys.biz.controller.business.manager;

import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.api.dubbo.response.RoleBO;
import com.taotao.cloud.sys.api.model.dto.role.RoleSaveDTO;
import com.taotao.cloud.sys.api.model.dto.role.RoleUpdateDTO;
import com.taotao.cloud.sys.api.model.vo.role.RoleQueryVO;
import com.taotao.cloud.sys.biz.model.entity.system.Role;
import com.taotao.cloud.sys.biz.mapstruct.IRoleMapStruct;
import com.taotao.cloud.sys.biz.service.business.IRoleService;
import com.taotao.cloud.web.base.controller.SuperController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 平台管理端-角色管理API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:09:56
 */
@Validated
@RestController
@RequestMapping("/sys/manager/role")
@Tag(name = "平台管理端-角色管理API", description = "平台管理端-角色管理API")
public class ManagerRoleController extends
	SuperController<IRoleService, Role, Long, BaseQuery, RoleSaveDTO, RoleUpdateDTO, RoleQueryVO> {

	@Operation(summary = "根据用户id获取角色列表", description = "根据用户id获取角色列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:role:info:userId')")
	@GetMapping("/userId/{userId}")
	public Result<List<RoleQueryVO>> findRoleByUserId(
		@Parameter(description = "用户id", required = true) @NotNull(message = "用户id不能为空")
		@PathVariable(name = "userId") Long userId) {
		List<RoleBO> roles = service().findRoleByUserIds(Set.of(userId));
		List<RoleQueryVO> result = IRoleMapStruct.INSTANCE.bosToVos(roles);
		return success(result);
	}

	@Operation(summary = "根据用户id列表获取角色列表", description = "根据用户id列表获取角色列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:role:info:userIds')")
	@GetMapping("/userId")
	public Result<List<RoleQueryVO>> findRoleByUserIds(
		@Parameter(description = "用户id列表", required = true) @NotEmpty(message = "用户id列表不能为空")
		@RequestParam Set<Long> userIds) {
		List<RoleBO> roles = service().findRoleByUserIds(userIds);
		List<RoleQueryVO> result = IRoleMapStruct.INSTANCE.bosToVos(roles);
		return success(result);
	}

	@Operation(summary = "根据角色id更新菜单信息(角色分配菜单)", description = "根据角色id更新菜单信息(角色分配菜单)")
	@RequestLogger
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
