package com.taotao.cloud.payment.biz.controller.buyer;

import com.taotao.cloud.web.request.annotation.RequestLogger;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import com.taotao.cloud.payment.biz.kit.RefundSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 买家端,退款回调
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-退款回调", description = "买家端-退款回调")
@RequestMapping("/buyer/payment/cashierRefund")
public class CashierRefundController {

	@Autowired
	private RefundSupport refundSupport;

	@Operation(summary = "退款通知", description = "退款通知")
	@RequestLogger
	@PreAuthorize("@el.check('admin','timing:list')")
	@RequestMapping(value = "/notify/{paymentMethod}", method = {RequestMethod.GET, RequestMethod.POST})
	public void notify(HttpServletRequest request, @PathVariable String paymentMethod) {
		PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(paymentMethod);
		refundSupport.notify(paymentMethodEnum, request);
	}
}
