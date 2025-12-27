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
import com.taotao.cloud.workflow.biz.common.model.form.purchaselist.PurchaseListEntryEntityInfoModel;
import com.taotao.cloud.workflow.biz.common.model.form.purchaselist.PurchaseListForm;
import com.taotao.cloud.workflow.biz.common.model.form.purchaselist.PurchaseListInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.PurchaseListEntity;
import com.taotao.cloud.workflow.biz.form.entity.PurchaseListEntryEntity;
import com.taotao.cloud.workflow.biz.form.service.PurchaseListService;
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

/** 日常物品采购清单 */
@Tag(tags = "日常物品采购清单", value = "PurchaseList")
@RestController
@RequestMapping("/api/workflow/Form/PurchaseList")
public class PurchaseListController {

    @Autowired
    private PurchaseListService purchaseListService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取日常物品采购清单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取日常物品采购清单信息")
    @GetMapping("/{id}")
    public Result<PurchaseListInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        PurchaseListInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), PurchaseListInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            PurchaseListEntity entity = purchaseListService.getInfo(id);
            List<PurchaseListEntryEntity> entityList = purchaseListService.getPurchaseEntryList(id);
            vo = JacksonUtils.getJsonToBean(entity, PurchaseListInfoVO.class);
            vo.setEntryList(JacksonUtils.getJsonToList(entityList, PurchaseListEntryEntityInfoModel.class));
        }
        return Result.success(vo);
    }

    /**
     * 新建日常物品采购清单
     *
     * @param purchaseListForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @Operation("新建日常物品采购清单")
    @PostMapping
    public Result create(@RequestBody PurchaseListForm purchaseListForm) throws WorkFlowException {
        PurchaseListEntity procurement = JacksonUtils.getJsonToBean(purchaseListForm, PurchaseListEntity.class);
        List<PurchaseListEntryEntity> procurementEntryList =
                JacksonUtils.getJsonToList(purchaseListForm.getEntryList(), PurchaseListEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(purchaseListForm.getStatus())) {
            purchaseListService.save(procurement.getId(), procurement, procurementEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        purchaseListService.submit(
                procurement.getId(), procurement, procurementEntryList, purchaseListForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改日常物品采购清单
     *
     * @param purchaseListForm 表单对象
     * @param id 主键
     * @return
     * @throws WorkFlowException
     */
    @Operation("修改日常物品采购清单")
    @PutMapping("/{id}")
    public Result update(@RequestBody PurchaseListForm purchaseListForm, @PathVariable("id") String id)
            throws WorkFlowException {
        PurchaseListEntity procurement = JacksonUtils.getJsonToBean(purchaseListForm, PurchaseListEntity.class);
        List<PurchaseListEntryEntity> procurementEntryList =
                JacksonUtils.getJsonToList(purchaseListForm.getEntryList(), PurchaseListEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(purchaseListForm.getStatus())) {
            purchaseListService.save(id, procurement, procurementEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        purchaseListService.submit(id, procurement, procurementEntryList, purchaseListForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
