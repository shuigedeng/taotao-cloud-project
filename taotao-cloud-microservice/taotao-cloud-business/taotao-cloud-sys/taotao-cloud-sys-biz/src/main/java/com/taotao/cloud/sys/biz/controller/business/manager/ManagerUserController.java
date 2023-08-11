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

import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.security.springsecurity.annotation.NotAuth;
import com.taotao.cloud.sys.api.model.dto.user.RestPasswordUserDTO;
import com.taotao.cloud.sys.api.model.dto.user.UpdateUserRolesDTO;
import com.taotao.cloud.sys.api.model.dto.user.UserSaveDTO;
import com.taotao.cloud.sys.api.model.dto.user.UserUpdateDTO;
import com.taotao.cloud.sys.api.model.vo.user.UserQueryVO;
import com.taotao.cloud.sys.biz.model.convert.UserConvert;
import com.taotao.cloud.sys.biz.model.entity.system.User;
import com.taotao.cloud.sys.biz.service.business.IUserService;
import com.taotao.cloud.web.base.controller.BaseSuperController;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 管理端-用户管理API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:13:54
 */
@Validated
@RestController
@RequestMapping("/sys/manager/user")
@Tag(name = "管理端-用户管理API", description = "管理端-用户管理API")
public class ManagerUserController
	extends BaseSuperController<IUserService, User, Long, BaseQuery, UserSaveDTO, UserUpdateDTO, UserQueryVO> {

	@Operation(summary = "根据手机号码查询用户是否存在",
		description = "根据手机号码查询用户是否存在",
		parameters = {
			@Parameter(name = "phone", description = "手机号码", required = true, example = "15730445331", in = ParameterIn.PATH)
		})
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:user:exists:phone')")
	@GetMapping("/exists/phone/{phone}")
	public Result<Boolean> existsByPhone(@NotBlank(message = "手机号码不能为空") @PathVariable(name = "phone") String phone) {
		return success(service().existsByPhone(phone));
	}

	@Operation(summary = "根据用户id查询用户是否存在",
		description = "根据用户id查询用户是否存在",
		parameters = {
			@Parameter(name = "userId", description = "用户id", required = true, example = "123", in = ParameterIn.PATH)
		})
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:user:exists:id')")
	@GetMapping("/exists/id/{userId}")
	public Result<Boolean> existsByUserId(@NotNull(message = "用户id不能为空") @PathVariable(name = "userId") Long userId) {
		return success(service().existsById(userId));
	}

	@Operation(summary = "重置密码",
		description = "后台页面-用户信息页面-重置密码",
		parameters = {
			@Parameter(name = "userId", description = "用户id", required = true, example = "123", in = ParameterIn.PATH)
		},
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "重置密码DTO",
			required = true,
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RestPasswordUserDTO.class))}
		))
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:user:rest:password')")
	@PostMapping("/rest/password/{userId}")
	public Result<Boolean> restPass(@NotNull(message = "用户id不能为空") @PathVariable(name = "userId") Long userId,
									@Validated @RequestBody RestPasswordUserDTO restPasswordDTO) {
		return success(service().restPass(userId, restPasswordDTO));
	}

	@Operation(summary = "获取当前登录人信息", description = "获取当前登录人信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:user:info:current')")
	@GetMapping("/current")
	public Result<UserQueryVO> getCurrentUser() {
		SecurityUser securityUser = SecurityUtils.getCurrentUser();
		if (Objects.isNull(securityUser)) {
			throw new BusinessException("用户未登录");
		}
		Long userId = securityUser.getUserId();
		User sysUser = service().getById(userId);
		return success(UserConvert.INSTANCE.convert(sysUser));
	}

	@Operation(summary = "根据用户id更新角色信息(用户分配角色)",
		description = "后台页面-用户信息页面-根据用户id更新角色信息(用户分配角色)",
		parameters = {
			@Parameter(name = "userId", description = "用户id", required = true, example = "123", in = ParameterIn.PATH)
		},
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "用户更新角色信息DTO",
			required = true,
			content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UpdateUserRolesDTO.class))}
		))
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:user:role')")
	@PutMapping("/roles/{userId}")
	@NotAuth
	public Result<Boolean> updateUserRoles(@NotNull(message = "用户id不能为空") @PathVariable(name = "userId") Long userId,
										   @Validated @RequestBody UpdateUserRolesDTO updateUserRolesDTO) {
		return success(service().updateUserRoles(userId, updateUserRolesDTO.getRoleIds()));
	}

	@PostMapping("/user/test/save")
	@Operation(summary = "test",
		description = "test",
		parameters = {
			@Parameter(name = "userId", description = "用户id", required = true, example = "123")
		},
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "新增DTO",
			required = true,
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserSaveDTO.class))}
		))
	@NotAuth
	public Result<Boolean> testSave(@NotNull(message = "用户id不能为空") @RequestParam(name = "userId") Long userId,
									@Validated @RequestBody UserSaveDTO saveDTO) {
		User user = new User();
		BeanUtils.copy(saveDTO, user);
		user.setAccount("sdfasfd");
		user.setNickname("sdfasfd");
		user.setUsername("sdfasfd");
		user.setPassword("xxx");
		user.setMobile("123455");
		user.setPhone("123455");
		user.setSex(0);
		user.setEmail("sdfasdf");
		user.setBirthday("sdfasdf");
		// user.setDeptId(1L);
		// user.setJobId(2L);
		user.setStatus(1);
		user.setTenantId("sdfasdf");
		service().im().insert(user);
		return Result.success(true);
	}

	@GetMapping("/user/test/get")
	@NotAuth
	public Result<List<User>> testGet() {
		List<User> all = service().ir().findAll();
		return Result.success(all);
	}
}
