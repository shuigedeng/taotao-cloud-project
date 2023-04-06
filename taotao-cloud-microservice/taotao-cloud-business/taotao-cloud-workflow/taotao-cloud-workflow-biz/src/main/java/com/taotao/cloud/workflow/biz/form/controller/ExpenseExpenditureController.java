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

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 费用支出单 */
@Tag(tags = "费用支出单", value = "ExpenseExpenditure")
@RestController
@RequestMapping("/api/workflow/Form/ExpenseExpenditure")
public class ExpenseExpenditureController {

    @Autowired
    private ExpenseExpenditureService expenseExpenditureService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取费用支出单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取费用支出单信息")
    @GetMapping("/{id}")
    public Result<ExpenseExpenditureInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        ExpenseExpenditureInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ExpenseExpenditureInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ExpenseExpenditureEntity entity = expenseExpenditureService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ExpenseExpenditureInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建费用支出单
     *
     * @param expenseExpenditureForm 表单对象
     * @return
     */
    @Operation("新建费用支出单")
    @PostMapping
    public Result create(@RequestBody @Valid ExpenseExpenditureForm expenseExpenditureForm) throws WorkFlowException {
        ExpenseExpenditureEntity entity =
                JsonUtil.getJsonToBean(expenseExpenditureForm, ExpenseExpenditureEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(expenseExpenditureForm.getStatus())) {
            expenseExpenditureService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        expenseExpenditureService.submit(entity.getId(), entity, expenseExpenditureForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改费用支出单
     *
     * @param expenseExpenditureForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改费用支出单")
    @PutMapping("/{id}")
    public Result update(
            @RequestBody @Valid ExpenseExpenditureForm expenseExpenditureForm, @PathVariable("id") String id)
            throws WorkFlowException {
        ExpenseExpenditureEntity entity =
                JsonUtil.getJsonToBean(expenseExpenditureForm, ExpenseExpenditureEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(expenseExpenditureForm.getStatus())) {
            expenseExpenditureService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        expenseExpenditureService.submit(id, entity, expenseExpenditureForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
