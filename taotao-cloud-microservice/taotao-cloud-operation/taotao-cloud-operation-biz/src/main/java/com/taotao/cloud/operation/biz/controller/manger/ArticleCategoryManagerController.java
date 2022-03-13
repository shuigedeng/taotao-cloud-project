package com.taotao.cloud.operation.biz.controller.manger;

import com.taotao.cloud.operation.api.vo.ArticleCategoryVO;
import com.taotao.cloud.operation.biz.entity.ArticleCategory;
import com.taotao.cloud.operation.biz.service.ArticleCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理端,文章分类管理接口
 */
@Slf4j
@RestController
@Api(tags = "管理端,文章分类管理接口")
@RequestMapping("/manager/other/articleCategory")
public class ArticleCategoryManagerController {

    /**
     * 文章分类
     */
    @Autowired
    private ArticleCategoryService articleCategoryService;

    @ApiOperation(value = "查询分类列表")
    @GetMapping(value = "/all-children")
    public Result<List<ArticleCategoryVO>> allChildren() {
        try {
            return Result.success(this.articleCategoryService.allChildren());
        } catch (Exception e) {
            log.error("查询分类列表错误", e);
        }
        return null;
    }

    @ApiOperation(value = "查看文章分类")
    @ApiImplicitParam(name = "id", value = "文章分类ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/{id}")
    public Result<ArticleCategory> getArticleCategory(@PathVariable String id) {
        return Result.success(this.articleCategoryService.getById(id));
    }

    @ApiOperation(value = "保存文章分类")
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

    @ApiOperation(value = "修改文章分类")
    @ApiImplicitParam(name = "id", value = "文章分类ID", required = true, dataType = "String", paramType = "path")
    @PutMapping("/update/{id}")
    public Result<ArticleCategory> update(@Valid ArticleCategory articleCategory, @PathVariable("id") String id) {

        if (articleCategory.getLevel() == null) {
            articleCategory.setLevel(0);
        }
        if (articleCategory.getSort() == null) {
            articleCategory.setSort(0);
        }

        articleCategory.setId(id);
        return Result.success(articleCategoryService.updateArticleCategory(articleCategory));
    }

    @ApiOperation(value = "删除文章分类")
    @ApiImplicitParam(name = "id", value = "文章分类ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/{id}")
    public Result<ArticleCategory> deleteById(@PathVariable String id) {
        articleCategoryService.deleteById(id);
        return ResultUtil.success();
    }
}
