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

package com.taotao.cloud.workflow.biz.common.model.engine.flowengine;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class FlowModel {
    /** 判断新增 */
    private String id;
    /** 引擎id */
    private String flowId;
    /** 流程主键 */
    private String processId;
    /** 流程标题 */
    private String flowTitle;
    /** 紧急程度 */
    private Integer flowUrgent;
    /** 流水号 */
    private String billNo;
    /** 提交表单对象 */
    private Object formEntity;
    /** 审核表单数据 */
    private Map<String, Object> formData;
    /** 加签人 */
    private String freeApproverUserId;
    /** 0.提交 1.保存 */
    private String status;
    /** 意见 */
    private String handleOpinion;
    /** 签名 */
    private String signImg;
    /** 加签人 */
    private String copyIds;
    /** 子流程 */
    private String parentId;
    /** 创建人 */
    private String userId;
    /** 当前经办id */
    private String operatorId;
    /** 同步异步 */
    private Boolean isAsync = false;
    /** 候选人 */
    private Map<String, List<String>> candidateList;
    /** 指派节点 */
    private String nodeCode;
}
