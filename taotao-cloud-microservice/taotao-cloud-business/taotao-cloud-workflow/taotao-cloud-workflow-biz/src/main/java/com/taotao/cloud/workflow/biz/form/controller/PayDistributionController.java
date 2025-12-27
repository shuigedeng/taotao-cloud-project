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
import com.taotao.cloud.workflow.biz.common.model.form.paydistribution.PayDistributionInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.PayDistributionEntity;
import com.taotao.cloud.workflow.biz.form.service.PayDistributionService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 薪酬发放 */
@Tag(tags = "薪酬发放", value = "PayDistribution")
@RestController
@RequestMapping("/api/workflow/Form/PayDistribution")
public class PayDistributionController {

    @Autowired
    private PayDistributionService payDistributionService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取薪酬发放信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取薪酬发放信息")
    @GetMapping("/{id}")
    public Result<PayDistributionInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        PayDistributionInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), PayDistributionInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            PayDistributionEntity entity = payDistributionService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, PayDistributionInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建薪酬发放
     *
     * @param payDistributionForm 表单对象
     * @return
     */
    @Operation("新建薪酬发放")
    @PostMapping
    public Result create(@RequestBody PayDistributionForm payDistributionForm) throws WorkFlowException {
        PayDistributionEntity entity = JacksonUtils.getJsonToBean(payDistributionForm, PayDistributionEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(payDistributionForm.getStatus())) {
            payDistributionService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        payDistributionService.submit(entity.getId(), entity, payDistributionForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改薪酬发放
     *
     * @param payDistributionForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改薪酬发放")
    @PutMapping("/{id}")
    public Result update(@RequestBody PayDistributionForm payDistributionForm, @PathVariable("id") String id)
            throws WorkFlowException {
        PayDistributionEntity entity = JacksonUtils.getJsonToBean(payDistributionForm, PayDistributionEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(payDistributionForm.getStatus())) {
            payDistributionService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        payDistributionService.submit(id, entity, payDistributionForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
