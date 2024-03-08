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

package com.taotao.cloud.workflow.biz.common.flowable.bpm.api.task.dto;

import java.util.Map;
import lombok.Data;

/**
 * 流程实例的创建 Request DTO
 *
 * @author 芋道源码
 */
@Data
public class BpmProcessInstanceCreateReqDTO {

    /** 流程定义的标识 */
    @NotEmpty(message = "流程定义的标识不能为空")
    private String processDefinitionKey;
    /** 变量实例 */
    private Map<String, Object> variables;

    /**
     * 业务的唯一标识
     *
     * <p>例如说，请假申请的编号。通过它，可以查询到对应的实例
     */
    @NotEmpty(message = "业务的唯一标识")
    private String businessKey;
}
