package com.taotao.cloud.promotion.api.event;

import org.springframework.context.ApplicationEvent;

public class UpdateEsGoodsIndexPromotionsEvent extends ApplicationEvent {

	private String promotionsJsonStr;

	public UpdateEsGoodsIndexPromotionsEvent(Object source, String promotionsJsonStr) {
		super(source);
		this.promotionsJsonStr = promotionsJsonStr;
	}

	public String getPromotionsJsonStr() {
		return promotionsJsonStr;
	}

	public void setPromotionsJsonStr(String promotionsJsonStr) {
		this.promotionsJsonStr = promotionsJsonStr;
	}
}
