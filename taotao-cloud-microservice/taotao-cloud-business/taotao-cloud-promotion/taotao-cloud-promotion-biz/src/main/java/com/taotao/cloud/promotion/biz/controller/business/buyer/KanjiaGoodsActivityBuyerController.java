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

package com.taotao.cloud.promotion.biz.controller.business.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuSpecGalleryVO;
import com.taotao.cloud.promotion.api.enums.KanJiaStatusEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.page.KanJiaActivityLogPageQuery;
import com.taotao.cloud.promotion.api.model.page.KanjiaActivityGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.page.KanjiaActivityPageQuery;
import com.taotao.cloud.promotion.api.model.query.KanjiaActivitySearchQuery;
import com.taotao.cloud.promotion.api.model.vo.KanjiaActivityGoodsListVO;
import com.taotao.cloud.promotion.api.model.vo.KanjiaActivityGoodsVO;
import com.taotao.cloud.promotion.api.model.vo.KanjiaActivityLogVO;
import com.taotao.cloud.promotion.api.model.vo.KanjiaActivityVO;
import com.taotao.cloud.promotion.biz.model.bo.KanjiaActivityGoodsBO;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivity;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityGoods;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityLog;
import com.taotao.cloud.promotion.biz.service.business.IKanjiaActivityGoodsService;
import com.taotao.cloud.promotion.biz.service.business.IKanjiaActivityLogService;
import com.taotao.cloud.promotion.biz.service.business.IKanjiaActivityService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 买家端,砍价活动商品
 */
@RestController
@Tag(name = "买家端,砍价商品接口")
@RequestMapping("/buyer/promotion/kanjiaGoods")
public class KanjiaGoodsActivityBuyerController {

