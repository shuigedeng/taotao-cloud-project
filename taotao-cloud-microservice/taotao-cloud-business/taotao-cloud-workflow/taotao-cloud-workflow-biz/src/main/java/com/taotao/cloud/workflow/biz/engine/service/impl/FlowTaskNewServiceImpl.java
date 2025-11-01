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

package com.taotao.cloud.workflow.biz.engine.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.api.vo.OrganizeEntity;
import com.taotao.cloud.workflow.api.vo.PositionEntity;
import com.taotao.cloud.workflow.api.vo.RoleEntity;
import com.taotao.cloud.workflow.api.vo.UserEntity;
import com.taotao.cloud.workflow.api.vo.UserRelationEntity;
import com.taotao.cloud.workflow.biz.common.base.Pagination;
import com.taotao.cloud.workflow.biz.common.base.UserInfo;
import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import com.taotao.cloud.workflow.biz.common.database.model.entity.DbLinkEntity;
import com.taotao.cloud.workflow.biz.common.model.FormAllModel;
import com.taotao.cloud.workflow.biz.common.model.FormEnum;
import com.taotao.cloud.workflow.biz.common.model.engine.FlowHandleModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowBeforeInfoVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowSummary;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowTaskModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowTaskNodeModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowTaskOperatorModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowTaskOperatorRecordModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcandidate.FlowCandidateUserModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcandidate.FlowCandidateVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowOperatordModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.ChildNode;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.FlowAssignModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.FormOperates;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.Properties;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.ChildNodeList;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.ConditionList;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.Custom;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.DateProperties;
import com.taotao.cloud.workflow.biz.common.model.engine.flowmessage.FlowMsgModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.method.TaskHandleIdStatus;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.method.TaskOperatoUser;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.method.TaskOperator;
import com.taotao.cloud.workflow.biz.common.model.visiual.FormDataModel;
import com.taotao.cloud.workflow.biz.common.model.visiual.RecursionForm;
import com.taotao.cloud.workflow.biz.common.model.visiual.fields.FieLdsModel;
import com.taotao.cloud.workflow.biz.common.util.DateUtil;
import com.taotao.cloud.workflow.biz.common.util.RandomUtil;
import com.taotao.cloud.workflow.biz.common.util.UserProvider;
import com.taotao.cloud.workflow.biz.common.util.context.SpringContext;
import com.taotao.cloud.workflow.biz.common.util.wxutil.HttpUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowCandidatesEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowDelegateEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskCirculateEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskNodeEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorRecordEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowNodeEnum;
import com.taotao.cloud.workflow.biz.engine.enums.FlowRecordEnum;
import com.taotao.cloud.workflow.biz.engine.enums.FlowRecordListEnum;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.enums.FlowTaskOperatorEnum;
import com.taotao.cloud.workflow.biz.engine.enums.FlowTaskStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowCandidatesService;
import com.taotao.cloud.workflow.biz.engine.service.FlowDelegateService;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskCirculateService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNewService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNodeService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorRecordService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.FlowDataUtil;
import com.taotao.cloud.workflow.biz.engine.util.FlowJsonUtil;
import com.taotao.cloud.workflow.biz.engine.util.FlowMsgUtil;
import com.taotao.cloud.workflow.biz.engine.util.FlowNature;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 流程引擎 */
@Service
@Slf4j
public class FlowTaskNewServiceImpl implements FlowTaskNewService {

    @Autowired
    private ServiceAllUtil serviceUtil;

    @Autowired
    private FlowCandidatesService flowCandidatesService;

    @Autowired
    private FlowTaskNodeService flowTaskNodeService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    @Autowired
    private FlowTaskOperatorRecordService flowTaskOperatorRecordService;

    @Autowired
    private FlowTaskCirculateService flowTaskCirculateService;

    @Autowired
    private FlowEngineService flowEngineService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private FlowDataUtil flowDataUtil;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private FlowMsgUtil flowMsgUtil;

    @Autowired
    private FlowDelegateService flowDelegateService;

    /** 节点id */
    private String taskNodeId = "taskNodeId";
    /** 任务id */
    private String taskId = "taskId";
    /** 空节点默认审批人 */
    private String user = "admin";

    @Override
    public FlowTaskEntity saveIsAdmin(FlowModel flowModel) throws WorkFlowException {
        return this.save(flowModel);
    }

    @Override
    @DSTransactional
    public FlowTaskEntity save(FlowModel flowModel) throws WorkFlowException {
        String flowId = flowModel.getFlowId();
        UserInfo userInfo = userProvider.get();
        flowModel.setStatus(
                StrUtil.isNotEmpty(flowModel.getStatus()) ? flowModel.getStatus() : FlowStatusEnum.save.getMessage());
        String userId = StrUtil.isNotEmpty(flowModel.getUserId()) ? flowModel.getUserId() : userInfo.getUserId();
        // 流程引擎
        FlowEngineEntity engine = flowEngineService.getInfo(flowId);
        boolean flag = flowModel.getId() == null;
        // 流程实例
        FlowTaskEntity taskEntity = new FlowTaskEntity();
        if (!flag) {
            flowModel.setProcessId(flowModel.getId());
            taskEntity = flowTaskService.getInfo(flowModel.getProcessId());
            if (!FlowNature.ParentId.equals(taskEntity.getParentId())) {
                flowModel.setParentId(taskEntity.getParentId());
                flowModel.setFlowTitle(taskEntity.getFullName());
                flowModel.setIsAsync(FlowNature.ChildAsync.equals(taskEntity.getIsAsync()));
            }
        }
        this.task(taskEntity, engine, flowModel, userId);
        // 更新流程任务
        if (flag) {
            flowTaskService.create(taskEntity);
        } else {
            flowTaskService.update(taskEntity);
        }
        return taskEntity;
    }

    @Override
    @DSTransactional
    public void submit(FlowModel flowModel) throws WorkFlowException {
        UserInfo userInfo = userProvider.get();
        flowModel.setStatus(FlowStatusEnum.submit.getMessage());
        // 流程实例
        FlowTaskEntity flowTask = saveIsAdmin(flowModel);
        FlowEngineEntity engine = flowEngineService.getInfo(flowTask.getFlowId());
        flowTask.setStartTime(new Date());
        flowModel.setOperatorId(FlowNature.ParentId);
        // 流程节点
        List<FlowTaskNodeEntity> taskNodeList = new LinkedList<>();
        // 流程经办
        List<FlowTaskOperatorEntity> operatorList = new ArrayList<>();
        // 流程表单Json
        String formDataJson = flowTask.getFlowTemplateJson();
        ChildNode childNodeAll = JsonUtils.toObject(formDataJson, ChildNode.class);
        // 获取流程节点
        List<ChildNodeList> nodeListAll = new ArrayList<>();
        List<ConditionList> conditionListAll = new ArrayList<>();
        this.updateNodeList(flowTask, childNodeAll, nodeListAll, conditionListAll, taskNodeList);
        // 保存节点
        this.nodeListAll(taskNodeList, flowModel, true);
        // 获取下一个节点
        Optional<FlowTaskNodeEntity> first = taskNodeList.stream()
                .filter(t -> FlowNature.NodeStart.equals(t.getNodeType()))
                .findFirst();
        if (first.isEmpty()) {
            throw new WorkFlowException(MsgCode.COD001.get());
        }
        FlowTaskNodeEntity startNode = first.get();
        List<String> nodeList = Arrays.asList(startNode.getNodeNext().split(","));
        // 获取下一审批人
        List<ChildNodeList> nextOperatorList = nodeListAll.stream()
                .filter(t -> nodeList.contains(t.getCustom().getNodeId()))
                .toList();
        Map<String, List<String>> nodeIdAll = this.nextOperator(operatorList, nextOperatorList, flowTask, flowModel);
        // 审核人
        flowTaskOperatorService.create(operatorList);
        // 更新关联子流程id
        for (String nodeId : nodeIdAll.keySet()) {
            FlowTaskNodeEntity entity = flowTaskNodeService.getInfo(nodeId);
            if (entity != null) {
                ChildNodeList childNodeList = JsonUtils.toObject(entity.getNodePropertyJson(), ChildNodeList.class);
                childNodeList.getCustom().setTaskId(nodeIdAll.get(nodeId));
                entity.setNodePropertyJson(JsonUtils.toJSONString(childNodeList));
                flowTaskNodeService.update(entity);
            }
        }
        // 提交记录
        ChildNodeList start = JsonUtils.toObject(startNode.getNodePropertyJson(), ChildNodeList.class);
        boolean request = requestData(start, flowModel.getFormData());
        if (request) {
            throw new WorkFlowException(MsgCode.WF001.get());
        }
        FlowTaskOperatorEntity operator = new FlowTaskOperatorEntity();
        operator.setTaskId(flowTask.getId());
        operator.setNodeCode(start.getCustom().getNodeId());
        FlowTaskOperatorRecordEntity operatorRecord = new FlowTaskOperatorRecordEntity();
        // 审批数据赋值
        FlowOperatordModel flowOperatordModel = new FlowOperatordModel();
        flowOperatordModel.setStatus(FlowRecordEnum.submit.getCode());
        flowOperatordModel.setFlowModel(flowModel);
        flowOperatordModel.setUserId(userInfo.getUserId());
        flowOperatordModel.setOperator(operator);
        this.operatorRecord(operatorRecord, flowOperatordModel);
        flowTaskOperatorRecordService.create(operatorRecord);
        // 定时器
        FlowTaskOperatorEntity startOperator = new FlowTaskOperatorEntity();
        startOperator.setTaskId(start.getTaskId());
        startOperator.setTaskNodeId(start.getTaskNodeId());
        DateProperties timer = start.getTimer();
        List<Date> dateList = new ArrayList<>();
        if (timer.getTime()) {
            Date date = new Date();
            date = DateUtil.dateAddDays(date, timer.getDay());
            date = DateUtil.dateAddHours(date, timer.getHour());
            date = DateUtil.dateAddMinutes(date, timer.getMinute());
            date = DateUtil.dateAddSeconds(date, timer.getSecond());
            dateList.add(date);
        }
        startOperator.setDescription(JsonUtils.toJSONString(dateList));
        List<FlowTaskOperatorEntity> operatorAll = this.timer(startOperator, taskNodeList, operatorList);
        for (FlowTaskOperatorEntity operatorTime : operatorAll) {
            List<Date> dateAll = JsonUtils.toList(operatorTime.getDescription(), Date.class);
            if (dateAll.size() > 0) {
                Date max = Collections.max(dateAll);
                operatorTime.setCreatorTime(max);
            }
            flowTaskOperatorService.update(operatorTime);
        }
        // 开始事件
        flowMsgUtil.event(1, start, operatorRecord, flowModel);
        // 更新流程节点
        if (StrUtil.isEmpty(flowTask.getThisStepId())) {
            this.getNextStepId(nextOperatorList, taskNodeList, flowTask, flowModel);
        }
        boolean isEnd = nodeList.contains(FlowNature.NodeEnd);
        if (isEnd) {
            this.endround(flowTask, nodeListAll.get(0), flowModel);
        }
        flowTaskService.update(flowTask);
        // 发送消息
        FlowMsgModel flowMsgModel = new FlowMsgModel();
        flowMsgModel.setCirculateList(new ArrayList<>());
        flowMsgModel.setNodeList(taskNodeList);
        flowMsgModel.setOperatorList(operatorList);
        flowMsgModel.setData(flowModel.getFormData());
        flowMsgModel.setTaskEntity(flowTask);
        if (isEnd) {
            flowMsgModel.setTaskNodeEntity(startNode);
        }
        flowMsgModel.setEngine(engine);
        flowMsgUtil.message(flowMsgModel);
    }

    @Override
    public void audit(String id, FlowModel flowModel) throws WorkFlowException {
        FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(id);
        if (operator != null) {
            if (FlowNature.ProcessCompletion.equals(operator.getCompletion())) {
                FlowTaskEntity flowTask = flowTaskService.getInfo(operator.getTaskId());
                this.audit(flowTask, operator, flowModel);
            }
        }
    }

    @Override
    @DSTransactional
    public void audit(FlowTaskEntity flowTask, FlowTaskOperatorEntity operator, FlowModel flowModel)
            throws WorkFlowException {
        // 更新数据
        FlowEngineEntity engine = flowEngineService.getInfo(flowTask.getFlowId());
        flowModel.setProcessId(flowTask.getId());
        flowModel.setId(flowTask.getId());
        Map<String, Object> dataAll = JsonUtils.toMap(flowTask.getFlowFormContentJson());
        if (FlowNature.CUSTOM.equals(engine.getFormType())) {
            Map<String, Object> formDataAll = flowModel.getFormData();
            flowModel.setFormData(dataAll);
            if (formDataAll.get("data") != null) {
                Map<String, Object> data = JsonUtils.toMap(String.valueOf(formDataAll.get("data")));
                flowModel.setFormData(data);
            }
        }
        // 更新新流程
        ChildNode childNodeAll = JsonUtils.toObject(flowTask.getFlowTemplateJson(), ChildNode.class);
        List<ChildNodeList> nodeListAll = new ArrayList<>();
        List<ConditionList> conditionListAll = new ArrayList<>();
        List<FlowTaskNodeEntity> taskNodeLis = new ArrayList<>();
        flowTask.setFlowFormContentJson(JsonUtils.toJSONString(flowModel.getFormData()));
        this.updateNodeList(flowTask, childNodeAll, nodeListAll, conditionListAll, taskNodeLis);
        this.nodeListAll(taskNodeLis, flowModel, false);
        this.updateTaskNode(taskNodeLis);
        if (!FlowNature.ProcessCompletion.equals(operator.getCompletion())) {
            throw new WorkFlowException(MsgCode.WF005.get());
        }
        UserInfo userInfo = userProvider.get();
        flowModel.setOperatorId(operator.getId());
        String userId = StrUtil.isNotEmpty(flowModel.getUserId()) ? flowModel.getUserId() : userInfo.getUserId();
        // 流程所有节点
        List<FlowTaskNodeEntity> flowTaskNodeAll = flowTaskNodeService.getList(flowTask.getId());
        List<FlowTaskNodeEntity> taskNodeList = flowTaskNodeAll.stream()
                .filter(t -> FlowNodeEnum.Process.getCode().equals(t.getState()))
                .toList();
        // 当前节点
        Optional<FlowTaskNodeEntity> first = taskNodeList.stream()
                .filter(m -> m.getId().equals(operator.getTaskNodeId()))
                .findFirst();
        if (first.isEmpty()) {
            throw new WorkFlowException(MsgCode.COD001.get());
        }
        FlowTaskNodeEntity taskNode = first.get();
        // 当前节点属性
        ChildNodeList nodeModel = JsonUtils.toObject(taskNode.getNodePropertyJson(), ChildNodeList.class);
        boolean request = requestData(nodeModel, flowModel.getFormData());
        if (request) {
            throw new WorkFlowException(MsgCode.WF001.get());
        }
        // 同意记录
        FlowTaskOperatorRecordEntity operatorRecord = new FlowTaskOperatorRecordEntity();
        // 审批数据赋值
        FlowOperatordModel flowOperatordModel = new FlowOperatordModel();
        flowOperatordModel.setStatus(FlowRecordEnum.audit.getCode());
        flowOperatordModel.setFlowModel(flowModel);
        flowOperatordModel.setUserId(userId);
        flowOperatordModel.setOperator(operator);
        this.operatorRecord(operatorRecord, flowOperatordModel);
        // 子流程不新增流转记录
        if (!flowModel.getIsAsync()) {
            flowTaskOperatorRecordService.create(operatorRecord);
        }
        // 修改或签、会签经办数据
        TaskHandleIdStatus handleIdStatus = new TaskHandleIdStatus();
        handleIdStatus.setStatus(1);
        handleIdStatus.setNodeModel(nodeModel);
        handleIdStatus.setUserInfo(userInfo);
        handleIdStatus.setTaskNodeList(taskNodeList);
        handleIdStatus.setFlowModel(flowModel);
        this.handleIdStatus(operator, handleIdStatus);
        // 更新流当前程经办状态
        if (StrUtil.isNotEmpty(operator.getId())) {
            flowTaskOperatorService.update(operator);
        }
        // 更新下一节点
        List<FlowTaskOperatorEntity> operatorList = new ArrayList<>();
        // 获取下一审批人
        List<FlowTaskNodeEntity> nextNode = taskNodeList.stream()
                .filter(t -> taskNode.getNodeNext().contains(t.getNodeCode()))
                .toList();
        List<ChildNodeList> nextOperatorList = new ArrayList<>();
        List<FlowTaskNodeEntity> result = this.isNextAll(taskNodeList, nextNode, taskNode, flowModel);
        for (FlowTaskNodeEntity entity : result) {
            ChildNodeList node = JsonUtils.toObject(entity.getNodePropertyJson(), ChildNodeList.class);
            nextOperatorList.add(node);
        }
        // 节点事件
        flowMsgUtil.event(4, nodeModel, operatorRecord, flowModel);
        Map<String, Object> data = this.createData(engine, flowTask, flowModel);
        // 更新流程节点
        this.getNextStepId(nextOperatorList, taskNodeList, flowTask, flowModel);
        flowTask.setFlowFormContentJson(JsonUtils.toJSONString(data));
        flowTaskService.update(flowTask);
        // 新增审批候选人
        Map<String, List<String>> candidateList =
                flowModel.getCandidateList() != null ? flowModel.getCandidateList() : new HashMap<>();
        for (String key : candidateList.keySet()) {
            FlowTaskNodeEntity taskNodeEntity = taskNodeList.stream()
                    .filter(t -> t.getNodeCode().equals(key))
                    .findFirst()
                    .orElse(null);
            if (taskNodeEntity != null) {
                List<String> list = candidateList.get(key);
                FlowCandidatesEntity entity = new FlowCandidatesEntity();
                entity.setHandleId(userInfo.getUserId());
                entity.setTaskId(taskNodeEntity.getTaskId());
                entity.setTaskNodeId(taskNodeEntity.getId());
                entity.setAccount(userInfo.getUserAccount());
                entity.setCandidates(JsonUtils.getObjectToString(list));
                entity.setOperatorId(operator.getId());
                flowCandidatesService.create(entity);
            }
        }
        // 下个节点
        Map<String, List<String>> nodeIdAll = this.nextOperator(operatorList, nextOperatorList, flowTask, flowModel);
        flowTaskOperatorService.create(operatorList);
        // 更新关联子流程id
        for (String nodeId : nodeIdAll.keySet()) {
            FlowTaskNodeEntity entity = flowTaskNodeService.getInfo(nodeId);
            if (entity != null) {
                ChildNodeList childNodeList = JsonUtils.toObject(entity.getNodePropertyJson(), ChildNodeList.class);
                childNodeList.getCustom().setTaskId(nodeIdAll.get(nodeId));
                entity.setNodePropertyJson(JsonUtils.toJSONString(childNodeList));
                flowTaskNodeService.update(entity);
            }
        }
        // 定时器
        List<FlowTaskOperatorEntity> operatorAll = this.timer(operator, taskNodeList, operatorList);
        for (FlowTaskOperatorEntity operatorTime : operatorAll) {
            List<Date> dateAll = JsonUtils.toList(operatorTime.getDescription(), Date.class);
            if (dateAll.size() > 0) {
                Date max = Collections.max(dateAll);
                operatorTime.setCreatorTime(max);
            }
            flowTaskOperatorService.update(operatorTime);
        }
        // 获取抄送人
        List<FlowTaskCirculateEntity> circulateList = new ArrayList<>();
        this.circulateList(nodeModel, circulateList, flowModel);
        flowTaskCirculateService.create(circulateList);
        // 发送消息
        FlowMsgModel flowMsgModel = new FlowMsgModel();
        flowMsgModel.setApprove(taskNode.getCompletion() == 1);
        flowMsgModel.setCopy(true);
        flowMsgModel.setNodeList(taskNodeList);
        for (FlowTaskOperatorEntity operatorEntity : operatorList) {
            operatorEntity.setTaskNodeId(taskNode.getId());
        }
        flowMsgModel.setOperatorList(operatorList);
        flowMsgModel.setCirculateList(circulateList);
        flowMsgModel.setData(flowModel.getFormData());
        flowMsgModel.setTaskNodeEntity(taskNode);
        flowMsgModel.setTaskEntity(flowTask);
        flowMsgModel.setEngine(engine);
        flowMsgModel.setTitle(StrUtil.isNotEmpty(flowModel.getFreeApproverUserId()) ? "已被【指派】" : "");
        flowMsgUtil.message(flowMsgModel);
    }

