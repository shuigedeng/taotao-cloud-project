package com.taotao.cloud.goods.biz.service.business;

import com.taotao.cloud.goods.biz.model.entity.GoodsGallery;
import com.taotao.cloud.web.base.service.BaseSuperService;

import java.util.List;

/**
 * 商品相册业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:00:12
 */
public interface IGoodsGalleryService extends BaseSuperService<GoodsGallery, Long> {

	/**
	 * 添加商品相册
	 *
	 * @param goodsGalleryList 商品相册列表
	 * @param goodsId          商品ID
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:12
	 */
	Boolean add(List<String> goodsGalleryList, Long goodsId);

	/**
	 * 根据原图获取缩略图
	 *
	 * @param origin 原图地址
	 * @return {@link GoodsGallery }
	 * @since 2022-04-27 17:00:12
	 */
	GoodsGallery getGoodsGallery(String origin);

	/**
	 * 根据商品id查询商品相册原图
	 *
	 * @param goodsId 商品ID
	 * @return {@link List }<{@link GoodsGallery }>
	 * @since 2022-04-27 17:00:12
	 */
	List<GoodsGallery> goodsGalleryList(Long goodsId);

}
