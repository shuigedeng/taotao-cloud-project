package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.ModelUtil;
import com.taotao.cloud.workflow.biz.form.entity.PaymentApplyEntity;
import com.taotao.cloud.workflow.biz.form.mapper.PaymentApplyMapper;
import com.taotao.cloud.workflow.biz.form.service.PaymentApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 付款申请单
 */
@Service
public class PaymentApplyServiceImpl extends ServiceImpl<PaymentApplyMapper, PaymentApplyEntity> implements PaymentApplyService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public PaymentApplyEntity getInfo(String id) {
        QueryWrapper<PaymentApplyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PaymentApplyEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, PaymentApplyEntity entity) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_PaymentApplyNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(),entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, PaymentApplyEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_PaymentApplyNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity,null, candidateList);
    }

    @Override
    public void data(String id, String data) {
        PaymentApplyForm paymentApplyForm = JsonUtil.getJsonToBean(data, PaymentApplyForm.class);
        PaymentApplyEntity entity = JsonUtil.getJsonToBean(paymentApplyForm, PaymentApplyEntity.class);
        entity.setId(id);
        this.saveOrUpdate(entity);
    }
}
