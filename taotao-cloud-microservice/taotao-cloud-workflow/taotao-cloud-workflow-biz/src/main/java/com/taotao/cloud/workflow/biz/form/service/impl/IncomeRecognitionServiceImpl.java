package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.form.entity.IncomeRecognitionEntity;
import com.taotao.cloud.workflow.biz.form.mapper.IncomeRecognitionMapper;
import com.taotao.cloud.workflow.biz.form.service.IncomeRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 收入确认分析表
 *
 */
@Service
public class IncomeRecognitionServiceImpl extends ServiceImpl<IncomeRecognitionMapper, IncomeRecognitionEntity> implements IncomeRecognitionService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public IncomeRecognitionEntity getInfo(String id) {
        QueryWrapper<IncomeRecognitionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IncomeRecognitionEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, IncomeRecognitionEntity entity) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_IncomeRecognitionNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(),entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, IncomeRecognitionEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_IncomeRecognitionNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity,null, candidateList);
    }

    @Override
    public void data(String id, String data) {
        IncomeRecognitionForm incomeRecognitionForm = JsonUtil.getJsonToBean(data, IncomeRecognitionForm.class);
        IncomeRecognitionEntity entity = JsonUtil.getJsonToBean(incomeRecognitionForm, IncomeRecognitionEntity.class);
        entity.setId(id);
        this.saveOrUpdate(entity);
    }
}
