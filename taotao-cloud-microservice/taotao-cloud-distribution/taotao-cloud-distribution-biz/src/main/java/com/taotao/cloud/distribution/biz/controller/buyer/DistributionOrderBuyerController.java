package com.taotao.cloud.distribution.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.distribution.api.vo.DistributionOrderSearchParams;
import com.taotao.cloud.distribution.biz.entity.DistributionOrder;
import com.taotao.cloud.distribution.biz.service.DistributionOrderService;
import com.taotao.cloud.distribution.biz.service.DistributionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 买家端,分销商品佣金提现接口
 */
@RestController
@Api(tags = "买家端,分销订单接口")
@RequestMapping("/buyer/distribution/order")
public class DistributionOrderBuyerController {

    /**
     * 分销订单
     */
    @Autowired
    private DistributionOrderService distributionOrderService;
    /**
     * 分销员
     */
    @Autowired
    private DistributionService distributionService;


    @ApiOperation(value = "分销员订单")
    @GetMapping
    public Result<IPage<DistributionOrder>> casHistory(
	    DistributionOrderSearchParams distributionOrderSearchParams) {
        //获取当前登录的分销员
        distributionOrderSearchParams.setDistributionId(distributionService.getDistribution().getId());
        return Result.success(distributionOrderService.getDistributionOrderPage(distributionOrderSearchParams));
    }


}
