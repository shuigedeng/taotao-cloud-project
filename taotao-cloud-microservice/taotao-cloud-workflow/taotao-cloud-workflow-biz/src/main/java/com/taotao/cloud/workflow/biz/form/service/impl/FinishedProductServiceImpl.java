package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.form.entity.FinishedProductEntity;
import com.taotao.cloud.workflow.biz.form.entity.FinishedProductEntryEntity;
import com.taotao.cloud.workflow.biz.form.mapper.FinishedProductMapper;
import com.taotao.cloud.workflow.biz.form.service.FinishedProductEntryService;
import com.taotao.cloud.workflow.biz.form.service.FinishedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 成品入库单
 *
 */
@Service
public class FinishedProductServiceImpl extends ServiceImpl<FinishedProductMapper, FinishedProductEntity> implements FinishedProductService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private FinishedProductEntryService finishedProductEntryService;
    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public List<FinishedProductEntryEntity> getFinishedEntryList(String id) {
        QueryWrapper<FinishedProductEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FinishedProductEntryEntity::getWarehouseId, id).orderByDesc(FinishedProductEntryEntity::getSortCode);
        return finishedProductEntryService.list(queryWrapper);
    }

    @Override
    public FinishedProductEntity getInfo(String id) {
        QueryWrapper<FinishedProductEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FinishedProductEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, FinishedProductEntity entity, List<FinishedProductEntryEntity> finishedProductEntryEntityList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < finishedProductEntryEntityList.size(); i++) {
                finishedProductEntryEntityList.get(i).setId(RandomUtil.uuId());
                finishedProductEntryEntityList.get(i).setWarehouseId(entity.getId());
                finishedProductEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                finishedProductEntryService.save(finishedProductEntryEntityList.get(i));
            }
            //创建
            this.save(entity);
            billRuleService.useBillNumber("WF_FinishedProductNo");
        } else {
            entity.setId(id);
            QueryWrapper<FinishedProductEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FinishedProductEntryEntity::getWarehouseId, entity.getId());
            finishedProductEntryService.remove(queryWrapper);
            for (int i = 0; i < finishedProductEntryEntityList.size(); i++) {
                finishedProductEntryEntityList.get(i).setId(RandomUtil.uuId());
                finishedProductEntryEntityList.get(i).setWarehouseId(entity.getId());
                finishedProductEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                finishedProductEntryService.save(finishedProductEntryEntityList.get(i));
            }
            //编辑
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, FinishedProductEntity entity, List<FinishedProductEntryEntity> finishedProductEntryEntityList, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < finishedProductEntryEntityList.size(); i++) {
                finishedProductEntryEntityList.get(i).setId(RandomUtil.uuId());
                finishedProductEntryEntityList.get(i).setWarehouseId(entity.getId());
                finishedProductEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                finishedProductEntryService.save(finishedProductEntryEntityList.get(i));
            }
            //创建
            this.save(entity);
            billRuleService.useBillNumber("WF_FinishedProductNo");
        } else {
            entity.setId(id);
            QueryWrapper<FinishedProductEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FinishedProductEntryEntity::getWarehouseId, entity.getId());
            finishedProductEntryService.remove(queryWrapper);
            for (int i = 0; i < finishedProductEntryEntityList.size(); i++) {
                finishedProductEntryEntityList.get(i).setId(RandomUtil.uuId());
                finishedProductEntryEntityList.get(i).setWarehouseId(entity.getId());
                finishedProductEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                finishedProductEntryService.save(finishedProductEntryEntityList.get(i));
            }
            //编辑
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity, null, candidateList);
    }

    @Override
    public void data(String id, String data) {
        FinishedProductForm finishedProductForm = JsonUtil.getJsonToBean(data, FinishedProductForm.class);
        FinishedProductEntity entity = JsonUtil.getJsonToBean(finishedProductForm, FinishedProductEntity.class);
        List<FinishedProductEntryEntityInfoModel> entryList = finishedProductForm.getEntryList() != null ? finishedProductForm.getEntryList() : new ArrayList<>();
        List<FinishedProductEntryEntity> finishedProductEntryEntityList = JsonUtil.getJsonToList(entryList, FinishedProductEntryEntity.class);
        entity.setId(id);
        QueryWrapper<FinishedProductEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FinishedProductEntryEntity::getWarehouseId, entity.getId());
        finishedProductEntryService.remove(queryWrapper);
        for (int i = 0; i < finishedProductEntryEntityList.size(); i++) {
            finishedProductEntryEntityList.get(i).setId(RandomUtil.uuId());
            finishedProductEntryEntityList.get(i).setWarehouseId(entity.getId());
            finishedProductEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
            finishedProductEntryService.save(finishedProductEntryEntityList.get(i));
        }
        this.saveOrUpdate(entity);
    }
}
