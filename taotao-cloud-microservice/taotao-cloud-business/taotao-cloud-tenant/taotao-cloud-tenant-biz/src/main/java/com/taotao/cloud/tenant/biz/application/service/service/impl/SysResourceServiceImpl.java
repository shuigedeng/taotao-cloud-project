package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.application.dto.SysResourceDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysResourceQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysResource;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysRoleResource;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysResourceMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysRoleResourceMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysUserRoleMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysResourceService;
import com.mdframe.forge.plugin.system.vo.UserResourceTreeVO;
import com.mdframe.forge.starter.core.session.LoginUser;
import com.mdframe.forge.starter.core.session.SessionHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资源Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements ISysResourceService {

    private final SysResourceMapper resourceMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleResourceMapper roleResourceMapper;

    @Override
    public IPage<SysResource> selectResourcePage(SysResourceQuery query) {
        LambdaQueryWrapper<SysResource> wrapper = buildQueryWrapper(query);
        Page<SysResource> page = new Page<>(query.getPageNum(), query.getPageSize());
        return resourceMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SysResource> selectResourceTree(SysResourceQuery query) {
        // 如果不是管理员，只能查询自己拥有的资源树，防止权限溢出
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser != null && !loginUser.isAdmin()) {
            List<SysResource> userResources = getUserResources(loginUser);
            // 如果有查询条件，进行过滤
            if (StringUtils.isNotBlank(query.getResourceName())) {
                userResources = userResources.stream()
                        .filter(r -> r.getResourceName().contains(query.getResourceName()))
                        .collect(Collectors.toList());
            }
            return buildResourceTree(userResources, 0L);
        }

        LambdaQueryWrapper<SysResource> wrapper = buildQueryWrapper(query);
        List<SysResource> list = resourceMapper.selectList(wrapper);
        
        // 构建树形结构
        return buildResourceTree(list, 0L);
    }
    
    /**
     * 构建资源树形结构
     *
     * @param list     资源列表
     * @param parentId 父级ID
     * @return 树形结构列表
     */
    private List<SysResource> buildResourceTree(List<SysResource> list, Long parentId) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }

        // 按照parentId分组
        Map<Long, List<SysResource>> groupMap = list.stream()
                .collect(Collectors.groupingBy(resource -> resource.getParentId() == null ? 0L : resource.getParentId()));

        // 递归构建树形结构
        return buildResourceTreeRecursive(groupMap, parentId);
    }

    /**
     * 递归构建资源树形结构
     */
    private List<SysResource> buildResourceTreeRecursive(Map<Long, List<SysResource>> groupMap, Long parentId) {
        List<SysResource> children = groupMap.get(parentId);
        if (CollUtil.isEmpty(children)) {
            return new ArrayList<>();
        }

        // 为每个节点递归设置子节点
        children.forEach(node -> {
            List<SysResource> subChildren = buildResourceTreeRecursive(groupMap, node.getId());
            if (CollUtil.isNotEmpty(subChildren)) {
                node.setChildren(subChildren);
            }
        });

        return children;
    }

    @Override
    public SysResource selectResourceById(Long id) {
        return resourceMapper.selectById(id);
    }

    @Override
    public boolean insertResource(SysResourceDTO dto) {
        SysResource resource = new SysResource();
        BeanUtil.copyProperties(dto, resource);
        return resourceMapper.insert(resource) > 0;
    }

    @Override
    public boolean updateResource(SysResourceDTO dto) {
        SysResource resource = new SysResource();
        BeanUtil.copyProperties(dto, resource);
        return resourceMapper.updateById(resource) > 0;
    }

    @Override
    public boolean deleteResourceById(Long id) {
        return resourceMapper.deleteById(id) > 0;
    }

    @Override
    public List<UserResourceTreeVO> selectCurrentUserResourceTree() {
        // 1. 获取当前登录用户信息
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser == null) {
            throw new RuntimeException("用户未登录");
        }

        // 2. 查询用户的所有资源（包含菜单和按钮）
        List<SysResource> userResources = getUserResources(loginUser);

        // 3. 转换为VO并构建树形结构
        List<UserResourceTreeVO> voList = userResources.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        // 4. 构建树形结构
        return buildTree(voList, 0L);
    }

    @Override
    public List<UserResourceTreeVO> selectCurrentUserMenuTree() {
        // 1. 获取当前登录用户信息
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser == null) {
            throw new RuntimeException("用户未登录");
        }

        // 2. 查询用户的菜单资源（仅包含目录和菜单，resourceType为1和2）
        List<SysResource> userResources = getUserResources(loginUser);
        List<SysResource> menuResources = userResources.stream()
                .filter(resource -> resource.getResourceType() != null
                        && (resource.getResourceType() == 1 || resource.getResourceType() == 2))
                .toList();

        // 3. 转换为VO并构建树形结构
        List<UserResourceTreeVO> voList = menuResources.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        // 4. 构建树形结构
        return buildTree(voList, 0L);
    }

    @Override
    public List<String> selectCurrentUserPermissions() {
        // 1. 获取当前登录用户信息
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser == null) {
            throw new RuntimeException("用户未登录");
        }

        // 2. 超级管理员拥有所有权限
        if (loginUser.isAdmin()) {
            List<String> allPermissions = new ArrayList<>();
            allPermissions.add("*:*:*");
            return allPermissions;
        }

        // 3. 查询用户的按钮资源（resourceType为3）
        List<SysResource> userResources = getUserResources(loginUser);
        return userResources.stream()
                .filter(resource -> resource.getResourceType() != null && resource.getResourceType() == 3)
                .filter(resource -> StrUtil.isNotBlank(resource.getPerms()))
                .map(SysResource::getPerms)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> selectCurrentUserResourceIds() {
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser == null) {
            return new ArrayList<>();
        }
        
        // 超级管理员拥有所有资源ID
        if (loginUser.isAdmin()) {
            return resourceMapper.selectList(new LambdaQueryWrapper<SysResource>().select(SysResource::getId))
                    .stream().map(SysResource::getId).collect(Collectors.toList());
        }
        
        return getUserResources(loginUser).stream()
                .map(SysResource::getId)
                .collect(Collectors.toList());
    }

    /**
     * 获取用户的资源列表
     */
    private List<SysResource> getUserResources(LoginUser loginUser) {
        // 1. 超级管理员拥有所有资源
        if (loginUser.isAdmin()) {
            LambdaQueryWrapper<SysResource> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByAsc(SysResource::getSort)
                    .orderByDesc(SysResource::getCreateTime);
            return resourceMapper.selectList(wrapper);
        }

        // 2. 普通用户根据角色查询资源
        List<Long> roleIds = loginUser.getRoleIds();
        if (CollUtil.isEmpty(roleIds)) {
            return new ArrayList<>();
        }

        // 3. 查询角色关联的资源ID列表
        LambdaQueryWrapper<SysRoleResource> roleResourceWrapper = new LambdaQueryWrapper<>();
        roleResourceWrapper.in(SysRoleResource::getRoleId, roleIds);
        List<SysRoleResource> roleResources = roleResourceMapper.selectList(roleResourceWrapper);

        if (CollUtil.isEmpty(roleResources)) {
            return new ArrayList<>();
        }

        List<Long> resourceIds = roleResources.stream()
                .map(SysRoleResource::getResourceId)
                .distinct()
                .collect(Collectors.toList());

        // 4. 查询资源详情
        if (CollUtil.isEmpty(resourceIds)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysResource> resourceWrapper = new LambdaQueryWrapper<>();
        resourceWrapper.in(SysResource::getId, resourceIds)
                .orderByAsc(SysResource::getSort)
                .orderByDesc(SysResource::getCreateTime);
        return resourceMapper.selectList(resourceWrapper);
    }

    /**
     * 将资源实体转换为VO
     */
    private UserResourceTreeVO convertToVO(SysResource resource) {
        UserResourceTreeVO vo = new UserResourceTreeVO();
        vo.setId(resource.getId());
        vo.setParentId(resource.getParentId());
        vo.setResourceName(resource.getResourceName());
        vo.setResourceType(resource.getResourceType());
        vo.setSort(resource.getSort());
        vo.setPath(resource.getPath());
        vo.setComponent(resource.getComponent());
        vo.setIsExternal(resource.getIsExternal());
        vo.setMenuStatus(resource.getMenuStatus());
        vo.setVisible(resource.getVisible());
        vo.setPerms(resource.getPerms());
        vo.setIcon(resource.getIcon());
        vo.setKeepAlive(resource.getKeepAlive());
        vo.setAlwaysShow(resource.getAlwaysShow());
        vo.setRedirect(resource.getRedirect());
        vo.setApiMethod(resource.getApiMethod());
        vo.setApiUrl(resource.getApiUrl());
        return vo;
    }

    /**
     * 构建树形结构
     *
     * @param list     资源列表
     * @param parentId 父级ID
     * @return 树形结构列表
     */
    private List<UserResourceTreeVO> buildTree(List<UserResourceTreeVO> list, Long parentId) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }

        // 按照parentId分组
        Map<Long, List<UserResourceTreeVO>> groupMap = list.stream()
                .collect(Collectors.groupingBy(vo -> vo.getParentId() == null ? 0L : vo.getParentId()));

        // 递归构建树形结构
        return buildTreeRecursive(groupMap, parentId);
    }

    /**
     * 递归构建树形结构
     */
    private List<UserResourceTreeVO> buildTreeRecursive(Map<Long, List<UserResourceTreeVO>> groupMap, Long parentId) {
        List<UserResourceTreeVO> children = groupMap.get(parentId);
        if (CollUtil.isEmpty(children)) {
            return new ArrayList<>();
        }

        // 为每个节点递归设置子节点
        children.forEach(node -> {
            List<UserResourceTreeVO> subChildren = buildTreeRecursive(groupMap, node.getId());
            if (CollUtil.isNotEmpty(subChildren)) {
                node.setChildren(subChildren);
            }
        });

        return children;
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<SysResource> buildQueryWrapper(SysResourceQuery query) {
        LambdaQueryWrapper<SysResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getTenantId() != null, SysResource::getTenantId, query.getTenantId())
                .like(StringUtils.isNotBlank(query.getResourceName()), SysResource::getResourceName, query.getResourceName())
                .eq(query.getParentId() != null, SysResource::getParentId, query.getParentId())
                .eq(query.getResourceType() != null, SysResource::getResourceType, query.getResourceType())
                .eq(query.getVisible() != null, SysResource::getVisible, query.getVisible())
                .orderByAsc(SysResource::getSort)
                .orderByDesc(SysResource::getCreateTime);
        return wrapper;
    }
}
