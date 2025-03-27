package com.taotao.cloud.payment.biz.daxpay.single.service.bo.reconcile;

import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

import java.util.List;

/**
 * 对账文件解析记录
 * @author xxm
 * @since 2024/8/6
 */
@Data
@Accessors(chain = true)
public class ReconcileResolveResultBo {

    /**
     * 通道交易明细
     */
    private List<ChannelReconcileTradeBo> channelTrades;

    /**
     * 原始对账文件URL
     */
    private String originalFileUrl;

}
