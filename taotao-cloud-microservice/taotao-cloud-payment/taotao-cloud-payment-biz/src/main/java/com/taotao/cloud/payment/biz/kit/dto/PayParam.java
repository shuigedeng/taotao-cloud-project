package com.taotao.cloud.payment.biz.kit.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 支付参数
 */
@Data
@ToString
public class PayParam {


    @NotNull
    @Schema(description =  "交易类型", allowableValues = "TRADE,ORDER,RECHARGE")
    private String orderType;

    @NotNull
    @Schema(description =  "订单号")
    private String sn;

    @NotNull
    @Schema(description =  "客户端类型")
    private String clientType;



}
