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
import com.taotao.cloud.workflow.biz.common.model.form.receiptprocessing.ReceiptProcessingForm;
import com.taotao.cloud.workflow.biz.common.model.form.receiptprocessing.ReceiptProcessingInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.ReceiptProcessingEntity;
import com.taotao.cloud.workflow.biz.form.service.ReceiptProcessingService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 收文处理表 */
@Tag(tags = "收文处理表", value = "ReceiptProcessing")
@RestController
@RequestMapping("/api/workflow/Form/ReceiptProcessing")
public class ReceiptProcessingController {

    @Autowired
    private ReceiptProcessingService receiptProcessingService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取收文处理表信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取收文处理表信息")
    @GetMapping("/{id}")
    public Result<ReceiptProcessingInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        ReceiptProcessingInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), ReceiptProcessingInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ReceiptProcessingEntity entity = receiptProcessingService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, ReceiptProcessingInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建收文处理表
     *
     * @param receiptProcessingForm 表单对象
     * @return
     */
    @Operation("新建收文处理表")
    @PostMapping
    public Result create(@RequestBody ReceiptProcessingForm receiptProcessingForm) throws WorkFlowException {
        ReceiptProcessingEntity entity = JacksonUtils.getJsonToBean(receiptProcessingForm, ReceiptProcessingEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(receiptProcessingForm.getStatus())) {
            receiptProcessingService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        receiptProcessingService.submit(entity.getId(), entity, receiptProcessingForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改收文处理表
     *
     * @param receiptProcessingForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改收文处理表")
    @PutMapping("/{id}")
    public Result update(@RequestBody ReceiptProcessingForm receiptProcessingForm, @PathVariable("id") String id)
            throws WorkFlowException {
        ReceiptProcessingEntity entity = JacksonUtils.getJsonToBean(receiptProcessingForm, ReceiptProcessingEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(receiptProcessingForm.getStatus())) {
            receiptProcessingService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        receiptProcessingService.submit(id, entity, receiptProcessingForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
