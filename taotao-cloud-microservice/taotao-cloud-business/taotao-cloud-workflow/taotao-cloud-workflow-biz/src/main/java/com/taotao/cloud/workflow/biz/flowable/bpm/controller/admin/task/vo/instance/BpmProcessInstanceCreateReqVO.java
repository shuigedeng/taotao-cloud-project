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

package com.taotao.cloud.workflow.biz.flowable.bpm.controller.admin.task.vo.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@ApiModel("管理后台 - 流程实例的创建 Request VO")
@Data
public class BpmProcessInstanceCreateReqVO {

    @ApiModelProperty(value = "流程定义的编号", required = true, example = "1024")
    @NotEmpty(message = "流程定义编号不能为空")
    private String processDefinitionId;

    @ApiModelProperty(value = "变量实例")
    private Map<String, Object> variables;
}
