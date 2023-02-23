package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.form.entity.ArticlesWarehousEntity;
import com.taotao.cloud.workflow.biz.form.mapper.ArticlesWarehousMapper;
import com.taotao.cloud.workflow.biz.form.service.ArticlesWarehousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 用品入库申请表
 *
 */
@Service
public class ArticlesWarehousServiceImpl extends ServiceImpl<ArticlesWarehousMapper, ArticlesWarehousEntity> implements ArticlesWarehousService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public ArticlesWarehousEntity getInfo(String id) {
        QueryWrapper<ArticlesWarehousEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ArticlesWarehousEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, ArticlesWarehousEntity entity) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_ArticlesWarehousNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(),entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, ArticlesWarehousEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_ArticlesWarehousNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity,null, candidateList);
    }

    @Override
    public void data(String id, String data) {
        ArticlesWarehousForm articlesWarehousForm = JsonUtil.getJsonToBean(data, ArticlesWarehousForm.class);
        ArticlesWarehousEntity entity = JsonUtil.getJsonToBean(articlesWarehousForm, ArticlesWarehousEntity.class);
        entity.setId(id);
        this.saveOrUpdate(entity);
    }
}
