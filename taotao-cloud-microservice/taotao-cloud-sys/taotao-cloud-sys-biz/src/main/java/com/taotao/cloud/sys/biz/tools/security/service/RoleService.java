package com.taotao.cloud.sys.biz.tools.security.service;

import com.sanri.tools.modules.core.exception.ToolException;
import com.sanri.tools.modules.core.security.UserService;
import com.sanri.tools.modules.core.security.dtos.ResourceInfo;
import com.sanri.tools.modules.core.security.dtos.RoleInfo;
import com.sanri.tools.modules.core.security.entitys.ToolRole;
import com.sanri.tools.modules.security.service.repository.GroupRepository;
import com.sanri.tools.modules.security.service.repository.ResourceRepository;
import com.sanri.tools.modules.security.service.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private ResourceService resourceService;

    /**
     * 添加角色
     * @param roleInfo
     */
    public void addRole(RoleInfo roleInfo){
        final ToolRole toolRole = roleInfo.getToolRole();
        // 检查角色是否已经存在
        if (roleRepository.existRole(toolRole.getRolename())){
            throw new ToolException("角色名已经存在:"+toolRole.getRolename());
        }
        userService.checkGroupAccess(roleInfo.getGroups().toArray(new String[]{}));
        userService.checkResourceAccess(roleInfo.getResources().toArray(new String[]{}));

        roleRepository.addRole(roleInfo);
    }

    /**
     * 获取角色信息
     * @param rolename 角色名称
     * @return
     */
    public RoleInfo getRole(String rolename){
        userService.checkRoleAccess(rolename);

        return roleRepository.getRole(rolename);
    }

    /**
     * 删除一个角色
     * @param rolename 角色名称
     */
    public void delRole(String rolename) {
        userService.checkRoleAccess(rolename);

        roleRepository.deleteRole(rolename);
    }

    /**
     * 角色可见资源列表
     * @param rolename
     * @return
     */
    public Set<String> queryAccessResources(String rolename){
        userService.checkRoleAccess(rolename);

        final RoleInfo role = roleRepository.getRole(rolename);
        final List<String> resources = role.getResources();

        // 找到资源的顶层组织列表
        final List<ResourceInfo> resourceInfos = resourceRepository.getResources(resources);
        Set<String> groups = new HashSet<>();
        for (ResourceInfo resourceInfo : resourceInfos) {
            groups.addAll(resourceInfo.getGroups());
        }
        final Set<Path> topGroups = groupService.filterTopGroups(groups);

        // 查询可授权资源列表
        Set<String> canGrantResources = new HashSet<>();
        for (Path topGroup : topGroups) {
            final Set<String> canGrantResourcesPart = groupService.findGroupResources(topGroup,true);
            canGrantResources.addAll(canGrantResourcesPart);
        }

        // 查询所有子级资源(查询菜单那里并不需要查子级资源, 这里应该优化)
        List<String> childResources = resourceService.loadChildResources(canGrantResources);
        canGrantResources.addAll(childResources);

        return canGrantResources;
    }

    /**
     * 角色授权组织
     * @param rolename 角色名称
     * @param groups    组织列表
     */
    public void grantGroups(String rolename,String... groups){
        userService.checkRoleAccess(rolename);
        userService.checkGroupAccess(groups);

        final Set<String> collect = Arrays.stream(groups).collect(Collectors.toSet());
        final Set<Path> paths = groupService.filterTopGroups(collect);
        final List<String> filterGroups = paths.stream().map(GroupRepository::convertPathToString).collect(Collectors.toList());

        roleRepository.changeGroups(rolename,filterGroups);
    }

    /**
     * 角色授权资源
     * @param rolename   角色名称
     * @param resources  资源列表
     */
    public void grantResources(String rolename,String...resources){
        userService.checkRoleAccess(rolename);
        userService.checkResourceAccess(resources);

        // 过滤出顶层资源
        final List<ResourceInfo> resourceInfos = Arrays.stream(resources).map(resourceRepository::getResource).collect(Collectors.toList());
        final List<ResourceInfo> topResources = resourceService.filterTopResources(resourceInfos);
        final List<String> collect = topResources.stream().map(resourceInfo -> resourceInfo.getToolResource().getResourceId()).collect(Collectors.toList());

        roleRepository.changeResources(rolename,collect);
    }
}
