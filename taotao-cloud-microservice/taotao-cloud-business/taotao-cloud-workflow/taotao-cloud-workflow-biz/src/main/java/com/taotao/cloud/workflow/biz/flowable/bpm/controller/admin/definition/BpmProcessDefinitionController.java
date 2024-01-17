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

package com.taotao.cloud.workflow.biz.flowable.bpm.controller.admin.definition;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionListReqVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionPageItemRespVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionPageReqVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionRespVO;
import com.taotao.cloud.flowable.biz.bpm.service.definition.BpmProcessDefinitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "管理后台 - 流程定义")
@RestController
@RequestMapping("/bpm/process-definition")
@Validated
public class BpmProcessDefinitionController {

    @Resource
    private BpmProcessDefinitionService bpmDefinitionService;

    @GetMapping("/page")
    @ApiOperation(value = "获得流程定义分页")
    @PreAuthorize("@ss.hasPermission('bpm:process-definition:query')")
    public CommonResult<PageResult<BpmProcessDefinitionPageItemRespVO>> getProcessDefinitionPage(
            BpmProcessDefinitionPageReqVO pageReqVO) {
        return success(bpmDefinitionService.getProcessDefinitionPage(pageReqVO));
    }

    @GetMapping("/list")
    @ApiOperation(value = "获得流程定义列表")
    @PreAuthorize("@ss.hasPermission('bpm:process-definition:query')")
    public CommonResult<List<BpmProcessDefinitionRespVO>> getProcessDefinitionList(
            BpmProcessDefinitionListReqVO listReqVO) {
        return success(bpmDefinitionService.getProcessDefinitionList(listReqVO));
    }

    @GetMapping("/get-bpmn-xml")
    @ApiOperation(value = "获得流程定义的 BPMN XML")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = String.class)
    @PreAuthorize("@ss.hasPermission('bpm:process-definition:query')")
    public CommonResult<String> getProcessDefinitionBpmnXML(@RequestParam("id") String id) {
        String bpmnXML = bpmDefinitionService.getProcessDefinitionBpmnXML(id);
        return success(bpmnXML);
    }
}
