package com.taotao.cloud.uc.biz.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.uc.api.vo.QueryRegionByParentIdVO;
import com.taotao.cloud.uc.biz.service.SysRegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/region")
@Tag(name = "SysRegionController", description = "地区管理API")
public class SysRegionController {

	@Autowired
	private SysRegionService sysRegionService;

	@Operation(summary = "根据父id查询", description = "根据父id查询", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据父id查询")
	@GetMapping("/parentId")
	public Result<List<QueryRegionByParentIdVO>> queryRegionByParentId(
		@Parameter(name = "parentId", description = "父id", required = true, in = ParameterIn.QUERY)
		@RequestParam(value = "parentId", defaultValue = "1") Long parentId) {
		List<QueryRegionByParentIdVO> result = sysRegionService.queryRegionByParentId(parentId);
		return Result.success(result);
	}

	@Operation(summary = "树形结构查询", description = "树形结构查询", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据父id查询")
	@GetMapping(value = "/tree")
	public Result<List<QueryRegionByParentIdVO>> tree() {
		List<QueryRegionByParentIdVO> result = sysRegionService.tree();
		return Result.success(result);
	}
}
