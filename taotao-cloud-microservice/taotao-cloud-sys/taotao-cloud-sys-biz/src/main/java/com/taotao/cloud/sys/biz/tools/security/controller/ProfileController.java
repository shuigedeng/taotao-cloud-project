package com.taotao.cloud.sys.biz.tools.security.controller;

import com.sanri.tools.modules.core.security.UserService;
import com.sanri.tools.modules.core.security.dtos.GroupTree;
import com.sanri.tools.modules.core.security.dtos.RoleInfo;
import com.sanri.tools.modules.core.security.dtos.ThinUser;
import com.sanri.tools.modules.core.security.entitys.UserProfile;
import com.sanri.tools.modules.security.service.*;
import com.sanri.tools.modules.security.service.dtos.ResourceTree;
import com.sanri.tools.modules.security.service.repository.ResourceRepository;
import com.sanri.tools.modules.security.service.repository.RoleRepository;
import com.sanri.tools.modules.security.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 当前用户信息
 */
@RestController
@RequestMapping("/profile")
@Validated
public class ProfileController {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private ResourceService resourceService;

    @GetMapping
    public UserProfile profile() throws IOException {
        return profileService.profile();
    }

    /**
     * 当前登录人用户名
     */
    @GetMapping("/username")
    public String username(){
        return profileService.username();
    }

    /**
     * 当前登录人
     */
    @GetMapping("/user")
    public ThinUser user(){
        return profileService.user();
    }

    /**
     * 修改密码
     * @param oldPassword 旧密码
     * @param password 新密码
     */
    @PostMapping("/changePassword")
    public void changePassword(String oldPassword,String password){
        profileService.changePassword(oldPassword,password);
    }

    /**
     * 可访问组织列表
     * @return
     */
    @GetMapping("/accessGroups")
    public GroupTree accessGroups(){
        final List<String> accessGroups = profileService.queryAccessGroups();
        final List<Path> collect = accessGroups.stream().map(Paths::get).collect(Collectors.toList());
        final GroupTree groupTree = GroupService.convertPathsToGroupTree(collect);
        return groupTree;
    }

    /**
     * 可访问用户列表
     */
    @GetMapping("/accessUsers")
    public List<? extends ThinUser> accessUsers(){
        final List<String> accessUsers = profileService.queryAccessUsers();
        return userRepository.getUsers(accessUsers);
    }

    /**
     * 可访问角色列表
     */
    @GetMapping("/accessRoles")
    public List<RoleInfo> accessRoles(){
        final List<String> accessRoles = profileService.queryAccessRoles();
        return roleRepository.getRoles(accessRoles);
    }

    /**
     * 可访问资源列表
     * @return
     */
    @GetMapping("/accessResources")
    public List<ResourceTree> accessResources(){
        final List<String> accessResources = profileService.queryAccessResources();
        return resourceService.completionToTree(accessResources);
    }

    /**
     * 可访问菜单查询
     */
    @GetMapping("/accessMenus")
    public List<ResourceService.Menu> accessMenus(){
        return profileService.queryAccessMenus();
    }
}
