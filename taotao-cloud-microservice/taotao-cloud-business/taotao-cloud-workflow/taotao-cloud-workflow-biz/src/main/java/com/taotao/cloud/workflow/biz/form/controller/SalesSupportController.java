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
import com.taotao.cloud.workflow.biz.common.model.form.salessupport.SalesSupportForm;
import com.taotao.cloud.workflow.biz.common.model.form.salessupport.SalesSupportInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.SalesSupportEntity;
import com.taotao.cloud.workflow.biz.form.service.SalesSupportService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 销售支持表 */
@Tag(tags = "销售支持表", value = "SalesSupport")
@RestController
@RequestMapping("/api/workflow/Form/SalesSupport")
public class SalesSupportController {

    @Autowired
    private SalesSupportService salesSupportService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取销售支持表信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取销售支持表信息")
    @GetMapping("/{id}")
    public Result<SalesSupportInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        SalesSupportInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), SalesSupportInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            SalesSupportEntity entity = salesSupportService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, SalesSupportInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建销售支持表
     *
     * @param salesSupportForm 表单对象
     * @return
     */
    @Operation("新建保存销售支持表")
    @PostMapping
    public Result create(@RequestBody SalesSupportForm salesSupportForm) throws WorkFlowException {
        SalesSupportEntity entity = JacksonUtils.getJsonToBean(salesSupportForm, SalesSupportEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(salesSupportForm.getStatus())) {
            salesSupportService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        salesSupportService.submit(entity.getId(), entity, salesSupportForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改销售支持表
     *
     * @param salesSupportForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改销售支持表")
    @PutMapping("/{id}")
    public Result update(@RequestBody SalesSupportForm salesSupportForm, @PathVariable("id") String id)
            throws WorkFlowException {
        SalesSupportEntity entity = JacksonUtils.getJsonToBean(salesSupportForm, SalesSupportEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(salesSupportForm.getStatus())) {
            salesSupportService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        salesSupportService.submit(id, entity, salesSupportForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
