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

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import java.util.List;
import java.util.Set;

/** 流程经办 */
public interface FlowTaskOperatorService extends IService<FlowTaskOperatorEntity> {

    /**
     * 列表
     *
     * @param taskId 流程实例Id
     * @return
     */
    List<FlowTaskOperatorEntity> getList(String taskId);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    FlowTaskOperatorEntity getInfo(String id);

    /**
     * 信息
     *
     * @param taskId 实例Id
     * @param nodeNo 节点编码
     * @return
     */
    FlowTaskOperatorEntity getInfo(String taskId, String nodeNo);

    /**
     * 删除（根据实例Id）
     *
     * @param taskId 任务主键
     */
    void deleteByTaskId(String taskId);

    /**
     * 删除
     *
     * @param nodeId 节点主键
     */
    void deleteByNodeId(String nodeId);

    /**
     * 创建
     *
     * @param entitys 实体对象
     */
    void create(List<FlowTaskOperatorEntity> entitys);

    /**
     * 更新
     *
     * @param entity 实体对象
     */
    void update(FlowTaskOperatorEntity entity);

    /**
     * 更新会签委托人的审核状态
     *
     * @param taskNodeId 流程节点id
     * @param userId 委托人id
     * @param completion 审批状态
     */
    void update(String taskNodeId, List<String> userId, String completion);

    /**
     * 更新流程经办审核状态
     *
     * @param taskNodeId 流程节点id
     * @param type 流程类型 会签、或签
     */
    void update(String taskNodeId, String type);

    /**
     * 更新驳回流程节点
     *
     * @param taskId 流程id
     */
    void update(String taskId);

    /**
     * 经办未审核人员
     *
     * @param taskId 任务id
     * @return
     */
    List<FlowTaskOperatorEntity> press(String taskId);

    /**
     * 驳回的节点之后的节点作废
     *
     * @param taskId
     * @param taskNodeId
     */
    void updateReject(String taskId, Set<String> taskNodeId);

    /**
     * 删除经办id
     *
     * @param idAll 经办id
     */
    void deleteList(List<String> idAll);

    /**
     * 查询加签人信息
     *
     * @param parentId 父节点Id
     * @return
     */
    List<FlowTaskOperatorEntity> getParentId(String parentId);

    /**
     * 更新经办记录作废
     *
     * @param idAll
     */
    void updateTaskOperatorState(List<String> idAll);

    /**
     * 获取自己代办的流程任务
     *
     * @return
     */
    List<FlowTaskOperatorEntity> getBatchList();
}
