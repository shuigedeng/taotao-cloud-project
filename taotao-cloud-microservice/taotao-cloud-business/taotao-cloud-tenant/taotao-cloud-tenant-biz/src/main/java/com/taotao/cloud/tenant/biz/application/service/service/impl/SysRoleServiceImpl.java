package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.application.dto.SysRoleDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysRoleQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysResource;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysRole;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysRoleResource;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysRoleMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysRoleResourceMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysResourceService;
import com.taotao.cloud.tenant.biz.application.service.service.ISysRoleService;
import com.mdframe.forge.starter.core.session.LoginUser;
import com.mdframe.forge.starter.core.session.SessionHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysRoleMapper roleMapper;
    private final SysRoleResourceMapper roleResourceMapper;
    @Lazy
    private final ISysResourceService resourceService;

    @Override
    public IPage<SysRole> selectRolePage(SysRoleQuery query) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getTenantId() != null, SysRole::getTenantId, query.getTenantId())
                .like(StringUtils.isNotBlank(query.getRoleName()), SysRole::getRoleName, query.getRoleName())
                .eq(StringUtils.isNotBlank(query.getRoleKey()), SysRole::getRoleKey, query.getRoleKey())
                .eq(query.getRoleStatus() != null, SysRole::getRoleStatus, query.getRoleStatus())
                .orderByAsc(SysRole::getSort)
                .orderByDesc(SysRole::getCreateTime);

        // 如果不是管理员，只能查询自己拥有的角色，防止权限溢出
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser != null && !loginUser.isAdmin()) {
            List<Long> userRoleIds = loginUser.getRoleIds();
            if (CollUtil.isEmpty(userRoleIds)) {
                return new Page<>();
            }
            wrapper.in(SysRole::getId, userRoleIds);
        }

        Page<SysRole> page = new Page<>(query.getPageNum(), query.getPageSize());
        return roleMapper.selectPage(page, wrapper);
    }

    @Override
    public SysRole selectRoleById(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public boolean insertRole(SysRoleDTO dto) {
        SysRole role = new SysRole();
        BeanUtil.copyProperties(dto, role);
        return roleMapper.insert(role) > 0;
    }

    @Override
    public boolean updateRole(SysRoleDTO dto) {
        SysRole role = new SysRole();
        BeanUtil.copyProperties(dto, role);
        return roleMapper.updateById(role) > 0;
    }

    @Override
    public boolean deleteRoleById(Long id) {
        return roleMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteRoleByIds(Long[] ids) {
        return roleMapper.deleteBatchIds(Arrays.asList(ids)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindRoleResources(Long roleId, Long[] resourceIds) {
        if (roleId == null) {
            return false;
        }
        
        // 获取角色信息以获取租户ID
        SysRole role = roleMapper.selectById(roleId);
        if (role == null) {
            return false;
        }

        // 防止权限溢出校验：非管理员只能分配自己拥有的资源
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser != null && !loginUser.isAdmin()) {
            List<Long> currentUserResourceIds = resourceService.selectCurrentUserResourceIds();
            if (resourceIds != null) {
                for (Long resourceId : resourceIds) {
                    if (!currentUserResourceIds.contains(resourceId)) {
                        throw new RuntimeException("权限溢出：不能分配自己没有的资源权限");
                    }
                }
            }
        }

        // 核心修复：自动补充所有父级ID，确保菜单树渲染完整
        Set<Long> finalResourceIdSet = new HashSet<>();
        if (resourceIds != null && resourceIds.length > 0) {
            finalResourceIdSet.addAll(Arrays.asList(resourceIds));
            
            // 获取所有资源并构建父子关系映射
            List<SysResource> allResources = resourceService.list();
            Map<Long, Long> parentMap = allResources.stream()
                .collect(Collectors.toMap(SysResource::getId, r -> r.getParentId() == null ? 0L : r.getParentId()));
            
            Set<Long> parentIdsToAdd = new HashSet<>();
            for (Long id : finalResourceIdSet) {
                Long pid = parentMap.get(id);
                while (pid != null && pid != 0L) {
                    if (finalResourceIdSet.contains(pid) || parentIdsToAdd.contains(pid)) {
                        break;
                    }
                    parentIdsToAdd.add(pid);
                    pid = parentMap.get(pid);
                }
            }
            finalResourceIdSet.addAll(parentIdsToAdd);
        }
        
        // 1. 先删除该角色的所有资源关联
        LambdaQueryWrapper<SysRoleResource> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(SysRoleResource::getRoleId, roleId);
        roleResourceMapper.delete(deleteWrapper);
        
        // 2. 如果没有新的资源ID，直接返回（表示清空所有权限）
        if (finalResourceIdSet.isEmpty()) {
            return true;
        }
        
        // 3. 批量插入新的角色资源关联
        List<SysRoleResource> roleResources = new ArrayList<>();
        for (Long resourceId : finalResourceIdSet) {
            SysRoleResource roleResource = new SysRoleResource();
            roleResource.setTenantId(role.getTenantId());
            roleResource.setRoleId(roleId);
            roleResource.setResourceId(resourceId);
            roleResources.add(roleResource);
        }
        
        if (!roleResources.isEmpty()) {
            roleResources.forEach(roleResourceMapper::insert);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbindRoleResources(Long roleId, Long[] resourceIds) {
        if (roleId == null || resourceIds == null || resourceIds.length == 0) {
            return false;
        }
        
        LambdaQueryWrapper<SysRoleResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleResource::getRoleId, roleId)
                .in(SysRoleResource::getResourceId, Arrays.asList(resourceIds));
        return roleResourceMapper.delete(wrapper) > 0;
    }

    @Override
    public List<Long> selectRoleResourceIds(Long roleId) {
        if (roleId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<SysRoleResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleResource::getRoleId, roleId)
                .select(SysRoleResource::getResourceId);
        List<Long> resourceIds = roleResourceMapper.selectList(wrapper)
                .stream()
                .map(SysRoleResource::getResourceId)
                .collect(Collectors.toList());

        // 优化：过滤掉父级ID，只返回叶子节点（在当前选中集合中没有子节点的节点）
        // 这样可以适配前端树组件的 cascade 模式，防止因父节点存在导致子节点全选
        if (CollUtil.isNotEmpty(resourceIds)) {
            List<SysResource> selectedResources = resourceService.listByIds(resourceIds);
            Set<Long> selectedParentIds = selectedResources.stream()
                .map(SysResource::getParentId)
                .filter(pid -> pid != null && pid != 0L)
                .collect(Collectors.toSet());
            
            return resourceIds.stream()
                .filter(id -> !selectedParentIds.contains(id))
                .collect(Collectors.toList());
        }
        
        return resourceIds;
    }

    @Override
    public List<Long> selectCurrentUserRoleIds() {
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser == null) {
            return new ArrayList<>();
        }
        if (loginUser.isAdmin()) {
            return roleMapper.selectList(new LambdaQueryWrapper<SysRole>().select(SysRole::getId))
                    .stream().map(SysRole::getId).collect(Collectors.toList());
        }
        return loginUser.getRoleIds();
    }
}
