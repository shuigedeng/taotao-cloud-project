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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.cache.redis.repository.RedisRepository;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.operation.api.enums.ArticleCategoryEnum;
import com.taotao.cloud.operation.api.enums.ArticleEnum;
import com.taotao.cloud.operation.api.model.vo.ArticleCategoryVO;
import com.taotao.cloud.operation.biz.mapper.ArticleCategoryMapper;
import com.taotao.cloud.operation.biz.model.convert.ArticleCategoryConvert;
import com.taotao.cloud.operation.biz.model.entity.Article;
import com.taotao.cloud.operation.biz.model.entity.ArticleCategory;
import com.taotao.cloud.operation.biz.service.business.ArticleCategoryService;
import com.taotao.cloud.operation.biz.service.business.ArticleService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章分类业务层实现
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-02 15:06:23
 */
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory>
        implements ArticleCategoryService {

    /** 缓存 */
    @Autowired
    private RedisRepository redisRepository;
    /** 文章 */
    @Autowired
    private ArticleService articleService;
    /** 顶级父分类ID */
    private static final String parentId = "0";
    /** 最大分类等级 */
    private static final int maxLevel = 2;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleCategory saveArticleCategory(ArticleCategory articleCategory) {
        // 非顶级分类
        if (articleCategory.getParentId() != null && !parentId.equals(articleCategory.getParentId())) {
            ArticleCategory parent = this.getById(articleCategory.getParentId());
            if (parent == null) {
                throw new BusinessException(ResultEnum.ARTICLE_CATEGORY_PARENT_NOT_EXIST);
            }
            if (articleCategory.getLevel() >= maxLevel) {
                throw new BusinessException(ResultEnum.ARTICLE_CATEGORY_BEYOND_TWO);
            }
        }
        articleCategory.setType(ArticleCategoryEnum.OTHER.name());
        this.save(articleCategory);
        // 清除文章分类缓存
        this.clearCache();
        return articleCategory;
    }

    @Override
    public ArticleCategory updateArticleCategory(ArticleCategory articleCategory) {
        // 非顶级分类校验是否存在
        if (!parentId.equals(articleCategory.getParentId())) {
            ArticleCategory parent = this.getById(articleCategory.getParentId());
            if (parent == null) {
                throw new BusinessException(ResultEnum.ARTICLE_CATEGORY_PARENT_NOT_EXIST);
            }
            // 替换catPath 根据path规则来匹配级别
            if (articleCategory.getLevel() >= maxLevel) {
                throw new BusinessException(ResultEnum.ARTICLE_CATEGORY_BEYOND_TWO);
            }
        }
        // 验证分类名称是否重复
        ArticleCategory category = this.getOne(new LambdaQueryWrapper<ArticleCategory>()
                .eq(ArticleCategory::getArticleCategoryName, articleCategory.getArticleCategoryName()));
        if (category != null && !category.getId().equals(articleCategory.getId())) {
            throw new BusinessException(ResultEnum.ARTICLE_CATEGORY_NAME_EXIST);
        }
        if (this.updateById(articleCategory)) {
            // 清除文章分类
            this.clearCache();
            return category;
        }
        return null;
    }

    @Override
    public boolean deleteById(String id) {

        LambdaQueryWrapper<ArticleCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ArticleCategory::getParentId, id);

        // 查看文章分类下是否有分类
        if (this.count(lambdaQueryWrapper) > 0) {
            throw new BusinessException(ResultEnum.ARTICLE_CATEGORY_DELETE_ERROR);
        }

        // 查看文章分类下是否有文章
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getCategoryId, id);
        if (articleService.count(articleLambdaQueryWrapper) > 0) {
            throw new BusinessException(ResultEnum.ARTICLE_CATEGORY_HAS_ARTICLE);
        }
        // 判断是否为默认的分类
        if (!this.getById(id).getType().equals(ArticleEnum.OTHER.name())) {
            throw new BusinessException(ResultEnum.ARTICLE_CATEGORY_NO_DELETION);
        }

        // 清除文章分类缓存
        this.clearCache();
        // 删除文章分类
        return this.removeById(id);
    }

    @Override
    public List<ArticleCategoryVO> allChildren() {
        // 从缓存取所有的分类
        Object all = redisRepository.get(CachePrefix.ARTICLE_CATEGORY.getPrefix());
        List<ArticleCategoryVO> articleCategories;
        if (all == null) {
            // 调用初始化分类缓存方法
            articleCategories = initCategory();
        } else {
            articleCategories = (List<ArticleCategoryVO>) all;
        }
        return articleCategories;
    }

    /**
     * 初始化所有文章分类
     *
     * @return 文章分类集合
     */
    private List<ArticleCategoryVO> initCategory() {
        List<ArticleCategory> articleCategories = this.list();
        List<ArticleCategoryVO> tree = new ArrayList<>();
        articleCategories.forEach(item -> {
            if (item.getLevel() == 0) {
                ArticleCategoryVO articleCategoryVO =
                        ArticleCategoryConvert.INSTANCE.articleCategoryToArticleCategoryVO(item);
                initChild(articleCategoryVO, articleCategories);
                tree.add(articleCategoryVO);
            }
        });

        // 对一级菜单排序
        tree.sort(new Comparator<ArticleCategoryVO>() {
            @Override
            public int compare(ArticleCategoryVO o1, ArticleCategoryVO o2) {
                return o1.getSortNum().compareTo(o2.getSortNum());
            }
        });
        redisRepository.set(CachePrefix.ARTICLE_CATEGORY.getPrefix(), tree);

        return tree;
    }

    /**
     * 递归初始化子树
     *
     * @param tree 树结构
     * @param articleCategories 数据库对象集合
     */
    private void initChild(ArticleCategoryVO tree, List<ArticleCategory> articleCategories) {
        if (articleCategories == null) {
            return;
        }
        articleCategories.stream()
                .filter(item -> (item.getParentId().equals(tree.getId())))
                .forEach(child -> {
                    ArticleCategoryVO childTree =
                            ArticleCategoryConvert.INSTANCE.articleCategoryToArticleCategoryVO(child);
                    initChild(childTree, articleCategories);
                    tree.getChildren().add(childTree);
                });
    }

    /** 清除缓存中的文章分类 */
    private void clearCache() {
        redisRepository.del(CachePrefix.ARTICLE_CATEGORY.getPrefix());
    }

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }
}
