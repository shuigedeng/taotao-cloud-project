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
import com.taotao.cloud.workflow.biz.common.model.form.outgoingapply.OutgoingApplyForm;
import com.taotao.cloud.workflow.biz.common.model.form.outgoingapply.OutgoingApplyInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.OutgoingApplyEntity;
import com.taotao.cloud.workflow.biz.form.service.OutgoingApplyService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 外出申请单 */
@Tag(tags = "外出申请单", value = "OutgoingApply")
@RestController
@RequestMapping("/api/workflow/Form/OutgoingApply")
public class OutgoingApplyController {

    @Autowired
    private OutgoingApplyService outgoingApplyService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取外出申请单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取外出申请单信息")
    @GetMapping("/{id}")
    public Result<OutgoingApplyInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        OutgoingApplyInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), OutgoingApplyInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            OutgoingApplyEntity entity = outgoingApplyService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, OutgoingApplyInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建外出申请单
     *
     * @param outgoingApplyForm 表单对象
     * @return
     */
    @Operation("新建外出申请单")
    @PostMapping
    public Result create(@RequestBody OutgoingApplyForm outgoingApplyForm) throws WorkFlowException {
        if (outgoingApplyForm.getStartTime() > outgoingApplyForm.getEndTime()) {
            return Result.fail("结束时间不能小于起始时间");
        }
        OutgoingApplyEntity entity = JacksonUtils.getJsonToBean(outgoingApplyForm, OutgoingApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(outgoingApplyForm.getStatus())) {
            outgoingApplyService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        outgoingApplyService.submit(entity.getId(), entity, outgoingApplyForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改外出申请单
     *
     * @param outgoingApplyForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改外出申请单")
    @PutMapping("/{id}")
    public Result update(@RequestBody OutgoingApplyForm outgoingApplyForm, @PathVariable("id") String id)
            throws WorkFlowException {
        if (outgoingApplyForm.getStartTime() > outgoingApplyForm.getEndTime()) {
            return Result.fail("结束时间不能小于起始时间");
        }
        OutgoingApplyEntity entity = JacksonUtils.getJsonToBean(outgoingApplyForm, OutgoingApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(outgoingApplyForm.getStatus())) {
            outgoingApplyService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        outgoingApplyService.submit(id, entity, outgoingApplyForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
