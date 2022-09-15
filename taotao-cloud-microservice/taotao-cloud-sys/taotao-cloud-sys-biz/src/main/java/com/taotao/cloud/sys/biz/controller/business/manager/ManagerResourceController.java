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

import cn.hutool.core.collection.CollUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.security.annotation.NotAuth;
import com.taotao.cloud.sys.api.dubbo.request.DubboMenuQueryRequest;
import com.taotao.cloud.sys.api.model.dto.resource.ResourceSaveDTO;
import com.taotao.cloud.sys.api.model.dto.resource.ResourceUpdateDTO;
import com.taotao.cloud.sys.api.model.vo.menu.MenuQueryVO;
import com.taotao.cloud.sys.api.model.vo.menu.MenuTreeVO;
import com.taotao.cloud.sys.biz.model.bo.MenuBO;
import com.taotao.cloud.sys.biz.model.convert.ResourceConvert;
import com.taotao.cloud.sys.biz.model.entity.system.Resource;
import com.taotao.cloud.sys.biz.service.business.IResourceService;
import com.taotao.cloud.web.base.controller.SuperController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 平台管理端-菜单管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 15:02:39
 */
@Validated
@RestController
@RequestMapping("/sys/manager/resource")
@Tag(name = "平台管理端-资源管理API", description = "平台管理端-资源管理API")
public class ManagerResourceController extends
	SuperController<IResourceService, Resource, Long, BaseQuery, ResourceSaveDTO, ResourceUpdateDTO, MenuQueryVO> {

	//************************************************菜单*************************************************************

	@Operation(summary = "根据角色id获取菜单列表", description = "根据角色id获取菜单列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@SentinelResource(value = "findResourceByRoleId", blockHandler = "findResourceByRoleIdException")
	@GetMapping("/roleId/{roleId}")
	public Result<List<MenuQueryVO>> findResourceByRoleId(
		@Parameter(description = "角色id", required = true) @NotNull(message = "角色id不能为空")
		@PathVariable(value = "roleId") Long roleId) {
		List<MenuBO> bos = service().findMenuByRoleIds(Set.of(roleId));
		List<MenuQueryVO> result = ResourceConvert.INSTANCE.convertListVO(bos);
		return success(result);
	}

	@Operation(summary = "根据角色id列表获取角色列表", description = "根据角色id列表获取角色列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleIds')")
	@GetMapping("/roleIds")
	public Result<List<MenuQueryVO>> findResourceByRoleIds(
		@Parameter(description = "角色id列表", required = true) @NotEmpty(message = "角色id列表不能为空")
		@RequestParam(value = "roleIds") Set<Long> roleIds) {
		List<MenuBO> resources = service().findMenuByRoleIds(roleIds);
		List<MenuQueryVO> result = ResourceConvert.INSTANCE.convertListVO(resources);
		return Result.success(result);
	}

	@Operation(summary = "根据角色code获取菜单列表", description = "根据角色code获取菜单列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:code')")
	@GetMapping("/code/{code}")
	public Result<List<MenuQueryVO>> findResourceByCode(
		@Parameter(description = "角色code", required = true) @NotBlank(message = "角色code不能为空")
		@PathVariable(value = "code") String code) {
		List<MenuBO> resources = service().findMenuByCodes(Set.of(code));
		List<MenuQueryVO> result = ResourceConvert.INSTANCE.convertListVO(resources);
		return Result.success(result);
	}

	@Operation(summary = "根据角色code列表获取角色列表", description = "根据角色code列表获取角色列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:codes')")
	@GetMapping("/codes")
	public Result<List<MenuQueryVO>> findResourceByCodes(
		@Parameter(description = "角色cde列表", required = true) @NotNull(message = "角色cde列表不能为空")
		@RequestParam(value = "codes") Set<String> codes) {
		List<MenuBO> resources = service().findMenuByCodes(codes);
		List<MenuQueryVO> result = ResourceConvert.INSTANCE.convertListVO(resources);
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

	@Operation(summary = "获取当前用户菜单列表", description = "获取当前用户菜单列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:current:user')")
	@GetMapping("/current/user")
	public Result<List<MenuQueryVO>> findCurrentUserResource() {
		Set<String> roleCodes = SecurityUtils.getCurrentUser().getRoleCodes();
		if (CollUtil.isEmpty(roleCodes)) {
			return success(new ArrayList<>());
		}
		return findResourceByCodes(roleCodes);
	}

	@Operation(summary = "获取当前用户树形菜单列表", description = "获取当前用户树形菜单列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:current:user:tree')")
	@GetMapping("/current/user/tree")
	public Result<List<MenuTreeVO>> findCurrentUserResourceTree(
		@Parameter(description = "父id") @RequestParam(value = "parentId") Long parentId) {
		Set<String> roleCodes = SecurityUtils.getCurrentUser().getRoleCodes();
		if (CollUtil.isEmpty(roleCodes)) {
			return Result.success(Collections.emptyList());
		}

		Result<List<MenuQueryVO>> result = findResourceByCodes(roleCodes);
		List<MenuQueryVO> resourceVOList = result.data();

		List<MenuTreeVO> trees = service().findCurrentUserMenuTree(resourceVOList,
			parentId);
		return Result.success(trees);
	}

	@Operation(summary = "获取树形菜单集合", description = "获取树形菜单集合 1.false-非懒加载，查询全部 " +
		"2.true-懒加载，根据parentId查询 2.1 父节点为空，则查询parentId=0")
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:tree')")
	@GetMapping("/tree")
	@SentinelResource(value = "findResourceTree", blockHandler = "testSeataException")
	public Result<List<MenuTreeVO>> findResourceTree(
		@Parameter(name = "lazy", description = "是否是延迟查询") @RequestParam(value = "lazy") boolean lazy,
		@Parameter(name = "parentId", description = "父id") @RequestParam(value = "parentId") Long parentId) {
		List<MenuTreeVO> trees = service().findMenuTree(lazy, parentId);
		return success(trees);
	}

	@NotAuth
	@Operation(summary = "testNotAuth", description = "testNotAuth")
	@RequestLogger
	@GetMapping("/test/se")
	public Result<Boolean> testNotAuth() {

		List<DubboMenuQueryRequest> allById = service().findAllById(1L);
		return Result.success(true);
	}

	@Operation(summary = "测试分布式事务", description = "测试分布式事务")
	@RequestLogger
	@GetMapping("/test/pe")
	@PreAuthorize("@pms.hasPermission(#request, authentication, 'export')")
	//@PreAuthorize("hasPermission(#request, 'batch')")
	public Result<Boolean> testPermissionVerifier(HttpServletRequest request) {
		return Result.success(true);
	}

	//@Operation(summary = "测试异步", description = "测试异步")
	//@RequestLogger("测试异步")
	//@GetMapping("/test/async")
	//public Result<Boolean> testAsync() throws ExecutionException, InterruptedException {
	//	Future<Boolean> result = service().testAsync();
	//	return Result.success(result.get());
	//}
	//
	//@Operation(summary = "测试异步结果", description = "测试异步结果")
	//@RequestLogger("测试异步结果")
	//@GetMapping("/test/async/future")
	//public Future<Boolean> testAsyncFuture() {
	//	return service().testAsync();
	//}
	//
	//@Operation(summary = "测试分布式事务", description = "测试分布式事务")
	//@RequestLogger("测试分布式事务")
	//@GetMapping("/test/seata")
	//@SentinelResource(value = "testSeata", blockHandler = "testSeataException")
	//public Result<Boolean> testSeata(HttpServletRequest request, HttpServletResponse response) {
	//	Boolean result = service().testSeata();
	//	return Result.success(result);
	//}

	public Result<Boolean> testSeataException(BlockException e) {
		e.printStackTrace();
		LogUtils.error(" 该接口已经被限流啦", e);
		return Result.fail("该接口已经被限流啦");
	}


	//************************************************资源*************************************************************

}
