package com.taotao.cloud.goods.api.event;

import org.springframework.context.ApplicationEvent;

public class GeneratorEsGoodsIndexEvent extends ApplicationEvent {

	private String goodsId;

	public GeneratorEsGoodsIndexEvent(Object source, String goodsId) {
		super(source);
		this.goodsId = goodsId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
}
