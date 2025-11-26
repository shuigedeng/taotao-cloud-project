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

import com.taotao.boot.common.model.request.BaseQuery;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.sys.biz.model.dto.role.RoleSaveDTO;
import com.taotao.cloud.sys.biz.model.dto.role.RoleUpdateDTO;
import com.taotao.cloud.sys.biz.model.vo.role.RoleQueryVO;
import com.taotao.cloud.sys.biz.model.bo.RoleBO;
import com.taotao.cloud.sys.biz.model.convert.RoleConvert;
import com.taotao.cloud.sys.biz.model.entity.system.Role;
import com.taotao.cloud.sys.biz.service.business.IRoleService;
import com.taotao.boot.webagg.controller.BaseSuperController;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 管理端-角色管理API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:09:56
 */
@Validated
@RestController
@RequestMapping("/sys/manager/role")
@Tag(name = "管理端-角色管理API", description = "管理端-角色管理API")
public class ManagerRoleController
        extends BaseSuperController<IRoleService, Role, Long, BaseQuery, RoleSaveDTO, RoleUpdateDTO, RoleQueryVO> {

    @Operation(summary = "根据用户id获取角色列表", description = "根据用户id获取角色列表")
    @Parameters({
            @Parameter(name = "userId", description = "用户id", required = true, example = "123", in = ParameterIn.PATH)
    })
    @RequestLogger
    @PreAuthorize("hasAuthority('sys:role:info:userId')")
    @GetMapping("/userId/{userId}")
    public Result<List<RoleQueryVO>> findRoleByUserId(@NotNull(message = "用户id不能为空") @PathVariable(name = "userId") Long userId) {
        List<RoleBO> roles = service().findRoleByUserIds(Set.of(userId));
        List<RoleQueryVO> result = RoleConvert.INSTANCE.convertListVO(roles);
        return success(result);
    }

    @Operation(summary = "根据用户id列表获取角色列表", description = "后台页面-用户信息页面-根据用户id列表获取角色列表")
    @Parameters({
            @Parameter(name = "userIds", description = "用户id列表", required = true, example = "1,2,3")
    })
    @RequestLogger
    @PreAuthorize("hasAuthority('sys:role:info:userIds')")
    @GetMapping("/userId")
    public Result<List<RoleQueryVO>> findRoleByUserIds(@NotEmpty(message = "用户id列表不能为空") @RequestParam("userIds") Set<Long> userIds) {
        List<RoleBO> roles = service().findRoleByUserIds(userIds);
        List<RoleQueryVO> result = RoleConvert.INSTANCE.convertListVO(roles);
        return success(result);
    }

    @Operation(summary = "根据角色id更新菜单信息(角色分配菜单)", description = "后台页面-用户信息页面-根据角色id更新菜单信息(角色分配菜单)")
    @Parameters({
            @Parameter(name = "roleId", description = "角色id", required = true, example = "1", in = ParameterIn.PATH),
    })
    @RequestLogger
    @PreAuthorize("hasAuthority('sys:role:menu')")
    @PutMapping("/resources/{roleId}")
    public Result<Boolean> saveRoleMenus(@NotNull(message = "角色id不能为空") @PathVariable(name = "roleId") Long roleId,
                                         @Validated @NotEmpty(message = "菜单id列表不能为空") @RequestBody Set<Long> menuIds) {
        return success(service().saveRoleMenus(roleId, menuIds));
    }
}
