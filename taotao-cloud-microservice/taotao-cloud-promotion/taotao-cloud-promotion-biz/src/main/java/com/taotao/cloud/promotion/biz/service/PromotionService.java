package com.taotao.cloud.promotion.biz.service;


import java.util.Map;

/**
 * 促销业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:44:19
 */
public interface PromotionService {

	/**
	 * 获取当前进行的所有促销活动信息
	 *
	 * @return {@link Map }<{@link String }, {@link Object }>
	 * @since 2022-04-27 16:44:19
	 */
	Map<String, Object> getCurrentPromotion();

	/**
	 * 根据商品索引获取当前商品索引的所有促销活动信息
	 *
	 * @param index 商品索引
	 * @return {@link Map }<{@link String }, {@link Object }>
	 * @since 2022-04-27 16:44:19
	 */
	Map<String, Object> getGoodsCurrentPromotionMap(EsGoodsIndex index);

}
