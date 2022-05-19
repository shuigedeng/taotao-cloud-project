package com.taotao.cloud.order.api.vo.order;

import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 子订单VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "子订单VO")
public class OrderItemVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -6293102172184734928L;


	/**
	 * 订单编号
	 */
	private String orderSn;

	/**
	 * 子订单编号
	 */
	private String sn;

	/**
	 * 单价
	 */
	private BigDecimal unitPrice;

	/**
	 * 小记
	 */
	private BigDecimal subTotal;

	/**
	 * 商品ID
	 */
	private Long goodsId;

	/**
	 * 货品ID
	 */
	private Long skuId;

	/**
	 * 销售量
	 */
	private Integer num;

	/**
	 * 交易编号
	 */
	private String tradeSn;

	/**
	 * 图片
	 */
	private String image;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 分类ID
	 */
	private Long categoryId;

	/**
	 * 快照id
	 */
	private Long snapshotId;

	/**
	 * 规格json
	 */
	private String specs;

	/**
	 * 促销类型
	 */
	private String promotionType;

	/**
	 * 促销id
	 */
	private Long promotionId;

	/**
	 * 销售金额
	 */
	private BigDecimal goodsPrice;

	/**
	 * 实际金额
	 */
	private BigDecimal flowPrice;

	/**
	 * 评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，
	 *
	 * @see CommentStatusEnum
	 */
	private String commentStatus;

	/**
	 * 售后状态
	 *
	 * @see OrderItemAfterSaleStatusEnum
	 */
	private String afterSaleStatus;

	/**
	 * 价格详情
	 */
	private String priceDetail;

	/**
	 * 投诉状态
	 *
	 * @see OrderComplaintStatusEnum
	 */
	private String complainStatus;

	/**
	 * 交易投诉id
	 */
	private Long complainId;

	/**
	 * 退货商品数量
	 */
	private Integer returnGoodsNumber;
}
