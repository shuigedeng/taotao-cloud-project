package com.taotao.cloud.payment.biz.daxpay.single.core.exception;

import com.taotao.cloud.payment.biz.daxpay.core.code.DaxPayErrorCode;

/**
 * 验签失败
 * @author xxm
 * @since 2024/6/17
 */
public class VerifySignFailedException extends PayFailureException{

    public VerifySignFailedException(String message) {
        super(DaxPayErrorCode.VERIFY_SIGN_FAILED,message);
    }

    public VerifySignFailedException() {
        super(DaxPayErrorCode.VERIFY_SIGN_FAILED,"验签失败");
    }
}
