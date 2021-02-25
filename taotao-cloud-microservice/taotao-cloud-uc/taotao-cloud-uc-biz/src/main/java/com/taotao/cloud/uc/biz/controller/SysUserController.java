package com.taotao.cloud.uc.biz.controller;

import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.core.model.PageResult;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.core.model.SecurityUser;
import com.taotao.cloud.core.utils.SecurityUtil;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.uc.api.dto.user.RestPasswordUserDTO;
import com.taotao.cloud.uc.api.dto.user.UserDTO;
import com.taotao.cloud.uc.api.dto.user.UserRoleDTO;
import com.taotao.cloud.uc.api.query.user.UserPageQuery;
import com.taotao.cloud.uc.api.query.user.UserQuery;
import com.taotao.cloud.uc.api.vo.user.AddUserVO;
import com.taotao.cloud.uc.api.vo.user.UserVO;
import com.taotao.cloud.uc.biz.entity.SysUser;
import com.taotao.cloud.uc.biz.mapper.UserMapper;
import com.taotao.cloud.uc.biz.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * 后台用户管理API
 *
 * @author dengtao
 * @date 2020/4/30 13:12
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Api(value = "用户管理API", tags = {"用户管理API"})
public class SysUserController {

	private final ISysUserService sysUserService;

	@ApiOperation("保存(添加)用户")
	@RequestOperateLog(description = "保存(添加)用户")
	//@PreAuthorize("hasAuthority('sys:user:add')")
	@PostMapping
	public Result<AddUserVO> saveUser(@Validated @RequestBody UserDTO userDTO) {
		SysUser sysUser = UserMapper.INSTANCE.userDtoToSysUser(userDTO);
		SysUser result = sysUserService.saveUser(sysUser);
		AddUserVO addUserVO = UserMapper.INSTANCE.sysUserToAddUserVO(result);
		return Result.succeed(addUserVO);
	}

	@ApiOperation("更新用户")
	@RequestOperateLog(description = "更新用户")
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PutMapping("/{id:[0-9]*}")
	public Result<UserVO> updateUser(@PathVariable(value = "id") Long id,
									 @Validated @RequestBody UserDTO userDTO) {
		SysUser user = sysUserService.findUserInfoById(id);
		UserMapper.INSTANCE.copyUserDtoToSysUser(userDTO, user);
		SysUser updateUser = sysUserService.updateUser(user);
		UserVO result = UserMapper.INSTANCE.sysUserToUserVO(updateUser);
		return Result.succeed(result);
	}

	@ApiOperation("根据手机号码查询用户是否存在")
	@RequestOperateLog(description = "根据手机号码查询用户是否存在")
	@PreAuthorize("hasAuthority('sys:user:exists:phone')")
	@GetMapping("/exists/phone")
	public Result<Boolean> existsByPhone(@NotBlank(message = "手机号码不能为空")
										 @RequestParam(value = "phone") String phone) {
		Boolean result = sysUserService.existsByPhone(phone);
		return Result.succeed(result);
	}

	@ApiOperation("根据用户id查询用户是否存在")
	@RequestOperateLog(description = "根据用户id查询用户是否存在")
	@PreAuthorize("hasAuthority('sys:user:exists:id')")
	@GetMapping("/exists/id")
	public Result<Boolean> existsByPhone(@NotNull(message = "用户id不能为空")
										 @RequestParam(value = "id") Long id) {
		Boolean result = sysUserService.existsById(id);
		return Result.succeed(result);
	}

	@ApiOperation("根据用户id删除用户")
	@RequestOperateLog(description = "根据用户id删除用户")
	@PreAuthorize("hasAuthority('sys:user:delete')")
	@DeleteMapping("/{id:[0-9]*}")
	public Result<Boolean> deleteUser(@PathVariable(value = "id") Long id) {
		Boolean result = sysUserService.removeUser(id);
		return Result.succeed(result);
	}

