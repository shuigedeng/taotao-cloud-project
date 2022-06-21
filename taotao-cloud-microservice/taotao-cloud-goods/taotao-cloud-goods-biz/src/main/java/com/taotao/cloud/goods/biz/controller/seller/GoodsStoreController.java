package com.taotao.cloud.goods.biz.controller.seller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.goods.api.dto.GoodsOperationDTO;
import com.taotao.cloud.goods.api.dto.GoodsSkuStockDTO;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.query.GoodsPageQuery;
import com.taotao.cloud.goods.api.vo.GoodsBaseVO;
import com.taotao.cloud.goods.api.vo.GoodsSkuBaseVO;
import com.taotao.cloud.goods.api.vo.GoodsSkuVO;
import com.taotao.cloud.goods.api.vo.GoodsVO;
import com.taotao.cloud.goods.api.vo.StockWarningVO;
import com.taotao.cloud.goods.biz.entity.Goods;
import com.taotao.cloud.goods.biz.entity.GoodsSku;
import com.taotao.cloud.goods.biz.service.IGoodsService;
import com.taotao.cloud.goods.biz.service.IGoodsSkuService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.store.api.feign.IFeignStoreDetailService;
import com.taotao.cloud.store.api.vo.StoreDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 店铺端,商品接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:09:23
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-商品API", description = "店铺端-商品API")
@RequestMapping("/goods/seller/goods")
public class GoodsStoreController {

	/**
	 * 商品
	 */
	private final IGoodsService goodsService;
	/**
	 * 商品sku
	 */
	private final IGoodsSkuService goodsSkuService;
	/**
	 * 店铺详情
	 */
	private final IFeignStoreDetailService storeDetailService;

	@Operation(summary = "分页获取商品列表", description = "分页获取商品列表")
	@RequestLogger("分页获取商品列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<PageModel<GoodsBaseVO>> getByPage(GoodsPageQuery goodsPageQuery) {
		//当前登录商家账号
		Long storeId = SecurityUtil.getCurrentUser().getStoreId();
		goodsPageQuery.setStoreId(storeId);
		IPage<Goods> goodsPage = goodsService.queryByParams(goodsPageQuery);
		return Result.success(PageModel.convertMybatisPage(goodsPage, GoodsBaseVO.class));
	}

	@Operation(summary = "分页获取商品Sku列表", description = "分页获取商品Sku列表")
	@RequestLogger("分页获取商品Sku列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/sku/page")
	public Result<PageModel<GoodsSkuBaseVO>> getSkuByPage(GoodsPageQuery goodsPageQuery) {
		//当前登录商家账号
		Long storeId = SecurityUtil.getCurrentUser().getStoreId();
		goodsPageQuery.setStoreId(storeId);
		IPage<GoodsSku> goodsSkuPage = goodsSkuService.getGoodsSkuByPage(goodsPageQuery);
		return Result.success(PageModel.convertMybatisPage(goodsSkuPage, GoodsSkuBaseVO.class));
	}

	@Operation(summary = "分页获取库存告警商品列表", description = "分页获取库存告警商品列表")
	@RequestLogger("分页获取库存告警商品列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/stock/warning")
	public Result<StockWarningVO> getWarningStockByPage(GoodsPageQuery goodsPageQuery) {
		//当前登录商家账号
		Long storeId = SecurityUtil.getCurrentUser().getStoreId();
		StoreDetailVO storeDetail = storeDetailService.getStoreDetailVO(storeId).data();
		//库存预警数量
		Integer stockWarnNum = storeDetail.getStockWarning();
		goodsPageQuery.setStoreId(storeId);
		goodsPageQuery.setLeQuantity(stockWarnNum);
		goodsPageQuery.setMarketEnable(GoodsStatusEnum.UPPER.name());
		//商品SKU列表
		IPage<GoodsSku> goodsSkuPage = goodsSkuService.getGoodsSkuByPage(goodsPageQuery);
		StockWarningVO stockWarning = new StockWarningVO(stockWarnNum,
			PageModel.convertMybatisPage(goodsSkuPage, GoodsSkuBaseVO.class));
		return Result.success(stockWarning);
	}

