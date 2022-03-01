package com.taotao.cloud.sys.biz.tools.security.service;

import com.sanri.tools.modules.core.exception.ToolException;
import com.sanri.tools.modules.core.security.UserService;
import com.sanri.tools.modules.core.security.dtos.ResourceInfo;
import com.sanri.tools.modules.core.security.dtos.RoleInfo;
import com.sanri.tools.modules.core.security.dtos.ThinUser;
import com.sanri.tools.modules.core.security.entitys.ToolUser;
import com.sanri.tools.modules.core.security.entitys.UserProfile;
import com.sanri.tools.modules.security.service.dtos.SecurityUser;
import com.sanri.tools.modules.security.service.repository.ResourceRepository;
import com.sanri.tools.modules.security.service.repository.RoleRepository;
import com.sanri.tools.modules.security.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ProfileService implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserManagerService userManagerService;

    @Override
    public String username() {
        return user().getToolUser().getUsername();
    }

    @Override
    public ThinUser user() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (SecurityUser) principal;
    }

    @Override
    public void changePassword(String oldPassword, String password) {
        final ToolUser toolUser = user().getToolUser();
        if (!toolUser.getPassword().equals(oldPassword)){
            throw new ToolException("旧密码不正确");
        }
        toolUser.setPassword(password);
        userRepository.changePassword(toolUser.getUsername(),password);
    }

    @Override
    public UserProfile profile() throws IOException {
        return userRepository.profile(username());
    }

    @Override
    public List<String> queryAccessRoles() {
        return userManagerService.queryAccessRoles(username());
    }


    @Override
    public List<String> queryAccessResources() {
       return userManagerService.queryAccessResources(username());
    }

    @Override
    public List<String> queryAccessUsers() {
        return userManagerService.queryAccessUsers(username());
    }

    @Override
    public List<String> queryAccessGroups() {
        return userManagerService.queryAccessGroups(username());
    }

    /**
     * 查询可访问的菜单列表
     */
    public List<ResourceService.Menu> queryAccessMenus(){
        final List<String> accessResources = this.queryAccessResources();
        return resourceService.loadMenusFromNames(accessResources);
    }
}
