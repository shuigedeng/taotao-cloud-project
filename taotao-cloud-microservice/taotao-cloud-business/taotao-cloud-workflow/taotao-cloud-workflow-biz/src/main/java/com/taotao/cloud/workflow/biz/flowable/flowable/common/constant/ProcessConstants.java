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

package com.taotao.cloud.workflow.biz.flowable.flowable.common.constant;

/**
 * 流程常量信息
 *
 * @author Tony
 * @since 2021/4/17 22:46
 */
public class ProcessConstants {

    /** 动态数据 */
    public static final String DYNAMIC = "dynamic";

    /** 固定任务接收 */
    public static final String FIXED = "fixed";

    /** 单个审批人 */
    public static final String ASSIGNEE = "assignee";

    /** 候选人 */
    public static final String CANDIDATE_USERS = "candidateUsers";

    /** 审批组 */
    public static final String CANDIDATE_GROUPS = "candidateGroups";

    /** 单个审批人 */
    public static final String PROCESS_APPROVAL = "approval";

    /** 会签人员 */
    public static final String PROCESS_MULTI_INSTANCE_USER = "userList";

    /** nameapace */
    public static final String NAMASPASE = "http://flowable.org/bpmn";

    /** 会签节点 */
    public static final String PROCESS_MULTI_INSTANCE = "multiInstance";

    /** 自定义属性 dataType */
    public static final String PROCESS_CUSTOM_DATA_TYPE = "dataType";

    /** 自定义属性 userType */
    public static final String PROCESS_CUSTOM_USER_TYPE = "userType";

    /** 初始化人员 */
    public static final String PROCESS_INITIATOR = "INITIATOR";

    /** 流程跳过 */
    public static final String FLOWABLE_SKIP_EXPRESSION_ENABLED = "_FLOWABLE_SKIP_EXPRESSION_ENABLED";
}
