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
import com.taotao.cloud.workflow.biz.common.model.form.workcontactsheet.WorkContactSheetForm;
import com.taotao.cloud.workflow.biz.common.model.form.workcontactsheet.WorkContactSheetInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.WorkContactSheetEntity;
import com.taotao.cloud.workflow.biz.form.service.WorkContactSheetService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 工作联系单 */
@Tag(tags = "工作联系单", value = "WorkContactSheet")
@RestController
@RequestMapping("/api/workflow/Form/WorkContactSheet")
public class WorkContactSheetController {

    @Autowired
    private WorkContactSheetService workContactSheetService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取工作联系单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取工作联系单信息")
    @GetMapping("/{id}")
    public Result<WorkContactSheetInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        WorkContactSheetInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), WorkContactSheetInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            WorkContactSheetEntity entity = workContactSheetService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, WorkContactSheetInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建工作联系单
     *
     * @param workContactSheetForm 表单对象
     * @return
     */
    @Operation("新建工作联系单")
    @PostMapping
    public Result create(@RequestBody WorkContactSheetForm workContactSheetForm) throws WorkFlowException {
        WorkContactSheetEntity entity = JacksonUtils.getJsonToBean(workContactSheetForm, WorkContactSheetEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(workContactSheetForm.getStatus())) {
            workContactSheetService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        workContactSheetService.submit(entity.getId(), entity, workContactSheetForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改工作联系单
     *
     * @param workContactSheetForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改工作联系单")
    @PutMapping("/{id}")
    public Result update(@RequestBody WorkContactSheetForm workContactSheetForm, @PathVariable("id") String id)
            throws WorkFlowException {
        WorkContactSheetEntity entity = JacksonUtils.getJsonToBean(workContactSheetForm, WorkContactSheetEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(workContactSheetForm.getStatus())) {
            workContactSheetService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        workContactSheetService.submit(id, entity, workContactSheetForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
