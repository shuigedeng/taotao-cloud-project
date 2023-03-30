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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.modules.wx.entity.Article;
import com.github.niefy.modules.wx.enums.ArticleTypeEnum;
import java.util.List;
import java.util.Map;

public interface ArticleService extends IService<Article> {
    /**
     * 分页查询用户数据
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询文章详情，每次查询后增加点击次数
     *
     * @param id
     * @return
     */
    Article findById(int id);

    /**
     * 添加或编辑文章,同名文章不可重复添加
     *
     * @param article
     */
    boolean saveArticle(Article article);

    /**
     * 按条件分页查询
     *
     * @param title
     * @param page
     * @return
     */
    IPage<Article> getArticles(String title, int page);

    /**
     * 查看目录，不返回文章详情字段
     *
     * @param articleType
     * @param category
     * @return
     */
    List<Article> selectCategory(ArticleTypeEnum articleType, String category);

    /**
     * 文章查找，不返回文章详情字段
     *
     * @param articleType
     * @param category
     * @param keywords
     * @return
     */
    List<Article> search(ArticleTypeEnum articleType, String category, String keywords);
}
