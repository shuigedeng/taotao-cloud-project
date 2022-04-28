package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.api.vo.StoreGoodsLabelVO;
import com.taotao.cloud.goods.biz.entity.StoreGoodsLabel;

import java.util.List;

/**
 * 店铺商品分类业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:01:05
 */
public interface IStoreGoodsLabelService extends IService<StoreGoodsLabel> {

	/**
	 * 根据商家ID获取店铺分类列表
	 *
	 * @param storeId 商家ID
	 * @return {@link List }<{@link StoreGoodsLabelVO }>
	 * @since 2022-04-27 17:01:05
	 */
	List<StoreGoodsLabelVO> listByStoreId(Long storeId);

	/**
	 * 根据分类id集合获取所有店铺分类根据层级排序
	 *
	 * @param ids 商家ID
	 * @return {@link List }<{@link StoreGoodsLabel }>
	 * @since 2022-04-27 17:01:05
	 */
	List<StoreGoodsLabel> listByStoreIds(List<Long> ids);

	/**
	 * 添加商品分类
	 *
	 * @param storeGoodsLabel 店铺商品分类
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:01:05
	 */
	Boolean addStoreGoodsLabel(StoreGoodsLabel storeGoodsLabel);

	/**
	 * 修改商品分类
	 *
	 * @param storeGoodsLabel 店铺商品分类
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:01:05
	 */
	Boolean editStoreGoodsLabel(StoreGoodsLabel storeGoodsLabel);

	/**
	 * 删除商品分类
	 *
	 * @param storeLabelId 店铺 分类 ID
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:01:05
	 */
	Boolean removeStoreGoodsLabel(Long storeLabelId);

}
