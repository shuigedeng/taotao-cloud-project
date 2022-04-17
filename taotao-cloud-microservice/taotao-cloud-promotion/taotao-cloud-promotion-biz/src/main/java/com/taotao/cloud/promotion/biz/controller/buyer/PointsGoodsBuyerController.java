package com.taotao.cloud.promotion.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.promotion.api.vo.PointsGoodsSearchParams;
import com.taotao.cloud.promotion.api.vo.PointsGoodsVO;
import com.taotao.cloud.promotion.biz.entity.PointsGoods;
import com.taotao.cloud.promotion.biz.entity.PointsGoodsCategory;
import com.taotao.cloud.promotion.biz.service.PointsGoodsCategoryService;
import com.taotao.cloud.promotion.biz.service.PointsGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,积分商品接口
 *
 * @since 2021/1/19
 **/
@RestController
@Tag(name = "买家端,积分商品接口")
@RequestMapping("/buyer/promotion/pointsGoods")
public class PointsGoodsBuyerController {

	@Autowired
	private PointsGoodsService pointsGoodsService;
	@Autowired
	private PointsGoodsCategoryService pointsGoodsCategoryService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping
	@Operation(summary = "分页获取积分商品")
	public Result<IPage<PointsGoods>> getPointsGoodsPage(PointsGoodsSearchParams searchParams,
		PageVO page) {
		IPage<PointsGoods> pointsGoodsByPage = pointsGoodsService.pageFindAll(searchParams, page);
		return Result.success(pointsGoodsByPage);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/category")
	@Operation(summary = "获取积分商品分类分页")
	public Result<IPage<PointsGoodsCategory>> page(String name, PageVO page) {
		return Result.success(pointsGoodsCategoryService.getCategoryByPage(name, page));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/{id}")
	@Operation(summary = "获取积分活动商品")
	public Result<PointsGoodsVO> getPointsGoodsPage(
		@Parameter(name = "积分商品ID") @PathVariable String id) {
		return Result.success(pointsGoodsService.getPointsGoodsDetail(id));
	}

}
