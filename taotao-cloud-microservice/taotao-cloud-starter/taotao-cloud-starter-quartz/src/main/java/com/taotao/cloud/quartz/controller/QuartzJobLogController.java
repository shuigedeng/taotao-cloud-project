package com.taotao.cloud.quartz.controller;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.quartz.vo.QuartzJobLogVO;
import com.taotao.cloud.quartz.param.QuartzJobLogQuery;
import com.taotao.cloud.quartz.service.QuartzJobLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "定时任务执行日志")
@RestController
@RequestMapping("/quartz/log")
@RequiredArgsConstructor
public class QuartzJobLogController {
    private final QuartzJobLogService quartzJobLogService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<PageResult<QuartzJobLogVO>> page(QuartzJobLogQuery quartzJobLogQuery){
        return Result.success(quartzJobLogService.page(quartzJobLogQuery));
    }

    @Operation(summary = "单条")
    @GetMapping("/findById")
    public Result<QuartzJobLogVO> findById(Long id){
        return Result.success(quartzJobLogService.findById(id));
    }

}
