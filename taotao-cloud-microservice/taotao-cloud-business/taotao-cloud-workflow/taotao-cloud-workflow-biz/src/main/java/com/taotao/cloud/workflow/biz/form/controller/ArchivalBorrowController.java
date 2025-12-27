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
import com.taotao.cloud.workflow.biz.common.model.form.archivalborrow.ArchivalBorrowForm;
import com.taotao.cloud.workflow.biz.common.model.form.archivalborrow.ArchivalBorrowInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.ArchivalBorrowEntity;
import com.taotao.cloud.workflow.biz.form.service.ArchivalBorrowService;
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

/** 档案借阅申请 */
@Tag(tags = "档案借阅申请", value = "ArchivalBorrow")
@RestController
@RequestMapping("/api/workflow/Form/ArchivalBorrow")
public class ArchivalBorrowController {

    @Autowired
    private ArchivalBorrowService archivalBorrowService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取档案借阅申请信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取档案借阅申请信息")
    @GetMapping("/{id}")
    public Result<ArchivalBorrowInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        ArchivalBorrowInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), ArchivalBorrowInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ArchivalBorrowEntity entity = archivalBorrowService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, ArchivalBorrowInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建档案借阅申请
     *
     * @param archivalBorrowForm 表单对象
     * @return
     */
    @Operation("新建档案借阅申请")
    @PostMapping
    public Result create(@RequestBody @Valid ArchivalBorrowForm archivalBorrowForm) throws WorkFlowException {
        if (archivalBorrowForm.getBorrowingDate() > archivalBorrowForm.getReturnDate()) {
            return Result.fail("归还时间不能小于借阅时间");
        }
        ArchivalBorrowEntity entity = JacksonUtils.getJsonToBean(archivalBorrowForm, ArchivalBorrowEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(archivalBorrowForm.getStatus())) {
            archivalBorrowService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        archivalBorrowService.submit(entity.getId(), entity, archivalBorrowForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改档案借阅申请
     *
     * @param archivalBorrowForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改档案借阅申请")
    @PutMapping("/{id}")
    public Result update(@RequestBody @Valid ArchivalBorrowForm archivalBorrowForm, @PathVariable("id") String id)
            throws WorkFlowException {
        if (archivalBorrowForm.getBorrowingDate() > archivalBorrowForm.getReturnDate()) {
            return Result.fail("归还时间不能小于借阅时间");
        }
        ArchivalBorrowEntity entity = JacksonUtils.getJsonToBean(archivalBorrowForm, ArchivalBorrowEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(archivalBorrowForm.getStatus())) {
            archivalBorrowService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        archivalBorrowService.submit(id, entity, archivalBorrowForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
