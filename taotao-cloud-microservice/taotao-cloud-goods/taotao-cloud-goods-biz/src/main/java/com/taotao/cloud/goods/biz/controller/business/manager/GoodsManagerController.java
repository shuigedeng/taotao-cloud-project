package com.taotao.cloud.goods.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.model.query.GoodsPageQuery;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuParamsVO;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuVO;
import com.taotao.cloud.goods.api.model.vo.GoodsVO;
import com.taotao.cloud.goods.biz.model.entity.Goods;
import com.taotao.cloud.goods.biz.model.entity.GoodsSku;
import com.taotao.cloud.goods.biz.service.business.IGoodsService;
import com.taotao.cloud.goods.biz.service.business.IGoodsSkuService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
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
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-商品管理API", description = "平台管理端-商品管理API")
@RequestMapping("/goods/manager/goods")
public class GoodsManagerController {

	/**
	 * 商品服务
	 */
	private final IGoodsService goodsService;
	/**
	 * 规格商品服务
	 */
	private final IGoodsSkuService goodsSkuService;

	@Operation(summary = "分页获取", description = "分页获取")
	@RequestLogger("分页获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<PageResult<GoodsVO>> getByPage(
			@Validated GoodsPageQuery goodsPageQuery) {
		IPage<Goods> goodsPage = goodsService.goodsQueryPage(goodsPageQuery);
		return Result.success(PageResult.convertMybatisPage(goodsPage, GoodsVO.class));
	}

	@Operation(summary = "分页获取商品列表", description = "分页获取商品列表")
	@RequestLogger("分页获取商品列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/sku/page")
	public Result<PageResult<GoodsSkuVO>> getSkuByPage(
			@Validated GoodsPageQuery goodsPageQuery) {
		IPage<GoodsSku> goodsSkuPage = goodsSkuService.goodsSkuQueryPage(goodsPageQuery);
		return Result.success(PageResult.convertMybatisPage(goodsSkuPage, GoodsSkuVO.class));
	}

	@Operation(summary = "分页获取待审核商品", description = "分页获取待审核商品")
	@RequestLogger("分页获取待审核商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/auth/page")
	public Result<PageResult<GoodsVO>> getAuthPage(@Validated GoodsPageQuery goodsPageQuery) {
		goodsPageQuery.setAuthFlag(GoodsAuthEnum.TOBEAUDITED.name());
		IPage<Goods> goodsPage = goodsService.goodsQueryPage(goodsPageQuery);
		return Result.success(PageResult.convertMybatisPage(goodsPage, GoodsVO.class));
	}

	@Operation(summary = "管理员下架商品", description = "管理员下架商品")
	@RequestLogger("管理员下架商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/{goodsId}/under")
	public Result<Boolean> underGoods(@PathVariable Long goodsId,
			@NotEmpty(message = "下架原因不能为空") @RequestParam String reason) {
		List<Long> goodsIds = List.of(goodsId);
		return Result.success(
				goodsService.managerUpdateGoodsMarketAble(goodsIds, GoodsStatusEnum.DOWN, reason));
	}

	@Operation(summary = "管理员审核商品", description = "管理员审核商品")
	@RequestLogger("管理员审核商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "{goodsIds}/auth")
	public Result<Boolean> auth(@PathVariable List<Long> goodsIds,
			@RequestParam String authFlag) {
		//校验商品是否存在
		return Result.success(goodsService.auditGoods(goodsIds, GoodsAuthEnum.valueOf(authFlag)));
	}

	@Operation(summary = "管理员上架商品", description = "管理员上架商品")
	@RequestLogger("管理员上架商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/{goodsId}/up")
	public Result<Boolean> unpGoods(@PathVariable List<Long> goodsId) {
		return Result.success(
				goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.UPPER, ""));
	}

	@Operation(summary = "通过id获取商品详情", description = "通过id获取商品详情")
	@RequestLogger("通过id获取商品详情")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<GoodsSkuParamsVO> get(@PathVariable Long id) {
		return Result.success(goodsService.getGoodsVO(id));
	}

}
