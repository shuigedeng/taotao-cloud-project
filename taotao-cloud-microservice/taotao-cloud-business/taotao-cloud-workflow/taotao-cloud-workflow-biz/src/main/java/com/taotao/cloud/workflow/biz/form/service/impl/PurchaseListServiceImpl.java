/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.form.entity.PurchaseListEntity;
import com.taotao.cloud.workflow.biz.form.entity.PurchaseListEntryEntity;
import com.taotao.cloud.workflow.biz.form.mapper.PurchaseListMapper;
import com.taotao.cloud.workflow.biz.form.service.PurchaseListEntryService;
import com.taotao.cloud.workflow.biz.form.service.PurchaseListService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 日常物品采购清单 */
@Service
public class PurchaseListServiceImpl extends ServiceImpl<PurchaseListMapper, PurchaseListEntity>
        implements PurchaseListService {

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
        queryWrapper
                .lambda()
                .eq(PurchaseListEntryEntity::getPurchaseId, id)
                .orderByDesc(PurchaseListEntryEntity::getSortCode);
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
    public void save(String id, PurchaseListEntity entity, List<PurchaseListEntryEntity> purchaseListEntryEntityList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < purchaseListEntryEntityList.size(); i++) {
                purchaseListEntryEntityList.get(i).setId(RandomUtil.uuId());
                purchaseListEntryEntityList.get(i).setPurchaseId(entity.getId());
                purchaseListEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                purchaseListEntryService.save(purchaseListEntryEntityList.get(i));
            }
            // 创建
            this.save(entity);
            billRuleService.useBillNumber("WF_PurchaseListNo");
            // 添加附件
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
            // 编辑
            this.updateById(entity);
            // 更新附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.updateFile(data);
        }
        // 流程信息
        ModelUtil.save(
                id,
                entity.getFlowId(),
                entity.getId(),
                entity.getFlowTitle(),
                entity.getFlowUrgent(),
                entity.getBillNo(),
                entity);
    }

    @Override
    @DSTransactional
    public void submit(
            String id,
            PurchaseListEntity entity,
            List<PurchaseListEntryEntity> purchaseListEntryEntityList,
            Map<String, List<String>> candidateList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < purchaseListEntryEntityList.size(); i++) {
                purchaseListEntryEntityList.get(i).setId(RandomUtil.uuId());
                purchaseListEntryEntityList.get(i).setPurchaseId(entity.getId());
                purchaseListEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                purchaseListEntryService.save(purchaseListEntryEntityList.get(i));
            }
            // 创建
            this.save(entity);
            billRuleService.useBillNumber("WF_PurchaseListNo");
            // 添加附件
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
            // 编辑
            this.updateById(entity);
            // 更新附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.updateFile(data);
        }
        // 流程信息
        ModelUtil.submit(
                id,
                entity.getFlowId(),
                entity.getId(),
                entity.getFlowTitle(),
                entity.getFlowUrgent(),
                entity.getBillNo(),
                entity,
                null,
                candidateList);
    }

    @Override
    public void data(String id, String data) {
        PurchaseListForm purchaseListForm = JsonUtil.getJsonToBean(data, PurchaseListForm.class);
        PurchaseListEntity entity = JsonUtil.getJsonToBean(purchaseListForm, PurchaseListEntity.class);
        List<PurchaseListEntryEntityInfoModel> entryList =
                purchaseListForm.getEntryList() != null ? purchaseListForm.getEntryList() : new ArrayList<>();
        List<PurchaseListEntryEntity> purchaseListEntryEntityList =
                JsonUtil.getJsonToList(entryList, PurchaseListEntryEntity.class);
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
        // 编辑
        this.saveOrUpdate(entity);
    }
}
