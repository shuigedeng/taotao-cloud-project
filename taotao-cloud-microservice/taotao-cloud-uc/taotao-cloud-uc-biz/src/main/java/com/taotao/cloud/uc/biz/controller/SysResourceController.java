package com.taotao.cloud.uc.biz.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.SecurityUtil;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.uc.api.dto.resource.ResourceDTO;
import com.taotao.cloud.uc.api.query.resource.ResourcePageQuery;
import com.taotao.cloud.uc.api.vo.resource.ResourceTree;
import com.taotao.cloud.uc.api.vo.resource.ResourceVO;
import com.taotao.cloud.uc.biz.entity.SysResource;
import com.taotao.cloud.uc.biz.mapper.ResourceMapper;
import com.taotao.cloud.uc.biz.service.ISysResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
 * 资源管理API
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:05
 * @since 1.0
 */
@Validated
@RestController
@RequestMapping("/resource")
@Tag(name = "资源管理API", description = "资源管理API")
public class SysResourceController {

	private final ISysResourceService resourceService;
	private final RedisRepository redisRepository;

	public SysResourceController(ISysResourceService resourceService,
		RedisRepository redisRepository) {
		this.resourceService = resourceService;
		this.redisRepository = redisRepository;
	}

	@Operation(summary = "添加资源", description = "添加资源", method = CommonConstant.POST, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "添加资源")
	@PreAuthorize("hasAuthority('sys:resource:save')")
	@PostMapping
	public Result<ResourceVO> saveResource(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "添加资源对象DTO", required = true)
		@Validated @RequestBody ResourceDTO resourceDTO) {
		SysResource resource = ResourceMapper.INSTANCE.resourceDtoToSysResource(resourceDTO);
		SysResource sysResource = resourceService.saveResource(resource);
		ResourceVO result = ResourceMapper.INSTANCE.sysResourceDtoResourceVo(sysResource);
		return Result.success(result);
	}

	@Operation(summary = "根据id删除资源", description = "根据id删除资源", method = CommonConstant.DELETE,
		security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据id删除资源")
	@PreAuthorize("hasAuthority('sys:resource:delete')")
	@DeleteMapping("/{id:[0-9]*}")
	public Result<Boolean> deleteResource(
		@Parameter(name = "id", description = "资源id", required = true, in = ParameterIn.PATH)
		@PathVariable(value = "id") Long id) {
		Boolean result = resourceService.deleteResource(id);
		return Result.success(result);
	}

	@Operation(summary = "修改资源", description = "修改资源", method = "POST", security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "修改资源")
	@PreAuthorize("hasAuthority('sys:resource:update')")
	@PutMapping("/{id:[0-9]*}")
	public Result<Boolean> updateResource(
		@Parameter(name = "id", description = "资源id", required = true, in = ParameterIn.PATH)
		@NotNull(message = "资源id不能为空")
		@PathVariable(value = "id") Long id,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "修改资源对象DTO", required = true)
		@Validated @RequestBody ResourceDTO resourceDTO) {
		SysResource resource = resourceService.findResourceById(id);
		ResourceMapper.INSTANCE.copyResourceDtoToSysResource(resourceDTO, resource);
		SysResource updateResource = resourceService.updateResource(resource);
		ResourceVO result = ResourceMapper.INSTANCE.sysResourceDtoResourceVo(updateResource);
		return Result.success(Objects.nonNull(result));
	}

	@Operation(summary = "根据id查询资源是否存在", description = "根据id查询资源是否存在", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据id查询资源是否存在")
	@PreAuthorize("hasAuthority('sys:resource:exists:phone')")
	@GetMapping("/exists/id")
	public Result<Boolean> existsByPhone(
		@Parameter(name = "id", description = "资源id", required = true, in = ParameterIn.QUERY)
		@NotNull(message = "资源id不能为空")
		@RequestParam(value = "id") Long id) {
		Boolean result = resourceService.existsById(id);
		return Result.success(result);
	}

	@Operation(summary = "根据名称查询资源是否存在", description = "根据名称查询资源是否存在", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据名称查询资源是否存在")
	@PreAuthorize("hasAuthority('sys:resource:exists:phone')")
	@GetMapping("/exists/name")
	public Result<Boolean> existsByName(
		@Parameter(name = "name", description = "资源名称", required = true, in = ParameterIn.QUERY)
		@NotBlank(message = "资源名称不能为空")
		@RequestParam(value = "name") String name) {
		Boolean result = resourceService.existsByName(name);
		return Result.success(result);
	}

	@Operation(summary = "根据id获取资源信息", description = "根据id获取资源信息", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据id获取资源信息")
	@PreAuthorize("hasAuthority('sys:resource:info:id')")
	@GetMapping("/info/id")
	public Result<ResourceVO> findResourceById(
		@Parameter(name = "id", description = "资源id", required = true, in = ParameterIn.QUERY)
		@NotNull(message = "资源id不能为空")
		@RequestParam(value = "id") Long id) {
		SysResource resource = resourceService.findResourceById(id);
		ResourceVO result = ResourceMapper.INSTANCE.sysResourceDtoResourceVo(resource);
		return Result.success(result);
	}

	@Operation(summary = "根据名称获取资源信息", description = "根据名称获取资源信息", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据名称获取资源信息")
	@PreAuthorize("hasAuthority('sys:resource:info:name')")
	@GetMapping("/info/name")
	public Result<ResourceVO> findResourceByName(
		@Parameter(name = "name", description = "资源名称", required = true, in = ParameterIn.QUERY)
		@NotBlank(message = "资源名称不能为空")
		@RequestParam(value = "name") String name) {
		SysResource resource = resourceService.findResourceByName(name);
		ResourceVO result = ResourceMapper.INSTANCE.sysResourceDtoResourceVo(resource);
		return Result.success(result);
	}

	@Operation(summary = "分页查询资源集合", description = "分页查询资源集合", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "分页查询资源集合")
	@PreAuthorize("hasAuthority('sys:resource:view:page')")
	@GetMapping(value = "/page")
	public Result<PageModel<ResourceVO>> findResourcePage(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "分页查询资源集合对象DTO", required = true)
		@Validated @NotNull ResourcePageQuery resourceQuery) {
		Pageable pageable = PageRequest
			.of(resourceQuery.getCurrentPage(), resourceQuery.getPageSize());
		Page<SysResource> page = resourceService
			.findResourcePage(pageable, resourceQuery);
		List<ResourceVO> resources = ResourceMapper.INSTANCE
			.sysResourceToResourceVo(page.getContent());
		Page<ResourceVO> result = new PageImpl<>(resources, pageable,
			page.getTotalElements());
		return Result.success(PageModel.convertJpaPage(result));
	}

	@Operation(summary = "查询所有资源列表", description = "查询所有资源列表", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "查询所有资源列表")
	@PreAuthorize("hasAuthority('sys:resource:list')")
	@GetMapping
	public Result<List<ResourceVO>> findAllResources() {
		List<SysResource> resources = resourceService.findAllResources();
		List<ResourceVO> result = ResourceMapper.INSTANCE.sysResourceToResourceVo(resources);
		return Result.success(result);
	}

	@Operation(summary = "根据角色id获取资源列表", description = "根据角色id获取资源列表", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据角色id获取资源列表")
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@SentinelResource(value = "findResourceByRoleId", blockHandler = "findResourceByRoleIdException")
	@GetMapping("/info/roleId")
	public Result<List<ResourceVO>> findResourceByRoleId(
		@Parameter(name = "roleId", description = "角色id", required = true, in = ParameterIn.QUERY)
		@NotNull(message = "角色id不能为空")
		@RequestParam(value = "roleId") Long roleId) {
		Set<Long> roleIds = new HashSet<>();
		roleIds.add(roleId);
		List<SysResource> resources = resourceService.findResourceByRoleIds(roleIds);
		List<ResourceVO> result = ResourceMapper.INSTANCE.sysResourceToResourceVo(resources);
		return Result.success(result);
	}

	@Operation(summary = "根据角色id列表获取角色列表", description = "根据角色id列表获取角色列表", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据角色id列表获取角色列表")
	@PreAuthorize("hasAuthority('sys:resource:info:roleIds')")
	@GetMapping("/info/roleIds")
	public Result<List<ResourceVO>> findResourceByRoleIds(
		@Parameter(name = "roleIds", description = "用户id列表", required = true, schema = @Schema(implementation = Set.class), in = ParameterIn.QUERY)
		@NotNull(message = "用户id列表不能为空")
		@RequestParam(value = "roleIds") Set<Long> roleIds) {
		List<SysResource> resources = resourceService.findResourceByRoleIds(roleIds);
		List<ResourceVO> result = ResourceMapper.INSTANCE.sysResourceToResourceVo(resources);
		return Result.success(result);
	}

	@Operation(summary = "根据角色code获取资源列表", description = "根据角色code获取资源列表", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据角色code获取资源列表")
	@PreAuthorize("hasAuthority('sys:resource:info:code')")
	@GetMapping("/info/code")
	public Result<List<ResourceVO>> findResourceByCode(
		@Parameter(name = "code", description = "角色code", required = true, in = ParameterIn.QUERY)
		@NotNull(message = "角色code不能为空")
		@RequestParam(value = "code") String code) {
		Set<String> codes = new HashSet<>();
		codes.add(code);
		List<SysResource> resources = resourceService.findResourceByCodes(codes);
		List<ResourceVO> result = ResourceMapper.INSTANCE.sysResourceToResourceVo(resources);
		return Result.success(result);
	}

	@Operation(summary = "根据角色code列表获取角色列表", description = "根据角色code列表获取角色列表", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据角色cde列表获取角色列表")
	//@PreAuthorize("hasAuthority('sys:resource:info:codes')")
	@GetMapping("/info/codes")
	public Result<List<ResourceVO>> findResourceByCodes(
		@Parameter(name = "codes", description = "角色cde列表", required = true, schema = @Schema(implementation = Set.class), in = ParameterIn.QUERY)
		@NotNull(message = "角色cde列表不能为空")
		@RequestParam(value = "codes") Set<String> codes) {
		List<SysResource> resources = resourceService.findResourceByCodes(codes);
		List<ResourceVO> result = ResourceMapper.INSTANCE.sysResourceToResourceVo(resources);
		return Result.success(result);
	}

	// @ApiOperation("根据parentId获取角色列表")
	// @SysOperateLog(description = "根据parentId获取角色列表")
	// @PreAuthorize("hasAuthority('sys:resource:info:parentId')")
	// @GetMapping("/info/parentId")
	// public Result<List<ResourceVO>> findResourceByCode1s(@NotNull(message = "parentId不能为空")
	// 													 @RequestParam(value = "parentId") Long parentId) {
	// 	List<SysResource> roles = resourceService.findResourceByParentId(parentId);
	// 	List<ResourceVO> collect = roles.stream().filter(Objects::nonNull)
	// 		.map(SysResourceUtil::copy).collect(Collectors.toList());
	// 	return Result.succeed(collect);
	// }

	@Operation(summary = "获取当前用户菜单列表", description = "获取当前用户菜单列表", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "获取当前用户菜单列表")
	@PreAuthorize("hasAuthority('sys:resource:current:user')")
	@GetMapping("/info/current/user")
	public Result<List<ResourceVO>> findCurrentUserResource() {
		Set<String> roleCodes = SecurityUtil.getUser().getRoles();
		if (CollUtil.isEmpty(roleCodes)) {
			return Result.success(Collections.emptyList());
		}
		return findResourceByCodes(roleCodes);
	}

	@Operation(summary = "获取当前用户树形菜单列表", description = "获取当前用户树形菜单列表", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "获取当前用户树形菜单列表")
	@PreAuthorize("hasAuthority('sys:resource:current:user:tree')")
	@GetMapping("/info/current/user/tree")
	public Result<List<ResourceTree>> findCurrentUserResourceTree(
		@Parameter(name = "parentId", description = "父id", required = true, in = ParameterIn.QUERY)
		@RequestParam(value = "parentId") Long parentId) {
		Set<String> roleCodes = SecurityUtil.getUser().getRoles();
		if (CollUtil.isEmpty(roleCodes)) {
			return Result.success(Collections.emptyList());
		}
		Result<List<ResourceVO>> result = findResourceByCodes(roleCodes);
		List<ResourceVO> resourceVOList = result.getData();
		List<ResourceTree> trees = resourceService
			.findCurrentUserResourceTree(resourceVOList, parentId);
		return Result.success(trees);
	}

	@Operation(summary = "获取树形菜单集合 1.false-非懒加载，查询全部 " +
		"2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0",
		description = "获取树形菜单集合 1.false-非懒加载，查询全部 " +
			"2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0", method = "POST", security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "获取树形菜单集合")
	@PreAuthorize("hasAuthority('sys:resource:info:tree')")
	@GetMapping("/info/tree")
	@SentinelResource(value = "findResourceTree", blockHandler = "testSeataException")
	public Result<List<ResourceTree>> findResourceTree(
		@Parameter(name = "lazy", description = "是否是延迟查询", required = false, in = ParameterIn.QUERY)
		@RequestParam(value = "lazy") boolean lazy,
		@Parameter(name = "parentId", description = "父id", required = false, in = ParameterIn.QUERY)
		@RequestParam(value = "parentId") Long parentId) {
		List<ResourceTree> trees = resourceService.findResourceTree(lazy, parentId);
		return Result.success(trees);
	}

	@Operation(summary = "测试分布式事务", description = "测试分布式事务", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "测试分布式事务")
	@GetMapping("/test/se")
	@SentinelResource(value = "se")
	public Result<Boolean> testSe(@RequestParam(value = "id") Long id) {
		redisRepository.set("hslfjsl", "sldf");
		if (1 == id) {
			throw new RuntimeException("hahahaaha");
		} else {
			Boolean result = resourceService.testSeata();
			return Result.success(result);
		}
	}

	@Operation(summary = "测试分布式事务", description = "测试分布式事务", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "测试分布式事务")
	@GetMapping("/test/seata")
	@SentinelResource(value = "testSeata", blockHandler = "testSeataException")
	public Result<Boolean> testSeata() {
		Boolean result = resourceService.testSeata();
		return Result.success(result);
	}

	public Result<Boolean> testSeataException(BlockException e) {
		e.printStackTrace();
		LogUtil.error(" 该接口已经被限流啦", e);
		return Result.fail(false);
	}

}
