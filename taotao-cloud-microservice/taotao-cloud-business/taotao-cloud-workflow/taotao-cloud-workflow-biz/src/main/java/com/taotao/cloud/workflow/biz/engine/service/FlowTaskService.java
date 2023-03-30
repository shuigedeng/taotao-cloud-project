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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowBatchModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskListModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.PaginationFlowTask;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import java.util.List;

/**
 * 流程任务
 *
 * @author shuigedeng
 * @version 2023.01
 * @since 2023-01-17 14:33:11
 */
public interface FlowTaskService extends IService<FlowTaskEntity> {

    /**
     * 列表（流程监控）
     *
     * @param paginationFlowTask
     * @return {@link List }<{@link FlowTaskEntity }>
     * @since 2023-01-17 14:33:11
     */
    List<FlowTaskEntity> getMonitorList(PaginationFlowTask paginationFlowTask);

    /**
     * 列表（我发起的）
     *
     * @param paginationFlowTask
     * @return {@link List }<{@link FlowTaskEntity }>
     * @since 2023-01-17 14:33:11
     */
    List<FlowTaskEntity> getLaunchList(PaginationFlowTask paginationFlowTask);

    /**
     * 列表（待我审批）
     *
     * @param paginationFlowTask
     * @return {@link IPage }<{@link FlowTaskListModel }>
     * @since 2023-01-17 14:33:11
     */
    IPage<FlowTaskListModel> getWaitList(PaginationFlowTask paginationFlowTask);

    /**
     * 批量列表（待我审批）
     *
     * @param paginationFlowTask
     * @return {@link IPage }<{@link FlowTaskListModel }>
     * @since 2023-01-17 14:33:11
     */
    IPage<FlowTaskListModel> getBatchWaitList(PaginationFlowTask paginationFlowTask);

    /**
     * 列表（待我审批）
     *
     * @param paginationFlowTask
     * @param paginationFlowTask
     * @return
     */
    List<FlowTaskListModel> getWaitListAll(PaginationFlowTask paginationFlowTask);

    /**
     * 列表（我已审批）
     *
     * @return
     */
    List<FlowTaskListModel> getTrialList();

    /**
     * 列表（待我审批）
     *
     * @return
     */
    List<FlowTaskEntity> getWaitList();

    /**
     * 列表（待我审批） 门户专用
     *
     * @return
     */
    List<FlowTaskEntity> getDashboardWaitList();

    /**
     * 列表（待我审批）
     *
     * @return
     */
    List<FlowTaskEntity> getAllWaitList();

    /**
     * 列表（待我审批） 门户专用
     *
     * @return
     */
    List<FlowTaskEntity> getDashboardAllWaitList();

    /**
     * 列表（我已审批）
     *
     * @param paginationFlowTask
     * @return
     */
    IPage<FlowTaskListModel> getTrialList(PaginationFlowTask paginationFlowTask);

    /**
     * 列表（抄送我的）
     *
     * @param paginationFlowTask
     * @return
     */
    IPage<FlowTaskListModel> getCirculateList(PaginationFlowTask paginationFlowTask);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     * @throws WorkFlowException 异常
     */
    FlowTaskEntity getInfo(String id);

    /**
     * 更新
     *
     * @param entity 主键值
     * @return
     */
    void update(FlowTaskEntity entity);

    /**
     * 创建
     *
     * @param entity 主键值
     * @return
     */
    void create(FlowTaskEntity entity);

    /**
     * 信息
     *
     * @param id 主键值
     * @param columns 指定获取的列数据 , 任务中存了三个JSON数据 ， 排除后可以提高查询速度
     * @return
     */
    FlowTaskEntity getInfoSubmit(String id, SFunction<FlowTaskEntity, ?>... columns);

    /**
     * 信息
     *
     * @param id 主键值
     * @param columns 指定获取的列数据 , 任务中存了三个JSON数据 ， 排除后可以提高查询速度
     * @return
     */
    List<FlowTaskEntity> getInfosSubmit(String[] ids, SFunction<FlowTaskEntity, ?>... columns);

    /**
     * 删除
     *
     * @param entity 实体对象
     * @throws WorkFlowException 异常
     */
    void delete(FlowTaskEntity entity) throws WorkFlowException;

    /**
     * 删除
     *
     * @param entity 实体对象
     * @throws WorkFlowException 异常
     */
    void deleteChild(FlowTaskEntity entity);

    /**
     * 批量删除流程
     *
     * @param ids
     */
    void delete(String[] ids) throws WorkFlowException;

    /**
     * 通过流程引擎id获取流程列表
     *
     * @param id
     * @return
     */
    List<FlowTaskEntity> getTaskList(String id);

    /**
     * 查询订单状态
     *
     * @param id
     * @return
     */
    List<FlowTaskEntity> getOrderStaList(List<String> id);

    /**
     * 查询子流程
     *
     * @param id
     * @return
     */
    List<FlowTaskEntity> getChildList(String id, SFunction<FlowTaskEntity, ?>... columns);

    /**
     * 通过流程引擎id获取流程列表
     *
     * @param flowId
     * @return
     */
    List<FlowTaskEntity> getTaskFlowList(String flowId);

    /**
     * 查询流程列表
     *
     * @param flowId
     * @return
     */
    List<FlowTaskEntity> getFlowList(String flowId);

    /**
     * 批量审批引擎
     *
     * @return
     */
    List<FlowBatchModel> batchFlowSelector();
}
