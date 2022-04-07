package com.taotao.cloud.goods.biz.controller.seller;


import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.biz.entity.GoodsUnit;
import com.taotao.cloud.goods.biz.service.GoodsUnitService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,商品计量单位接口
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "商户管理端-商品计量单位API", description = "商户管理端-商品计量单位API")
@RequestMapping("/goods/seller/store/goods/goodsUnit")
public class GoodsUnitStoreController {

	/**
	 * 商品计量单位服务
	 */
	private final GoodsUnitService goodsUnitService;

	@Operation(summary = "分页获取商品计量单位", description = "分页获取商品计量单位", method = CommonConstant.GET)
	@RequestLogger("分页获取商品计量单位")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<PageModel<GoodsUnit>> getByPage(PageParam pageParam) {
		IPage<GoodsUnit> page = goodsUnitService.page(pageParam.buildMpPage());
		return Result.success(PageModel.convertMybatisPage(page,GoodsUnit.class));
	}

}
