package com.taotao.cloud.workflow.biz.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.engine.entity.FlowCandidatesEntity;

import java.util.List;

/**
 * 流程候选人
 *
 */
public interface FlowCandidatesService extends IService<FlowCandidatesEntity> {

    /**
     * 列表
     *
     * @param taskNodeId 节点主键
     * @return
     */
    List<FlowCandidatesEntity> getlist(String taskNodeId);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    FlowCandidatesEntity getInfo(String id);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(FlowCandidatesEntity entity);

    /**
     * 创建
     *
     * @param list 实体对象
     */
    void create(List<FlowCandidatesEntity> list);

    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     * @return
     */
    void update(String id, FlowCandidatesEntity entity);

    /**
     * 删除
     *
     * @param entity 实体对象
     * @return
     */
    void delete(FlowCandidatesEntity entity);

    /**
     * 删除
     *
     * @param taskId
     */
    void deleteByTaskId(String taskId);

    /**
     * 拒绝删除候选人节点
     */
    void deleteTaskNodeId(List<String> taskNodeId);

    /**
     * 撤回删除候选人
     *
     * @param taskNodeId 节点主键
     * @param handleId   用户主键
     * @param operatorId 待办主键
     */
    void delete(List<String> taskNodeId, String handleId, String operatorId);
}
