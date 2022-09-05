package com.taotao.cloud.quartz.one.controller;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.Res;
import cn.bootx.common.core.rest.ResResult;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.starter.quartz.core.service.QuartzJobLogService;
import cn.bootx.starter.quartz.dto.QuartzJobLogDto;
import cn.bootx.starter.quartz.param.QuartzJobLogQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
*
* @author xxm
* @date 2022/5/2
*/
@Tag(name = "定时任务执行日志")
@RestController
@RequestMapping("/quartz/log")
@RequiredArgsConstructor
public class QuartzJobLogController {
    private final QuartzJobLogService quartzJobLogService;


    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<QuartzJobLogDto>> page(PageParam pageParam, QuartzJobLogQuery param){
        return Res.ok(quartzJobLogService.page(pageParam,param));
    }

    @Operation(summary = "单条")
    @GetMapping("/findById")
    public ResResult<QuartzJobLogDto> findById(Long id){
        return Res.ok(quartzJobLogService.findById(id));
    }

}
