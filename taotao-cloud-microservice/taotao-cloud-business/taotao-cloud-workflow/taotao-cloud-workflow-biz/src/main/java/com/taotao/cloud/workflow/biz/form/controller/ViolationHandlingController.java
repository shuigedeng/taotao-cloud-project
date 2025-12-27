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
import com.taotao.cloud.workflow.biz.common.model.form.violationhandling.ViolationHandlingForm;
import com.taotao.cloud.workflow.biz.common.model.form.violationhandling.ViolationHandlingInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.ViolationHandlingEntity;
import com.taotao.cloud.workflow.biz.form.service.ViolationHandlingService;
import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 违章处理申请表 */
@Tag(tags = "违章处理申请表", value = "ViolationHandling")
@RestController
@RequestMapping("/api/workflow/Form/ViolationHandling")
public class ViolationHandlingController {

    @Autowired
    private ViolationHandlingService violationHandlingService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取违章处理申请表信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取违章处理申请表信息")
    @GetMapping("/{id}")
    public Result<ViolationHandlingInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        ViolationHandlingInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), ViolationHandlingInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ViolationHandlingEntity entity = violationHandlingService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, ViolationHandlingInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建违章处理申请表
     *
     * @param violationHandlingForm 表单对象
     * @return
     */
    @Operation("新建违章处理申请表")
    @PostMapping
    public Result create(@RequestBody ViolationHandlingForm violationHandlingForm) throws WorkFlowException {
        ViolationHandlingEntity entity = JacksonUtils.getJsonToBean(violationHandlingForm, ViolationHandlingEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(violationHandlingForm.getStatus())) {
            violationHandlingService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        violationHandlingService.submit(entity.getId(), entity, violationHandlingForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改违章处理申请表
     *
     * @param violationHandlingForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改违章处理申请表")
    @PutMapping("/{id}")
    public Result update(@RequestBody ViolationHandlingForm violationHandlingForm, @PathVariable("id") String id)
            throws WorkFlowException {
        ViolationHandlingEntity entity = JacksonUtils.getJsonToBean(violationHandlingForm, ViolationHandlingEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(violationHandlingForm.getStatus())) {
            violationHandlingService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        violationHandlingService.submit(id, entity, violationHandlingForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
