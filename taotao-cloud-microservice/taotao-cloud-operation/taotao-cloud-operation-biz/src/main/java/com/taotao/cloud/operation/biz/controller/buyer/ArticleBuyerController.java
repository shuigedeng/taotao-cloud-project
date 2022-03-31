package com.taotao.cloud.operation.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 买家端,文章接口
 */
@RestController
@Api(tags = "买家端,文章接口")
@RequestMapping("/buyer/other/article")
public class ArticleBuyerController {

    /**
     * 文章
     */
    @Autowired
    private ArticleService articleService;

    /**
     * 文章分类
     */
    @Autowired
    private ArticleCategoryService articleCategoryService;

    @ApiOperation(value = "获取文章分类列表")
    @GetMapping(value = "/articleCategory/list")
    public Result<List<ArticleCategoryVO>> getArticleCategoryList() {
        return Result.success(articleCategoryService.allChildren());
    }

    @ApiOperation(value = "分页获取")
    @GetMapping
    public Result<IPage<ArticleVO>> getByPage(ArticleSearchParams articleSearchParams) {
        return Result.success(articleService.articlePage(articleSearchParams));
    }

    @ApiOperation(value = "通过id获取文章")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, paramType = "path")
    @GetMapping(value = "/get/{id}")
    public Result<Article> get(@NotNull(message = "文章ID") @PathVariable("id") String id) {
        return Result.success(articleService.customGet(id));
    }

    @ApiOperation(value = "通过类型获取文章")
    @ApiImplicitParam(name = "type", value = "文章类型", required = true, paramType = "path")
    @GetMapping(value = "/type/{type}")
    public Result<Article> getByType(@NotNull(message = "文章类型") @PathVariable("type") String type) {
        return Result.success(articleService.customGetByType(type));
    }
}