	/**
	 * 砍价活动商品
	 */
	@Autowired
	private IKanjiaActivityGoodsService kanJiaActivityGoodsService;
	/**
	 * 帮砍记录
	 */
	@Autowired
	private IKanjiaActivityLogService kanJiaActivityLogService;
	/**
	 * 砍价活动
	 */
	@Autowired
	private IKanjiaActivityService kanJiaActivityService;
	@Autowired
	private IKanjiaActivityLogService kanjiaActivityLogService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping
	@Operation(summary = "分页获取砍价商品")
	public Result<PageResult<KanjiaActivityGoodsListVO>> kanjiaActivityGoodsPage(
		KanjiaActivityGoodsPageQuery pageQuery) {
		// 会员端查询到的肯定是已经开始的活动商品
		pageQuery.setPromotionStatus(PromotionsStatusEnum.START.name());
		pageQuery.setStartTime(System.currentTimeMillis());
		pageQuery.setEndTime(System.currentTimeMillis());
		IPage<KanjiaActivityGoodsBO> kanjiaGoodsPage = kanJiaActivityGoodsService.kanjiaGoodsPage(pageQuery, pageQuery.getPageParm());
		return Result.success(MpUtils.convertMybatisPage(kanjiaGoodsPage, KanjiaActivityGoodsListVO.class));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/{id}")
	@Operation(summary = "获取砍价活动商品")
	public Result<KanjiaActivityGoodsVO> getKanjiaActivityGoods(@Parameter(name = "砍价活动商品ID") @PathVariable String id) {
		KanjiaActivityGoodsVO kanJiaActivityGoodsVO = new KanjiaActivityGoodsVO();
		// 获取砍价商品
		KanjiaActivityGoods kanJiaActivityGoods = this.getById(id);
		// 获取商品SKU
		GoodsSkuSpecGalleryVO goodsSku = this.goodsSkuApi.getGoodsSkuByIdFromCache(kanJiaActivityGoods.getSkuId());
		// 填写活动商品价格、剩余数量
		kanJiaActivityGoodsVO.setGoodsSku(goodsSku);
		kanJiaActivityGoodsVO.setStock(kanJiaActivityGoods.getStock());
		kanJiaActivityGoodsVO.setPurchasePrice(kanJiaActivityGoods.getPurchasePrice());
		// 返回商品数据
		return Result.success(kanJiaActivityGoodsVO);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/getKanjiaActivity/logs")
	@Operation(summary = "分页获取砍价活动-帮砍记录")
	public Result<PageResult<KanjiaActivityLogVO>> getKanjiaActivityLog(
		KanJiaActivityLogPageQuery pageQuery) {
		IPage<KanjiaActivityLog> page = kanJiaActivityLogService.getForPage(pageQuery, pageQuery.getPageParm());
		return Result.success(MpUtils.convertMybatisPage(page, KanjiaActivityLogVO.class));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PostMapping("/getKanjiaActivity")
	@Operation(summary = "获取砍价活动")
	public Result<KanjiaActivityVO> getKanJiaActivity(KanjiaActivitySearchQuery kanjiaActivitySearchParams) {
		// 如果是非被邀请关系则填写会员ID
		// if (StrUtil.isEmpty(kanjiaActivitySearchParams.getKanjiaActivityId())) {
		//     kanjiaActivitySearchParams.setMemberId(SecurityUtils.getCurrentUser().getId());
		// }

		SecurityUser authUser = Objects.requireNonNull(SecurityUtils.getCurrentUser());
		KanjiaActivity kanjiaActivity = kanJiaActivityService.getKanjiaActivity(kanjiaActivitySearchParams);
		KanjiaActivityVO kanjiaActivityVO = new KanjiaActivityVO();
		// 判断是否参与活动
		if (kanjiaActivity == null) {
			throw new BusinessException("未参加活动");
		}
		BeanUtil.copyProperties(kanjiaActivity, kanjiaActivityVO);

		// 判断是否发起了砍价活动,如果发起可参与活动
		kanjiaActivityVO.setLaunch(true);
		KanjiaActivityLog kanjiaActivityLog = kanjiaActivityLogService.queryKanjiaActivityLog(kanjiaActivity.getId(), authUser.getUserId());

		if (kanjiaActivityLog == null) {
			kanjiaActivityVO.setHelp(true);
		}

		// 判断活动已通过并且是当前用户发起的砍价则可以进行购买
		if (kanjiaActivity.getStatus().equals(KanJiaStatusEnum.SUCCESS.name())
			&& kanjiaActivity
			.getMemberId()
			.equals(SecurityUtils.getCurrentUser().getId())) {
			kanjiaActivityVO.setPass(true);
		}

		return Result.success(kanjiaActivityVO);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PostMapping
	@Operation(summary = "发起砍价活动")
	public Result<KanjiaActivityLog> launchKanJiaActivity(@Parameter(name = "砍价活动商品ID") String id) {
		KanjiaActivityLog kanjiaActivityLog = kanJiaActivityService.add(id);
		return Result.success(kanjiaActivityLog);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PostMapping("/help/{kanjiaActivityId}")
	@Operation(summary = "帮砍一刀")
	public Result<KanjiaActivityLog> helpKanJia(@Parameter(name = "砍价活动ID") @PathVariable String kanjiaActivityId) {
		KanjiaActivityLog kanjiaActivityLog = kanJiaActivityService.helpKanJia(kanjiaActivityId);
		return Result.success(kanjiaActivityLog);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/kanjiaActivity/mine/")
	@Operation(summary = "分页获取已参与的砍价活动")
	public Result<IPage<KanjiaActivity>> getPointsGoodsPage(KanjiaActivityPageQuery kanjiaActivityPageQuery) {
		// 会员端查询到的肯定是已经开始的活动商品
		// kanjiaActivityPageQuery.setMemberId(SecurityUtils.getCurrentUser().getId());
		IPage<KanjiaActivity> kanjiaActivity = kanJiaActivityService.getForPage(kanjiaActivityPageQuery, kanjiaActivityPageQuery.getPageParm());
		return Result.success(kanjiaActivity);
	}
}
