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
import com.taotao.cloud.workflow.biz.common.model.form.finishedproduct.FinishedProductEntryEntityInfoModel;
import com.taotao.cloud.workflow.biz.common.model.form.finishedproduct.FinishedProductForm;
import com.taotao.cloud.workflow.biz.common.model.form.finishedproduct.FinishedProductInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.FinishedProductEntity;
import com.taotao.cloud.workflow.biz.form.entity.FinishedProductEntryEntity;
import com.taotao.cloud.workflow.biz.form.service.FinishedProductService;
import jakarta.validation.Valid;
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

/** 成品入库单 */
@Tag(tags = "成品入库单", value = "FinishedProduct")
@RestController
@RequestMapping("/api/workflow/Form/FinishedProduct")
public class FinishedProductController {

    @Autowired
    private FinishedProductService finishedProductService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取成品入库单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取成品入库单信息")
    @GetMapping("/{id}")
    public Result<FinishedProductInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        FinishedProductInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), FinishedProductInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            FinishedProductEntity entity = finishedProductService.getInfo(id);
            List<FinishedProductEntryEntity> entityList = finishedProductService.getFinishedEntryList(id);
            vo = JacksonUtils.getJsonToBean(entity, FinishedProductInfoVO.class);
            vo.setEntryList(JacksonUtils.getJsonToList(entityList, FinishedProductEntryEntityInfoModel.class));
        }
        return Result.success(vo);
    }

    /**
     * 新建成品入库单
     *
     * @param finishedProductForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @Operation("新建成品入库单")
    @PostMapping
    public Result create(@RequestBody @Valid FinishedProductForm finishedProductForm) throws WorkFlowException {
        FinishedProductEntity finished = JacksonUtils.getJsonToBean(finishedProductForm, FinishedProductEntity.class);
        List<FinishedProductEntryEntity> finishedEntryList =
                JacksonUtils.getJsonToList(finishedProductForm.getEntryList(), FinishedProductEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(finishedProductForm.getStatus())) {
            finishedProductService.save(finished.getId(), finished, finishedEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        finishedProductService.submit(
                finished.getId(), finished, finishedEntryList, finishedProductForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改成品入库单
     *
     * @param finishedProductForm 表单对象
     * @param id 主键
     * @return
     * @throws WorkFlowException
     */
    @Operation("修改成品入库单")
    @PutMapping("/{id}")
    public Result update(@RequestBody @Valid FinishedProductForm finishedProductForm, @PathVariable("id") String id)
            throws WorkFlowException {
        FinishedProductEntity finished = JacksonUtils.getJsonToBean(finishedProductForm, FinishedProductEntity.class);
        List<FinishedProductEntryEntity> finishedEntryList =
                JacksonUtils.getJsonToList(finishedProductForm.getEntryList(), FinishedProductEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(finishedProductForm.getStatus())) {
            finishedProductService.save(id, finished, finishedEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        finishedProductService.submit(id, finished, finishedEntryList, finishedProductForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
