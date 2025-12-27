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
import com.taotao.cloud.workflow.biz.common.model.form.outboundorder.OutboundEntryEntityInfoModel;
import com.taotao.cloud.workflow.biz.common.model.form.outboundorder.OutboundOrderInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.OutboundEntryEntity;
import com.taotao.cloud.workflow.biz.form.entity.OutboundOrderEntity;
import com.taotao.cloud.workflow.biz.form.service.OutboundOrderService;
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

/** 出库单 */
@Tag(tags = "出库单", value = "OutboundOrder")
@RestController
@RequestMapping("/api/workflow/Form/OutboundOrder")
public class OutboundOrderController {

    @Autowired
    private OutboundOrderService outboundOrderService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取出库单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取出库单信息")
    @GetMapping("/{id}")
    public Result<OutboundOrderInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        OutboundOrderInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), OutboundOrderInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            OutboundOrderEntity entity = outboundOrderService.getInfo(id);
            List<OutboundEntryEntity> entityList = outboundOrderService.getOutboundEntryList(id);
            vo = JacksonUtils.getJsonToBean(entity, OutboundOrderInfoVO.class);
            vo.setEntryList(JacksonUtils.getJsonToList(entityList, OutboundEntryEntityInfoModel.class));
        }
        return Result.success(vo);
    }

    /**
     * 新建出库单
     *
     * @param outboundOrderForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @Operation("新建出库单")
    @PostMapping
    public Result create(@RequestBody OutboundOrderForm outboundOrderForm) throws WorkFlowException {
        OutboundOrderEntity outbound = JacksonUtils.getJsonToBean(outboundOrderForm, OutboundOrderEntity.class);
        List<OutboundEntryEntity> outboundEntryList =
                JacksonUtils.getJsonToList(outboundOrderForm.getEntryList(), OutboundEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(outboundOrderForm.getStatus())) {
            outboundOrderService.save(outbound.getId(), outbound, outboundEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        outboundOrderService.submit(
                outbound.getId(), outbound, outboundEntryList, outboundOrderForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改出库单
     *
     * @param outboundOrderForm 表单对象
     * @param id 主键
     * @return
     * @throws WorkFlowException
     */
    @Operation("修改出库单")
    @PutMapping("/{id}")
    public Result update(@RequestBody OutboundOrderForm outboundOrderForm, @PathVariable("id") String id)
            throws WorkFlowException {
        OutboundOrderEntity outbound = JacksonUtils.getJsonToBean(outboundOrderForm, OutboundOrderEntity.class);
        List<OutboundEntryEntity> outboundEntryList =
                JacksonUtils.getJsonToList(outboundOrderForm.getEntryList(), OutboundEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(outboundOrderForm.getStatus())) {
            outboundOrderService.save(id, outbound, outboundEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        outboundOrderService.submit(id, outbound, outboundEntryList, outboundOrderForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
