package com.taotao.cloud.goods.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.dto.GoodsSearchParams;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.vo.GoodsVO;
import com.taotao.cloud.goods.biz.entity.Goods;
import com.taotao.cloud.goods.biz.entity.GoodsSku;
import com.taotao.cloud.goods.biz.service.GoodsService;
import com.taotao.cloud.goods.biz.service.GoodsSkuService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,商品管理接口
 */
@Validated
@RestController
@Tag(name = "平台管理端-商品管理API", description = "平台管理端-商品管理API")
@RequestMapping("/goods/manager/goods")
public class GoodsManagerController {

	/**
	 * 商品
	 */
	@Autowired
	private GoodsService goodsService;
	/**
	 * 规格商品
	 */
	@Autowired
	private GoodsSkuService goodsSkuService;

	@Operation(summary = "分页获取", description = "分页获取", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<IPage<Goods>> getByPage(GoodsSearchParams goodsSearchParams) {
		return Result.success(goodsService.queryByParams(goodsSearchParams));
	}

	@Operation(summary = "分页获取商品列表", description = "分页获取商品列表", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取商品列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/sku/page")
	public Result<IPage<GoodsSku>> getSkuByPage(GoodsSearchParams goodsSearchParams) {
		return Result.success(goodsSkuService.getGoodsSkuByPage(goodsSearchParams));
	}

	@Operation(summary = "分页获取待审核商品", description = "分页获取待审核商品", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取待审核商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/auth/page")
	public Result<IPage<Goods>> getAuthPage(GoodsSearchParams goodsSearchParams) {
		goodsSearchParams.setAuthFlag(GoodsAuthEnum.TOBEAUDITED.name());
		return Result.success(goodsService.queryByParams(goodsSearchParams));
	}

	@Operation(summary = "管理员下架商品", description = "管理员下架商品", method = CommonConstant.PUT)
	@RequestLogger(description = "管理员下架商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/{goodsId}/under")
	public Result<Object> underGoods(@PathVariable String goodsId,
		@NotEmpty(message = "下架原因不能为空") @RequestParam String reason) {
		List<String> goodsIds = Arrays.asList(goodsId.split(","));
		if (Boolean.TRUE.equals(
			goodsService.managerUpdateGoodsMarketAble(goodsIds, GoodsStatusEnum.DOWN, reason))) {
			return Result.success();
		}
		throw new ServiceException(ResultCode.GOODS_UNDER_ERROR);
	}

	@Operation(summary = "管理员审核商品", description = "管理员审核商品", method = CommonConstant.PUT)
	@RequestLogger(description = "管理员审核商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "{goodsIds}/auth")
	public Result<Object> auth(@PathVariable List<String> goodsIds,
		@RequestParam String authFlag) {
		//校验商品是否存在
		if (goodsService.auditGoods(goodsIds, GoodsAuthEnum.valueOf(authFlag))) {
			return Result.success;
		}
		throw new ServiceException(ResultCode.GOODS_AUTH_ERROR);
	}

	@Operation(summary = "管理员上架商品", description = "管理员上架商品", method = CommonConstant.PUT)
	@RequestLogger(description = "管理员上架商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/{goodsId}/up")
	public Result<Object> unpGoods(@PathVariable List<String> goodsId) {
		if (goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.UPPER, "")) {
			return Result.success();
		}
		throw new ServiceException(ResultCode.GOODS_UPPER_ERROR);
	}

	@Operation(summary = "通过id获取商品详情", description = "通过id获取商品详情", method = CommonConstant.GET)
	@RequestLogger(description = "通过id获取商品详情")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<GoodsVO> get(@PathVariable String id) {
		GoodsVO goods = goodsService.getGoodsVO(id);
		return Result.success(goods);
	}

}
