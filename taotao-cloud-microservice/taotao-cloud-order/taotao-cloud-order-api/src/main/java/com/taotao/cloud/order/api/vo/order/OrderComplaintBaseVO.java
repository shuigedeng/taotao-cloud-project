package com.taotao.cloud.order.api.vo.order;

import com.taotao.cloud.order.api.enums.aftersale.ComplaintStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单交易投诉基础VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Schema(description = "订单交易投诉基础VO")
public record OrderComplaintBaseVO(

	@Schema(description = "id")
	Long id,

	@Schema(description = "投诉主题")
	String complainTopic,

	@Schema(description = "投诉内容")
	String content,

	@Schema(description = "投诉凭证图片")
	String images,

	/**
	 * @see ComplaintStatusEnum
	 */
	@Schema(description = "交易投诉状态")
	String complainStatus,

	@Schema(description = "申诉商家内容")
	String appealContent,

	@Schema(description = "申诉商家时间")
	LocalDateTime appealTime,

	@Schema(description = "申诉商家上传的图片")
	String appealImages,

	@Schema(description = "订单号")
	String orderSn,

	@Schema(description = "下单时间")
	LocalDateTime orderTime,

	@Schema(description = "商品名称")
	String goodsName,

	@Schema(description = "商品id")
	String goodsId,

	@Schema(description = "sku主键")
	String skuId,

	@Schema(description = "商品价格")
	BigDecimal goodsPrice,

	@Schema(description = "商品图片")
	String goodsImage,

	@Schema(description = "购买的商品数量")
	Integer num,

	@Schema(description = "运费")
	BigDecimal freightPrice,

	@Schema(description = "订单金额")
	BigDecimal orderPrice,

	@Schema(description = "物流单号")
	String logisticsNo,

	@Schema(description = "商家id")
	String storeId,

	@Schema(description = "商家名称")
	String storeName,

	@Schema(description = "会员id")
	String memberId,

	@Schema(description = "会员名称")
	String memberName,

	@Schema(description = "收货人")
	String consigneeName,

	@Schema(description = "收货地址")
	String consigneeAddressPath,

	@Schema(description = "收货人手机")
	String consigneeMobile,

	@Schema(description = "仲裁结果")
	String arbitrationResult
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -7013465343480854816L;

}
