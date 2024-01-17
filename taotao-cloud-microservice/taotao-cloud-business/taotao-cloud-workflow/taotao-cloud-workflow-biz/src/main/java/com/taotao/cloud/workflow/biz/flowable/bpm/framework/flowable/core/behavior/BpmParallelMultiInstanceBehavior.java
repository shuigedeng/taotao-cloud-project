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

package com.taotao.cloud.workflow.biz.flowable.bpm.framework.flowable.core.behavior;

import com.taotao.cloud.flowable.biz.bpm.service.definition.BpmTaskAssignRuleService;
import com.taotao.cloud.flowable.biz.flowable.core.util.FlowableUtils;
import java.util.Set;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Activity;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;

/**
 * 自定义的【并行】的【多个】流程任务的 assignee 负责人的分配 第一步，基于分配规则，计算出分配任务的【多个】候选人们。 第二步，将【多个】任务候选人们，设置到
 * DelegateExecution 的 collectionVariable 变量中，以便 BpmUserTaskActivityBehavior 使用它
 *
 * @author kemengkai
 * @since 2022-04-21 16:57
 */
@Slf4j
public class BpmParallelMultiInstanceBehavior extends ParallelMultiInstanceBehavior {

    @Setter
    private BpmTaskAssignRuleService bpmTaskRuleService;

    public BpmParallelMultiInstanceBehavior(Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior) {
        super(activity, innerActivityBehavior);
    }

    /**
     * 重写该方法，主要实现两个功能： 1. 忽略原有的 collectionVariable、collectionElementVariable 表达式，而是采用自己定义的 2.
     * 获得任务的处理人，并设置到 collectionVariable 中，用于 BpmUserTaskActivityBehavior 从中可以获取任务的处理人
     *
     * <p>注意，多个任务实例，每个任务实例对应一个处理人，所以返回的数量就是任务处理人的数量
     *
     * @param execution 执行任务
     * @return 数量
     */
    @Override
    protected int resolveNrOfInstances(DelegateExecution execution) {
        // 第一步，设置 collectionVariable 和 CollectionVariable
        // 从  execution.getVariable() 读取所有任务处理人的 key
        super.collectionExpression = null; // collectionExpression 和 collectionVariable 是互斥的
        super.collectionVariable = FlowableUtils.formatCollectionVariable(execution.getCurrentActivityId());
        // 从 execution.getVariable() 读取当前所有任务处理的人的 key
        super.collectionElementVariable =
                FlowableUtils.formatCollectionElementVariable(execution.getCurrentActivityId());

        // 第二步，获取任务的所有处理人
        Set<Long> assigneeUserIds = bpmTaskRuleService.calculateTaskCandidateUsers(execution);
        execution.setVariable(super.collectionVariable, assigneeUserIds);
        return assigneeUserIds.size();
    }
}
