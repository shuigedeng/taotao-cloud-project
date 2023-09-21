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

package com.taotao.cloud.wechat.biz.wechat.param.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信消息模板
 *
 * @author xxm
 * @since 2022-08-03
 */
@Data
@Schema(title = "微信消息模板")
@Accessors(chain = true)
public class WeChatTemplateParam {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "模板ID")
    private String templateId;

    @Schema(description = "模板标题")
    private String title;

    @Schema(description = "模板所属行业的一级行业")
    private String primaryIndustry;

    @Schema(description = "模板所属行业的二级行业")
    private String deputyIndustry;

    @Schema(description = "模板内容")
    private String content;

    @Schema(description = "示例")
    private String example;
}
