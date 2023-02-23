package com.taotao.cloud.distribution.biz.api.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.distribution.api.model.query.DistributionOrderPageQuery;
import com.taotao.cloud.distribution.biz.model.entity.DistributionOrder;
import com.taotao.cloud.distribution.biz.service.IDistributionOrderService;
import com.taotao.cloud.distribution.biz.service.IDistributionService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 买家端,分销商品佣金提现接口
 */
@Validated
@RestController
@Tag(name = "买家端-分销订单接口", description = "买家端-分销订单接口")
@RequestMapping("/buyer/distribution/order")
public class DistributionOrderBuyerController {

	/**
	 * 分销订单
	 */
	@Autowired
	private IDistributionOrderService distributionOrderService;
	/**
	 * 分销员
	 */
	@Autowired
	private IDistributionService distributionService;

	@Operation(summary = "分销员订单", description = "分销员订单")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<IPage<DistributionOrder>> casHistory(
		DistributionOrderPageQuery distributionOrderPageQuery) {
		//获取当前登录的分销员
		distributionOrderPageQuery.setDistributionId(distributionService.getDistribution().getId());
		return Result.success(
			distributionOrderService.getDistributionOrderPage(distributionOrderPageQuery));
	}


}
