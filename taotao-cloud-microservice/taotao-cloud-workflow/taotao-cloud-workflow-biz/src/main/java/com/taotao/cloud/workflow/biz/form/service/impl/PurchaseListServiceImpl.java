package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jnpf.base.service.BillRuleService;
import jnpf.base.util.FileManageUtil;
import jnpf.engine.service.FlowTaskService;
import jnpf.engine.util.ModelUtil;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.PurchaseListEntity;
import jnpf.form.entity.PurchaseListEntryEntity;
import jnpf.form.mapper.PurchaseListMapper;
import jnpf.form.model.purchaselist.PurchaseListEntryEntityInfoModel;
import jnpf.form.model.purchaselist.PurchaseListForm;
import jnpf.form.service.PurchaseListEntryService;
import jnpf.form.service.PurchaseListService;
import jnpf.model.FileModel;
import jnpf.util.JsonUtil;
import jnpf.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日常物品采购清单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Service
public class PurchaseListServiceImpl extends ServiceImpl<PurchaseListMapper, PurchaseListEntity> implements PurchaseListService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private PurchaseListEntryService purchaseListEntryService;
    @Autowired
    private FlowTaskService flowTaskService;
    @Autowired
    private FileManageUtil fileManageUtil;

    @Override
    public List<PurchaseListEntryEntity> getPurchaseEntryList(String id) {
        QueryWrapper<PurchaseListEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PurchaseListEntryEntity::getPurchaseId, id).orderByDesc(PurchaseListEntryEntity::getSortCode);
        return purchaseListEntryService.list(queryWrapper);
    }

    @Override
    public PurchaseListEntity getInfo(String id) {
        QueryWrapper<PurchaseListEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PurchaseListEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, PurchaseListEntity entity, List<PurchaseListEntryEntity> purchaseListEntryEntityList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < purchaseListEntryEntityList.size(); i++) {
                purchaseListEntryEntityList.get(i).setId(RandomUtil.uuId());
                purchaseListEntryEntityList.get(i).setPurchaseId(entity.getId());
                purchaseListEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                purchaseListEntryService.save(purchaseListEntryEntityList.get(i));
            }
            //创建
            this.save(entity);
            billRuleService.useBillNumber("WF_PurchaseListNo");
            //添加附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.createFile(data);
        } else {
            entity.setId(id);
            QueryWrapper<PurchaseListEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PurchaseListEntryEntity::getPurchaseId, entity.getId());
            purchaseListEntryService.remove(queryWrapper);
            for (int i = 0; i < purchaseListEntryEntityList.size(); i++) {
                purchaseListEntryEntityList.get(i).setId(RandomUtil.uuId());
                purchaseListEntryEntityList.get(i).setPurchaseId(entity.getId());
                purchaseListEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                purchaseListEntryService.save(purchaseListEntryEntityList.get(i));
            }
            //编辑
            this.updateById(entity);
            //更新附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.updateFile(data);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, PurchaseListEntity entity, List<PurchaseListEntryEntity> purchaseListEntryEntityList, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < purchaseListEntryEntityList.size(); i++) {
                purchaseListEntryEntityList.get(i).setId(RandomUtil.uuId());
                purchaseListEntryEntityList.get(i).setPurchaseId(entity.getId());
                purchaseListEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                purchaseListEntryService.save(purchaseListEntryEntityList.get(i));
            }
            //创建
            this.save(entity);
            billRuleService.useBillNumber("WF_PurchaseListNo");
            //添加附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.createFile(data);
        } else {
            entity.setId(id);
            QueryWrapper<PurchaseListEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PurchaseListEntryEntity::getPurchaseId, entity.getId());
            purchaseListEntryService.remove(queryWrapper);
            for (int i = 0; i < purchaseListEntryEntityList.size(); i++) {
                purchaseListEntryEntityList.get(i).setId(RandomUtil.uuId());
                purchaseListEntryEntityList.get(i).setPurchaseId(entity.getId());
                purchaseListEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                purchaseListEntryService.save(purchaseListEntryEntityList.get(i));
            }
            //编辑
            this.updateById(entity);
            //更新附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.updateFile(data);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity, null, candidateList);
    }

    @Override
    public void data(String id, String data) {
        PurchaseListForm purchaseListForm = JsonUtil.getJsonToBean(data, PurchaseListForm.class);
        PurchaseListEntity entity = JsonUtil.getJsonToBean(purchaseListForm, PurchaseListEntity.class);
        List<PurchaseListEntryEntityInfoModel> entryList = purchaseListForm.getEntryList() != null ? purchaseListForm.getEntryList() : new ArrayList<>();
        List<PurchaseListEntryEntity> purchaseListEntryEntityList = JsonUtil.getJsonToList(entryList, PurchaseListEntryEntity.class);
        entity.setId(id);
        QueryWrapper<PurchaseListEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PurchaseListEntryEntity::getPurchaseId, entity.getId());
        purchaseListEntryService.remove(queryWrapper);
        for (int i = 0; i < purchaseListEntryEntityList.size(); i++) {
            purchaseListEntryEntityList.get(i).setId(RandomUtil.uuId());
            purchaseListEntryEntityList.get(i).setPurchaseId(entity.getId());
            purchaseListEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
            purchaseListEntryService.save(purchaseListEntryEntityList.get(i));
        }
        //编辑
        this.saveOrUpdate(entity);
    }
}
