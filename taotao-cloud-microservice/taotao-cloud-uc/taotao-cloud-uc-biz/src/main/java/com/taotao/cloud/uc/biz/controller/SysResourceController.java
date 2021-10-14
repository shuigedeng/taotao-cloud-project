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

import com.taotao.cloud.uc.api.dto.resource.ResourceQueryDTO;
import com.taotao.cloud.uc.api.dto.resource.ResourceSaveDTO;
import com.taotao.cloud.uc.api.dto.resource.ResourceUpdateDTO;
import com.taotao.cloud.uc.api.service.ISysResourceService;
import com.taotao.cloud.uc.api.vo.resource.ResourceQueryVO;
import com.taotao.cloud.uc.biz.entity.SysResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.taotao.cloud.web.base.controller.SuperController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 15:02:39
 */
@Validated
@RestController
@RequestMapping("/uc/resource")
@Tag(name = "资源管理API", description = "资源管理API")
public class SysResourceController  extends
	SuperController<ISysResourceService<SysResource, Long>, SysResource, Long, ResourceQueryDTO, ResourceSaveDTO, ResourceUpdateDTO, ResourceQueryVO> {


	//private final ISysResourceService resourceService;
	//private final RedisRepository redisRepository;
	//
	//public SysResourceController(ISysResourceService resourceService,
	//	RedisRepository redisRepository) {
	//	this.resourceService = resourceService;
	//	this.redisRepository = redisRepository;
	//}
	//
	///**
	// * 添加资源
	// *
	// * @param resourceDTO 添加资源对象DTO
	// * @return {@link Result&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:03:23
	// */
	//@Operation(summary = "添加资源", description = "添加资源", method = CommonConstant.POST,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "添加资源")
	//@PreAuthorize("hasAuthority('sys:resource:save')")
	//@PostMapping
	//public Result<ResourceVO> saveResource(
	//	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "添加资源对象DTO", required = true)
	//	@Validated @RequestBody ResourceDTO resourceDTO) {
	//	SysResource resource = ResourceMapper.INSTANCE.resourceDtoToSysResource(resourceDTO);
	//	SysResource sysResource = resourceService.saveResource(resource);
	//	ResourceVO result = ResourceMapper.INSTANCE.sysResourceDtoResourceVo(sysResource);
	//	return Result.success(result);
	//}
	//
	///**
	// * 根据id删除资源
	// *
	// * @param id 资源id
	// * @return {@link Result&lt;java.lang.Boolean&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:03:34
	// */
	//@Operation(summary = "根据id删除资源", description = "根据id删除资源", method = CommonConstant.DELETE,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据id删除资源")
	//@PreAuthorize("hasAuthority('sys:resource:delete')")
	//@DeleteMapping("/{id:[0-9]*}")
	//public Result<Boolean> deleteResource(
	//	@Parameter(name = "id", description = "资源id", required = true, in = ParameterIn.PATH)
	//	@PathVariable(value = "id") Long id) {
	//	Boolean result = resourceService.deleteResource(id);
	//	return Result.success(result);
	//}
	//
	///**
	// * 修改资源
	// *
	// * @param id          资源id
	// * @param resourceDTO 修改资源对象DTO
	// * @return {@link Result&lt;java.lang.Boolean&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 15:03:42
	// */
	//@Operation(summary = "修改资源", description = "修改资源", method = "POST",
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "修改资源")
	//@PreAuthorize("hasAuthority('sys:resource:update')")
	//@PutMapping("/{id:[0-9]*}")
	//public Result<Boolean> updateResource(
	//	@Parameter(name = "id", description = "资源id", required = true, in = ParameterIn.PATH)
	//	@NotNull(message = "资源id不能为空")
	//	@PathVariable(value = "id") Long id,
	//	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "修改资源对象DTO", required = true)
	//	@Validated @RequestBody ResourceDTO resourceDTO) {
	//	SysResource resource = resourceService.findResourceById(id);
	//	ResourceMapper.INSTANCE.copyResourceDtoToSysResource(resourceDTO, resource);
	//	SysResource updateResource = resourceService.updateResource(resource);
	//	ResourceVO result = ResourceMapper.INSTANCE.sysResourceDtoResourceVo(updateResource);
	//	return Result.success(Objects.nonNull(result));
	//}
	//
	///**
	// * 根据id查询资源是否存在
	// *
	// * @param id 资源id
	// * @return {@link Result&lt;java.lang.Boolean&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:03:55
	// */
	//@Operation(summary = "根据id查询资源是否存在", description = "根据id查询资源是否存在",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据id查询资源是否存在")
	//@PreAuthorize("hasAuthority('sys:resource:exists:phone')")
	//@GetMapping("/exists/id")
	//public Result<Boolean> existsByPhone(
	//	@Parameter(name = "id", description = "资源id", required = true, in = ParameterIn.QUERY)
	//	@NotNull(message = "资源id不能为空")
	//	@RequestParam(value = "id") Long id) {
	//	Boolean result = resourceService.existsById(id);
	//	return Result.success(result);
	//}
	//
	///**
	// * 根据名称查询资源是否存在
	// *
	// * @param name 资源名称
	// * @return {@link Result&lt;java.lang.Boolean&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 15:04:04
	// */
	//@Operation(summary = "根据名称查询资源是否存在", description = "根据名称查询资源是否存在",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据名称查询资源是否存在")
	//@PreAuthorize("hasAuthority('sys:resource:exists:phone')")
	//@GetMapping("/exists/name")
	//public Result<Boolean> existsByName(
	//	@Parameter(name = "name", description = "资源名称", required = true, in = ParameterIn.QUERY)
	//	@NotBlank(message = "资源名称不能为空")
	//	@RequestParam(value = "name") String name) {
	//	Boolean result = resourceService.existsByName(name);
	//	return Result.success(result);
	//}
	//
	///**
	// * 根据id获取资源信息
	// *
	// * @param id 资源id
	// * @return {@link Result&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:04:11
	// */
	//@Operation(summary = "根据id获取资源信息", description = "根据id获取资源信息",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据id获取资源信息")
	//@PreAuthorize("hasAuthority('sys:resource:info:id')")
	//@GetMapping("/info/id")
	//public Result<ResourceVO> findResourceById(
	//	@Parameter(name = "id", description = "资源id", required = true, in = ParameterIn.QUERY)
	//	@NotNull(message = "资源id不能为空")
	//	@RequestParam(value = "id") Long id) {
	//	SysResource resource = resourceService.findResourceById(id);
	//	ResourceVO result = ResourceMapper.INSTANCE.sysResourceDtoResourceVo(resource);
	//	return Result.success(result);
	//}
	//
	///**
	// * 根据名称获取资源信息
	// *
	// * @param name 资源名称
	// * @return {@link Result&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:04:22
	// */
	//@Operation(summary = "根据名称获取资源信息", description = "根据名称获取资源信息",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据名称获取资源信息")
	//@PreAuthorize("hasAuthority('sys:resource:info:name')")
	//@GetMapping("/info/name")
	//public Result<ResourceVO> findResourceByName(
	//	@Parameter(name = "name", description = "资源名称", required = true, in = ParameterIn.QUERY)
	//	@NotBlank(message = "资源名称不能为空")
	//	@RequestParam(value = "name") String name) {
	//	SysResource resource = resourceService.findResourceByName(name);
	//	ResourceVO result = ResourceMapper.INSTANCE.sysResourceDtoResourceVo(resource);
	//	return Result.success(result);
	//}
	//
	///**
	// * 分页查询资源集合
	// *
	// * @param resourceQuery 分页查询资源集合对象DTO
	// * @return {@link Result&lt;PageModel&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:04:29
	// */
	//@Operation(summary = "分页查询资源集合", description = "分页查询资源集合",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "分页查询资源集合")
	//@PreAuthorize("hasAuthority('sys:resource:view:page')")
	//@GetMapping(value = "/page")
	//public Result<PageModel<ResourceVO>> findResourcePage(
	//	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "分页查询资源集合对象DTO", required = true)
	//	@Validated @NotNull ResourceQuery resourceQuery) {
	//	Pageable pageable = PageRequest
	//		.of(resourceQuery.getCurrentPage(), resourceQuery.getPageSize());
	//	Page<SysResource> page = resourceService
	//		.findResourcePage(pageable, resourceQuery);
	//	List<ResourceVO> resources = ResourceMapper.INSTANCE
	//		.sysResourceToResourceVo(page.getContent());
	//	Page<ResourceVO> result = new PageImpl<>(resources, pageable,
	//		page.getTotalElements());
	//	return Result.success(PageModel.convertJpaPage(result));
	//}
	//
	///**
	// * 查询所有资源列表
	// *
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:04:40
	// */
	//@Operation(summary = "查询所有资源列表", description = "查询所有资源列表",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "查询所有资源列表")
	//@PreAuthorize("hasAuthority('sys:resource:list')")
	//@GetMapping
	//public Result<List<ResourceVO>> findAllResources() {
	//	List<SysResource> resources = resourceService.findAllResources();
	//	List<ResourceVO> result = ResourceMapper.INSTANCE.sysResourceToResourceVo(resources);
	//	return Result.success(result);
	//}
	//
	///**
	// * 根据角色id获取资源列表
	// *
	// * @param roleId 角色id
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:04:45
	// */
	//@Operation(summary = "根据角色id获取资源列表", description = "根据角色id获取资源列表",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据角色id获取资源列表")
	//@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	//@SentinelResource(value = "findResourceByRoleId", blockHandler = "findResourceByRoleIdException")
	//@GetMapping("/info/roleId")
	//public Result<List<ResourceVO>> findResourceByRoleId(
	//	@Parameter(name = "roleId", description = "角色id", required = true, in = ParameterIn.QUERY)
	//	@NotNull(message = "角色id不能为空")
	//	@RequestParam(value = "roleId") Long roleId) {
	//	Set<Long> roleIds = new HashSet<>();
	//	roleIds.add(roleId);
	//	List<SysResource> resources = resourceService.findResourceByRoleIds(roleIds);
	//	List<ResourceVO> result = ResourceMapper.INSTANCE.sysResourceToResourceVo(resources);
	//	return Result.success(result);
	//}
	//
	///**
	// * 根据角色id列表获取角色列表
	// *
	// * @param roleIds 用户id列表
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:04:52
	// */
	//@Operation(summary = "根据角色id列表获取角色列表", description = "根据角色id列表获取角色列表",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据角色id列表获取角色列表")
	//@PreAuthorize("hasAuthority('sys:resource:info:roleIds')")
	//@GetMapping("/info/roleIds")
	//public Result<List<ResourceVO>> findResourceByRoleIds(
	//	@Parameter(name = "roleIds", description = "用户id列表", required = true, schema = @Schema(implementation = Set.class), in = ParameterIn.QUERY)
	//	@NotNull(message = "用户id列表不能为空")
	//	@RequestParam(value = "roleIds") Set<Long> roleIds) {
	//	List<SysResource> resources = resourceService.findResourceByRoleIds(roleIds);
	//	List<ResourceVO> result = ResourceMapper.INSTANCE.sysResourceToResourceVo(resources);
	//	return Result.success(result);
	//}
	//
	///**
	// * 根据角色code获取资源列表
	// *
	// * @param code 角色code
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:04:59
	// */
	//@Operation(summary = "根据角色code获取资源列表", description = "根据角色code获取资源列表",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据角色code获取资源列表")
	//@PreAuthorize("hasAuthority('sys:resource:info:code')")
	//@GetMapping("/info/code")
	//public Result<List<ResourceVO>> findResourceByCode(
	//	@Parameter(name = "code", description = "角色code", required = true, in = ParameterIn.QUERY)
	//	@NotNull(message = "角色code不能为空")
	//	@RequestParam(value = "code") String code) {
	//	Set<String> codes = new HashSet<>();
	//	codes.add(code);
	//	List<SysResource> resources = resourceService.findResourceByCodes(codes);
	//	List<ResourceVO> result = ResourceMapper.INSTANCE.sysResourceToResourceVo(resources);
	//	return Result.success(result);
	//}
	//
	//
	///**
	// * 根据角色code列表获取角色列表
	// *
	// * @param codes 角色cde列表
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:05:06
	// */
	//@Operation(summary = "根据角色code列表获取角色列表", description = "根据角色code列表获取角色列表",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据角色cde列表获取角色列表")
	////@PreAuthorize("hasAuthority('sys:resource:info:codes')")
	//@GetMapping("/info/codes")
	//public Result<List<ResourceVO>> findResourceByCodes(
	//	@Parameter(name = "codes", description = "角色cde列表", required = true, schema = @Schema(implementation = Set.class), in = ParameterIn.QUERY)
	//	@NotNull(message = "角色cde列表不能为空")
	//	@RequestParam(value = "codes") Set<String> codes) {
	//	List<SysResource> resources = resourceService.findResourceByCodes(codes);
	//	List<ResourceVO> result = ResourceMapper.INSTANCE.sysResourceToResourceVo(resources);
	//	return Result.success(result);
	//}
	//
	//// @ApiOperation("根据parentId获取角色列表")
	//// @SysOperateLog(description = "根据parentId获取角色列表")
	//// @PreAuthorize("hasAuthority('sys:resource:info:parentId')")
	//// @GetMapping("/info/parentId")
	//// public Result<List<ResourceVO>> findResourceByCode1s(@NotNull(message = "parentId不能为空")
	//// 													 @RequestParam(value = "parentId") Long parentId) {
	//// 	List<SysResource> roles = resourceService.findResourceByParentId(parentId);
	//// 	List<ResourceVO> collect = roles.stream().filter(Objects::nonNull)
	//// 		.map(SysResourceUtil::copy).collect(Collectors.toList());
	//// 	return Result.succeed(collect);
	//// }
	//
	///**
	// * 获取当前用户菜单列表
	// *
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:05:16
	// */
	//@Operation(summary = "获取当前用户菜单列表", description = "获取当前用户菜单列表",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "获取当前用户菜单列表")
	//@PreAuthorize("hasAuthority('sys:resource:current:user')")
	//@GetMapping("/info/current/user")
	//public Result<List<ResourceVO>> findCurrentUserResource() {
	//	Set<String> roleCodes = SecurityUtil.getUser().getRoles();
	//	if (CollUtil.isEmpty(roleCodes)) {
	//		return Result.success(Collections.emptyList());
	//	}
	//	return findResourceByCodes(roleCodes);
	//}
	//
	///**
	// * 获取当前用户树形菜单列表
	// *
	// * @param parentId 父id
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceTree&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:05:20
	// */
	//@Operation(summary = "获取当前用户树形菜单列表", description = "获取当前用户树形菜单列表",
	//	method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "获取当前用户树形菜单列表")
	//@PreAuthorize("hasAuthority('sys:resource:current:user:tree')")
	//@GetMapping("/info/current/user/tree")
	//public Result<List<ResourceTree>> findCurrentUserResourceTree(
	//	@Parameter(name = "parentId", description = "父id", required = true, in = ParameterIn.QUERY)
	//	@RequestParam(value = "parentId") Long parentId) {
	//	Set<String> roleCodes = SecurityUtil.getUser().getRoles();
	//	if (CollUtil.isEmpty(roleCodes)) {
	//		return Result.success(Collections.emptyList());
	//	}
	//	Result<List<ResourceVO>> result = findResourceByCodes(roleCodes);
	//	List<ResourceVO> resourceVOList = result.getData();
	//	List<ResourceTree> trees = resourceService
	//		.findCurrentUserResourceTree(resourceVOList, parentId);
	//	return Result.success(trees);
	//}
	//
	///**
	// * 获取树形菜单集合 1.false-非懒加载，查询全部 2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0
	// *
	// * @param lazy     是否是延迟查询
	// * @param parentId 父id
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceTree&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:05:42
	// */
	//@Operation(summary = "获取树形菜单集合 1.false-非懒加载，查询全部 " +
	//	"2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0",
	//	description = "获取树形菜单集合 1.false-非懒加载，查询全部 " +
	//		"2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0",
	//	method = "POST", security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "获取树形菜单集合")
	//@PreAuthorize("hasAuthority('sys:resource:info:tree')")
	//@GetMapping("/info/tree")
	//@SentinelResource(value = "findResourceTree", blockHandler = "testSeataException")
	//public Result<List<ResourceTree>> findResourceTree(
	//	@Parameter(name = "lazy", description = "是否是延迟查询", required = false, in = ParameterIn.QUERY)
	//	@RequestParam(value = "lazy") boolean lazy,
	//	@Parameter(name = "parentId", description = "父id", required = false, in = ParameterIn.QUERY)
	//	@RequestParam(value = "parentId") Long parentId) {
	//	List<ResourceTree> trees = resourceService.findResourceTree(lazy, parentId);
	//	return Result.success(trees);
	//}
	//
	///**
	// * 测试分布式事务
	// *
	// * @param id id
	// * @return {@link Result&lt;java.lang.Boolean&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:05:55
	// */
	//@Operation(summary = "测试分布式事务", description = "测试分布式事务", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "测试分布式事务")
	//@GetMapping("/test/se")
	//@SentinelResource(value = "se")
	//public Result<Boolean> testSe(@RequestParam(value = "id") Long id) {
	//	Boolean result = resourceService.testSeata();
	//	return Result.success(result);
	//}
	//
	//
	///**
	// * 测试异步
	// *
	// * @return {@link Result&lt;java.lang.Boolean&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:06:05
	// */
	//@Operation(summary = "测试异步", description = "测试异步", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "测试异步")
	//@GetMapping("/test/async")
	//public Result<Boolean> testAsync() throws ExecutionException, InterruptedException {
	//	Future<Boolean> result = resourceService.testAsync();
	//	return Result.success(result.get());
	//}
	//
	///**
	// * 测试异步结果
	// *
	// * @return {@link java.util.concurrent.Future&lt;java.lang.Boolean&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:08:02
	// */
	//@Operation(summary = "测试异步结果", description = "测试异步结果", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "测试异步结果")
	//@GetMapping("/test/async/future")
	//public Future<Boolean> testAsyncFuture() throws ExecutionException, InterruptedException {
	//	return resourceService.testAsync();
	//}
	//
	///**
	// * 测试分布式事务
	// *
	// * @param request  request
	// * @param response response
	// * @return {@link Result&lt;java.lang.Boolean&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 15:06:16
	// */
	//@Operation(summary = "测试分布式事务", description = "测试分布式事务", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "测试分布式事务")
	//@GetMapping("/test/seata")
	//@SentinelResource(value = "testSeata", blockHandler = "testSeataException")
	//public Result<Boolean> testSeata(HttpServletRequest request, HttpServletResponse response) {
	//	Boolean result = resourceService.testSeata();
	//	return Result.success(result);
	//}
	//
	//public Result<Boolean> testSeataException(BlockException e) {
	//	e.printStackTrace();
	//	LogUtil.error(" 该接口已经被限流啦", e);
	//	return Result.fail("该接口已经被限流啦");
	//}

}
