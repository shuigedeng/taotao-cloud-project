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

package com.taotao.cloud.workflow.biz.flowable.bpm.framework.bpm.core.event;

import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * 流程实例的结果发生变化的 Event 定位：由于额外增加了 {@link BpmProcessInstanceExtDO#getResult()} 结果，所以增加该事件
 *
 * @author 芋道源码
 */
@SuppressWarnings("ALL")
@Data
public class BpmProcessInstanceResultEvent extends ApplicationEvent {

    /** 流程实例的编号 */
    @NotNull(message = "流程实例的编号不能为空")
    private String id;
    /** 流程实例的 key */
    @NotNull(message = "流程实例的 key 不能为空")
    private String processDefinitionKey;
    /** 流程实例的结果 */
    @NotNull(message = "流程实例的结果不能为空")
    private Integer result;
    /** 流程实例对应的业务标识 例如说，请假 */
    private String businessKey;

    public BpmProcessInstanceResultEvent(Object source) {
        super(source);
    }
}
