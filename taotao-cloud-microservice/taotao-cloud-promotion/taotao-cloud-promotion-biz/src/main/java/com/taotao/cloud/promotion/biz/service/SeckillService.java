package com.taotao.cloud.promotion.biz.service;


import com.taotao.cloud.promotion.api.web.vo.SeckillVO;
import com.taotao.cloud.promotion.biz.model.entity.Seckill;
import com.taotao.cloud.promotion.biz.model.entity.SeckillApply;

import java.util.List;

/**
 * 秒杀业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:44:28
 */
public interface SeckillService extends AbstractPromotionsService<Seckill> {

	/**
	 * 前创建
	 * 预创建活动数量
	 */
	Integer PRE_CREATION = 7;

	/**
	 * 获取秒杀活动信息
	 *
	 * @param id 秒杀活动id
	 * @return {@link SeckillVO }
	 * @since 2022-04-27 16:44:28
	 */
	SeckillVO getSeckillDetail(String id);

	/**
	 * 初始化秒杀活动，默认开启三十天的秒杀活动
	 *
	 * @since 2022-04-27 16:44:28
	 */
	void init();

	/**
	 * 获取当前可参与的活动数量
	 *
	 * @return long
	 * @since 2022-04-27 16:44:28
	 */
	long getApplyNum();

	/**
	 * 更新秒杀活动的商品数量
	 *
	 * @param seckillId 秒杀活动ID
	 * @since 2022-04-27 16:44:28
	 */
	void updateSeckillGoodsNum(String seckillId);

	/**
	 * 更新商品索引限时抢购信息
	 *
	 * @param seckill        限时抢购信息
	 * @param seckillApplies 限时抢购商品列表
	 * @since 2022-04-27 16:44:28
	 */
	void updateEsGoodsSeckill(Seckill seckill, List<SeckillApply> seckillApplies);

	/**
	 * 设置秒杀活动的每个参与活动商品的详细时间
	 *
	 * @param seckill      秒杀活动信息
	 * @param seckillApply 申请参与秒杀活动的商品信息
	 * @since 2022-04-27 16:44:28
	 */
	void setSeckillApplyTime(Seckill seckill, SeckillApply seckillApply);
}
