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

package com.taotao.cloud.workflow.biz.flowable.bpm.controller.admin.task;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.task.vo.activity.BpmActivityRespVO;
import com.taotao.cloud.flowable.biz.bpm.service.task.BpmActivityService;
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

@Api(tags = "管理后台 - 流程活动实例")
@RestController
@RequestMapping("/bpm/activity")
@Validated
public class BpmActivityController {

    @Resource
    private BpmActivityService activityService;

    @GetMapping("/list")
    @ApiOperation(
            value = "生成指定流程实例的高亮流程图",
            notes = "只高亮进行中的任务。不过要注意，该接口暂时没用，通过前端的 ProcessViewer.vue 界面的 highlightDiagram 方法生成")
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例的编号", required = true, dataTypeClass = String.class)
    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    public CommonResult<List<BpmActivityRespVO>> getActivityList(
            @RequestParam("processInstanceId") String processInstanceId) {
        return success(activityService.getActivityListByProcessInstanceId(processInstanceId));
    }
}
