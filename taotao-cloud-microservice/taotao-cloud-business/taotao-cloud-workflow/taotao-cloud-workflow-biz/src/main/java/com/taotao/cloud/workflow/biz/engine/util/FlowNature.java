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

package com.taotao.cloud.workflow.biz.engine.util;

import lombok.Data;

/** 在线工作流开发 */
@Data
public class FlowNature {

    /** 驳回开始* */
    public static String START = "0";

    /** 驳回上一节点* */
    public static String UP = "1";

    /** 系统表单* */
    public static Integer SYSTEM = 1;

    /** 自定义表单* */
    public static Integer CUSTOM = 2;

    /** 待办事宜* */
    public static String WAIT = "1";

    /** 已办事宜* */
    public static String TRIAL = "2";

    /** 抄送事宜* */
    public static String CIRCULATE = "3";

    /** 抄送事宜* */
    public static String BATCH = "4";

    /** 工作流完成* */
    public static String CompletionEnd = "100";

    /** 流程父节点* */
    public static String ParentId = "0";

    /** 加签人* */
    public static String RecordStatus = "0";

    /** 审批开始* */
    public static String NodeStart = "start";

    /** 审批结束* */
    public static String NodeEnd = "end";

    /** 子流程* */
    public static String NodeSubFlow = "subFlow";

    /** 或签* */
    public static Integer Fixedapprover = 0;

    /** 会签* */
    public static Integer FixedJointlyApprover = 1;

    /** 通过* */
    public static Integer AuditCompletion = 1;

    /** 拒绝* */
    public static Integer RejectCompletion = -1;

    /** 进行* */
    public static Integer ProcessCompletion = 0;

    /** 子流程同步* */
    public static Integer ChildSync = 0;

    /** 子流程异步* */
    public static Integer ChildAsync = 1;
}
