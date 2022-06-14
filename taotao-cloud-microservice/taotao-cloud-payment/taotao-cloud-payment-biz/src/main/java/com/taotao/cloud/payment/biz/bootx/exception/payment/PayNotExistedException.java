package com.taotao.cloud.payment.biz.bootx.exception.payment;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.payment.biz.bootx.code.PaymentCenterErrorCode;

/**
* 付款记录不存在
* @author xxm  
* @date 2020/12/8 
*/
public class PayNotExistedException extends BaseException {

    public PayNotExistedException() {
        super(PaymentCenterErrorCode.PAYMENT_RECORD_NOT_EXISTED, "付款记录不存在");
    }
}
