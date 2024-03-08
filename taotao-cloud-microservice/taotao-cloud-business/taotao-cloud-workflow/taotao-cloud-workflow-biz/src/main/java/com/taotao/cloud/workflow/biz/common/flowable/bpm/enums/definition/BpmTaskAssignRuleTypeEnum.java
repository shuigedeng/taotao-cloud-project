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
 * BPM 任务分配规则的类型枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum BpmTaskAssignRuleTypeEnum {
    ROLE(10, "角色"),
    DEPT_MEMBER(20, "部门的成员"), // 包括负责人
    DEPT_LEADER(21, "部门的负责人"),
    POST(22, "岗位"),
    USER(30, "用户"),
    USER_GROUP(40, "用户组"),
    SCRIPT(50, "自定义脚本"), // 例如说，发起人所在部门的领导、发起人所在部门的领导的领导
    ;

    /** 类型 */
    private final Integer type;
    /** 描述 */
    private final String desc;
}
