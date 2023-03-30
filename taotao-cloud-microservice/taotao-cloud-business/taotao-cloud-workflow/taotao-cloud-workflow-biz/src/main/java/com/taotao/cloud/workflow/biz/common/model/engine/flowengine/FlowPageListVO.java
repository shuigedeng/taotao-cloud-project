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

package com.taotao.cloud.workflow.biz.common.model.engine.flowengine;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FlowPageListVO {
    @Schema(description = "编码")
    private String enCode;

    @Schema(description = "名称")
    private String fullName;

    @Schema(description = "主键")
    private String id;

    @Schema(description = "流程分类")
    private String category;

    @Schema(description = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;

    @Schema(description = "表单类型 1-系统表单、2-动态表单")
    private Integer type;

    @Schema(description = "可见类型 0-全部可见、1-部分可见")
    private Integer visibleType;

    @Schema(description = "排序码")
    private Long sortCode;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "图标背景色")
    private String iconBackground;

    @Schema(description = "创建人")
    private String creatorUser;

    @Schema(description = "创建时间")
    private Long creatorTime;

    @Schema(description = "有效标志")
    private Integer enabledMark;
}
