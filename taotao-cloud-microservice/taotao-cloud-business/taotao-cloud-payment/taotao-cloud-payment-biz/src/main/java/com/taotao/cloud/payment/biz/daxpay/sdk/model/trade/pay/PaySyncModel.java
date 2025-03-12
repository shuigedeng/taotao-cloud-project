package com.taotao.cloud.payment.biz.daxpay.sdk.model.trade.pay;

import cn.daxpay.single.sdk.code.PaySyncStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 交易同步结果
 * @author xxm
 * @since 2023/12/27
 */
@Data
public class PaySyncModel{

    /**
     * 同步结果
     * @see PaySyncStatusEnum
     */
    private String status;

}
