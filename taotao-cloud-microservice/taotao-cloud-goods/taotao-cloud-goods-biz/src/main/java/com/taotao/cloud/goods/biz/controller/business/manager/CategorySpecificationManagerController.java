package com.taotao.cloud.goods.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.biz.model.entity.CategorySpecification;
import com.taotao.cloud.goods.biz.model.entity.Specification;
import com.taotao.cloud.goods.biz.service.business.ICategorySpecificationService;
import com.taotao.cloud.goods.biz.service.business.ISpecificationService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理端,商品分类规格接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-商品分类规格管理API", description = "平台管理端-商品分类规格管理API")
@RequestMapping("/goods/manager/category/spec")
public class CategorySpecificationManagerController {

	/**
	 * 分类规格服务
	 */
	private final ICategorySpecificationService categorySpecificationService;
	/**
	 * 规格服务
	 */
	private final ISpecificationService specificationService;

	@Operation(summary = "查询某分类下绑定的规格信息", description = "查询某分类下绑定的规格信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}")
	public Result<List<Specification>> getCategorySpec(@PathVariable Long categoryId) {
		return Result.success(categorySpecificationService.getCategorySpecList(categoryId));
	}

	@Operation(summary = "查询某分类下绑定的规格信息,商品操作使用", description = "查询某分类下绑定的规格信息,商品操作使用")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/goods/{categoryId}")
	public Result<List<Specification>> getSpec(@PathVariable Long categoryId) {
		return Result.success(specificationService.list());
	}

	@Operation(summary = "保存某分类下绑定的规格信息", description = "保存某分类下绑定的规格信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{categoryId}")
	public Result<Boolean> saveCategoryBrand(@PathVariable Long categoryId,
											 @RequestParam String[] categorySpecs) {
		//删除分类规格绑定信息
		this.categorySpecificationService.remove(
			new QueryWrapper<CategorySpecification>().eq("category_id", categoryId));
		//绑定规格信息
		if (categorySpecs != null && categorySpecs.length > 0) {
			List<CategorySpecification> categorySpecifications = new ArrayList<>();
			for (String categorySpec : categorySpecs) {
				//categorySpecifications.add(new CategorySpecification(categoryId, categorySpec));
			}
			categorySpecificationService.saveBatch(categorySpecifications);
		}
		return Result.success(true);
	}
}
