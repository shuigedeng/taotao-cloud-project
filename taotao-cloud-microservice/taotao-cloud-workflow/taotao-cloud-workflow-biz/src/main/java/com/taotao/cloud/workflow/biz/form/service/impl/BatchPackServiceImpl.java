package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.ModelUtil;
import com.taotao.cloud.workflow.biz.form.entity.BatchPackEntity;
import com.taotao.cloud.workflow.biz.form.mapper.BatchPackMapper;
import com.taotao.cloud.workflow.biz.form.model.batchpack.BatchPackForm;
import com.taotao.cloud.workflow.biz.form.service.BatchPackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 */
@Service
public class BatchPackServiceImpl extends ServiceImpl<BatchPackMapper, BatchPackEntity> implements BatchPackService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public BatchPackEntity getInfo(String id) {
        QueryWrapper<BatchPackEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BatchPackEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, BatchPackEntity entity) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_BatchPackNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(),entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, BatchPackEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_BatchPackNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity,null, candidateList);
    }

    @Override
    public void data(String id, String data) {
        BatchPackForm batchPackForm = JsonUtil.getJsonToBean(data, BatchPackForm.class);
        BatchPackEntity entity = JsonUtil.getJsonToBean(batchPackForm, BatchPackEntity.class);
        entity.setId(id);
        this.saveOrUpdate(entity);
    }
}
