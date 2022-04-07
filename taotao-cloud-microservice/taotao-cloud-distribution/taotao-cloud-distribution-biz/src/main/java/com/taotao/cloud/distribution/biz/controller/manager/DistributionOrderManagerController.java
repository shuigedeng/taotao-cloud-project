package com.taotao.cloud.distribution.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.distribution.api.vo.DistributionOrderSearchParams;
import com.taotao.cloud.distribution.biz.entity.DistributionOrder;
import com.taotao.cloud.distribution.biz.service.DistributionOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,分销订单管理接口
 */
@RestController
@Api(tags = "管理端,分销订单管理接口")
@RequestMapping("/manager/distribution/order")
public class DistributionOrderManagerController {

    @Autowired
    private DistributionOrderService distributionOrderService;

    @ApiOperation(value = "通过id获取分销订单")
    @GetMapping(value = "/get/{id}")
    public Result<DistributionOrder> get(@PathVariable String id) {

        return Result.success(distributionOrderService.getById(id));
    }


    @ApiOperation(value = "分页获取分销订单")
    @GetMapping(value = "/getByPage")
    public Result<IPage<DistributionOrder>> getByPage(
	    DistributionOrderSearchParams distributionOrderSearchParams) {

        return Result.success(distributionOrderService.getDistributionOrderPage(distributionOrderSearchParams));
    }
}
