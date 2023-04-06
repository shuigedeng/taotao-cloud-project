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
import com.taotao.cloud.workflow.biz.form.entity.SalesOrderEntity;
import com.taotao.cloud.workflow.biz.form.entity.SalesOrderEntryEntity;
import com.taotao.cloud.workflow.biz.form.mapper.SalesOrderMapper;
import com.taotao.cloud.workflow.biz.form.service.SalesOrderEntryService;
import com.taotao.cloud.workflow.biz.form.service.SalesOrderService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 销售订单 */
@Service
public class SalesOrderServiceImpl extends ServiceImpl<SalesOrderMapper, SalesOrderEntity>
        implements SalesOrderService {

    @Autowired
    private BillRuleService billRuleService;

    @Autowired
    private SalesOrderEntryService salesOrderEntryService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private FileManageUtil fileManageUtil;

    @Override
    public List<SalesOrderEntryEntity> getSalesEntryList(String id) {
        QueryWrapper<SalesOrderEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(SalesOrderEntryEntity::getSalesOrderId, id)
                .orderByDesc(SalesOrderEntryEntity::getSortCode);
        return salesOrderEntryService.list(queryWrapper);
    }

    @Override
    public SalesOrderEntity getInfo(String id) {
        QueryWrapper<SalesOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SalesOrderEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, SalesOrderEntity entity, List<SalesOrderEntryEntity> salesOrderEntryEntityList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < salesOrderEntryEntityList.size(); i++) {
                salesOrderEntryEntityList.get(i).setId(RandomUtil.uuId());
                salesOrderEntryEntityList.get(i).setSalesOrderId(entity.getId());
                salesOrderEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                salesOrderEntryService.save(salesOrderEntryEntityList.get(i));
            }
            // 创建
            this.save(entity);
            billRuleService.useBillNumber("WF_SalesOrderNo");
            // 添加附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.createFile(data);
        } else {
            entity.setId(id);
            QueryWrapper<SalesOrderEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SalesOrderEntryEntity::getSalesOrderId, entity.getId());
            salesOrderEntryService.remove(queryWrapper);
            for (int i = 0; i < salesOrderEntryEntityList.size(); i++) {
                salesOrderEntryEntityList.get(i).setId(RandomUtil.uuId());
                salesOrderEntryEntityList.get(i).setSalesOrderId(entity.getId());
                salesOrderEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                salesOrderEntryService.save(salesOrderEntryEntityList.get(i));
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
            SalesOrderEntity entity,
            List<SalesOrderEntryEntity> salesOrderEntryEntityList,
            Map<String, List<String>> candidateList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < salesOrderEntryEntityList.size(); i++) {
                salesOrderEntryEntityList.get(i).setId(RandomUtil.uuId());
                salesOrderEntryEntityList.get(i).setSalesOrderId(entity.getId());
                salesOrderEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                salesOrderEntryService.save(salesOrderEntryEntityList.get(i));
            }
            // 创建
            this.save(entity);
            billRuleService.useBillNumber("WF_SalesOrderNo");
            // 添加附件
            List<FileModel> data = JsonUtil.getJsonToList(entity.getFileJson(), FileModel.class);
            fileManageUtil.createFile(data);
        } else {
            entity.setId(id);
            QueryWrapper<SalesOrderEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SalesOrderEntryEntity::getSalesOrderId, entity.getId());
            salesOrderEntryService.remove(queryWrapper);
            for (int i = 0; i < salesOrderEntryEntityList.size(); i++) {
                salesOrderEntryEntityList.get(i).setId(RandomUtil.uuId());
                salesOrderEntryEntityList.get(i).setSalesOrderId(entity.getId());
                salesOrderEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                salesOrderEntryService.save(salesOrderEntryEntityList.get(i));
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
        SalesOrderForm salesOrderForm = JsonUtil.getJsonToBean(data, SalesOrderForm.class);
        SalesOrderEntity entity = JsonUtil.getJsonToBean(salesOrderForm, SalesOrderEntity.class);
        List<SalesOrderEntryEntityInfoModel> entryList =
                salesOrderForm.getEntryList() != null ? salesOrderForm.getEntryList() : new ArrayList<>();
        List<SalesOrderEntryEntity> salesOrderEntryEntityList =
                JsonUtil.getJsonToList(entryList, SalesOrderEntryEntity.class);
        entity.setId(id);
        QueryWrapper<SalesOrderEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SalesOrderEntryEntity::getSalesOrderId, entity.getId());
        salesOrderEntryService.remove(queryWrapper);
        for (int i = 0; i < salesOrderEntryEntityList.size(); i++) {
            salesOrderEntryEntityList.get(i).setId(RandomUtil.uuId());
            salesOrderEntryEntityList.get(i).setSalesOrderId(entity.getId());
            salesOrderEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
            salesOrderEntryService.save(salesOrderEntryEntityList.get(i));
        }
        // 编辑
        this.saveOrUpdate(entity);
    }
}
