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

/** 合同审批 */
@Tag(tags = "合同审批", value = "ContractApproval")
@RestController
@RequestMapping("/api/workflow/Form/ContractApproval")
public class ContractApprovalController {

    @Autowired
    private ContractApprovalService contractApprovalService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取合同审批信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取合同审批信息")
    @GetMapping("/{id}")
    public Result<ContractApprovalInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        ContractApprovalInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ContractApprovalInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ContractApprovalEntity entity = contractApprovalService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ContractApprovalInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建合同审批
     *
     * @param contractApprovalForm 表单对象
     * @return
     */
    @Operation("新建合同审批")
    @PostMapping
    public Result create(@RequestBody @Valid ContractApprovalForm contractApprovalForm) throws WorkFlowException {
        ContractApprovalEntity entity = JsonUtil.getJsonToBean(contractApprovalForm, ContractApprovalEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(contractApprovalForm.getStatus())) {
            contractApprovalService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        contractApprovalService.submit(
                entity.getId(),
                entity,
                contractApprovalForm.getFreeApproverUserId(),
                contractApprovalForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改合同审批
     *
     * @param contractApprovalForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改合同审批")
    @PutMapping("/{id}")
    public Result update(@RequestBody @Valid ContractApprovalForm contractApprovalForm, @PathVariable("id") String id)
            throws WorkFlowException {
        ContractApprovalEntity entity = JsonUtil.getJsonToBean(contractApprovalForm, ContractApprovalEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(contractApprovalForm.getStatus())) {
            contractApprovalService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        contractApprovalService.submit(
                id, entity, contractApprovalForm.getFreeApproverUserId(), contractApprovalForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
