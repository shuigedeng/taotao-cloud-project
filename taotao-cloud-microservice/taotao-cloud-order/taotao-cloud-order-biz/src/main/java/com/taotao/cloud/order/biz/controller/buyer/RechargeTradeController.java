package com.taotao.cloud.order.biz.controller.buyer;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.wallet.entity.dos.Recharge;
import cn.lili.modules.wallet.service.RechargeService;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 买家端,预存款充值记录接口
 */
@Validated
@RestController
@RequestMapping("/sys/manager/dept")
@Tag(name = "买家端-部门管理API", description = "买家端-部门管理API")

@RestController
@Api(tags = "买家端,预存款充值记录接口")
@RequestMapping("/order/buyer/trade/recharge")
@Transactional(rollbackFor = Exception.class)
public class RechargeTradeController {

    @Autowired
    private RechargeService rechargeService;
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @PostMapping
    @ApiOperation(value = "创建余额充值订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "price", value = "充值金额", required = true, dataType = "double", paramType = "query")
    })
    public ResultMessage<Recharge> create(@Max(value = 10000, message = "充值金额单次最多允许充值10000元") @Min(value = 1, message = "充值金额单次最少充值金额为1元") Double price) {
        Recharge recharge = this.rechargeService.recharge(price);
        return ResultUtil.data(recharge);
    }

}
