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
import java.time.LocalDateTime;
import lombok.Data;

@ApiModel("管理后台 - 流程任务的 Running 进行中的分页项 Response VO")
@Data
public class BpmTaskTodoPageItemRespVO {

    @ApiModelProperty(value = "任务编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "任务名字", required = true, example = "芋道")
    private String name;

    @ApiModelProperty(value = "接收时间", required = true)
    private LocalDateTime claimTime;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "激活状态", required = true, example = "1", notes = "参见 SuspensionState 枚举")
    private Integer suspensionState;

    /** 所属流程实例 */
    private ProcessInstance processInstance;

    @Data
    @ApiModel("流程实例")
    public static class ProcessInstance {

        @ApiModelProperty(value = "流程实例编号", required = true, example = "1024")
        private String id;

        @ApiModelProperty(value = "流程实例名称", required = true, example = "芋道")
        private String name;

        @ApiModelProperty(value = "发起人的用户编号", required = true, example = "1024")
        private Long startUserId;

        @ApiModelProperty(value = "发起人的用户昵称", required = true, example = "芋艿")
        private String startUserNickname;

        @ApiModelProperty(value = "流程定义的编号", required = true, example = "2048")
        private String processDefinitionId;
    }
}
