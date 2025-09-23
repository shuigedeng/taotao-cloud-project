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

package com.taotao.cloud.operation.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.operation.api.model.vo.ArticleVO;
import com.taotao.cloud.operation.biz.model.entity.Article;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 文章数据处理层 */
public interface ArticleMapper extends MpSuperMapper<Article, Long> {

    /**
     * 获取文章VO分页
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 文章VO分页
     */
    @Select(
            """
		select a.id,a.title,a.sort,ac.article_category_name,a.open_status
		from tt_article as a inner join tt_article_category ac on a.category_id=ac.id ${ew.customSqlSegment}
		""")
    IPage<ArticleVO> getArticleList(IPage<ArticleVO> page, @Param(Constants.WRAPPER) Wrapper<ArticleVO> queryWrapper);
}
