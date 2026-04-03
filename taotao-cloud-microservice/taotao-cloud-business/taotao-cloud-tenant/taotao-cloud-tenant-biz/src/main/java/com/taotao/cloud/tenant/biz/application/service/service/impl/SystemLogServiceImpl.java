package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysLoginLog;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysOperationLog;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysUser;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysLoginLogMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysOperationLogMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysUserMapper;
import com.mdframe.forge.starter.log.domain.LoginLogInfo;
import com.mdframe.forge.starter.log.domain.OperationLogInfo;
import com.mdframe.forge.starter.log.service.ILogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 系统日志服务实现
 * 实现ILogService接口，持久化日志到数据库
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemLogServiceImpl implements ILogService {

    private final SysOperationLogMapper operationLogMapper;
    private final SysLoginLogMapper loginLogMapper;
    private final SysUserMapper sysUserMapper;

    @Override
    public void saveOperationLog(OperationLogInfo logInfo) {
        try {
            // 补充用户信息（从Session中获取）
            if (logInfo.getUserId() != null) {
                try {
                    SysUser sysUser = sysUserMapper.selectById(logInfo.getUserId());
                    logInfo.setUsername(sysUser.getUsername());
                    logInfo.setTenantId(sysUser.getTenantId());
                } catch (Exception e) {
                    log.debug("从Session获取用户信息失败", e);
                }
            }
            // 转换并保存
            SysOperationLog entity = new SysOperationLog();
            BeanUtil.copyProperties(logInfo, entity);
            operationLogMapper.insert(entity);

            log.debug("操作日志保存成功: userId={}, module={}, type={}",
                    logInfo.getUserId(), logInfo.getOperationModule(), logInfo.getOperationType());
        } catch (Exception e) {
            log.error("操作日志保存失败", e);
        }
    }

    @Override
    public void saveLoginLog(LoginLogInfo logInfo) {
        try {
            if (logInfo.getUserId() != null) {
                try {
                    SysUser sysUser = sysUserMapper.selectById(logInfo.getUserId());
                    logInfo.setUsername(sysUser.getUsername());
                    logInfo.setTenantId(sysUser.getTenantId());
                } catch (Exception e) {
                    log.debug("从Session获取用户信息失败", e);
                }
            }
            // 转换并保存
            SysLoginLog entity = new SysLoginLog();
            BeanUtil.copyProperties(logInfo, entity);
            loginLogMapper.insert(entity);

            log.debug("登录日志保存成功: userId={}, type={}, status={}",
                    logInfo.getUserId(), logInfo.getLoginType(), logInfo.getLoginStatus());
        } catch (Exception e) {
            log.error("登录日志保存失败", e);
        }
    }
}