	@Operation(summary = "通过id获取", description = "通过id获取")
	@RequestLogger("通过id获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{goodsId}")
	public Result<GoodsVO> get(@PathVariable Long goodsId) {
		return Result.success(goodsService.getGoodsVO(goodsId));
	}

	@Operation(summary = "新增商品", description = "新增商品")
	@RequestLogger("新增商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(
		@Validated @RequestBody GoodsOperationDTO goodsOperationDTO) {
		return Result.success(goodsService.addGoods(goodsOperationDTO));
	}

	@Operation(summary = "修改商品", description = "修改商品")
	@RequestLogger("修改商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/{goodsId}")
	public Result<Boolean> update(@RequestBody GoodsOperationDTO goodsOperationDTO,
		@PathVariable Long goodsId) {
		return Result.success(goodsService.editGoods(goodsOperationDTO, goodsId));
	}

	@Operation(summary = "下架商品", description = "下架商品")
	@RequestLogger("下架商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/under")
	public Result<Boolean> underGoods(@RequestParam List<Long> goodsId) {
		return Result.success(
			goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.DOWN, "商家下架"));
	}

	@Operation(summary = "上架商品", description = "上架商品")
	@RequestLogger("上架商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/up")
	public Result<Boolean> unpGoods(@RequestParam List<Long> goodsId) {
		return Result.success(
			goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.UPPER, ""));
	}

	@Operation(summary = "删除商品", description = "删除商品")
	@RequestLogger("删除商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping
	public Result<Boolean> deleteGoods(@RequestParam List<Long> goodsId) {
		return Result.success(goodsService.deleteGoods(goodsId));
	}

	@Operation(summary = "设置商品运费模板", description = "设置商品运费模板")
	@RequestLogger("设置商品运费模板")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/freight")
	public Result<Boolean> freight(@RequestParam List<Long> goodsId,
		@RequestParam Long templateId) {
		return Result.success(goodsService.freight(goodsId, templateId));
	}

	@Operation(summary = "根据goodsId分页获取商品规格列表", description = "根据goodsId分页获取商品规格列表")
	@RequestLogger("根据goodsId分页获取商品规格列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/sku/{goodsId}/page")
	public Result<List<GoodsSkuVO>> getSkuByList(@PathVariable Long goodsId) {
		Long storeId = SecurityUtil.getCurrentUser().getStoreId();
		return Result.success(goodsSkuService.getGoodsSkuVOList(goodsSkuService.list(
			new LambdaQueryWrapper<GoodsSku>().eq(GoodsSku::getGoodsId, goodsId)
				.eq(GoodsSku::getStoreId, storeId))));
	}

	@Operation(summary = "修改商品库存", description = "修改商品库存")
	@RequestLogger("修改商品库存")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/stocks")
	public Result<Boolean> updateStocks(@RequestBody List<GoodsSkuStockDTO> updateStockList) {
		Long storeId = SecurityUtil.getCurrentUser().getStoreId();
		// 获取商品skuId集合
		List<Long> goodsSkuIds = updateStockList.stream().map(GoodsSkuStockDTO::getSkuId)
			.collect(Collectors.toList());
		// 根据skuId集合查询商品信息
		List<GoodsSku> goodsSkuList = goodsSkuService.list(
			new LambdaQueryWrapper<GoodsSku>().in(GoodsSku::getId, goodsSkuIds)
				.eq(GoodsSku::getStoreId, storeId));
		// 过滤不符合当前店铺的商品
		List<Long> filterGoodsSkuIds = goodsSkuList.stream().map(GoodsSku::getId).toList();
		List<GoodsSkuStockDTO> collect = updateStockList.stream()
			.filter(i -> filterGoodsSkuIds.contains(i.getSkuId())).collect(Collectors.toList());
		return Result.success(goodsSkuService.updateStocks(collect));
	}

}
