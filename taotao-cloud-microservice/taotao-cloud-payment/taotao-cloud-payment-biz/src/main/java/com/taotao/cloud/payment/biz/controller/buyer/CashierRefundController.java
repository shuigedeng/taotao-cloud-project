package com.taotao.cloud.payment.biz.controller.buyer;

import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import com.taotao.cloud.payment.biz.kit.RefundSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 买家端,退款回调
 */
@Api(tags = "买家端,退款回调")
@RestController
@RequestMapping("/buyer/payment/cashierRefund")
public class CashierRefundController {

    @Autowired
    private RefundSupport refundSupport;


    @ApiOperation(value = "退款通知")
    @RequestMapping(value = "/notify/{paymentMethod}", method = {RequestMethod.GET, RequestMethod.POST})
    public void notify(HttpServletRequest request, @PathVariable String paymentMethod) {
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(paymentMethod);
        refundSupport.notify(paymentMethodEnum, request);
    }
}
