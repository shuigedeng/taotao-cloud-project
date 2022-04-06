package com.taotao.cloud.goods.api.event;

import org.springframework.context.ApplicationEvent;

public class GeneratorEsGoodsIndexEvent extends ApplicationEvent {

	private Long goodsId;

	public GeneratorEsGoodsIndexEvent(Object source, Long goodsId) {
		super(source);
		this.goodsId = goodsId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
}
