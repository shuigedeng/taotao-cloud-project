package com.taotao.cloud.distribution.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.distribution.api.dto.DistributionApplyDTO;
import com.taotao.cloud.distribution.api.vo.DistributionOrderSearchParams;
import com.taotao.cloud.distribution.biz.entity.Distribution;
import com.taotao.cloud.distribution.biz.entity.DistributionOrder;
import com.taotao.cloud.distribution.biz.service.DistributionOrderService;
import com.taotao.cloud.distribution.biz.service.DistributionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 买家端,分销员接口
 */
@RestController
@Api(tags = "买家端,分销员接口")
@RequestMapping("/buyer/distribution/distribution")
public class DistributionBuyerController {

    /**
     * 分销员
     */
    @Autowired
    private DistributionService distributionService;
    /**
     * 分销员订单
     */
    @Autowired
    private DistributionOrderService distributionOrderService;

    @ApiOperation(value = "申请分销员")
    @PostMapping
    public Result<Object> applyDistribution(DistributionApplyDTO distributionApplyDTO) {
        return Result.success(distributionService.applyDistribution(distributionApplyDTO));
    }

    @ApiOperation(value = "获取分销员分页订单列表")
    @GetMapping("/distributionOrder")
    public Result<IPage<DistributionOrder>> distributionOrderPage(
	    DistributionOrderSearchParams distributionOrderSearchParams) {
        distributionOrderSearchParams.setDistributionId(distributionService.getDistribution().getId());
        return Result.success(distributionOrderService.getDistributionOrderPage(distributionOrderSearchParams));
    }

    @ApiOperation(value = "获取当前会员的分销员信息", notes = "可根据分销员信息查询待提现金额以及冻结金额等信息")
    @GetMapping
    public Result<Distribution> getDistribution() {
        //检查分销开关
        distributionService.checkDistributionSetting();
        return Result.success(distributionService.getDistribution());
    }

    @ApiOperation(value = "绑定分销员")
    @ApiImplicitParam(name = "distributionId", value = "分销员ID", required = true, paramType = "path")
    @GetMapping("/bindingDistribution/{distributionId}")
    public Result<Boolean> bindingDistribution(@PathVariable String distributionId){
        distributionService.bindingDistribution(distributionId);
        return Result.success(true);
    }
}
