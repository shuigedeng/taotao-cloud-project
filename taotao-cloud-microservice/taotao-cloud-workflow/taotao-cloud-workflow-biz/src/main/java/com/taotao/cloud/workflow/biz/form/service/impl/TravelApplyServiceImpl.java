package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.ModelUtil;
import com.taotao.cloud.workflow.biz.form.entity.TravelApplyEntity;
import com.taotao.cloud.workflow.biz.form.mapper.TravelApplyMapper;
import com.taotao.cloud.workflow.biz.common.model.form.travelapply.TravelApplyForm;
import com.taotao.cloud.workflow.biz.form.service.TravelApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 出差预支申请单
 */
@Service
public class TravelApplyServiceImpl extends ServiceImpl<TravelApplyMapper, TravelApplyEntity> implements TravelApplyService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public TravelApplyEntity getInfo(String id) {
        QueryWrapper<TravelApplyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TravelApplyEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, TravelApplyEntity entity) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_TravelApplyNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(),entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, TravelApplyEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_TravelApplyNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity,null, candidateList);
    }

    @Override
    public void data(String id, String data) {
        TravelApplyForm travelApplyForm = JsonUtils.getJsonToBean(data, TravelApplyForm.class);
        TravelApplyEntity entity = JsonUtils.getJsonToBean(travelApplyForm, TravelApplyEntity.class);
        entity.setId(id);
        this.saveOrUpdate(entity);
    }
}
