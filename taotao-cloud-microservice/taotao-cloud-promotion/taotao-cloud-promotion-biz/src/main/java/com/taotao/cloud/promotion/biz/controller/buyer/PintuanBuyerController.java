package com.taotao.cloud.promotion.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.vo.PintuanMemberVO;
import com.taotao.cloud.promotion.api.vo.PintuanShareVO;
import com.taotao.cloud.promotion.api.query.PromotionGoodsSearchParams;
import com.taotao.cloud.promotion.biz.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.PintuanService;
import com.taotao.cloud.promotion.biz.service.PromotionGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,拼团接口
 *
 * @since 2021/2/20
 */
@Tag(name = "买家端,拼团接口")
@RestController
@RequestMapping("/buyer/promotion/pintuan")
public class PintuanBuyerController {

	@Autowired
	private PromotionGoodsService promotionGoodsService;
	@Autowired
	private PintuanService pintuanService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取拼团商品")
	@GetMapping
	public Result<IPage<PromotionGoods>> getPintuanCategory(String goodsName, String categoryPath,
		PageVO pageVo) {
		PromotionGoodsSearchParams searchParams = new PromotionGoodsSearchParams();
		searchParams.setGoodsName(goodsName);
		searchParams.setPromotionType(PromotionTypeEnum.PINTUAN.name());
		searchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
		searchParams.setCategoryPath(categoryPath);
		return Result.success(promotionGoodsService.pageFindAll(searchParams, pageVo));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取当前拼团活动的未成团的会员")
	@GetMapping("/{pintuanId}/members")
	public Result<List<PintuanMemberVO>> getPintuanMember(@PathVariable String pintuanId) {
		return Result.success(pintuanService.getPintuanMember(pintuanId));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取当前拼团订单的拼团分享信息")
	@GetMapping("/share")
	public Result<PintuanShareVO> share(String parentOrderSn, String skuId) {
		return Result.success(pintuanService.getPintuanShareInfo(parentOrderSn, skuId));
	}

}
