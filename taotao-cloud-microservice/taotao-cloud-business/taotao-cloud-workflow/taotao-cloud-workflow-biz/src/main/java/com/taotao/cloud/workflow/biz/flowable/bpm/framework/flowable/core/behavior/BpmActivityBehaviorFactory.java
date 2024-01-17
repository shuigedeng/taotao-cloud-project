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

import com.taotao.cloud.flowable.biz.bpm.framework.flowable.core.behavior.BpmParallelMultiInstanceBehavior;
import com.taotao.cloud.flowable.biz.bpm.framework.flowable.core.behavior.BpmUserTaskActivityBehavior;
import com.taotao.cloud.flowable.biz.bpm.service.definition.BpmTaskAssignRuleService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.flowable.bpmn.model.Activity;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.flowable.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

/**
 * 自定义的 ActivityBehaviorFactory 实现类，目的如下： 1. 自定义 {@link
 * #createUserTaskActivityBehavior(UserTask)}：实现自定义的流程任务的 assignee 负责人的分配
 *
 * @author 芋道源码
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmActivityBehaviorFactory extends DefaultActivityBehaviorFactory {

    @Setter
    private BpmTaskAssignRuleService bpmTaskRuleService;

    @Override
    public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
        return new BpmUserTaskActivityBehavior(userTask).setBpmTaskRuleService(bpmTaskRuleService);
    }

    @Override
    public ParallelMultiInstanceBehavior createParallelMultiInstanceBehavior(
            Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior) {
        return new BpmParallelMultiInstanceBehavior(activity, innerActivityBehavior)
                .setBpmTaskRuleService(bpmTaskRuleService);
    }

    // TODO @ke：SequentialMultiInstanceBehavior 这个抽空也可以看看

}
