package com.taotao.cloud.payment.biz.controller.manger;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 管理端,收款日志接口
 */
@RestController
@Api(tags = "管理端,收款日志接口")
@RequestMapping("/manager/order/paymentLog")
public class PaymentLogManagerController {

    @Autowired
    private OrderService orderService;


    @GetMapping
    @ApiOperation(value = "分页获取支付日志")
    public Result<IPage<PaymentLog>> getByPage(Order order,
                                                      SearchVO searchVo,
                                                      PageVO page) {
        return Result.success(orderService.queryPaymentLogs(
	        PageUtil.initPage(page), PageUtil.initWrapper(order, searchVo)));
    }
}
