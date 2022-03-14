package com.taotao.cloud.payment.biz.kit.plugin.wechat.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统一下单-结算信息
 *
 */
@Data
@Accessors(chain = true)
public class SettleInfo {
    /**
     * 是否指定分账
     */
    private boolean profit_sharing;
    /**
     * 补差金额
     */
    private int subsidy_amount;
}
