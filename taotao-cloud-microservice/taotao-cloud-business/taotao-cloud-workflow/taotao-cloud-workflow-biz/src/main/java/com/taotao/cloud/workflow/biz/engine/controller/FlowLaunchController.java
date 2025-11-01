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
import com.taotao.cloud.workflow.biz.common.base.vo.PaginationVO;
import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import com.taotao.cloud.workflow.biz.common.model.engine.FlowHandleModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowlaunch.FlowLaunchListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.PaginationFlowTask;
import com.taotao.cloud.workflow.biz.covert.FlowTaskConvert;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNewService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.FlowNature;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 流程发起 */
@Validated
@Tag(name = "工作流程-流程发起", description = "工作流程-流程发起")
@RestController
@RequestMapping("/api/workflow/engine/flow-launch")
public class FlowLaunchController {

    @Autowired
    private FlowEngineService flowEngineService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private FlowTaskNewService flowTaskNewService;

    @Operation(summary = "获取流程发起列表", description = "获取流程发起列表(带分页)")
    @GetMapping("/page")
    public Result<PageResult<FlowLaunchListVO>> list(PaginationFlowTask paginationFlowTask) {
        List<FlowTaskEntity> data = flowTaskService.getLaunchList(paginationFlowTask);
        List<FlowEngineEntity> engineList = flowEngineService.getFlowList(
                data.stream().map(FlowTaskEntity::getFlowId).toList());
        List<FlowLaunchListVO> listVO = new LinkedList<>();
        for (FlowTaskEntity taskEntity : data) {
            // 用户名称赋值
            FlowLaunchListVO vo = FlowTaskConvert.INSTANCE.convertLaunch(taskEntity);
            FlowEngineEntity entity = engineList.stream()
                    .filter(t -> t.getId().equals(taskEntity.getFlowId()))
                    .findFirst()
                    .orElse(null);
            if (entity != null) {
                vo.setFormData(entity.getFormData());
                vo.setFormType(entity.getFormType());
            }
            listVO.add(vo);
        }
        PaginationVO paginationVO = JsonUtils.getJsonToBean(paginationFlowTask, PaginationVO.class);
        return Result.page(listVO, paginationVO);
    }

    @Operation(summary = "删除流程发起", description = "删除流程发起")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") String id) throws WorkFlowException {
        FlowTaskEntity entity = flowTaskService.getInfo(id);
        if (entity != null) {
            if (entity.getFlowType() == 1) {
                return Result.fail("功能流程不能删除");
            }
            if (!FlowNature.ParentId.equals(entity.getParentId()) && StrUtil.isNotEmpty(entity.getParentId())) {
                return Result.fail("子表数据不能删除");
            }
            flowTaskService.delete(entity);
            return Result.success(MsgCode.SU003.get());
        }
        return Result.fail(MsgCode.FA003.get());
    }

    @Operation(summary = "发起催办", description = "待我审核催办")
    @PostMapping("/actions/press/{id}")
    public Result<String> press(@PathVariable("id") String id) throws WorkFlowException {
        boolean flag = flowTaskNewService.press(id);
        if (flag) {
            return Result.success("催办成功");
        }
        return Result.fail("未找到催办人");
    }

    @Operation(summary = "撤回流程发起", description = "注意：在撤销流程时要保证你的下一节点没有处理这条记录；如已处理则无法撤销流程。")
    @PutMapping("/actions/withdraw/{id}")
    public Result<String> revoke(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel)
            throws WorkFlowException {
        FlowTaskEntity flowTaskEntity = flowTaskService.getInfo(id);
        FlowModel flowModel = FlowTaskConvert.INSTANCE.convert(flowHandleModel);
        flowTaskNewService.revoke(flowTaskEntity, flowModel);
        return Result.success("撤回成功");
    }
}
