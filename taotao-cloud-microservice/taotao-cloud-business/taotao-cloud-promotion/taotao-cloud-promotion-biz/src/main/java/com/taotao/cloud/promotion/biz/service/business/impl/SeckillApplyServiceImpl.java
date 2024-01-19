/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.promotion.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.cache.redis.repository.RedisRepository;

import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuApi;
import com.taotao.cloud.promotion.api.enums.PromotionsApplyStatusEnum;
import com.taotao.cloud.promotion.api.model.page.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.page.SeckillPageQuery;
import com.taotao.cloud.promotion.api.model.vo.SeckillApplyVO;
import com.taotao.cloud.promotion.api.model.vo.SeckillGoodsVO;
import com.taotao.cloud.promotion.api.model.vo.SeckillTimelineVO;
import com.taotao.cloud.promotion.api.tools.PromotionCacheKeys;
import com.taotao.cloud.promotion.biz.mapper.SeckillApplyMapper;
import com.taotao.cloud.promotion.biz.model.entity.BasePromotions;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.model.entity.Seckill;
import com.taotao.cloud.promotion.biz.model.entity.SeckillApply;
import com.taotao.cloud.promotion.biz.model.pojo.PromotionTools;
import com.taotao.cloud.promotion.biz.service.business.IPromotionGoodsService;
import com.taotao.cloud.promotion.biz.service.business.ISeckillApplyService;
import com.taotao.cloud.promotion.biz.service.business.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 秒杀申请业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:46:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SeckillApplyServiceImpl extends ServiceImpl<SeckillApplyMapper, SeckillApply>
	implements ISeckillApplyService {

	/**
	 * 缓存
	 */
	@Autowired
	private RedisRepository redisRepository;
	/**
	 * 规格商品
	 */
	@Autowired
	private IFeignGoodsSkuApi goodsSkuApi;
	/**
	 * 促销商品
	 */
	@Autowired
	private IPromotionGoodsService promotionGoodsService;
	/**
	 * 秒杀
	 */
	@Autowired
	private ISeckillService seckillService;

	@Override
	public List<SeckillTimelineVO> getSeckillTimeline() {
		// 秒杀活动缓存key
		return getSeckillTimelineToCache(null);
	}

	@Override
	public List<SeckillGoodsVO> getSeckillGoods(Integer timeline) {
		List<SeckillGoodsVO> seckillGoodsVoS = new ArrayList<>();
		// 秒杀活动缓存key
		String seckillCacheKey = PromotionCacheKeys.getSeckillTimelineKey(
			DateUtil.format(DateUtil.beginOfDay(new DateTime()), "yyyyMMdd"));
		List<SeckillTimelineVO> cacheSeckill = cache.get(seckillCacheKey);
		if (cacheSeckill == null || cacheSeckill.isEmpty()) {
			// 如缓存中不存在，则单独获取
			List<SeckillTimelineVO> seckillTimelineToCache = getSeckillTimelineToCache(seckillCacheKey);
			Optional<SeckillTimelineVO> first = seckillTimelineToCache
				.stream()
				.filter(i -> i.getTimeLine().equals(timeline))
				.findFirst();
			if (first.isPresent()) {
				seckillGoodsVoS = first.get().getSeckillGoodsList();
			}
		} else {
			// 如缓存中存在，则取缓存中转为展示的信息
			Optional<SeckillTimelineVO> first = cacheSeckill
				.stream()
				.filter(i -> i.getTimeLine().equals(timeline))
				.findFirst();
			if (first.isPresent()) {
				seckillGoodsVoS = first.get().getSeckillGoodsList();
			}
		}
		return seckillGoodsVoS;
	}

	@Override
	public IPage<SeckillApply> getSeckillApply(SeckillPageQuery queryParam) {
		IPage<SeckillApply> seckillApplyPage = this.page(PageUtil.initPage(pageVo), queryParam.queryWrapper());
		if (seckillApplyPage != null && !seckillApplyPage.getRecords().isEmpty()) {

			// 获取skuId
			List<String> skuIds = seckillApplyPage.getRecords()
				.stream()
				.map(SeckillApply::getSkuId)
				.toList();

			// 循环获取 店铺/全平台 参与的促销商品库存进行填充
			if (!skuIds.isEmpty()) {
				List<Integer> skuStock = promotionGoodsService.getPromotionGoodsStock(
					PromotionTypeEnum.SECKILL, queryParam.getSeckillId(), skuIds);
				for (int i = 0; i < skuIds.size(); i++) {
					seckillApplyPage.getRecords().get(i).setQuantity(skuStock.get(i));
				}
			}
		}
		return seckillApplyPage;
	}

	/**
	 * 分页查询限时请购申请列表
	 *
	 * @param queryParam 秒杀活动申请查询参数
	 * @return 限时请购申请列表
	 */
	@Override
	public List<SeckillApply> getSeckillApply(SeckillPageQuery queryParam) {
		return this.list(queryParam.queryWrapper());
	}

	@Override
	public void addSeckillApply(String seckillId, String storeId, List<SeckillApplyVO> seckillApplyList) {
		Seckill seckill = this.seckillService.getById(seckillId);
		if (seckill == null) {
			throw new BusinessException(ResultEnum.SECKILL_NOT_EXIST_ERROR);
		}
		if (seckillApplyList == null || seckillApplyList.isEmpty()) {
			return;
		}
		// 检查秒杀活动申请是否合法
		checkSeckillApplyList(seckill.getHours(), seckillApplyList);
		// 获取已参与活动的秒杀活动活动申请列表
		List<String> skuIds =
			seckillApplyList
				.stream()
				.map(SeckillApply::getSkuId)
				.toList();
		List<SeckillApply> originList = new ArrayList<>();
		List<PromotionGoods> promotionGoodsList = new ArrayList<>();
		for (SeckillApplyVO seckillApply : seckillApplyList) {
			// 获取参与活动的商品信息
			GoodsSku goodsSku = goodsSkuApi.getGoodsSkuByIdFromCache(seckillApply.getSkuId());
			if (!goodsSku.getStoreId().equals(storeId)) {
				continue;
			}
			// 获取秒杀活动时间段
			DateTime startTime = DateUtil.offsetHour(seckill.getStartTime(), seckillApply.getTimeLine());
			// 检测是否可以发布促销商品
			checkSeckillGoodsSku(seckill, seckillApply, goodsSku, startTime);
			// 设置秒杀申请默认内容
			seckillApply.setOriginalPrice(goodsSku.getPrice());
			seckillApply.setPromotionApplyStatus(PromotionsApplyStatusEnum.PASS.name());
			seckillApply.setSalesNum(0);
			originList.add(seckillApply);
			// 获取促销商品
			PromotionGoods promotionGoods = this.setSeckillGoods(goodsSku, seckillApply, seckill);
			promotionGoodsList.add(promotionGoods);
		}
		this.remove(new LambdaQueryWrapper<SeckillApply>()
			.eq(SeckillApply::getSeckillId, seckillId)
			.in(SeckillApply::getSkuId, skuIds));
		this.saveBatch(originList);
		this.seckillService.updateEsGoodsSeckill(seckill, originList);
		// 保存促销活动商品信息
		if (!promotionGoodsList.isEmpty()) {
			PromotionGoodsPageQuery searchParams = new PromotionGoodsPageQuery();
			searchParams.setStoreId(storeId);
			searchParams.setSkuIds(
				promotionGoodsList
					.stream()
					.map(PromotionGoods::getSkuId)
					.toList());
			promotionGoodsService.deletePromotionGoods(searchParams);
			// 初始化促销商品
			PromotionTools.promotionGoodsInit(promotionGoodsList, seckill, PromotionTypeEnum.SECKILL);
			promotionGoodsService.saveBatch(promotionGoodsList);
		}
		// 设置秒杀活动的商品数量、店铺数量
		seckillService.updateSeckillGoodsNum(seckillId);
		cache.vagueDel(CachePrefix.STORE_ID_SECKILL);
	}

	/**
	 * 批量删除秒杀活动申请
	 *
	 * @param seckillId 秒杀活动活动id
	 * @param id        id
	 */
	@Override
	public void removeSeckillApply(String seckillId, String id) {
		Seckill seckill = this.seckillService.getById(seckillId);
		if (seckill == null) {
			throw new BusinessException(ResultEnum.SECKILL_NOT_EXIST_ERROR);
		}
		SeckillApply seckillApply = this.getById(id);
		if (seckillApply == null) {
			throw new BusinessException(ResultEnum.SECKILL_APPLY_NOT_EXIST_ERROR);
		}

		// 清除秒杀活动中的商品
		this.remove(new LambdaQueryWrapper<SeckillApply>()
			.eq(SeckillApply::getSeckillId, seckillId)
			.in(SeckillApply::getId, id));

		// 删除促销商品
		this.promotionGoodsService.deletePromotionGoods(seckillId, Collections.singletonList(seckillApply.getSkuId()));
	}

	/**
	 * 检查秒杀活动申请列表参数信息
	 *
	 * @param hours            秒杀活动时间段
	 * @param seckillApplyList 秒杀活动申请列表
	 */
	private void checkSeckillApplyList(String hours, List<SeckillApplyVO> seckillApplyList) {
		List<String> existSku = new ArrayList<>();
		for (SeckillApplyVO seckillApply : seckillApplyList) {
			if (seckillApply.getPrice() > seckillApply.getOriginalPrice()) {
				throw new BusinessException(ResultEnum.SECKILL_PRICE_ERROR);
			}
			// 检查秒杀活动申请的时刻，是否存在在秒杀活动的时间段内
			String[] rangeHours = hours.split(",");
			boolean containsSame = Arrays.stream(rangeHours)
				.anyMatch(i -> i.equals(seckillApply.getTimeLine().toString()));
			if (!containsSame) {
				throw new BusinessException(ResultEnum.SECKILL_TIME_ERROR);
			}
			// 检查商品是否参加多个时间段的活动
			if (existSku.contains(seckillApply.getSkuId())) {
				throw new BusinessException(seckillApply.getGoodsName() + "该商品不能同时参加多个时间段的活动");
			} else {
				existSku.add(seckillApply.getSkuId());
			}
		}
	}

	/**
	 * 从缓存中获取秒杀活动信息
	 *
	 * @param seckillCacheKey 秒杀活动缓存键
	 * @return 秒杀活动信息
	 */
	private List<SeckillTimelineVO> getSeckillTimelineToCache(String seckillCacheKey) {
		List<SeckillTimelineVO> timelineList = new ArrayList<>();
		LambdaQueryWrapper<Seckill> queryWrapper = new LambdaQueryWrapper<>();
		// 查询当天时间段内的秒杀活动活动
		Date now = new Date();
		queryWrapper.between(BasePromotions::getStartTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now));
		queryWrapper.ge(BasePromotions::getEndTime, DateUtil.endOfDay(now));
		List<Seckill> seckillList = this.seckillService.list(queryWrapper);
		for (Seckill seckill : seckillList) {
			// 读取系统时间的时刻
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			String[] split = seckill.getHours().split(",");
			int[] hoursSored = Arrays.stream(split)
				.mapToInt(Integer::parseInt).toArray();
			Arrays.sort(hoursSored);
			for (int i = 0; i < hoursSored.length; i++) {
				SeckillTimelineVO tempTimeline = new SeckillTimelineVO();
				boolean hoursSoredHour =
					(hoursSored[i] >= hour || ((i + 1) < hoursSored.length && hoursSored[i + 1] > hour));
				if (hoursSoredHour) {
					SimpleDateFormat format = new SimpleDateFormat(DatePattern.NORM_DATE_PATTERN);
					String date = format.format(new Date());
					// 当前时间的秒数
					long currentTime = DateUtil.currentSeconds();
					// 秒杀活动的时刻
					long timeLine = DateUtil.getDateline(date + " " + hoursSored[i], "yyyy-MM-dd HH");

					Long distanceTime = timeLine - currentTime < 0 ? 0 : timeLine - currentTime;
					tempTimeline.setDistanceStartTime(distanceTime);
					tempTimeline.setStartTime(timeLine);
					tempTimeline.setTimeLine(hoursSored[i]);
					tempTimeline.setSeckillGoodsList(wrapperSeckillGoods(hoursSored[i], seckill.getId()));
					timelineList.add(tempTimeline);
				}
			}
		}
		if (CharSequenceUtil.isNotEmpty(seckillCacheKey)) {
			cache.put(seckillCacheKey, timelineList);
		}
		return timelineList;
	}

	/**
	 * 组装当时间秒杀活动的商品数据 w
	 *
	 * @param startTimeline 秒杀活动开始时刻
	 * @return 当时间秒杀活动的商品数据
	 */
	private List<SeckillGoodsVO> wrapperSeckillGoods(Integer startTimeline, String seckillId) {
		List<SeckillGoodsVO> seckillGoodsVoS = new ArrayList<>();
		List<SeckillApply> seckillApplyList =
			this.list(new LambdaQueryWrapper<SeckillApply>().eq(SeckillApply::getSeckillId, seckillId));
		if (!seckillApplyList.isEmpty()) {
			List<SeckillApply> collect = seckillApplyList
				.stream()
				.filter(i -> i.getTimeLine().equals(startTimeline)
					&& i.getPromotionApplyStatus().equals(PromotionsApplyStatusEnum.PASS.name()))
				.toList();
			for (SeckillApply seckillApply : collect) {
				GoodsSku goodsSku = goodsSkuApi.getGoodsSkuByIdFromCache(seckillApply.getSkuId());
				if (goodsSku != null) {
					SeckillGoodsVO goodsVO = new SeckillGoodsVO();
					BeanUtil.copyProperties(seckillApply, goodsVO);
					goodsVO.setGoodsImage(goodsSku.getThumbnail());
					goodsVO.setGoodsId(goodsSku.getGoodsId());
					goodsVO.setGoodsName(goodsSku.getGoodsName());
					seckillGoodsVoS.add(goodsVO);
				}
			}
		}
		return seckillGoodsVoS;
	}

	/**
	 * 检测秒杀申请的商品
	 *
	 * @param seckill      秒杀活动
	 * @param seckillApply 秒杀活动申请
	 * @param goodsSku     商品SKU
	 * @param startTime    秒杀时段开启时间
	 */
	private void checkSeckillGoodsSku(
		Seckill seckill, SeckillApplyVO seckillApply, GoodsSku goodsSku, DateTime startTime) {
		// 活动库存不能大于商品库存
		if (goodsSku.getQuantity() < seckillApply.getQuantity()) {
			throw new BusinessException(seckillApply.getGoodsName() + ",此商品库存不足");
		}
		// 查询是否在同一时间段参与了拼团活动
		if (promotionGoodsService.findInnerOverlapPromotionGoods(
			PromotionTypeEnum.PINTUAN.name(),
			goodsSku.getId(),
			startTime,
			seckill.getEndTime(),
			seckill.getId())
			> 0) {
			throw new BusinessException("商品[" + goodsSku.getGoodsName() + "]已经在重叠的时间段参加了拼团活动，不能参加秒杀活动");
		}
		// 查询是否在同一时间段参与了秒杀活动活动
		if (promotionGoodsService.findInnerOverlapPromotionGoods(
			PromotionTypeEnum.SECKILL.name(),
			goodsSku.getId(),
			startTime,
			seckill.getEndTime(),
			seckill.getId())
			> 0) {
			throw new BusinessException("商品[" + goodsSku.getGoodsName() + "]已经在重叠的时间段参加了秒杀活动，不能参加秒杀活动活动");
		}
	}

	/**
	 * 获取秒杀活动促销商品
	 *
	 * @param goodsSku     商品SKU
	 * @param seckillApply 秒杀活动申请
	 * @param seckill      秒杀活动
	 * @return 秒杀活动促销商品
	 */
	private PromotionGoods setSeckillGoods(GoodsSku goodsSku, SeckillApply seckillApply, Seckill seckill) {
		// 设置促销商品默认内容
		PromotionGoods promotionGoods = new PromotionGoods(goodsSku);
		promotionGoods.setPrice(seckillApply.getPrice());
		promotionGoods.setQuantity(seckillApply.getQuantity());
		// 设置单独每个促销商品的结束时间
		DateTime startTime =
			DateUtil.offsetHour(DateUtil.beginOfDay(seckill.getStartTime()), seckillApply.getTimeLine());
		promotionGoods.setStartTime(startTime);
		promotionGoods.setEndTime(seckill.getEndTime());
		return promotionGoods;
	}
}
