package com.taotao.cloud.payment.biz.bootx.exception.payment;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.payment.biz.bootx.code.PaymentCenterErrorCode;

/**
* 付款正在处理中
* @author xxm  
* @date 2020/12/8 
*/
public class PayIsProcessingException extends BaseException {

    public PayIsProcessingException() {
        super(PaymentCenterErrorCode.PAYMENT_IS_PROCESSING, "付款正在处理中");
    }
}
