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

import com.taotao.boot.common.utils.json.JacksonUtils;
import com.taotao.cloud.workflow.api.vo.UserEntity;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.FuncConfig;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.MsgConfig;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.Properties;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.TemplateJsonModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.ChildNodeList;
import com.taotao.cloud.workflow.biz.common.model.engine.flowmessage.FlowEventModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowmessage.FlowMessageModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowmessage.FlowMsgModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowContModel;
import com.taotao.cloud.workflow.biz.common.util.UserProvider;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskCirculateEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskNodeEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorRecordEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowMessageEnum;
import com.taotao.cloud.workflow.biz.engine.enums.FlowTaskStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowDelegateService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** */
@Component
public class FlowMsgUtil {

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private SentMessageUtil sentMessageUtil;

    @Autowired
    private ServiceAllUtil serviceUtil;

    @Autowired
    private FlowDelegateService flowDelegateService;

    /**
     * 发送消息
     *
     * @param flowMsgModel
     */
    public void message(FlowMsgModel flowMsgModel) {
        Map<String, Object> data = flowMsgModel.getData();
        List<SentMessageForm> messageListAll = new ArrayList<>();
        FlowTaskEntity taskEntity = flowMsgModel.getTaskEntity();
        List<String> creatorUserId = new ArrayList<>();
        if (taskEntity != null) {
            creatorUserId.add(taskEntity.getCreatorUserId());
        }
        FlowTaskNodeEntity taskNodeEntity = flowMsgModel.getTaskNodeEntity();
        FlowEngineEntity engine = flowMsgModel.getEngine();
        List<FlowTaskNodeEntity> nodeList = flowMsgModel.getNodeList();
        List<FlowTaskOperatorEntity> operatorList = flowMsgModel.getOperatorList();
        List<FlowTaskCirculateEntity> circulateList = flowMsgModel.getCirculateList();
        FlowTaskNodeEntity startNode = nodeList.stream()
                .filter(t -> FlowNature.NodeStart.equals(t.getNodeType()))
                .findFirst()
                .orElse(null);
        FlowTaskOperatorRecordEntity recordEntity = new FlowTaskOperatorRecordEntity();
        recordEntity.setTaskId(startNode != null ? startNode.getTaskId() : "");
        // 等待
        if (flowMsgModel.isWait()) {
            ChildNodeList childNode = JacksonUtils.getJsonToBean(startNode.getNodePropertyJson(), ChildNodeList.class);
            Properties properties = childNode.getProperties();
            MsgConfig taskMsgConfig = properties.getWaitMsgConfig();
            Map<String, List<FlowTaskOperatorEntity>> operatorMap =
                    operatorList.stream().collect(Collectors.groupingBy(FlowTaskOperatorEntity::getTaskNodeId));
            for (String key : operatorMap.keySet()) {
                recordEntity.setTaskNodeId(key);
                List<SentMessageForm> messageList = new ArrayList<>();
                List<FlowTaskOperatorEntity> taskOperatorList = operatorMap.get(key);
                FlowMessageModel messageModel = new FlowMessageModel();
                messageModel.setData(data);
                messageModel.setMsgConfig(taskMsgConfig);
                messageModel.setRecordEntity(recordEntity);
                messageModel.setStatus(taskEntity.getStatus());
                messageModel.setFullName(taskEntity.getFullName());
                this.messageModel(taskOperatorList, engine, messageModel);
                this.setMessageList(messageList, messageModel);
                messageListAll.addAll(messageList);
                for (FlowTaskOperatorEntity operator : taskOperatorList) {
                    List<SentMessageForm> delegationMsg = this.delegationMsg(operator, messageModel, engine);
                    messageListAll.addAll(delegationMsg);
                }
            }
        }
        // 结束
        if (flowMsgModel.isEnd()) {
            // 发起人
            ChildNodeList childNode = JacksonUtils.getJsonToBean(startNode.getNodePropertyJson(), ChildNodeList.class);
            Properties properties = childNode.getProperties();
            MsgConfig msgConfig = properties.getEndMsgConfig();
            List<SentMessageForm> messageList = new ArrayList<>();
            FlowMessageModel messageModel = new FlowMessageModel();
            messageModel.setData(data);
            messageModel.setTitle("已【结束】");
            messageModel.setMsgConfig(msgConfig);
            messageModel.setType(FlowMessageEnum.me.getCode());
            messageModel.setRecordEntity(recordEntity);
            messageModel.setStatus(taskEntity.getStatus());
            messageModel.setFullName(taskEntity.getFullName());
            List<FlowTaskOperatorEntity> taskOperatorList = new ArrayList() {
                {
                    FlowTaskOperatorEntity operatorEntity = new FlowTaskOperatorEntity();
                    operatorEntity.setTaskId(childNode.getTaskId());
                    operatorEntity.setTaskNodeId(childNode.getTaskNodeId());
                    operatorEntity.setHandleId(taskEntity.getCreatorUserId());
                    add(operatorEntity);
                }
            };
            this.messageModel(taskOperatorList, engine, messageModel);
            this.setMessageList(messageList, messageModel);
            messageListAll.addAll(messageList);
        }
        // 同意
        if (flowMsgModel.isApprove()) {
            ChildNodeList childNode = JacksonUtils.getJsonToBean(startNode.getNodePropertyJson(), ChildNodeList.class);
            Properties properties = childNode.getProperties();
            MsgConfig msgConfig = properties.getApproveMsgConfig();
            Map<String, List<FlowTaskOperatorEntity>> operatorMap =
                    operatorList.stream().collect(Collectors.groupingBy(FlowTaskOperatorEntity::getTaskNodeId));
            for (String key : operatorMap.keySet()) {
                recordEntity.setTaskNodeId(key);
                // 默认获取当前节点
                FlowTaskNodeEntity taskNode = nodeList.stream()
                        .filter(t -> t.getId().equals(key))
                        .findFirst()
                        .orElse(null);
                ChildNodeList taskChildNode =
                        JacksonUtils.getJsonToBean(taskNode.getNodePropertyJson(), ChildNodeList.class);
                Properties taskProperties = taskChildNode.getProperties();
                MsgConfig taskMsgConfig = taskProperties.getApproveMsgConfig();
                if (taskMsgConfig.getOn() == 2) {
                    taskMsgConfig = msgConfig;
                }
                List<SentMessageForm> messageList = new ArrayList<>();
                List<FlowTaskOperatorEntity> taskOperatorList = operatorMap.get(key);
                FlowMessageModel messageModel = new FlowMessageModel();
                messageModel.setData(data);
                messageModel.setTitle("已被【同意】");
                messageModel.setMsgConfig(taskMsgConfig);
                messageModel.setRecordEntity(recordEntity);
                messageModel.setStatus(taskEntity.getStatus());
                messageModel.setFullName(taskEntity.getFullName());
                this.messageModel(taskOperatorList, engine, messageModel);
                this.setMessageList(messageList, messageModel);
                messageListAll.addAll(messageList);
            }
        }
        // 拒绝
        if (flowMsgModel.isReject()) {
            ChildNodeList childNode = JacksonUtils.getJsonToBean(startNode.getNodePropertyJson(), ChildNodeList.class);
            Properties properties = childNode.getProperties();
            MsgConfig msgConfig = properties.getRejectMsgConfig();
            Map<String, List<FlowTaskOperatorEntity>> operatorMap =
                    operatorList.stream().collect(Collectors.groupingBy(FlowTaskOperatorEntity::getTaskNodeId));
            for (String key : operatorMap.keySet()) {
                recordEntity.setTaskNodeId(key);
                // 默认获取当前节点
                FlowTaskNodeEntity taskNode = nodeList.stream()
                        .filter(t -> t.getId().equals(key))
                        .findFirst()
                        .orElse(null);
                ChildNodeList taskChildNode =
                        JacksonUtils.getJsonToBean(taskNode.getNodePropertyJson(), ChildNodeList.class);
                Properties taskProperties = taskChildNode.getProperties();
                MsgConfig taskMsgConfig = taskProperties.getRejectMsgConfig();
                if (taskMsgConfig.getOn() == 2) {
                    taskMsgConfig = msgConfig;
                }
                List<SentMessageForm> messageList = new ArrayList<>();
                List<FlowTaskOperatorEntity> taskOperatorList = operatorMap.get(key);
                FlowMessageModel messageModel = new FlowMessageModel();
                messageModel.setData(data);
                messageModel.setTitle("已被【拒绝】");
                messageModel.setMsgConfig(taskMsgConfig);
                messageModel.setRecordEntity(recordEntity);
                messageModel.setStatus(taskEntity.getStatus());
                messageModel.setFullName(taskEntity.getFullName());
                this.messageModel(taskOperatorList, engine, messageModel);
                this.setMessageList(messageList, messageModel);
                messageListAll.addAll(messageList);
            }
        }
        // 抄送
        if (flowMsgModel.isCopy()) {
            ChildNodeList childNode = JacksonUtils.getJsonToBean(startNode.getNodePropertyJson(), ChildNodeList.class);
            Properties properties = childNode.getProperties();
            MsgConfig msgConfig = properties.getCopyMsgConfig();
            Map<String, List<FlowTaskCirculateEntity>> circulateMap =
                    circulateList.stream().collect(Collectors.groupingBy(FlowTaskCirculateEntity::getTaskNodeId));
            for (String key : circulateMap.keySet()) {
                recordEntity.setTaskNodeId(key);
                // 默认获取当前节点
                FlowTaskNodeEntity taskNode = nodeList.stream()
                        .filter(t -> t.getId().equals(key))
                        .findFirst()
                        .orElse(null);
                ChildNodeList taskChildNode =
                        JacksonUtils.getJsonToBean(taskNode.getNodePropertyJson(), ChildNodeList.class);
                Properties taskProperties = taskChildNode.getProperties();
                MsgConfig taskMsgConfig = taskProperties.getCopyMsgConfig();
                if (taskMsgConfig.getOn() == 2) {
                    taskMsgConfig = msgConfig;
                }
                List<SentMessageForm> messageList = new ArrayList<>();
                List<FlowTaskOperatorEntity> taskOperatorList = new ArrayList<>();
                for (FlowTaskCirculateEntity circulateEntity : circulateMap.get(key)) {
                    FlowTaskOperatorEntity operatorEntity =
                            JacksonUtils.getJsonToBean(circulateEntity, FlowTaskOperatorEntity.class);
                    operatorEntity.setHandleId(circulateEntity.getObjectId());
                    taskOperatorList.add(operatorEntity);
                }
                FlowMessageModel messageModel = new FlowMessageModel();
                messageModel.setData(data);
                messageModel.setTitle("已被【抄送】");
                messageModel.setMsgConfig(taskMsgConfig);
                messageModel.setRecordEntity(recordEntity);
                messageModel.setStatus(taskEntity.getStatus());
                messageModel.setType(FlowMessageEnum.circulate.getCode());
                messageModel.setFullName(taskEntity.getFullName());
                this.messageModel(taskOperatorList, engine, messageModel);
                this.setMessageList(messageList, messageModel);
                messageListAll.addAll(messageList);
            }
        }
        // 子流程
        if (flowMsgModel.isLaunch()) {
            ChildNodeList childNode = JacksonUtils.getJsonToBean(startNode.getNodePropertyJson(), ChildNodeList.class);
            Properties properties = childNode.getProperties();
            MsgConfig msgConfig = properties.getLaunchMsgConfig();
            Map<String, List<FlowTaskOperatorEntity>> operatorMap =
                    operatorList.stream().collect(Collectors.groupingBy(FlowTaskOperatorEntity::getTaskNodeId));
            for (String key : operatorMap.keySet()) {
                recordEntity.setTaskNodeId(key);
                // 默认获取当前节点
                FlowTaskNodeEntity taskNode = nodeList.stream()
                        .filter(t -> t.getId().equals(key))
                        .findFirst()
                        .orElse(null);
                ChildNodeList taskChildNode =
                        JacksonUtils.getJsonToBean(taskNode.getNodePropertyJson(), ChildNodeList.class);
                Properties taskProperties = taskChildNode.getProperties();
                MsgConfig taskMsgConfig = taskProperties.getLaunchMsgConfig();
                if (taskMsgConfig.getOn() == 2) {
                    taskMsgConfig = msgConfig;
                }
                List<SentMessageForm> messageList = new ArrayList<>();
                List<FlowTaskOperatorEntity> taskOperatorList = operatorMap.get(key);
                FlowMessageModel messageModel = new FlowMessageModel();
                messageModel.setData(data);
                messageModel.setTitle("请发起【子流程】");
                messageModel.setMsgConfig(taskMsgConfig);
                messageModel.setRecordEntity(recordEntity);
                messageModel.setType(FlowMessageEnum.me.getCode());
                messageModel.setStatus(FlowTaskStatusEnum.Draft.getCode());
                messageModel.setFullName(taskEntity.getFullName());
                this.messageModel(taskOperatorList, engine, messageModel);
                this.setMessageList(messageList, messageModel);
                messageListAll.addAll(messageList);
            }
        }
        // 发起人
        if (flowMsgModel.isStart()) {
            List<SentMessageForm> messageList = new ArrayList<>();
            FlowMessageModel meModel = new FlowMessageModel();
            meModel.setData(data);
            meModel.setTitle("已被【拒绝】");
            meModel.setRecordEntity(recordEntity);
            meModel.setStatus(taskEntity.getStatus());
            meModel.setType(FlowMessageEnum.me.getCode());
            meModel.setFullName(taskEntity.getFullName());
            List<FlowTaskOperatorEntity> meOperatorList = new ArrayList() {
                {
                    FlowTaskOperatorEntity operatorEntity = new FlowTaskOperatorEntity();
                    operatorEntity.setTaskId(taskNodeEntity.getTaskId());
                    operatorEntity.setHandleId(taskEntity.getCreatorUserId());
                    add(operatorEntity);
                }
            };
            this.messageModel(meOperatorList, engine, meModel);
            this.setMessageList(messageList, meModel);
            messageListAll.addAll(messageList);
        }
        for (SentMessageForm messageForm : messageListAll) {
            messageForm.setSysMessage(true);
            sentMessageUtil.sendMessage(messageForm);
        }
    }

