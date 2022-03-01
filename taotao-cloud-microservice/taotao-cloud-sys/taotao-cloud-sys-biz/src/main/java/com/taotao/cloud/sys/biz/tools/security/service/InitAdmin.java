package com.taotao.cloud.sys.biz.tools.security.service;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sanri.tools.modules.core.security.dtos.RoleInfo;
import com.sanri.tools.modules.core.security.dtos.ThinUser;
import com.sanri.tools.modules.core.security.entitys.ToolMenu;
import com.sanri.tools.modules.core.security.entitys.ToolResource;
import com.sanri.tools.modules.core.security.entitys.ToolRole;
import com.sanri.tools.modules.core.security.entitys.ToolUser;
import com.sanri.tools.modules.security.service.repository.GroupRepository;
import com.sanri.tools.modules.security.service.repository.ResourceRepository;
import com.sanri.tools.modules.security.service.repository.RoleRepository;
import com.sanri.tools.modules.security.service.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InitAdmin implements InitializingBean {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        final String username = "admin";
        final String password = "0";
        final String rolename = "admin";
        final String rootGroup = "/";

        if (!userRepository.existUser(username)) {
            log.info("初始化用户 admin, 密码 0, 角色 admin, 分组 /");
            final List<ToolMenu> menus = resourceRepository.findMenus();
            final List<String> topMenus = menus.stream().filter(menu -> StringUtils.isBlank(menu.getParentResourceId()))
                    .map(ToolResource::getResourceId).collect(Collectors.toList());
            groupRepository.addGroup(Paths.get("/"));

            final RoleInfo roleInfo = new RoleInfo(new ToolRole(rolename));
            roleInfo.setResources(topMenus);
            roleInfo.addGroup(rootGroup);
            roleRepository.addRole(roleInfo);

            final ThinUser thinUser = new ThinUser(new ToolUser(username, password));
            thinUser.addRole(rolename);
            thinUser.addGroup(rootGroup);
            userRepository.addUser(thinUser);
        }
    }
}
