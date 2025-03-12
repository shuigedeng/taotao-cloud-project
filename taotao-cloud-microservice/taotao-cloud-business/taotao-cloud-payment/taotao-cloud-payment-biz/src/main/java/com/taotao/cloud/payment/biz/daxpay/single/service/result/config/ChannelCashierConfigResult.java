package com.taotao.cloud.payment.biz.daxpay.single.service.result.config;

import com.taotao.cloud.payment.biz.daxpay.core.enums.CashierTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.core.enums.PayMethodEnum;
import com.taotao.cloud.payment.biz.daxpay.core.result.MchAppResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通道收银台配置
 * @author xxm
 * @since 2024/9/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "通道收银台配置")
public class ChannelCashierConfigResult extends MchAppResult {

    /**
     * 收银台类型
     * @see CashierTypeEnum
     */
    @Schema(description = "收银台类型")
    private String cashierType;

    /**
     * 收银台名称
     */
    @Schema(description = "收银台名称")
    private String cashierName;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "支付方式")
    private String payMethod;


    /** 是否开启分账 */
    @Schema(description = "是否开启分账")
    private Boolean allocation;

    /** 自动分账 */
    @Schema(description = "自动分账")
    private Boolean autoAllocation;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
