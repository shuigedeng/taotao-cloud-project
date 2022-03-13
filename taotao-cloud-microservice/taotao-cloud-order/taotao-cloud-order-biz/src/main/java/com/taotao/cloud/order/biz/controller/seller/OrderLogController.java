package com.taotao.cloud.order.biz.controller.seller;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.order.order.service.OrderService;
import cn.lili.modules.order.trade.entity.dos.OrderLog;
import cn.lili.modules.order.trade.service.OrderLogService;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 店铺端,订单日志接口
 *
 *
 * @since 2020/12/5
 **/

@Validated
@RestController
@RequestMapping("/sys/manager/dept")
@Tag(name = "店铺端-部门管理API", description = "店铺端-部门管理API")

@RestController
@Api(tags = "店铺端,订单日志接口")
@RequestMapping("/order/seller/orderLog")
public class OrderLogController {

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private OrderService orderService;
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "通过订单编号获取订单日志")
    @ApiImplicitParam(name = "orderSn", value = "订单编号", required = true, paramType = "path")
    @GetMapping(value = "/{orderSn}")
    public ResultMessage<List<OrderLog>> get(@PathVariable String orderSn) {
        OperationalJudgment.judgment(orderService.getBySn(orderSn));
        return ResultUtil.data(orderLogService.getOrderLog(orderSn));
    }
}
