package com.taotao.cloud.goods.biz.controller.manager;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.vo.CategoryBrandVO;
import com.taotao.cloud.goods.biz.service.ICategoryBrandService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 管理端,分类品牌接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-分类品牌管理API", description = "平台管理端-分类品牌管理API")
@RequestMapping("/goods/manager/category/brand")
public class CategoryBrandManagerController {

	/**
	 * 规格品牌管理服务
	 */
	private final ICategoryBrandService categoryBrandService;

	@Operation(summary = "查询某分类下绑定的品牌信息", description = "查询某分类下绑定的品牌信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}")
	public Result<List<CategoryBrandVO>> getCategoryBrand(
		@NotBlank(message = "分类id不能为空") @PathVariable(value = "categoryId") Long categoryId) {
		return Result.success(categoryBrandService.getCategoryBrandList(categoryId));
	}

	@Operation(summary = "保存某分类下绑定的品牌信息", description = "保存某分类下绑定的品牌信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{categoryId}/{categoryBrands}")
	public Result<Boolean> saveCategoryBrand(
		@NotBlank(message = "分类id不能为空") @PathVariable(value = "categoryId") Long categoryId,
		@NotBlank(message = "品牌id列表不能为空") @PathVariable(value = "categoryBrands") List<Long> categoryBrands) {
		return Result.success(
			categoryBrandService.saveCategoryBrandList(categoryId, categoryBrands));
	}

}
