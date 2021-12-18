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

import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.SecurityUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.api.dto.user.RestPasswordUserDTO;
import com.taotao.cloud.sys.api.dto.user.UserSaveDTO;
import com.taotao.cloud.sys.api.dto.user.UserUpdateDTO;
import com.taotao.cloud.sys.biz.entity.User;
import com.taotao.cloud.sys.api.vo.user.UserQueryVO;
import com.taotao.cloud.sys.biz.mapstruct.IUserMapStruct;
import com.taotao.cloud.sys.biz.service.IUserService;
import com.taotao.cloud.web.base.controller.SuperController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台用户管理API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:13:54
 */
@Validated
@RestController
@RequestMapping("/manager/user")
@Tag(name = "后台用户管理API", description = "后台用户管理API")
public class ManagerUserController extends
	SuperController<IUserService, User, Long, BaseQuery, UserSaveDTO, UserUpdateDTO, UserQueryVO> {


	/**
	 * 根据手机号码查询用户是否存在
	 *
	 * @param phone 手机号码
	 * @return {@link Result&lt;java.lang.Boolean&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 15:14:58
	 */
	@Operation(summary = "根据手机号码查询用户是否存在", description = "根据手机号码查询用户是否存在")
	@RequestLogger(description = "根据手机号码查询用户是否存在")
	@PreAuthorize("hasAuthority('sys:user:exists:phone')")
	@GetMapping("/exists/phone/{phone}")
	public Result<Boolean> existsByPhone(
		@Parameter(description = "手机号码", required = true) @NotBlank(message = "手机号码不能为空")
		@PathVariable(name = "phone") String phone) {
		return success(service().existsByPhone(phone));
	}

	/**
	 * 根据用户id查询用户是否存在
	 *
	 * @param userId 用户id
	 * @return {@link Result&lt;java.lang.Boolean&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 15:15:05
	 */
	@Operation(summary = "根据用户id查询用户是否存在", description = "根据用户id查询用户是否存在")
	@RequestLogger(description = "根据用户id查询用户是否存在")
	@PreAuthorize("hasAuthority('sys:user:exists:id')")
	@GetMapping("/exists/id/{userId}")
	public Result<Boolean> existsByUserId(
		@Parameter(description = "用户id", required = true) @NotNull(message = "用户id不能为空")
		@PathVariable(name = "userId") Long userId) {
		return success(service().existsById(userId));
	}

	/**
	 * 重置密码
	 *
	 * @param userId          用户id
	 * @param restPasswordDTO 重置密码DTO
	 * @return {@link Result&lt;java.lang.Boolean&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 15:15:57
	 */
	@Operation(summary = "重置密码", description = "重置密码")
	@RequestLogger(description = "重置密码")
	@PreAuthorize("hasAuthority('sys:user:rest:password')")
	@PostMapping("/rest/password/{userId}")
	public Result<Boolean> restPass(
		@Parameter(description = "用户id", required = true) @NotNull(message = "用户id不能为空")
		@PathVariable(name = "userId") Long userId,
		@Parameter(description = "重置密码DTO", required = true)
		@Validated @RequestBody RestPasswordUserDTO restPasswordDTO) {
		return success(service().restPass(userId, restPasswordDTO));
	}

	/**
	 * 获取当前登录人信息
	 *
	 * @return {@link Result&lt;com.taotao.cloud.sys.api.vo.user.UserVO&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 15:16:08
	 */
	@Operation(summary = "获取当前登录人信息", description = "获取当前登录人信息")
	@RequestLogger(description = "获取当前登录人信息")
	@PreAuthorize("hasAuthority('sys:user:info:current')")
	@GetMapping("/current")
	public Result<UserQueryVO> getCurrentUser() {
		SecurityUser securityUser = SecurityUtil.getUser();
		if (Objects.isNull(securityUser)) {
			throw new BusinessException("用户未登录");
		}
		Long userId = securityUser.getUserId();
		User sysUser = service().getById(userId);
		return success(IUserMapStruct.INSTANCE.sysUserToUserQueryVO(sysUser));
	}


	/**
	 * 根据用户id更新角色信息(用户分配角色)
	 *
	 * @param roleIds 角色id列表
	 * @return {@link Result&lt;java.lang.Boolean&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 16:41:06
	 */
	@Operation(summary = "根据用户id更新角色信息(用户分配角色)", description = "根据用户id更新角色信息(用户分配角色)")
	@RequestLogger(description = "根据用户id更新角色信息(用户分配角色)")
	@PreAuthorize("hasAuthority('sys:user:role')")
	@PutMapping("/roles/{userId}")
	public Result<Boolean> updateUserRoles(
		@Parameter(description = "用户id", required = true) @NotNull(message = "用户id不能为空")
		@PathVariable(name = "userId") Long userId,
		@Parameter(description = "角色id列表", required = true) @NotEmpty(message = "角色id列表不能为空")
		@RequestBody Set<Long> roleIds) {
		return success(service().updateUserRoles(userId, roleIds));
	}
	// **********************内部微服务接口*****************************

	// @ApiIgnore
	// @ApiOperation("第三方登录调用获取用户信息")
	// @SysOperateLog(description = "第三方登录调用获取用户信息")
	// @GetMapping("/info/social")
	// public Result<SecurityUser> getUserInfoBySocial(@RequestParam(value = "providerId") String providerId,
	//                                                 @RequestParam(value = "providerUserId") int providerUserId) {
	//     SysUser sysUser = sysUserService.getUserBySocial(providerId, providerUserId);
	//     SecurityUser securityUser = new SecurityUser(sysUser.getId(), sysUser.getUsername(),
	//             sysUser.getPassword(), CollectionUtil.newHashSet(), CollectionUtil.newHashSet());
	//     BeanUtil.copyIncludeNull(sysUser, securityUser);
	//     return Result.succeed(securityUser);
	// }

}

