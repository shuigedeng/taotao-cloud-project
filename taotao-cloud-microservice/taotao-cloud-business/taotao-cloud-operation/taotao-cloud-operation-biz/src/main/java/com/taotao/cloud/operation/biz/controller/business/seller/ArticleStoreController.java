/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.operation.biz.controller.business.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.operation.api.model.query.ArticlePageQuery;
import com.taotao.cloud.operation.api.model.vo.ArticleVO;
import com.taotao.cloud.operation.biz.model.entity.Article;
import com.taotao.cloud.operation.biz.service.business.ArticleService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 店铺端,文章接口 */
@RestController
@Tag(name = "店铺端,文章接口")
@RequestMapping("/store/other/article")
public class ArticleStoreController {

    /** 文章 */
    @Autowired
    private ArticleService articleService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "分页获取")
    @GetMapping(value = "/getByPage")
    public Result<IPage<ArticleVO>> getByPage(ArticlePageQuery articlePageQuery) {
        return Result.success(articleService.articlePage(articlePageQuery));
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "查看文章")
    @GetMapping(value = "/{id}")
    public Result<Article> get(@Parameter(description = "文章ID", required = true) @PathVariable String id) {
        return Result.success(articleService.getById(id));
    }
}
