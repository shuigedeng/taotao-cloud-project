package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 评分VO
 *
 *
 * @since 2021/3/15 5:55 下午
 */
@Schema(description = "评分VO")
public class StoreRatingVO {

	@Schema(description = "物流评分")
	private String deliveryScore;

	@Schema(description = "服务评分")
	private String serviceScore;

	@Schema(description = "描述评分")
	private String descriptionScore;

	public String getDeliveryScore() {
		return deliveryScore;
	}

	public void setDeliveryScore(String deliveryScore) {
		this.deliveryScore = deliveryScore;
	}

	public String getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(String serviceScore) {
		this.serviceScore = serviceScore;
	}

	public String getDescriptionScore() {
		return descriptionScore;
	}

	public void setDescriptionScore(String descriptionScore) {
		this.descriptionScore = descriptionScore;
	}
}
