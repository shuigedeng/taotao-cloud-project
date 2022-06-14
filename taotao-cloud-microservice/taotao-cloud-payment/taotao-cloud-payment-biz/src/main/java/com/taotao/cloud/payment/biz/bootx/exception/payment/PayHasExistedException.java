package com.taotao.cloud.payment.biz.bootx.exception.payment;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.payment.biz.bootx.code.PaymentCenterErrorCode;

/**
* 付款已存在
* @author xxm  
* @date 2020/12/8 
*/
public class PayHasExistedException extends BaseException {

    public PayHasExistedException() {
        super(PaymentCenterErrorCode.PAYMENT_HAS_EXISTED, "付款已存在");
    }
}
