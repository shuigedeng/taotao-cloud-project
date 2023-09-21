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

package com.taotao.cloud.wechat.biz.wechat.dto.media;

import cn.bootx.common.core.util.LocalDateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem;

/**
 * @author xxm
 * @since 2022/8/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "微信素材")
public class WeChatMediaDto {
    @Schema(description = "媒体id")
    private String mediaId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "链接地址")
    private String url;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    public static WeChatMediaDto init(WxMaterialFileBatchGetNewsItem item) {
        return new WeChatMediaDto()
                .setMediaId(item.getMediaId())
                .setName(item.getName())
                .setUrl(item.getUrl())
                .setUpdateTime(LocalDateTimeUtil.of(item.getUpdateTime()));
    }
}
