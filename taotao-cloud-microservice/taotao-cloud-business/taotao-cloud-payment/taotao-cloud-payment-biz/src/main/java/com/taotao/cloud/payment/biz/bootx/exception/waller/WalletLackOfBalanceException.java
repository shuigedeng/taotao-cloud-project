package com.taotao.cloud.payment.biz.bootx.exception.waller;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.payment.biz.bootx.code.PaymentCenterErrorCode;

/**   
* 余额不足异常
* @author xxm  
* @date 2020/12/8 
*/
public class WalletLackOfBalanceException extends BaseException {
    public WalletLackOfBalanceException() {
        super(PaymentCenterErrorCode.WALLET_BALANCE_NOT_ENOUGH, "余额不足异常");
    }
}
