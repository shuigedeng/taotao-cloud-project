package com.taotao.cloud.distribution.biz.api.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.distribution.api.model.query.DistributionOrderPageQuery;
import com.taotao.cloud.distribution.biz.model.entity.DistributionOrder;
import com.taotao.cloud.distribution.biz.service.DistributionOrderService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 店铺端,分销订单接口
 */
@Validated
@RestController
@Tag(name = "店铺端-分销订单接口", description = "店铺端-分销订单接口")
@RequestMapping("/store/distribution/order")
public class DistributionOrderStoreController {

	/**
	 * 分销订单
	 */
	@Autowired
	private DistributionOrderService distributionOrderService;

	@Operation(summary = "获取分销订单列表", description = "获取分销订单列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<IPage<DistributionOrder>> distributionOrder(
		DistributionOrderPageQuery distributionOrderPageQuery) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		//获取当前登录商家账号-查询当前店铺的分销订单
		distributionOrderPageQuery.setStoreId(storeId);
		//查询分销订单列表
		IPage<DistributionOrder> distributionOrderPage = distributionOrderService.getDistributionOrderPage(distributionOrderPageQuery);
		return Result.success(distributionOrderPage);
	}

}
