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
import com.taotao.cloud.workflow.biz.common.model.form.travelreimbursement.TravelReimbursementForm;
import com.taotao.cloud.workflow.biz.common.model.form.travelreimbursement.TravelReimbursementInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.TravelReimbursementEntity;
import com.taotao.cloud.workflow.biz.form.service.TravelReimbursementService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 差旅报销申请表 */
@Tag(tags = "差旅报销申请表", value = "TravelReimbursement")
@RestController
@RequestMapping("/api/workflow/Form/TravelReimbursement")
public class TravelReimbursementController {

    @Autowired
    private TravelReimbursementService travelReimbursementService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取差旅报销申请表信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取差旅报销申请表信息")
    @GetMapping("/{id}")
    public Result<TravelReimbursementInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        TravelReimbursementInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), TravelReimbursementInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            TravelReimbursementEntity entity = travelReimbursementService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, TravelReimbursementInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建差旅报销申请表
     *
     * @param travelReimbursementForm 表单对象
     * @return
     */
    @Operation("新建差旅报销申请表")
    @PostMapping
    public Result create(@RequestBody TravelReimbursementForm travelReimbursementForm) throws WorkFlowException {
        if (travelReimbursementForm.getSetOutDate() > travelReimbursementForm.getReturnDate()) {
            return Result.fail("结束时间不能小于起始时间");
        }
        TravelReimbursementEntity entity =
                JacksonUtils.getJsonToBean(travelReimbursementForm, TravelReimbursementEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(travelReimbursementForm.getStatus())) {
            travelReimbursementService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        travelReimbursementService.submit(entity.getId(), entity, travelReimbursementForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改差旅报销申请表
     *
     * @param travelReimbursementForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改差旅报销申请表")
    @PutMapping("/{id}")
    public Result update(@RequestBody TravelReimbursementForm travelReimbursementForm, @PathVariable("id") String id)
            throws WorkFlowException {
        if (travelReimbursementForm.getSetOutDate() > travelReimbursementForm.getReturnDate()) {
            return Result.fail("结束时间不能小于起始时间");
        }
        TravelReimbursementEntity entity =
                JacksonUtils.getJsonToBean(travelReimbursementForm, TravelReimbursementEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(travelReimbursementForm.getStatus())) {
            travelReimbursementService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        travelReimbursementService.submit(id, entity, travelReimbursementForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
