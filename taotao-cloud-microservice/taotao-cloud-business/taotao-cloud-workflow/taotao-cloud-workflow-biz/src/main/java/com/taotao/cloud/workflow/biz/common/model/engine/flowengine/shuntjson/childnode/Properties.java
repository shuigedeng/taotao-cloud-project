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

package com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode;

import com.taotao.cloud.workflow.biz.engine.util.FlowNature;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** 解析引擎 */
@Data
public class Properties {

    /** 流程代办 */
    public MsgConfig waitMsgConfig = new MsgConfig();
    /** 流程结束 */
    public MsgConfig endMsgConfig = new MsgConfig();
    /** 节点同意 */
    public MsgConfig approveMsgConfig = new MsgConfig();
    /** 节点拒绝 */
    public MsgConfig rejectMsgConfig = new MsgConfig();
    /** 节点抄送 */
    public MsgConfig copyMsgConfig = new MsgConfig();
    /** 子流程 */
    public MsgConfig launchMsgConfig = new MsgConfig();
    /** condition属性 */
    private Boolean isDefault = false;

    private String priority;
    private List<ProperCond> conditions = new ArrayList<>();
    /** approver属性 */
    private String title;
    /** 发起人 */
    private List<String> initiator = new ArrayList<>();
    /** 发起岗位 */
    private List<String> initiatePos = new ArrayList<>();
    /** 发起角色 */
    private List<String> initiateRole = new ArrayList<>();
    /** 批准人 */
    private List<String> approvers = new ArrayList<>();
    /** 批准岗位 */
    private List<String> approverPos = new ArrayList<>();
    /** 批准角色 */
    private List<String> approverRole = new ArrayList<>();
    /** 经办对象 */
    private String assigneeType;
    /** 字段 */
    private List<FormOperates> formOperates = new ArrayList<>();
    /** 传阅岗位 */
    private List<String> circulatePosition = new ArrayList<>();
    /** 传阅人 */
    private List<String> circulateUser = new ArrayList<>();
    /** 传阅角色 */
    private List<String> circulateRole = new ArrayList<>();
    /** 流程进度 */
    private String progress;
    /** 驳回步骤 1.上一步骤 0.返回开始 */
    private String rejectStep;
    /** 备注 */
    private String description;

    /** 定时器* */
    /** 拒绝事件 */
    private FuncConfig rejectFuncConfig = new FuncConfig();
    /** 同意事件 */
    private FuncConfig approveFuncConfig = new FuncConfig();
    /** 开始事件 */
    private FuncConfig initFuncConfig = new FuncConfig();
    /** 结束事件 */
    private FuncConfig endFuncConfig = new FuncConfig();

    /** 新加属性* */
    /** 节点撤回事件 */
    private FuncConfig recallFuncConfig = new FuncConfig();
    /** 发起撤回事件 */
    private FuncConfig flowRecallFuncConfig = new FuncConfig();
    /** 天 */
    private Integer day = 0;
    /** 时 */
    private Integer hour = 0;
    /** 分 */
    private Integer minute = 0;
    /** 秒 */
    private Integer second = 0;
    /** 指定人审批(0 或签 1 会签) */
    private Integer counterSign = FlowNature.Fixedapprover;
    /** 自定义抄送人 */
    private Boolean isCustomCopy = false;
    /** 发起人的第几级主管 */
    private Integer managerLevel = 1;
    /** 表单字段 */
    private String formField;
    /** 审批节点 */
    private String nodeId;
    /** 会签比例 */
    private Long countersignRatio = 100L;
    /** 请求路径 */
    private String getUserUrl;
    /** 审批人为空时是否自动通过 */
    private Boolean noApproverHandler = false;
    /** 前台按钮权限 */
    private Boolean hasAuditBtn = true;
    /** 前台通过 */
    private String auditBtnText = "通过";
    /** 前台按钮权限 */
    private Boolean hasRejectBtn = true;
    /** 前台拒绝 */
    private String rejectBtnText = "拒绝";
    /** 前台按钮权限 */
    private Boolean hasRevokeBtn = true;
    /** 前台撤回 */
    private String revokeBtnText = "撤回";
    /** 前台按钮权限 */
    private Boolean hasTransferBtn = true;
    /** 前台转办 */
    private String transferBtnText = "转办";
    /** 前台按钮权限 */
    private Boolean hasSubmitBtn = true;
    /** 前台提交 */
    private String submitBtnText = "提交审核";
    /** 前台按钮权限 */
    private Boolean hasSaveBtn = false;
    /** 前台保存 */
    private String saveBtnText = "保存草稿";
    /** 前台按钮权限 */
    private Boolean hasPressBtn = true;
    /** 前台催办 */
    private String pressBtnText = "催办";
    /** 前台打印权限 */
    private Boolean hasPrintBtn = false;
    /** 前台打印 */
    private String printBtnText = "打印";

    /** 子流程属性* */
    /** 打印id */
    private String printId;
    /** 是否批量审批 */
    private Boolean isBatchApproval = false;
    /** 是否评论 */
    private Boolean isComment = false;
    /** 是否有签名 */
    private Boolean hasSign = false;
    /** 超时设置 */
    private TimeOutConfig timeoutConfig = new TimeOutConfig();
    /** 是否加签 */
    private Boolean hasFreeApprover = false;
    /** 审批类型 */
    private String initiateType;
    /** 子流程引擎id */
    private String flowId;
    /** 子流程赋值 */
    private List<FlowAssignModel> assignList = new ArrayList<>();
    /** 子流程异步同步(true 异步 false同步) */
    private Boolean isAsync = false;
}
