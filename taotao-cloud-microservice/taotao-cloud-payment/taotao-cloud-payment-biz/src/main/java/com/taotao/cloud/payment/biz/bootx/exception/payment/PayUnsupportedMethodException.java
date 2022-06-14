package com.taotao.cloud.payment.biz.bootx.exception.payment;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.payment.biz.bootx.code.PaymentCenterErrorCode;

/**
 * 付款方式不支持异常
* @author xxm
* @date 2020/12/9
*/
public class PayUnsupportedMethodException extends BaseException {

    public PayUnsupportedMethodException() {
        super(PaymentCenterErrorCode.PAYMENT_METHOD_UNSUPPORT, "不支持的支付方式");
    }
}
