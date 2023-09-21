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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.manage;

import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.entity.Article;
import com.github.niefy.modules.wx.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.Map;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章
 *
 * @author niefy
 * @email niefy@qq.com
 * @since 2019-11-12 18:30:16
 */
@RestController
@RequestMapping("/manage/article")
@Api(tags = {"文章管理-管理后台"})
public class ArticleManageController {
    @Autowired
    private ArticleService articleService;

    /** 列表 */
    @GetMapping("/list")
    @RequiresPermissions("wx:article:list")
    @ApiOperation(value = "列表")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = articleService.queryPage(params);

        return R.ok().put("page", page);
    }

    /** 信息 */
    @GetMapping("/info/{id}")
    @RequiresPermissions("wx:article:info")
    @ApiOperation(value = "详情")
    public R info(@PathVariable("id") Integer id) {
        Article article = articleService.getById(id);

        return R.ok().put("article", article);
    }

    /** 保存 */
    @PostMapping("/save")
    @RequiresPermissions("wx:article:save")
    @ApiOperation(value = "保存")
    public R save(@RequestBody Article article) {
        articleService.saveArticle(article);

        return R.ok();
    }

    /** 删除 */
    @PostMapping("/delete")
    @RequiresPermissions("wx:article:delete")
    @ApiOperation(value = "删除")
    public R delete(@RequestBody Integer[] ids) {
        articleService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