    @Override
    public void reject(String id, FlowModel flowModel) throws WorkFlowException {
        FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(id);
        if (operator != null) {
            if (FlowNature.ProcessCompletion.equals(operator.getCompletion())) {
                FlowTaskEntity flowTaskEntity = flowTaskService.getInfo(operator.getTaskId());
                this.reject(flowTaskEntity, operator, flowModel);
            }
        }
    }

    @Override
    @DSTransactional
    public void reject(FlowTaskEntity flowTask, FlowTaskOperatorEntity operator, FlowModel flowModel)
            throws WorkFlowException {
        UserInfo userInfo = userProvider.get();
        String userId = StrUtil.isNotEmpty(flowModel.getUserId()) ? flowModel.getUserId() : userInfo.getUserId();
        // 流程所有节点
        List<FlowTaskNodeEntity> flowTaskNodeAll = flowTaskNodeService.getList(flowTask.getId());
        List<FlowTaskNodeEntity> taskNodeList = flowTaskNodeAll.stream()
                .filter(t -> FlowNodeEnum.Process.getCode().equals(t.getState()))
                .toList();
        // 当前节点
        Optional<FlowTaskNodeEntity> first = taskNodeList.stream()
                .filter(m -> m.getId().equals(operator.getTaskNodeId()))
                .findFirst();
        if (first.isEmpty()) {
            throw new WorkFlowException(MsgCode.COD001.get());
        }
        FlowTaskNodeEntity taskNode = first.get();
        FlowEngineEntity engine = flowEngineService.getInfo(flowTask.getFlowId());
        // 当前节点属性
        ChildNodeList nodeModel = JsonUtils.toObject(taskNode.getNodePropertyJson(), ChildNodeList.class);
        // 驳回记录
        FlowTaskOperatorRecordEntity operatorRecord = new FlowTaskOperatorRecordEntity();
        // 审批数据赋值
        FlowOperatordModel flowOperatordModel = new FlowOperatordModel();
        flowOperatordModel.setStatus(FlowRecordEnum.reject.getCode());
        flowOperatordModel.setFlowModel(flowModel);
        flowOperatordModel.setUserId(userId);
        flowOperatordModel.setOperator(operator);
        this.operatorRecord(operatorRecord, flowOperatordModel);
        flowTaskOperatorRecordService.create(operatorRecord);
        // 修改或签、会签经办数据
        TaskHandleIdStatus handleIdStatus = new TaskHandleIdStatus();
        handleIdStatus.setStatus(0);
        handleIdStatus.setNodeModel(nodeModel);
        handleIdStatus.setUserInfo(userInfo);
        handleIdStatus.setTaskNodeList(taskNodeList);
        this.handleIdStatus(operator, handleIdStatus);
        // 更新流当前程经办状态
        flowTaskOperatorService.update(operator);
        List<FlowTaskEntity> childList =
                flowTaskService.getChildList(operatorRecord.getTaskId(), FlowTaskEntity::getId);
        boolean isNext = childList.size() > 0;
        if (isNext) {
            throw new WorkFlowException(MsgCode.WF110.get());
        }
        boolean isReject = this.isReject(taskNode);
        // 更新驳回节点
        List<ChildNodeList> nextOperatorList = new ArrayList<>();
        Set<FlowTaskNodeEntity> thisStepAll = new HashSet<>();
        List<String> rejectList = new ArrayList<>();
        String[] thisStepId = flowTask.getThisStepId().split(",");
        List<FlowTaskNodeEntity> upAll =
                this.isUpAll(taskNodeList, taskNode, isReject, thisStepAll, rejectList, thisStepId);
        for (FlowTaskNodeEntity entity : upAll) {
            ChildNodeList node = JsonUtils.toObject(entity.getNodePropertyJson(), ChildNodeList.class);
            nextOperatorList.add(node);
        }
        // 驳回节点
        List<FlowTaskOperatorEntity> operatorList = new ArrayList<>();
        // 如果开始节点就不需要找下一节点
        boolean isStart = nextOperatorList.stream()
                .anyMatch(t -> FlowNature.NodeStart.equals(t.getCustom().getType()));
        if (!isStart) {
            // 赋值数据
            flowModel.setProcessId(flowTask.getId());
            flowModel.setId(flowTask.getId());
            Map<String, Object> data = JsonUtils.toMap(flowTask.getFlowFormContentJson());
            flowModel.setFormData(data);
            this.nextOperator(operatorList, nextOperatorList, flowTask, flowModel);
            // 驳回节点之后的状态修改
            flowTaskNodeService.updateCompletion(rejectList, 0);
            Set<String> uptList = upAll.stream().map(FlowTaskNodeEntity::getId).collect(Collectors.toSet());
            flowTaskOperatorRecordService.updateStatus(uptList, flowTask.getId());
        } else {
            flowTaskNodeService.update(flowTask.getId());
            flowTaskOperatorService.update(flowTask.getId());
            flowTaskOperatorRecordService.update(flowTask.getId());
        }
        // 更新驳回当前节点
        List<String> stepIdList = new ArrayList<>();
        List<String> stepNameList = new ArrayList<>();
        List<String> progressList = new ArrayList<>();
        for (FlowTaskNodeEntity taskNodes : thisStepAll) {
            ChildNodeList childNode = JsonUtils.toObject(taskNodes.getNodePropertyJson(), ChildNodeList.class);
            Properties properties = childNode.getProperties();
            String progress = properties.getProgress();
            if (StrUtil.isNotEmpty(progress)) {
                progressList.add(progress);
            }
            stepIdList.add(taskNodes.getNodeCode());
            stepNameList.add(taskNodes.getNodeName());
        }
        // 驳回比例不够，不修改当前节点
        if (thisStepAll.size() > 0) {
            Collections.sort(progressList);
            flowTask.setCompletion(progressList.size() > 0 ? Integer.parseInt(progressList.get(0)) : 0);
            flowTask.setThisStepId(String.join(",", stepIdList));
            flowTask.setThisStep(String.join(",", stepNameList));
            // 判断驳回节点是否是开发节点
            flowTask.setStatus(isStart ? FlowTaskStatusEnum.Reject.getCode() : flowTask.getStatus());
            // 会签拒绝更新未审批用户
            Set<String> rejectNodeList = new HashSet<>();
            this.upAll(rejectNodeList, rejectList, taskNodeList);
            flowTaskOperatorService.updateReject(flowTask.getId(), rejectNodeList);
            if (isStart) {
                flowTask.setCompletion(0);
                flowTask.setThisStepId(String.join(",", new ArrayList<>()));
                flowTask.setThisStep(String.join(",", new ArrayList<>()));
            }
            // 删除节点候选人
            List<String> candidates = new ArrayList<>(rejectNodeList);
            flowCandidatesService.deleteTaskNodeId(candidates);
        }
        // 更新流程节点
        flowTaskService.update(flowTask);
        // 显示当前的驳回记录
        flowTaskOperatorRecordService.update(operatorRecord.getId(), operatorRecord);
        // 创建审批人
        flowTaskOperatorService.create(operatorList);
        // 获取抄送人
        List<FlowTaskCirculateEntity> circulateList = new ArrayList<>();
        this.circulateList(nodeModel, circulateList, flowModel);
        flowTaskCirculateService.create(circulateList);
        // 节点事件
        flowMsgUtil.event(5, nodeModel, operatorRecord, flowModel);
        // 发送消息
        FlowMsgModel flowMsgModel = new FlowMsgModel();
        flowMsgModel.setCirculateList(circulateList);
        flowMsgModel.setNodeList(taskNodeList);
        for (FlowTaskOperatorEntity operatorEntity : operatorList) {
            operatorEntity.setTaskNodeId(taskNode.getId());
        }
        flowMsgModel.setOperatorList(operatorList);
        flowMsgModel.setReject(true);
        flowMsgModel.setCopy(true);
        flowMsgModel.setStart(isStart);
        flowMsgModel.setData(flowModel.getFormData());
        flowMsgModel.setTaskNodeEntity(taskNode);
        flowMsgModel.setTaskEntity(flowTask);
        flowMsgModel.setEngine(engine);
        flowMsgUtil.message(flowMsgModel);
    }

    /**
     * 驳回获取节点下所有节点
     *
     * @param rejectNodeList
     * @param rejectList
     * @param taskNodeList
     */
    private void upAll(Set<String> rejectNodeList, List<String> rejectList, List<FlowTaskNodeEntity> taskNodeList) {
        List<FlowTaskNodeEntity> nodeList = taskNodeList.stream()
                .filter(t -> rejectList.contains(t.getId()))
                .toList();
        for (FlowTaskNodeEntity taskNode : nodeList) {
            List<String> list = StrUtil.isNotEmpty(taskNode.getNodeNext())
                    ? Arrays.asList(taskNode.getNodeNext().split(","))
                    : new ArrayList<>();
            List<FlowTaskNodeEntity> taskList = taskNodeList.stream()
                    .filter(t -> list.contains(t.getNodeCode()))
                    .toList();
            List<String> rejectListAll =
                    taskList.stream().map(FlowTaskNodeEntity::getId).toList();
            rejectNodeList.add(taskNode.getId());
            upAll(rejectNodeList, rejectListAll, taskNodeList);
        }
    }

    @Override
    @DSTransactional
    public void recall(String id, FlowTaskOperatorRecordEntity operatorRecord, FlowModel flowModel)
            throws WorkFlowException {
        UserInfo userInfo = userProvider.get();
        // 撤回经办
        FlowTaskOperatorEntity operatorEntity = flowTaskOperatorService.getInfo(operatorRecord.getTaskOperatorId());
        // 撤回节点
        FlowTaskNodeEntity flowTaskNodeEntity = flowTaskNodeService.getInfo(operatorRecord.getTaskNodeId());
        // 撤回任务
        FlowTaskEntity flowTaskEntity = flowTaskService.getInfo(operatorRecord.getTaskId());
        FlowEngineEntity engine = flowEngineService.getInfo(flowTaskEntity.getFlowId());
        // 所有节点
        List<FlowTaskNodeEntity> flowTaskNodeEntityList =
                flowTaskNodeService.getList(operatorRecord.getTaskId()).stream()
                        .filter(t -> FlowNodeEnum.Process.getCode().equals(t.getState()))
                        .toList();
        // 所有经办
        List<FlowTaskOperatorEntity> flowTaskOperatorEntityList =
                flowTaskOperatorService.getList(operatorRecord.getTaskId()).stream()
                        .filter(t -> FlowNodeEnum.Process.getCode().equals(t.getState()))
                        .toList();
        // 撤回节点属性
        ChildNodeList nodeModel = JsonUtils.toObject(flowTaskNodeEntity.getNodePropertyJson(), ChildNodeList.class);
        // 拒绝不撤回
        if (FlowNature.ProcessCompletion.equals(operatorEntity.getHandleStatus())) {
            throw new WorkFlowException(MsgCode.WF104.get());
        }
        // 任务待审状态才能撤回
        if (!(flowTaskEntity.getEnabledMark() == 1
                && FlowTaskStatusEnum.Handle.getCode().equals(flowTaskEntity.getStatus()))) {
            throw new WorkFlowException(MsgCode.WF105.get());
        }
        // 撤回节点下一节点已操作
        List<FlowTaskOperatorEntity> recallNextOperatorList = flowTaskOperatorEntityList.stream()
                .filter(x -> flowTaskNodeEntity.getNodeNext().contains(x.getNodeCode()))
                .toList();
        boolean isRecall = recallNextOperatorList.stream()
                .anyMatch(t -> FlowNature.AuditCompletion.equals(t.getCompletion())
                        && FlowNodeEnum.Process.getCode().equals(t.getState()));
        if (isRecall) {
            throw new WorkFlowException(MsgCode.WF106.get());
        }
        List<FlowTaskEntity> childList =
                flowTaskService.getChildList(operatorRecord.getTaskId(), FlowTaskEntity::getId);
        boolean isNext = childList.size() > 0;
        if (isNext) {
            throw new WorkFlowException(MsgCode.WF107.get());
        }
        // 加签人
        Set<FlowTaskOperatorEntity> operatorList = new HashSet<>();
        this.getOperator(operatorEntity.getId(), operatorList);
        operatorEntity.setHandleStatus(null);
        operatorEntity.setHandleTime(null);
        operatorEntity.setCompletion(FlowNature.ProcessCompletion);
        operatorEntity.setState(FlowNodeEnum.Process.getCode());
        operatorList.add(operatorEntity);
        List<String> delOperatorRecordIds = new ArrayList<>();
        for (FlowTaskOperatorEntity item : operatorList) {
            FlowTaskOperatorRecordEntity record =
                    flowTaskOperatorRecordService.getInfo(item.getTaskId(), item.getTaskNodeId(), item.getId());
            if (record != null) {
                delOperatorRecordIds.add(record.getId());
            }
        }
        // 撤回节点是否完成
        if (FlowNature.AuditCompletion.equals(flowTaskNodeEntity.getCompletion())) {
            // 撤回节点下一节点经办删除
            List<String> idAll = recallNextOperatorList.stream()
                    .map(FlowTaskOperatorEntity::getId)
                    .toList();
            flowTaskOperatorService.updateTaskOperatorState(idAll);
            List<FlowTaskOperatorEntity> hanleOperatorList = flowTaskOperatorEntityList.stream()
                    .filter(x -> x.getTaskNodeId().equals(operatorRecord.getTaskNodeId())
                            && Objects.isNull(x.getHandleStatus())
                            && Objects.isNull(x.getHandleTime())
                            && Objects.isNull(x.getParentId()))
                    .toList();
            for (FlowTaskOperatorEntity taskOperator : hanleOperatorList) {
                taskOperator.setCompletion(FlowNature.ProcessCompletion);
            }
            operatorList.addAll(hanleOperatorList);
            // 更新任务流程
            List<String> stepIdList = new ArrayList<>();
            List<String> stepNameList = new ArrayList<>();
            List<String> progressList = new ArrayList<>();
            List<FlowTaskNodeEntity> recallNodeList = flowTaskNodeEntityList.stream()
                    .filter(x -> flowTaskNodeEntity.getSortCode().equals(x.getSortCode()))
                    .toList();
            for (FlowTaskNodeEntity taskNodeEntity : recallNodeList) {
                ChildNodeList childNode = JsonUtils.toObject(taskNodeEntity.getNodePropertyJson(), ChildNodeList.class);
                Properties properties = childNode.getProperties();
                String progress = properties.getProgress();
                if (StrUtil.isNotEmpty(progress)) {
                    progressList.add(progress);
                }
                stepIdList.add(taskNodeEntity.getNodeCode());
                stepNameList.add(taskNodeEntity.getNodeName());
                taskNodeEntity.setCompletion(FlowNature.ProcessCompletion);
                if (operatorRecord.getTaskNodeId().equals(taskNodeEntity.getId())) {
                    flowTaskNodeService.update(taskNodeEntity);
                }
            }
            // 更新当前节点
            flowTaskEntity.setCompletion(progressList.size() > 0 ? Integer.parseInt(progressList.get(0)) : 0);
            flowTaskEntity.setThisStepId(String.join(",", stepIdList));
            flowTaskEntity.setThisStep(String.join(",", stepNameList));
            flowTaskEntity.setStatus(FlowTaskStatusEnum.Handle.getCode());
            flowTaskService.update(flowTaskEntity);
        }
        for (FlowTaskOperatorEntity taskOperator : operatorList) {
            flowTaskOperatorService.update(taskOperator);
        }
        // 撤回删除候选人
        List<String> nextNodeList = flowTaskNodeEntityList.stream()
                .filter(t -> t.getSortCode().equals(flowTaskNodeEntity.getSortCode() + 1))
                .map(FlowTaskNodeEntity::getId)
                .toList();
        String handId = userInfo.getUserId();
        flowCandidatesService.delete(nextNodeList, handId, operatorRecord.getTaskOperatorId());
        // 删除经办记录
        delOperatorRecordIds.add(operatorRecord.getId());
        flowTaskOperatorRecordService.updateStatus(delOperatorRecordIds);
        // 撤回记录
        FlowTaskOperatorEntity operator = JsonUtils.toObject(operatorRecord, FlowTaskOperatorEntity.class);
        operator.setId(operatorRecord.getTaskOperatorId());
        // 审批数据赋值
        FlowOperatordModel flowOperatordModel = new FlowOperatordModel();
        flowOperatordModel.setStatus(FlowRecordEnum.revoke.getCode());
        flowOperatordModel.setFlowModel(flowModel);
        flowOperatordModel.setUserId(userInfo.getUserId());
        flowOperatordModel.setOperator(operator);
        this.operatorRecord(operatorRecord, flowOperatordModel);
        flowTaskOperatorRecordService.create(operatorRecord);
        flowModel.setFormData(JsonUtils.toMap(flowTaskEntity.getFlowFormContentJson()));
        // 节点事件
        flowMsgUtil.event(6, nodeModel, operatorRecord, flowModel);
    }

