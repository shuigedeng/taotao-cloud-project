package com.taotao.cloud.goods.biz.listener;

import org.springframework.context.ApplicationEvent;


/**
 * 生成es商品索引事件
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:32:34
 */
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