	@ApiOperation("分页查询用户集合")
	@RequestOperateLog(description = "分页查询用户集合")
	@PreAuthorize("hasAuthority('sys:user:view:page')")
	@GetMapping(value = "/page")
	public PageResult<UserVO> findUserPage(@Validated UserPageQuery userQuery) {
		Pageable pageable = PageRequest.of(userQuery.getCurrentPage(), userQuery.getPageSize());
		Page<SysUser> page = sysUserService.findUserPage(pageable, userQuery);
		List<UserVO> users = UserMapper.INSTANCE.sysUserToUserVO(page.getContent());
		Page<UserVO> result = new PageImpl<>(users, pageable, page.getTotalElements());
		return PageResult.succeed(result);
	}

	@ApiOperation("重置密码")
	@RequestOperateLog(description = "重置密码")
	@PreAuthorize("hasAuthority('sys:user:rest:password')")
	@PutMapping("/rest/password/{id:[0-9]*}")
	public Result<Boolean> restPass(@PathVariable(value = "id") Long id,
									@Validated @RequestBody RestPasswordUserDTO restPasswordDTO) {
		Boolean result = sysUserService.restPass(id, restPasswordDTO);
		return Result.succeed(result);
	}

	@ApiOperation("获取当前登录人信息")
	@RequestOperateLog(description = "获取当前登录人信息")
	@PreAuthorize("hasAuthority('sys:user:info:current')")
	@GetMapping("/current/info")
	public Result<UserVO> getCurrentUser() {
		SecurityUser securityUser = SecurityUtil.getUser();
		if (Objects.isNull(securityUser)) {
			throw new BusinessException("用户未登录");
		}
		Long userId = securityUser.getUserId();
		SysUser user = sysUserService.findUserInfoById(userId);
		UserVO result = UserMapper.INSTANCE.sysUserToUserVO(user);
		return Result.succeed(result);
	}

	@ApiOperation("根据id获取用户信息")
	@RequestOperateLog(description = "根据id获取用户信息")
	//@PreAuthorize("hasAuthority('sys:user:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<UserVO> findUserInfoById(@PathVariable(value = "id") Long id) {
		SysUser user = sysUserService.findUserInfoById(id);
		UserVO result = UserMapper.INSTANCE.sysUserToUserVO(user);
		return Result.succeed(result);
	}

	@ApiOperation("根据username获取用户信息")
	@RequestOperateLog(description = "根据username获取用户信息")
	//@PreAuthorize("hasAuthority('sys:user:info:username')")
	@GetMapping("/info/username")
	public Result<UserVO> findUserInfoByUsername(@NotBlank(message = "用户名称不能为空")
												 @RequestParam(value = "username") String username) {
		SysUser user = sysUserService.findUserInfoByUsername(username);
		UserVO result = UserMapper.INSTANCE.sysUserToUserVO(user);
		return Result.succeed(result);
	}

	@ApiOperation("查询用户集合")
	@RequestOperateLog(description = "查询用户集合")
	@PreAuthorize("hasAuthority('sys:user:info:list')")
	@GetMapping("/info")
	public Result<List<UserVO>> findUserList(@Validated UserQuery userQuery) {
		List<SysUser> userList = sysUserService.findUserList(userQuery);
		List<UserVO> result = UserMapper.INSTANCE.sysUserToUserVO(userList);
		return Result.succeed(result);
	}

	@ApiOperation("根据用户id更新角色信息(用户分配角色)")
	@RequestOperateLog(description = "根据用户id更新角色信息(用户分配角色)")
	@PreAuthorize("hasAuthority('sys:user:role')")
	@PutMapping("/role")
	public Result<Boolean> updateUserRoles(@Validated @RequestBody UserRoleDTO userRoleDTO) {
		Boolean result = sysUserService.updateUserRoles(userRoleDTO);
		return Result.succeed(result);
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

