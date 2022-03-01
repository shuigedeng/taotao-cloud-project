package com.taotao.cloud.sys.biz.tools.security.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.sanri.tools.modules.core.security.dtos.GroupTree;
import com.sanri.tools.modules.core.security.dtos.RoleInfo;
import com.sanri.tools.modules.security.service.GroupService;
import com.sanri.tools.modules.security.service.ResourceService;
import com.sanri.tools.modules.security.service.dtos.ResourceTree;
import com.sanri.tools.modules.security.service.repository.RoleRepository;
import com.sanri.tools.modules.security.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sanri.tools.modules.core.security.dtos.ThinUser;
import com.sanri.tools.modules.security.service.ProfileService;
import com.sanri.tools.modules.security.service.UserManagerService;

/**
 * 当前用户管理另一个用户的权限信息管理
 */
@RestController
@RequestMapping("/security/user")
@Validated
public class UserController {

    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ResourceService resourceService;

    /**
     * 获取用户信息
     * @param username 用户名
     */
    @GetMapping("/{username}")
    public ThinUser userInfo(@Validated @PathVariable("username") String username){
        return userManagerService.getUser(username);
    }

    /**
     * 重置密码
     * @param username 用户名
     */
    @PostMapping("/{username}/resetPassword")
    public void resetPassword(@Validated @PathVariable("username") String username){
        userManagerService.resetPassword(username);
    }

    /**
     * 添加用户
     * @param user
     */
    @PostMapping("/add")
    public void addUser(@RequestBody @Validated ThinUser user){
        userManagerService.addUser(user);
    }

    /**
     * 删除用户
     * @param username
     */
    @PostMapping("/del")
    public void delUser(@NotBlank String username) throws IOException {
        userManagerService.delUser(username);
    }

    /**
     * 查询用户的可授权组织列表
     * @param username 用户名
     */
    @GetMapping("/{username}/accessGroups")
    public GroupTree queryAccessGroups(@NotBlank @PathVariable("username") String username){
        profileService.checkUserAccess(username);
        final List<String> accessGroups = userManagerService.queryAccessGroups(username);
        final List<Path> collect = accessGroups.stream().map(Paths::get).collect(Collectors.toList());
        return GroupService.convertPathsToGroupTree(collect);
    }

    /**
     * 查询用户的可授权用户
     * @param username 用户名
     * @return
     */
    @GetMapping("/{username}/accessUsers")
    public List<? extends ThinUser> queryAccessUsers(@NotBlank @PathVariable("username") String username){
        profileService.checkUserAccess(username);
        final List<String> accessUsers = userManagerService.queryAccessUsers(username);
        return userRepository.getUsers(accessUsers);
    }

    /**
     * 查询用户的可授权角色
     * @param username 用户名
     * @return
     */
    @GetMapping("/{username}/accessRoles")
    public List<RoleInfo> queryAccessRoles(@NotBlank @PathVariable("username") String username){
        profileService.checkUserAccess(username);
        final List<String> accessRoles = userManagerService.queryAccessRoles(username);
        return roleRepository.getRoles(accessRoles);
    }

    /**
     * 查询用户的可授权资源
     * @param username 用户名
     * @return
     */
    @GetMapping("/{username}/accessResources")
    public List<ResourceTree> queryAccessResources(@NotBlank @PathVariable("username") String username){
        profileService.checkUserAccess(username);
        final List<String> accessResources = userManagerService.queryAccessResources(username);
        return resourceService.completionToTree(accessResources);
    }

    /**
     * 用户授权角色信息
     * @param username 用户名
     * @param roles 角色名列表
     */
    @PostMapping("/{username}/grantRoles")
    public void grantUserRoles(@Validated @PathVariable("username") String username,String[] roles){
        userManagerService.grantRoles(username,roles);
    }

    /**
     * 用户授权分组
     * @param username 用户名
     * @param groups 分组信息
     */
    @PostMapping("/{username}/grantGroups")
    public void grantUserGroups(@Validated @PathVariable("username") String username,String[] groups){
        userManagerService.grantGroups(username,groups);
    }
}
