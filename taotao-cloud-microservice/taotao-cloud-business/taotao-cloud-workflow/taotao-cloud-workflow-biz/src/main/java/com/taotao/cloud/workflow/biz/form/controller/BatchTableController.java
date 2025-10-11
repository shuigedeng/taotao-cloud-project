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

import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import com.taotao.cloud.workflow.biz.common.model.form.batchtable.BatchTableForm;
import com.taotao.cloud.workflow.biz.common.model.form.batchtable.BatchTableInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import com.taotao.cloud.workflow.biz.form.entity.BatchTableEntity;
import com.taotao.cloud.workflow.biz.form.service.BatchTableService;
import io.swagger.v3.oas.annotations.Operation;
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

/** 行文呈批表 */
@Tag(tags = "行文呈批表", value = "BatchTable")
@RestController
@RequestMapping("/api/workflow/Form/BatchTable")
public class BatchTableController {

    @Autowired
    private BatchTableService batchTableService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取行文呈批表信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取行文呈批表信息")
    @GetMapping("/{id}")
    public Result<BatchTableInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        BatchTableInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtils.getJsonToBean(operator.getDraftData(), BatchTableInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            BatchTableEntity entity = batchTableService.getInfo(id);
            vo = JsonUtils.getJsonToBean(entity, BatchTableInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建行文呈批表
     *
     * @param batchTableForm 表单对象
     * @return
     */
    @Operation("新建行文呈批表")
    @PostMapping
    public Result create(@RequestBody @Valid BatchTableForm batchTableForm) throws WorkFlowException {
        if (batchTableForm.getShareNum() != null
                && StringUtil.isNotEmpty(batchTableForm.getShareNum())
                && !RegexUtils.checkDigit2(batchTableForm.getShareNum())) {
            return Result.fail("份数只能输入正整数");
        }
        BatchTableEntity entity = JsonUtils.getJsonToBean(batchTableForm, BatchTableEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(batchTableForm.getStatus())) {
            batchTableService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        batchTableService.submit(entity.getId(), entity, batchTableForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改行文呈批表
     *
     * @param batchTableForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改行文呈批表")
    @PutMapping("/{id}")
    public Result update(@RequestBody @Valid BatchTableForm batchTableForm, @PathVariable("id") String id)
            throws WorkFlowException {
        if (batchTableForm.getShareNum() != null
                && StringUtil.isNotEmpty(batchTableForm.getShareNum())
                && !RegexUtils.checkDigit2(batchTableForm.getShareNum())) {
            return Result.fail("份数只能输入正整数");
        }
        BatchTableEntity entity = JsonUtils.getJsonToBean(batchTableForm, BatchTableEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(batchTableForm.getStatus())) {
            batchTableService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        batchTableService.submit(id, entity, batchTableForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
