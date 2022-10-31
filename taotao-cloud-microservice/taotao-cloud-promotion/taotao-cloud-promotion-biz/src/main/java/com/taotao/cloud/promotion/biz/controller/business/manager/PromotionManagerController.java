package com.taotao.cloud.promotion.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.query.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.business.PromotionGoodsService;
import com.taotao.cloud.promotion.biz.service.business.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,促销接口
 *
 * @since 2021/2/2
 */
@RestController
@Tag(name = "管理端,促销接口")
@RequestMapping("/manager/promotion")
public class PromotionManagerController {

	@Autowired
	private PromotionService promotionService;
	@Autowired
	private PromotionGoodsService promotionGoodsService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/current")
	@Operation(summary = "获取当前进行中的促销活动")
	public Result<Map<String, Object>> getCurrentPromotion() {
		Map<String, Object> currentPromotion = promotionService.getCurrentPromotion();
		return Result.success(currentPromotion);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/{promotionId}/goods")
	@Operation(summary = "获取当前进行中的促销活动商品")
	public Result<IPage<PromotionGoods>> getPromotionGoods(@PathVariable String promotionId,
		String promotionType, PageVO pageVO) {
		PromotionGoodsPageQuery searchParams = new PromotionGoodsPageQuery();
		searchParams.setPromotionId(promotionId);
		searchParams.setPromotionType(promotionType);
		searchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
		IPage<PromotionGoods> promotionGoods = promotionGoodsService.pageFindAll(searchParams,
			pageVO);
		return Result.success(promotionGoods);
	}


}
