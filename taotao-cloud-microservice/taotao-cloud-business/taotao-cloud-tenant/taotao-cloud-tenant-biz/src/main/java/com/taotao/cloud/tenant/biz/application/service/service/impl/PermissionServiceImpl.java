package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mdframe.forge.starter.core.session.LoginUser;
import com.mdframe.forge.starter.core.session.SessionHelper;
import com.mdframe.forge.starter.auth.service.IPermissionService;
import com.mdframe.forge.starter.auth.util.PathMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限服务实现类
 * 优化：API权限已在登录时加载并缓存到Redis中，直接从Lo ginUser读取
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements IPermissionService {

    @Override
    public List<String> getCurrentUserApiPermissions() {
        // 1. 获取当前登录用户
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser == null) {
            log.warn("获取API权限失败: 用户未登录");
            return new ArrayList<>();
        }

        // 2. 从缓存的LoginUser中直接读取API权限（已在登录时加载）
        List<String> apiPermissions = loginUser.getApiPermissions();
        
        if (CollUtil.isNotEmpty(apiPermissions)) {
            log.debug("从缓存获取API权限: userId={}, apiCount={}",
                    loginUser.getUserId(), apiPermissions.size());
            return apiPermissions;
        }

        // 3. 如果缓存中没有，返回空列表（正常情况下不应该进入这里）
        log.warn("缓存中没有API权限: userId={}", loginUser.getUserId());
        return new ArrayList<>();
    }

    @Override
    public boolean hasApiPermission(String apiUrl) {
        if (StrUtil.isBlank(apiUrl)) {
            return false;
        }

        // 1. 获取当前用户的API权限列表
        List<String> apiPermissions = getCurrentUserApiPermissions();
        if (CollUtil.isEmpty(apiPermissions)) {
            log.debug("权限校验失败: 用户没有API权限, apiUrl={}", apiUrl);
            return false;
        }

        // 2. 超级管理员直接通过
        if (apiPermissions.contains("/**") || apiPermissions.contains("*")) {
            log.debug("权限校验通过: 超级管理员, apiUrl={}", apiUrl);
            return true;
        }

        // 3. 使用通配符匹配
        boolean hasPermission = PathMatcher.matchAny(apiPermissions, apiUrl);
        
        if (hasPermission) {
            log.debug("权限校验通过: apiUrl={}", apiUrl);
        } else {
            log.warn("权限校验失败: apiUrl={}, userPermissions={}", apiUrl, apiPermissions);
        }

        return hasPermission;
    }
}
