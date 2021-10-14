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
package com.taotao.cloud.uc.biz.controller;

import com.taotao.cloud.uc.api.dto.role.RoleQueryDTO;
import com.taotao.cloud.uc.api.dto.role.RoleSaveDTO;
import com.taotao.cloud.uc.api.dto.role.RoleUpdateDTO;
import com.taotao.cloud.uc.api.service.ISysRoleService;
import com.taotao.cloud.uc.api.vo.role.RoleQueryVO;
import com.taotao.cloud.uc.biz.entity.SysRole;
import com.taotao.cloud.web.base.controller.SuperController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理API
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:09:56
 */
@Validated
@RestController
@RequestMapping("/uc/role")
@Tag(name = "角色管理API", description = "角色管理API")
public class SysRoleController extends
	SuperController<ISysRoleService<SysRole, Long>, SysRole, Long, RoleQueryDTO, RoleSaveDTO, RoleUpdateDTO, RoleQueryVO> {


	//private final ISysRoleService sysRoleService;
	//
	//public SysRoleController(ISysRoleService sysRoleService) {
	//	this.sysRoleService = sysRoleService;
	//}
	//
	///**
	// * 根据角色id获取角色信息
	// *
	// * @param id 角色id
	// * @return {@link Result&lt;com.taotao.cloud.uc.api.vo.role.RoleVO&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:10:01
	// */
	//@Operation(summary = "根据角色id获取角色信息", description = "根据角色id获取角色信息",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据角色id获取角色信息")
	//@PreAuthorize("hasAuthority('sys:role:info:id')")
	//@GetMapping("/info/id/{id:[0-9]*}")
	//public Result<RoleVO> findRoleById(
	//	@Parameter(name = "id", description = "角色id", required = true, in = ParameterIn.QUERY)
	//	@NotNull(message = "角色id不能为空")
	//	@PathVariable(value = "id") Long id) {
	//	SysRole role = sysRoleService.findRoleById(id);
	//	RoleVO vo = SysRoleUtil.copy(role);
	//	return Result.success(vo);
	//}
	//
	///**
	// * 根据code查询角色是否存在
	// *
	// * @param code 角色code
	// * @return {@link Result&lt;java.lang.Boolean&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 15:10:08
	// */
	//@Operation(summary = "根据code查询角色是否存在", description = "根据code查询角色是否存在", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据code查询角色是否存在")
	//@PreAuthorize("hasAuthority('sys:role:exist:code')")
	//@GetMapping("/exist/code/{code}")
	//public Result<Boolean> existRoleByCode(
	//	@Parameter(name = "code", description = "角色code", required = true, in = ParameterIn.QUERY)
	//	@NotBlank(message = "角色code不能为空")
	//	@PathVariable(value = "code") String code) {
	//	Boolean result = sysRoleService.existRoleByCode(code);
	//	return Result.success(result);
	//}
	//
	///**
	// * 根据code获取角色信息
	// *
	// * @param code 角色code
	// * @return {@link Result&lt;com.taotao.cloud.uc.api.vo.role.RoleVO&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:10:22
	// */
	//@Operation(summary = "根据code获取角色信息", description = "根据code获取角色信息", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据code获取角色信息")
	//@PreAuthorize("hasAuthority('sys:role:info:code')")
	//@GetMapping("/info/code")
	//public Result<RoleVO> findRoleByCode(
	//	@Parameter(name = "code", description = "角色code", required = true, in = ParameterIn.QUERY)
	//	@RequestParam(value = "code") String code) {
	//	SysRole role = sysRoleService.findRoleByCode(code);
	//	RoleVO vo = SysRoleUtil.copy(role);
	//	return Result.success(vo);
	//}
	//
	///**
	// * 根据code列表获取角色信息
	// *
	// * @param codes code列表
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.role.RoleVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:10:32
	// */
	//@Operation(summary = "根据code列表获取角色信息", description = "根据code列表获取角色信息", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据code列表获取角色信息")
	//@PreAuthorize("hasAuthority('sys:role:info:code')")
	//@GetMapping("/info/codes")
	//public Result<List<RoleVO>> findRoleByCodes(
	//	@Parameter(name = "codes", description = "code列表", required = true, schema = @Schema(implementation = Set.class), in = ParameterIn.QUERY)
	//	@NotNull(message = "code列表不能为空")
	//	@RequestParam(value = "codes") Set<String> codes) {
	//	List<SysRole> roles = sysRoleService.findRoleByCodes(codes);
	//	List<RoleVO> collect = roles.stream().filter(Objects::nonNull)
	//		.map(SysRoleUtil::copy).collect(Collectors.toList());
	//	return Result.success(collect);
	//}
	//
	///**
	// * 添加角色
	// *
	// * @param roleDTO 添加角色对象DTO
	// * @return {@link Result&lt;java.lang.Boolean&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 15:10:40
	// */
	//@Operation(summary = "添加角色", description = "添加角色", method = CommonConstant.POST,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "添加角色")
	////@PreAuthorize("hasAuthority('sys:role:save')")
	//@PostMapping
	//public Result<Boolean> saveRole(
	//	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "添加角色对象DTO", required = true)
	//	@Valid @RequestBody RoleDTO roleDTO) {
	//	Boolean result = sysRoleService.saveRole(roleDTO);
	//	return Result.success(result);
	//}
	//
	///**
	// * 修改角色
	// *
	// * @param id      角色id
	// * @param roleDTO 修改角色对象DTO
	// * @return {@link Result&lt;java.lang.Boolean&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:10:57
	// */
	//@Operation(summary = "修改角色", description = "修改角色", method = CommonConstant.PUT,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "修改角色")
	//@PreAuthorize("hasAuthority('sys:role:update')")
	//@PostMapping("/{id:[0-9]*}")
	//public Result<Boolean> updateRole(
	//	@Parameter(name = "id", description = "角色id", required = true, in = ParameterIn.PATH)
	//	@PathVariable(value = "id") Long id,
	//	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "修改角色对象DTO", required = true)
	//	@Validated @RequestBody RoleDTO roleDTO) {
	//	Boolean result = sysRoleService.updateRole(id, roleDTO);
	//	return Result.success(result);
	//}
	//
	///**
	// * 根据id删除角色
	// *
	// * @param id 角色id
	// * @return {@link Result&lt;java.lang.Boolean&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:11:26
	// */
	//@Operation(summary = "根据id删除角色", description = "根据id删除角色", method = CommonConstant.DELETE,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据id删除角色")
	//@PreAuthorize("hasAuthority('sys:role:delete')")
	//@DeleteMapping("/{id:[0-9]*}")
	//public Result<Boolean> deleteRole(
	//	@Parameter(name = "id", description = "角色id", required = true, in = ParameterIn.PATH)
	//	@PathVariable(value = "id") Long id) {
	//	Boolean result = sysRoleService.deleteRole(id);
	//	return Result.success(result);
	//}
	//
	///**
	// * 分页查询角色集合
	// *
	// * @param roleQuery 分页查询角色集合对象QUERY
	// * @return {@link Result&lt;PageModel&lt;com.taotao.cloud.uc.api.vo.role.RoleVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:11:39
	// */
	//@Operation(summary = "分页查询角色集合", description = "分页查询角色集合", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "分页查询角色集合")
	//@PreAuthorize("hasAuthority('sys:role:view:page')")
	//@GetMapping(value = "/page")
	//public Result<PageModel<RoleVO>> findRolePage(
	//	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "分页查询角色集合对象QUERY", required = true)
	//	@Validated @NotNull RoleQuery roleQuery) {
	//	Pageable pageable = PageRequest.of(roleQuery.getCurrentPage(), roleQuery.getPageSize());
	//	Page<SysRole> page = sysRoleService.findRolePage(pageable, roleQuery);
	//	List<RoleVO> collect = page.stream().filter(Objects::nonNull)
	//		.map(SysRoleUtil::copy).collect(Collectors.toList());
	//	Page<RoleVO> result = new PageImpl<>(collect, pageable, page.getTotalElements());
	//	return Result.success(PageModel.convertJpaPage(result));
	//}
	//
	///**
	// * 查询所有角色列表
	// *
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.role.RoleVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:12:18
	// */
	//@Operation(summary = "查询所有角色列表", description = "查询所有角色列表", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "查询所有角色列表")
	//@PreAuthorize("hasAuthority('sys:role:list')")
	//@GetMapping
	//public Result<List<RoleVO>> findAllRoles() {
	//	List<SysRole> pages = sysRoleService.findAllRoles();
	//	List<RoleVO> collect = pages.stream().filter(Objects::nonNull)
	//		.map(SysRoleUtil::copy).collect(Collectors.toList());
	//	return Result.success(collect);
	//}
	//
	///**
	// * 根据用户id获取角色列表
	// *
	// * @param userId 用户id
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.role.RoleVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:12:23
	// */
	//@Operation(summary = "根据用户id获取角色列表", description = "根据用户id获取角色列表", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据用户id获取角色列表")
	////@PreAuthorize("hasAuthority('sys:role:info:userId')")
	//@GetMapping("/info/userId")
	//public Result<List<RoleVO>> findRoleByUserId(
	//	@Parameter(name = "userId", description = "用户id", required = true, in = ParameterIn.QUERY)
	//	@NotNull(message = "用户id不能为空")
	//	@RequestParam(value = "userId") Long userId) {
	//	Set<Long> userIds = new HashSet<>();
	//	userIds.add(userId);
	//	List<SysRole> roles = sysRoleService.findRoleByUserIds(userIds);
	//	List<RoleVO> collect = roles.stream().filter(Objects::nonNull)
	//		.map(SysRoleUtil::copy).collect(Collectors.toList());
	//	return Result.success(collect);
	//}
	//
	///**
	// * 根据用户id列表获取角色列表
	// *
	// * @param userIds 用户id列表
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.role.RoleVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:12:32
	// */
	//@Operation(summary = "根据用户id列表获取角色列表", description = "根据用户id列表获取角色列表", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据用户id列表获取角色列表")
	//@PreAuthorize("hasAuthority('sys:role:info:userIds')")
	//@GetMapping("/info/userIds")
	//public Result<List<RoleVO>> findRoleByUserIds(
	//	@Parameter(name = "userIds", description = "用户id列表", required = true, schema = @Schema(implementation = Set.class), in = ParameterIn.QUERY)
	//	@NotNull(message = "用户id列表不能为空")
	//	@RequestParam(value = "userIds") Set<Long> userIds) {
	//	List<SysRole> roles = sysRoleService.findRoleByUserIds(userIds);
	//	List<RoleVO> collect = roles.stream().filter(Objects::nonNull)
	//		.map(SysRoleUtil::copy).collect(Collectors.toList());
	//	return Result.success(collect);
	//}
	//
	///**
	// * 根据角色id更新资源信息(角色分配资源)
	// *
	// * @param roleResourceDTO 根据角色id更新资源信息DTO
	// * @return {@link Result&lt;java.lang.Boolean&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 15:12:46
	// */
	//@Operation(summary = "根据角色id更新资源信息(角色分配资源)", description = "根据角色id更新资源信息(角色分配资源)",
	//	method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据角色id更新资源信息(角色分配资源)")
	//@PreAuthorize("hasAuthority('sys:role:resource')")
	//@PutMapping("/resource")
	//public Result<Boolean> saveRoleResources(
	//	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "根据角色id更新资源信息DTO", required = true)
	//	@Validated @RequestBody RoleResourceDTO roleResourceDTO) {
	//	Boolean result = sysRoleService.saveRoleResources(roleResourceDTO);
	//	return Result.success(result);
	//}
}
