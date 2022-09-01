package com.taotao.cloud.goods.biz.service;

import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.goods.api.model.dto.GoodsParamsDTO;
import com.taotao.cloud.goods.biz.elasticsearch.EsGoodsIndex;
import com.taotao.cloud.goods.biz.model.entity.GoodsSku;
import com.taotao.cloud.promotion.api.web.vo.BasePromotionsVO;
import com.taotao.cloud.promotion.api.web.vo.PromotionGoodsVO;
import org.elasticsearch.action.update.UpdateRequest;

import java.util.List;
import java.util.Map;

/**
 * 商品索引业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:00:00
 */
public interface IEsGoodsIndexService {

	/**
	 * 全局索引初始化
	 *
	 * @since 2022-04-27 17:00:00
	 */
	void init();

	/**
	 * 获取es生成索引进度
	 *
	 * @return {@link Map }<{@link String }, {@link Integer }>
	 * @since 2022-04-27 17:00:00
	 */
	Map<String, Integer> getProgress();

	/**
	 * 添加商品索引
	 *
	 * @param goods 商品索引信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:00
	 */
	Boolean addIndex(EsGoodsIndex goods);

	/**
	 * 更新商品索引
	 *
	 * @param goods 商品索引信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:00
	 */
	Boolean updateIndex(EsGoodsIndex goods);

	/**
	 * 更新商品索引的的部分属性（只填写更新的字段，不需要更新的字段不要填写）
	 *
	 * @param id    商品索引id
	 * @param goods 更新后的购买数量
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:00
	 */
	Boolean updateIndex(Long id, EsGoodsIndex goods);

	/**
	 * 更新商品索引的的部分属性
	 *
	 * @param queryFields  查询字段
	 * @param updateFields 更新字段
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:00
	 */
	Boolean updateIndex(Map<String, Object> queryFields, Map<String, Object> updateFields);

	/**
	 * 批量商品索引的的属性（ID 必填, 其他字段只填写更新的字段，不需要更新的字段不要填写。）
	 *
	 * @param goodsIndices 商品索引列表
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:00
	 */
	Boolean updateBulkIndex(List<EsGoodsIndex> goodsIndices);

	/**
	 * 删除索引
	 *
	 * @param goods 商品索引信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:00
	 */
	Boolean deleteIndex(EsGoodsIndex goods);

	/**
	 * 删除索引
	 *
	 * @param id 商品索引信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:00
	 */
	Boolean deleteIndexById(Long id);

	/**
	 * 删除索引
	 *
	 * @param ids 商品索引id集合
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:00
	 */
	Boolean deleteIndexByIds(List<Long> ids);

	/**
	 * 初始化商品索引
	 *
	 * @param goodsIndexList 商品索引列表
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:00
	 */
	Boolean initIndex(List<EsGoodsIndex> goodsIndexList);

	/**
	 * 更新商品索引的促销信息
	 *
	 * @param id        id(skuId)
	 * @param promotion 促销信息
	 * @param key       促销信息的key
	 * @return {@link UpdateRequest }
	 * @since 2022-04-27 17:00:01
	 */
	UpdateRequest updateEsGoodsIndexPromotions(Long id, BasePromotionsVO promotion, String key);

	/**
	 * 更新商品索引的促销信息
	 *
	 * @param ids       id(skuId)
	 * @param promotion 促销信息
	 * @param key       促销信息的key
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:01
	 */
	Boolean updateEsGoodsIndexPromotions(List<Long> ids, BasePromotionsVO promotion, String key);

	/**
	 * 根据列表更新商品索引的促销信息
	 *
	 * @param promotionGoodsList 促销商品列表
	 * @param promotion          促销信息
	 * @param key                促销信息的key
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:01
	 */
	Boolean updateEsGoodsIndexByList(List<PromotionGoodsVO> promotionGoodsList,
									 BasePromotionsVO promotion,
									 String key);

	/**
	 * 更新全部商品索引的促销信息
	 *
	 * @param promotion 促销信息
	 * @param key       促销信息的key
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:01
	 */
	Boolean updateEsGoodsIndexAllByList(BasePromotionsVO promotion, String key);

	/**
	 * 删除指定商品的促销信息
	 *
	 * @param skuIds        skuId列表
	 * @param promotionType 促销类型
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:01
	 */
	Boolean deleteEsGoodsPromotionIndexByList(List<Long> skuIds, PromotionTypeEnum promotionType);

	/**
	 * 删除索引中指定的促销活动id的促销活动
	 *
	 * @param skuIds        商品skuId
	 * @param promotionsKey 促销活动Key
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:01
	 */
	Boolean deleteEsGoodsPromotionByPromotionKey(List<Long> skuIds, String promotionsKey);


	/**
	 * 删除索引中指定的促销活动id的促销活动
	 *
	 * @param promotionsKey 促销活动Key
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:01
	 */
	Boolean deleteEsGoodsPromotionByPromotionKey(String promotionsKey);

	/**
	 * 清除所以商品索引的无效促销活动
	 *
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:00:01
	 */
	Boolean cleanInvalidPromotion();

	/**
	 * 根据id获取商品索引信息
	 *
	 * @param id skuId
	 * @return {@link EsGoodsIndex }
	 * @since 2022-04-27 17:00:01
	 */
	EsGoodsIndex findById(Long id);

	/**
	 * 根据id获取商品索引信息的促销信息
	 *
	 * @param id skuId
	 * @return {@link Map }<{@link String }, {@link Object }>
	 * @since 2022-04-27 17:00:01
	 */
	Map<String, Object> getPromotionMap(Long id);

	/**
	 * 根据id获取商品索引信息的指定促销活动的id
	 *
	 * @param id                skuId
	 * @param promotionTypeEnum 促销活动类型
	 * @return {@link List }<{@link Long }>
	 * @since 2022-04-27 17:00:01
	 */
	List<Long> getPromotionIdByPromotionType(Long id, PromotionTypeEnum promotionTypeEnum);

	/**
	 * 获取重置的商品索引
	 *
	 * @param goodsSku       商品sku信息
	 * @param goodsParamDTOS 商品参数
	 * @return {@link EsGoodsIndex }
	 * @since 2022-04-27 17:00:01
	 */
	EsGoodsIndex getResetEsGoodsIndex(GoodsSku goodsSku, List<GoodsParamsDTO> goodsParamDTOS);
}
