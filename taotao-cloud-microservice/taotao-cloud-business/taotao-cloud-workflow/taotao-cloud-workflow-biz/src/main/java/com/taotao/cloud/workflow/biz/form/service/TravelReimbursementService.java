package com.taotao.cloud.workflow.biz.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.form.entity.TravelReimbursementEntity;

import java.util.List;
import java.util.Map;

/**
 * 差旅报销申请表
 */
public interface TravelReimbursementService extends IService<TravelReimbursementEntity> {

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    TravelReimbursementEntity getInfo(String id);

    /**
     * 保存
     *
     * @param id     主键值
     * @param entity 实体对象
     * @throws WorkFlowException 异常
     */
    void save(String id, TravelReimbursementEntity entity) throws WorkFlowException;

    /**
     * 提交
     *
     * @param id     主键值
     * @param entity 实体对象
     * @throws WorkFlowException 异常
     */
    void submit(String id, TravelReimbursementEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException;

    /**
     * 更改数据
     *
     * @param id   主键值
     * @param data 实体对象
     */
    void data(String id, String data);
}
