package com.taotao.cloud.payment.biz.daxpay.single.core.exception;

import com.taotao.cloud.payment.biz.daxpay.core.code.DaxPayErrorCode;

/**
 * 对账失败
 * @author xxm
 * @since 2024/6/17
 */
public class ReconciliationFailException extends PayFailureException{

    public ReconciliationFailException(String message) {
        super(DaxPayErrorCode.RECONCILE_FAIL,message);
    }

    public ReconciliationFailException() {
        super(DaxPayErrorCode.RECONCILE_FAIL,"对账失败");
    }
}
