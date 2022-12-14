package com.taotao.cloud.workflow.biz.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 流程节点
 */
public interface FlowTaskNodeService extends IService<FlowTaskNodeEntity> {

    /**
     * 列表
     *
     * @return
     */
    List<FlowTaskNodeEntity> getListAll();

    /**
     * 列表
     *
     * @param taskId 任务主键
     * @return
     */
    List<FlowTaskNodeEntity> getList(String taskId);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    FlowTaskNodeEntity getInfo(String id);

    /**
     * 删除（根据实例Id）
     *
     * @param taskId 任务主键
     */
    void deleteByTaskId(String taskId);

    /**
     * 创建
     *
     * @param entitys 实体对象
     */
    void create(List<FlowTaskNodeEntity> entitys);

    /**
     * 创建
     * @param entity 实体对象
     */
    void create(FlowTaskNodeEntity entity);

    /**
     * 更新
     *
     * @param entity 实体对象
     */
    void update(FlowTaskNodeEntity entity);

    /**
     * 更新驳回开始流程节点
     *
     * @param taskId 流程id
     */
    void update(String taskId);

    /**
     * 修改节点的审批状态
     *
     * @param id    主键值
     * @param start 状态
     */
    void updateCompletion(List<String> id, int start);
}
