package com.taotao.cloud.goods.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.goods.api.dto.EsGoodsSearchDTO;
import com.taotao.cloud.goods.api.dto.GoodsSearchParams;
import com.taotao.cloud.goods.api.vo.GoodsBaseVO;
import com.taotao.cloud.goods.api.vo.GoodsVO;
import com.taotao.cloud.goods.biz.entity.EsGoodsIndex;
import com.taotao.cloud.goods.biz.entity.EsGoodsRelatedInfo;
import com.taotao.cloud.goods.biz.entity.Goods;
import com.taotao.cloud.goods.biz.service.EsGoodsSearchService;
import com.taotao.cloud.goods.biz.service.GoodsService;
import com.taotao.cloud.goods.biz.service.GoodsSkuService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.netty.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,商品接口
 */
@Validated
@RestController
@Tag(name = "买家端-商品API", description = "买家端-商品API")
@RequestMapping("/buyer/goods/goods")
public class GoodsBuyerController {

	/**
	 * 商品
	 */
	@Autowired
	private GoodsService goodsService;
	/**
	 * 商品SKU
	 */
	@Autowired
	private GoodsSkuService goodsSkuService;
	/**
	 * ES商品搜索
	 */
	@Autowired
	private EsGoodsSearchService goodsSearchService;

	@Operation(summary = "通过id获取商品信息", description = "通过id获取商品信息", method = CommonConstant.GET)
	@RequestLogger(description = "通过id获取商品信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{goodsId}")
	public Result<GoodsVO> get(
		@Parameter(description = "商品ID") @NotBlank(message = "商品ID不能为空") @PathVariable("goodsId") String goodsId) {
		return Result.success(goodsService.getGoodsVO(goodsId));
	}

	@Operation(summary = "通过sku_id获取商品信息", description = "通过sku_id获取商品信息", method = CommonConstant.GET)
	@RequestLogger(description = "通过sku_id获取商品信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/sku/{goodsId}/{skuId}")
	//@PageViewPoint(type = PageViewEnum.SKU, id = "#id")
	public Result<Map<String, Object>> getSku(
		@Parameter(description = "商品ID")@NotNull(message = "商品ID不能为空") @PathVariable("goodsId") String goodsId,
		@Parameter(description = "SKU_ID")@NotNull(message = "SKU ID不能为空") @PathVariable("skuId") String skuId) {
		try {
			// 读取选中的列表
			Map<String, Object> map = goodsSkuService.getGoodsSkuDetail(goodsId, skuId);
			return Result.success(map);
		} catch (ServiceException se) {
			LogUtil.info(se.getMsg(), se);
			throw se;
		} catch (Exception e) {
			LogUtil.error(ResultCode.GOODS_ERROR.message(), e);
			return ResultUtil.error(ResultCode.GOODS_ERROR);
		}

	}

	@Operation(summary = "获取商品分页列表", description = "获取商品分页列表", method = CommonConstant.GET)
	@RequestLogger(description = "获取商品分页列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/page")
	public Result<PageModel<GoodsBaseVO>> getByPage(@Validated GoodsSearchParams goodsSearchParams) {
		return Result.success(goodsService.queryByParams(goodsSearchParams));
	}

	@Operation(summary = "从ES中获取商品信息", description = "从ES中获取商品信息", method = CommonConstant.GET)
	@RequestLogger(description = "从ES中获取商品信息")
	@GetMapping("/es")
	public Result<SearchPage<EsGoodsIndex>> getGoodsByPageFromEs(
		EsGoodsSearchDTO goodsSearchParams, PageParam pageParam) {
		SearchPage<EsGoodsIndex> esGoodsIndices = goodsSearchService.searchGoods(goodsSearchParams,
			pageParam);
		return Result.success(esGoodsIndices);
	}

	@Operation(summary = "从ES中获取相关商品品牌名称，分类名称及属性", description = "从ES中获取相关商品品牌名称，分类名称及属性", method = CommonConstant.GET)
	@RequestLogger(description = "从ES中获取相关商品品牌名称，分类名称及属性")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/es/related")
	public Result<EsGoodsRelatedInfo> getGoodsRelatedByPageFromEs(
		EsGoodsSearchDTO goodsSearchParams, PageParam pageParam) {
		pageVO.setNotConvert(true);
		EsGoodsRelatedInfo selector = goodsSearchService.getSelector(goodsSearchParams, pageParam);
		return Result.success(selector);
	}

	@Operation(summary = "获取热门关键词", description = "获取热门关键词", method = CommonConstant.GET)
	@RequestLogger(description = "获取热门关键词")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/hot-words")
	public Result<List<String>> getGoodsHotWords(@RequestParam Integer count) {
		List<String> hotWords = goodsSearchService.getHotWords(count);
		return Result.success(hotWords);
	}

}
