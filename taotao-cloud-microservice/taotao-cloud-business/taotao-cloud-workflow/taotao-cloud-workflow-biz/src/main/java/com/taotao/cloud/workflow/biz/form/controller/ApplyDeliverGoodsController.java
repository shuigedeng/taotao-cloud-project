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

package com.taotao.cloud.workflow.biz.form.controller;

import com.taotao.boot.common.utils.json.JacksonUtils;
import com.taotao.cloud.workflow.biz.common.model.form.applydelivergoods.ApplyDeliverGoodsEntryInfoModel;
import com.taotao.cloud.workflow.biz.common.model.form.applydelivergoods.ApplyDeliverGoodsForm;
import com.taotao.cloud.workflow.biz.common.model.form.applydelivergoods.ApplyDeliverGoodsInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.ApplyDeliverGoodsEntity;
import com.taotao.cloud.workflow.biz.form.entity.ApplyDeliverGoodsEntryEntity;
import com.taotao.cloud.workflow.biz.form.service.ApplyDeliverGoodsService;
import jakarta.validation.Valid;
import java.util.List;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 发货申请单 */
@Tag(tags = "发货申请单", value = "ApplyDeliverGoods")
@RestController
@RequestMapping("/api/workflow/Form/ApplyDeliverGoods")
public class ApplyDeliverGoodsController {

    @Autowired
    private ApplyDeliverGoodsService applyDeliverGoodsService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取发货申请单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取发货申请单信息")
    @GetMapping("/{id}")
    public Result<ApplyDeliverGoodsInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        ApplyDeliverGoodsInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), ApplyDeliverGoodsInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ApplyDeliverGoodsEntity entity = applyDeliverGoodsService.getInfo(id);
            List<ApplyDeliverGoodsEntryEntity> entityList = applyDeliverGoodsService.getDeliverEntryList(id);
            vo = JacksonUtils.getJsonToBean(entity, ApplyDeliverGoodsInfoVO.class);
            vo.setEntryList(JacksonUtils.getJsonToList(entityList, ApplyDeliverGoodsEntryInfoModel.class));
        }
        return Result.success(vo);
    }

    /**
     * 新建发货申请单
     *
     * @param applyDeliverGoodsForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @Operation("新建发货申请单")
    @PostMapping
    public Result create(@RequestBody @Valid ApplyDeliverGoodsForm applyDeliverGoodsForm) throws WorkFlowException {
        ApplyDeliverGoodsEntity deliver = JacksonUtils.getJsonToBean(applyDeliverGoodsForm, ApplyDeliverGoodsEntity.class);
        List<ApplyDeliverGoodsEntryEntity> deliverEntryList =
                JacksonUtils.getJsonToList(applyDeliverGoodsForm.getEntryList(), ApplyDeliverGoodsEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(applyDeliverGoodsForm.getStatus())) {
            applyDeliverGoodsService.save(deliver.getId(), deliver, deliverEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        applyDeliverGoodsService.submit(
                deliver.getId(), deliver, deliverEntryList, applyDeliverGoodsForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改发货申请单
     *
     * @param applyDeliverGoodsForm 表单对象
     * @param id 主键
     * @return
     * @throws WorkFlowException
     */
    @Operation("修改发货申请单")
    @PutMapping("/{id}")
    public Result update(@RequestBody @Valid ApplyDeliverGoodsForm applyDeliverGoodsForm, @PathVariable("id") String id)
            throws WorkFlowException {
        ApplyDeliverGoodsEntity deliver = JacksonUtils.getJsonToBean(applyDeliverGoodsForm, ApplyDeliverGoodsEntity.class);
        List<ApplyDeliverGoodsEntryEntity> deliverEntryList =
                JacksonUtils.getJsonToList(applyDeliverGoodsForm.getEntryList(), ApplyDeliverGoodsEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(applyDeliverGoodsForm.getStatus())) {
            applyDeliverGoodsService.save(id, deliver, deliverEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        applyDeliverGoodsService.submit(id, deliver, deliverEntryList, applyDeliverGoodsForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
