package com.taotao.cloud.payment.biz.kit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

/**
 * 支付参数
 */
@Data
@ToString
public class PayParam {


	@NotNull
	@Schema(description = "交易类型", allowableValues = "TRADE,ORDER,RECHARGE")
	private String orderType;

	@NotNull
	@Schema(description = "订单号")
	private String sn;

	@NotNull
	@Schema(description = "客户端类型")
	private String clientType;


}
