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

package com.taotao.cloud.workflow.biz.engine.service;

import com.taotao.cloud.workflow.biz.common.model.engine.FlowHandleModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowBeforeInfoVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowSummary;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcandidate.FlowCandidateUserModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcandidate.FlowCandidateVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorRecordEntity;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import java.util.List;

/** 流程引擎 */
public interface FlowTaskNewService {

    /**
     * 保存
     *
     * @param flowModel 保存数据
     * @return
     * @throws WorkFlowException 异常
     */
    FlowTaskEntity saveIsAdmin(FlowModel flowModel) throws WorkFlowException;

    /**
     * 保存
     *
     * @param flowModel 保存数据
     * @return
     * @throws WorkFlowException 异常
     */
    FlowTaskEntity save(FlowModel flowModel) throws WorkFlowException;

    /**
     * 提交
     *
     * @param flowModel 提交数据
     * @throws WorkFlowException 异常
     */
    void submit(FlowModel flowModel) throws WorkFlowException;

    /**
     * 审批
     *
     * @param id 待办主键
     * @param flowModel 提交数据
     * @throws WorkFlowException 异常
     */
    void audit(String id, FlowModel flowModel) throws WorkFlowException;

    /**
     * 审批
     *
     * @param flowTask 流程实例
     * @param operator 流程经办
     * @param flowModel 提交数据
     * @throws WorkFlowException
     */
    void audit(FlowTaskEntity flowTask, FlowTaskOperatorEntity operator, FlowModel flowModel) throws WorkFlowException;

    /**
     * 驳回
     *
     * @param id 待办主键
     * @param flowModel 提交数据
     * @return
     * @throws WorkFlowException 异常
     */
    void reject(String id, FlowModel flowModel) throws WorkFlowException;

    /**
     * 驳回
     *
     * @param flowTask 流程实例
     * @param operator 流程经办
     * @param flowModel 提交数据
     * @throws WorkFlowException
     */
    void reject(FlowTaskEntity flowTask, FlowTaskOperatorEntity operator, FlowModel flowModel) throws WorkFlowException;

    /**
     * 已办撤回
     *
     * @param id 已办id
     * @param operatorRecord 经办记录
     * @param flowModel 提交数据
     * @throws WorkFlowException 异常
     */
    void recall(String id, FlowTaskOperatorRecordEntity operatorRecord, FlowModel flowModel) throws WorkFlowException;

    /**
     * 发起撤回
     *
     * @param flowTask 流程实例
     * @param flowModel 提交数据
     */
    void revoke(FlowTaskEntity flowTask, FlowModel flowModel);

    /**
     * 终止
     *
     * @param flowTask 流程实例
     * @param flowModel 提交数据
     */
    void cancel(FlowTaskEntity flowTask, FlowModel flowModel);

    /**
     * 指派
     *
     * @param id
     * @param flowModel 提交数据
     * @return
     */
    boolean assign(String id, FlowModel flowModel) throws WorkFlowException;

    /**
     * 转办
     *
     * @param taskOperator 经办数据
     */
    void transfer(FlowTaskOperatorEntity taskOperator) throws WorkFlowException;

    /**
     * 获取任务详情
     *
     * @param id 主键
     * @param taskNodeId
     * @param taskOperatorId
     * @return
     * @throws WorkFlowException 异常
     */
    FlowBeforeInfoVO getBeforeInfo(String id, String taskNodeId, String taskOperatorId) throws WorkFlowException;

    /**
     * 查询审批汇总
     *
     * @param id 主键
     * @param category 类型(1.部门 2.角色 3.岗位)
     * @param type 0.查询全部 1.查询通过和拒绝
     * @return
     */
    List<FlowSummary> recordList(String id, String category, String type);

    /**
     * 催办
     *
     * @param id 主键
     * @return
     * @throws WorkFlowException
     */
    boolean press(String id) throws WorkFlowException;

    /**
     * 获取候选人节点
     *
     * @param id 主键
     * @param flowHandleModel 数据
     * @return
     * @throws WorkFlowException
     */
    List<FlowCandidateVO> candidates(String id, FlowHandleModel flowHandleModel) throws WorkFlowException;

    /**
     * 获取候选人list
     *
     * @param id 主键
     * @param flowHandleModel 数据
     * @return
     * @throws WorkFlowException
     */
    List<FlowCandidateUserModel> candidateUser(String id, FlowHandleModel flowHandleModel) throws WorkFlowException;

    /**
     * 批量审批
     *
     * @param flowHandleModel
     * @throws WorkFlowException
     */
    void batch(FlowHandleModel flowHandleModel) throws WorkFlowException;

    /**
     * 批量获取候选人节点
     *
     * @return
     */
    List<FlowCandidateVO> batchCandidates(String flowId, String taskOperatorId) throws WorkFlowException;

    /**
     * 判断操作权限
     *
     * @param userId
     * @param flowId
     * @param operator
     * @param msg
     * @throws WorkFlowException
     */
    void permissions(String userId, String flowId, FlowTaskOperatorEntity operator, String msg)
            throws WorkFlowException;
}
