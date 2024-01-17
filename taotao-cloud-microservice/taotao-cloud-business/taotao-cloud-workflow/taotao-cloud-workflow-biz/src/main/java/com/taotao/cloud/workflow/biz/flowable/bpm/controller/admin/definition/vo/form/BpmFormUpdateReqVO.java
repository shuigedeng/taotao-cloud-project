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

package com.taotao.cloud.workflow.biz.flowable.bpm.controller.admin.definition.vo.form;

import java.util.List;
import jakarta.validation.constraints.*;
import lombok.*;

@ApiModel("管理后台 - 动态表单更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmFormUpdateReqVO extends BpmFormBaseVO {

    @ApiModelProperty(value = "表单编号", required = true, example = "1024")
    @NotNull(message = "表单编号不能为空")
    private Long id;

    @ApiModelProperty(value = "表单的配置", required = true, notes = "JSON 字符串")
    @NotNull(message = "表单的配置不能为空")
    private String conf;

    @ApiModelProperty(value = "表单项的数组", required = true, notes = "JSON 字符串的数组")
    @NotNull(message = "表单项的数组不能为空")
    private List<String> fields;
}
