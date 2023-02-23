package com.taotao.cloud.payment.biz.bootx.exception.waller;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.payment.biz.bootx.code.PaymentCenterErrorCode;

/**   
* 钱包日志错误
* @author xxm  
* @date 2020/12/8 
*/
public class WalletLogError extends BaseException {

    public WalletLogError() {
        super(PaymentCenterErrorCode.WALLET_LOG_ERROR, "钱包日志错误");
    }
}
