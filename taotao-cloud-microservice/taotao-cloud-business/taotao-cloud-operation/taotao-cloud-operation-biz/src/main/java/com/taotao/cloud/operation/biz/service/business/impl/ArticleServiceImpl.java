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

package com.taotao.cloud.operation.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.cloud.operation.api.enums.ArticleEnum;
import com.taotao.cloud.operation.api.model.query.ArticlePageQuery;
import com.taotao.cloud.operation.api.model.vo.ArticleVO;
import com.taotao.cloud.operation.biz.mapper.ArticleMapper;
import com.taotao.cloud.operation.biz.model.entity.Article;
import com.taotao.cloud.operation.biz.service.business.ArticleService;
import java.util.List;
import org.springframework.stereotype.Service;

/** 文章业务层实现 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public IPage<ArticleVO> managerArticlePage(ArticlePageQuery articlePageQuery) {
        articlePageQuery.setSort("a.sort");

        QueryWrapper<ArticleVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(
                StringUtils.isNotBlank(articlePageQuery.getCategoryId()),
                "category_id",
                articlePageQuery.getCategoryId());
        queryWrapper.like(StringUtils.isNotBlank(articlePageQuery.getTitle()), "title", articlePageQuery.getTitle());

        return this.baseMapper.getArticleList(articlePageQuery.buildMpPage(), queryWrapper);
    }

    @Override
    public IPage<ArticleVO> articlePage(ArticlePageQuery articlePageQuery) {
        articlePageQuery.setSort("a.sort");
        QueryWrapper<ArticleVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(
                StringUtils.isNotBlank(articlePageQuery.getCategoryId()),
                "category_id",
                articlePageQuery.getCategoryId());
        queryWrapper.like(StringUtils.isNotBlank(articlePageQuery.getTitle()), "title", articlePageQuery.getTitle());
        queryWrapper.eq("open_status", true);

        return this.baseMapper.getArticleList(articlePageQuery.buildMpPage(), queryWrapper);
    }

    @Override
    public List<Article> list(String categoryId) {

        QueryWrapper<Article> queryWrapper = Wrappers.query();
        queryWrapper.eq(StringUtils.isNotBlank(categoryId), "category_id", categoryId);
        return this.list(queryWrapper);
    }

    @Override
    public Article updateArticle(Article article) {
        Article oldArticle = this.getById(article.getId());
        BeanUtils.copyProperties(article, oldArticle);
        this.updateById(oldArticle);
        return oldArticle;
    }

    @Override
    public void customRemove(String id) {
        // 判断是否为默认文章
        if (this.getById(id).getType().equals(ArticleEnum.OTHER.name())) {
            this.removeById(id);
        } else {
            throw new BusinessException(ResultEnum.ARTICLE_NO_DELETION);
        }
    }

    @Override
    public Article customGet(String id) {
        return this.getById(id);
    }

    @Override
    public Article customGetByType(String type) {
        if (!CharSequenceUtil.equals(type, ArticleEnum.OTHER.name())) {
            return this.getOne(new LambdaUpdateWrapper<Article>().eq(Article::getType, type));
        }
        return null;
    }

    @Override
    public Boolean updateArticleStatus(String id, boolean status) {
        Article article = this.getById(id);
        article.setOpenStatus(status);
        return this.updateById(article);
    }
}
