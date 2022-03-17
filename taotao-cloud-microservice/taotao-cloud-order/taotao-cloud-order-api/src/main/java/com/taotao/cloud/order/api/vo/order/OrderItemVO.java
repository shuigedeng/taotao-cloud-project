package com.taotao.cloud.order.api.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 子订单VO
 */
@Data
@Schema(description = "子订单VO")
public class OrderItemVO {

	@Schema(description = "编号")
	private String sn;

	@Schema(description = "商品ID")
	private String goodsId;

	@Schema(description = "货品ID")
	private String skuId;

	@Schema(description = "销售量")
	private String num;

	@Schema(description = "图片")
	private String image;

	@Schema(description = "商品名称")
	private String name;

	@Schema(description = "商品名称")
	private Double goodsPrice;

	/**
	 * @see OrderItemAfterSaleStatusEnum
	 */
	@Schema(description = "售后状态", allowableValues = "NOT_APPLIED(未申请),ALREADY_APPLIED(已申请),EXPIRED(已失效不允许申请售后)")
	private String afterSaleStatus;

	/**
	 * @see OrderComplaintStatusEnum
	 */
	@Schema(description = "投诉状态")
	private String complainStatus;

	/**
	 * @see CommentStatusEnum
	 */
	@Schema(description = "评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，")
	private String commentStatus;


	public OrderItemVO(String sn, String goodsId, String skuId, String num, String image,
		String name, String afterSaleStatus, String complainStatus, String commentStatus,
		Double goodsPrice) {
		this.sn = sn;
		this.goodsId = goodsId;
		this.skuId = skuId;
		this.num = num;
		this.image = image;
		this.name = name;
		this.afterSaleStatus = afterSaleStatus;
		this.complainStatus = complainStatus;
		this.commentStatus = commentStatus;
		this.goodsPrice = goodsPrice;
	}

}
