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
import com.taotao.cloud.workflow.biz.common.model.form.officesupplies.OfficeSuppliesForm;
import com.taotao.cloud.workflow.biz.common.model.form.officesupplies.OfficeSuppliesInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.OfficeSuppliesEntity;
import com.taotao.cloud.workflow.biz.form.service.OfficeSuppliesService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 领用办公用品申请表 */
@Tag(tags = "领用办公用品申请表", value = "OfficeSupplies")
@RestController
@RequestMapping("/api/workflow/Form/OfficeSupplies")
public class OfficeSuppliesController {

    @Autowired
    private OfficeSuppliesService officeSuppliesService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取领用办公用品申请表信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取领用办公用品申请表信息")
    @GetMapping("/{id}")
    public Result<OfficeSuppliesInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        OfficeSuppliesInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), OfficeSuppliesInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            OfficeSuppliesEntity entity = officeSuppliesService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, OfficeSuppliesInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建领用办公用品申请表
     *
     * @param officeSuppliesForm 表单对象
     * @return
     */
    @Operation("新建领用办公用品申请表")
    @PostMapping
    public Result create(@RequestBody OfficeSuppliesForm officeSuppliesForm) throws WorkFlowException {
        OfficeSuppliesEntity entity = JacksonUtils.getJsonToBean(officeSuppliesForm, OfficeSuppliesEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(officeSuppliesForm.getStatus())) {
            officeSuppliesService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        officeSuppliesService.submit(entity.getId(), entity, officeSuppliesForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改领用办公用品申请表
     *
     * @param officeSuppliesForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改领用办公用品申请表")
    @PutMapping("/{id}")
    public Result update(@RequestBody OfficeSuppliesForm officeSuppliesForm, @PathVariable("id") String id)
            throws WorkFlowException {
        OfficeSuppliesEntity entity = JacksonUtils.getJsonToBean(officeSuppliesForm, OfficeSuppliesEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(officeSuppliesForm.getStatus())) {
            officeSuppliesService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        officeSuppliesService.submit(id, entity, officeSuppliesForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
