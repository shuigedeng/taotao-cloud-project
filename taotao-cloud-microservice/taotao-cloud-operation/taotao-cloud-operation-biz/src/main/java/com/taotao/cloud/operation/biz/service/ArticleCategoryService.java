package com.taotao.cloud.operation.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.taotao.cloud.operation.api.vo.ArticleCategoryVO;
import com.taotao.cloud.operation.biz.entity.ArticleCategory;
import java.util.List;


/**
 * 文章分类业务层
 */
public interface ArticleCategoryService extends IService<ArticleCategory> {

    /**
     * 添加文章分类
     *
     * @param articleCategory 文章分类
     * @return 文章分类
     */
    ArticleCategory saveArticleCategory(ArticleCategory articleCategory);

    /**
     * 修改文章分类
     *
     * @param articleCategory 文章分类
     * @return 文章分类
     */
    ArticleCategory updateArticleCategory(ArticleCategory articleCategory);

    /**
     * 查询所有的分类，父子关系
     *
     * @return 文章分类
     */
    List<ArticleCategoryVO> allChildren();

    /**
     * 删除文章分类
     *
     * @param id 文章分类id
     * @return 操作状态
     */
    boolean deleteById(String id);


}
