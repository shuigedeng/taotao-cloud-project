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

/** */
@Data
public class FlowEngineInfoVO {
    @Schema(description = "主键")
    private String id;

    @Schema(description = "排序码")
    private Long sortCode;

    @Schema(description = "流程编码")
    private String enCode;

    @Schema(description = "流程名称")
    private String fullName;

    @Schema(description = "流程类型")
    private Integer type;

    @Schema(description = "流程分类")
    private String category;

    @Schema(description = "可见类型 0-全部可见、1-指定经办")
    private Integer visibleType;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "图标背景色")
    private String iconBackground;

    @Schema(description = "流程版本")
    private String version;

    @Schema(description = "流程模板")
    private String flowTemplateJson;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "有效标志")
    private Integer enabledMark;

    @Schema(description = "表单字段")
    private String formData;

    @Schema(description = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;

    @Schema(description = "关联表")
    private String tables;

    @Schema(description = "数据连接")
    private String dbLinkId;

    @Schema(description = "app表单路径")
    private String appFormUrl;

    @Schema(description = "pc表单路径")
    private String formUrl;
}
