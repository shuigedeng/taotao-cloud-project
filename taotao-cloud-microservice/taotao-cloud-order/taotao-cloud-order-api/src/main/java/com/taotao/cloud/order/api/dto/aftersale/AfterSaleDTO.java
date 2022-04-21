package com.taotao.cloud.order.api.dto.aftersale;

import com.taotao.cloud.order.api.enums.trade.AfterSaleRefundWayEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 售后dto
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@Builder
@Schema(description = "售后dto")
public class AfterSaleDTO {

	@Schema(description = "订单SN")
	private String orderItemSn;

	@Schema(description = "商品ID")
	private String goodsId;

	@Schema(description = "货品ID")
	private String skuId;

	@Schema(description = "申请退款金额")
	private BigDecimal applyRefundPrice;

	@Schema(description = "申请数量")
	private Integer num;

	@Schema(description = "申请原因")
	private String reason;

	@Schema(description = "问题描述")
	private String problemDesc;

	@Schema(description = "售后图片")
	private String images;

	/**
	 * @see AfterSaleTypeEnum
	 */
	@Schema(description = "售后类型", allowableValues = "RETURN_GOODS,EXCHANGE_GOODS,RETURN_MONEY")
	private String serviceType;

	/**
	 * @see AfterSaleRefundWayEnum
	 */
	@Schema(description = "退款方式", allowableValues = "ORIGINAL,OFFLINE")
	private String refundWay;

	@Schema(description = "账号类型", allowableValues = "ALIPAY,WECHATPAY,BANKTRANSFER")
	private String accountType;

	@Schema(description = "银行开户行")
	private String bankDepositName;

	@Schema(description = "银行开户名")
	private String bankAccountName;

	@Schema(description = "银行卡号")
	private String bankAccountNumber;
}
