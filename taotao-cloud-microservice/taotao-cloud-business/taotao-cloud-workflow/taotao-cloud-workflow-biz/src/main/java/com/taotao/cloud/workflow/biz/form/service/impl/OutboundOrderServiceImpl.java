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
import com.taotao.cloud.workflow.biz.form.entity.OutboundEntryEntity;
import com.taotao.cloud.workflow.biz.form.entity.OutboundOrderEntity;
import com.taotao.cloud.workflow.biz.form.mapper.OutboundOrderMapper;
import com.taotao.cloud.workflow.biz.form.service.OutboundEntryService;
import com.taotao.cloud.workflow.biz.form.service.OutboundOrderService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 出库单 */
@Service
public class OutboundOrderServiceImpl extends ServiceImpl<OutboundOrderMapper, OutboundOrderEntity>
        implements OutboundOrderService {

    // @Autowired
    // private BillRuleService billRuleService;
    @Autowired
    private OutboundEntryService outboundEntryService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public List<OutboundEntryEntity> getOutboundEntryList(String id) {
        QueryWrapper<OutboundEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OutboundEntryEntity::getOutboundId, id).orderByDesc(OutboundEntryEntity::getSortCode);
        return outboundEntryService.list(queryWrapper);
    }

    @Override
    public OutboundOrderEntity getInfo(String id) {
        QueryWrapper<OutboundOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OutboundOrderEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, OutboundOrderEntity entity, List<OutboundEntryEntity> outboundEntryEntityList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < outboundEntryEntityList.size(); i++) {
                outboundEntryEntityList.get(i).setId(RandomUtil.uuId());
                outboundEntryEntityList.get(i).setOutboundId(entity.getId());
                outboundEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                outboundEntryService.save(outboundEntryEntityList.get(i));
            }
            // 创建
            this.save(entity);
            billRuleService.useBillNumber("WF_OutboundOrderNo");
        } else {
            entity.setId(id);
            QueryWrapper<OutboundEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(OutboundEntryEntity::getOutboundId, entity.getId());
            outboundEntryService.remove(queryWrapper);
            for (int i = 0; i < outboundEntryEntityList.size(); i++) {
                outboundEntryEntityList.get(i).setId(RandomUtil.uuId());
                outboundEntryEntityList.get(i).setOutboundId(entity.getId());
                outboundEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                outboundEntryService.save(outboundEntryEntityList.get(i));
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
            OutboundOrderEntity entity,
            List<OutboundEntryEntity> outboundEntryEntityList,
            Map<String, List<String>> candidateList)
            throws WorkFlowException {
        // 表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            for (int i = 0; i < outboundEntryEntityList.size(); i++) {
                outboundEntryEntityList.get(i).setId(RandomUtil.uuId());
                outboundEntryEntityList.get(i).setOutboundId(entity.getId());
                outboundEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                outboundEntryService.save(outboundEntryEntityList.get(i));
            }
            // 创建
            save(entity);
            billRuleService.useBillNumber("WF_OutboundOrderNo");
        } else {
            entity.setId(id);
            QueryWrapper<OutboundEntryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(OutboundEntryEntity::getOutboundId, entity.getId());
            outboundEntryService.remove(queryWrapper);
            for (int i = 0; i < outboundEntryEntityList.size(); i++) {
                outboundEntryEntityList.get(i).setId(RandomUtil.uuId());
                outboundEntryEntityList.get(i).setOutboundId(entity.getId());
                outboundEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
                outboundEntryService.save(outboundEntryEntityList.get(i));
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
        OutboundOrderForm outboundOrderForm = JsonUtil.getJsonToBean(data, OutboundOrderForm.class);
        OutboundOrderEntity entity = JsonUtil.getJsonToBean(outboundOrderForm, OutboundOrderEntity.class);
        List<OutboundEntryEntityInfoModel> entryList =
                outboundOrderForm.getEntryList() != null ? outboundOrderForm.getEntryList() : new ArrayList<>();
        List<OutboundEntryEntity> outboundEntryEntityList =
                JsonUtil.getJsonToList(entryList, OutboundEntryEntity.class);
        entity.setId(id);
        QueryWrapper<OutboundEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OutboundEntryEntity::getOutboundId, entity.getId());
        outboundEntryService.remove(queryWrapper);
        for (int i = 0; i < outboundEntryEntityList.size(); i++) {
            outboundEntryEntityList.get(i).setId(RandomUtil.uuId());
            outboundEntryEntityList.get(i).setOutboundId(entity.getId());
            outboundEntryEntityList.get(i).setSortCode(Long.parseLong(i + ""));
            outboundEntryService.save(outboundEntryEntityList.get(i));
        }
        // 编辑
        this.saveOrUpdate(entity);
    }
}
