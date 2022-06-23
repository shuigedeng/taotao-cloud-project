package com.taotao.cloud.order.api.web.vo.order;

import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 子订单VO
 */
@RecordBuilder
@Schema(description = "子订单VO")
public record OrderItemVO(

	/**
	 * 订单编号
	 */
	String orderSn,

	/**
	 * 子订单编号
	 */
	String sn,

	/**
	 * 单价
	 */
	BigDecimal unitPrice,

	/**
	 * 小记
	 */
	BigDecimal subTotal,

	/**
	 * 商品ID
	 */
	Long goodsId,

	/**
	 * 货品ID
	 */
	Long skuId,

	/**
	 * 销售量
	 */
	Integer num,

	/**
	 * 交易编号
	 */
	String tradeSn,

	/**
	 * 图片
	 */
	String image,

	/**
	 * 商品名称
	 */
	String goodsName,

	/**
	 * 分类ID
	 */
	Long categoryId,

	/**
	 * 快照id
	 */
	Long snapshotId,

	/**
	 * 规格json
	 */
	String specs,

	/**
	 * 促销类型
	 */
	String promotionType,

	/**
	 * 促销id
	 */
	Long promotionId,

	/**
	 * 销售金额
	 */
	BigDecimal goodsPrice,

	/**
	 * 实际金额
	 */
	BigDecimal flowPrice,

	/**
	 * 评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，
	 *
	 * @see CommentStatusEnum
	 */
	String commentStatus,

	/**
	 * 售后状态
	 *
	 * @see OrderItemAfterSaleStatusEnum
	 */
	String afterSaleStatus,

	/**
	 * 价格详情
	 */
	String priceDetail,

	/**
	 * 投诉状态
	 *
	 * @see OrderComplaintStatusEnum
	 */
	String complainStatus,

	/**
	 * 交易投诉id
	 */
	Long complainId,

	/**
	 * 退货商品数量
	 */
	Integer returnGoodsNumber
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -6293102172184734928L;

}
