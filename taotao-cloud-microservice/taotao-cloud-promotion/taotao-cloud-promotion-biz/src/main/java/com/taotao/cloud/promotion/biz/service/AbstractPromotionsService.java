package com.taotao.cloud.promotion.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.promotion.api.query.BasePromotionsSearchQuery;
import com.taotao.cloud.promotion.biz.entity.BasePromotions;

import java.util.List;

/**
 * 抽象通用促销服务 如需拓展原促销实体字段，新拓展类继承自促销实体即可
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:42:25
 */
public interface AbstractPromotionsService<T extends BasePromotions<T, Long>> extends IService<T> {

	/**
	 * 通用促销保存 调用顺序:
	 * 1. initPromotion 初始化促销信息
	 * 2. checkPromotions 检查促销参数
	 * 3. save 保存促销信息
	 * 4. updatePromotionGoods 更新促销商品信息
	 * 5。updateEsGoodsIndex 更新商品索引促销信息
	 *
	 * @param promotions 促销信息
	 * @return boolean
	 * @since 2022-04-27 16:42:25
	 */
	boolean savePromotions(T promotions);

	/**
	 * 通用促销更新 调用顺序:
	 * 1. checkStatus 检查促销状态
	 * 2. checkPromotions 检查促销参数
	 * 3. saveOrUpdate 保存促销信息
	 * 4. updatePromotionGoods 更新促销商品信息
	 * 5. updateEsGoodsIndex 更新商品索引促销信息
	 *
	 * @param promotions 促销信息
	 * @return boolean
	 * @since 2022-04-27 16:42:25
	 */
	boolean updatePromotions(T promotions);

	/**
	 * 更新促销状态 如果要更新促销状态为关闭，startTime和endTime置为空即可
	 *
	 * @param ids       促销id集合
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return boolean
	 * @since 2022-04-27 16:42:25
	 */
	boolean updateStatus(List<String> ids, Long startTime, Long endTime);

	/**
	 * 移除促销活动
	 *
	 * @param ids 促销活动id集合
	 * @return boolean
	 * @since 2022-04-27 16:42:25
	 */
	boolean removePromotions(List<String> ids);

	/**
	 * 分页查询促销信息
	 *
	 * @param searchParams 查询参数，继承自继承促销查询参数
	 * @param page         分页参数
	 * @return {@link IPage }<{@link T }>
	 * @since 2022-04-27 16:42:25
	 */
	<S extends BasePromotionsSearchQuery> IPage<T> pageFindAll(S searchParams, PageParam page);

	/**
	 * 列表查询促销信息
	 *
	 * @param searchParams 查询参数，继承自继承促销查询参数
	 * @return {@link List }<{@link T }>
	 * @since 2022-04-27 16:42:25
	 */
	<S extends BasePromotionsSearchQuery> List<T> listFindAll(S searchParams);

	/**
	 * 初始化促销字段
	 *
	 * @param promotions 促销实体
	 * @since 2022-04-27 16:42:25
	 */
	void initPromotion(T promotions);

	/**
	 * 检查促销参数
	 *
	 * @param promotions 促销实体
	 * @since 2022-04-27 16:42:25
	 */
	void checkPromotions(T promotions);

	/**
	 * 检查促销状态
	 *
	 * @param promotions 促销实体
	 * @since 2022-04-27 16:42:25
	 */
	void checkStatus(T promotions);

	/**
	 * 更新促销商品信息
	 *
	 * @param promotions 促销实体
	 * @since 2022-04-27 16:42:25
	 */
	void updatePromotionsGoods(T promotions);

	/**
	 * 更新促销信息到商品索引
	 *
	 * @param promotions 促销实体
	 * @since 2022-04-27 16:42:25
	 */
	void updateEsGoodsIndex(T promotions);

	/**
	 * 当前促销类型
	 *
	 * @return {@link PromotionTypeEnum }
	 * @since 2022-04-27 16:42:25
	 */
	PromotionTypeEnum getPromotionType();

}
