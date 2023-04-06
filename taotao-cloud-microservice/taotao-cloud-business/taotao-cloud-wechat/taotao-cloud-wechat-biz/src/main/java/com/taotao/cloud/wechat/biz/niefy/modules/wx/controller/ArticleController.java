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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.controller;

import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.entity.Article;
import com.github.niefy.modules.wx.enums.ArticleTypeEnum;
import com.github.niefy.modules.wx.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** cms文章 */
@RestController
@RequestMapping("/article")
@Api(tags = {"CMS文章"})
public class ArticleController {
    @Autowired
    ArticleService articleService;

    /**
     * 查看文章详情
     *
     * @param articleId
     * @return
     */
    @GetMapping("/detail")
    @ApiOperation(value = "文章详情", notes = "")
    public R getArticle(int articleId) {
        Article article = articleService.findById(articleId);
        return R.ok().put(article);
    }

    /**
     * 查看目录
     *
     * @param category
     * @return
     */
    @GetMapping("/category")
    @ApiOperation(value = "目录信息", notes = "")
    public R getQuestions(String type, String category) {
        ArticleTypeEnum articleType = ArticleTypeEnum.of(type);
        if (articleType == null) {
            return R.error("文章类型有误");
        }
        List<Article> articles = articleService.selectCategory(articleType, category);
        return R.ok().put(articles);
    }

    /**
     * 文章搜索
     *
     * @param category
     * @param keywords
     * @return
     */
    @GetMapping("/search")
    @ApiOperation(value = "文章搜索", notes = "")
    public R getQuestions(
            String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keywords) {
        ArticleTypeEnum articleType = ArticleTypeEnum.of(type);
        if (articleType == null) {
            return R.error("文章类型有误");
        }
        if (!StringUtils.hasText(keywords)) {
            return R.error("关键词不得为空");
        }
        List<Article> articles = articleService.search(articleType, category, keywords);
        return R.ok().put(articles);
    }
}
