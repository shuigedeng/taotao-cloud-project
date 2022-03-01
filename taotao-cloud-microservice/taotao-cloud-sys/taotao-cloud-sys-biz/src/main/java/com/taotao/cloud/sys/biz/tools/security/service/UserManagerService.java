package com.taotao.cloud.sys.biz.tools.security.service;

import com.sanri.tools.modules.core.exception.ToolException;
import com.sanri.tools.modules.core.security.UserService;
import com.sanri.tools.modules.core.security.dtos.RoleInfo;
import com.sanri.tools.modules.core.security.dtos.ThinUser;
import com.sanri.tools.modules.core.security.entitys.ToolUser;
import com.sanri.tools.modules.security.service.dtos.SecurityUser;
import com.sanri.tools.modules.security.service.repository.GroupRepository;
import com.sanri.tools.modules.security.service.repository.ResourceRepository;
import com.sanri.tools.modules.security.service.repository.RoleRepository;
import com.sanri.tools.modules.security.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserManagerService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RoleService roleService;

    /**
     * 添加一个用户
     * @param thinUser 用户数据
     */
    public void addUser(ThinUser thinUser){
        // 检查用户是否存在
        final ToolUser toolUser = thinUser.getToolUser();
        if (userRepository.existUser(toolUser.getUsername())){
            throw new ToolException("用户已经存在:"+toolUser.getUsername());
        }

        // 检查当前用户是否可以访问指定分组, 角色
        userService.checkGroupAccess(thinUser.getGroups().toArray(new String[]{}));
        userService.checkRoleAccess(thinUser.getRoles().toArray(new String[]{}));

        // 初始化密码为 0
        toolUser.setPassword("0");

        // 添加用户
        userRepository.addUser(thinUser);
    }

    /**
     * 删除一个用户
     * @param username 用户名
     */
    public void delUser(String username) throws IOException {
        userService.checkUserAccess(username);

        userRepository.deleteUser(username);
    }

    /**
     * 重置密码, 默认重置为 0
     * @param username 用户名
     */
    public void resetPassword(String username){
        userService.checkUserAccess(username);

        final SecurityUser user = userRepository.getUser(username);
        user.getToolUser().setPassword("0");
    }

    /**
     * 查询用户
     * @param username 用户名
     * @return
     */
    public ThinUser getUser(String username){
        userService.checkUserAccess(username);

        return userRepository.getUser(username);
    }

    /**
     * 用户授权角色
     * @param username 用户名
     * @param roles
     */
    public void grantRoles(String username,String...roles){
        userService.checkUserAccess(username);
        userService.checkRoleAccess(roles);

        userRepository.changeRoles(username,roles);
    }

    /**
     * 用户授权组织
     * @param username 用户名
     * @param groups 组织列表
     */
    public void grantGroups(String username,String...groups){
        userService.checkUserAccess(username);
        userService.checkGroupAccess(groups);

        final Set<String> collect = Arrays.stream(groups).collect(Collectors.toSet());
        final Set<Path> paths = groupService.filterTopGroups(collect);
        final List<String> filterGroups = paths.stream().map(GroupRepository::convertPathToString).collect(Collectors.toList());

        userRepository.changeGroups(username,filterGroups);
    }

    /**
     * 查询可访问组织列表
     * @param username 用户名
     * @return
     */
    public List<String> queryAccessGroups(String username){
        final SecurityUser user = userRepository.getUser(username);
        final List<String> groups = user.getGroups();

        Set<String> canGrantGroups = new HashSet<>();
        for (String group : groups) {
            final Set<String> childGroups = groupService.childGroups(group);
            canGrantGroups.addAll(childGroups);
        }
        // 用户可以把自己的分组授权出去  add 20220208
        canGrantGroups.addAll(groups);
        return new ArrayList<>(canGrantGroups);
    }

    /**
     * 查询可访问用户列表
     * @param username 用户名
     * @return
     */
    public List<String> queryAccessUsers(String username){
        final SecurityUser user = userRepository.getUser(username);
        final List<String> groups = user.getGroups();
        Set<String> usernames = new HashSet<>();
        for (String group : groups) {
            final Set<String> canGrantUsers = groupService.findGroupUsers(Paths.get(group),true);
            usernames.addAll(canGrantUsers);
        }
        return new ArrayList<>(usernames);
    }

    /**
     * 查询可授权资源列表
     * @param username 用户名
     *
     * 逻辑如下:
     * 当前用户拥有的角色
     * 角色拥有的资源列表
     * 查到资源的分组
     * 找到顶层分组列表
     * 查询可授权资源列表
     * 查询可授权资源的所有子资源
     */
    public List<String> queryAccessResources(String username){
        final SecurityUser user = userRepository.getUser(username);
        final List<String> roles = user.getRoles();

        Set<String> canGrantResources = new HashSet<>();

        for (String role : roles) {
            final Set<String> roleCanViewResources = roleService.queryAccessResources(role);
            canGrantResources.addAll(roleCanViewResources);
        }

        return new ArrayList<>(canGrantResources);
    }

    /**
     * 查询用户可访问角色列表
     * @param username
     * @return
     */
    public List<String> queryAccessRoles(String username){
        final SecurityUser user = userRepository.getUser(username);
        final List<String> roles = user.getRoles();

        Set<String> allRoles = new HashSet<>(roles);

        // 角色所在组织及其以下的组织中的角色
        Set<String> groupsInRoles = new HashSet<>();
        for (String role : roles) {
            final RoleInfo roleInfo = roleRepository.getRole(role);
            if (roleInfo == null){
                log.info("角色信息不存在[{}]",role);
                continue;
            }
            final List<String> groups = roleInfo.getGroups();
            groupsInRoles.addAll(groups);
        }

        final Set<Path> topGroups = groupService.filterTopGroups(groupsInRoles);
        for (Path topGroup : topGroups) {
            final Set<String> canGrantRoles = groupService.findGroupRoles(topGroup,true);
            allRoles.addAll(canGrantRoles);
        }

        return new ArrayList<>(allRoles);
    }
}
