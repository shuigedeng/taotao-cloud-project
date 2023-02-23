package com.taotao.cloud.workflow.biz.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.form.entity.SalesOrderEntity;
import com.taotao.cloud.workflow.biz.form.entity.SalesOrderEntryEntity;

import java.util.List;
import java.util.Map;

/**
 * 销售订单
 */
public interface SalesOrderService extends IService<SalesOrderEntity> {

    /**
     * 列表
     *
     * @param id 主键值
     * @return
     */
    List<SalesOrderEntryEntity> getSalesEntryList(String id);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    SalesOrderEntity getInfo(String id);

    /**
     * 保存
     *
     * @param id                        主键值
     * @param entity                    实体对象
     * @param salesOrderEntryEntityList 子表
     * @throws WorkFlowException 异常
     */
    void save(String id, SalesOrderEntity entity, List<SalesOrderEntryEntity> salesOrderEntryEntityList) throws WorkFlowException;

    /**
     * 提交
     *
     * @param id                        主键值
     * @param entity                    实体对象
     * @param salesOrderEntryEntityList 子表
     * @throws WorkFlowException 异常
     */
    void submit(String id, SalesOrderEntity entity, List<SalesOrderEntryEntity> salesOrderEntryEntityList, Map<String, List<String>> candidateList) throws WorkFlowException;

    /**
     * 更改数据
     *
     * @param id   主键值
     * @param data 实体对象
     */
    void data(String id, String data);
}
