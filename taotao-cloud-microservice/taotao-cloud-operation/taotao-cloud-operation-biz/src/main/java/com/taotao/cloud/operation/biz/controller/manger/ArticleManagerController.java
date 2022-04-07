package com.taotao.cloud.operation.biz.controller.manger;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 管理端,文章接口
 */
@RestController
@Api(tags = "管理端,文章接口")
@RequestMapping("/manager/other/article")
public class ArticleManagerController {

    /**
     * 文章
     */
    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "查看文章")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/{id}")
    public Result<Article> get(@PathVariable String id) {

        return Result.success(articleService.getById(id));
    }

    @ApiOperation(value = "分页获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "文章分类ID", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "标题", paramType = "query")
    })
    @GetMapping(value = "/getByPage")
    public Result<IPage<ArticleVO>> getByPage(ArticleSearchParams articleSearchParams) {
        return Result.success(articleService.managerArticlePage(articleSearchParams));
    }

    @ApiOperation(value = "添加文章")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public Result<Article> save(@RequestBody Article article) {
        article.setType(ArticleEnum.OTHER.name());
        articleService.save(article);
        return Result.success(article);
    }

    @ApiOperation(value = "修改文章")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, paramType = "path")
    @PutMapping(value = "update/{id}", consumes = "application/json", produces = "application/json")
    public Result<Article> update(@RequestBody Article article, @PathVariable("id") String id) {
        article.setId(id);
        return Result.success(articleService.updateArticle(article));
    }

    @ApiOperation(value = "修改文章状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "status", value = "操作状态", required = true, paramType = "query")
    })
    @PutMapping("update/status/{id}")
    public Result<Article> updateStatus(@PathVariable("id") String id, boolean status) {
        articleService.updateArticleStatus(id, status);
        return Result.success();
    }


    @ApiOperation(value = "批量删除")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/delByIds/{id}")
    public Result<Object> delAllByIds(@PathVariable String id) {
        articleService.customRemove(id);
        return Result.success();
    }


}
