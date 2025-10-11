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

package com.taotao.cloud.workflow.biz.engine.controller;

import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.api.vo.UserEntity;
import com.taotao.cloud.workflow.biz.common.base.vo.PaginationVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowmonitor.FlowMonitorListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowDeleteModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.PaginationFlowTask;
import com.taotao.cloud.workflow.biz.covert.FlowTaskConvert;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 流程监控 */
@Validated
@Tag(name = "工作流程-流程监控", description = "工作流程-流程监控")
@RestController
@RequestMapping("/api/workflow/engine/flow-monitor")
public class FlowMonitorController {

    @Autowired
    private FlowEngineService flowEngineService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private ServiceAllUtil serviceUtil;

    @Operation(summary = "获取流程监控列表", description = "获取流程监控列表")
    @GetMapping("/page")
    public Result<PageResult<FlowMonitorListVO>> list(PaginationFlowTask paginationFlowTask) {
        List<FlowTaskEntity> list = flowTaskService.getMonitorList(paginationFlowTask);
        List<FlowEngineEntity> engineList = flowEngineService.getFlowList(
                list.stream().map(FlowTaskEntity::getFlowId).toList());
        List<UserEntity> userList = serviceUtil.getUserName(
                list.stream().map(FlowTaskEntity::getCreatorUserId).toList());

        List<FlowMonitorListVO> listVO = new LinkedList<>();
        for (FlowTaskEntity taskEntity : list) {
            // 用户名称赋值
            FlowMonitorListVO vo = FlowTaskConvert.INSTANCE.convertMonitor(taskEntity);
            UserEntity user = userList.stream()
                    .filter(t -> t.getId().equals(taskEntity.getCreatorUserId()))
                    .findFirst()
                    .orElse(null);
            vo.setUserName(user != null ? user.getRealName() + "/" + user.getAccount() : "");
            FlowEngineEntity engine = engineList.stream()
                    .filter(t -> t.getId().equals(taskEntity.getFlowId()))
                    .findFirst()
                    .orElse(null);
            if (engine != null) {
                vo.setFormData(engine.getFormData());
                vo.setFormType(engine.getFormType());
                listVO.add(vo);
            }
        }
        PaginationVO paginationVO = JsonUtils.getJsonToBean(paginationFlowTask, PaginationVO.class);
        return Result.page(listVO, paginationVO);
    }

    @Operation(summary = "批量删除流程监控", description = "批量删除流程监控")
    @DeleteMapping
    public Result<Boolean> delete(@RequestBody FlowDeleteModel deleteModel) throws WorkFlowException {
        String[] taskId = deleteModel.getIds().split(",");
        flowTaskService.delete(taskId);
        return Result.success(true);
    }
}
