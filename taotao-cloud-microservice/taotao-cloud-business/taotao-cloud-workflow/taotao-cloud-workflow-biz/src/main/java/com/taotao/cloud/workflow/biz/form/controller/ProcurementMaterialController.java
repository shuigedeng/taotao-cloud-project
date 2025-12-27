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
import com.taotao.cloud.workflow.biz.common.model.form.procurementmaterial.ProcurementEntryEntityInfoModel;
import com.taotao.cloud.workflow.biz.common.model.form.procurementmaterial.ProcurementMaterialForm;
import com.taotao.cloud.workflow.biz.common.model.form.procurementmaterial.ProcurementMaterialInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.ProcurementEntryEntity;
import com.taotao.cloud.workflow.biz.form.entity.ProcurementMaterialEntity;
import com.taotao.cloud.workflow.biz.form.service.ProcurementMaterialService;
import java.util.List;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 采购原材料 */
@Tag(tags = "采购原材料", value = "ProcurementMaterial")
@RestController
@RequestMapping("/api/workflow/Form/ProcurementMaterial")
public class ProcurementMaterialController {

    @Autowired
    private ProcurementMaterialService procurementMaterialService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取采购原材料信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取采购原材料信息")
    @GetMapping("/{id}")
    public Result<ProcurementMaterialInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        ProcurementMaterialInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), ProcurementMaterialInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ProcurementMaterialEntity entity = procurementMaterialService.getInfo(id);
            List<ProcurementEntryEntity> entityList = procurementMaterialService.getProcurementEntryList(id);
            vo = JacksonUtils.getJsonToBean(entity, ProcurementMaterialInfoVO.class);
            vo.setEntryList(JacksonUtils.getJsonToList(entityList, ProcurementEntryEntityInfoModel.class));
        }
        return Result.success(vo);
    }

    /**
     * 新建采购原材料
     *
     * @param procurementMaterialForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @Operation("新建采购原材料")
    @PostMapping
    public Result create(@RequestBody ProcurementMaterialForm procurementMaterialForm) throws WorkFlowException {
        ProcurementMaterialEntity procurement =
                JacksonUtils.getJsonToBean(procurementMaterialForm, ProcurementMaterialEntity.class);
        List<ProcurementEntryEntity> procurementEntryList =
                JacksonUtils.getJsonToList(procurementMaterialForm.getEntryList(), ProcurementEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(procurementMaterialForm.getStatus())) {
            procurementMaterialService.save(procurement.getId(), procurement, procurementEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        procurementMaterialService.submit(
                procurement.getId(), procurement, procurementEntryList, procurementMaterialForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改采购原材料
     *
     * @param procurementMaterialForm 表单对象
     * @param id 主键
     * @return
     * @throws WorkFlowException
     */
    @Operation("修改采购原材料")
    @PutMapping("/{id}")
    public Result update(@RequestBody ProcurementMaterialForm procurementMaterialForm, @PathVariable("id") String id)
            throws WorkFlowException {
        ProcurementMaterialEntity procurement =
                JacksonUtils.getJsonToBean(procurementMaterialForm, ProcurementMaterialEntity.class);
        List<ProcurementEntryEntity> procurementEntryList =
                JacksonUtils.getJsonToList(procurementMaterialForm.getEntryList(), ProcurementEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(procurementMaterialForm.getStatus())) {
            procurementMaterialService.save(id, procurement, procurementEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        procurementMaterialService.submit(
                id, procurement, procurementEntryList, procurementMaterialForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