    /**
     * 封装站内信消息
     *
     * @param taskOperatorList
     * @param engine
     * @param messageModel
     */
    private void messageModel(
            List<FlowTaskOperatorEntity> taskOperatorList, FlowEngineEntity engine, FlowMessageModel messageModel) {
        List<String> userList = new ArrayList<>();
        Map<String, String> contMsg = new HashMap<>();
        for (FlowTaskOperatorEntity taskOperator : taskOperatorList) {
            FlowContModel contModel = this.flowMessage(engine, taskOperator, messageModel);
            contMsg.put(taskOperator.getHandleId(), JacksonUtils.getObjectToString(contModel));
            userList.add(taskOperator.getHandleId());
        }
        messageModel.setUserList(userList);
        messageModel.setContMsg(contMsg);
    }

    /**
     * 封装站内信对象
     *
     * @param engine
     * @param taskOperator
     * @return
     */
    private FlowContModel flowMessage(
            FlowEngineEntity engine, FlowTaskOperatorEntity taskOperator, FlowMessageModel messageModel) {
        FlowContModel contModel = new FlowContModel();
        contModel.setEnCode(engine.getEnCode());
        contModel.setFlowId(engine.getId());
        contModel.setFormType(engine.getFormType());
        contModel.setTaskNodeId(taskOperator.getTaskNodeId());
        contModel.setTaskOperatorId(taskOperator.getId());
        contModel.setProcessId(taskOperator.getTaskId());
        contModel.setType(messageModel.getType());
        contModel.setStatus(messageModel.getStatus());
        return contModel;
    }

