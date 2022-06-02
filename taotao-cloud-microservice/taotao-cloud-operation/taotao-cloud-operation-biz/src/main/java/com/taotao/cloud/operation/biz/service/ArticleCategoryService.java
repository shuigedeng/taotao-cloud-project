package com.taotao.cloud.operation.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.operation.api.vo.ArticleCategoryVO;
import com.taotao.cloud.operation.biz.entity.ArticleCategory;

import java.util.List;


/**
 * 文章分类业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-02 15:05:41
 */
public interface ArticleCategoryService extends IService<ArticleCategory> {

	/**
	 * 添加文章分类
	 *
	 * @param articleCategory 文章分类
	 * @return {@link ArticleCategory }
	 * @since 2022-06-02 15:05:41
	 */
	ArticleCategory saveArticleCategory(ArticleCategory articleCategory);

	/**
	 * 修改文章分类
	 *
	 * @param articleCategory 文章分类
	 * @return {@link ArticleCategory }
	 * @since 2022-06-02 15:05:41
	 */
	ArticleCategory updateArticleCategory(ArticleCategory articleCategory);

	/**
	 * 查询所有的分类，父子关系
	 *
	 * @return {@link List }<{@link ArticleCategoryVO }>
	 * @since 2022-06-02 15:05:41
	 */
	List<ArticleCategoryVO> allChildren();

	/**
	 * 删除文章分类
	 *
	 * @param id 文章分类id
	 * @return boolean
	 * @since 2022-06-02 15:05:41
	 */
	boolean deleteById(String id);


}
