package com.taotao.cloud.goods.biz.controller.seller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.vo.CategoryBrandVO;
import com.taotao.cloud.goods.api.vo.CategoryVO;
import com.taotao.cloud.goods.biz.service.CategoryBrandService;
import com.taotao.cloud.goods.biz.service.CategoryService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,商品分类接口
 */
@Validated
@RestController
@Tag(name = "商户管理端-商品分类API", description = "商户管理端-商品分类API")
@RequestMapping("/goods/seller/store/goods/category")
public class CategoryStoreController {

	/**
	 * 分类
	 */
	@Autowired
	private CategoryService categoryService;
	/**
	 * 分类品牌
	 */
	@Autowired
	private CategoryBrandService categoryBrandService;
	/**
	 * 店铺详情
	 */
	@Autowired
	private StoreDetailService storeDetailService;

	@Operation(summary = "获取店铺经营的分类", description = "获取店铺经营的分类", method = CommonConstant.GET)
	@RequestLogger(description = "获取店铺经营的分类")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/all")
	public Result<List<CategoryVO>> getListAll() {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		//获取店铺经营范围
		String goodsManagementCategory = storeDetailService.getStoreDetail(storeId)
			.getGoodsManagementCategory();
		return Result.success(
			this.categoryService.getStoreCategory(goodsManagementCategory.split(",")));
	}

	@Operation(summary = "获取所选分类关联的品牌信息", description = "获取所选分类关联的品牌信息", method = CommonConstant.GET)
	@RequestLogger(description = "获取所选分类关联的品牌信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}/brands")
	public Result<List<CategoryBrandVO>> queryBrands(@PathVariable String categoryId) {
		return Result.success(this.categoryBrandService.getCategoryBrandList(categoryId));
	}

}
