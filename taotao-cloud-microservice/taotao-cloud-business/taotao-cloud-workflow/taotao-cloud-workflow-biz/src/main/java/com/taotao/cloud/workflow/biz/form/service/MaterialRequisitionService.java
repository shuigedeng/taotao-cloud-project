package com.taotao.cloud.workflow.biz.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.form.entity.MaterialEntryEntity;
import com.taotao.cloud.workflow.biz.form.entity.MaterialRequisitionEntity;

import java.util.List;
import java.util.Map;

/**
 * 领料单
 */
public interface MaterialRequisitionService extends IService<MaterialRequisitionEntity> {

    /**
     * 列表
     *
     * @param id 主键值
     * @return
     */
    List<MaterialEntryEntity> getMaterialEntryList(String id);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    MaterialRequisitionEntity getInfo(String id);

    /**
     * 保存
     *
     * @param id                      主键值
     * @param entity                  实体对象
     * @param materialEntryEntityList 子表
     * @throws WorkFlowException 异常
     */
    void save(String id, MaterialRequisitionEntity entity, List<MaterialEntryEntity> materialEntryEntityList) throws WorkFlowException;

    /**
     * 提交
     *
     * @param id                      主键值
     * @param entity                  实体对象
     * @param materialEntryEntityList 子表
     * @throws WorkFlowException 异常
     */
    void submit(String id, MaterialRequisitionEntity entity, List<MaterialEntryEntity> materialEntryEntityList, Map<String, List<String>> candidateList) throws WorkFlowException;

    /**
     * 更改数据
     *
     * @param id   主键值
     * @param data 实体对象
     */
    void data(String id, String data);
}
