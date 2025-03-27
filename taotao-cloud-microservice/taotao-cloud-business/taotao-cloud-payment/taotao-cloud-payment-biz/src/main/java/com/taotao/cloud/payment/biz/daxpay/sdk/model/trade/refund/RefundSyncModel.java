package com.taotao.cloud.payment.biz.daxpay.sdk.model.trade.refund;

import cn.daxpay.single.sdk.code.RefundSyncStatusEnum;
import lombok.Data;
import lombok.experimental.*;

/**
 * 交易同步结果
 * @author xxm
 * @since 2023/12/27
 */
@Data
public class RefundSyncModel{

    /**
     * 同步结果
     * @see RefundSyncStatusEnum
     */
    private String status;

}
