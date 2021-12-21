package com.taotao.cloud.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

/**
 * 会员评价DTO
 *¬
 * 
 * @since 2020/11/29 11:13 下午
 */
@Schema(description = "租户id")
public class MemberEvaluationDTO {

    @Schema(description = "子订单编号")
    @NotEmpty(message = "订单异常")
    private String orderItemSn;

    @Schema(description = "商品ID")
    @NotEmpty(message = "订单商品异常不能为空")
    private String goodsId;

    @Schema(description = "规格ID")
    @NotEmpty(message = "订单商品不能为空")
    private String skuId;

    @Schema(description = "好中差评价")
    @NotEmpty(message = "请评价")
    private String grade;

    @Schema(description = "评论内容")
    @NotEmpty(message = "评论内容不能为空")
    @Length(max = 500, message = "评论内容不能超过500字符")
    private String content;

    @Schema(description = "评论图片")
    private String images;

    @Schema(description = "物流评分")
    private Integer deliveryScore;

    @Schema(description = "服务评分")
    private Integer serviceScore;

    @Schema(description = "描述评分")
    private Integer descriptionScore;


	public String getOrderItemSn() {
		return orderItemSn;
	}

	public void setOrderItemSn(String orderItemSn) {
		this.orderItemSn = orderItemSn;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public Integer getDeliveryScore() {
		return deliveryScore;
	}

	public void setDeliveryScore(Integer deliveryScore) {
		this.deliveryScore = deliveryScore;
	}

	public Integer getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(Integer serviceScore) {
		this.serviceScore = serviceScore;
	}

	public Integer getDescriptionScore() {
		return descriptionScore;
	}

	public void setDescriptionScore(Integer descriptionScore) {
		this.descriptionScore = descriptionScore;
	}
}
