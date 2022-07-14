package com.taotao.cloud.workflow.biz.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.form.entity.ExpenseExpenditureEntity;

import java.util.List;
import java.util.Map;

/**
 * 费用支出单
 */
public interface ExpenseExpenditureService extends IService<ExpenseExpenditureEntity> {

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    ExpenseExpenditureEntity getInfo(String id);

    /**
     * 保存
     *
     * @param id     主键值
     * @param entity 实体对象
     * @throws WorkFlowException 异常
     */
    void save(String id, ExpenseExpenditureEntity entity) throws WorkFlowException;

    /**
     * 提交
     *
     * @param id     主键值
     * @param entity 实体对象
     * @throws WorkFlowException 异常
     */
    void submit(String id, ExpenseExpenditureEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException;

    /**
     * 更改数据
     *
     * @param id   主键值
     * @param data 实体对象
     */
    void data(String id, String data);
}
