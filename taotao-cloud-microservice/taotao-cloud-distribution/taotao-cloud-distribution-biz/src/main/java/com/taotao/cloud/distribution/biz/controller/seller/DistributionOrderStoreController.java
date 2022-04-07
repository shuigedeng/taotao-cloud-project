package com.taotao.cloud.distribution.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.distribution.api.vo.DistributionOrderSearchParams;
import com.taotao.cloud.distribution.biz.entity.DistributionOrder;
import com.taotao.cloud.distribution.biz.service.DistributionOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 店铺端,分销订单接口
 */
@RestController
@Api(tags = "店铺端,分销订单接口")
@RequestMapping("/store/distribution/order")
public class DistributionOrderStoreController {

    /**
     * 分销订单
     */
    @Autowired
    private DistributionOrderService distributionOrderService;

    @ApiOperation(value = "获取分销订单列表")
    @GetMapping
    public Result<IPage<DistributionOrder>> distributionOrder(
	    DistributionOrderSearchParams distributionOrderSearchParams) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        //获取当前登录商家账号-查询当前店铺的分销订单
        distributionOrderSearchParams.setStoreId(storeId);
        //查询分销订单列表
        IPage<DistributionOrder> distributionOrderPage = distributionOrderService.getDistributionOrderPage(distributionOrderSearchParams);
        return Result.success(distributionOrderPage);
    }

}
