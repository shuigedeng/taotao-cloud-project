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

import com.taotao.cloud.workflow.biz.common.model.form.salesorder.SalesOrderInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.service.SalesOrderService;
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

/** 销售订单 */
@Tag(tags = "销售订单", value = "SalesOrder")
@RestController
@RequestMapping("/api/workflow/Form/SalesOrder")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取销售订单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取销售订单信息")
    @GetMapping("/{id}")
    public Result<SalesOrderInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        SalesOrderInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), SalesOrderInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            SalesOrderEntity entity = salesOrderService.getInfo(id);
            List<SalesOrderEntryEntity> entityList = salesOrderService.getSalesEntryList(id);
            vo = JsonUtil.getJsonToBean(entity, SalesOrderInfoVO.class);
            vo.setEntryList(JsonUtil.getJsonToList(entityList, SalesOrderEntryEntityInfoModel.class));
        }
        return Result.success(vo);
    }

    /**
     * 新建销售订单
     *
     * @param salesOrderForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @Operation("新建销售订单")
    @PostMapping
    public Result create(@RequestBody SalesOrderForm salesOrderForm) throws WorkFlowException {
        SalesOrderEntity sales = JsonUtil.getJsonToBean(salesOrderForm, SalesOrderEntity.class);
        List<SalesOrderEntryEntity> salesEntryList =
                JsonUtil.getJsonToList(salesOrderForm.getEntryList(), SalesOrderEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(salesOrderForm.getStatus())) {
            salesOrderService.save(sales.getId(), sales, salesEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        salesOrderService.submit(sales.getId(), sales, salesEntryList, salesOrderForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改销售订单
     *
     * @param salesOrderForm 表单对象
     * @param id 主键
     * @return
     * @throws WorkFlowException
     */
    @Operation("修改销售订单")
    @PutMapping("/{id}")
    public Result update(@RequestBody SalesOrderForm salesOrderForm, @PathVariable("id") String id)
            throws WorkFlowException {
        SalesOrderEntity sales = JsonUtil.getJsonToBean(salesOrderForm, SalesOrderEntity.class);
        List<SalesOrderEntryEntity> salesEntryList =
                JsonUtil.getJsonToList(salesOrderForm.getEntryList(), SalesOrderEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(salesOrderForm.getStatus())) {
            salesOrderService.save(id, sales, salesEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        salesOrderService.submit(id, sales, salesEntryList, salesOrderForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
