package com.taotao.cloud.operation.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.operation.api.web.vo.ArticleVO;
import com.taotao.cloud.operation.biz.model.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * 文章数据处理层
 */
public interface ArticleMapper extends BaseSuperMapper<Article> {

	/**
	 * 获取文章VO分页
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 * @return 文章VO分页
	 */
	@Select("""
		select a.id,a.title,a.sort,ac.article_category_name,a.open_status 
		from tt_article as a inner join tt_article_category ac on a.category_id=ac.id ${ew.customSqlSegment}
		""")
	IPage<ArticleVO> getArticleList(IPage<ArticleVO> page, @Param(Constants.WRAPPER) Wrapper<ArticleVO> queryWrapper);
}