    @Override
    @DSTransactional
    public void revoke(FlowTaskEntity flowTask, FlowModel flowModel) {
        UserInfo userInfo = userProvider.get();
        List<FlowTaskNodeEntity> list = flowTaskNodeService.getList(flowTask.getId());
        FlowTaskNodeEntity start = list.stream()
                .filter(t -> FlowNature.NodeStart.equals(String.valueOf(t.getNodeType())))
                .findFirst()
                .orElse(null);
        // 删除节点
        flowTaskNodeService.deleteByTaskId(flowTask.getId());
        // 删除经办
        flowTaskOperatorService.deleteByTaskId(flowTask.getId());
        // 删除候选人
        flowCandidatesService.deleteByTaskId(flowTask.getId());
        // 修改经办记录状态
        List<FlowTaskOperatorRecordEntity> recordList = flowTaskOperatorRecordService.getList(flowTask.getId());
        List<String> recordListAll =
                recordList.stream().map(FlowTaskOperatorRecordEntity::getId).toList();
        flowTaskOperatorRecordService.updateStatus(recordListAll);
        // 更新当前节点
        flowTask.setThisStepId(start.getNodeCode());
        flowTask.setThisStep(start.getNodeName());
        flowTask.setCompletion(FlowNature.ProcessCompletion);
        flowTask.setStatus(FlowTaskStatusEnum.Revoke.getCode());
        flowTask.setStartTime(null);
        flowTask.setEndTime(null);
        flowTask.setThisStepId(String.join(",", new ArrayList<>()));
        flowTask.setThisStep(String.join(",", new ArrayList<>()));
        flowTaskService.update(flowTask);
        // 撤回记录
        FlowTaskOperatorRecordEntity operatorRecord = new FlowTaskOperatorRecordEntity();
        operatorRecord.setTaskId(flowTask.getId());
        operatorRecord.setHandleStatus(FlowRecordEnum.revoke.getCode());
        FlowTaskOperatorEntity operator = JsonUtils.toObject(operatorRecord, FlowTaskOperatorEntity.class);
        // 审批数据赋值
        FlowOperatordModel flowOperatordModel = new FlowOperatordModel();
        flowOperatordModel.setStatus(FlowRecordEnum.revoke.getCode());
        flowOperatordModel.setFlowModel(flowModel);
        flowOperatordModel.setUserId(userInfo.getUserId());
        flowOperatordModel.setOperator(operator);
        this.operatorRecord(operatorRecord, flowOperatordModel);
        flowTaskOperatorRecordService.create(operatorRecord);
        // 撤回事件
        ChildNodeList nodeModel = JsonUtils.toObject(start.getNodePropertyJson(), ChildNodeList.class);
        flowModel.setFormData(JsonUtils.toMap(flowTask.getFlowFormContentJson()));
        operatorRecord.setHandleStatus(FlowTaskStatusEnum.Revoke.getCode());
        flowMsgUtil.event(3, nodeModel, operatorRecord, flowModel);
        // 递归删除子流程任务
        this.delChild(flowTask);
    }

    @Override
    @DSTransactional
    public void cancel(FlowTaskEntity flowTask, FlowModel flowModel) {
        UserInfo userInfo = userProvider.get();
        // 终止记录
        FlowTaskOperatorRecordEntity operatorRecord = new FlowTaskOperatorRecordEntity();
        FlowTaskOperatorEntity operator = new FlowTaskOperatorEntity();
        operator.setTaskId(flowTask.getId());
        operator.setNodeCode(flowTask.getThisStepId());
        operator.setNodeName(flowTask.getThisStep());
        // 审批数据赋值
        FlowOperatordModel flowOperatordModel = new FlowOperatordModel();
        flowOperatordModel.setStatus(FlowRecordEnum.cancel.getCode());
        flowOperatordModel.setFlowModel(flowModel);
        flowOperatordModel.setUserId(userInfo.getUserId());
        flowOperatordModel.setOperator(operator);
        this.operatorRecord(operatorRecord, flowOperatordModel);
        flowTaskOperatorRecordService.create(operatorRecord);
        // 更新实例
        flowTask.setStatus(FlowTaskStatusEnum.Cancel.getCode());
        flowTask.setEndTime(new Date());
        flowTaskService.update(flowTask);
    }

    @Override
    @DSTransactional
    public boolean assign(String id, FlowModel flowModel) throws WorkFlowException {
        List<FlowTaskOperatorEntity> list = flowTaskOperatorService.getList(id).stream()
                .filter(t -> FlowNodeEnum.Process.getCode().equals(t.getState())
                        && flowModel.getNodeCode().equals(t.getNodeCode())
                        && FlowNature.ParentId.equals(t.getParentId()))
                .toList();
        boolean isOk = list.size() > 0;
        if (list.size() > 0) {
            FlowTaskOperatorEntity entity = list.get(0);
            entity.setHandleStatus(null);
            entity.setHandleTime(null);
            entity.setCompletion(FlowNature.ProcessCompletion);
            entity.setCreatorTime(new Date());
            entity.setDraftData(null);
            entity.setHandleId(flowModel.getFreeApproverUserId());
            List<String> idAll =
                    list.stream().map(FlowTaskOperatorEntity::getId).toList();
            flowTaskOperatorService.deleteList(idAll);
            List<FlowTaskOperatorEntity> operatorList = new ArrayList<>();
            operatorList.add(entity);
            flowTaskOperatorService.create(operatorList);
            // 指派记录
            UserInfo userInfo = userProvider.get();
            FlowTaskOperatorRecordEntity operatorRecord = new FlowTaskOperatorRecordEntity();
            FlowTaskOperatorEntity operator = new FlowTaskOperatorEntity();
            operator.setTaskId(entity.getTaskId());
            operator.setNodeCode(entity.getNodeCode());
            operator.setNodeName(entity.getNodeName());
            // 审批数据赋值
            FlowOperatordModel flowOperatordModel = new FlowOperatordModel();
            flowOperatordModel.setStatus(FlowRecordEnum.assign.getCode());
            flowOperatordModel.setFlowModel(flowModel);
            flowOperatordModel.setUserId(userInfo.getUserId());
            flowOperatordModel.setOperator(operator);
            flowOperatordModel.setOperatorId(entity.getHandleId());
            this.operatorRecord(operatorRecord, flowOperatordModel);
            flowTaskOperatorRecordService.create(operatorRecord);
            // 发送消息
            List<FlowTaskNodeEntity> taskNodeList = flowTaskNodeService.getList(entity.getTaskId());
            FlowTaskNodeEntity taskNode = taskNodeList.stream()
                    .filter(t -> t.getId().equals(entity.getTaskNodeId()))
                    .findFirst()
                    .orElse(null);
            FlowTaskEntity flowTask = flowTaskService.getInfoSubmit(entity.getTaskId());
            FlowEngineEntity engine = flowEngineService.getInfo(flowTask.getFlowId());
            FlowMsgModel flowMsgModel = new FlowMsgModel();
            flowMsgModel.setCirculateList(new ArrayList<>());
            flowMsgModel.setTitle("已被【指派】");
            flowMsgModel.setData(JsonUtils.toMap(flowTask.getFlowFormContentJson()));
            flowMsgModel.setNodeList(taskNodeList);
            flowMsgModel.setOperatorList(operatorList);
            flowMsgModel.setTaskNodeEntity(taskNode);
            flowMsgModel.setTaskEntity(flowTask);
            flowMsgModel.setEngine(engine);
            flowMsgUtil.message(flowMsgModel);
        }
        return isOk;
    }

    @Override
    @DSTransactional
    public void transfer(FlowTaskOperatorEntity taskOperator) throws WorkFlowException {
        flowTaskOperatorService.update(taskOperator);
        // 转办记录
        UserInfo userInfo = userProvider.get();
        FlowModel flowModel = new FlowModel();
        FlowTaskOperatorRecordEntity operatorRecord = new FlowTaskOperatorRecordEntity();
        FlowTaskOperatorEntity operator = new FlowTaskOperatorEntity();
        operator.setTaskId(taskOperator.getTaskId());
        operator.setNodeCode(taskOperator.getNodeCode());
        operator.setTaskNodeId(taskOperator.getTaskNodeId());
        operator.setNodeName(taskOperator.getNodeName());
        // 审批数据赋值
        FlowOperatordModel flowOperatordModel = new FlowOperatordModel();
        flowOperatordModel.setStatus(FlowRecordEnum.transfer.getCode());
        flowOperatordModel.setFlowModel(flowModel);
        flowOperatordModel.setUserId(userInfo.getUserId());
        flowOperatordModel.setOperator(operator);
        flowOperatordModel.setOperatorId(taskOperator.getHandleId());
        this.operatorRecord(operatorRecord, flowOperatordModel);
        flowTaskOperatorRecordService.create(operatorRecord);
        // 发送消息
        List<FlowTaskNodeEntity> taskNodeList = flowTaskNodeService.getList(taskOperator.getTaskId());
        FlowTaskNodeEntity taskNode = taskNodeList.stream()
                .filter(t -> t.getId().equals(taskOperator.getTaskNodeId()))
                .findFirst()
                .orElse(null);
        FlowTaskEntity flowTask = flowTaskService.getInfoSubmit(
                taskNode.getTaskId(),
                FlowTaskEntity::getId,
                FlowTaskEntity::getFullName,
                FlowTaskEntity::getCreatorUserId,
                FlowTaskEntity::getStatus,
                FlowTaskEntity::getFlowFormContentJson);
        FlowEngineEntity engine = flowEngineService.getInfo(flowTask.getFlowId());
        List<FlowTaskOperatorEntity> operatorList = new ArrayList() {
            {
                FlowTaskOperatorEntity operatorEntity = new FlowTaskOperatorEntity();
                operatorEntity.setId(taskOperator.getId());
                operatorEntity.setTaskId(operatorRecord.getTaskId());
                operatorEntity.setHandleId(taskOperator.getHandleId());
                operatorEntity.setTaskNodeId(operatorRecord.getTaskNodeId());
                add(operatorEntity);
            }
        };
        FlowMsgModel flowMsgModel = new FlowMsgModel();
        flowMsgModel.setCirculateList(new ArrayList<>());
        flowMsgModel.setTitle("已被【转办】");
        flowMsgModel.setNodeList(taskNodeList);
        flowMsgModel.setOperatorList(operatorList);
        flowMsgModel.setData(JsonUtils.toMap(flowTask.getFlowFormContentJson()));
        flowMsgModel.setTaskNodeEntity(taskNode);
        flowMsgModel.setTaskEntity(flowTask);
        flowMsgModel.setEngine(engine);
        flowMsgUtil.message(flowMsgModel);
    }