    /**
     * 整合发送消息
     *
     * @param messageList
     * @param flowMessageModel
     */
    private void setMessageList(List<SentMessageForm> messageList, FlowMessageModel flowMessageModel) {
        Map<String, Object> data = flowMessageModel.getData();
        MsgConfig msgConfig =
                flowMessageModel.getMsgConfig() != null ? flowMessageModel.getMsgConfig() : new MsgConfig();
        List<String> userList = flowMessageModel.getUserList();
        FlowTaskOperatorRecordEntity recordEntity = flowMessageModel.getRecordEntity();
        String templateId = msgConfig.getOn() == 0 ? "0" : msgConfig.getMsgId();
        List<TemplateJsonModel> templateJson =
                msgConfig.getTemplateJson() != null ? msgConfig.getTemplateJson() : new ArrayList<>();
        SentMessageForm messageModel = new SentMessageForm();
        messageModel.setTemplateId(templateId);
        messageModel.setToUserIds(userList);
        Map<String, String> parameterMap = new HashMap<>();
        for (TemplateJsonModel templateJsonModel : templateJson) {
            String fieldId = templateJsonModel.getField();
            String relationField = templateJsonModel.getRelationField();
            String dataJson = data.get(relationField) != null ? String.valueOf(data.get(relationField)) : "";
            FlowEventModel eventModel = FlowEventModel.builder()
                    .dataJson(dataJson)
                    .record(recordEntity)
                    .relationField(relationField)
                    .build();
            dataJson = this.data(eventModel);
            parameterMap.put(fieldId, dataJson);
        }
        messageModel.setParameterMap(parameterMap);
        messageModel.setContentMsg(flowMessageModel.getContMsg());
        messageModel.setTitle(flowMessageModel.getFullName() + flowMessageModel.getTitle());
        messageList.add(messageModel);
    }

