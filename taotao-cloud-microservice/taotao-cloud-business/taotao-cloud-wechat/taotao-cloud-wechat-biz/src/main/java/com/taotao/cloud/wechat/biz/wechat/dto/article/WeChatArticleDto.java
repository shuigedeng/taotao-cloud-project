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

package com.taotao.cloud.wechat.biz.wechat.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;
import me.chanjar.weixin.mp.bean.freepublish.WxMpFreePublishArticles;
import me.chanjar.weixin.mp.bean.freepublish.WxMpFreePublishItem;

/**
 * 微信文章
 *
 * @author xxm
 * @since 2022/8/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信文章")
public class WeChatArticleDto {

    @Schema(description = "文章id")
    private String articleId;

    @Schema(description = "标题组")
    private String titles;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /** 构建 */
    public static WeChatArticleDto init(WxMpFreePublishItem item) {
        DateTime date = DateUtil.date(Long.parseLong(item.getUpdateTime()));
        LocalDateTime localDateTime = DateUtil.toLocalDateTime(date);
        String titles = item.getContent().getNewsItem().stream()
                .map(WxMpFreePublishArticles::getTitle)
                .collect(Collectors.joining(","));
        return new WeChatArticleDto()
                .setArticleId(item.getArticleId())
                .setUpdateTime(localDateTime)
                .setTitles(titles);
    }
}