    @Override
    public FlowBeforeInfoVO getBeforeInfo(String id, String taskNodeId, String taskOperatorId)
            throws WorkFlowException {
        FlowBeforeInfoVO vo = new FlowBeforeInfoVO();
        FlowTaskEntity taskEntity = flowTaskService.getInfo(id);
        List<FlowTaskNodeEntity> taskNodeAllList = flowTaskNodeService.getList(taskEntity.getId()).stream()
                .filter(t -> FlowNodeEnum.Process.getCode().equals(t.getState()))
                .toList();
        List<FlowTaskNodeEntity> taskNodeList = taskNodeAllList.stream()
                .sorted(Comparator.comparing(FlowTaskNodeEntity::getSortCode))
                .toList();
        List<FlowTaskOperatorEntity> taskOperatorList = flowTaskOperatorService.getList(taskEntity.getId()).stream()
                .filter(t -> FlowNodeEnum.Process.getCode().equals(t.getState()))
                .toList();
        List<FlowTaskOperatorRecordEntity> operatorRecordList =
                flowTaskOperatorRecordService.getList(taskEntity.getId());
        boolean colorFlag = true;
        // 已办人员
        List<FlowTaskOperatorRecordModel> recordList = new ArrayList<>();
        List<String> userIdAll = operatorRecordList.stream()
                .map(FlowTaskOperatorRecordEntity::getHandleId)
                .toList();
        userIdAll.addAll(operatorRecordList.stream()
                .map(FlowTaskOperatorRecordEntity::getOperatorId)
                .toList());
        List<UserEntity> userList = serviceUtil.getUserName(userIdAll);
        for (FlowTaskOperatorRecordEntity entity : operatorRecordList) {
            UserEntity userName = userList.stream()
                    .filter(t -> t.getId().equals(entity.getHandleId()))
                    .findFirst()
                    .orElse(null);
            FlowTaskOperatorRecordModel infoModel = JsonUtils.toObject(entity, FlowTaskOperatorRecordModel.class);
            infoModel.setUserName(userName != null ? userName.getRealName() + "/" + userName.getAccount() : "");
            UserEntity operatorName = userList.stream()
                    .filter(t -> t.getId().equals(entity.getOperatorId()))
                    .findFirst()
                    .orElse(null);
            infoModel.setOperatorId(
                    operatorName != null ? operatorName.getRealName() + "/" + operatorName.getAccount() : "");
            recordList.add(infoModel);
        }
        vo.setFlowTaskOperatorRecordList(recordList);
        // 流程节点
        String[] tepId =
                taskEntity.getThisStepId() != null ? taskEntity.getThisStepId().split(",") : new String[] {};
        List<String> tepIdAll = Arrays.asList(tepId);
        List<FlowTaskNodeModel> flowTaskNodeListAll = JsonUtils.toList(taskNodeList, FlowTaskNodeModel.class);
        for (FlowTaskNodeModel model : flowTaskNodeListAll) {
            // 流程图节点颜色
            if (colorFlag || model.getCompletion() == 1) {
                if (model.getSortCode() != -2) {
                    model.setType("0");
                }
            }
            if (tepIdAll.contains(model.getNodeCode())) {
                model.setType("1");
                colorFlag = false;
                if (FlowNature.NodeEnd.equals(model.getNodeCode())) {
                    model.setType("0");
                }
            }
            // 查询审批人
            ChildNodeList childNode = JsonUtils.toObject(model.getNodePropertyJson(), ChildNodeList.class);
            Custom custom = childNode.getCustom();
            Properties properties = childNode.getProperties();
            String type = properties.getAssigneeType();
            List<FlowTaskOperatorEntity> operatorList = new ArrayList<>();
            FlowModel flowModel = new FlowModel();
            TaskOperator taskOperator = new TaskOperator();
            taskOperator.setChildNode(childNode);
            taskOperator.setTaskEntity(taskEntity);
            taskOperator.setFlowModel(flowModel);
            taskOperator.setDetails(false);
            taskOperator.setId(FlowNature.ParentId);
            this.operator(operatorList, taskOperator);
            List<String> userName = new ArrayList<>();
            if (FlowNature.NodeStart.equals(custom.getType())) {
                UserEntity startUser = serviceUtil.getUserInfo(taskEntity.getCreatorUserId());
                userName.add(startUser != null ? startUser.getRealName() + "/" + startUser.getAccount() : "");
            } else if (FlowNature.NodeSubFlow.equals(custom.getType())) {
                List<UserEntity> list = this.childSaveList(childNode, taskEntity);
                List<String> nameList = new ArrayList<>();
                for (UserEntity entity : list) {
                    nameList.add(entity.getRealName() + "/" + entity.getAccount());
                }
                userName.addAll(nameList);
            } else if (FlowTaskOperatorEnum.FreeApprover.getCode().equals(type)) {
                List<String> operatorUserList = taskOperatorList.stream()
                        .filter(t -> t.getNodeCode().equals(custom.getNodeId())
                                && FlowNature.ParentId.equals(t.getParentId()))
                        .map(FlowTaskOperatorEntity::getHandleId)
                        .toList();
                List<UserEntity> userListAll = serviceUtil.getUserName(operatorUserList);
                List<String> nameList = new ArrayList<>();
                for (UserEntity operator : userListAll) {
                    nameList.add(operator.getRealName() + "/" + operator.getAccount());
                }
                userName.addAll(nameList);
            } else if (!FlowNature.NodeEnd.equals(custom.getNodeId())) {
                boolean isShow = true;
                // 环节还没有经过和当前不显示审批人
                if (FlowTaskOperatorEnum.Tache.getCode().equals(type)) {
                    boolean completion = ("0".equals(model.getType()) || "1".equals(model.getType()));
                    if (!completion) {
                        isShow = false;
                    }
                }
                if (isShow) {
                    List<String> nameList = new ArrayList<>();
                    List<String> operatorUserList = operatorList.stream()
                            .map(FlowTaskOperatorEntity::getHandleId)
                            .toList();
                    List<UserEntity> userListAll = serviceUtil.getUserName(operatorUserList);
                    for (UserEntity operator : userListAll) {
                        nameList.add(operator.getRealName() + "/" + operator.getAccount());
                    }
                    userName.addAll(nameList);
                }
            }
            model.setUserName(String.join(",", userName));
        }
        vo.setFlowTaskNodeList(flowTaskNodeListAll);
        // 表单权限
        Properties approversProperties = new Properties();
        if (StrUtil.isNotEmpty(taskNodeId)) {
            FlowTaskNodeEntity taskNode = flowTaskNodeService.getInfo(taskNodeId);
            vo.setFormOperates(new ArrayList<>());
            if (taskNode != null) {
                ChildNodeList childNode = JsonUtils.toObject(taskNode.getNodePropertyJson(), ChildNodeList.class);
                approversProperties = childNode.getProperties();
                vo.setFormOperates(childNode.getProperties().getFormOperates());
            }
        }
        FlowJsonUtil.assignment(approversProperties);
        vo.setApproversProperties(approversProperties);
        // 流程任务
        FlowTaskModel inof = JsonUtils.toObject(taskEntity, FlowTaskModel.class);
        FlowEngineEntity engine = flowEngineService.getInfo(taskEntity.getFlowId());
        inof.setAppFormUrl(engine.getAppFormUrl());
        inof.setFormUrl(engine.getFormUrl());
        inof.setType(engine.getType());
        vo.setFlowTaskInfo(inof);
        // 流程经办
        vo.setFlowTaskOperatorList(JsonUtils.toList(taskOperatorList, FlowTaskOperatorModel.class));
        // 流程引擎
        vo.setFlowFormInfo(taskEntity.getFlowForm());
        // 草稿数据
        if (StrUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StrUtil.isNotEmpty(operator.getDraftData())) {
                    vo.setDraftData(JsonUtils.toMap(operator.getDraftData()));
                }
            }
        }
        return vo;
    }

    @Override
    public List<FlowSummary> recordList(String id, String category, String type) {
        // 审批汇总
        List<Integer> handleStatus = new ArrayList<>();
        if (!"0".equals(type)) {
            handleStatus.add(0);
            handleStatus.add(1);
        }
        List<FlowTaskOperatorRecordEntity> recordListAll =
                flowTaskOperatorRecordService.getRecordList(id, handleStatus);
        List<String> userIdAll = new ArrayList<>();
        List<String> userIdList = recordListAll.stream()
                .map(FlowTaskOperatorRecordEntity::getHandleId)
                .toList();
        List<String> operatorId = recordListAll.stream()
                .map(FlowTaskOperatorRecordEntity::getOperatorId)
                .filter(StrUtil::isNotEmpty)
                .toList();
        userIdAll.addAll(userIdList);
        userIdAll.addAll(operatorId);
        List<UserEntity> userList = serviceUtil.getUserName(userIdAll);
        List<FlowSummary> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        Map<String, List<FlowTaskOperatorRecordEntity>> operatorAll = new HashMap<>();
        if (FlowRecordListEnum.position.getCode().equals(category)) {
            List<String> userId = userList.stream().map(UserEntity::getId).toList();
            List<UserRelationEntity> relationList = serviceUtil.getListByUserIdAll(userId);
            List<String> objectId =
                    relationList.stream().map(UserRelationEntity::getObjectId).toList();
            List<PositionEntity> positionListAll = serviceUtil.getPositionName(objectId);
            for (PositionEntity entity : positionListAll) {
                map.put(entity.getId(), entity.getFullName());
                List<String> userAll = relationList.stream()
                        .filter(t -> t.getObjectId().equals(entity.getId()))
                        .map(UserRelationEntity::getUserId)
                        .toList();
                List<FlowTaskOperatorRecordEntity> operator = new LinkedList<>();
                for (FlowTaskOperatorRecordEntity recordEntity : recordListAll) {
                    if (userAll.contains(recordEntity.getHandleId())) {
                        operator.add(recordEntity);
                    }
                }
                operatorAll.put(entity.getId(), operator);
            }
        } else if (FlowRecordListEnum.role.getCode().equals(category)) {
            List<String> userId = userList.stream().map(UserEntity::getId).toList();
            List<UserRelationEntity> relationList = serviceUtil.getListByUserIdAll(userId);
            List<String> objectId =
                    relationList.stream().map(UserRelationEntity::getObjectId).toList();
            List<RoleEntity> roleListAll = serviceUtil.getListByIds(objectId);
            for (RoleEntity entity : roleListAll) {
                map.put(entity.getId(), entity.getFullName());
                List<String> userAll = relationList.stream()
                        .filter(t -> t.getObjectId().equals(entity.getId()))
                        .map(UserRelationEntity::getUserId)
                        .toList();
                List<FlowTaskOperatorRecordEntity> operator = new LinkedList<>();
                for (FlowTaskOperatorRecordEntity recordEntity : recordListAll) {
                    if (userAll.contains(recordEntity.getHandleId())) {
                        operator.add(recordEntity);
                    }
                }
                operatorAll.put(entity.getId(), operator);
            }
        } else if (FlowRecordListEnum.department.getCode().equals(category)) {
            List<String> organizeList =
                    userList.stream().map(UserEntity::getOrganizeId).toList();
            List<OrganizeEntity> organizeListAll = serviceUtil.getOrganizeName(organizeList);
            for (OrganizeEntity entity : organizeListAll) {
                map.put(entity.getId(), entity.getFullName());
                List<String> userAll = userList.stream()
                        .filter(t -> t.getOrganizeId().equals(entity.getId()))
                        .map(UserEntity::getId)
                        .toList();
                List<FlowTaskOperatorRecordEntity> operator = new LinkedList<>();
                for (FlowTaskOperatorRecordEntity recordEntity : recordListAll) {
                    if (userAll.contains(recordEntity.getHandleId())) {
                        operator.add(recordEntity);
                    }
                }
                operatorAll.put(entity.getId(), operator);
            }
        }
        for (String key : map.keySet()) {
            String fullName = map.get(key);
            FlowSummary summary = new FlowSummary();
            summary.setId(key);
            summary.setFullName(fullName);
            List<FlowTaskOperatorRecordEntity> recordList = operatorAll.get(key);
            List<FlowSummary> childList = new ArrayList<>();
            for (FlowTaskOperatorRecordEntity entity : recordList) {
                FlowSummary childSummary = JsonUtils.toObject(entity, FlowSummary.class);
                UserEntity user = userList.stream()
                        .filter(t -> t.getId().equals(entity.getHandleId()))
                        .findFirst()
                        .orElse(null);
                childSummary.setUserName(user != null ? user.getRealName() + "/" + user.getAccount() : "");
                UserEntity userEntity = userList.stream()
                        .filter(t -> t.getId().equals(entity.getOperatorId()))
                        .findFirst()
                        .orElse(null);
                childSummary.setOperatorId(
                        userEntity != null ? userEntity.getRealName() + "/" + userEntity.getAccount() : "");
                childList.add(childSummary);
            }
            summary.setList(childList);
            list.add(summary);
        }
        return list;
    }

    @Override
    public boolean press(String id) throws WorkFlowException {
        FlowTaskEntity flowTaskEntity = flowTaskService.getInfo(id);
        FlowEngineEntity engine = flowEngineService.getInfo(flowTaskEntity.getFlowId());
        List<FlowTaskOperatorEntity> operatorList = flowTaskOperatorService.press(id);
        boolean flag = operatorList.size() > 0;
        Map<String, Object> data = JsonUtils.toMap(flowTaskEntity.getFlowFormContentJson());
        List<FlowTaskNodeEntity> taskNodeList = flowTaskNodeService.getList(id);
        // 发送消息
        FlowMsgModel flowMsgModel = new FlowMsgModel();
        flowMsgModel.setCirculateList(new ArrayList<>());
        flowMsgModel.setNodeList(taskNodeList);
        flowMsgModel.setOperatorList(operatorList);
        flowMsgModel.setTaskEntity(flowTaskEntity);
        flowMsgModel.setData(data);
        flowMsgModel.setEngine(engine);
        flowMsgModel.setTitle("已被【催办】");
        flowMsgUtil.message(flowMsgModel);
        return flag;
    }

    @Override
    public List<FlowCandidateVO> candidates(String id, FlowHandleModel flowCandidateModel) throws WorkFlowException {
        List<ChildNodeList> childNodeListAll = this.childNodeListAll(id, flowCandidateModel);
        List<FlowCandidateVO> listVO = new ArrayList<>();
        for (ChildNodeList childNodeList : childNodeListAll) {
            Properties properties = childNodeList.getProperties();
            String nodeId = childNodeList.getCustom().getNodeId();
            String nodeName = properties.getTitle();
            String type = properties.getAssigneeType();
            if (FlowTaskOperatorEnum.FreeApprover.getCode().equals(type)) {
                FlowCandidateVO candidateVO = new FlowCandidateVO();
                candidateVO.setNodeName(nodeName);
                candidateVO.setNodeId(nodeId);
                listVO.add(candidateVO);
            }
        }
        return listVO;
    }

    @Override
    public List<FlowCandidateUserModel> candidateUser(String id, FlowHandleModel flowCandidateModel)
            throws WorkFlowException {
        List<FlowCandidateUserModel> dataList = new ArrayList<>();
        List<ChildNodeList> childNodeListAll = this.childNodeListAll(id, flowCandidateModel);
        for (ChildNodeList childNodeList : childNodeListAll) {
            Properties properties = childNodeList.getProperties();
            List<String> positionList = properties.getApproverPos();
            List<String> roleList = properties.getApproverRole();
            List<String> list = new ArrayList<>();
            list.addAll(positionList);
            list.addAll(roleList);
            List<UserRelationEntity> listByObjectIdAll = serviceUtil.getListByObjectIdAll(list);
            List<String> userId = listByObjectIdAll.stream()
                    .map(UserRelationEntity::getUserId)
                    .toList();
            userId.addAll(properties.getApprovers());
            Pagination pagination = JsonUtils.getJsonToBean(flowCandidateModel, Pagination.class);
            List<UserEntity> userName = serviceUtil.getUserName(userId, pagination);
            flowCandidateModel.setTotal(pagination.getTotal());
            for (UserEntity userEntity : userName) {
                FlowCandidateUserModel userModel = new FlowCandidateUserModel();
                userModel.setUserId(userEntity.getId());
                userModel.setUserName(userEntity.getRealName() + "/" + userEntity.getAccount());
                dataList.add(userModel);
            }
        }
        return dataList;
    }

    @Override
    @DSTransactional
    public void batch(FlowHandleModel flowHandleModel) throws WorkFlowException {
        List<String> idList = flowHandleModel.getIds() != null ? flowHandleModel.getIds() : new ArrayList<>();
        Integer batchType = flowHandleModel.getBatchType();
        UserInfo userInfo = userProvider.get();
        for (String id : idList) {
            String rejecttKey = userInfo.getTenantId() + id;
            if (redisUtil.exists(rejecttKey)) {
                throw new WorkFlowException(MsgCode.WF005.get());
            }
            redisUtil.insert(rejecttKey, id, 10);
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(id);
            FlowTaskEntity taskEntity = flowTaskService.getInfo(operator.getTaskId());
            flowHandleModel.setFormData(JsonUtils.toMap(taskEntity.getFlowFormContentJson()));
            FlowModel flowModel = JsonUtils.toObject(flowHandleModel, FlowModel.class);
            switch (batchType) {
                case 0:
                    this.audit(id, flowModel);
                case 1:
                    this.reject(id, flowModel);
                    break;
                case 2:
                    operator.setHandleId(flowHandleModel.getFreeApproverUserId());
                    this.transfer(operator);
                    break;
            }
        }
    }

    @Override
    public List<FlowCandidateVO> batchCandidates(String flowId, String operatorId) throws WorkFlowException {
        FlowEngineEntity flowEngine = flowEngineService.getInfo(flowId);
        FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(operatorId);
        FlowTaskNodeEntity taskNode = flowTaskNodeService.getInfo(operator.getTaskNodeId());
        FlowTaskEntity task = flowTaskService.getInfo(operator.getTaskId());
        ChildNode childNodeAll = JsonUtils.toObject(flowEngine.getFlowTemplateJson(), ChildNode.class);
        // 获取流程节点
        List<ChildNodeList> nodeListAll = new ArrayList<>();
        List<ConditionList> conditionListAll = new ArrayList<>();
        // 递归获取条件数据和节点数据
        FlowJsonUtil.getTemplateAll(childNodeAll, nodeListAll, conditionListAll);
        // 判断节点是否有在条件中
        boolean isCondition =
                conditionListAll.stream().anyMatch(t -> operator.getNodeCode().equals(t.getPrevId()));
        boolean isNext = false;
        if (isCondition) {
            List<String> nodeNext = StrUtil.isNotEmpty(taskNode.getNodeNext())
                    ? Arrays.asList(taskNode.getNodeNext().split(","))
                    : new ArrayList<>();
            isNext = nodeListAll.stream()
                    .anyMatch(t -> nodeNext.contains(t.getCustom().getNodeId())
                            && FlowTaskOperatorEnum.FreeApprover.getCode()
                                    .equals(t.getProperties().getAssigneeType()));
        }
        if (isNext) {
            throw new WorkFlowException("条件流程包含候选人无法批量通过");
        }
        FlowHandleModel flowCandidateModel = new FlowHandleModel();
        Map<String, Object> objectMap = JsonUtils.toMap(task.getFlowFormContentJson());
        objectMap.put("flowId", task.getFlowId());
        flowCandidateModel.setFormData(objectMap);
        return candidates(operatorId, flowCandidateModel);
    }

    /**
     * 判断是否有权限
     *
     * @param userId
     * @param flowId
     * @param operator
     * @throws WorkFlowException
     */
    @Override
    public void permissions(String userId, String flowId, FlowTaskOperatorEntity operator, String msg)
            throws WorkFlowException {
        UserInfo userInfo = userProvider.get();
        if (operator == null
                || FlowNodeEnum.Futility.getCode().equals(operator.getState())
                || !FlowNature.ProcessCompletion.equals(operator.getCompletion())) {
            throw new WorkFlowException(StrUtil.isEmpty(msg) ? MsgCode.WF123.get() : msg);
        }
        List<String> flowDelegateList = flowDelegateService.getUser(userInfo.getUserId(), flowId, userId).stream()
                .map(FlowDelegateEntity::getFTouserid)
                .toList();
        flowDelegateList.add(userId);
        if (!flowDelegateList.contains(userInfo.getUserId())) {
            throw new WorkFlowException(MsgCode.WF123.get());
        }
        FlowTaskEntity flowTask = flowTaskService.getInfo(operator.getTaskId());
        if (flowTask == null) {
            throw new WorkFlowException(MsgCode.WF115.get());
        }
        if (FlowTaskStatusEnum.Cancel.getCode().equals(flowTask.getStatus())) {
            throw new WorkFlowException(MsgCode.WF122.get());
        }
        if (FlowTaskStatusEnum.Revoke.getCode().equals(flowTask.getStatus())) {
            throw new WorkFlowException(MsgCode.WF120.get());
        }
    }

    /**
     * 查询候选人
     *
     * @param taskNodeList 所有节点
     * @param childNodeListAll 节点数据
     * @param nodeCode 当前节点
     */
    private void candidate(
            List<FlowTaskNodeEntity> taskNodeList,
            List<ChildNodeList> childNodeListAll,
            String nodeCode,
            boolean isNext) {
        List<FlowTaskNodeEntity> nodeList = taskNodeList.stream()
                .filter(t -> t.getNodeCode().equals(nodeCode))
                .toList();
        for (FlowTaskNodeEntity taskNodeEntity : nodeList) {
            if (isNext) {
                List<String> nextNodeList =
                        Arrays.asList(taskNodeEntity.getNodeNext().split(","));
                List<FlowTaskNodeEntity> nextTaskNodeList = taskNodeList.stream()
                        .filter(t -> nextNodeList.contains(t.getNodeCode()))
                        .toList();
                for (FlowTaskNodeEntity nodeEntity : nextTaskNodeList) {
                    String nodeType = nodeEntity.getNodeType();
                    String code = nodeEntity.getNodeCode();
                    if (FlowNature.NodeSubFlow.equals(nodeType)) {
                        candidate(taskNodeList, childNodeListAll, code, true);
                    } else {
                        ChildNodeList childNodeList =
                                JsonUtils.toObject(nodeEntity.getNodePropertyJson(), ChildNodeList.class);
                        childNodeListAll.add(childNodeList);
                    }
                }
            } else {
                ChildNodeList childNodeList =
                        JsonUtils.toObject(taskNodeEntity.getNodePropertyJson(), ChildNodeList.class);
                childNodeListAll.add(childNodeList);
            }
        }
    }

    // --------------------------------------候选人------------------------------------------------------------------

    /**
     * 获取节点候选人
     *
     * @param id
     * @param flowCandidateModel
     * @return
     * @throws WorkFlowException
     */
    private List<ChildNodeList> childNodeListAll(String id, FlowHandleModel flowCandidateModel)
            throws WorkFlowException {
        List<ChildNodeList> childNodeListAll = new ArrayList<>();
        List<FlowTaskNodeEntity> taskNodeList = new ArrayList<>();
        String nodeCode = "";
        FlowTaskOperatorEntity operatorEntity = flowTaskOperatorService.getInfo(id);
        boolean isNodeCode = StrUtil.isNotEmpty(flowCandidateModel.getNodeCode());
        boolean parentId = false;
        Map<String, Object> formData = flowCandidateModel.getFormData();
        Object flowId = formData.get("flowId");
        if (ObjectUtil.isNotNull(flowId)) {
            FlowEngineEntity engine = flowEngineService.getInfo(String.valueOf(flowId));
            if (FlowNature.CUSTOM.equals(engine.getFormType())) {
                Map<String, Object> formDataAll = flowCandidateModel.getFormData();
                Object data = formDataAll.get("data");
                if (data != null) {
                    formData = JsonUtils.toMap(String.valueOf(data));
                }
            }
            ChildNode childNodeAll = JsonUtils.toObject(engine.getFlowTemplateJson(), ChildNode.class);
            // 获取流程节点
            List<ChildNodeList> nodeListAll = new ArrayList<>();
            List<ConditionList> conditionListAll = new ArrayList<>();
            // 递归获取条件数据和节点数据
            FlowTaskEntity flowTask = new FlowTaskEntity();
            flowTask.setId(RandomUtil.uuId());
            flowTask.setFlowFormContentJson(JsonUtils.toJSONString(formData));
            this.updateNodeList(flowTask, childNodeAll, nodeListAll, conditionListAll, taskNodeList);
            Optional<FlowTaskNodeEntity> first = taskNodeList.stream()
                    .filter(t -> FlowNature.NodeStart.equals(t.getNodeType()))
                    .findFirst();
            if (first.isEmpty()) {
                throw new WorkFlowException(MsgCode.COD001.get());
            }
            FlowTaskNodeEntity startNodes = first.get();
            nodeCode = startNodes.getNodeCode();
            this.nodeList(taskNodeList, nodeCode, 1L);
        }
        if (operatorEntity != null) {
            nodeCode = operatorEntity.getNodeCode();
            parentId = !FlowNature.ParentId.equals(operatorEntity.getParentId());
        }
        if (isNodeCode) {
            nodeCode = flowCandidateModel.getNodeCode();
        }
        this.candidate(taskNodeList, childNodeListAll, nodeCode, !isNodeCode);
        if (parentId) {
            childNodeListAll = new ArrayList<>();
        }
        return childNodeListAll;
    }

    // -----------------------------------提交保存--------------------------------------------

    /**
     * 流程任务赋值
     *
     * @param taskEntity 流程任务实例
     * @param engine 流程引擎实例
     * @param flowModel 提交数据
     * @throws WorkFlowException 异常
     */
    private void task(FlowTaskEntity taskEntity, FlowEngineEntity engine, FlowModel flowModel, String userId)
            throws WorkFlowException {
        if (flowModel.getId() != null && !checkStatus(taskEntity.getStatus())) {
            throw new WorkFlowException(MsgCode.WF108.get());
        }
        // 创建实例
        taskEntity.setId(flowModel.getProcessId());
        taskEntity.setProcessId(flowModel.getProcessId());
        taskEntity.setEnCode(flowModel.getBillNo());
        taskEntity.setFullName(flowModel.getFlowTitle());
        taskEntity.setFlowUrgent(flowModel.getFlowUrgent() != null ? flowModel.getFlowUrgent() : 1);
        taskEntity.setFlowId(engine.getId());
        taskEntity.setFlowCode(engine.getEnCode() != null ? engine.getEnCode() : MsgCode.WF109.get());
        taskEntity.setFlowName(engine.getFullName());
        taskEntity.setFlowType(engine.getType());
        taskEntity.setFlowCategory(engine.getCategory());
        taskEntity.setFlowForm(engine.getFormData());
        taskEntity.setFlowTemplateJson(engine.getFlowTemplateJson());
        taskEntity.setFlowVersion(engine.getVersion());
        taskEntity.setStatus(
                FlowStatusEnum.save.getMessage().equals(flowModel.getStatus())
                        ? FlowTaskStatusEnum.Draft.getCode()
                        : FlowTaskStatusEnum.Handle.getCode());
        taskEntity.setCompletion(FlowNature.ProcessCompletion);
        taskEntity.setCreatorTime(new Date());
        taskEntity.setEnabledMark(1);
        taskEntity.setCreatorUserId(userId);
        taskEntity.setFlowFormContentJson(
                flowModel.getFormData() != null ? JsonUtils.toJSONString(flowModel.getFormData()) : "{}");
        taskEntity.setParentId(flowModel.getParentId() != null ? flowModel.getParentId() : FlowNature.ParentId);
        taskEntity.setIsAsync(flowModel.getIsAsync() ? FlowNature.ChildAsync : FlowNature.ChildSync);
        ChildNode childNode = JsonUtils.toObject(engine.getFlowTemplateJson(), ChildNode.class);
        boolean isBatchApproval = false;
        if (ObjectUtil.isNotEmpty(childNode.getProperties().getIsBatchApproval())
                && childNode.getProperties().getIsBatchApproval()) {
            isBatchApproval = true;
        }
        taskEntity.setIsBatch(isBatchApproval ? 1 : 0);
    }

    /**
     * 验证有效状态
     *
     * @param status 状态编码
     * @return
     */
    private boolean checkStatus(int status) {
        if (status == FlowTaskStatusEnum.Draft.getCode()
                || status == FlowTaskStatusEnum.Reject.getCode()
                || status == FlowTaskStatusEnum.Revoke.getCode()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 提交节点
     *
     * @param dataAll 所有流程节点
     */
    private void nodeListAll(List<FlowTaskNodeEntity> dataAll, FlowModel flowModel, boolean isAdd)
            throws WorkFlowException {
        UserInfo userInfo = userProvider.get();
        Optional<FlowTaskNodeEntity> first = dataAll.stream()
                .filter(t -> FlowNature.NodeStart.equals(t.getNodeType()))
                .findFirst();
        if (!first.isPresent()) {
            throw new WorkFlowException(MsgCode.COD001.get());
        }
        FlowTaskNodeEntity startNodes = first.get();
        long num = 1L;
        this.nodeList(dataAll, startNodes.getNodeCode(), num);
        String nodeNext = FlowNature.NodeEnd;
        String type = "endround";
        long maxNum = 1L;
        Map<String, List<String>> candidateList =
                flowModel.getCandidateList() != null ? flowModel.getCandidateList() : new HashMap<>();
        List<FlowCandidatesEntity> candidateListAll = new ArrayList<>();
        for (FlowTaskNodeEntity entity : dataAll) {
            if (StrUtil.isEmpty(entity.getNodeNext())) {
                entity.setNodeNext(nodeNext);
            }
            if (entity.getSortCode() != null && entity.getSortCode() > maxNum) {
                maxNum = entity.getSortCode();
            }
            if (!"timer".equals(entity.getNodeType())) {
                List<String> list = candidateList.get(entity.getNodeCode()) != null
                        ? candidateList.get(entity.getNodeCode())
                        : new ArrayList<>();
                if (list.size() > 0) {
                    FlowCandidatesEntity candidates = new FlowCandidatesEntity();
                    candidates.setHandleId(userInfo.getId());
                    candidates.setTaskNodeId(entity.getId());
                    candidates.setTaskId(entity.getTaskId());
                    candidates.setAccount(userInfo.getUserAccount());
                    candidates.setCandidates(JsonUtils.getObjectToString(list));
                    candidates.setOperatorId(FlowNature.ParentId);
                    candidateListAll.add(candidates);
                }
            }
        }
        FlowTaskNodeEntity endround = new FlowTaskNodeEntity();
        endround.setId(RandomUtil.uuId());
        endround.setNodeCode(nodeNext);
        endround.setNodeName(MsgCode.WF007.get());
        endround.setCompletion(FlowNature.ProcessCompletion);
        endround.setCreatorTime(new Date());
        endround.setSortCode(++maxNum);
        endround.setTaskId(startNodes.getTaskId());
        ChildNodeList endNode = JsonUtils.toObject(startNodes.getNodePropertyJson(), ChildNodeList.class);
        endNode.getCustom().setNodeId(nodeNext);
        endNode.setTaskNodeId(endround.getId());
        endNode.getCustom().setType(type);
        endround.setNodePropertyJson(JsonUtils.toJSONString(endNode));
        endround.setNodeType(type);
        endround.setState(FlowNodeEnum.Process.getCode());
        dataAll.add(endround);
        if (isAdd) {
            for (FlowTaskNodeEntity entity : dataAll) {
                flowTaskNodeService.create(entity);
            }
            for (FlowCandidatesEntity entity : candidateListAll) {
                flowCandidatesService.create(entity);
            }
        }
    }

    /**
     * 递归遍历编码
     *
     * @param dataAll 所有节点
     * @param node 当前节点
     * @param num 排序
     */
    private void nodeList(List<FlowTaskNodeEntity> dataAll, String node, long num) {
        List<String> nodeAll = Arrays.asList(node.split(","));
        List<FlowTaskNodeEntity> nodeList =
                dataAll.stream().filter(t -> nodeAll.contains(t.getNodeCode())).toList();
        for (FlowTaskNodeEntity entity : nodeList) {
            entity.setSortCode(num);
            entity.setState(FlowNodeEnum.Process.getCode());
        }
        List<String> nextNode = nodeList.stream()
                .map(FlowTaskNodeEntity::getNodeNext)
                .filter(Objects::nonNull)
                .toList();
        if (nextNode.size() > 0) {
            String nodes = String.join(",", nextNode);
            num++;
            nodeList(dataAll, nodes, num);
        }
    }

    /**
     * 创建节点
     *
     * @param flowTask
     * @param nodeListAll
     * @param conditionListAll
     * @param taskNodeList
     */
    private void createNodeList(
            FlowTaskEntity flowTask,
            List<ChildNodeList> nodeListAll,
            List<ConditionList> conditionListAll,
            List<FlowTaskNodeEntity> taskNodeList) {
        List<FlowTaskNodeEntity> timerList = new ArrayList<>();
        List<FlowTaskNodeEntity> emptyList = new ArrayList<>();
        for (ChildNodeList childNode : nodeListAll) {
            FlowTaskNodeEntity taskNode = new FlowTaskNodeEntity();
            String nodeId = childNode.getCustom().getNodeId();
            Properties properties = childNode.getProperties();
            String dataJson = flowTask.getFlowFormContentJson();
            String type = childNode.getCustom().getType();
            taskNode.setId(RandomUtil.uuId());
            childNode.setTaskNodeId(taskNode.getId());
            childNode.setTaskId(flowTask.getId());
            taskNode.setCreatorTime(new Date());
            taskNode.setTaskId(flowTask.getId());
            taskNode.setNodeCode(nodeId);
            taskNode.setNodeType(type);
            taskNode.setState(FlowNodeEnum.Futility.getCode());
            taskNode.setSortCode(-2L);
            taskNode.setNodeUp(properties.getRejectStep());
            taskNode.setNodeNext(FlowJsonUtil.getNextNode(nodeId, dataJson, nodeListAll, conditionListAll));
            taskNode.setNodePropertyJson(JsonUtils.toJSONString(childNode));
            boolean isStart = FlowNature.NodeStart.equals(childNode.getCustom().getType());
            taskNode.setCompletion(isStart ? FlowNature.AuditCompletion : FlowNature.ProcessCompletion);
            taskNode.setNodeName(isStart ? MsgCode.WF006.get() : properties.getTitle());
            taskNodeList.add(taskNode);
            if ("empty".equals(type)) {
                emptyList.add(taskNode);
            }
            if ("timer".equals(type)) {
                timerList.add(taskNode);
            }
        }
        // 指向empty，继续指向下一个节点
        for (FlowTaskNodeEntity empty : emptyList) {
            List<FlowTaskNodeEntity> noxtEmptyList = taskNodeList.stream()
                    .filter(t -> t.getNodeNext().contains(empty.getNodeCode()))
                    .toList();
            for (FlowTaskNodeEntity entity : noxtEmptyList) {
                entity.setNodeNext(empty.getNodeNext());
            }
        }
        // 指向timer，继续指向下一个节点
        for (FlowTaskNodeEntity timer : timerList) {
            // 获取到timer的上一节点
            ChildNodeList timerlList = JsonUtils.toObject(timer.getNodePropertyJson(), ChildNodeList.class);
            DateProperties timers = timerlList.getTimer();
            timers.setNodeId(timer.getNodeCode());
            timers.setTime(true);
            List<FlowTaskNodeEntity> upEmptyList = taskNodeList.stream()
                    .filter(t -> t.getNodeNext().contains(timer.getNodeCode()))
                    .toList();
            for (FlowTaskNodeEntity entity : upEmptyList) {
                // 上一节点赋值timer的属性
                ChildNodeList modelList = JsonUtils.toObject(entity.getNodePropertyJson(), ChildNodeList.class);
                modelList.setTimer(timers);
                entity.setNodeNext(timer.getNodeNext());
                entity.setNodePropertyJson(JsonUtils.toJSONString(modelList));
            }
        }
    }

    // -------------------------审批--------------------------------
    // ---------通过-------------

    /**
     * 下一审批人
     *
     * @param operatorListAll 审批人数据
     * @param nodeList 下一审批的数据
     * @param flowTask 引擎实例
     * @param flowModel 提交数据
     * @throws WorkFlowException 异常
     */
    private Map<String, List<String>> nextOperator(
            List<FlowTaskOperatorEntity> operatorListAll,
            List<ChildNodeList> nodeList,
            FlowTaskEntity flowTask,
            FlowModel flowModel)
            throws WorkFlowException {
        Map<String, List<String>> taskNode = new HashMap<>(16);
        try {
            // 查询审批人
            for (ChildNodeList childNode : nodeList) {
                List<FlowTaskOperatorEntity> operatorList = new ArrayList<>();
                Custom custom = childNode.getCustom();
                Properties properties = childNode.getProperties();
                String type = custom.getType();
                String flowId = properties.getFlowId();
                List<FlowAssignModel> assignList = childNode.getProperties().getAssignList();
                // 判断子流程
                boolean isChild = FlowNature.NodeSubFlow.equals(type);
                if (isChild) {
                    // 判断当前流程引擎类型
                    FlowEngineEntity parentEngine = flowEngineService.getInfo(flowTask.getFlowId());
                    boolean isCustom = FlowNature.CUSTOM.equals(parentEngine.getFormType());
                    List<String> taskNodeList = new ArrayList<>();
                    FlowEngineEntity engine = flowEngineService.getInfo(flowId);
                    // 创建子流程
                    Map<String, Object> data = this.childData(engine, flowModel, assignList, isCustom);
                    data.put("flowId", flowId);
                    // 子节点审批人
                    List<UserEntity> list = this.childSaveList(childNode, flowTask);
                    // 子流程消息
                    List<FlowTaskNodeEntity> childTaskNodeAll = flowTaskNodeService.getList(flowTask.getId());
                    List<FlowTaskOperatorEntity> childOperatorList = new ArrayList<>();
                    FlowMsgModel flowMsgModel = new FlowMsgModel();
                    flowMsgModel.setCirculateList(new ArrayList<>());
                    flowMsgModel.setNodeList(childTaskNodeAll);
                    flowMsgModel.setData(flowModel.getFormData());
                    flowMsgModel.setWait(false);
                    flowMsgModel.setLaunch(true);
                    for (UserEntity entity : list) {
                        String title = entity.getRealName() + "的" + engine.getFullName() + "(子流程)";
                        FlowModel nextFlowModel = this.assignment(data, parentEngine, flowTask.getId(), title);
                        nextFlowModel.setUserId(entity.getId());
                        nextFlowModel.setFlowTitle(title);
                        nextFlowModel.setFormData(data);
                        nextFlowModel.setIsAsync(properties.getIsAsync());
                        nextFlowModel.setFlowId(engine.getId());
                        FlowTaskEntity childTaskEntity = this.save(nextFlowModel);
                        this.createData(engine, childTaskEntity, nextFlowModel);
                        // 子流程数据整合
                        FlowModel parentModel = new FlowModel();
                        parentModel.setUserId("");
                        parentModel.setFormData(data);
                        parentModel.setIsAsync(properties.getIsAsync());
                        FlowTaskNodeEntity taskNodeEntity = flowTaskNodeService.getInfo(childNode.getTaskNodeId());
                        FlowTaskOperatorEntity parentOperator = new FlowTaskOperatorEntity();
                        this.parentOperator(parentOperator, taskNodeEntity);
                        if (properties.getIsAsync()) {
                            FlowTaskEntity parentFlowTask = flowTask;
                            this.audit(parentFlowTask, parentOperator, parentModel);
                            taskNodeEntity.setCompletion(FlowNature.AuditCompletion);
                            flowTaskNodeService.update(taskNodeEntity);
                        } else {
                            // 同步
                            taskNodeList.add(nextFlowModel.getProcessId());
                        }
                        parentOperator.setHandleId(entity.getId());
                        parentOperator.setTaskId(nextFlowModel.getProcessId());
                        childOperatorList.add(parentOperator);
                        // 发送子流程消息
                        List<FlowTaskOperatorEntity> launchList = new ArrayList<>();
                        FlowTaskEntity taskEntity = new FlowTaskEntity();
                        taskEntity.setFullName(title);
                        launchList.add(parentOperator);
                        flowMsgModel.setOperatorList(launchList);
                        flowMsgModel.setEngine(engine);
                        flowMsgModel.setTaskEntity(taskEntity);
                        flowMsgUtil.message(flowMsgModel);
                    }
                    taskNode.put(childNode.getTaskNodeId(), taskNodeList);
                } else {
                    if (!FlowNature.NodeEnd.equals(childNode.getCustom().getNodeId())) {
                        // 审批人
                        TaskOperator taskOperator = new TaskOperator();
                        taskOperator.setChildNode(childNode);
                        taskOperator.setTaskEntity(flowTask);
                        taskOperator.setFlowModel(flowModel);
                        taskOperator.setDetails(true);
                        taskOperator.setId(flowModel.getOperatorId());
                        this.operator(operatorList, taskOperator);
                    }
                }
                operatorListAll.addAll(operatorList);
            }
        } catch (WorkFlowException e) {
            log.error("下一审批人异常:{}", e.getMessage());
            throw new WorkFlowException(e.getMessage());
        }
        return taskNode;
    }

    /**
     * 审批人 taskOperator 对象
     *
     * @param operatorList
     * @param taskOperator
     */
    private void operator(List<FlowTaskOperatorEntity> operatorList, TaskOperator taskOperator) {
        ChildNodeList childNode = taskOperator.getChildNode();
        FlowTaskEntity taskEntity = taskOperator.getTaskEntity();
        FlowModel flowModel = taskOperator.getFlowModel();
        List<String> userIdAll = new ArrayList<>();
        String createUserId = taskEntity.getCreatorUserId();
        Date date = new Date();
        List<FlowTaskOperatorEntity> nextList = new ArrayList<>();
        Properties properties = childNode.getProperties();
        String type = properties.getAssigneeType();
        String userId = "";
        String freeApproverUserId = flowModel.getFreeApproverUserId();
        TaskOperatoUser taskOperatoUser = new TaskOperatoUser();
        taskOperatoUser.setDate(date);
        taskOperatoUser.setChildNode(childNode);
        boolean isStatus = StrUtil.isNotEmpty(freeApproverUserId);
        taskOperatoUser.setId(FlowNature.ParentId);
        // 【加签】
        if (isStatus) {
            taskOperatoUser.setHandLeId(freeApproverUserId);
            taskOperatoUser.setId(taskOperator.getId());
            this.operatorUser(nextList, taskOperatoUser);
            boolean details = taskOperator.getDetails();
            // 加签记录
            if (details) {
                UserInfo userInfo = userProvider.get();
                Custom custom = childNode.getCustom();
                FlowTaskOperatorRecordEntity operatorRecord = new FlowTaskOperatorRecordEntity();
                FlowTaskOperatorEntity operator = new FlowTaskOperatorEntity();
                operator.setTaskId(childNode.getTaskId());
                operator.setNodeCode(custom.getNodeId());
                operator.setNodeName(properties.getTitle());
                // 审批数据赋值
                FlowOperatordModel flowOperatordModel = new FlowOperatordModel();
                flowOperatordModel.setStatus(FlowRecordEnum.copyId.getCode());
                flowOperatordModel.setFlowModel(flowModel);
                flowOperatordModel.setUserId(userInfo.getUserId());
                flowOperatordModel.setOperator(operator);
                flowOperatordModel.setOperatorId(freeApproverUserId);
                this.operatorRecord(operatorRecord, flowOperatordModel);
                flowTaskOperatorRecordService.create(operatorRecord);
            }
        } else {
            // 发起者【发起者主管】
            if (FlowTaskOperatorEnum.LaunchCharge.getCode().equals(type)) {
                // 时时查用户主管
                UserEntity info = serviceUtil.getUserInfo(createUserId);
                if (info != null) {
                    userId = getManagerByLevel(info.getManagerId(), properties.getManagerLevel(), new ArrayList<>());
                    userIdAll.add(userId);
                }
            }
            // 发起者【部门主管】
            if (FlowTaskOperatorEnum.DepartmentCharge.getCode().equals(type)) {
                UserEntity userEntity = serviceUtil.getUserInfo(createUserId);
                if (userEntity != null) {
                    OrganizeEntity organizeEntity = serviceUtil.getOrganizeInfo(userEntity.getOrganizeId());
                    if (organizeEntity != null) {
                        userId = organizeEntity.getManager();
                        userIdAll.add(userId);
                    }
                }
            }
            // 发起者【发起本人】
            if (FlowTaskOperatorEnum.InitiatorMe.getCode().equals(type)) {
                userIdAll.add(createUserId);
            }
            // 【环节】
            if (FlowTaskOperatorEnum.Tache.getCode().equals(type)) {
                List<FlowTaskOperatorRecordEntity> operatorUserList =
                        flowTaskOperatorRecordService.getList(taskEntity.getId()).stream()
                                .filter(t -> properties.getNodeId().equals(t.getNodeCode())
                                        && FlowRecordEnum.audit.getCode().equals(t.getHandleStatus())
                                        && FlowNodeEnum.Process.getCode().equals(t.getStatus()))
                                .toList();
                List<String> handleId = operatorUserList.stream()
                        .map(FlowTaskOperatorRecordEntity::getHandleId)
                        .toList();
                userIdAll.addAll(handleId);
            }
            // 【变量】
            if (FlowTaskOperatorEnum.Variate.getCode().equals(type)) {
                Map<String, Object> dataAll = JsonUtils.toMap(taskEntity.getFlowFormContentJson());
                Object data = dataAll.get(properties.getFormField());
                if (data != null) {
                    List<String> handleIdAll = new ArrayList<>();
                    if (data instanceof List) {
                        handleIdAll.addAll((List) data);
                    } else {
                        if (String.valueOf(data).contains("[")) {
                            handleIdAll.addAll(JsonUtils.toList(String.valueOf(data), String.class));
                        } else {
                            handleIdAll.addAll(
                                    Arrays.asList(String.valueOf(data).split(",")));
                        }
                    }
                    userIdAll.addAll(handleIdAll);
                }
            }
            // 【服务】
            if (FlowTaskOperatorEnum.Serve.getCode().equals(type)) {
                String url = properties.getGetUserUrl()
                        + "?"
                        + taskNodeId
                        + "="
                        + childNode.getTaskNodeId()
                        + "&"
                        + taskId
                        + "="
                        + childNode.getTaskId();
                String token = UserProvider.getToken();
                JSONObject object = HttpUtil.httpRequest(url, "GET", null, token);
                if (object != null) {
                    if (object.get("data") != null) {
                        JSONObject data = object.getJSONObject("data");
                        List<String> handleId = StrUtil.isNotEmpty(data.getString("handleId"))
                                ? Arrays.asList(data.getString("handleId").split(","))
                                : new ArrayList<>();
                        userIdAll.addAll(handleId);
                    }
                }
            }
            // 【候选人】
            if (FlowTaskOperatorEnum.FreeApprover.getCode().equals(type)) {
                String nodeId = childNode.getTaskNodeId();
                List<FlowCandidatesEntity> candidatesList = flowCandidatesService.getlist(nodeId);
                candidatesList.forEach(t -> {
                    List<String> candidates = StrUtil.isNotEmpty(t.getCandidates())
                            ? JsonUtils.toList(t.getCandidates(), String.class)
                            : new ArrayList<>();
                    userIdAll.addAll(candidates);
                });
            } else {
                // 发起者【指定用户】
                userIdAll.addAll(properties.getApprovers());
                // 发起者【指定岗位】
                List<String> positionList = properties.getApproverPos();
                // 发起者【指定角色】
                List<String> roleList = properties.getApproverRole();
                List<String> list = new ArrayList<>();
                list.addAll(positionList);
                list.addAll(roleList);
                List<UserRelationEntity> listByObjectIdAll = serviceUtil.getListByObjectIdAll(list);
                List<String> userPosition = listByObjectIdAll.stream()
                        .map(UserRelationEntity::getUserId)
                        .toList();
                userIdAll.addAll(userPosition);
            }
            List<UserEntity> userAll = serviceUtil.getUserName(userIdAll);
            for (UserEntity entity : userAll) {
                taskOperatoUser.setHandLeId(entity.getId());
                this.operatorUser(nextList, taskOperatoUser);
            }
        }
        if (nextList.size() == 0) {
            taskOperatoUser.setHandLeId(user);
            this.operatorUser(nextList, taskOperatoUser);
        }
        operatorList.addAll(nextList);
    }

    /**
     * 递归主管
     *
     * @param managerId 主管id
     * @param level 第几级
     * @return
     */
    private String getManagerByLevel(String managerId, long level, List<UserEntity> userList) {
        --level;
        if (level == 0) {
            return managerId;
        } else {
            UserEntity userEntity = userList.stream()
                    .filter(t -> t.getId().equals(managerId))
                    .findFirst()
                    .orElse(null);
            if (userEntity == null) {
                userEntity = serviceUtil.getUserInfo(managerId);
                if (userEntity != null) {
                    userList.add(userEntity);
                }
            }
            return userEntity != null ? getManagerByLevel(userEntity.getManagerId(), level, userList) : "";
        }
    }

    /**
     * 封装审批人
     *
     * @param nextList 所有审批人数据
     * @param taskOperatoUser 对象
     */
    private void operatorUser(List<FlowTaskOperatorEntity> nextList, TaskOperatoUser taskOperatoUser) {
        String handLeId = taskOperatoUser.getHandLeId();
        Date date = taskOperatoUser.getDate();
        ChildNodeList childNode = taskOperatoUser.getChildNode();
        Properties properties = childNode.getProperties();
        Custom custom = childNode.getCustom();
        String type = properties.getAssigneeType();
        FlowTaskOperatorEntity operator = new FlowTaskOperatorEntity();
        operator.setId(RandomUtil.uuId());
        operator.setHandleType(type);
        operator.setHandleId(StrUtil.isEmpty(handLeId) ? user : handLeId);
        operator.setTaskNodeId(childNode.getTaskNodeId());
        operator.setTaskId(childNode.getTaskId());
        operator.setNodeCode(custom.getNodeId());
        operator.setNodeName(properties.getTitle());
        operator.setDescription(JsonUtils.toJSONString(new ArrayList<>()));
        operator.setCreatorTime(date);
        operator.setCompletion(FlowNature.ProcessCompletion);
        operator.setType(type);
        operator.setState(FlowNodeEnum.Process.getCode());
        operator.setParentId(taskOperatoUser.getId());
        nextList.add(operator);
    }

    /**
     * 更新经办数据
     *
     * @param operator 当前经办
     * @param handStatus 对象
     */
    private void handleIdStatus(FlowTaskOperatorEntity operator, TaskHandleIdStatus handStatus) {
        int status = handStatus.getStatus();
        ChildNodeList nodeModel = handStatus.getNodeModel();
        FlowModel flowModel = handStatus.getFlowModel();
        Properties properties = nodeModel.getProperties();
        Integer counterSign = properties.getCounterSign();
        operator.setHandleTime(new Date());
        operator.setHandleStatus(status);
        String type = properties.getAssigneeType();
        boolean isApprover = FlowNature.FixedJointlyApprover.equals(counterSign);
        List<String> userIdListAll = new ArrayList<>();
        if (status == 1) {
            boolean hasFreeApprover = StrUtil.isEmpty(flowModel.getFreeApproverUserId());
            if (isApprover) {
                // 更新会签都改成完成
                flowTaskOperatorService.update(operator.getTaskNodeId(), userIdListAll, "1");
            } else {
                if (hasFreeApprover) {
                    // 更新或签都改成完成
                    flowTaskOperatorService.update(operator.getTaskNodeId(), type);
                }
            }
            operator.setCompletion(FlowNature.AuditCompletion);
            // 修改当前审批的定时器
            List<Date> list = JsonUtils.getJsonToList(operator.getDescription(), Date.class);
            DateProperties timer = nodeModel.getTimer();
            if (timer.getTime()) {
                Date date = new Date();
                date = DateUtil.dateAddDays(date, timer.getDay());
                date = DateUtil.dateAddHours(date, timer.getHour());
                date = DateUtil.dateAddMinutes(date, timer.getMinute());
                date = DateUtil.dateAddSeconds(date, timer.getSecond());
                list.add(date);
                operator.setDescription(JsonUtils.toJSONString(list));
            }
        } else {
            if (isApprover) {
                // 更新会签都改成完成
                flowTaskOperatorService.update(operator.getTaskNodeId(), userIdListAll, "-1");
            } else {
                // 更新或签都改成完成
                flowTaskOperatorService.update(operator.getTaskNodeId(), type);
            }
            operator.setCompletion(FlowNature.RejectCompletion);
        }
    }

    /**
     * 判断是否进行下一步
     *
     * @param nodeListAll 所有节点
     * @param nextNodeEntity 下一节点
     * @param taskNode 当前节点
     * @param flowModel 提交数据
     * @return
     */
    private List<FlowTaskNodeEntity> isNextAll(
            List<FlowTaskNodeEntity> nodeListAll,
            List<FlowTaskNodeEntity> nextNodeEntity,
            FlowTaskNodeEntity taskNode,
            FlowModel flowModel) {
        // 1.先看是否加签人，有都不要进行，无进行下一步
        // 2.判断会签是否比例通过
        // 3.判断分流是否都结束
        // 4.判断审批人是否都通过
        List<FlowTaskNodeEntity> result = new ArrayList<>();
        boolean hasFreeApprover = StrUtil.isNotEmpty(flowModel.getFreeApproverUserId());
        if (hasFreeApprover) {
            result.add(taskNode);
            // 加签记录
        } else {
            ChildNodeList nodeModel = JsonUtils.toObject(taskNode.getNodePropertyJson(), ChildNodeList.class);
            Properties properties = nodeModel.getProperties();
            // 会签通过
            boolean isCountersign = true;
            boolean fixed = FlowNature.FixedJointlyApprover.equals(properties.getCounterSign());
            long pass = properties.getCountersignRatio();
            String type = properties.getAssigneeType();
            // 判断是否是会签
            if (fixed) {
                List<FlowTaskOperatorEntity> operatorList =
                        flowTaskOperatorService.getList(taskNode.getTaskId()).stream()
                                .filter(t -> t.getTaskNodeId().equals(taskNode.getId())
                                        && FlowNodeEnum.Process.getCode().equals(t.getState()))
                                .toList();
                double total = operatorList.stream()
                        .filter(t -> FlowNature.ParentId.equals(t.getParentId()))
                        .count();
                List<FlowTaskOperatorEntity> passNumList = this.passNum(operatorList, FlowNature.AuditCompletion);
                double passNum = passNumList.size();
                isCountersign = this.isCountersign(pass, total, passNum);
            }
            // 流程通过
            if (isCountersign) {
                // 会签通过更新未审批用户
                if (fixed) {
                    flowTaskOperatorService.update(nodeModel.getTaskNodeId(), type);
                }
                taskNode.setCompletion(FlowNature.AuditCompletion);
                // 跟新审批状态
                flowTaskNodeService.update(taskNode);
                // 分流通过
                boolean isShunt = this.isShunt(nodeListAll, nextNodeEntity, taskNode);
                if (isShunt) {
                    result.addAll(nextNodeEntity);
                }
            }
        }
        return result;
    }

    /**
     * 会签比例
     *
     * @param pass 比例
     * @param total 总数
     * @param passNum 数量
     * @return
     */
    private boolean isCountersign(long pass, double total, double passNum) {
        int scale = (int) (passNum / total * 100);
        return scale >= pass;
    }

    /**
     * 获取通过人数
     *
     * @param operatorList 流程经办数据
     * @return
     */
    private List<FlowTaskOperatorEntity> passNum(List<FlowTaskOperatorEntity> operatorList, Integer completion) {
        // 1.先挑选parentId为0,没有加签人的数据
        List<String> idListAll = operatorList.stream()
                .filter(t -> FlowNature.ParentId.equals(t.getParentId()))
                .map(FlowTaskOperatorEntity::getId)
                .toList();
        List<String> childList = operatorList.stream()
                .map(FlowTaskOperatorEntity::getParentId)
                .filter(parentId -> !FlowNature.ParentId.equals(parentId))
                .toList();
        idListAll.removeAll(childList);
        Set<String> idAll = new HashSet<>(idListAll);
        // 2.从加签人中筛选最后审批人数据
        List<String> parentList = operatorList.stream()
                .filter(t -> !FlowNature.ParentId.equals(t.getParentId()))
                .map(FlowTaskOperatorEntity::getId)
                .toList();
        parentList.removeAll(childList);
        idAll.addAll(parentList);
        // 3.获取最后的审批人数据
        return operatorList.stream()
                .filter(t -> idAll.contains(t.getId()) && completion.equals(t.getCompletion()))
                .toList();
    }

    /**
     * 判断分流是否结束
     *
     * @param nodeListAll 所有节点
     * @param nextNodeEntity 下一节点
     * @param taskNode 单前节点
     * @return
     */
    private boolean isShunt(
            List<FlowTaskNodeEntity> nodeListAll,
            List<FlowTaskNodeEntity> nextNodeEntity,
            FlowTaskNodeEntity taskNode) {
        boolean isNext = true;
        for (FlowTaskNodeEntity nodeEntity : nextNodeEntity) {
            String nextNode = nodeEntity.getNodeCode();
            List<FlowTaskNodeEntity> interflowAll = nodeListAll.stream()
                    .filter(t -> String.valueOf(t.getNodeNext()).contains(nextNode)
                            && FlowNodeEnum.Process.getCode().equals(t.getState()))
                    .toList();
            List<FlowTaskNodeEntity> flowAll = interflowAll.stream()
                    .filter(t -> !FlowNature.AuditCompletion.equals(t.getCompletion()))
                    .toList();
            if (flowAll.size() > 0) {
                isNext = false;
                break;
            }
        }
        return isNext;
    }

    /**
     * 抄送人
     *
     * @param nodeModel 当前json对象
     * @param circulateList 抄送list
     * @param flowModel 提交数据
     */
    private void circulateList(
            ChildNodeList nodeModel, List<FlowTaskCirculateEntity> circulateList, FlowModel flowModel) {
        Properties circleproperties = nodeModel.getProperties();
        List<String> userIdAll = new ArrayList<>(circleproperties.getCirculateUser());
        // 传阅者【指定角色】
        List<String> roleList = circleproperties.getCirculateRole();
        // 传阅者【指定岗位】
        List<String> posList = circleproperties.getCirculatePosition();
        List<String> userAll = new ArrayList<>();
        userAll.addAll(roleList);
        userAll.addAll(posList);
        List<UserRelationEntity> listByObjectIdAll = serviceUtil.getListByObjectIdAll(userAll);
        List<String> userPosition =
                listByObjectIdAll.stream().map(UserRelationEntity::getUserId).toList();
        userIdAll.addAll(userPosition);
        // 指定传阅人
        String[] copyIds = StrUtil.isNotEmpty(flowModel.getCopyIds())
                ? flowModel.getCopyIds().split(",")
                : new String[] {};
        List<String> id = Arrays.asList(copyIds);
        userIdAll.addAll(id);
        List<UserEntity> list = serviceUtil.getUserName(userIdAll);
        for (UserEntity userEntity : list) {
            FlowTaskCirculateEntity flowTask = new FlowTaskCirculateEntity();
            flowTask.setId(RandomUtil.uuId());
            flowTask.setObjectId(userEntity.getId());
            flowTask.setNodeCode(nodeModel.getCustom().getNodeId());
            flowTask.setNodeName(nodeModel.getProperties().getTitle());
            flowTask.setTaskNodeId(nodeModel.getTaskNodeId());
            flowTask.setTaskId(nodeModel.getTaskId());
            flowTask.setCreatorTime(new Date());
            circulateList.add(flowTask);
        }
    }

    /**
     * 流程任务结束
     *
     * @param flowTask 流程任务
     */
    private boolean endround(FlowTaskEntity flowTask, ChildNodeList childNode, FlowModel flowModel)
            throws WorkFlowException {
        flowTask.setStatus(FlowTaskStatusEnum.Adopt.getCode());
        flowTask.setCompletion(100);
        flowTask.setEndTime(DateUtil.getNowDate());
        flowTask.setThisStepId(FlowNature.NodeEnd);
        flowTask.setThisStep("结束");
        // 结束事件
        FlowTaskOperatorRecordEntity operatorRecord = new FlowTaskOperatorRecordEntity();
        operatorRecord.setTaskId(flowTask.getId());
        operatorRecord.setHandleStatus(flowTask.getStatus());
        flowMsgUtil.event(2, childNode, operatorRecord, flowModel);
        flowTaskService.update(flowTask);
        FlowEngineEntity engine = flowEngineService.getInfo(flowTask.getFlowId());
        List<FlowTaskNodeEntity> taskNodeList = flowTaskNodeService.getList(flowTask.getId());
        // 发送消息
        FlowMsgModel flowMsgModel = new FlowMsgModel();
        flowMsgModel.setEnd(true);
        flowMsgModel.setCirculateList(new ArrayList<>());
        flowMsgModel.setNodeList(taskNodeList);
        flowMsgModel.setOperatorList(new ArrayList<>());
        flowMsgModel.setTaskEntity(flowTask);
        FlowTaskNodeEntity taskNodeEntity = new FlowTaskNodeEntity();
        taskNodeEntity.setNodePropertyJson(JsonUtils.toJSONString(childNode));
        flowMsgModel.setTaskNodeEntity(taskNodeEntity);
        flowMsgModel.setEngine(engine);
        flowMsgModel.setData(JsonUtils.toMap(flowTask.getFlowFormContentJson()));
        flowMsgUtil.message(flowMsgModel);
        // 子流程结束，触发主流程
        boolean isEnd = this.isNext(flowTask);
        return isEnd;
    }

    /**
     * 修改节点数据
     *
     * @param taskNodeLis
     */
    private void updateTaskNode(List<FlowTaskNodeEntity> taskNodeLis) {
        for (FlowTaskNodeEntity taskNodeLi : taskNodeLis) {
            UpdateWrapper<FlowTaskNodeEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(FlowTaskNodeEntity::getTaskId, taskNodeLi.getTaskId());
            updateWrapper.lambda().eq(FlowTaskNodeEntity::getNodeCode, taskNodeLi.getNodeCode());
            updateWrapper.lambda().ne(FlowTaskNodeEntity::getCompletion, -1);
            updateWrapper.lambda().set(FlowTaskNodeEntity::getNodeNext, taskNodeLi.getNodeNext());
            updateWrapper.lambda().set(FlowTaskNodeEntity::getSortCode, taskNodeLi.getSortCode());
            updateWrapper.lambda().set(FlowTaskNodeEntity::getState, taskNodeLi.getState());
            flowTaskNodeService.update(updateWrapper);
        }
    }

    // ---------------拒绝-------------------

    /**
     * 审批驳回节点
     *
     * @param nodeListAll 所有节点
     * @param taskNode 审批节点
     * @param isReject 是否驳回
     * @param thisStepAll 当前节点
     * @param rejectList 驳回节点
     * @param thisStepId 任务当前节点
     * @return
     */
    private List<FlowTaskNodeEntity> isUpAll(
            List<FlowTaskNodeEntity> nodeListAll,
            FlowTaskNodeEntity taskNode,
            boolean isReject,
            Set<FlowTaskNodeEntity> thisStepAll,
            List<String> rejectList,
            String[] thisStepId)
            throws WorkFlowException {
        List<FlowTaskNodeEntity> result = new ArrayList<>();
        List<String> thisStepIdAll = new ArrayList<>(Arrays.asList(thisStepId));
        if (isReject) {
            boolean isUp = FlowNature.UP.equals(taskNode.getNodeUp());
            if (FlowNature.START.equals(taskNode.getNodeUp())) {
                List<FlowTaskNodeEntity> startNode = nodeListAll.stream()
                        .filter(t -> FlowNature.NodeStart.equals(t.getNodeType()))
                        .toList();
                result.addAll(startNode);
                thisStepAll.addAll(result);
            } else if (FlowNature.UP.equals(taskNode.getNodeUp())) {
                List<FlowTaskNodeEntity> nodeList = nodeListAll.stream()
                        .filter(t -> StrUtil.isNotEmpty(t.getNodeNext())
                                && t.getNodeNext().contains(taskNode.getNodeCode()))
                        .toList();
                result.addAll(nodeList);
                for (FlowTaskNodeEntity taskNodeEntity : nodeList) {
                    List<String> next =
                            Arrays.asList(taskNodeEntity.getNodeNext().split(","));
                    thisStepIdAll.removeAll(next);
                }
                List<FlowTaskNodeEntity> stepId = nodeListAll.stream()
                        .filter(t -> thisStepIdAll.contains(t.getNodeCode()))
                        .toList();
                thisStepAll.addAll(result);
                thisStepAll.addAll(stepId);
            } else {
                List<FlowTaskNodeEntity> taskNodeList = new ArrayList<>();
                FlowTaskNodeEntity taskNodeEntity = nodeListAll.stream()
                        .filter(t -> t.getNodeCode().equals(taskNode.getNodeUp()))
                        .findFirst()
                        .orElse(null);
                if (taskNodeEntity != null) {
                    taskNodeList = nodeListAll.stream()
                            .filter(t -> t.getSortCode().equals(taskNodeEntity.getSortCode()))
                            .toList();
                }
                result.addAll(taskNodeList);
                thisStepAll.addAll(result);
            }
            result = result.stream()
                    .sorted(Comparator.comparing(FlowTaskNodeEntity::getSortCode)
                            .reversed())
                    .toList();
            boolean isChild = result.stream().anyMatch(t -> FlowNature.NodeSubFlow.equals(t.getNodeType()));
            if (isChild) {
                throw new WorkFlowException(MsgCode.WF114.get());
            }
            Long nodeSortCode = result.size() > 0
                    ? result.stream()
                            .min(Comparator.comparing(FlowTaskNodeEntity::getSortCode))
                            .get()
                            .getSortCode()
                    : 0L;
            if (isUp) {
                rejectList.addAll(result.stream().map(FlowTaskNodeEntity::getId).toList());
            } else {
                rejectList.addAll(result.stream()
                        .filter(t -> t.getSortCode() >= nodeSortCode)
                        .map(FlowTaskNodeEntity::getId)
                        .toList());
            }
        }
        return result;
    }

    /**
     * 拒绝比例
     *
     * @param taskNode 节点实体
     * @return
     */
    private boolean isReject(FlowTaskNodeEntity taskNode) {
        List<FlowTaskOperatorEntity> operatorList = flowTaskOperatorService.getList(taskNode.getTaskId()).stream()
                .filter(t -> t.getTaskNodeId().equals(taskNode.getId())
                        && FlowNodeEnum.Process.getCode().equals(t.getState()))
                .toList();
        ChildNodeList nodeModel = JsonUtils.toObject(taskNode.getNodePropertyJson(), ChildNodeList.class);
        Properties properties = nodeModel.getProperties();
        long pass = 100 - properties.getCountersignRatio();
        double total = operatorList.stream()
                .filter(t -> FlowNature.ParentId.equals(t.getParentId()))
                .count();
        List<FlowTaskOperatorEntity> passNumList = this.passNum(operatorList, FlowNature.RejectCompletion);
        double passNum = passNumList.size();
        return this.isCountersign(pass, total, passNum);
    }

    // -----------------------子节点---------------------------------

    /**
     * 插入数据
     *
     * @param engine 引擎
     * @param flowModel 提交数据
     */
    private Map<String, Object> createData(FlowEngineEntity engine, FlowTaskEntity taskEntity, FlowModel flowModel)
            throws WorkFlowException {
        Map<String, Object> resultData = flowModel.getFormData();
        try {
            Map<String, Object> data = flowModel.getFormData();
            if (FlowNature.CUSTOM.equals(engine.getFormType())) {
                List<TableModel> tableList = JsonUtils.toList(engine.getFlowTables(), TableModel.class);
                // 获取属性
                DbLinkEntity dbLink = serviceUtil.getDbLink(engine.getDbLinkId());
                FormDataModel formData = JsonUtils.toObject(taskEntity.getFlowForm(), FormDataModel.class);
                List<FieLdsModel> list = JsonUtils.toList(formData.getFields(), FieLdsModel.class);
                if (StrUtil.isNotEmpty(flowModel.getId())) {
                    // 更新
                    resultData = flowDataUtil.update(data, list, tableList, taskEntity.getProcessId(), dbLink);
                } else {
                    // 新增
                    resultData = flowDataUtil.create(
                            data, list, tableList, taskEntity.getProcessId(), new HashMap<>(16), dbLink);
                }
            } else {
                // 系统表单
                String dataAll = JsonUtils.toJSONString(data);
                if (engine.getType() != 1) {
                    String coed = engine.getEnCode();
                    this.formData(coed, flowModel.getProcessId(), dataAll);
                }
            }
        } catch (Exception e) {
            log.error("新增数据失败:{}", e.getMessage());
            throw new WorkFlowException("新增数据失败");
        }
        return resultData;
    }

    /**
     * 判断子流程是否全部走完，进行主流程任务
     *
     * @param flowTask 子流程任务
     * @throws WorkFlowException
     */
    private boolean isNext(FlowTaskEntity flowTask) throws WorkFlowException {
        boolean isEnd = true;
        // 子流程结束，触发主流程
        if (!FlowNature.ParentId.equals(flowTask.getParentId()) && StrUtil.isNotEmpty(flowTask.getParentId())) {
            isEnd = false;
            if (FlowNature.ChildSync.equals(flowTask.getIsAsync()) || ObjectUtil.isEmpty(flowTask.getIsAsync())) {
                List<FlowTaskEntity> parentList = flowTaskService.getChildList(
                        flowTask.getParentId(), FlowTaskEntity::getId, FlowTaskEntity::getThisStepId);
                // 判断子流程排除自己，判断其他子流程是否都完成
                boolean isNext = parentList.stream()
                                .filter(t -> !FlowNature.NodeEnd.equals(t.getThisStepId()))
                                .count()
                        == 0;
                if (isNext) {
                    FlowTaskEntity parentFlowTask = flowTaskService.getInfo(flowTask.getParentId());
                    List<FlowTaskNodeEntity> parentNodeAll = flowTaskNodeService.getList(parentFlowTask.getId());
                    FlowTaskOperatorEntity parentOperator = new FlowTaskOperatorEntity();
                    boolean isNode = this.updateTaskNode(parentList, parentNodeAll, parentOperator);
                    FlowModel parentModel = new FlowModel();
                    parentModel.setUserId("");
                    parentModel.setIsAsync(true);
                    Map<String, Object> data = new HashMap<>(16);
                    parentModel.setFormData(data);
                    if (isNode) {
                        this.audit(parentFlowTask, parentOperator, parentModel);
                    }
                }
            }
        }
        return isEnd;
    }

    /**
     * 子节点审批人
     *
     * @param childNode
     * @param taskEntity
     * @return
     */
    private List<UserEntity> childSaveList(ChildNodeList childNode, FlowTaskEntity taskEntity) {
        String createUserId = taskEntity.getCreatorUserId();
        Properties properties = childNode.getProperties();
        String type = properties.getInitiateType();
        List<String> userIdAll = new ArrayList<>();
        String userId = "";
        Date date = new Date();
        TaskOperatoUser taskOperatoUser = new TaskOperatoUser();
        taskOperatoUser.setDate(date);
        taskOperatoUser.setChildNode(childNode);
        // 发起者【部门主管】
        if (FlowTaskOperatorEnum.DepartmentCharge.getCode().equals(type)) {
            UserEntity userEntity = serviceUtil.getUserInfo(createUserId);
            if (userEntity != null) {
                OrganizeEntity organizeEntity = serviceUtil.getOrganizeInfo(userEntity.getOrganizeId());
                if (organizeEntity != null) {
                    userId = organizeEntity.getManager();
                    userIdAll.add(userId);
                }
            }
        }
        // 发起者【发起者主管】
        if (FlowTaskOperatorEnum.LaunchCharge.getCode().equals(type)) {
            // 时时查用户主管
            UserEntity info = serviceUtil.getUserInfo(createUserId);
            if (info != null) {
                userId = getManagerByLevel(info.getManagerId(), properties.getManagerLevel(), new ArrayList<>());
                userIdAll.add(userId);
            }
        }
        // 发起者【发起本人】
        if (FlowTaskOperatorEnum.InitiatorMe.getCode().equals(type)) {
            userIdAll.add(createUserId);
        }
        // 【变量】
        if (FlowTaskOperatorEnum.Variate.getCode().equals(type)) {
            Map<String, Object> dataAll = JsonUtils.toMap(taskEntity.getFlowFormContentJson());
            Object data = dataAll.get(properties.getFormField());
            if (data != null) {
                List<String> handleIdAll = new ArrayList<>();
                if (data instanceof List) {
                    handleIdAll.addAll((List) data);
                } else {
                    if (String.valueOf(data).contains("[")) {
                        handleIdAll.addAll(JsonUtils.toList(String.valueOf(data), String.class));
                    } else {
                        handleIdAll.addAll(Arrays.asList(String.valueOf(data).split(",")));
                    }
                }
                userIdAll.addAll(handleIdAll);
            }
        }
        // 【环节】
        if (FlowTaskOperatorEnum.Tache.getCode().equals(type)) {
            List<FlowTaskOperatorRecordEntity> operatorUserList =
                    flowTaskOperatorRecordService.getList(taskEntity.getId()).stream()
                            .filter(t -> properties.getNodeId().equals(t.getNodeCode())
                                    && FlowRecordEnum.audit.getCode().equals(t.getHandleStatus())
                                    && FlowNodeEnum.Process.getCode().equals(t.getStatus()))
                            .toList();
            List<String> handleId = operatorUserList.stream()
                    .map(FlowTaskOperatorRecordEntity::getHandleId)
                    .toList();
            userIdAll.addAll(handleId);
        }
        // 【服务】
        if (FlowTaskOperatorEnum.Serve.getCode().equals(type)) {
            String url = properties.getGetUserUrl()
                    + "?"
                    + taskNodeId
                    + "="
                    + childNode.getTaskNodeId()
                    + "&"
                    + taskId
                    + "="
                    + childNode.getTaskId();
            String token = UserProvider.getToken();
            JSONObject object = HttpUtil.httpRequest(url, "GET", null, token);
            if (object != null) {
                if (object.get("data") != null) {
                    JSONObject data = object.getJSONObject("data");
                    List<String> handleId = StrUtil.isNotEmpty(data.getString("handleId"))
                            ? Arrays.asList(data.getString("handleId").split(","))
                            : new ArrayList<>();
                    userIdAll.addAll(handleId);
                }
            }
        }
        // 发起者【指定用户】
        userIdAll.addAll(properties.getInitiator());
        // 发起者【指定岗位】
        List<String> positionList = properties.getInitiatePos();
        // 发起者【指定角色】
        List<String> roleList = properties.getInitiateRole();
        List<String> list = new ArrayList<>();
        list.addAll(positionList);
        list.addAll(roleList);
        List<UserRelationEntity> listByObjectIdAll = serviceUtil.getListByObjectIdAll(list);
        List<String> handleId =
                listByObjectIdAll.stream().map(UserRelationEntity::getUserId).toList();
        userIdAll.addAll(handleId);
        List<UserEntity> userList = serviceUtil.getUserName(userIdAll);
        if (userList.size() == 0) {
            UserEntity info = serviceUtil.getUserInfo(user);
            userList.add(info);
        }
        return userList;
    }

    /**
     * 赋值
     *
     * @param data 数据
     * @param engine 引擎
     * @param parentId 上一节点
     * @return
     */
    private FlowModel assignment(Map<String, Object> data, FlowEngineEntity engine, String parentId, String title) {
        FlowModel flowModel = new FlowModel();
        String billNo = "单据规则不存在";
        if (FlowNature.CUSTOM.equals(engine.getFormType())) {
            FormDataModel formData = JsonUtils.toObject(engine.getFormData(), FormDataModel.class);
            List<FieLdsModel> list = JsonUtils.toList(formData.getFields(), FieLdsModel.class);
            List<FormAllModel> formAllModel = new ArrayList<>();
            List<TableModel> tableModelList = JsonUtils.toList(engine.getFlowTables(), TableModel.class);
            RecursionForm recursionForm = new RecursionForm(list, tableModelList);
            FormCloumnUtil.recursionForm(recursionForm, formAllModel);
            List<FormAllModel> mastForm = formAllModel.stream()
                    .filter(t -> FormEnum.mast.getMessage().equals(t.getFlowKey()))
                    .toList();
            FormAllModel formModel = mastForm.stream()
                    .filter(t -> FlowKeyConsts.BILLRULE.equals(
                            t.getFormColumnModel().getFieLdsModel().getConfig().getFlowKey()))
                    .findFirst()
                    .orElse(null);
            try {
                if (formModel != null) {
                    FieLdsModel fieLdsModel = formModel.getFormColumnModel().getFieLdsModel();
                    String ruleKey = fieLdsModel.getConfig().getRule();
                    billNo = serviceUtil.getBillNumber(ruleKey);
                }
            } catch (Exception e) {
                LogUtils.error(e);
            }
        }
        flowModel.setFormData(data);
        flowModel.setParentId(parentId);
        flowModel.setProcessId(RandomUtil.uuId());
        flowModel.setBillNo(billNo);
        flowModel.setFlowTitle(title);
        return flowModel;
    }

    /**
     * 子表表单赋值
     *
     * @param engine 子表引擎
     * @param flowModel 提交数据
     * @param assignList 数据传递
     * @param isCustom true自定义表单 false系统表单
     * @return
     */
    private Map<String, Object> childData(
            FlowEngineEntity engine, FlowModel flowModel, List<FlowAssignModel> assignList, boolean isCustom) {
        Map<String, Object> result = new HashMap<>(16);
        if (engine != null) {
            Map<String, Object> formData = flowModel.getFormData();
            for (FlowAssignModel assignMode : assignList) {
                String childField = assignMode.getChildField();
                String parentField = assignMode.getParentField();
                result.put(childField, formData.get(parentField));
            }
        }
        return result;
    }

    /**
     * 递归删除子流程任务
     *
     * @param task 父节点流程任务
     */
    private void delChild(FlowTaskEntity task) {
        List<FlowTaskEntity> childTaskList = flowTaskService.getChildList(task.getId(), FlowTaskEntity::getId);
        for (FlowTaskEntity flowTask : childTaskList) {
            // 删除子流程
            flowTaskService.deleteChild(flowTask);
            this.delChild(flowTask);
        }
    }

    /**
     * 子流程完成了修改父节点的状态
     *
     * @param parentList 子流程
     * @param parentNodeAll 父流程
     */
    private boolean updateTaskNode(
            List<FlowTaskEntity> parentList,
            List<FlowTaskNodeEntity> parentNodeAll,
            FlowTaskOperatorEntity parentOperator) {
        Set<FlowTaskNodeEntity> taskNodeList = new HashSet<>();
        List<String> taskId = parentList.stream().map(FlowTaskEntity::getId).toList();
        for (FlowTaskNodeEntity nodeEntity : parentNodeAll) {
            ChildNodeList parentNode = JsonUtils.toObject(nodeEntity.getNodePropertyJson(), ChildNodeList.class);
            List<String> taskIdAll = parentNode.getCustom().getTaskId();
            boolean isNum = taskIdAll.stream().anyMatch(taskId::contains);
            if (isNum) {
                this.parentOperator(parentOperator, nodeEntity);
                taskNodeList.add(nodeEntity);
            }
        }
        for (FlowTaskNodeEntity taskNodeEntity : taskNodeList) {
            taskNodeEntity.setCompletion(FlowNature.AuditCompletion);
            flowTaskNodeService.update(taskNodeEntity);
        }
        return taskNodeList.size() > 0;
    }

    /**
     * 赋值审批数据
     *
     * @param parentOperator
     * @param nodeEntity
     */
    private void parentOperator(FlowTaskOperatorEntity parentOperator, FlowTaskNodeEntity nodeEntity) {
        parentOperator.setTaskNodeId(nodeEntity.getId());
        parentOperator.setDescription(JsonUtils.toJSONString(new ArrayList<>()));
        parentOperator.setNodeCode(nodeEntity.getNodeCode());
        parentOperator.setNodeName(nodeEntity.getNodeName());
        parentOperator.setTaskId(nodeEntity.getTaskId());
        parentOperator.setCompletion(FlowNature.ProcessCompletion);
    }

    // ----------------------撤回--------------------------

    /**
     * 递归获取加签人
     *
     * @param id
     * @param operatorList
     */
    private void getOperator(String id, Set<FlowTaskOperatorEntity> operatorList) {
        if (StrUtil.isNotEmpty(id)) {
            List<FlowTaskOperatorEntity> operatorListAll = flowTaskOperatorService.getParentId(id);
            for (FlowTaskOperatorEntity operatorEntity : operatorListAll) {
                operatorEntity.setState(FlowNodeEnum.Futility.getCode());
                operatorList.add(operatorEntity);
                this.getOperator(operatorEntity.getId(), operatorList);
            }
        }
    }

    // ---------------------公共方法--------------------------

    /**
     * 更新当前节点
     *
     * @param nextOperatorList 下一审批节点
     * @param flowTaskNodeList 所有节点
     * @param flowTask 流程任务
     */
    private boolean getNextStepId(
            List<ChildNodeList> nextOperatorList,
            List<FlowTaskNodeEntity> flowTaskNodeList,
            FlowTaskEntity flowTask,
            FlowModel flowModel)
            throws WorkFlowException {
        boolean isEnd = false;
        Set<String> delNodeList = new HashSet<>();
        List<String> progressList = new ArrayList<>();
        List<String> nextOperator = new ArrayList<>();
        ChildNodeList end = nextOperatorList.stream()
                .filter(t -> t.getCustom().getNodeId().contains(FlowNature.NodeEnd))
                .findFirst()
                .orElse(null);
        for (ChildNodeList childNode : nextOperatorList) {
            Properties properties = childNode.getProperties();
            String id = childNode.getCustom().getNodeId();
            String progress = properties.getProgress();
            List<FlowTaskNodeEntity> taskNodeList = flowTaskNodeList.stream()
                    .filter(t -> t.getNodeNext() != null)
                    .filter(t -> t.getNodeNext().contains(id))
                    .toList();
            List<String> nodeList =
                    taskNodeList.stream().map(FlowTaskNodeEntity::getNodeCode).toList();
            nextOperatorList.stream()
                    .filter(t -> t.getProperties().getProgress() != null)
                    .map(t -> t.getProperties().getProgress())
                    .toList();
            delNodeList.addAll(nodeList);
            nextOperator.add(id);
            if (StrUtil.isNotEmpty(progress)) {
                progressList.add(progress);
            }
        }
        String[] thisNode =
                flowTask.getThisStepId() != null ? flowTask.getThisStepId().split(",") : new String[] {};
        Set<String> thisStepId = new HashSet<>();
        for (String id : thisNode) {
            boolean isStepId = flowTaskNodeList.stream()
                    .anyMatch(
                            t -> t.getNodeCode().equals(id) && FlowNature.ProcessCompletion.equals(t.getCompletion()));
            if (isStepId) {
                thisStepId.add(id);
            }
        }
        thisStepId.removeAll(delNodeList);
        thisStepId.addAll(nextOperator);
        List<String> thisNodeName = new ArrayList<>();
        for (String id : thisStepId) {
            List<String> nodeList = flowTaskNodeList.stream()
                    .filter(t -> t.getNodeCode().equals(id))
                    .map(FlowTaskNodeEntity::getNodeName)
                    .toList();
            thisNodeName.addAll(nodeList);
        }
        flowTask.setThisStepId(String.join(",", thisStepId));
        flowTask.setThisStep(String.join(",", thisNodeName));
        Collections.sort(progressList);
        flowTask.setCompletion(progressList.size() > 0 ? Integer.valueOf(progressList.get(0)) : null);
        if (end != null) {
            isEnd = this.endround(flowTask, end, flowModel);
        }
        return isEnd;
    }

    /**
     * 审核记录
     *
     * @param record 审批实例
     * @param operatordModel 对象数据
     */
    private void operatorRecord(FlowTaskOperatorRecordEntity record, FlowOperatordModel operatordModel) {
        int status = operatordModel.getStatus();
        FlowModel flowModel = operatordModel.getFlowModel();
        String userId = operatordModel.getUserId();
        FlowTaskOperatorEntity operator = operatordModel.getOperator();
        String operatorId = operatordModel.getOperatorId();
        record.setHandleOpinion(flowModel.getHandleOpinion());
        record.setHandleId(userId);
        record.setHandleTime(new Date());
        record.setHandleStatus(status);
        record.setOperatorId(operatorId);
        record.setNodeCode(operator.getNodeCode());
        record.setNodeName(operator.getNodeName() != null ? operator.getNodeName() : "开始");
        record.setTaskOperatorId(operator.getId());
        record.setTaskNodeId(operator.getTaskNodeId());
        record.setTaskId(operator.getTaskId());
        record.setSignImg(flowModel.getSignImg());
        boolean freeApprover = !FlowNature.ParentId.equals(operator.getParentId());
        record.setStatus(freeApprover ? FlowNodeEnum.FreeApprover.getCode() : FlowNodeEnum.Process.getCode());
    }

    /**
     * 修改系统表单数据
     *
     * @param code 编码
     * @param id 主键id
     * @param data 数据
     * @throws WorkFlowException
     */
    private void formData(String code, String id, String data) throws WorkFlowException {
        Map<String, Object> objectData = JsonUtils.toMap(data);
        if (objectData.size() > 0) {
            try {
                Class[] types = new Class[] {String.class, String.class};
                Object[] datas = new Object[] {id, data};
                Object service = SpringContext.getBean(code + "ServiceImpl");
                ReflectionUtil.invokeMethod(service, "data", types, datas);
            } catch (Exception e) {
                log.error(MsgCode.WF119.get() + ":{}", e.getMessage());
                throw new WorkFlowException(MsgCode.WF119.get());
            }
        }
    }

    /**
     * 定时器
     *
     * @param taskOperator 流程经办
     * @param taskNodeList 所有流程节点
     * @param operatorList 下一流程经办
     * @return
     */
    private List<FlowTaskOperatorEntity> timer(
            FlowTaskOperatorEntity taskOperator,
            List<FlowTaskNodeEntity> taskNodeList,
            List<FlowTaskOperatorEntity> operatorList) {
        List<FlowTaskOperatorEntity> operatorListAll = new ArrayList<>();
        FlowTaskNodeEntity taskNode = taskNodeList.stream()
                .filter(t -> t.getId().equals(taskOperator.getTaskNodeId()))
                .findFirst()
                .orElse(null);
        if (taskNode != null) {
            // 获取其他分流的定时器
            List<String> nodeList = taskNodeList.stream()
                    .filter(t -> t.getSortCode().equals(taskNode.getSortCode()))
                    .map(FlowTaskNodeEntity::getId)
                    .toList();
            List<FlowTaskOperatorEntity> operatorAll = flowTaskOperatorService.getList(taskOperator.getTaskId());
            Set<Date> dateListAll = new HashSet<>();
            List<FlowTaskOperatorEntity> list = operatorAll.stream()
                    .filter(t -> nodeList.contains(t.getTaskNodeId()))
                    .toList();
            for (FlowTaskOperatorEntity operator : list) {
                if (StrUtil.isNotEmpty(operator.getDescription())) {
                    List<Date> dateList = JsonUtils.toList(operator.getDescription(), Date.class);
                    dateListAll.addAll(dateList);
                }
            }
            // 获取单前审批定时器
            if (StrUtil.isNotEmpty(taskOperator.getDescription())) {
                List<Date> date = JsonUtils.toList(taskOperator.getDescription(), Date.class);
                dateListAll.addAll(date);
            }
            for (FlowTaskOperatorEntity operator : operatorList) {
                operator.setDescription(JsonUtils.toJSONString(dateListAll));
                operatorListAll.add(operator);
            }
        }
        return operatorListAll;
    }

    /**
     * 验证必填
     *
     * @param nodeModel 节点
     * @param formData
     * @return
     */
    private boolean requestData(ChildNodeList nodeModel, Map<String, Object> formData) {
        List<FormOperates> formOperates = nodeModel.getProperties().getFormOperates().stream()
                .filter(FormOperates::isRequired)
                .toList();
        boolean flag = false;
        for (FormOperates formOperate : formOperates) {
            String model = formOperate.getId();
            Object data = formData.get(model);
            if (ObjectUtil.isEmpty(data)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 更新节点数据
     *
     * @param flowTask 任务
     * @param childNodeAll 工作流对象
     * @param nodeListAll 所有节点
     * @param conditionListAll 所有条件
     * @param taskNodeList 节点数据
     */
    private void updateNodeList(
            FlowTaskEntity flowTask,
            ChildNode childNodeAll,
            List<ChildNodeList> nodeListAll,
            List<ConditionList> conditionListAll,
            List<FlowTaskNodeEntity> taskNodeList) {
        FlowJsonUtil.getTemplateAll(childNodeAll, nodeListAll, conditionListAll);
        this.createNodeList(flowTask, nodeListAll, conditionListAll, taskNodeList);
    }
}
