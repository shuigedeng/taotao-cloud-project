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
import com.taotao.cloud.workflow.biz.common.model.form.incomerecognition.IncomeRecognitionForm;
import com.taotao.cloud.workflow.biz.common.model.form.incomerecognition.IncomeRecognitionInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.IncomeRecognitionEntity;
import com.taotao.cloud.workflow.biz.form.service.IncomeRecognitionService;
import jakarta.validation.Valid;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 收入确认分析表 */
@Tag(tags = "收入确认分析表", value = "IncomeRecognition")
@RestController
@RequestMapping("/api/workflow/Form/IncomeRecognition")
public class IncomeRecognitionController {

    @Autowired
    private IncomeRecognitionService incomeRecognitionService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取收入确认分析表信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取收入确认分析表信息")
    @GetMapping("/{id}")
    public Result<IncomeRecognitionInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        IncomeRecognitionInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), IncomeRecognitionInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            IncomeRecognitionEntity entity = incomeRecognitionService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, IncomeRecognitionInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建收入确认分析表
     *
     * @param incomeRecognitionForm 表单对象
     * @return
     */
    @Operation("新建收入确认分析表")
    @PostMapping
    public Result create(@RequestBody @Valid IncomeRecognitionForm incomeRecognitionForm) throws WorkFlowException {
        IncomeRecognitionEntity entity = JacksonUtils.getJsonToBean(incomeRecognitionForm, IncomeRecognitionEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(incomeRecognitionForm.getStatus())) {
            incomeRecognitionService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        incomeRecognitionService.submit(entity.getId(), entity, incomeRecognitionForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改收入确认分析表
     *
     * @param incomeRecognitionForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改收入确认分析表")
    @PutMapping("/{id}")
    public Result update(@RequestBody @Valid IncomeRecognitionForm incomeRecognitionForm, @PathVariable("id") String id)
            throws WorkFlowException {
        IncomeRecognitionEntity entity = JacksonUtils.getJsonToBean(incomeRecognitionForm, IncomeRecognitionEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(incomeRecognitionForm.getStatus())) {
            incomeRecognitionService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        incomeRecognitionService.submit(id, entity, incomeRecognitionForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
