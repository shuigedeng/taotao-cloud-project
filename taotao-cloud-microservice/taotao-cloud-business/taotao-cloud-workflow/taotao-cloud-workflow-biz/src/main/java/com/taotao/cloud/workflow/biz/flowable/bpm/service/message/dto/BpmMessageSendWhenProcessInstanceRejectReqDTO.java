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

package com.taotao.cloud.workflow.biz.flowable.bpm.service.message.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** BPM 发送流程实例被不通过 Request DTO */
@Data
public class BpmMessageSendWhenProcessInstanceRejectReqDTO {

    /** 流程实例的编号 */
    @NotEmpty(message = "流程实例的编号不能为空")
    private String processInstanceId;
    /** 流程实例的名字 */
    @NotEmpty(message = "流程实例的名字不能为空")
    private String processInstanceName;

    @NotNull(message = "发起人的用户编号")
    private Long startUserId;

    /** 不通过理由 */
    @NotEmpty(message = "不通过理由不能为空")
    private String reason;
}
