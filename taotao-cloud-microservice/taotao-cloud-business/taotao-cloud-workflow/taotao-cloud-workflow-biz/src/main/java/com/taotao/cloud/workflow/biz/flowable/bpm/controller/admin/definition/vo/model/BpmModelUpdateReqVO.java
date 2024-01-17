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

package com.taotao.cloud.workflow.biz.flowable.bpm.controller.admin.definition.vo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@ApiModel("管理后台 - 流程模型的更新 Request VO")
@Data
public class BpmModelUpdateReqVO {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    @NotEmpty(message = "编号不能为空")
    private String id;

    @ApiModelProperty(value = "流程名称", example = "芋道")
    private String name;

    @ApiModelProperty(value = "流程描述", example = "我是描述")
    private String description;

    @ApiModelProperty(value = "流程分类", notes = "参见 bpm_model_category 数据字典", example = "1")
    private String category;

    @ApiModelProperty(value = "BPMN XML", required = true)
    private String bpmnXml;

    @ApiModelProperty(value = "表单类型", notes = "参见 bpm_model_form_type 数据字典", example = "1")
    private Integer formType;

    @ApiModelProperty(value = "表单编号", example = "1024", notes = "在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空")
    private Long formId;

    @ApiModelProperty(
            value = "自定义表单的提交路径，使用 Vue 的路由地址",
            example = "/bpm/oa/leave/create",
            notes = "在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空")
    private String formCustomCreatePath;

    @ApiModelProperty(
            value = "自定义表单的查看路径，使用 Vue 的路由地址",
            example = "/bpm/oa/leave/view",
            notes = "在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空")
    private String formCustomViewPath;
}
