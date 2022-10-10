package com.taotao.cloud.promotion.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.promotion.api.model.query.SeckillPageQuery;
import com.taotao.cloud.promotion.api.model.vo.SeckillApplyVO;
import com.taotao.cloud.promotion.api.model.vo.SeckillGoodsVO;
import com.taotao.cloud.promotion.api.model.vo.SeckillTimelineVO;
import com.taotao.cloud.promotion.biz.model.entity.SeckillApply;

import java.util.List;

/**
 * 秒杀申请业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:44:22
 */
public interface SeckillApplyService extends IService<SeckillApply> {


	/**
	 * 获取当天秒杀活动信息列表（时刻及对应时刻下的商品）
	 *
	 * @return {@link List }<{@link SeckillTimelineVO }>
	 * @since 2022-04-27 16:44:22
	 */
	List<SeckillTimelineVO> getSeckillTimeline();

	/**
	 * 获取当天某个时刻的秒杀活动商品列表
	 *
	 * @param timeline 指定时刻
	 * @return {@link List }<{@link SeckillGoodsVO }>
	 * @since 2022-04-27 16:44:22
	 */
	List<SeckillGoodsVO> getSeckillGoods(Integer timeline);

	/**
	 * 分页查询限时请购申请列表
	 *
	 * @param queryParam 秒杀活动申请查询参数
	 * @param pageVo     分页参数
	 * @return {@link IPage }<{@link SeckillApply }>
	 * @since 2022-04-27 16:44:22
	 */
	IPage<SeckillApply> getSeckillApply(SeckillPageQuery queryParam, PageParam pageVo);

	/**
	 * 分页查询限时请购申请列表
	 *
	 * @param queryParam 秒杀活动申请查询参数
	 * @return {@link List }<{@link SeckillApply }>
	 * @since 2022-04-27 16:44:22
	 */
	List<SeckillApply> getSeckillApply(SeckillPageQuery queryParam);

	/**
	 * 添加秒杀活动申请
	 * 检测是否商品是否同时参加多个活动
	 * 将秒杀商品信息存入秒杀活动中
	 * 保存秒杀活动商品，促销商品信息
	 *
	 * @param seckillId        秒杀活动编号
	 * @param storeId          商家id
	 * @param seckillApplyList 秒杀活动申请列表
	 * @since 2022-04-27 16:44:22
	 */
	void addSeckillApply(String seckillId, String storeId, List<SeckillApplyVO> seckillApplyList);

	/**
	 * 批量删除秒杀活动商品
	 *
	 * @param seckillId 秒杀活动活动id
	 * @param id        秒杀活动商品
	 * @since 2022-04-27 16:44:22
	 */
	void removeSeckillApply(String seckillId, String id);

}
