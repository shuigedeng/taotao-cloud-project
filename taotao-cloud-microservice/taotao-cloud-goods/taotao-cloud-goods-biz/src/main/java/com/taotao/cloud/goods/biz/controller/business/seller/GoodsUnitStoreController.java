package com.taotao.cloud.goods.biz.controller.business.seller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.model.vo.GoodsUnitVO;
import com.taotao.cloud.goods.biz.model.entity.GoodsUnit;
import com.taotao.cloud.goods.biz.service.business.IGoodsUnitService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端-商品计量单位接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:05:11
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-商品计量单位API", description = "店铺端-商品计量单位API")
@RequestMapping("/goods/seller/goods/unit")
public class GoodsUnitStoreController {

	/**
	 * 商品计量单位服务
	 */
	private final IGoodsUnitService goodsUnitService;

	@Operation(summary = "分页获取商品计量单位", description = "分页获取商品计量单位")
	@RequestLogger("分页获取商品计量单位")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<PageResult<GoodsUnitVO>> getByPage(PageParam pageParam) {
		IPage<GoodsUnit> page = goodsUnitService.page(pageParam.buildMpPage());
		return Result.success(PageResult.convertMybatisPage(page, GoodsUnitVO.class));
	}

}
