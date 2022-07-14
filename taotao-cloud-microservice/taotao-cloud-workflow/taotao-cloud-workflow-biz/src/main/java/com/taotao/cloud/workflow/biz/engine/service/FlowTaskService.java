package com.taotao.cloud.workflow.biz.engine.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.model.flowbefore.FlowBatchModel;
import com.taotao.cloud.workflow.biz.engine.model.flowtask.FlowTaskListModel;
import com.taotao.cloud.workflow.biz.engine.model.flowtask.PaginationFlowTask;

import java.util.List;

/**
 * 流程任务
 */
public interface FlowTaskService extends IService<FlowTaskEntity> {

    /**
     * 列表（流程监控）
     *
     * @param paginationFlowTask
     * @return
     */
    List<FlowTaskEntity> getMonitorList(PaginationFlowTask paginationFlowTask);

    /**
     * 列表（我发起的）
     *
     * @param paginationFlowTask
     * @return
     */
    List<FlowTaskEntity> getLaunchList(PaginationFlowTask paginationFlowTask);

    /**
     * 列表（待我审批）
     *
     * @param paginationFlowTask
     * @return
     */
    List<FlowTaskListModel> getWaitList(PaginationFlowTask paginationFlowTask);

    /**
     * 批量列表（待我审批）
     *
     * @param paginationFlowTask
     * @return
     */
    List<FlowTaskListModel> getBatchWaitList(PaginationFlowTask paginationFlowTask);

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
     * 列表（待我审批）
     * 门户专用
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
     * 列表（待我审批）
     * 门户专用
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
    List<FlowTaskListModel> getTrialList(PaginationFlowTask paginationFlowTask);


    /**
     * 列表（抄送我的）
     *
     * @param paginationFlowTask
     * @return
     */
    List<FlowTaskListModel> getCirculateList(PaginationFlowTask paginationFlowTask);

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
    void update(FlowTaskEntity entity) ;

    /**
     * 创建
     *
     * @param entity 主键值
     * @return
     */
    void create(FlowTaskEntity entity) ;

    /**
     *
     * 信息
     *
     * @param id 主键值
     * @param columns 指定获取的列数据 , 任务中存了三个JSON数据 ， 排除后可以提高查询速度
     * @return
     */
    FlowTaskEntity getInfoSubmit(String id, SFunction<FlowTaskEntity, ?>... columns);

    /**
     *
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
