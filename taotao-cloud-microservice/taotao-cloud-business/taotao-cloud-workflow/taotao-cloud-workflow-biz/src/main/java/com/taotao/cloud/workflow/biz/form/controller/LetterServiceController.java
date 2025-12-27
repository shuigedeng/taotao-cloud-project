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
import com.taotao.cloud.workflow.biz.common.model.form.letterservice.LetterServiceForm;
import com.taotao.cloud.workflow.biz.common.model.form.letterservice.LetterServiceInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.LetterServiceEntity;
import com.taotao.cloud.workflow.biz.form.service.LetterServiceService;
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

/** 发文单 */
@Tag(tags = "发文单", value = "LetterService")
@RestController
@RequestMapping("/api/workflow/Form/LetterService")
public class LetterServiceController {

    @Autowired
    private LetterServiceService letterServiceService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取发文单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取发文单信息")
    @GetMapping("/{id}")
    public Result<LetterServiceInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        LetterServiceInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), LetterServiceInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            LetterServiceEntity entity = letterServiceService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, LetterServiceInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建发文单
     *
     * @param letterServiceForm 表单对象
     * @return
     */
    @Operation("新建发文单")
    @PostMapping
    public Result create(@RequestBody @Valid LetterServiceForm letterServiceForm) throws WorkFlowException {
        if (letterServiceForm.getShareNum() != null
                && StringUtil.isNotEmpty(letterServiceForm.getShareNum())
                && !RegexUtils.checkDigit2(letterServiceForm.getShareNum())) {
            return Result.fail("份数只能输入正整数");
        }
        LetterServiceEntity entity = JacksonUtils.getJsonToBean(letterServiceForm, LetterServiceEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(letterServiceForm.getStatus())) {
            letterServiceService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        letterServiceService.submit(entity.getId(), entity, letterServiceForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改发文单
     *
     * @param letterServiceForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改发文单")
    @PutMapping("/{id}")
    public Result update(@RequestBody @Valid LetterServiceForm letterServiceForm, @PathVariable("id") String id)
            throws WorkFlowException {
        if (letterServiceForm.getShareNum() != null
                && StringUtil.isNotEmpty(letterServiceForm.getShareNum())
                && !RegexUtils.checkDigit2(letterServiceForm.getShareNum())) {
            return Result.fail("份数只能输入正整数");
        }
        LetterServiceEntity entity = JacksonUtils.getJsonToBean(letterServiceForm, LetterServiceEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(letterServiceForm.getStatus())) {
            letterServiceService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        letterServiceService.submit(id, entity, letterServiceForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
