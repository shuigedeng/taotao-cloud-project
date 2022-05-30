package com.taotao.cloud.order.api.dto.aftersale;

import com.taotao.cloud.order.api.enums.trade.AfterSaleRefundWayEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * 售后dto
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:16:46
 */
@RecordBuilder
@Schema(description = "售后dto")
public record AfterSaleDTO(
	@Schema(description = "订单SN")
	String orderItemSn,

	@Schema(description = "商品ID")
	String goodsId,

	@Schema(description = "货品ID")
	String skuId,

	@Schema(description = "申请退款金额")
	BigDecimal applyRefundPrice,

	@Schema(description = "申请数量")
	Integer num,

	@Schema(description = "申请原因")
	String reason,

	@Schema(description = "问题描述")
	String problemDesc,

	@Schema(description = "售后图片")
	String images,

	/**
	 * @see AfterSaleTypeEnum
	 */
	@Schema(description = "售后类型", allowableValues = "RETURN_GOODS,EXCHANGE_GOODS,RETURN_MONEY")
	String serviceType,

	/**
	 * @see AfterSaleRefundWayEnum
	 */
	@Schema(description = "退款方式", allowableValues = "ORIGINAL,OFFLINE")
	String refundWay,

	@Schema(description = "账号类型", allowableValues = "ALIPAY,WECHATPAY,BANKTRANSFER")
	String accountType,

	@Schema(description = "银行开户行")
	String bankDepositName,

	@Schema(description = "银行开户名")
	String bankAccountName,

	@Schema(description = "银行卡号")
	String bankAccountNumber
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;


}
