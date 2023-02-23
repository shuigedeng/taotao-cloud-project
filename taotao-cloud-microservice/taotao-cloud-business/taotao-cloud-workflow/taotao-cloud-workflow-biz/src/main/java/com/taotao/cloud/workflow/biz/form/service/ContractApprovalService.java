package com.taotao.cloud.workflow.biz.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.form.entity.ContractApprovalEntity;

import java.util.List;
import java.util.Map;

/**
 * 合同审批
 */
public interface ContractApprovalService extends IService<ContractApprovalEntity> {

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    ContractApprovalEntity getInfo(String id);

    /**
     * 保存
     *
     * @param id     主键值
     * @param entity 实体对象
     * @throws WorkFlowException 异常
     */
    void save(String id, ContractApprovalEntity entity) throws WorkFlowException;

    /**
     * 提交
     *
     * @param id                 主键值
     * @param entity             实体对象
     * @param freeApproverUserId 审批人
     * @throws WorkFlowException 异常
     */
    void submit(String id, ContractApprovalEntity entity, String freeApproverUserId, Map<String, List<String>> candidateList) throws WorkFlowException;

    /**
     * 更改数据
     *
     * @param id   主键值
     * @param data 实体对象
     */
    void data(String id, String data);
}
