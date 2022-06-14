package com.taotao.cloud.payment.biz.bootx.exception.payment;

import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.payment.biz.bootx.code.PaymentCenterErrorCode;

/**   
* 异常金额
* @author xxm  
* @date 2020/12/8 
*/
public class PayAmountAbnormalException extends BaseException {

    public PayAmountAbnormalException(String msg) {
        super(PaymentCenterErrorCode.PAYMENT_AMOUNT_ABNORMAL, msg);
    }
    public PayAmountAbnormalException() {
        super(PaymentCenterErrorCode.PAYMENT_AMOUNT_ABNORMAL, "异常金额");
    }
}
