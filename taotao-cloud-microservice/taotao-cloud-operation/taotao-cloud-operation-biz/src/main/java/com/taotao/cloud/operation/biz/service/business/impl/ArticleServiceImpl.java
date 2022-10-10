package com.taotao.cloud.operation.biz.service.business.impl;


import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.operation.api.enums.ArticleEnum;
import com.taotao.cloud.operation.api.model.query.ArticlePageQuery;
import com.taotao.cloud.operation.api.model.vo.ArticleVO;
import com.taotao.cloud.operation.biz.model.entity.Article;
import com.taotao.cloud.operation.biz.mapper.ArticleMapper;
import com.taotao.cloud.operation.biz.service.business.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章业务层实现
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements
	ArticleService {

	@Override
	public IPage<ArticleVO> managerArticlePage(ArticlePageQuery articlePageQuery) {
		articlePageQuery.setSort("a.sort");

		QueryWrapper<ArticleVO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(StringUtils.isNotBlank(articlePageQuery.getCategoryId()), "category_id", articlePageQuery.getCategoryId());
		queryWrapper.like(StringUtils.isNotBlank(articlePageQuery.getTitle()), "title", articlePageQuery.getTitle());

		return this.baseMapper.getArticleList(articlePageQuery.buildMpPage(), queryWrapper);
	}

	@Override
	public IPage<ArticleVO> articlePage(ArticlePageQuery articlePageQuery) {
		articlePageQuery.setSort("a.sort");
		QueryWrapper<ArticleVO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(StringUtils.isNotBlank(articlePageQuery.getCategoryId()), "category_id", articlePageQuery.getCategoryId());
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
		//判断是否为默认文章
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
