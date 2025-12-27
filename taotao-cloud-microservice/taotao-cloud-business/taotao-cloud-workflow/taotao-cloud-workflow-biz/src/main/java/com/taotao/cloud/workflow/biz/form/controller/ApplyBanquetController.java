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
import com.taotao.cloud.workflow.biz.common.model.form.applybanquet.ApplyBanquetForm;
import com.taotao.cloud.workflow.biz.common.model.form.applybanquet.ApplyBanquetInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.ApplyBanquetEntity;
import com.taotao.cloud.workflow.biz.form.service.ApplyBanquetService;
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

/** 宴请申请 */
@Tag(tags = "宴请申请", value = "ApplyBanquet")
@RestController
@RequestMapping("/api/workflow/Form/ApplyBanquet")
public class ApplyBanquetController {

    @Autowired
    private ApplyBanquetService applyBanquetService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取宴请申请信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取宴请申请信息")
    @GetMapping("/{id}")
    public Result<ApplyBanquetInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ApplyBanquetInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), ApplyBanquetInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ApplyBanquetEntity entity = applyBanquetService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, ApplyBanquetInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建宴请申请
     *
     * @param applyBanquetForm 表单对象
     * @return
     */
    @Operation("新建宴请申请")
    @PostMapping
    public Result create(@RequestBody @Valid ApplyBanquetForm applyBanquetForm) throws WorkFlowException {
        if (applyBanquetForm.getBanquetNum() != null
                && StringUtil.isNotEmpty(applyBanquetForm.getBanquetNum())
                && !RegexUtils.checkDigit2(applyBanquetForm.getBanquetNum())) {
            return Result.fail("宴请人数必须大于0");
        }
        if (applyBanquetForm.getTotal() != null
                && StringUtil.isNotEmpty(applyBanquetForm.getTotal())
                && !RegexUtils.checkDigit2(applyBanquetForm.getTotal())) {
            return Result.fail("人员总数必须大于0");
        }
        if (applyBanquetForm.getExpectedCost() != null
                && !RegexUtils.checkDecimals2(String.valueOf(applyBanquetForm.getExpectedCost()))) {
            return Result.fail("预计费用必须大于0，最多只能有两位小数");
        }
        ApplyBanquetEntity entity = JacksonUtils.getJsonToBean(applyBanquetForm, ApplyBanquetEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(applyBanquetForm.getStatus())) {
            applyBanquetService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        applyBanquetService.submit(entity.getId(), entity, applyBanquetForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改宴请申请
     *
     * @param applyBanquetForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改宴请申请")
    @PutMapping("/{id}")
    public Result update(@RequestBody @Valid ApplyBanquetForm applyBanquetForm, @PathVariable("id") String id)
            throws WorkFlowException {
        if (applyBanquetForm.getBanquetNum() != null
                && StringUtil.isNotEmpty(applyBanquetForm.getBanquetNum())
                && !RegexUtils.checkDigit2(applyBanquetForm.getBanquetNum())) {
            return Result.fail("宴请人数必须大于0");
        }
        if (applyBanquetForm.getTotal() != null
                && StringUtil.isNotEmpty(applyBanquetForm.getTotal())
                && !RegexUtils.checkDigit2(applyBanquetForm.getTotal())) {
            return Result.fail("人员总数必须大于0");
        }
        if (applyBanquetForm.getExpectedCost() != null
                && !RegexUtils.checkDecimals2(String.valueOf(applyBanquetForm.getExpectedCost()))) {
            return Result.fail("预计费用必须大于0，最多只能有两位小数");
        }
        ApplyBanquetEntity entity = JacksonUtils.getJsonToBean(applyBanquetForm, ApplyBanquetEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(applyBanquetForm.getStatus())) {
            applyBanquetService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        applyBanquetService.submit(id, entity, applyBanquetForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
