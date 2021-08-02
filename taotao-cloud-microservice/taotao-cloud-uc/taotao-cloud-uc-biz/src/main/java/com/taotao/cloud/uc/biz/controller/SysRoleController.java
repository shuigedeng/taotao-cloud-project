package com.taotao.cloud.uc.biz.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.uc.api.dto.role.RoleDTO;
import com.taotao.cloud.uc.api.dto.role.RoleResourceDTO;
import com.taotao.cloud.uc.api.query.role.RolePageQuery;
import com.taotao.cloud.uc.api.vo.role.RoleVO;
import com.taotao.cloud.uc.biz.entity.SysRole;
import com.taotao.cloud.uc.biz.service.ISysRoleService;
import com.taotao.cloud.uc.biz.utils.SysRoleUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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

/**
 * 角色管理API
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:05
 * @since 1.0
 */
@Validated
@RestController
@RequestMapping("/role")
@Tag(name = "角色管理API", description = "角色管理API")
public class SysRoleController {

	private final ISysRoleService sysRoleService;

	public SysRoleController(ISysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	@Operation(summary = "根据角色id获取角色信息", description = "根据角色id获取角色信息", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据角色id获取角色信息")
	@PreAuthorize("hasAuthority('sys:role:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<RoleVO> findRoleById(
		@Parameter(name = "id", description = "角色id", required = true, in = ParameterIn.QUERY)
		@NotNull(message = "角色id不能为空")
		@PathVariable(value = "id") Long id) {
		SysRole role = sysRoleService.findRoleById(id);
		RoleVO vo = SysRoleUtil.copy(role);
		return Result.success(vo);
	}

	@Operation(summary = "根据code查询角色是否存在", description = "根据code查询角色是否存在", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据code查询角色是否存在")
	@PreAuthorize("hasAuthority('sys:role:exist:code')")
	@GetMapping("/exist/code/{code}")
	public Result<Boolean> existRoleByCode(
		@Parameter(name = "code", description = "code", required = true, in = ParameterIn.QUERY)
		@NotBlank(message = "角色code不能为空")
		@PathVariable(value = "code") String code) {
		Boolean result = sysRoleService.existRoleByCode(code);
		return Result.success(result);
	}

	@Operation(summary = "根据code获取角色信息", description = "根据code获取角色信息", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据code获取角色信息")
	@PreAuthorize("hasAuthority('sys:role:info:code')")
	@GetMapping("/info/code")
	public Result<RoleVO> findRoleByCode(
		@Parameter(name = "code", description = "code", required = true, in = ParameterIn.QUERY)
		@RequestParam(value = "code") String code) {
		SysRole role = sysRoleService.findRoleByCode(code);
		RoleVO vo = SysRoleUtil.copy(role);
		return Result.success(vo);
	}

	@Operation(summary = "根据code列表获取角色信息", description = "根据code列表获取角色信息", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据code列表获取角色信息")
	@PreAuthorize("hasAuthority('sys:role:info:code')")
	@GetMapping("/info/codes")
	public Result<List<RoleVO>> findRoleByCodes(
		@Parameter(name = "codes", description = "code列表", required = true, schema = @Schema(implementation = Set.class), in = ParameterIn.QUERY)
		@NotNull(message = "code列表不能为空")
		@RequestParam(value = "codes") Set<String> codes) {
		List<SysRole> roles = sysRoleService.findRoleByCodes(codes);
		List<RoleVO> collect = roles.stream().filter(Objects::nonNull)
			.map(SysRoleUtil::copy).collect(Collectors.toList());
		return Result.success(collect);
	}

	@Operation(summary = "添加角色", description = "添加角色", method = CommonConstant.POST, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "添加角色")
	//@PreAuthorize("hasAuthority('sys:role:save')")
	@PostMapping
	public Result<Boolean> saveRole(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "添加角色对象DTO", required = true)
		@Valid @RequestBody RoleDTO roleDTO) {
		Boolean result = sysRoleService.saveRole(roleDTO);
		return Result.success(result);
	}

	@Operation(summary = "修改角色", description = "修改角色", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "修改角色")
	@PreAuthorize("hasAuthority('sys:role:update')")
	@PostMapping("/{id:[0-9]*}")
	public Result<Boolean> updateRole(
		@Parameter(name = "id", description = "角色id", required = true, in = ParameterIn.PATH)
		@PathVariable(value = "id") Long id,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "修改角色对象DTO", required = true)
		@Validated @RequestBody RoleDTO roleDTO) {
		Boolean result = sysRoleService.updateRole(id, roleDTO);
		return Result.success(result);
	}

	@Operation(summary = "根据id删除角色", description = "根据id删除角色", method = CommonConstant.DELETE, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据id删除角色")
	@PreAuthorize("hasAuthority('sys:role:delete')")
	@DeleteMapping("/{id:[0-9]*}")
	public Result<Boolean> deleteRole(
		@Parameter(name = "id", description = "角色id", required = true, in = ParameterIn.PATH)
		@PathVariable(value = "id") Long id) {
		Boolean result = sysRoleService.deleteRole(id);
		return Result.success(result);
	}

	@Operation(summary = "分页查询角色集合", description = "分页查询角色集合", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "分页查询角色集合")
	@PreAuthorize("hasAuthority('sys:role:view:page')")
	@GetMapping(value = "/page")
	public Result<PageModel<RoleVO>> findRolePage(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "分页查询角色集合对象QUERY", required = true)
		@Validated @NotNull RolePageQuery roleQuery) {
		Pageable pageable = PageRequest.of(roleQuery.getCurrentPage(), roleQuery.getPageSize());
		Page<SysRole> page = sysRoleService.findRolePage(pageable, roleQuery);
		List<RoleVO> collect = page.stream().filter(Objects::nonNull)
			.map(SysRoleUtil::copy).collect(Collectors.toList());
		Page<RoleVO> result = new PageImpl<>(collect, pageable, page.getTotalElements());
		return Result.success(PageModel.convertJpaPage(result));
	}

	@Operation(summary = "查询所有角色列表", description = "查询所有角色列表", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "查询所有角色列表")
	@PreAuthorize("hasAuthority('sys:role:list')")
	@GetMapping
	public Result<List<RoleVO>> findAllRoles() {
		List<SysRole> pages = sysRoleService.findAllRoles();
		List<RoleVO> collect = pages.stream().filter(Objects::nonNull)
			.map(SysRoleUtil::copy).collect(Collectors.toList());
		return Result.success(collect);
	}

	@Operation(summary = "根据用户id获取角色列表", description = "根据用户id获取角色列表", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据用户id获取角色列表")
	//@PreAuthorize("hasAuthority('sys:role:info:userId')")
	@GetMapping("/info/userId")
	public Result<List<RoleVO>> findRoleByUserId(
		@Parameter(name = "userId", description = "用户id", required = true, in = ParameterIn.QUERY)
		@NotNull(message = "用户id不能为空")
		@RequestParam(value = "userId") Long userId) {
		Set<Long> userIds = new HashSet<>();
		userIds.add(userId);
		List<SysRole> roles = sysRoleService.findRoleByUserIds(userIds);
		List<RoleVO> collect = roles.stream().filter(Objects::nonNull)
			.map(SysRoleUtil::copy).collect(Collectors.toList());
		return Result.success(collect);
	}

	@Operation(summary = "根据用户id列表获取角色列表", description = "根据用户id列表获取角色列表", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据用户id列表获取角色列表")
	@PreAuthorize("hasAuthority('sys:role:info:userIds')")
	@GetMapping("/info/userIds")
	public Result<List<RoleVO>> findRoleByUserIds(
		@Parameter(name = "userIds", description = "用户id列表", required = true, schema = @Schema(implementation = Set.class), in = ParameterIn.QUERY)
		@NotNull(message = "用户id列表不能为空")
		@RequestParam(value = "userIds") Set<Long> userIds) {
		List<SysRole> roles = sysRoleService.findRoleByUserIds(userIds);
		List<RoleVO> collect = roles.stream().filter(Objects::nonNull)
			.map(SysRoleUtil::copy).collect(Collectors.toList());
		return Result.success(collect);
	}

	@Operation(summary = "根据角色id更新资源信息(角色分配资源)", description = "根据角色id更新资源信息(角色分配资源)", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据角色id更新资源信息(角色分配资源)")
	@PreAuthorize("hasAuthority('sys:role:resource')")
	@PutMapping("/resource")
	public Result<Boolean> saveRoleResources(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "根据角色id更新资源信息DTO", required = true)
		@Validated @RequestBody RoleResourceDTO roleResourceDTO) {
		Boolean result = sysRoleService.saveRoleResources(roleResourceDTO);
		return Result.success(result);
	}
}
