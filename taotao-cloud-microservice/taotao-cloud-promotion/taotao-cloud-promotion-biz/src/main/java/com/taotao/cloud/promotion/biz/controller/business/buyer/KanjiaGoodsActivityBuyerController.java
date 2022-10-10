package com.taotao.cloud.promotion.biz.controller.business.buyer;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.promotion.api.model.query.KanJiaActivityLogPageQuery;
import com.taotao.cloud.promotion.api.model.query.KanjiaActivityPageQuery;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.vo.kanjia.KanjiaActivityGoodsListVO;
import com.taotao.cloud.promotion.api.model.vo.kanjia.KanjiaActivityGoodsParams;
import com.taotao.cloud.promotion.api.model.vo.kanjia.KanjiaActivityGoodsVO;
import com.taotao.cloud.promotion.api.model.vo.kanjia.KanjiaActivitySearchQuery;
import com.taotao.cloud.promotion.api.model.vo.kanjia.KanjiaActivityVO;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivity;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityLog;
import com.taotao.cloud.promotion.biz.service.business.KanjiaActivityGoodsService;
import com.taotao.cloud.promotion.biz.service.business.KanjiaActivityLogService;
import com.taotao.cloud.promotion.biz.service.business.KanjiaActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	private KanjiaActivityGoodsService kanJiaActivityGoodsService;
	/**
	 * 帮砍记录
	 */
	@Autowired
	private KanjiaActivityLogService kanJiaActivityLogService;
	/**
	 * 砍价活动
	 */
	@Autowired
	private KanjiaActivityService kanJiaActivityService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping
	@Operation(summary = "分页获取砍价商品")
	public Result<IPage<KanjiaActivityGoodsListVO>> kanjiaActivityGoodsPage(
		KanjiaActivityGoodsParams kanjiaActivityGoodsParams, PageVO page) {
		// 会员端查询到的肯定是已经开始的活动商品
		kanjiaActivityGoodsParams.setPromotionStatus(PromotionsStatusEnum.START.name());
		kanjiaActivityGoodsParams.setStartTime(System.currentTimeMillis());
		kanjiaActivityGoodsParams.setEndTime(System.currentTimeMillis());
		return Result.success(
			kanJiaActivityGoodsService.kanjiaGoodsVOPage(kanjiaActivityGoodsParams, page));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/{id}")
	@Operation(summary = "获取砍价活动商品")
	public Result<KanjiaActivityGoodsVO> getKanjiaActivityGoods(
		@Parameter(name = "砍价活动商品ID") @PathVariable String id) {
		return Result.success(kanJiaActivityGoodsService.getKanJiaGoodsVO(id));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/getKanjiaActivity/logs")
	@Operation(summary = "分页获取砍价活动-帮砍记录")
	public Result<IPage<KanjiaActivityLog>> getKanjiaActivityLog(
		KanJiaActivityLogPageQuery kanJiaActivityLogPageQuery, PageVO page) {
		return Result.success(kanJiaActivityLogService.getForPage(kanJiaActivityLogPageQuery, page));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PostMapping("/getKanjiaActivity")
	@Operation(summary = "获取砍价活动")
	public Result<KanjiaActivityVO> getKanJiaActivity(
		KanjiaActivitySearchQuery kanjiaActivitySearchParams) {
		//如果是非被邀请关系则填写会员ID
		if (StrUtil.isEmpty(kanjiaActivitySearchParams.getKanjiaActivityId())) {
			kanjiaActivitySearchParams.setMemberId(UserContext.getCurrentUser().getId());
		}
		return Result.success(
			kanJiaActivityService.getKanjiaActivityVO(kanjiaActivitySearchParams));
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
	public Result<KanjiaActivityLog> helpKanJia(
		@Parameter(name = "砍价活动ID") @PathVariable String kanjiaActivityId) {
		KanjiaActivityLog kanjiaActivityLog = kanJiaActivityService.helpKanJia(kanjiaActivityId);
		return Result.success(kanjiaActivityLog);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/kanjiaActivity/mine/")
	@Operation(summary = "分页获取已参与的砍价活动")
	public Result<IPage<KanjiaActivity>> getPointsGoodsPage(
		KanjiaActivityPageQuery kanjiaActivityPageQuery, PageVO page) {
		// 会员端查询到的肯定是已经开始的活动商品
		kanjiaActivityPageQuery.setMemberId(UserContext.getCurrentUser().getId());
		IPage<KanjiaActivity> kanjiaActivity = kanJiaActivityService.getForPage(kanjiaActivityPageQuery,
			page);
		return Result.success(kanjiaActivity);
	}

}
