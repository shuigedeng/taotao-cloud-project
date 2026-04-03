package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdframe.forge.plugin.system.entity.SysLoginLog;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysLoginLogMapper;
import com.mdframe.forge.starter.core.domain.PageQuery;

import cn.hutool.core.util.StrUtil;
import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 登录日志查询接口
 */
@RestController
@RequestMapping("/system/loginLog")
@RequiredArgsConstructor
@ApiDecrypt
@ApiEncrypt
public class SysLoginLogController {

    private final SysLoginLogMapper loginLogMapper;

    /**
     * 分页查询登录日志
     */
    @GetMapping("/page")
    public Result<Page<SysLoginLog>> page(
            PageQuery pageQuery,
            SysLoginLog query,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        
        if (query != null) {
            // 用户名模糊查询
            wrapper.like(StrUtil.isNotBlank(query.getUsername()),
                        SysLoginLog::getUsername, query.getUsername());
            
            // 登录类型
            wrapper.eq(StrUtil.isNotBlank(query.getLoginType()),
                      SysLoginLog::getLoginType, query.getLoginType());
            
            // 登录状态
            wrapper.eq(query.getLoginStatus() != null,
                      SysLoginLog::getLoginStatus, query.getLoginStatus());
            
            // 登录IP模糊查询
            wrapper.like(StrUtil.isNotBlank(query.getLoginIp()),
                        SysLoginLog::getLoginIp, query.getLoginIp());
            
            // 浏览器
            wrapper.like(StrUtil.isNotBlank(query.getBrowser()),
                        SysLoginLog::getBrowser, query.getBrowser());
            
            // 操作系统
            wrapper.like(StrUtil.isNotBlank(query.getOs()),
                        SysLoginLog::getOs, query.getOs());
        }
        
        // 时间范围查询
        wrapper.ge(StrUtil.isNotBlank(startTime), SysLoginLog::getLoginTime, startTime);
        wrapper.le(StrUtil.isNotBlank(endTime), SysLoginLog::getLoginTime, endTime);
        
        // 按登录时间倒序排列
        wrapper.orderByDesc(SysLoginLog::getLoginTime);
        
        Page<SysLoginLog> page = loginLogMapper.selectPage(pageQuery.toPage(), wrapper);
        return Result.success(page);
    }

    /**
     * 查询登录日志详情
     */
    @GetMapping("/{id}")
    public Result<SysLoginLog> detail(@PathVariable Long id) {
        SysLoginLog log = loginLogMapper.selectById(id);
        return Result.success(log);
    }
}
