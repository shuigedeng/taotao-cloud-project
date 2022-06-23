package com.taotao.cloud.distribution.biz.api.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.distribution.api.web.query.DistributionOrderPageQuery;
import com.taotao.cloud.distribution.biz.model.entity.DistributionOrder;
import com.taotao.cloud.distribution.biz.service.DistributionOrderService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,分销订单管理接口
 */
@Validated
@RestController
@Tag(name = "管理端,分销订单管理接口", description = "管理端,分销订单管理接口")
@RequestMapping("/manager/distribution/order")
public class DistributionOrderManagerController {

	@Autowired
	private DistributionOrderService distributionOrderService;

	@Operation(summary = "通过id获取分销订单", description = "通过id获取分销订单")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/get/{id}")
	public Result<DistributionOrder> get(@PathVariable String id) {
		return Result.success(distributionOrderService.getById(id));
	}

	@Operation(summary = "分页获取分销订单", description = "分页获取分销订单")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/getByPage")
	public Result<IPage<DistributionOrder>> getByPage(
		DistributionOrderPageQuery distributionOrderPageQuery) {

		return Result.success(distributionOrderService.getDistributionOrderPage(distributionOrderPageQuery));
	}
}
