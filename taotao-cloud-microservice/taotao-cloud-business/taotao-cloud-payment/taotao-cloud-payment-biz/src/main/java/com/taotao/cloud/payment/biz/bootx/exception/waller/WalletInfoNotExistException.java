package com.taotao.cloud.payment.biz.bootx.exception.waller;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.payment.biz.bootx.code.PaymentCenterErrorCode;

/**   
* 钱包信息不存在
* @author xxm  
* @date 2020/12/8 
*/
public class WalletInfoNotExistException extends BaseException {

    public WalletInfoNotExistException() {
        super(PaymentCenterErrorCode.WALLET_INFO_NOT_EXISTS, "钱包信息不存在");
    }
}
