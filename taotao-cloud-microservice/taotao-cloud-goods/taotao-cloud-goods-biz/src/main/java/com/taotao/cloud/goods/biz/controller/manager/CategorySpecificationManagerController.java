package com.taotao.cloud.goods.biz.controller.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.biz.entity.CategorySpecification;
import com.taotao.cloud.goods.biz.service.CategorySpecificationService;
import com.taotao.cloud.goods.biz.service.SpecificationService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,商品分类规格接口
 */
@Validated
@RestController
@Tag(name = "平台管理端-商品分类规格管理API", description = "平台管理端-商品分类规格管理API")
@RequestMapping("/goods/manager/categorySpec")
public class CategorySpecificationManagerController {

	/**
	 * 分类规格
	 */
	@Autowired
	private CategorySpecificationService categorySpecificationService;

	/**
	 * 规格
	 */
	@Autowired
	private SpecificationService specificationService;

	@Operation(summary = "查询某分类下绑定的规格信息", description = "查询某分类下绑定的规格信息", method = CommonConstant.GET)
	@RequestLogger(description = "查询某分类下绑定的规格信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}")
	public Result<List<Specification>> getCategorySpec(@PathVariable String categoryId) {
		return Result.success(categorySpecificationService.getCategorySpecList(categoryId));
	}

	@Operation(summary = "查询某分类下绑定的规格信息,商品操作使用", description = "查询某分类下绑定的规格信息,商品操作使用", method = CommonConstant.GET)
	@RequestLogger(description = "查询某分类下绑定的规格信息,商品操作使用")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/goods/{categoryId}")
	public Result<List<Specification>> getSpec(@PathVariable String categoryId) {
		return Result.success(specificationService.list());
	}

	@Operation(summary = "保存某分类下绑定的规格信息", description = "保存某分类下绑定的规格信息", method = CommonConstant.POST)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{categoryId}")
	public Result<String> saveCategoryBrand(@PathVariable String categoryId,
		@RequestParam String[] categorySpecs) {
		//删除分类规格绑定信息
		this.categorySpecificationService.remove(
			new QueryWrapper<CategorySpecification>().eq("category_id", categoryId));
		//绑定规格信息
		if (categorySpecs != null && categorySpecs.length > 0) {
			List<CategorySpecification> categorySpecifications = new ArrayList<>();
			for (String categorySpec : categorySpecs) {
				categorySpecifications.add(new CategorySpecification(categoryId, categorySpec));
			}
			categorySpecificationService.saveBatch(categorySpecifications);
		}
		return Result.success();
	}

}
