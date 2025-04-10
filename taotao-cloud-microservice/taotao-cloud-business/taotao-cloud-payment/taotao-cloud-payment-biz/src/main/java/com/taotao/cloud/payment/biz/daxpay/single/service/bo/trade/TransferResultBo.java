package com.taotao.cloud.payment.biz.daxpay.single.service.bo.trade;

import com.taotao.cloud.payment.biz.daxpay.core.enums.TransferStatusEnum;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

import java.time.LocalDateTime;

/**
 * 转账结果业务类
 * @author xxm
 * @since 2024/7/23
 */
@Data
@Accessors(chain = true)
public class TransferResultBo {
    /** 通道转账订单号 */
    private String outTransferNo;

    /** 状态 */
    private TransferStatusEnum status = TransferStatusEnum.PROGRESS;

    /** 完成时间 */
    private LocalDateTime finishTime;
}
