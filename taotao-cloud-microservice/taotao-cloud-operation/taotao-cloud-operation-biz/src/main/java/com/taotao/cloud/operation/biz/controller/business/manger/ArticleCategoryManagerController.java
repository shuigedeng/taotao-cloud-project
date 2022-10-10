package com.taotao.cloud.operation.biz.controller.business.manger;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.operation.api.model.vo.ArticleCategoryVO;
import com.taotao.cloud.operation.biz.model.entity.ArticleCategory;
import com.taotao.cloud.operation.biz.service.business.ArticleCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,文章分类管理接口
 */

@RestController
@Tag(name = "管理端,文章分类管理接口")
@RequestMapping("/manager/other/articleCategory")
public class ArticleCategoryManagerController {

	/**
	 * 文章分类
	 */
	@Autowired
	private ArticleCategoryService articleCategoryService;
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "查询分类列表")
	@GetMapping(value = "/all-children")
	public Result<List<ArticleCategoryVO>> allChildren() {
		try {
			return Result.success(this.articleCategoryService.allChildren());
		} catch (Exception e) {
			log.error("查询分类列表错误", e);
		}
		return null;
	}
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "查看文章分类")
	@GetMapping(value = "/{id}")
	public Result<ArticleCategory> getArticleCategory(
		@Parameter(description = "文章分类ID") @PathVariable String id) {
		return Result.success(this.articleCategoryService.getById(id));
	}
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "保存文章分类")
	@PostMapping
	public Result<ArticleCategory> save(@Valid ArticleCategory articleCategory) {
		if (articleCategory.getLevel() == null) {
			articleCategory.setLevel(0);
		}
		if (articleCategory.getSort() == null) {
			articleCategory.setSort(0);
		}

		return Result.success(articleCategoryService.saveArticleCategory(articleCategory));
	}
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "修改文章分类")
	@PutMapping("/update/{id}")
	public Result<ArticleCategory> update(@Valid ArticleCategory articleCategory,
		@Parameter(description = "文章分类ID") @PathVariable("id") String id) {

		if (articleCategory.getLevel() == null) {
			articleCategory.setLevel(0);
		}
		if (articleCategory.getSort() == null) {
			articleCategory.setSort(0);
		}

		articleCategory.setId(id);
		return Result.success(articleCategoryService.updateArticleCategory(articleCategory));
	}
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "删除文章分类")
	@DeleteMapping("/{id}")
	public Result<ArticleCategory> deleteById(
		@Parameter(description = "文章分类ID") @PathVariable String id) {
		articleCategoryService.deleteById(id);
		return Result.success();
	}
}
