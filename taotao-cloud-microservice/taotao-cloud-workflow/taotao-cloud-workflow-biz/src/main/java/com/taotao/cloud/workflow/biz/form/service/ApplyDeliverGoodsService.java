package com.taotao.cloud.workflow.biz.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.form.entity.ApplyDeliverGoodsEntity;
import com.taotao.cloud.workflow.biz.form.entity.ApplyDeliverGoodsEntryEntity;

import java.util.List;
import java.util.Map;

/**
 * 发货申请单
 *
 */
public interface ApplyDeliverGoodsService extends IService<ApplyDeliverGoodsEntity> {

    /**
     * 列表
     *
     * @param id 主键值
     * @return
     */
    List<ApplyDeliverGoodsEntryEntity> getDeliverEntryList(String id);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    ApplyDeliverGoodsEntity getInfo(String id);

    /**
     * 保存
     *
     * @param id                               主键值
     * @param entity                           实体对象
     * @param applyDeliverGoodsEntryEntityList 子表
     * @throws WorkFlowException 异常
     */
    void save(String id, ApplyDeliverGoodsEntity entity, List<ApplyDeliverGoodsEntryEntity> applyDeliverGoodsEntryEntityList) throws WorkFlowException;

    /**
     * 提交
     *
     * @param id                               主键值
     * @param entity                           实体对象
     * @param applyDeliverGoodsEntryEntityList 子表
     * @throws WorkFlowException 异常
     */
    void submit(String id, ApplyDeliverGoodsEntity entity, List<ApplyDeliverGoodsEntryEntity> applyDeliverGoodsEntryEntityList, Map<String, List<String>> candidateList) throws WorkFlowException;

    /**
     * 更改数据
     *
     * @param id   主键值
     * @param data 实体对象
     */
    void data(String id, String data) ;
}
