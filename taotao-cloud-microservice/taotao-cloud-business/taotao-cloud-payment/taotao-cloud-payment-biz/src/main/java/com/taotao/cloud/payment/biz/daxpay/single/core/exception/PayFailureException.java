package com.taotao.cloud.payment.biz.daxpay.single.core.exception;

import cn.bootx.platform.core.exception.BizException;
import com.taotao.cloud.payment.biz.daxpay.core.code.DaxPayErrorCode;

/**
 * 支付错误
 *
 * @author xxm
 * @since 2020/12/8
 */
public class PayFailureException extends BizException {


    public PayFailureException(int code, String message) {
        super(code, message);
    }

    public PayFailureException(String message) {
        super(DaxPayErrorCode.UNCLASSIFIED_ERROR, message);
    }

    public PayFailureException() {
        super(DaxPayErrorCode.UNCLASSIFIED_ERROR, "支付失败");
    }

}
