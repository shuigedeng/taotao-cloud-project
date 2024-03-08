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

package com.taotao.cloud.workflow.biz.common.flowable.bpm.enums.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BPM 任务规则的脚本枚举 目前暂时通过 TODO 芋艿：硬编码，未来可以考虑 Groovy 动态脚本的方式
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum BpmTaskRuleScriptEnum {
    START_USER(10L, "流程发起人"),

    LEADER_X1(20L, "流程发起人的一级领导"),
    LEADER_X2(21L, "流程发起人的二级领导");

    /** 脚本编号 */
    private final Long id;
    /** 脚本描述 */
    private final String desc;
}
