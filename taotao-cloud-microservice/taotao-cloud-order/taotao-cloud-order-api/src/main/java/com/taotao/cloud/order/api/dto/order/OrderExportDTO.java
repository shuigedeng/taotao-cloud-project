package com.taotao.cloud.order.api.dto.order;

import cn.lili.modules.order.order.entity.enums.DeliverStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.order.order.entity.enums.PayStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 订单导出DTO
 *
 * @since 2021/6/3 6:36 下午
 */
@Schema(description = "订单导出DTO")
public class OrderExportDTO {

	@Schema(description = "订单编号")
	private String sn;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "创建时间")
	private Date createTime;

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
	private Double freightPrice;

	@Schema(description = "商品价格")
	private Double goodsPrice;

	@Schema(description = "优惠的金额")
	private Double discountPrice;

	@Schema(description = "总价格")
	private Double flowPrice;

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
