package com.taotao.cloud.order.api.vo.order;

import com.taotao.cloud.order.api.enums.aftersale.ComplaintStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单交易投诉基础VO
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单交易投诉基础VO")
public class OrderComplaintBaseVO implements Serializable {

	private static final long serialVersionUID = -7013465343480854816L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "投诉主题")
	private String complainTopic;
	/**
	 * 应用ID
	 */
	@Schema(description = "投诉内容")
	private String content;
	/**
	 * 应用ID
	 */
	@Schema(description = "投诉凭证图片")
	private String images;

	/**
	 * @see ComplaintStatusEnum
	 */
	@Schema(description = "交易投诉状态")
	private String complainStatus;
	/**
	 * 应用ID
	 */
	@Schema(description = "申诉商家内容")
	private String appealContent;
	/**
	 * 应用ID
	 */
	@Schema(description = "申诉商家时间")
	private LocalDateTime appealTime;
	/**
	 * 应用ID
	 */
	@Schema(description = "申诉商家上传的图片")
	private String appealImages;
	/**
	 * 应用ID
	 */
	@Schema(description = "订单号")
	private String orderSn;
	/**
	 * 应用ID
	 */
	@Schema(description = "下单时间")
	private LocalDateTime orderTime;
	/**
	 * 应用ID
	 */
	@Schema(description = "商品名称")
	private String goodsName;
	/**
	 * 应用ID
	 */
	@Schema(description = "商品id")
	private String goodsId;
	/**
	 * 应用ID
	 */
	@Schema(description = "sku主键")
	private String skuId;
	/**
	 * 应用ID
	 */
	@Schema(description = "商品价格")
	private BigDecimal goodsPrice;
	/**
	 * 应用ID
	 */
	@Schema(description = "商品图片")
	private String goodsImage;
	/**
	 * 应用ID
	 */
	@Schema(description = "购买的商品数量")
	private Integer num;
	/**
	 * 应用ID
	 */
	@Schema(description = "运费")
	private BigDecimal freightPrice;
	/**
	 * 应用ID
	 */
	@Schema(description = "订单金额")
	private BigDecimal orderPrice;
	/**
	 * 应用ID
	 */
	@Schema(description = "物流单号")
	private String logisticsNo;
	/**
	 * 应用ID
	 */
	@Schema(description = "商家id")
	private String storeId;
	/**
	 * 应用ID
	 */
	@Schema(description = "商家名称")
	private String storeName;
	/**
	 * 应用ID
	 */
	@Schema(description = "会员id")
	private String memberId;
	/**
	 * 应用ID
	 */
	@Schema(description = "会员名称")
	private String memberName;
	/**
	 * 应用ID
	 */
	@Schema(description = "收货人")
	private String consigneeName;
	/**
	 * 应用ID
	 */
	@Schema(description = "收货地址")
	private String consigneeAddressPath;
	/**
	 * 应用ID
	 */
	@Schema(description = "收货人手机")
	private String consigneeMobile;
	/**
	 * 应用ID
	 */
	@Schema(description = "仲裁结果")
	private String arbitrationResult;
}
