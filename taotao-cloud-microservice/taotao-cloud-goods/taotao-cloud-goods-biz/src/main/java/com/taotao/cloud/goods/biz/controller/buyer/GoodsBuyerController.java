package com.taotao.cloud.goods.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.query.EsGoodsSearchQuery;
import com.taotao.cloud.goods.api.query.GoodsPageQuery;
import com.taotao.cloud.goods.api.vo.GoodsVO;
import com.taotao.cloud.goods.api.vo.GoodsSkuParamsVO;
import com.taotao.cloud.goods.biz.elasticsearch.EsGoodsIndex;
import com.taotao.cloud.goods.biz.elasticsearch.EsGoodsRelatedInfo;
import com.taotao.cloud.goods.biz.entity.Goods;
import com.taotao.cloud.goods.biz.service.IEsGoodsSearchService;
import com.taotao.cloud.goods.biz.service.IGoodsService;
import com.taotao.cloud.goods.biz.service.IGoodsSkuService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.netty.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 买家端,商品接口
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-商品API", description = "买家端-商品API")
@RequestMapping("/goods/buyer/goods")
public class GoodsBuyerController {

	/**
	 * 商品
	 */
	private final IGoodsService goodsService;
	/**
	 * 商品SKU
	 */
	private final IGoodsSkuService goodsSkuService;
	/**
	 * ES商品搜索
	 */
	private final IEsGoodsSearchService goodsSearchService;

	@RequestLogger
	@Operation(summary = "通过id获取商品信息", description = "通过id获取商品信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{goodsId}")
	public Result<GoodsSkuParamsVO> get(
		@Parameter(description = "商品ID") @NotNull(message = "商品ID不能为空") @PathVariable Long goodsId) {
		return Result.success(goodsService.getGoodsVO(goodsId));
	}

	@RequestLogger
	@Operation(summary = "通过skuId获取商品信息", description = "通过skuId获取商品信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{goodsId}/{skuId}")
	//@PageViewPoint(type = PageViewEnum.SKU, id = "#id")
	public Result<Map<String, Object>> getSku(
		@Parameter(description = "商品ID") @NotNull(message = "商品ID不能为空") @PathVariable Long goodsId,
		@Parameter(description = "skuId") @NotNull(message = "skuId不能为空") @PathVariable Long skuId) {
		Map<String, Object> map = goodsSkuService.getGoodsSkuDetail(goodsId, skuId);
		return Result.success(map);
	}

	@RequestLogger
	@Operation(summary = "获取商品分页列表", description = "获取商品分页列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<PageModel<GoodsVO>> getByPage(@Validated GoodsPageQuery goodsPageQuery) {
		IPage<Goods> goodsPage = goodsService.queryByParams(goodsPageQuery);
		return Result.success(PageModel.convertMybatisPage(goodsPage, GoodsVO.class));
	}

	@Operation(summary = "从ES中获取商品信息", description = "从ES中获取商品信息")
	@RequestLogger
	@GetMapping("/es")
	public Result<SearchPage<EsGoodsIndex>> getGoodsByPageFromEs(@Validated EsGoodsSearchQuery goodsSearchParams) {
		SearchPage<EsGoodsIndex> esGoodsIndices = goodsSearchService.searchGoods(goodsSearchParams);
		return Result.success(esGoodsIndices);
	}

	@Operation(summary = "从ES中获取相关商品品牌名称，分类名称及属性", description = "从ES中获取相关商品品牌名称，分类名称及属性")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/es/related")
	public Result<EsGoodsRelatedInfo> getGoodsRelatedByPageFromEs(@Validated EsGoodsSearchQuery esGoodsSearchQuery) {
		//pageVO.setNotConvert(true);
		EsGoodsRelatedInfo selector = goodsSearchService.getSelector(esGoodsSearchQuery);
		return Result.success(selector);
	}

	@Operation(summary = "获取热门关键词", description = "获取热门关键词")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/hot-words")
	public Result<List<String>> getGoodsHotWords(@RequestParam Integer count) {
		List<String> hotWords = goodsSearchService.getHotWords(count);
		return Result.success(hotWords);
	}

}
