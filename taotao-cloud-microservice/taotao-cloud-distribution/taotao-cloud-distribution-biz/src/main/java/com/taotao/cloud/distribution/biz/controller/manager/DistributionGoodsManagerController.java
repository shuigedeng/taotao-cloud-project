package com.taotao.cloud.distribution.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.distribution.api.query.DistributionGoodsPageQuery;
import com.taotao.cloud.distribution.api.vo.DistributionGoodsVO;
import com.taotao.cloud.distribution.biz.service.DistributionGoodsService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理端,分销商品管理接口
 */
@Validated
@RestController
@Tag(name = "管理端-分销商品管理接口", description = "管理端-分销商品管理接口")
@RequestMapping("/manager/distribution/goods")
public class DistributionGoodsManagerController {

	@Autowired
	private DistributionGoodsService distributionGoodsService;

	@Operation(summary = "分页获取", description = "分页获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/getByPage")
	public Result<IPage<DistributionGoodsVO>> getByPage(
		DistributionGoodsPageQuery distributionGoodsPageQuery) {
		return Result.success(distributionGoodsService.goodsPage(distributionGoodsPageQuery));
	}

	@Operation(summary = "批量删除", description = "批量删除")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/delByIds/{ids}")
	public Result<Object> delAllByIds(@PathVariable List ids) {
		distributionGoodsService.removeByIds(ids);
		return Result.success();
	}
}
