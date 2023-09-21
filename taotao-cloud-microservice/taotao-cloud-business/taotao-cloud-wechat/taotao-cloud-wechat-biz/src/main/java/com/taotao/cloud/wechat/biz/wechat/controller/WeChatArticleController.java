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

package com.taotao.cloud.wechat.biz.wechat.controller;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.Res;
import cn.bootx.common.core.rest.ResResult;
import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.starter.wechat.core.article.service.WeChatArticleService;
import cn.bootx.starter.wechat.dto.article.WeChatArticleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxm
 * @since 2022/8/11
 */
@Tag(name = "微信文章管理")
@RestController
@RequestMapping("/wechat/article")
@RequiredArgsConstructor
public class WeChatArticleController {
    private final WeChatArticleService weChatArticleService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<WeChatArticleDto>> page(PageQuery PageQuery) {
        return Res.ok(weChatArticleService.page(PageQuery));
    }
}
