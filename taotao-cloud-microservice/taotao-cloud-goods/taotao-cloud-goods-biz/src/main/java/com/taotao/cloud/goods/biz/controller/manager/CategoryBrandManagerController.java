package com.taotao.cloud.goods.biz.controller.manager;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.dto.CategoryBrandDTO;
import com.taotao.cloud.goods.api.vo.CategoryBrandVO;
import com.taotao.cloud.goods.biz.service.ICategoryBrandService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		@NotNull(message = "分类id不能为空") @PathVariable(value = "categoryId") Long categoryId) {
		return Result.success(categoryBrandService.getCategoryBrandList(categoryId));
	}

	@Operation(summary = "保存某分类下绑定的品牌信息", description = "保存某分类下绑定的品牌信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> saveCategoryBrand(
		@Validated @RequestBody CategoryBrandDTO categoryBrandDTO) {
		Boolean result = categoryBrandService.saveCategoryBrandList(categoryBrandDTO.categoryId(),
			categoryBrandDTO.brandIds());
		return Result.success(result);
	}

}
