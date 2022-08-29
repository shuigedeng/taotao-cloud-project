package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.ModelUtil;
import com.taotao.cloud.workflow.biz.form.entity.PayDistributionEntity;
import com.taotao.cloud.workflow.biz.form.mapper.PayDistributionMapper;
import com.taotao.cloud.workflow.biz.form.model.paydistribution.PayDistributionForm;
import com.taotao.cloud.workflow.biz.form.service.PayDistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 薪酬发放
 */
@Service
public class PayDistributionServiceImpl extends ServiceImpl<PayDistributionMapper, PayDistributionEntity> implements PayDistributionService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public PayDistributionEntity getInfo(String id) {
        QueryWrapper<PayDistributionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PayDistributionEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, PayDistributionEntity entity) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_PayDistributionNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(),entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, PayDistributionEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_PayDistributionNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity,null, candidateList);
    }

    @Override
    public void data(String id, String data) {
        PayDistributionForm payDistributionForm = JsonUtils.getJsonToBean(data, PayDistributionForm.class);
        PayDistributionEntity entity = JsonUtils.getJsonToBean(payDistributionForm, PayDistributionEntity.class);
        entity.setId(id);
        this.saveOrUpdate(entity);
    }
}
