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

package com.taotao.cloud.workflow.biz.flowable.bpm.controller.admin.task.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("管理后台 - 流程任务的 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskRespVO extends BpmTaskDonePageItemRespVO {

    @ApiModelProperty(value = "任务定义的标识", required = true, example = "user-001")
    private String definitionKey;

    /** 审核的用户信息 */
    private User assigneeUser;

    @ApiModel("用户信息")
    @Data
    public static class User {

        @ApiModelProperty(value = "用户编号", required = true, example = "1")
        private Long id;

        @ApiModelProperty(value = "用户昵称", required = true, example = "芋艿")
        private String nickname;

        @ApiModelProperty(value = "部门编号", required = true, example = "1")
        private Long deptId;

        @ApiModelProperty(value = "部门名称", required = true, example = "研发部")
        private String deptName;
    }
}
