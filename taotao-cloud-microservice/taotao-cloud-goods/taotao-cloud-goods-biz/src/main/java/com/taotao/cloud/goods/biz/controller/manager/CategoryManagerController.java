package com.taotao.cloud.goods.biz.controller.manager;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.vo.CategoryVO;
import com.taotao.cloud.goods.biz.entity.Category;
import com.taotao.cloud.goods.biz.service.CategoryService;
import com.taotao.cloud.goods.biz.service.GoodsService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
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
 * 管理端,商品分类接口
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-商品分类管理API", description = "平台管理端-商品分类管理API")
@RequestMapping("/goods/manager/category")
@CacheConfig(cacheNames = "category")
public class CategoryManagerController {

	/**
	 * 分类服务
	 */
	private final CategoryService categoryService;
	/**
	 * 商品服务
	 */
	private final GoodsService goodsService;

	@Operation(summary = "查询某分类下的全部子分类列表", description = "查询某分类下的全部子分类列表", method = CommonConstant.GET)
	@RequestLogger("查询某分类下的全部子分类列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{parentId}/children/all")
	public Result<List<Category>> list(@PathVariable Long parentId) {
		return Result.success(this.categoryService.dbList(parentId));
	}

	@Operation(summary = "查询全部分类列表", description = "查询全部分类列表", method = CommonConstant.GET)
	@RequestLogger("查询全部分类列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/children/all")
	public Result<List<CategoryVO>> list() {
		return Result.success(this.categoryService.listAllChildren());
	}

	@Operation(summary = "添加商品分类", description = "添加商品分类", method = CommonConstant.POST)
	@RequestLogger("添加商品分类")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> saveCategory(@Validated @RequestBody Category category) {
		//非顶级分类
		if (category.getParentId() != null && !"0".equals(category.getParentId())) {
			Category parent = categoryService.getById(category.getParentId());
			if (parent == null) {
				throw new BusinessException(ResultEnum.CATEGORY_PARENT_NOT_EXIST);
			}
			if (category.getLevel() >= 4) {
				throw new BusinessException(ResultEnum.CATEGORY_BEYOND_THREE);
			}
		}
		return Result.success(categoryService.saveCategory(category));
	}

	@Operation(summary = "修改商品分类", description = "修改商品分类", method = CommonConstant.PUT)
	@RequestLogger("修改商品分类")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping
	public Result<Boolean> updateCategory(@Valid CategoryVO category) {
		Category catTemp = categoryService.getById(category.getId());
		if (catTemp == null) {
			throw new BusinessException(ResultEnum.CATEGORY_NOT_EXIST);
		}
		return Result.success(categoryService.updateCategory(catTemp));
	}

	@Operation(summary = "通过id删除分类", description = "通过id删除分类", method = CommonConstant.DELETE)
	@RequestLogger("通过id删除分类")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delAllByIds(@NotBlank(message = "id不能为空") @PathVariable Long id) {
		Category category = new Category();
		category.setParentId(id);
		List<Category> list = categoryService.findByAllBySortOrder(category);
		if (list != null && !list.isEmpty()) {
			throw new BusinessException(ResultEnum.CATEGORY_HAS_CHILDREN);
		}

		//查询某商品分类的商品数量
		long count = goodsService.getGoodsCountByCategory(id);
		if (count > 0) {
			throw new BusinessException(ResultEnum.CATEGORY_HAS_GOODS);
		}
		return Result.success(categoryService.delete(id));
	}

	@Operation(summary = "后台 禁用/启用 分类", description = "后台 禁用/启用 分类", method = CommonConstant.PUT)
	@RequestLogger("后台 禁用/启用 分类")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping(value = "/disable/{id}")
	public Result<Boolean> disable(@PathVariable Long id,
		@RequestParam Boolean enableOperations) {
		Category category = categoryService.getById(id);
		if (category == null) {
			throw new BusinessException(ResultEnum.CATEGORY_NOT_EXIST);
		}
		return Result.success(categoryService.updateCategoryStatus(id, enableOperations));
	}

}
