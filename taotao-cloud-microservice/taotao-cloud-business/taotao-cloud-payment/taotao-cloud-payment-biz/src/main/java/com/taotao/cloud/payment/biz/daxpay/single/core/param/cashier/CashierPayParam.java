package com.taotao.cloud.payment.biz.daxpay.single.core.param.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

import java.math.BigDecimal;

/**
 * 通道收银支付参数
 * @author xxm
 * @since 2024/9/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "通道收银支付参数")
public class CashierPayParam {

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "支付金额")
    private BigDecimal amount;

    @Schema(description = "收银台类型")
    private String cashierType;

    @Schema(description = "唯一标识")
    private String openId;

    @Schema(description = "描述")
    private String description;
}
