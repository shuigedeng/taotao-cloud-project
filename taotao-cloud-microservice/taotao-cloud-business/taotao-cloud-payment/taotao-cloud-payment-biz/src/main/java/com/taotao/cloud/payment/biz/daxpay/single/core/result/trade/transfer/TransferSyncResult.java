package com.taotao.cloud.payment.biz.daxpay.single.core.result.trade.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

/**
 * 转账同步结果
 * @author xxm
 * @since 2024/6/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "转账同步结果")
public class TransferSyncResult {

    /**
     * 转账状态
     * @see com.taotao.cloud.payment.biz.daxpay.core.enums.TransferStatusEnum
     */
    @Schema(description = "转账状态")
    private String orderStatus;

    /**
     * 是否触发了调整
     */
    @Schema(description = "是否触发了调整")
    private boolean adjust;
}
