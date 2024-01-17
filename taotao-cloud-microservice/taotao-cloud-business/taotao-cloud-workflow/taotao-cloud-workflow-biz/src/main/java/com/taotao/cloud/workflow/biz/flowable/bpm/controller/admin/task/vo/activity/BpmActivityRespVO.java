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

package com.taotao.cloud.workflow.biz.flowable.bpm.controller.admin.task.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Data;

@ApiModel("管理后台 - 流程活动的 Response VO")
@Data
public class BpmActivityRespVO {

    @ApiModelProperty(value = "流程活动的标识", required = true, example = "1024")
    private String key;

    @ApiModelProperty(value = "流程活动的类型", required = true, example = "StartEvent")
    private String type;

    @ApiModelProperty(value = "流程活动的开始时间", required = true)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "流程活动的结束时间", required = true)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "关联的流程任务的编号", example = "2048", notes = "关联的流程任务，只有 UserTask 等类型才有")
    private String taskId;
}
