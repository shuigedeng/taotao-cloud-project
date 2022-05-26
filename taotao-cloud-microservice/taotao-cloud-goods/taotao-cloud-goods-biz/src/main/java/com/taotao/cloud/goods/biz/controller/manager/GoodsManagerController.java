package com.taotao.cloud.goods.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.query.GoodsPageQuery;
import com.taotao.cloud.goods.api.vo.GoodsBaseVO;
import com.taotao.cloud.goods.api.vo.GoodsSkuBaseVO;
import com.taotao.cloud.goods.api.vo.GoodsVO;
import com.taotao.cloud.goods.biz.entity.Goods;
import com.taotao.cloud.goods.biz.entity.GoodsSku;
import com.taotao.cloud.goods.biz.service.IGoodsService;
import com.taotao.cloud.goods.biz.service.IGoodsSkuService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;

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
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<PageModel<GoodsBaseVO>> getByPage(
		@Validated GoodsPageQuery goodsPageQuery) {
		IPage<Goods> goodsPage = goodsService.queryByParams(goodsPageQuery);
		return Result.success(PageModel.convertMybatisPage(goodsPage, GoodsBaseVO.class));
	}

	@Operation(summary = "分页获取商品列表", description = "分页获取商品列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/sku/page")
	public Result<PageModel<GoodsSkuBaseVO>> getSkuByPage(
		@Validated GoodsPageQuery goodsPageQuery) {
		IPage<GoodsSku> goodsSkuPage = goodsSkuService.getGoodsSkuByPage(goodsPageQuery);
		return Result.success(PageModel.convertMybatisPage(goodsSkuPage, GoodsSkuBaseVO.class));
	}

	@Operation(summary = "分页获取待审核商品", description = "分页获取待审核商品")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/auth/page")
	public Result<PageModel<GoodsBaseVO>> getAuthPage(@Validated GoodsPageQuery goodsPageQuery) {
		goodsPageQuery.setAuthFlag(GoodsAuthEnum.TOBEAUDITED.name());
		IPage<Goods> goodsPage = goodsService.queryByParams(goodsPageQuery);
		return Result.success(PageModel.convertMybatisPage(goodsPage, GoodsBaseVO.class));
	}

	@Operation(summary = "管理员下架商品", description = "管理员下架商品")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/{goodsId}/under")
	public Result<Boolean> underGoods(@PathVariable Long goodsId,
		@NotEmpty(message = "下架原因不能为空") @RequestParam String reason) {
		List<Long> goodsIds = List.of(goodsId);
		return Result.success(
			goodsService.managerUpdateGoodsMarketAble(goodsIds, GoodsStatusEnum.DOWN, reason));
	}

	@Operation(summary = "管理员审核商品", description = "管理员审核商品")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "{goodsIds}/auth")
	public Result<Boolean> auth(@PathVariable List<Long> goodsIds,
		@RequestParam String authFlag) {
		//校验商品是否存在
		return Result.success(goodsService.auditGoods(goodsIds, GoodsAuthEnum.valueOf(authFlag)));
	}

	@Operation(summary = "管理员上架商品", description = "管理员上架商品")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/{goodsId}/up")
	public Result<Boolean> unpGoods(@PathVariable List<Long> goodsId) {
		return Result.success(
			goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.UPPER, ""));
	}

	@Operation(summary = "通过id获取商品详情", description = "通过id获取商品详情")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<GoodsVO> get(@PathVariable Long id) {
		return Result.success(goodsService.getGoodsVO(id));
	}

}
