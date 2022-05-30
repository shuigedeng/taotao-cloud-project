package com.taotao.cloud.payment.biz.controller.manger;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 管理端,收款日志接口
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-收款日志接口", description = "管理端-收款日志接口")
@RequestMapping("/manager/order/paymentLog")
public class PaymentLogManagerController {

    @Autowired
    private OrderService orderService;

	@Operation(summary = "分页获取支付日志", description = "分页获取支付日志")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping
    public Result<IPage<PaymentLog>> getByPage(Order order,
                                                      SearchVO searchVo,
                                                      PageVO page) {
        return Result.success(orderService.queryPaymentLogs(
	        PageUtil.initPage(page), PageUtil.initWrapper(order, searchVo)));
    }
}
