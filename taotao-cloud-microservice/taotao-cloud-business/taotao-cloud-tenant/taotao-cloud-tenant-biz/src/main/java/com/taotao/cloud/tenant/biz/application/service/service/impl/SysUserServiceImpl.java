package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import
import com.taotao.boot.web.annotation.LoginUser;
import com.taotao.cloud.tenant.biz.application.dto.SysUserDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysUserQuery;
import com.taotao.cloud.tenant.biz.application.service.service.ISysUserService;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysUser;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysUserOrg;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysUserRole;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysUserMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysUserOrgMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysUserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysUserOrgMapper userOrgMapper;

    @Override
    public IPage<SysUser> selectUserPage( SysUserQuery query) {
        return this.lambdaQuery().eq(query.getTenantId() != null, SysUser::getTenantId, query.getTenantId())
                .like(StringUtils.isNotBlank(query.getUsername()), SysUser::getUsername, query.getUsername())
                .like(StringUtils.isNotBlank(query.getRealName()), SysUser::getRealName, query.getRealName())
                .eq(query.getUserType() != null, SysUser::getUserType, query.getUserType())
                .eq(StringUtils.isNotBlank(query.getPhone()), SysUser::getPhone, query.getPhone())
                .eq(query.getUserStatus() != null, SysUser::getUserStatus, query.getUserStatus())
                .eq(query.getCreateDept() != null, SysUser::getCreateDept, query.getCreateDept())
                .orderByDesc(SysUser::getCreateTime).page(new Page<>(query.getPageNum(), query.getPageSize()));
    }

    @Override
    public SysUser selectUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean insertUser( SysUserDTO dto) {
        SysUser user = new SysUser();
        BeanUtil.copyProperties(dto, user);
        user.setPassword(PasswordUtil.encrypt(dto.getPassword()));
        return userMapper.insert(user) > 0;
    }

    @Override
    public boolean updateUser(SysUserDTO dto) {
        SysUser user = new SysUser();
        BeanUtil.copyProperties(dto, user);
        // 修改时不更新密码
        user.setPassword(null);
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean deleteUserById(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteUserByIds(Long[] ids) {
        return userMapper.deleteBatchIds(Arrays.asList(ids)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindUserRoles(Long userId, Long[] roleIds) {
        if (userId == null || roleIds == null || roleIds.length == 0) {
            return false;
        }
        
        // 防止权限溢出校验：非管理员只能分配自己拥有的角色
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser != null && !loginUser.isAdmin()) {
            List<Long> currentUserRoleIds = loginUser.getRoleIds();
            for (Long roleId : roleIds) {
                if (!currentUserRoleIds.contains(roleId)) {
                    throw new RuntimeException("权限溢出：不能分配自己没有的角色");
                }
            }
        }
        
        // 获取用户信息以获取租户ID
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        
        // 批量插入用户角色关联
        List<SysUserRole> userRoles = new ArrayList<>();
        for (Long roleId : roleIds) {
            // 检查是否已存在
            LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUserRole::getUserId, userId)
                    .eq(SysUserRole::getRoleId, roleId);
            Long count = userRoleMapper.selectCount(wrapper);
            
            if (count == 0) {
                SysUserRole userRole = new SysUserRole();
                userRole.setTenantId(user.getTenantId());
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoles.add(userRole);
            }
        }
        
        if (!userRoles.isEmpty()) {
            userRoles.forEach(userRoleMapper::insert);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbindUserRoles(Long userId, Long[] roleIds) {
        if (userId == null || roleIds == null || roleIds.length == 0) {
            return false;
        }
        
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId)
                .in(SysUserRole::getRoleId, Arrays.asList(roleIds));
        return userRoleMapper.delete(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindUserOrg(Long userId, Long orgId, Integer isMain) {
        if (userId == null || orgId == null) {
            return false;
        }
        
        // 获取用户信息以获取租户ID
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        
        // 检查是否已存在
        LambdaQueryWrapper<SysUserOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserOrg::getUserId, userId)
                .eq(SysUserOrg::getOrgId, orgId);
        Long count = userOrgMapper.selectCount(wrapper);
        
        if (count > 0) {
            return false;
        }
        
        // 如果是主组织，先取消其他主组织
        if (isMain != null && isMain == 1) {
            LambdaQueryWrapper<SysUserOrg> updateWrapper = new LambdaQueryWrapper<>();
            updateWrapper.eq(SysUserOrg::getUserId, userId)
                    .eq(SysUserOrg::getIsMain, 1);
            SysUserOrg updateOrg = new SysUserOrg();
            updateOrg.setIsMain(0);
            userOrgMapper.update(updateOrg, updateWrapper);
        }
        
        SysUserOrg userOrg = new SysUserOrg();
        userOrg.setTenantId(user.getTenantId());
        userOrg.setUserId(userId);
        userOrg.setOrgId(orgId);
        userOrg.setIsMain(isMain != null ? isMain : 0);
        return userOrgMapper.insert(userOrg) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbindUserOrg(Long userId, Long orgId) {
        if (userId == null || orgId == null) {
            return false;
        }
        
        LambdaQueryWrapper<SysUserOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserOrg::getUserId, userId)
                .eq(SysUserOrg::getOrgId, orgId);
        return userOrgMapper.delete(wrapper) > 0;
    }

    @Override
    public List<Long> selectUserRoleIds(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId)
                .select(SysUserRole::getRoleId);
        return userRoleMapper.selectList(wrapper)
                .stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> selectUserOrgIds(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<SysUserOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserOrg::getUserId, userId)
                .select(SysUserOrg::getOrgId);
        return userOrgMapper.selectList(wrapper)
                .stream()
                .map(SysUserOrg::getOrgId)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindUserOrgs(Long userId, List<Long> orgIds, Long mainOrgId) {
        if (userId == null || orgIds == null || orgIds.isEmpty()) {
            return false;
        }
        
        // 获取用户信息以获取租户ID
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        
        // 验证主组织是否在组织列表中
        if (mainOrgId != null && !orgIds.contains(mainOrgId)) {
            mainOrgId = null;
        }
        
        // 获取用户当前的所有组织
        LambdaQueryWrapper<SysUserOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserOrg::getUserId, userId);
        List<SysUserOrg> existingOrgs = userOrgMapper.selectList(wrapper);
        List<Long> existingOrgIds = existingOrgs.stream()
                .map(SysUserOrg::getOrgId)
                .collect(Collectors.toList());
        
        // 删除不再需要的组织
        List<Long> toDelete = existingOrgIds.stream()
                .filter(orgId -> !orgIds.contains(orgId))
                .collect(Collectors.toList());
        if (!toDelete.isEmpty()) {
            LambdaQueryWrapper<SysUserOrg> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(SysUserOrg::getUserId, userId)
                    .in(SysUserOrg::getOrgId, toDelete);
            userOrgMapper.delete(deleteWrapper);
        }
        
        // 添加新的组织或更新现有组织
        for (Long orgId : orgIds) {
            SysUserOrg userOrg = existingOrgs.stream()
                    .filter(org -> org.getOrgId().equals(orgId))
                    .findFirst()
                    .orElse(null);
            
            if (userOrg == null) {
                // 插入新组织
                userOrg = new SysUserOrg();
                userOrg.setTenantId(user.getTenantId());
                userOrg.setUserId(userId);
                userOrg.setOrgId(orgId);
                userOrg.setIsMain(orgId.equals(mainOrgId) ? 1 : 0);
                userOrgMapper.insert(userOrg);
            } else {
                // 更新现有组织的主组织标记
                int newIsMain = orgId.equals(mainOrgId) ? 1 : 0;
                if (!userOrg.getIsMain().equals(newIsMain)) {
                    userOrg.setIsMain(newIsMain);
                    userOrgMapper.updateById(userOrg);
                }
            }
        }
        
        return true;
    }
    
    @Override
    public void doUntieDisable(Long userId) {
        StpUtil.untieDisable(userId);
        // 同时更新数据库状态为正常
        this.updateUserStatus(userId, 1);
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(PasswordUtil.encrypt(newPassword));
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setUserStatus(status);
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean updateUserProfile(SysUserDTO dto) {
        Long currentUserId = SessionHelper.getUserId();
        if (currentUserId == null) {
            throw new RuntimeException("用户未登录");
        }

        SysUser user = new SysUser();
        user.setId(currentUserId);
        user.setUsername(dto.getUsername());
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());

        return userMapper.updateById(user) > 0;
    }
}
