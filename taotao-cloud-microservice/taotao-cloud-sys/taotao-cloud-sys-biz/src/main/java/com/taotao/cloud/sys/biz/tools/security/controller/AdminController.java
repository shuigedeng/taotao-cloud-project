package com.taotao.cloud.sys.biz.tools.security.controller;

import com.taotao.cloud.sys.biz.tools.codepatch.controller.dtos.GroupRepository;
import com.taotao.cloud.sys.biz.tools.core.security.dtos.GroupTree;
import com.taotao.cloud.sys.biz.tools.core.security.entitys.ToolResource;
import com.taotao.cloud.sys.biz.tools.core.security.entitys.ToolUser;
import com.taotao.cloud.sys.biz.tools.security.service.GroupService;
import com.taotao.cloud.sys.biz.tools.security.service.dtos.SecurityUser;
import com.taotao.cloud.sys.biz.tools.security.service.repository.ResourceRepository;
import com.taotao.cloud.sys.biz.tools.security.service.repository.RoleRepository;
import com.taotao.cloud.sys.biz.tools.security.service.repository.UserRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 这个类里的方法, 只允许管理员调用
 */
@RestController
@RequestMapping("/security/admin")
@Validated
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ResourceRepository resourceRepository;

    /**
     * 所有用户信息
     * @return
     */
    @GetMapping("/users")
    public List<ToolUser> users(){
        return userRepository.findUsers().stream().map(SecurityUser::getToolUser).collect(Collectors.toList());
    }

    /**
     * 所有角色信息
     * @return
     */
    @GetMapping("/roles")
    public Set<String> roles(){return roleRepository.findRoles();}

    /**
     * 所有分组信息
     * @return
     */
    @GetMapping("/group/tree")
    public GroupTree groupTrees() {
        final List<Path> groups = groupRepository.findGroups();
        return GroupService.convertPathsToGroupTree(groups);
    }
    /**
     * 所有资源信息
     * @return
     */
    @GetMapping("/resources")
    public List<ToolResource> toolsResources(){
        return resourceRepository.findResources().stream().map(ResourceInfo::getToolResource).collect(Collectors.toList());
    }

}
