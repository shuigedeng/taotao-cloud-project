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

package com.taotao.cloud.workflow.biz.flowable.bpm.controller.admin.definition.vo.group;

import java.util.*;
import jakarta.validation.constraints.*;
import lombok.*;

/** 用户组 Base VO，提供给添加、修改、详细的子 VO 使用 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成 */
@Data
public class BpmUserGroupBaseVO {

    @ApiModelProperty(value = "组名", required = true, example = "芋道")
    @NotNull(message = "组名不能为空")
    private String name;

    @ApiModelProperty(value = "描述", required = true, example = "芋道源码")
    @NotNull(message = "描述不能为空")
    private String description;

    @ApiModelProperty(value = "成员编号数组", required = true, example = "1,2,3")
    @NotNull(message = "成员编号数组不能为空")
    private Set<Long> memberUserIds;

    @ApiModelProperty(value = "状态", required = true, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;
}
