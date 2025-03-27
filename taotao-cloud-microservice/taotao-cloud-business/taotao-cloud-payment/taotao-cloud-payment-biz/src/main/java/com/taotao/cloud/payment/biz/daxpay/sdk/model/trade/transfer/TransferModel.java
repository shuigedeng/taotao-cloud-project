package com.taotao.cloud.payment.biz.daxpay.sdk.model.trade.transfer;

import cn.daxpay.single.sdk.code.TransferStatusEnum;
import lombok.Data;
import lombok.experimental.*;

/**
 * 转账结果
 * @author xxm
 * @since 2024/6/19
 */
@Data
public class TransferModel{

    /** 商户转账号 */
    private String bizTransferNo;

    /** 转账号 */
    private String transferNo;

    /**
     * 状态
     * @see TransferStatusEnum
     */
    private String status;


    /**
     * 提示信息
     */
    private String errorMsg;
}
