package com.taotao.cloud.payment.biz.bootx.exception.waller;

import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.payment.biz.bootx.code.PaymentCenterErrorCode;

/**   
* 钱包不存在
* @author xxm  
* @date 2020/12/8 
*/
public class WalletNotExistsException extends BaseException {

    public WalletNotExistsException() {
        super(PaymentCenterErrorCode.WALLET_NOT_EXISTS, "钱包不存在");
    }
}
