package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.ModelUtil;
import com.taotao.cloud.workflow.biz.form.entity.SupplementCardEntity;
import com.taotao.cloud.workflow.biz.form.mapper.SupplementCardMapper;
import com.taotao.cloud.workflow.biz.form.service.SupplementCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 补卡申请
 */
@Service
public class SupplementCardServiceImpl extends ServiceImpl<SupplementCardMapper, SupplementCardEntity> implements SupplementCardService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public SupplementCardEntity getInfo(String id) {
        QueryWrapper<SupplementCardEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SupplementCardEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, SupplementCardEntity entity) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_SupplementCardNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(),entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, SupplementCardEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_SupplementCardNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity,null, candidateList);
    }

    @Override
    public void data(String id, String data) {
        SupplementCardForm supplementCardForm = JsonUtil.getJsonToBean(data, SupplementCardForm.class);
        SupplementCardEntity entity = JsonUtil.getJsonToBean(supplementCardForm, SupplementCardEntity.class);
        entity.setId(id);
        this.saveOrUpdate(entity);
    }
}
