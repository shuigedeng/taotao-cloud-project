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

package com.taotao.cloud.workflow.biz.flowable.bpm.framework.flowable.core.behavior.script;

import cn.iocoder.yudao.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import java.util.Set;
import org.flowable.engine.delegate.DelegateExecution;

/**
 * Bpm 任务分配的自定义 Script 脚本 使用场景： 1. 设置审批人为发起人 2. 设置审批人为发起人的 Leader 3. 甚至审批人为发起人的 Leader 的 Leader
 *
 * @author 芋道源码
 */
public interface BpmTaskAssignScript {

    /**
     * 基于执行任务，获得任务的候选用户们
     *
     * @param execution 执行任务
     * @return 候选人用户的编号数组
     */
    Set<Long> calculateTaskCandidateUsers(DelegateExecution execution);

    /**
     * 获得枚举值
     *
     * @return 枚举值
     */
    BpmTaskRuleScriptEnum getEnum();
}
