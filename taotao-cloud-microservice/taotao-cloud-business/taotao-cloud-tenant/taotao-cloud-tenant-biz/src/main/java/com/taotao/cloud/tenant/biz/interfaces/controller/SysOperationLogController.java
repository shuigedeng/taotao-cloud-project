package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdframe.forge.plugin.system.entity.SysOperationLog;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysOperationLogMapper;
import com.mdframe.forge.starter.core.domain.PageQuery;

import cn.hutool.core.util.StrUtil;
import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志查询接口
 */
@RestController
@RequestMapping("/system/operationLog")
@RequiredArgsConstructor
@ApiDecrypt
@ApiEncrypt
public class SysOperationLogController {

    private final SysOperationLogMapper operationLogMapper;

    /**
     * 分页查询操作日志
     */
    @GetMapping("/page")
    public Result<Page<SysOperationLog>> page(
            PageQuery pageQuery,
            SysOperationLog query,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();
        
        if (query != null) {
            // 用户名模糊查询
            wrapper.like(StrUtil.isNotBlank(query.getUsername()),
                        SysOperationLog::getUsername, query.getUsername());
            
            // 操作模块
            wrapper.like(StrUtil.isNotBlank(query.getOperationModule()),
                        SysOperationLog::getOperationModule, query.getOperationModule());
            
            // 操作类型
            wrapper.eq(StrUtil.isNotBlank(query.getOperationType()),
                      SysOperationLog::getOperationType, query.getOperationType());
            
            // 操作状态
            wrapper.eq(query.getOperationStatus() != null,
                      SysOperationLog::getOperationStatus, query.getOperationStatus());
            
            // 操作IP模糊查询
            wrapper.like(StrUtil.isNotBlank(query.getOperationIp()),
                        SysOperationLog::getOperationIp, query.getOperationIp());
            
            // 请求URL模糊查询
            wrapper.like(StrUtil.isNotBlank(query.getRequestUrl()),
                        SysOperationLog::getRequestUrl, query.getRequestUrl());
            
            // 请求参数模糊查询
            wrapper.like(StrUtil.isNotBlank(query.getRequestParams()),
                        SysOperationLog::getRequestParams, query.getRequestParams());
        }
        
        // 时间范围查询
        wrapper.ge(StrUtil.isNotBlank(startTime), SysOperationLog::getOperationTime, startTime);
        wrapper.le(StrUtil.isNotBlank(endTime), SysOperationLog::getOperationTime, endTime);
        
        // 按操作时间倒序排列
        wrapper.orderByDesc(SysOperationLog::getOperationTime);
        
        Page<SysOperationLog> page = operationLogMapper.selectPage(pageQuery.toPage(), wrapper);
        return Result.success(page);
    }

    /**
     * 查询操作日志详情
     */
    @GetMapping("/{id}")
    public Result<SysOperationLog> detail(@PathVariable Long id) {
        SysOperationLog log = operationLogMapper.selectById(id);
        return Result.success(log);
    }
}
