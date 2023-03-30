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

package com.taotao.cloud.workflow.biz.common.model.engine.flowbefore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** */
@Data
public class FlowTaskOperatorModel {
    @Schema(description = "节点经办主键")
    private String id;

    @Schema(description = "经办对象")
    private String handleType;

    @Schema(description = "经办主键")
    private String handleId;

    @Schema(description = "处理状态 0-拒绝、1-同意")
    private Integer handleStatus;

    @Schema(description = "处理时间")
    private Long handleTime;

    @Schema(description = "节点编码")
    private String nodeCode;

    @Schema(description = "节点名称")
    private String nodeName;

    @Schema(description = "是否完成")
    private Integer completion;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "创建时间")
    private Long creatorTime;

    @Schema(description = "节点主键")
    private String taskNodeId;

    @Schema(description = "任务主键")
    private String taskId;
}
