package com.taotao.cloud.payment.biz.bootx.exception.waller;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.payment.biz.bootx.code.PaymentCenterErrorCode;

/**   
* 钱包被禁用
* @author xxm  
* @date 2020/12/8 
*/
public class WalletBannedException extends BaseException {

    public WalletBannedException() {
        super(PaymentCenterErrorCode.WALLET_BANNED, "钱包被禁用");
    }
}
