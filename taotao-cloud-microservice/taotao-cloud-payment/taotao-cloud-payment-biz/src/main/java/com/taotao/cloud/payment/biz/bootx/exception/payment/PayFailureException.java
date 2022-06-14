package com.taotao.cloud.payment.biz.bootx.exception.payment;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.payment.biz.bootx.code.PaymentCenterErrorCode;

/**
* 付款错误
* @author xxm
* @date 2020/12/8
*/
public class PayFailureException extends BaseException {

    public PayFailureException(String message) {
        super(PaymentCenterErrorCode.PAY_FAILURE, message);
    }
    public PayFailureException() {
        super(PaymentCenterErrorCode.PAY_FAILURE, "支付失败");
    }
}
