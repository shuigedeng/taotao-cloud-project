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
import com.taotao.cloud.workflow.biz.common.model.form.rewardpunishment.RewardPunishmentForm;
import com.taotao.cloud.workflow.biz.common.model.form.rewardpunishment.RewardPunishmentInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.RewardPunishmentEntity;
import com.taotao.cloud.workflow.biz.form.service.RewardPunishmentService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 行政赏罚单 */
@Tag(tags = "行政赏罚单", value = "RewardPunishment")
@RestController
@RequestMapping("/api/workflow/Form/RewardPunishment")
public class RewardPunishmentController {

    @Autowired
    private RewardPunishmentService rewardPunishmentService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    ;

    /**
     * 获取行政赏罚单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取行政赏罚单信息")
    @GetMapping("/{id}")
    public Result<RewardPunishmentInfoVO> info(@PathVariable("id") String id, String taskOperatorId)
            throws DataException {
        RewardPunishmentInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JacksonUtils.getJsonToBean(operator.getDraftData(), RewardPunishmentInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            RewardPunishmentEntity entity = rewardPunishmentService.getInfo(id);
            vo = JacksonUtils.getJsonToBean(entity, RewardPunishmentInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建行政赏罚单
     *
     * @param rewardPunishmentForm 表单对象
     * @return
     */
    @Operation("新建行政赏罚单")
    @PostMapping
    public Result create(@RequestBody RewardPunishmentForm rewardPunishmentForm) throws WorkFlowException {
        RewardPunishmentEntity entity = JacksonUtils.getJsonToBean(rewardPunishmentForm, RewardPunishmentEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(rewardPunishmentForm.getStatus())) {
            rewardPunishmentService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        rewardPunishmentService.submit(entity.getId(), entity, rewardPunishmentForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改行政赏罚单
     *
     * @param rewardPunishmentForm 表单对象
     * @param id 主键
     * @return
     */
    @Operation("修改行政赏罚单")
    @PutMapping("/{id}")
    public Result update(@RequestBody RewardPunishmentForm rewardPunishmentForm, @PathVariable("id") String id)
            throws WorkFlowException {
        RewardPunishmentEntity entity = JacksonUtils.getJsonToBean(rewardPunishmentForm, RewardPunishmentEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(rewardPunishmentForm.getStatus())) {
            rewardPunishmentService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        rewardPunishmentService.submit(id, entity, rewardPunishmentForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
