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

package com.taotao.cloud.job.biz.quartz.controller;

import cn.hutool.core.bean.BeanUtil;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.job.api.model.vo.QuartzJobVO;
import com.taotao.cloud.job.biz.quartz.entity.QuartzJobEntity;
import com.taotao.cloud.job.biz.quartz.param.QuartzJobDTO;
import com.taotao.cloud.job.biz.quartz.param.QuartzJobQuery;
import com.taotao.cloud.job.biz.quartz.service.QuartzJobService;
import com.taotao.cloud.job.quartz.utils.CronUtils;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 石英工作控制器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-09 15:18:19
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/job/quartz")
@Tag(name = "quartz任务管理API", description = "quartz任务管理API")
public class QuartzJobController {

    private final QuartzJobService quartzJobService;

    @PostMapping("/job")
    @Operation(summary = "添加任务", description = "添加任务")
    @RequestLogger
    public Result<Boolean> add(@RequestBody QuartzJobDTO quartzJobDTO) {
        if (!CronUtils.isValid(quartzJobDTO.getCronExpression())) {
            return Result.fail("操作失败，Cron表达式不正确");
        }
        quartzJobService.addJob(quartzJobDTO);
        return Result.success(true);
    }

    @PutMapping("/job")
    @Operation(summary = "更新任务", description = "更新任务")
    @RequestLogger
    public Result<Boolean> update(@RequestBody QuartzJobDTO quartzJobDTO) {
        if (!CronUtils.isValid(quartzJobDTO.getCronExpression())) {
            return Result.fail("操作失败，Cron表达式不正确");
        }
        quartzJobService.updateJob(quartzJobDTO);
        return Result.success(true);
    }

    @GetMapping("/job/page")
    @Operation(summary = "分页查询任务列表", description = "分页查询任务列表")
    @RequestLogger
    public Result<PageResult<QuartzJobVO>> page(QuartzJobQuery quartzJobQuery) {
        return Result.success(quartzJobService.page(quartzJobQuery));
    }

    @GetMapping("/job/{id}")
    @Operation(summary = "单个任务", description = "单个任务")
    @RequestLogger
    public Result<QuartzJobVO> findById(@PathVariable Long id) {
        QuartzJobEntity quartzJob = quartzJobService.findById(id);

        QuartzJobVO quartzJobVO = BeanUtil.copyProperties(quartzJob, QuartzJobVO.class);
        return Result.success(quartzJobVO);
    }

    @PostMapping("/job/start/{id}")
    @Operation(summary = "启动任务", description = "启动任务")
    @RequestLogger
    public Result<Boolean> start(@PathVariable Long id) {
        quartzJobService.start(id);
        return Result.success(true);
    }

    @PostMapping("/job/stop/{id}")
    @Operation(summary = "停止任务", description = "停止任务")
    @RequestLogger
    public Result<Boolean> stop(@PathVariable Long id) {
        quartzJobService.stopJob(id);
        return Result.success(true);
    }

    @PostMapping("/job/execute/{id}")
    @Operation(summary = "立即执行任务", description = "立即执行任务")
    @RequestLogger
    public Result<Boolean> execute(@PathVariable Long id) {
        quartzJobService.runOnce(id);
        return Result.success(true);
    }

    @DeleteMapping("/job/{id}")
    @Operation(summary = "删除任务", description = "删除任务")
    @RequestLogger
    public Result<Boolean> delete(@PathVariable Long id) {
        quartzJobService.deleteJob(id);
        return Result.success(true);
    }

    @GetMapping("/job/judge-job-class")
    @Operation(summary = "判断是否是定时任务类", description = "判断是否是定时任务类")
    @RequestLogger
    public Result<String> judgeJobClass(String jobClassName) {
        return Result.success(quartzJobService.judgeJobClass(jobClassName));
    }

    @PostMapping("/job/sync-job-status")
    @Operation(summary = "同步定时任务状态", description = "同步定时任务状态")
    @RequestLogger
    public Result<Boolean> syncJobStatus() {
        quartzJobService.syncJobStatus();
        return Result.success(true);
    }
}