    /**
     * @return
     */
    public String data(FlowEventModel eventModel) {
        FlowTaskOperatorRecordEntity record = eventModel.getRecord();
        String relationField = eventModel.getRelationField();
        String dataJson = eventModel.getDataJson();
        String userId = userProvider.get().getUserId();
        String value = dataJson;
        FlowTaskEntity taskEntity = flowTaskService.getInfoSubmit(
                record.getTaskId(),
                FlowTaskEntity::getFlowId,
                FlowTaskEntity::getFlowName,
                FlowTaskEntity::getFullName,
                FlowTaskEntity::getCreatorUserId);
        switch (relationField) {
            case "flowFlowId":
                value = taskEntity.getFlowId();
                break;
            case "flowTaskNodeId":
                value = record.getTaskNodeId();
                break;
            case "flowFlowFullName":
                value = taskEntity.getFlowName();
                break;
            case "flowTaskFullName":
                value = taskEntity.getFullName();
                break;
            case "flowLaunchUserId":
                value = taskEntity.getCreatorUserId();
                break;
            case "flowLaunchUserName":
                UserEntity createUser =
                        taskEntity != null ? serviceUtil.getUserInfo(taskEntity.getCreatorUserId()) : null;
                value = createUser != null ? createUser.getRealName() : "";
                break;
            case "flowFlowOperatorUserId":
                value = userId;
                break;
            case "flowFlowOperatorUserName":
                UserEntity userEntity = serviceUtil.getUserInfo(userId);
                value = userEntity != null ? userEntity.getRealName() : "";
                break;
            default:
                break;
        }
        return value;
    }

