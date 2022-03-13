package com.taotao.cloud.operation.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.operation.api.dto.ArticleSearchParams;
import com.taotao.cloud.operation.api.vo.ArticleVO;
import com.taotao.cloud.operation.biz.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,文章接口
 */
@RestController
@Api(tags = "店铺端,文章接口")
@RequestMapping("/store/other/article")
public class ArticleStoreController {

    /**
     * 文章
     */
    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "分页获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "文章分类ID", paramType = "query")
    })
    @GetMapping(value = "/getByPage")
    public Result<IPage<ArticleVO>> getByPage(ArticleSearchParams articleSearchParams) {
        return Result.success(articleService.articlePage(articleSearchParams));
    }

    @ApiOperation(value = "查看文章")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, paramType = "path")
    @GetMapping(value = "/{id}")
    public Result<Article> get(@PathVariable String id) {

        return Result.success(articleService.getById(id));
    }
}
