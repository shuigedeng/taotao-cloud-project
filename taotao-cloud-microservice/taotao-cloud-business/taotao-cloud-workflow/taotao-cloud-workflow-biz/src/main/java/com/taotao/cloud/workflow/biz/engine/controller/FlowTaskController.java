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

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskForm;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskInfoVO;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowDynamicService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "工作流程-流程任务", description = "工作流程-流程任务")
@RestController
@RequestMapping("/api/workflow/engine/flow-task")
public class FlowTaskController {

    @Autowired
    private FlowDynamicService flowDynamicService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Operation(summary = "动态表单信息", description = "动态表单信息")
    @GetMapping("/{id}")
    public Result<FlowTaskInfoVO> dataInfo(@PathVariable("id") String id, String taskOperatorId)
            throws WorkFlowException {
        FlowTaskEntity entity = flowTaskService.getInfo(id);
        FlowTaskInfoVO vo = flowDynamicService.info(entity, taskOperatorId);
        return Result.success(vo);
    }

    @Operation(summary = "保存动态表单", description = "保存动态表单")
    @PostMapping
    public Result<Boolean> save(@RequestBody FlowTaskForm flowTaskForm) throws WorkFlowException {
        if (FlowStatusEnum.save.getMessage().equals(flowTaskForm.getStatus())) {
            flowDynamicService.save(null, flowTaskForm);
            return Result.success(true);
        }

        flowDynamicService.submit(null, flowTaskForm);
        return Result.success(true);
    }

    @Operation(summary = "更新动态表单", description = "更新动态表单")
    @PutMapping("/{id}")
    public Result<Boolean> submit(@RequestBody FlowTaskForm flowTaskForm, @PathVariable("id") String id)
            throws WorkFlowException {
        if (FlowStatusEnum.save.getMessage().equals(flowTaskForm.getStatus())) {
            flowDynamicService.save(id, flowTaskForm);
            return Result.success(true);
        }

        flowDynamicService.submit(id, flowTaskForm);
        return Result.success(true);
    }

    @Operation(summary = "动态表单详情", description = "动态表单详情")
    @GetMapping("/{flowId}/{id}")
    public Result<Map<String, Object>> info(
            @Parameter(description = "引擎主键值") @NotNull(message = "引擎主键值不能为空") @PathVariable("flowId") String flowId,
            @Parameter(description = "id") @NotNull(message = "主键值不能为空") @PathVariable("id") String id)
            throws WorkFlowException {
        Map<String, Object> data = flowDynamicService.getData(flowId, id);
        return Result.success(data);
    }
}