    // --------------------------------------------事件处理---------------------------------------------------------

    /**
     * 流程事件
     *
     * @param status 事件状态 1.发起 2.结束 3.发起撤回 4同意 5拒绝 6节点撤回
     * @param childNode 节点数据
     * @param record 审批数据
     */
    public void event(
            Integer status, ChildNodeList childNode, FlowTaskOperatorRecordEntity record, FlowModel flowModel) {
        boolean on = false;
        String interId = "";
        List<TemplateJsonModel> templateJsonModelList = new ArrayList<>();
        FuncConfig config = null;
        // 属性
        if (childNode != null) {
            Properties properties = childNode.getProperties();
            switch (status) {
                case 1:
                    config = properties.getInitFuncConfig();
                    break;
                case 2:
                    config = properties.getEndFuncConfig();
                    break;
                case 3:
                    config = properties.getFlowRecallFuncConfig();
                    break;
                case 4:
                    config = properties.getApproveFuncConfig();
                    break;
                case 5:
                    config = properties.getRejectFuncConfig();
                    break;
                case 6:
                    config = properties.getRecallFuncConfig();
                    break;
                default:
                    break;
            }
        }
        if (config != null) {
            on = config.isOn();
            interId = config.getInterfaceId();
            templateJsonModelList = config.getTemplateJson();
        }
        if (on && StrUtil.isNotEmpty(interId)) {
            Map<String, Object> data = flowModel.getFormData();
            Map<String, String> parameterMap = new HashMap<>();
            for (TemplateJsonModel templateJsonModel : templateJsonModelList) {
                String fieldId = templateJsonModel.getField();
                String relationField = templateJsonModel.getRelationField();
                String dataJson = data.get(relationField) != null ? String.valueOf(data.get(relationField)) : "";
                FlowEventModel eventModel = FlowEventModel.builder()
                        .dataJson(dataJson)
                        .record(record)
                        .relationField(relationField)
                        .build();
                dataJson = data(eventModel);
                parameterMap.put(fieldId, "'" + dataJson + "'");
            }
            serviceUtil.infoToId(interId, parameterMap);
        }
    }

    /**
     * 封装委托消息
     *
     * @param operator
     * @param messageModel
     * @param engine
     * @return
     */
    private List<SentMessageForm> delegationMsg(
            FlowTaskOperatorEntity operator, FlowMessageModel messageModel, FlowEngineEntity engine) {
        List<SentMessageForm> messageList = new ArrayList<>();
        FlowTaskEntity taskEntity = flowTaskService.getInfoSubmit(operator.getTaskId(), FlowTaskEntity::getFlowId);
        List<String> userList =
                flowDelegateService.getUser(null, taskEntity.getFlowId(), operator.getHandleId()).stream()
                        .map(t -> t.getFTouserid())
                        .toList();
        List<FlowTaskOperatorEntity> taskOperatorList = new ArrayList<>();
        for (String user : userList) {
            FlowTaskOperatorEntity delegaOperator = JacksonUtils.getJsonToBean(operator, FlowTaskOperatorEntity.class);
            delegaOperator.setHandleId(user);
            taskOperatorList.add(delegaOperator);
        }
        this.messageModel(taskOperatorList, engine, messageModel);
        this.setMessageList(messageList, messageModel);
        return messageList;
    }
}
