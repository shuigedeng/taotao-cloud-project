package com.taotao.cloud.goods.biz.controller.seller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.goods.api.dto.GoodsOperationDTO;
import com.taotao.cloud.goods.api.dto.GoodsSearchParams;
import com.taotao.cloud.goods.api.dto.GoodsSkuStockDTO;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.vo.GoodsBaseVO;
import com.taotao.cloud.goods.api.vo.GoodsSkuBaseVO;
import com.taotao.cloud.goods.api.vo.GoodsSkuVO;
import com.taotao.cloud.goods.api.vo.GoodsVO;
import com.taotao.cloud.goods.api.vo.StockWarningVO;
import com.taotao.cloud.goods.biz.entity.Goods;
import com.taotao.cloud.goods.biz.entity.GoodsSku;
import com.taotao.cloud.goods.biz.service.GoodsService;
import com.taotao.cloud.goods.biz.service.GoodsSkuService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * 店铺端,商品接口
 */
@Validated
@RestController
@Tag(name = "商户管理端-商品管理API", description = "商户管理端-商品管理API")
@RequestMapping("/goods/seller/store/goods/goods")
public class GoodsStoreController {

	/**
	 * 商品
	 */
	@Autowired
	private GoodsService goodsService;
	/**
	 * 商品sku
	 */
	@Autowired
	private GoodsSkuService goodsSkuService;

	/**
	 * 店铺详情
	 */
	//@Autowired
	//private StoreDetailService storeDetailService;
	@Operation(summary = "分页获取商品列表", description = "分页获取商品列表", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取商品列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<PageModel<GoodsBaseVO>> getByPage(GoodsSearchParams goodsSearchParams) {
		//获取当前登录商家账号
		String storeId = Objects.requireNonNull(SecurityUtil.getUser()).getStoreId();
		goodsSearchParams.setStoreId(storeId);
		return Result.success(goodsService.queryByParams(goodsSearchParams));
	}

	@Operation(summary = "分页获取商品Sku列表", description = "分页获取商品Sku列表", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取商品Sku列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/sku/page")
	public Result<PageModel<GoodsSkuBaseVO>> getSkuByPage(GoodsSearchParams goodsSearchParams) {
		//获取当前登录商家账号
		String storeId = Objects.requireNonNull(SecurityUtil.getUser()).getStoreId();
		goodsSearchParams.setStoreId(storeId);
		return Result.success(goodsSkuService.getGoodsSkuByPage(goodsSearchParams));
	}

	@Operation(summary = "分页获取库存告警商品列表", description = "分页获取库存告警商品列表", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取库存告警商品列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/list/stock/page")
	public Result<StockWarningVO> getWarningStockByPage(GoodsSearchParams goodsSearchParams) {
		//获取当前登录商家账号
		//String storeId = Objects.requireNonNull(SecurityUtil.getUser()).getStoreId();
		//StoreDetail storeDetail = storeDetailService.getStoreDetail(storeId);
		//Integer stockWarnNum = storeDetail.getStockWarning();
		//goodsSearchParams.setStoreId(storeId);
		//goodsSearchParams.setLeQuantity(stockWarnNum);
		//goodsSearchParams.setMarketEnable(GoodsStatusEnum.UPPER.name());
		//PageModel<GoodsSku> goodsSku = goodsSkuService.getGoodsSkuByPage(goodsSearchParams);
		//StockWarningVO stockWarning = new StockWarningVO(stockWarnNum, goodsSku);
		return Result.success(null);
	}

	@Operation(summary = "通过id获取", description = "通过id获取", method = CommonConstant.GET)
	@RequestLogger(description = "通过id获取")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<GoodsVO> get(@PathVariable String id) {
		return Result.success(goodsService.getGoodsVO(id));
	}

	@Operation(summary = "新增商品", description = "新增商品", method = CommonConstant.POST)
	@RequestLogger(description = "新增商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(
		@Valid @RequestBody GoodsOperationDTO goodsOperationDTO) {
		return Result.success(goodsService.addGoods(goodsOperationDTO));
	}

	@Operation(summary = "修改商品", description = "修改商品", method = CommonConstant.PUT)
	@RequestLogger(description = "修改商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/{goodsId}")
	public Result<Boolean> update(@RequestBody GoodsOperationDTO goodsOperationDTO,
		@PathVariable String goodsId) {
		return Result.success(goodsService.editGoods(goodsOperationDTO, goodsId));
	}

	@Operation(summary = "下架商品", description = "下架商品", method = CommonConstant.PUT)
	@RequestLogger(description = "下架商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/under")
	public Result<Boolean> underGoods(@RequestParam List<String> goodsId) {
		return Result.success(
			goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.DOWN, "商家下架"));
	}

	@Operation(summary = "上架商品", description = "上架商品", method = CommonConstant.PUT)
	@RequestLogger(description = "上架商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/up")
	public Result<Boolean> unpGoods(@RequestParam List<String> goodsId) {
		return Result.success(
			goodsService.updateGoodsMarketAble(goodsId, GoodsStatusEnum.UPPER, ""));
	}

	@Operation(summary = "删除商品", description = "删除商品", method = CommonConstant.DELETE)
	@RequestLogger(description = "删除商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping
	public Result<Boolean> deleteGoods(@RequestParam List<String> goodsId) {
		return Result.success(goodsService.deleteGoods(goodsId));
	}

	@Operation(summary = "设置商品运费模板", description = "设置商品运费模板", method = CommonConstant.POST)
	@RequestLogger(description = "设置商品运费模板")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/freight")
	public Result<Boolean> freight(@RequestParam List<String> goodsId,
		@RequestParam String templateId) {
		return Result.success(goodsService.freight(goodsId, templateId));
	}

	@Operation(summary = "根据goodsId分页获取商品规格列表", description = "根据goodsId分页获取商品规格列表", method = CommonConstant.GET)
	@RequestLogger(description = "根据goodsId分页获取商品规格列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/sku/{goodsId}/page")
	public Result<List<GoodsSkuVO>> getSkuByList(@PathVariable String goodsId) {
		String storeId = Objects.requireNonNull(SecurityUtil.getUser()).getStoreId();
		return Result.success(goodsSkuService.getGoodsSkuVOList(goodsSkuService.list(
			new LambdaQueryWrapper<GoodsSku>().eq(GoodsSku::getGoodsId, goodsId)
				.eq(GoodsSku::getStoreId, storeId))));
	}

	@Operation(summary = "修改商品库存", description = "修改商品库存", method = CommonConstant.PUT)
	@RequestLogger(description = "修改商品库存")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/stocks")
	public Result<Boolean> updateStocks(@RequestBody List<GoodsSkuStockDTO> updateStockList) {
		String storeId = Objects.requireNonNull(SecurityUtil.getUser()).getStoreId();
		// 获取商品skuId集合
		List<String> goodsSkuIds = updateStockList.stream().map(GoodsSkuStockDTO::getSkuId)
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
