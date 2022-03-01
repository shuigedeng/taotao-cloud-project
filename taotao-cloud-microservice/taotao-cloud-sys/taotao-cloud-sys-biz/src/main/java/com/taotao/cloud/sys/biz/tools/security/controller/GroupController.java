package com.taotao.cloud.sys.biz.tools.security.controller;

import com.sanri.tools.modules.security.controller.dtos.Mount;
import com.sanri.tools.modules.security.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.nio.file.Paths;
import java.util.Set;

@RestController
@RequestMapping("/security/group")
@Validated
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 添加组织, 每个用户都只能在自己允许的父级组织内添加组织
     * @param parentGroup 父级组织
     * @param childGroup  子组织
     */
    @PostMapping("/add")
    public void addGroup(String parentGroup,String childGroup){
        groupService.addGroup(parentGroup,childGroup);
    }

    /**
     * 删除组织
     * @param groupPath 组织路径
     */
    @PostMapping("/del")
    public void delGroup(String groupPath){
        groupService.delGroup(groupPath);
    }

    /**
     * 加载挂载信息
     * @param groupPath
     * @param loadChild
     * @return
     */
    @GetMapping("/mount")
    public Mount mount(@NotBlank String groupPath, boolean loadChild){
        final Set<String> mountUsers = groupService.findGroupUsers(Paths.get(groupPath), loadChild);
        final Set<String> mountRoles = groupService.findGroupRoles(Paths.get(groupPath), loadChild);
        final Set<String> mountResources = groupService.findGroupResources(Paths.get(groupPath), loadChild);
        return new Mount(mountUsers,mountRoles,mountResources);
    }

    /**
     * 组织挂载的用户列表
     * @param groupPath 组织路径
     * @param loadChild 是否加载子组织的数据
     * @return
     */
    @GetMapping("/mount/users")
    public Set<String> mountUsers(@NotBlank String groupPath, boolean loadChild){
        return groupService.findGroupUsers(Paths.get(groupPath), loadChild);
    }

    /**
     * 组织挂载的角色列表
     * @param groupPath 组织路径
     * @param loadChild 是否加载子组织的数据
     * @return
     */
    @GetMapping("/mount/roles")
    public Set<String> mountRoles(@NotBlank String groupPath, boolean loadChild){
        return groupService.findGroupRoles(Paths.get(groupPath), loadChild);
    }

    /**
     * 组织挂载的资源列表
     * @param groupPath 组织路径
     * @param loadChild 是否加载子组织的数据
     * @return
     */
    @GetMapping("/mount/resources")
    public Set<String> mountResources(@NotBlank String groupPath, boolean loadChild){
        return groupService.findGroupResources(Paths.get(groupPath), loadChild);
    }
}
