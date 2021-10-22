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

import cn.hutool.core.collection.CollUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.SecurityUtil;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.uc.api.bo.resource.ResourceBO;
import com.taotao.cloud.uc.api.dto.resource.ResourceSaveDTO;
import com.taotao.cloud.uc.api.dto.resource.ResourceUpdateDTO;
import com.taotao.cloud.uc.biz.service.ISysResourceService;
import com.taotao.cloud.uc.api.vo.resource.ResourceQueryVO;
import com.taotao.cloud.uc.api.vo.resource.ResourceTreeVO;
import com.taotao.cloud.uc.biz.entity.SysResource;
import com.taotao.cloud.uc.biz.mapstruct.ResourceMapper;
import com.taotao.cloud.web.base.controller.SuperController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class SysResourceController extends
	SuperController<ISysResourceService<SysResource, Long>, SysResource, Long, BaseQuery, ResourceSaveDTO, ResourceUpdateDTO, ResourceQueryVO> {

	/**
	 * 根据角色id获取资源列表
	 *
	 * @param roleId 角色id
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 15:04:45
	 */
	@Operation(summary = "根据角色id获取资源列表", description = "根据角色id获取资源列表")
	@RequestOperateLog(description = "根据角色id获取资源列表")
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@SentinelResource(value = "findResourceByRoleId", blockHandler = "findResourceByRoleIdException")
	@GetMapping("/roleId/{roleId}")
	public Result<List<ResourceQueryVO>> findResourceByRoleId(
		@Parameter(description = "角色id", required = true) @NotNull(message = "角色id不能为空")
		@PathVariable(value = "roleId") Long roleId) {
		List<ResourceBO> resources = service().findResourceByRoleIds(Set.of(roleId));
		List<ResourceQueryVO> result = ResourceMapper.INSTANCE.resourceBosToVos(resources);
		return success(result);
	}

	/**
	 * 根据角色id列表获取角色列表
	 *
	 * @param roleIds 用户id列表
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 15:04:52
	 */
	@Operation(summary = "根据角色id列表获取角色列表", description = "根据角色id列表获取角色列表")
	@RequestOperateLog(description = "根据角色id列表获取角色列表")
	@PreAuthorize("hasAuthority('sys:resource:info:roleIds')")
	@GetMapping("/roleIds")
	public Result<List<ResourceQueryVO>> findResourceByRoleIds(
		@Parameter(description = "角色id列表", required = true) @NotEmpty(message = "角色id列表不能为空")
		@RequestParam(value = "roleIds") Set<Long> roleIds) {
		List<ResourceBO> resources = service().findResourceByRoleIds(roleIds);
		List<ResourceQueryVO> result = ResourceMapper.INSTANCE.resourceBosToVos(resources);
		return Result.success(result);
	}

	/**
	 * 根据角色code获取资源列表
	 *
	 * @param code 角色code
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 15:04:59
	 */
	@Operation(summary = "根据角色code获取资源列表", description = "根据角色code获取资源列表")
	@RequestOperateLog(description = "根据角色code获取资源列表")
	@PreAuthorize("hasAuthority('sys:resource:info:code')")
	@GetMapping("/code/{code}")
	public Result<List<ResourceQueryVO>> findResourceByCode(
		@Parameter(description = "角色code", required = true) @NotBlank(message = "角色code不能为空")
		@PathVariable(value = "code") String code) {
		List<ResourceBO> resources = service().findResourceByCodes(Set.of(code));
		List<ResourceQueryVO> result = ResourceMapper.INSTANCE.resourceBosToVos(resources);
		return Result.success(result);
	}

	/**
	 * 根据角色code列表获取角色列表
	 *
	 * @param codes 角色cde列表
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 15:05:06
	 */
	@Operation(summary = "根据角色code列表获取角色列表", description = "根据角色code列表获取角色列表")
	@RequestOperateLog(description = "根据角色cde列表获取角色列表")
	@PreAuthorize("hasAuthority('sys:resource:info:codes')")
	@GetMapping("/codes")
	public Result<List<ResourceQueryVO>> findResourceByCodes(
		@Parameter(description = "角色cde列表", required = true) @NotNull(message = "角色cde列表不能为空")
		@RequestParam(value = "codes") Set<String> codes) {
		List<ResourceBO> resources = service().findResourceByCodes(codes);
		List<ResourceQueryVO> result = ResourceMapper.INSTANCE.resourceBosToVos(resources);
		return success(result);
	}

	//@ApiOperation("根据parentId获取角色列表")
	//@SysOperateLog(description = "根据parentId获取角色列表")
	//@PreAuthorize("hasAuthority('sys:resource:info:parentId')")
	//@GetMapping("/info/parentId")
	//public Result<List<ResourceVO>> findResourceByCode1s(@NotNull(message = "parentId不能为空")
	//@RequestParam(value = "parentId") Long parentId) {
	//	List<SysResource> roles = resourceService.findResourceByParentId(parentId);
	//	List<ResourceVO> collect = roles.stream()
	//		.filter(Objects::nonNull)
	//		.map(SysResourceUtil::copy)
	//		.collect(Collectors.toList());
	//	return Result.succeed(collect);
	//}

	/**
	 * 获取当前用户菜单列表
	 *
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceVO&gt;&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 15:05:16
	 */
	@Operation(summary = "获取当前用户菜单列表", description = "获取当前用户菜单列表")
	@RequestOperateLog(description = "获取当前用户菜单列表")
	@PreAuthorize("hasAuthority('sys:resource:current:user')")
	@GetMapping("/current/user")
	public Result<List<ResourceQueryVO>> findCurrentUserResource() {
		Set<String> roleCodes = SecurityUtil.getUser().getRoles();
		if (CollUtil.isEmpty(roleCodes)) {
			return success(new ArrayList<>());
		}
		return findResourceByCodes(roleCodes);
	}

	/**
	 * 获取当前用户树形菜单列表
	 *
	 * @param parentId 父id
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceTree&gt;&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 15:05:20
	 */
	@Operation(summary = "获取当前用户树形菜单列表", description = "获取当前用户树形菜单列表")
	@RequestOperateLog(description = "获取当前用户树形菜单列表")
	@PreAuthorize("hasAuthority('sys:resource:current:user:tree')")
	@GetMapping("/current/user/tree")
	public Result<List<ResourceTreeVO>> findCurrentUserResourceTree(
		@Parameter(description = "父id") @RequestParam(value = "parentId") Long parentId) {
		Set<String> roleCodes = SecurityUtil.getUser().getRoles();
		if (CollUtil.isEmpty(roleCodes)) {
			return Result.success(Collections.emptyList());
		}
		Result<List<ResourceQueryVO>> result = findResourceByCodes(roleCodes);
		List<ResourceQueryVO> resourceVOList = result.data();
		List<ResourceTreeVO> trees = service().findCurrentUserResourceTree(resourceVOList,
			parentId);
		return Result.success(trees);
	}

	/**
	 * 获取树形菜单集合 1.false-非懒加载，查询全部 2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0
	 *
	 * @param lazy     是否是延迟查询
	 * @param parentId 父id
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.resource.ResourceTree&gt;&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 15:05:42
	 */
	@Operation(summary = "获取树形菜单集合", description = "获取树形菜单集合 1.false-非懒加载，查询全部 " +
		"2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0")
	@RequestOperateLog(description = "获取树形菜单集合")
	@PreAuthorize("hasAuthority('sys:resource:info:tree')")
	@GetMapping("/tree")
	@SentinelResource(value = "findResourceTree", blockHandler = "testSeataException")
	public Result<List<ResourceTreeVO>> findResourceTree(
		@Parameter(name = "lazy", description = "是否是延迟查询") @RequestParam(value = "lazy") boolean lazy,
		@Parameter(name = "parentId", description = "父id") @RequestParam(value = "parentId") Long parentId) {
		List<ResourceTreeVO> trees = service().findResourceTree(lazy, parentId);
		return success(trees);
	}


	@Operation(summary = "测试分布式事务", description = "测试分布式事务")
	@RequestOperateLog(description = "测试分布式事务")
	@GetMapping("/test/se")
	@SentinelResource(value = "se")
	public Result<Boolean> testSe(@RequestParam(value = "id") Long id) {
		Boolean result = service().testSeata();
		return Result.success(result);
	}

	@Operation(summary = "测试异步", description = "测试异步")
	@RequestOperateLog(description = "测试异步")
	@GetMapping("/test/async")
	public Result<Boolean> testAsync() throws ExecutionException, InterruptedException {
		Future<Boolean> result = service().testAsync();
		return Result.success(result.get());
	}

	@Operation(summary = "测试异步结果", description = "测试异步结果")
	@RequestOperateLog(description = "测试异步结果")
	@GetMapping("/test/async/future")
	public Future<Boolean> testAsyncFuture() {
		return service().testAsync();
	}

	@Operation(summary = "测试分布式事务", description = "测试分布式事务")
	@RequestOperateLog(description = "测试分布式事务")
	@GetMapping("/test/seata")
	@SentinelResource(value = "testSeata", blockHandler = "testSeataException")
	public Result<Boolean> testSeata(HttpServletRequest request, HttpServletResponse response) {
		Boolean result = service().testSeata();
		return Result.success(result);
	}

	public Result<Boolean> testSeataException(BlockException e) {
		e.printStackTrace();
		LogUtil.error(" 该接口已经被限流啦", e);
		return Result.fail("该接口已经被限流啦");
	}

}
