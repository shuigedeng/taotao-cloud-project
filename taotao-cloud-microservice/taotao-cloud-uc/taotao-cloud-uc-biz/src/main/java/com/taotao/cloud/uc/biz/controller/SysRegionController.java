package com.taotao.cloud.uc.biz.controller;

import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.uc.api.vo.QueryRegionByParentIdVO;
import com.taotao.cloud.uc.biz.service.SysRegionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "地区管理API")
@RestController
@RequestMapping("/region")
public class SysRegionController {

	@Autowired
	private SysRegionService sysRegionService;

	@ApiOperation(value = "根据父id查询", httpMethod = "GET")
	@ApiParam(value = "parentId", name = "父id")
	@ApiOperationSupport(author = "dengtao")
	@GetMapping("/parentId")
	public Result<List<QueryRegionByParentIdVO>> queryRegionByParentId(
		@RequestParam(value = "parentId", defaultValue = "1") Long parentId) {
		List<QueryRegionByParentIdVO> result = sysRegionService.queryRegionByParentId(parentId);
		return Result.success(result);
	}

	@ApiOperation(value = "树形结构查询")
	@ApiOperationSupport(author = "dengtao")
	@RequestMapping(value = "/tree", method = {RequestMethod.POST, RequestMethod.GET})
	public Result<List<QueryRegionByParentIdVO>> tree() {
		List<QueryRegionByParentIdVO> result = sysRegionService.tree();
		return Result.success(result);
	}
}
