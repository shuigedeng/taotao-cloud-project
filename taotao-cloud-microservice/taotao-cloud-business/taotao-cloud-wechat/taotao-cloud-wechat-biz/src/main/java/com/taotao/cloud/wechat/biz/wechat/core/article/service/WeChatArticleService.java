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

package com.taotao.cloud.wechat.biz.wechat.core.article.service;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.starter.wechat.dto.article.WeChatArticleDto;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;

/**
 * @author xxm
 * @since 2022/8/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatArticleService {
    private final WxMpService wxMpService;

    /**
     * 查询图文
     *
     * @return
     */
    @SneakyThrows
    public PageResult<WeChatArticleDto> page(PageQuery PageQuery) {
        val freePublishService = wxMpService.getFreePublishService();
        val result = freePublishService.getPublicationRecords(PageQuery.start(), PageQuery.getSize());
        val items = result.getItems().stream().map(WeChatArticleDto::init).toList();
        PageResult<WeChatArticleDto> pageResult = new PageResult<>();
        pageResult
                .setCurrent(PageQuery.getCurrent())
                .setRecords(items)
                .setSize(PageQuery.getSize())
                .setTotal(result.getTotalCount());
        return pageResult;
    }
}
