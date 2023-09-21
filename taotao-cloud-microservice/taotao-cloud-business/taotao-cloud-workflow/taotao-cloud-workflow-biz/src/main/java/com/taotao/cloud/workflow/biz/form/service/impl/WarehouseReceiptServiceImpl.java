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
import com.taotao.cloud.workflow.biz.form.entity.WarehouseEntryEntity;
import com.taotao.cloud.workflow.biz.form.entity.WarehouseReceiptEntity;
import com.taotao.cloud.workflow.biz.form.mapper.WarehouseReceiptMapper;
import com.taotao.cloud.workflow.biz.form.service.WarehouseEntryService;
import com.taotao.cloud.workflow.biz.form.service.WarehouseReceiptService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 入库申请单
 *
 * @since 2019年9月29日 上午9:18
 */
@Service
public class WarehouseReceiptServiceImpl extends ServiceImpl<WarehouseReceiptMapper, WarehouseReceiptEntity>
        implements WarehouseReceiptService {

    @Autowired
    private BillRuleService billRuleService;

    @Autowired
    private WarehouseEntryService warehouseEntryService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public List<WarehouseEntryEntity> getWarehouseEntryList(String id) {
        QueryWrapper<WarehouseEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(WarehouseEntryEntity::getWarehouseId, id)
                .orderByDesc(WarehouseEntryEntity::getSortCode);
        return warehouseEntryService.list(queryWrapper);
    }

    @Override
    public WarehouseReceiptEntity getInfo(String id) {
        QueryWrapper<WarehouseReceiptEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WarehouseReceiptEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, WarehouseReceiptEntity entity, List<WarehouseEntryEntity> warehouseEntryEntityList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < warehouseEntryEntityList.size(); i++) {
                warehouseEntryEntityList.get(i).setId(RandomUtil.uuId());
                warehouseEntryEntityList.get(i).setWarehouseId(entity.getId());
                warehouseEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                warehouseEntryService.save(warehouseEntryEntityList.get(i));
            }
            // 创建
            this.save(entity);
            billRuleService.useBillNumber("WF_WarehouseReceiptNo");
        } else {
            entity.setId(id);
            QueryWrapper<WarehouseEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WarehouseEntryEntity::getWarehouseId, entity.getId());
            warehouseEntryService.remove(queryWrapper);
            for (int i = 0; i < warehouseEntryEntityList.size(); i++) {
                warehouseEntryEntityList.get(i).setId(RandomUtil.uuId());
                warehouseEntryEntityList.get(i).setWarehouseId(entity.getId());
                warehouseEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                warehouseEntryService.save(warehouseEntryEntityList.get(i));
            }
            // 编辑
            this.updateById(entity);
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
            WarehouseReceiptEntity entity,
            List<WarehouseEntryEntity> warehouseEntryEntityList,
            Map<String, List<String>> candidateList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < warehouseEntryEntityList.size(); i++) {
                warehouseEntryEntityList.get(i).setId(RandomUtil.uuId());
                warehouseEntryEntityList.get(i).setWarehouseId(entity.getId());
                warehouseEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                warehouseEntryService.save(warehouseEntryEntityList.get(i));
            }
            // 创建
            this.save(entity);
            billRuleService.useBillNumber("WF_WarehouseReceiptNo");
        } else {
            entity.setId(id);
            QueryWrapper<WarehouseEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WarehouseEntryEntity::getWarehouseId, entity.getId());
            warehouseEntryService.remove(queryWrapper);
            for (int i = 0; i < warehouseEntryEntityList.size(); i++) {
                warehouseEntryEntityList.get(i).setId(RandomUtil.uuId());
                warehouseEntryEntityList.get(i).setWarehouseId(entity.getId());
                warehouseEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                warehouseEntryService.save(warehouseEntryEntityList.get(i));
            }
            // 编辑
            this.updateById(entity);
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
        WarehouseReceiptForm warehouseReceiptForm = JsonUtil.getJsonToBean(data, WarehouseReceiptForm.class);
        WarehouseReceiptEntity entity = JsonUtil.getJsonToBean(warehouseReceiptForm, WarehouseReceiptEntity.class);
        List<WarehouseReceiptEntityInfoModel> entryList =
                warehouseReceiptForm.getEntryList() != null ? warehouseReceiptForm.getEntryList() : new ArrayList<>();
        List<WarehouseEntryEntity> warehouseEntryEntityList =
                JsonUtil.getJsonToList(entryList, WarehouseEntryEntity.class);
        entity.setId(id);
        QueryWrapper<WarehouseEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WarehouseEntryEntity::getWarehouseId, entity.getId());
        warehouseEntryService.remove(queryWrapper);
        for (int i = 0; i < warehouseEntryEntityList.size(); i++) {
            warehouseEntryEntityList.get(i).setId(RandomUtil.uuId());
            warehouseEntryEntityList.get(i).setWarehouseId(entity.getId());
            warehouseEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
            warehouseEntryService.save(warehouseEntryEntityList.get(i));
        }
        // 编辑
        this.saveOrUpdate(entity);
    }
}
