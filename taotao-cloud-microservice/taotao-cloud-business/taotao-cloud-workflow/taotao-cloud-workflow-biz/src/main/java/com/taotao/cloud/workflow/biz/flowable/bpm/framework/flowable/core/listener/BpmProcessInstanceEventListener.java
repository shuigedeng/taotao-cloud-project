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

package com.taotao.cloud.workflow.biz.flowable.bpm.framework.flowable.core.listener;

import com.google.common.collect.ImmutableSet;
import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import com.taotao.cloud.flowable.biz.bpm.service.task.BpmProcessInstanceService;
import java.util.Set;
import javax.annotation.Resource;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableCancelledEvent;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 监听 {@link ProcessInstance} 的开始与完成，创建与更新对应的 {@link BpmProcessInstanceExtDO} 记录
 *
 * @author jason
 */
@Component
public class BpmProcessInstanceEventListener extends AbstractFlowableEngineEventListener {

    @Resource
    @Lazy
    private BpmProcessInstanceService processInstanceService;

    public static final Set<FlowableEngineEventType> PROCESS_INSTANCE_EVENTS =
            ImmutableSet.<FlowableEngineEventType>builder()
                    .add(FlowableEngineEventType.PROCESS_CREATED)
                    .add(FlowableEngineEventType.PROCESS_CANCELLED)
                    .add(FlowableEngineEventType.PROCESS_COMPLETED)
                    .build();

    public BpmProcessInstanceEventListener() {
        super(PROCESS_INSTANCE_EVENTS);
    }

    @Override
    protected void processCreated(FlowableEngineEntityEvent event) {
        processInstanceService.createProcessInstanceExt((ProcessInstance) event.getEntity());
    }

    @Override
    protected void processCancelled(FlowableCancelledEvent event) {
        processInstanceService.updateProcessInstanceExtCancel(event);
    }

    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
        processInstanceService.updateProcessInstanceExtComplete((ProcessInstance) event.getEntity());
    }
}
