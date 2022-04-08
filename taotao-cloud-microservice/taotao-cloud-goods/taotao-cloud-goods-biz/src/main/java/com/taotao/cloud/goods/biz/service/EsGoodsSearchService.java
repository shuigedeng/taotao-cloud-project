package com.taotao.cloud.goods.biz.service;

import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.goods.api.dto.EsGoodsSearchDTO;
import com.taotao.cloud.goods.api.dto.HotWordsDTO;
import com.taotao.cloud.goods.biz.elasticsearch.EsGoodsIndex;
import com.taotao.cloud.goods.biz.elasticsearch.EsGoodsRelatedInfo;
import java.util.List;
import org.springframework.data.elasticsearch.core.SearchPage;

/**
 * ES商品搜索业务层
 **/
public interface EsGoodsSearchService {

	/**
	 * 商品搜索
	 *
	 * @param searchDTO 搜索参数
	 * @param pageParam 分页参数
	 * @return 搜索结果
	 */
	SearchPage<EsGoodsIndex> searchGoods(EsGoodsSearchDTO searchDTO, PageParam pageParam);

	/**
	 * 获取热门关键词
	 *
	 * @param count 热词数量
	 * @return 热词集合
	 */
	List<String> getHotWords(Integer count);

	/**
	 * 设置热门关键词
	 *
	 * @param hotWords 热词分数
	 */
	Boolean setHotWords(HotWordsDTO hotWords);

	/**
	 * 删除热门关键词
	 *
	 * @param keywords 热词
	 */
	Boolean deleteHotWords(String keywords);

	/**
	 * 获取筛选器
	 *
	 * @param goodsSearch 搜索条件
	 * @param pageParam      分页参数
	 * @return ES商品关联
	 */
	EsGoodsRelatedInfo getSelector(EsGoodsSearchDTO goodsSearch, PageParam pageParam);

	/**
	 * 根据SkuID列表获取ES商品
	 *
	 * @param skuIds SkuId列表
	 * @return ES商品列表
	 */
	List<EsGoodsIndex> getEsGoodsBySkuIds(List<Long> skuIds);

	/**
	 * 根据id获取商品索引
	 *
	 * @param id 商品skuId
	 * @return 商品索引
	 */
	EsGoodsIndex getEsGoodsById(Long id);
}
