package com.taotao.cloud.order.api.vo.aftersale;

import com.taotao.cloud.order.api.enums.trade.AfterSaleRefundWayEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 售后表
 */
@Data
@Builder
@Schema(description = "售后VO")
public class AfterSaleVO {

	@Schema(description = "售后服务单号")
	private String sn;

	@Schema(description = "订单编号")
	private String orderSn;

	@Schema(description = "订单货物编号")
	private String orderItemSn;

	@Schema(description = "交易编号")
	private String tradeSn;

	@Schema(description = "会员ID")
	private String memberId;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "商家ID")
	private String storeId;

	@Schema(description = "商家名称")
	private String storeName;

	//商品信息
	@Schema(description = "商品ID")
	private String goodsId;

	@Schema(description = "货品ID")
	private String skuId;

	@Schema(description = "申请数量")
	private Integer num;

	@Schema(description = "商品图片")
	private String goodsImage;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "规格json")
	private String specs;

	@Schema(description = "实际金额")
	private BigDecimal flowPrice;

	//交涉信息
	@Schema(description = "申请原因")
	private String reason;

	@Schema(description = "问题描述")
	private String problemDesc;

	@Schema(description = "评价图片")
	private String afterSaleImage;

	/**
	 * @see AfterSaleTypeEnum
	 */
	@Schema(description = "售后类型", allowableValues = "RETURN_GOODS,RETURN_MONEY")
	private String serviceType;

	/**
	 * @see AfterSaleStatusEnum
	 */
	@Schema(description = "售后单状态", allowableValues = "APPLY,PASS,REFUSE,BUYER_RETURN,SELLER_RE_DELIVERY,BUYER_CONFIRM,SELLER_CONFIRM,COMPLETE")
	private String serviceStatus;

	//退款信息

	/**
	 * @see AfterSaleRefundWayEnum
	 */
	@Schema(description = "退款方式", allowableValues = "ORIGINAL,OFFLINE")
	private String refundWay;

	@Schema(description = "账号类型", allowableValues = "ALIPAY,WECHATPAY,BANKTRANSFER")
	private String accountType;

	@Schema(description = "银行账户")
	private String bankAccountNumber;

	@Schema(description = "银行开户名")
	private String bankAccountName;

	@Schema(description = "银行开户行")
	private String bankDepositName;

	@Schema(description = "商家备注")
	private String auditRemark;

	@Schema(description = "订单支付方式返回的交易号")
	private String payOrderNo;

	@Schema(description = "申请退款金额")
	private BigDecimal applyRefundPrice;

	@Schema(description = "实际退款金额")
	private BigDecimal actualRefundPrice;

	@Schema(description = "退还积分")
	private Integer refundPoint;

	@Schema(description = "退款时间")
	private LocalDateTime refundTime;

	/**
	 * 买家物流信息
	 */
	@Schema(description = "发货单号")
	private String mLogisticsNo;

	@Schema(description = "物流公司CODE")
	private String mLogisticsCode;

	@Schema(description = "物流公司名称")
	private String mLogisticsName;

	@Schema(description = "买家发货时间")
	private LocalDateTime mDeliverTime;

	/**
	 * 初始化自身状态
	 */
	public AfterSaleAllowOperation getAfterSaleAllowOperationVO() {
		//设置订单的可操作状态
		return new AfterSaleAllowOperation(this);
	}
}
