package com.taotao.cloud.workflow.biz.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.form.entity.WarehouseEntryEntity;
import com.taotao.cloud.workflow.biz.form.entity.WarehouseReceiptEntity;

import java.util.List;
import java.util.Map;

/**
 * 入库申请单
 */
public interface WarehouseReceiptService extends IService<WarehouseReceiptEntity> {

    /**
     * 列表
     *
     * @param id 主键值
     * @return
     */
    List<WarehouseEntryEntity> getWarehouseEntryList(String id);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    WarehouseReceiptEntity getInfo(String id);

    /**
     * 保存
     *
     * @param id                       主键值
     * @param entity                   实体对象
     * @param warehouseEntryEntityList 子表
     * @throws WorkFlowException 异常
     */
    void save(String id, WarehouseReceiptEntity entity, List<WarehouseEntryEntity> warehouseEntryEntityList) throws WorkFlowException;

    /**
     * 提交
     *
     * @param id                       主键值
     * @param entity                   实体对象
     * @param warehouseEntryEntityList 子表
     * @throws WorkFlowException 异常
     */
    void submit(String id, WarehouseReceiptEntity entity, List<WarehouseEntryEntity> warehouseEntryEntityList, Map<String, List<String>> candidateList) throws WorkFlowException;

    /**
     * 更改数据
     *
     * @param id   主键值
     * @param data 实体对象
     */
    void data(String id, String data);
}
