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
import com.taotao.cloud.workflow.biz.common.model.form.warehousereceipt.WarehouseReceiptEntityInfoModel;
import com.taotao.cloud.workflow.biz.common.model.form.warehousereceipt.WarehouseReceiptForm;
import com.taotao.cloud.workflow.biz.common.model.form.warehousereceipt.WarehouseReceiptInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.WarehouseEntryEntity;
import com.taotao.cloud.workflow.biz.form.entity.WarehouseReceiptEntity;
import com.taotao.cloud.workflow.biz.form.service.WarehouseReceiptService;
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

/** 入库申请单 */
@Tag(tags = "入库申请单", value = "WarehouseReceipt")
@RestController
@RequestMapping("/api/workflow/Form/WarehouseReceipt")
public class WarehouseReceiptController {

    @Autowired
    private WarehouseReceiptService warehouseReceiptService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取入库申请单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取入库申请单信息")
    @GetMapping("/{id}")
    public Result<WarehouseReceiptInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        WarehouseReceiptInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), WarehouseReceiptInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            WarehouseReceiptEntity entity = warehouseReceiptService.getInfo(id);
            List<WarehouseEntryEntity> entityList = warehouseReceiptService.getWarehouseEntryList(id);
            vo = JacksonUtils.getJsonToBean(entity, WarehouseReceiptInfoVO.class);
            vo.setEntryList(JacksonUtils.getJsonToList(entityList, WarehouseReceiptEntityInfoModel.class));
        }
        return Result.success(vo);
    }

    /**
     * 新建入库申请单
     *
     * @param warehouseReceiptForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @Operation("新建入库申请单")
    @PostMapping
    public Result create(@RequestBody WarehouseReceiptForm warehouseReceiptForm) throws WorkFlowException {
        WarehouseReceiptEntity warehouse = JacksonUtils.getJsonToBean(warehouseReceiptForm, WarehouseReceiptEntity.class);
        List<WarehouseEntryEntity> warehouseEntryList =
                JacksonUtils.getJsonToList(warehouseReceiptForm.getEntryList(), WarehouseEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(warehouseReceiptForm.getStatus())) {
            warehouseReceiptService.save(warehouse.getId(), warehouse, warehouseEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        warehouseReceiptService.submit(
                warehouse.getId(), warehouse, warehouseEntryList, warehouseReceiptForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改入库申请单
     *
     * @param warehouseReceiptForm 表单对象
     * @param id 主键
     * @return
     * @throws WorkFlowException
     */
    @Operation("修改入库申请单")
    @PutMapping("/{id}")
    public Result update(@RequestBody WarehouseReceiptForm warehouseReceiptForm, @PathVariable("id") String id)
            throws WorkFlowException {
        WarehouseReceiptEntity warehouse = JacksonUtils.getJsonToBean(warehouseReceiptForm, WarehouseReceiptEntity.class);
        List<WarehouseEntryEntity> warehouseEntryList =
                JacksonUtils.getJsonToList(warehouseReceiptForm.getEntryList(), WarehouseEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(warehouseReceiptForm.getStatus())) {
            warehouseReceiptService.save(id, warehouse, warehouseEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        warehouseReceiptService.submit(id, warehouse, warehouseEntryList, warehouseReceiptForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
