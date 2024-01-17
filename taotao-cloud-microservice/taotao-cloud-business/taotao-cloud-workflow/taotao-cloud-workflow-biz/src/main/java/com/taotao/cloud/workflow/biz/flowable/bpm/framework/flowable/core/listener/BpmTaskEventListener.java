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
import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.task.BpmTaskExtDO;
import com.taotao.cloud.flowable.biz.bpm.service.task.BpmActivityService;
import com.taotao.cloud.flowable.biz.bpm.service.task.BpmTaskService;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableActivityCancelledEvent;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.task.api.Task;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 监听 {@link org.flowable.task.api.Task} 的开始与完成，创建与更新对应的 {@link BpmTaskExtDO} 记录
 *
 * @author jason
 */
@Component
@Slf4j
public class BpmTaskEventListener extends AbstractFlowableEngineEventListener {

    @Resource
    @Lazy // 解决循环依赖
    private BpmTaskService taskService;

    @Resource
    @Lazy // 解决循环依赖
    private BpmActivityService activityService;

    public static final Set<FlowableEngineEventType> TASK_EVENTS = ImmutableSet.<FlowableEngineEventType>builder()
            .add(FlowableEngineEventType.TASK_CREATED)
            .add(FlowableEngineEventType.TASK_ASSIGNED)
            .add(FlowableEngineEventType.TASK_COMPLETED)
            .add(FlowableEngineEventType.ACTIVITY_CANCELLED)
            .build();

    public BpmTaskEventListener() {
        super(TASK_EVENTS);
    }

    @Override
    protected void taskCreated(FlowableEngineEntityEvent event) {
        taskService.createTaskExt((Task) event.getEntity());
    }

    @Override
    protected void taskCompleted(FlowableEngineEntityEvent event) {
        taskService.updateTaskExtComplete((Task) event.getEntity());
    }

    @Override
    protected void taskAssigned(FlowableEngineEntityEvent event) {
        taskService.updateTaskExtAssign((Task) event.getEntity());
    }

    @Override
    protected void activityCancelled(FlowableActivityCancelledEvent event) {
        List<HistoricActivityInstance> activityList =
                activityService.getHistoricActivityListByExecutionId(event.getExecutionId());
        if (CollUtil.isEmpty(activityList)) {
            log.error("[activityCancelled][使用 executionId({}) 查找不到对应的活动实例]", event.getExecutionId());
            return;
        }
        // 遍历处理
        activityList.forEach(activity -> {
            if (StrUtil.isEmpty(activity.getTaskId())) {
                return;
            }
            taskService.updateTaskExtCancel(activity.getTaskId());
        });
    }
}
