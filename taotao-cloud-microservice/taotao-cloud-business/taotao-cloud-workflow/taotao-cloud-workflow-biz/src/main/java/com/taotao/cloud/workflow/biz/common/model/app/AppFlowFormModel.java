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

package com.taotao.cloud.workflow.biz.common.model.app;

import lombok.Data;

/** */
@Data
public class AppFlowFormModel {
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "流程名称")
    private String fullName;

    @Schema(description = "流程分类")
    private String category;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "编码")
    private String enCode;

    @Schema(description = "图标背景色")
    private String iconBackground;

    @Schema(description = "表单类型")
    private Integer formType;

    @Schema(description = "是否常用")
    private Boolean isData;
}
