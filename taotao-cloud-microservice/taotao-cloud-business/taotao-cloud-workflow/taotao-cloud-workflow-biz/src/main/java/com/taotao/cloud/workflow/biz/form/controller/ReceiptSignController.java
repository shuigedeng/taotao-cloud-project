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
import com.taotao.cloud.workflow.biz.common.model.form.receiptsign.ReceiptSignForm;
import com.taotao.cloud.workflow.biz.common.model.form.receiptsign.ReceiptSignInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.ReceiptSignEntity;
import com.taotao.cloud.workflow.biz.form.service.ReceiptSignService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 收文签呈单 */
@Tag(tags = "收文签呈单", value = "ReceiptSign")
@RestController
@RequestMapping("/api/workflow/Form/ReceiptSign")
public class ReceiptSignController {

    @Autowired
    private ReceiptSignService receiptSignService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取收文签呈单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取收文签呈单信息")
    @GetMapping("/{id}")
    public Result<ReceiptSignInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ReceiptSignInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), ReceiptSignInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ReceiptSignEntity entity = receiptSignService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, ReceiptSignInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建收文签呈单
     *
     * @param receiptSignForm 表单对象
     * @return
     */
    @Operation("新建收文签呈单")
    @PostMapping
    public Result create(@RequestBody ReceiptSignForm receiptSignForm) throws WorkFlowException {
        ReceiptSignEntity entity = JacksonUtils.getJsonToBean(receiptSignForm, ReceiptSignEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(receiptSignForm.getStatus())) {
            receiptSignService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        receiptSignService.submit(entity.getId(), entity, receiptSignForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改收文签呈单
     *
     * @param receiptSignForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改收文签呈单")
    @PutMapping("/{id}")
    public Result update(@RequestBody ReceiptSignForm receiptSignForm, @PathVariable("id") String id)
            throws WorkFlowException {
        ReceiptSignEntity entity = JacksonUtils.getJsonToBean(receiptSignForm, ReceiptSignEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(receiptSignForm.getStatus())) {
            receiptSignService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        receiptSignService.submit(id, entity, receiptSignForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
