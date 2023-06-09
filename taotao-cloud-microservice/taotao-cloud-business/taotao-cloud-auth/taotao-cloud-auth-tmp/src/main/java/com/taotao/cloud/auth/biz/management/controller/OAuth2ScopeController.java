/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.management.controller;

import com.taotao.cloud.auth.biz.management.dto.OAuth2PermissionDto;
import com.taotao.cloud.auth.biz.management.dto.OAuth2ScopeDto;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Permission;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Scope;
import com.taotao.cloud.auth.biz.management.service.OAuth2ScopeService;
import com.taotao.cloud.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p> Description : OauthScopesController </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/3/25 17:10
 */
@RestController
@RequestMapping("/authorize/scope")
@Tags({
	@Tag(name = "OAuth2 认证服务接口"),
	@Tag(name = "OAuth2 权限范围管理接口")
})
public class OAuth2ScopeController {

	private final OAuth2ScopeService scopeService;

	@Autowired
	public OAuth2ScopeController(OAuth2ScopeService scopeService) {
		this.scopeService = scopeService;
	}


	@Operation(summary = "给Scope分配权限", description = "给Scope分配权限",
		responses = {
			@ApiResponse(description = "查询到的角色", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OAuth2ScopeDto.class))),
		})
	@Parameters({
		@Parameter(name = "scope", required = true, description = "范围请求参数"),
	})
	@PostMapping("/assigned")
	public Result<OAuth2Scope> assigned(@RequestBody OAuth2ScopeDto scope) {

		Set<OAuth2Permission> permissions = new HashSet<>();
		if (CollectionUtils.isNotEmpty(scope.getPermissions())) {
			permissions = scope.getPermissions().stream().map(this::toEntity).collect(Collectors.toSet());
		}

		OAuth2Scope result = scopeService.assigned(scope.getScopeId(), permissions);
		return Result.success(result);
	}

//	@AccessLimited
	@Operation(summary = "获取全部范围", description = "获取全部范围", responses = {
		@ApiResponse(description = "全部数据列表", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))),
		@ApiResponse(responseCode = "204", description = "查询成功，未查到数据"),
		@ApiResponse(responseCode = "500", description = "查询失败")
	})
	@GetMapping("/list")
	public Result<List<OAuth2Scope>> findAll() {
		List<OAuth2Scope> oAuth2Scopes = scopeService.findAll();
		return Result.success(oAuth2Scopes);
	}

//	@AccessLimited
	@Operation(summary = "根据范围代码查询应用范围", description = "根据范围代码查询应用范围",
		responses = {
			@ApiResponse(description = "查询到的应用范围", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OAuth2Scope.class))),
			@ApiResponse(responseCode = "204", description = "查询成功，未查到数据"),
			@ApiResponse(responseCode = "500", description = "查询失败")
		}
	)
	@GetMapping("/{scopeCode}")
	public Result<OAuth2Scope> findByUserName(@PathVariable("scopeCode") String scopeCode) {
		OAuth2Scope scope = scopeService.findByScopeCode(scopeCode);
		return Result.success(scope);
	}

	private OAuth2Permission toEntity(OAuth2PermissionDto dto) {
		OAuth2Permission entity = new OAuth2Permission();
		entity.setPermissionId(dto.getPermissionId());
		entity.setPermissionCode(dto.getPermissionCode());
		entity.setPermissionName(dto.getPermissionName());
		return entity;
	}

}
