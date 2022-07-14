package com.taotao.cloud.workflow.biz.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.ProcurementEntryEntity;
import jnpf.form.entity.ProcurementMaterialEntity;

/**
 * 采购原材料
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
public interface ProcurementMaterialService extends IService<ProcurementMaterialEntity> {

    /**
     * 列表
     *
     * @param id 主键值
     * @return
     */
    List<ProcurementEntryEntity> getProcurementEntryList(String id);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    ProcurementMaterialEntity getInfo(String id);

    /**
     * 保存
     *
     * @param id                         主键值
     * @param entity                     实体对象
     * @param procurementEntryEntityList 子表
     * @throws WorkFlowException 异常
     */
    void save(String id, ProcurementMaterialEntity entity, List<ProcurementEntryEntity> procurementEntryEntityList) throws WorkFlowException;

    /**
     * 提交
     *
     * @param id                         主键值
     * @param entity                     实体对象
     * @param procurementEntryEntityList 子表
     * @throws WorkFlowException 异常
     */
    void submit(String id, ProcurementMaterialEntity entity, List<ProcurementEntryEntity> procurementEntryEntityList, Map<String, List<String>> candidateList) throws WorkFlowException;

    /**
     * 更改数据
     *
     * @param id   主键值
     * @param data 实体对象
     */
    void data(String id, String data);
}
