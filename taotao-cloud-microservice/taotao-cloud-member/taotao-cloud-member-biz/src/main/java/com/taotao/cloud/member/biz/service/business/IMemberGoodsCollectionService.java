package com.taotao.cloud.member.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.member.api.model.vo.GoodsCollectionVO;
import com.taotao.cloud.member.biz.model.entity.MemberGoodsCollection;

import java.util.List;

/**
 * 商品收藏业务层
 *
 * @since 2020/11/18 2:25 下午
 */
public interface IMemberGoodsCollectionService extends IService<MemberGoodsCollection> {

	/**
	 * 获取商品搜索分页
	 *
	 * @param pageParam 查询参数
	 * @return 商品搜索分页
	 */
	IPage<GoodsCollectionVO> goodsCollection(PageParam pageParam);

	/**
	 * 是否收藏商品
	 *
	 * @param skuId 规格ID
	 * @return 是否收藏
	 */
	Boolean isCollection(Long skuId);

	/**
	 * 添加商品收藏
	 *
	 * @param skuId 规格ID
	 * @return 操作状态
	 */
	Boolean addGoodsCollection(Long skuId);

	/**
	 * 商品收藏
	 *
	 * @param skuId 规格ID
	 * @return 操作状态
	 */
	Boolean deleteGoodsCollection(Long skuId);

	/**
	 * 删除商品收藏
	 *
	 * @param goodsIds 规格ID
	 * @return 操作状态
	 */
	Boolean deleteGoodsCollection(List<Long> goodsIds);

	/**
	 * 删除商品SKU收藏
	 *
	 * @param skuIds 规格ID
	 * @return 操作状态
	 */
	Boolean deleteSkuCollection(List<Long> skuIds);
}
