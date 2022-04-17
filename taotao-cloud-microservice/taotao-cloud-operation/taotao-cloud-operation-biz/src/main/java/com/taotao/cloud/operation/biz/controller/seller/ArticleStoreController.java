package com.taotao.cloud.operation.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.operation.api.dto.ArticleSearchParams;
import com.taotao.cloud.operation.api.vo.ArticleVO;
import com.taotao.cloud.operation.biz.entity.Article;
import com.taotao.cloud.operation.biz.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,文章接口
 */
@RestController
@Tag(name = "店铺端,文章接口")
@RequestMapping("/store/other/article")
public class ArticleStoreController {

	/**
	 * 文章
	 */
	@Autowired
	private ArticleService articleService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "分页获取")
	@GetMapping(value = "/getByPage")
	public Result<IPage<ArticleVO>> getByPage(ArticleSearchParams articleSearchParams) {
		return Result.success(articleService.articlePage(articleSearchParams));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "查看文章")
	@GetMapping(value = "/{id}")
	public Result<Article> get(
		@Parameter(description = "文章ID", required = true) @PathVariable String id) {
		return Result.success(articleService.getById(id));
	}
}
