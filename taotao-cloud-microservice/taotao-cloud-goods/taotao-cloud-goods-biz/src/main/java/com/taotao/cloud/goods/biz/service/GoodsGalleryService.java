package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.biz.entity.GoodsGallery;
import java.util.List;

/**
 * 商品相册业务层
 */
public interface GoodsGalleryService extends IService<GoodsGallery> {

	/**
	 * 添加商品相册
	 *
	 * @param goodsGalleryList 商品相册列表
	 * @param goodsId          商品ID
	 */
	Boolean add(List<String> goodsGalleryList, String goodsId);

	/**
	 * 根据原图获取缩略图
	 *
	 * @param origin 原图地址
	 * @return 商品相册
	 */
	GoodsGallery getGoodsGallery(String origin);

	/**
	 * 根据商品 id查询商品相册原图
	 *
	 * @param goodsId 商品ID
	 * @return 商品相册列表
	 */
	List<GoodsGallery> goodsGalleryList(String goodsId);

}
