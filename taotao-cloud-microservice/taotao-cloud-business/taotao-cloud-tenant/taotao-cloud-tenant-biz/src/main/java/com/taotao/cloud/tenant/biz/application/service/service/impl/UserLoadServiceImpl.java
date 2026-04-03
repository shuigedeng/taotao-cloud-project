package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.*;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.*;
import com.taotao.cloud.tenant.biz.application.service.service.IUserLoadService;
import com.mdframe.forge.starter.core.session.LoginUser;
import com.mdframe.forge.starter.auth.service.ICaptchaService;
import com.mdframe.forge.starter.auth.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户加载服务实现
 * 专门用于认证策略调用，避免循环依赖
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoadServiceImpl implements IUserLoadService {

    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleMapper roleMapper;
    private final SysUserOrgMapper userOrgMapper;
    private final SysRoleResourceMapper roleResourceMapper;
    private final SysResourceMapper resourceMapper;
    private final ICaptchaService captchaService;
    
    private final SysOrgMapper sysOrgMapper;
    
    @Override
    public LoginUser loadUserByUsername(String username, Long tenantId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        if (tenantId != null) {
            wrapper.eq(SysUser::getTenantId, tenantId);
        }
        SysUser user = userMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return buildLoginUser(user);
    }

    @Override
    public LoginUser loadUserByPhone(String phone, Long tenantId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhone, phone);
        if (tenantId != null) {
            wrapper.eq(SysUser::getTenantId, tenantId);
        }
        SysUser user = userMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return buildLoginUser(user);
    }

    @Override
    public LoginUser loadUserByEmail(String email, Long tenantId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, email);
        if (tenantId != null) {
            wrapper.eq(SysUser::getTenantId, tenantId);
        }
        SysUser user = userMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return buildLoginUser(user);
    }

    @Override
    public String getUserPassword(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user.getPassword();
    }

    @Override
    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return PasswordUtil.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean validateCode(String codeKey, String code) {
        return captchaService.validateAndDelete(codeKey, code);
    }

    @Override
    public boolean validatePhoneCode(String phone, String code) {
        String cacheKey = "phone_code:" + phone;
        return captchaService.validateAndDelete(cacheKey, code);
    }

    /**
     * 构建LoginUser（包含角色、权限、组织）
     */
    private LoginUser buildLoginUser(SysUser user) {
        // 1. 构建基本信息
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setTenantId(user.getTenantId());
        loginUser.setUsername(user.getUsername());
        loginUser.setRealName(user.getRealName());
        loginUser.setUserType(user.getUserType());
        loginUser.setPhone(user.getPhone());
        loginUser.setEmail(user.getEmail());
        loginUser.setAvatar(user.getAvatar());
        loginUser.setUserStatus(user.getUserStatus());

        // 2. 加载用户角色
        loadUserRoles(loginUser);

        // 3. 加载用户组织
        loadUserOrgs(loginUser);

        // 4. 加载用户权限（按钮权限）
        loadUserPermissions(loginUser);

        // 5. 加载API接口权限（缓存到Session）
        loadApiPermissions(loginUser);

        return loginUser;
    }

    /**
     * 加载用户角色
     */
    private void loadUserRoles(LoginUser loginUser) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, loginUser.getUserId());
        List<SysUserRole> userRoles = userRoleMapper.selectList(wrapper);

        if (CollUtil.isNotEmpty(userRoles)) {
            List<Long> roleIds = userRoles.stream()
                    .map(SysUserRole::getRoleId)
                    .collect(Collectors.toList());
            loginUser.setRoleIds(roleIds);

            if (CollUtil.isNotEmpty(roleIds)) {
                LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<>();
                roleWrapper.in(SysRole::getId, roleIds)
                        .eq(SysRole::getRoleStatus, 1);
                List<SysRole> roles = roleMapper.selectList(roleWrapper);

                if (CollUtil.isNotEmpty(roles)) {
                    Set<String> roleKeys = roles.stream()
                            .map(SysRole::getRoleKey)
                            .filter(StrUtil::isNotBlank)
                            .collect(Collectors.toSet());
                    loginUser.setRoleKeys(roleKeys);
                    
                    log.debug("加载用户角色: userId={}, roleIds={}, roleKeys={}",
                            loginUser.getUserId(), roleIds, roleKeys);
                } else {
                    log.warn("用户没有启用的角色: userId={}", loginUser.getUserId());
                }
            }
        } else {
            log.warn("用户没有分配角色: userId={}", loginUser.getUserId());
        }
    }

    /**
     * 加载用户组织
     */
    private void loadUserOrgs(LoginUser loginUser) {
        LambdaQueryWrapper<SysUserOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserOrg::getUserId, loginUser.getUserId());
        List<SysUserOrg> userOrgs = userOrgMapper.selectList(wrapper);

        if (CollUtil.isNotEmpty(userOrgs)) {
            List<Long> orgIds = userOrgs.stream()
                    .map(SysUserOrg::getOrgId)
                    .collect(Collectors.toList());
            loginUser.setOrgIds(orgIds);

            userOrgs.stream()
                    .filter(uo -> uo.getIsMain() != null && uo.getIsMain() == 1)
                    .findFirst()
                    .ifPresent(uo -> {
                        loginUser.setMainOrgId(uo.getOrgId());
                        SysOrg sysOrg = sysOrgMapper.selectById(uo.getOrgId());
                        loginUser.setDeptName(sysOrg.getOrgName());
                    });
        }
    }

    /**
     * 加载用户权限（按钮权限）
     */
    private void loadUserPermissions(LoginUser loginUser) {
        if (loginUser.isAdmin()) {
            Set<String> permissions = new HashSet<>();
            permissions.add("*:*:*");
            loginUser.setPermissions(permissions);
            log.debug("超级管理员拥有所有权限: userId={}", loginUser.getUserId());
            return;
        }
        
        List<Long> roleIds = loginUser.getRoleIds();
        if (CollUtil.isEmpty(roleIds)) {
            log.warn("用户没有角色，无法加载权限: userId={}", loginUser.getUserId());
            return;
        }

        LambdaQueryWrapper<SysRoleResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleResource::getRoleId, roleIds);
        List<SysRoleResource> roleResources = roleResourceMapper.selectList(wrapper);

        if (CollUtil.isNotEmpty(roleResources)) {
            List<Long> resourceIds = roleResources.stream()
                    .map(SysRoleResource::getResourceId)
                    .distinct()
                    .collect(Collectors.toList());

            if (CollUtil.isNotEmpty(resourceIds)) {
                LambdaQueryWrapper<SysResource> resourceWrapper = new LambdaQueryWrapper<>();
                resourceWrapper.in(SysResource::getId, resourceIds)
                        .eq(SysResource::getVisible, 1)
                        .isNotNull(SysResource::getPerms);
                List<SysResource> resources = resourceMapper.selectList(resourceWrapper);

                if (CollUtil.isNotEmpty(resources)) {
                    Set<String> permissions = resources.stream()
                            .map(SysResource::getPerms)
                            .filter(StrUtil::isNotBlank)
                            .collect(Collectors.toSet());
                    loginUser.setPermissions(permissions);
                    
                    log.debug("加载用户权限: userId={}, permissionCount={}, permissions={}",
                            loginUser.getUserId(), permissions.size(), permissions);
                } else {
                    log.warn("用户角色没有分配有效权限: userId={}", loginUser.getUserId());
                }
            }
        } else {
            log.warn("用户角色没有分配资源: userId={}", loginUser.getUserId());
        }
    }

    /**
     * 加载API接口权限（缓存到LoginUser中）
     */
    private void loadApiPermissions(LoginUser loginUser) {
        // 1. 超级管理员拥有所有API权限
        if (loginUser.isAdmin()) {
            List<String> apiPermissions = new ArrayList<>();
            apiPermissions.add("/**");  // 匹配所有接口
            loginUser.setApiPermissions(apiPermissions);
            log.debug("超级管理员拥有所有API权限: userId={}", loginUser.getUserId());
            return;
        }

        // 2. 获取用户的角色ID列表
        List<Long> roleIds = loginUser.getRoleIds();
        if (CollUtil.isEmpty(roleIds)) {
            log.warn("获取API权限失败: 用户没有角色, userId={}", loginUser.getUserId());
            loginUser.setApiPermissions(new ArrayList<>());
            return;
        }

        // 3. 查询角色关联的资源ID列表
        LambdaQueryWrapper<SysRoleResource> roleResourceWrapper = new LambdaQueryWrapper<>();
        roleResourceWrapper.in(SysRoleResource::getRoleId, roleIds);
        List<SysRoleResource> roleResources = roleResourceMapper.selectList(roleResourceWrapper);

        if (CollUtil.isEmpty(roleResources)) {
            log.warn("获取API权限失败: 角色没有分配资源, userId={}, roleIds={}",
                    loginUser.getUserId(), roleIds);
            loginUser.setApiPermissions(new ArrayList<>());
            return;
        }

        List<Long> resourceIds = roleResources.stream()
                .map(SysRoleResource::getResourceId)
                .distinct()
                .collect(Collectors.toList());

        // 4. 查询API类型的资源（resourceType=4）
        LambdaQueryWrapper<SysResource> resourceWrapper = new LambdaQueryWrapper<>();
        resourceWrapper.in(SysResource::getId, resourceIds)
                .eq(SysResource::getResourceType, 4)  // API接口类型
                .eq(SysResource::getVisible, 1)  // 可见的资源
                .isNotNull(SysResource::getApiUrl);  // 必须配置了apiUrl

        List<SysResource> apiResources = resourceMapper.selectList(resourceWrapper);

        if (CollUtil.isEmpty(apiResources)) {
            log.debug("用户没有API权限: userId={}", loginUser.getUserId());
            loginUser.setApiPermissions(new ArrayList<>());
            return;
        }

        // 5. 提取apiUrl列表（支持通配符）
        List<String> apiUrls = apiResources.stream()
                .map(SysResource::getApiUrl)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        loginUser.setApiPermissions(apiUrls);

        log.debug("加载用户API权限: userId={}, apiCount={}, apis={}",
                loginUser.getUserId(), apiUrls.size(), apiUrls);
    }
}
