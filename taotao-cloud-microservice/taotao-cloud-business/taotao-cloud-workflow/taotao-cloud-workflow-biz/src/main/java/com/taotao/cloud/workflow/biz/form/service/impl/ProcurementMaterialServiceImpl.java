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
import com.taotao.cloud.workflow.biz.form.entity.ProcurementEntryEntity;
import com.taotao.cloud.workflow.biz.form.entity.ProcurementMaterialEntity;
import com.taotao.cloud.workflow.biz.form.mapper.ProcurementMaterialMapper;
import com.taotao.cloud.workflow.biz.form.service.ProcurementEntryService;
import com.taotao.cloud.workflow.biz.form.service.ProcurementMaterialService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 采购原材料 */
@Service
public class ProcurementMaterialServiceImpl extends ServiceImpl<ProcurementMaterialMapper, ProcurementMaterialEntity>
        implements ProcurementMaterialService {

    @Autowired
    private BillRuleService billRuleService;

    @Autowired
    private ProcurementEntryService procurementEntryEntityService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private FileManageUtil fileManageUtil;

    @Override
    public List<ProcurementEntryEntity> getProcurementEntryList(String id) {
        QueryWrapper<ProcurementEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(ProcurementEntryEntity::getProcurementId, id)
                .orderByDesc(ProcurementEntryEntity::getSortCode);
        return procurementEntryEntityService.list(queryWrapper);
    }

    @Override
    @DSTransactional
    public ProcurementMaterialEntity getInfo(String id) {
        QueryWrapper<ProcurementMaterialEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProcurementMaterialEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(
            String id, ProcurementMaterialEntity entity, List<ProcurementEntryEntity> procurementEntryEntityList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < procurementEntryEntityList.size(); i++) {
                procurementEntryEntityList.get(i).setId(RandomUtil.uuId());
                procurementEntryEntityList.get(i).setProcurementId(entity.getId());
                procurementEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                procurementEntryEntityService.save(procurementEntryEntityList.get(i));
            }
            // 创建
            this.save(entity);
            billRuleService.useBillNumber("WF_ProcurementMaterialNo");
            // 添加附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.createFile(data);
        } else {
            entity.setId(id);
            QueryWrapper<ProcurementEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(ProcurementEntryEntity::getProcurementId, entity.getId());
            procurementEntryEntityService.remove(queryWrapper);
            for (int i = 0; i < procurementEntryEntityList.size(); i++) {
                procurementEntryEntityList.get(i).setId(RandomUtil.uuId());
                procurementEntryEntityList.get(i).setProcurementId(entity.getId());
                procurementEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                procurementEntryEntityService.save(procurementEntryEntityList.get(i));
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
            ProcurementMaterialEntity entity,
            List<ProcurementEntryEntity> procurementEntryEntityList,
            Map<String, List<String>> candidateList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < procurementEntryEntityList.size(); i++) {
                procurementEntryEntityList.get(i).setId(RandomUtil.uuId());
                procurementEntryEntityList.get(i).setProcurementId(entity.getId());
                procurementEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                procurementEntryEntityService.save(procurementEntryEntityList.get(i));
            }
            // 创建
            this.save(entity);
            billRuleService.useBillNumber("WF_ProcurementMaterialNo");
            // 添加附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.createFile(data);
        } else {
            entity.setId(id);
            QueryWrapper<ProcurementEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(ProcurementEntryEntity::getProcurementId, entity.getId());
            procurementEntryEntityService.remove(queryWrapper);
            for (int i = 0; i < procurementEntryEntityList.size(); i++) {
                procurementEntryEntityList.get(i).setId(RandomUtil.uuId());
                procurementEntryEntityList.get(i).setProcurementId(entity.getId());
                procurementEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                procurementEntryEntityService.save(procurementEntryEntityList.get(i));
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
        ProcurementMaterialForm procurementMaterialForm = JsonUtil.getJsonToBean(data, ProcurementMaterialForm.class);
        ProcurementMaterialEntity entity =
                JsonUtil.getJsonToBean(procurementMaterialForm, ProcurementMaterialEntity.class);
        List<ProcurementEntryEntityInfoModel> entryList = procurementMaterialForm.getEntryList() != null
                ? procurementMaterialForm.getEntryList()
                : new ArrayList<>();
        List<ProcurementEntryEntity> procurementEntryEntityList =
                JsonUtil.getJsonToList(entryList, ProcurementEntryEntity.class);
        entity.setId(id);
        QueryWrapper<ProcurementEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProcurementEntryEntity::getProcurementId, entity.getId());
        procurementEntryEntityService.remove(queryWrapper);
        for (int i = 0; i < procurementEntryEntityList.size(); i++) {
            procurementEntryEntityList.get(i).setId(RandomUtil.uuId());
            procurementEntryEntityList.get(i).setProcurementId(entity.getId());
            procurementEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
            procurementEntryEntityService.save(procurementEntryEntityList.get(i));
        }
        // 编辑
        this.saveOrUpdate(entity);
    }
}
