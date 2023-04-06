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
import com.taotao.cloud.workflow.biz.form.entity.ApplyDeliverGoodsEntity;
import com.taotao.cloud.workflow.biz.form.entity.ApplyDeliverGoodsEntryEntity;
import com.taotao.cloud.workflow.biz.form.mapper.ApplyDeliverGoodsMapper;
import com.taotao.cloud.workflow.biz.form.service.ApplyDeliverGoodsEntryService;
import com.taotao.cloud.workflow.biz.form.service.ApplyDeliverGoodsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 发货申请单 */
@Service
public class ApplyDeliverGoodsServiceImpl extends ServiceImpl<ApplyDeliverGoodsMapper, ApplyDeliverGoodsEntity>
        implements ApplyDeliverGoodsService {

    @Autowired
    private ApplyDeliverGoodsEntryService applyDeliverGoodsEntryService;

    @Autowired
    private BillRuleService billRuleService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public List<ApplyDeliverGoodsEntryEntity> getDeliverEntryList(String id) {
        QueryWrapper<ApplyDeliverGoodsEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(ApplyDeliverGoodsEntryEntity::getInvoiceId, id)
                .orderByDesc(ApplyDeliverGoodsEntryEntity::getSortCode);
        return applyDeliverGoodsEntryService.list(queryWrapper);
    }

    @Override
    public ApplyDeliverGoodsEntity getInfo(String id) {
        QueryWrapper<ApplyDeliverGoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApplyDeliverGoodsEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(
            String id,
            ApplyDeliverGoodsEntity entity,
            List<ApplyDeliverGoodsEntryEntity> applyDeliverGoodsEntryEntityList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < applyDeliverGoodsEntryEntityList.size(); i++) {
                applyDeliverGoodsEntryEntityList.get(i).setId(RandomUtil.uuId());
                applyDeliverGoodsEntryEntityList.get(i).setInvoiceId(entity.getId());
                applyDeliverGoodsEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                applyDeliverGoodsEntryService.save(applyDeliverGoodsEntryEntityList.get(i));
            }
            this.save(entity);
            billRuleService.useBillNumber("WF_ApplyDeliverGoodsNo");
        } else {
            entity.setId(id);
            QueryWrapper<ApplyDeliverGoodsEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(ApplyDeliverGoodsEntryEntity::getInvoiceId, entity.getId());
            applyDeliverGoodsEntryService.remove(queryWrapper);
            for (int i = 0; i < applyDeliverGoodsEntryEntityList.size(); i++) {
                applyDeliverGoodsEntryEntityList.get(i).setId(RandomUtil.uuId());
                applyDeliverGoodsEntryEntityList.get(i).setInvoiceId(entity.getId());
                applyDeliverGoodsEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                applyDeliverGoodsEntryService.save(applyDeliverGoodsEntryEntityList.get(i));
            }
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
            ApplyDeliverGoodsEntity entity,
            List<ApplyDeliverGoodsEntryEntity> applyDeliverGoodsEntryEntityList,
            Map<String, List<String>> candidateList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < applyDeliverGoodsEntryEntityList.size(); i++) {
                applyDeliverGoodsEntryEntityList.get(i).setId(RandomUtil.uuId());
                applyDeliverGoodsEntryEntityList.get(i).setInvoiceId(entity.getId());
                applyDeliverGoodsEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                applyDeliverGoodsEntryService.save(applyDeliverGoodsEntryEntityList.get(i));
            }
            this.save(entity);
            billRuleService.useBillNumber("WF_ApplyDeliverGoodsNo");
        } else {
            entity.setId(id);
            QueryWrapper<ApplyDeliverGoodsEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(ApplyDeliverGoodsEntryEntity::getInvoiceId, entity.getId());
            applyDeliverGoodsEntryService.remove(queryWrapper);
            for (int i = 0; i < applyDeliverGoodsEntryEntityList.size(); i++) {
                applyDeliverGoodsEntryEntityList.get(i).setId(RandomUtil.uuId());
                applyDeliverGoodsEntryEntityList.get(i).setInvoiceId(entity.getId());
                applyDeliverGoodsEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                applyDeliverGoodsEntryService.save(applyDeliverGoodsEntryEntityList.get(i));
            }
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
        ApplyDeliverGoodsForm applyDeliverGoodsForm = JsonUtil.getJsonToBean(data, ApplyDeliverGoodsForm.class);
        ApplyDeliverGoodsEntity entity = JsonUtil.getJsonToBean(applyDeliverGoodsForm, ApplyDeliverGoodsEntity.class);
        List<ApplyDeliverGoodsEntryInfoModel> entryList =
                applyDeliverGoodsForm.getEntryList() != null ? applyDeliverGoodsForm.getEntryList() : new ArrayList<>();
        List<ApplyDeliverGoodsEntryEntity> applyDeliverGoodsEntryEntityList =
                JsonUtil.getJsonToList(entryList, ApplyDeliverGoodsEntryEntity.class);
        entity.setId(id);
        QueryWrapper<ApplyDeliverGoodsEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApplyDeliverGoodsEntryEntity::getInvoiceId, entity.getId());
        applyDeliverGoodsEntryService.remove(queryWrapper);
        for (int i = 0; i < applyDeliverGoodsEntryEntityList.size(); i++) {
            applyDeliverGoodsEntryEntityList.get(i).setId(RandomUtil.uuId());
            applyDeliverGoodsEntryEntityList.get(i).setInvoiceId(entity.getId());
            applyDeliverGoodsEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
            applyDeliverGoodsEntryService.save(applyDeliverGoodsEntryEntityList.get(i));
        }
        this.saveOrUpdate(entity);
    }
}
