package com.taotao.cloud.promotion.biz.service.business;


import com.taotao.cloud.promotion.api.model.vo.PointsGoodsVO;
import com.taotao.cloud.promotion.biz.model.entity.PointsGoods;

import java.util.List;

/**
 * 积分商品业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:44:10
 */
public interface IPointsGoodsService extends AbstractPromotionsService<PointsGoods> {

	/**
	 * 批量保存库存商品
	 *
	 * @param promotionsList 积分商品列表
	 * @return boolean
	 * @since 2022-04-27 16:44:10
	 */
	boolean savePointsGoodsBatch(List<PointsGoods> promotionsList);

	/**
	 * 根据ID获取积分详情
	 *
	 * @param id 积分商品id
	 * @return {@link PointsGoodsVO }
	 * @since 2022-04-27 16:44:10
	 */
	PointsGoodsVO getPointsGoodsDetail(String id);

	/**
	 * 根据ID获取积分详情
	 *
	 * @param skuId 商品SkuId
	 * @return {@link PointsGoodsVO }
	 * @since 2022-04-27 16:44:10
	 */
	PointsGoodsVO getPointsGoodsDetailBySkuId(String skuId);

}
