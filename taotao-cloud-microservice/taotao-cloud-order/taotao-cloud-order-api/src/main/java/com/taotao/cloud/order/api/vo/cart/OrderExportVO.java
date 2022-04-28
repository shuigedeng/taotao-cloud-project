package com.taotao.cloud.order.api.vo.cart;

import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单导出VO
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@Builder
@Schema(description = "订单导出DTO")
public class OrderExportVO {

	@Schema(description = "订单编号")
	private String sn;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "用户名")
	private String memberName;

	@Schema(description = "收件人姓名")
	private String consigneeName;

	@Schema(description = "收件人手机")
	private String consigneeMobile;

	@Schema(description = "收件人地址")
	private String consigneeAddressPath;

	@Schema(description = "详细地址")
	private String consigneeDetail;

	@Schema(description = "支付方式")
	private String paymentMethod;

	@Schema(description = "物流公司名称")
	private String logisticsName;

	@Schema(description = "运费")
	private BigDecimal freightPrice;

	@Schema(description = "商品价格")
	private BigDecimal goodsPrice;

	@Schema(description = "优惠的金额")
	private BigDecimal discountPrice;

	@Schema(description = "总价格")
	private BigDecimal flowPrice;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品数量")
	private Integer num;

	@Schema(description = "买家订单备注")
	private String remark;

	/**
	 * @see OrderStatusEnum
	 */
	@Schema(description = "订单状态")
	private String orderStatus;

	/**
	 * @see PayStatusEnum
	 */
	@Schema(description = "付款状态")
	private String payStatus;

	/**
	 * @see DeliverStatusEnum
	 */
	@Schema(description = "货运状态")
	private String deliverStatus;

	@Schema(description = "是否需要发票")
	private Boolean needReceipt;

	@Schema(description = "店铺名称")
	private String storeName;
}
