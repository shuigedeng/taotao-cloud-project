package com.taotao.cloud.payment.biz.daxpay.single.unisdk.common.exception;

import com.taotao.cloud.payment.biz.daxpay.unisdk.common.bean.result.PayError;

/**
 * @author  egan
 *  <pre>
 * email egzosn@gmail.com
 * date 2016-5-18 14:09:01
 *  </pre>
 */
public class PayErrorException extends RuntimeException  {

    private PayError error;

    public PayErrorException(PayError error) {
        super(error.getString());
        this.error = error;
    }

    public PayErrorException(PayError error, Throwable throwable) {
        super(error.getString(), throwable);
        this.error = error;
    }


    public PayError getPayError() {
        return error;
    }
}
