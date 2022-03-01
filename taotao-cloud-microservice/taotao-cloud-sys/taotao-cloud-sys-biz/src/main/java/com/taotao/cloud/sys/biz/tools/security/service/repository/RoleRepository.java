package com.taotao.cloud.sys.biz.tools.security.service.repository;

import com.sanri.tools.modules.core.aspect.SerializerToFile;
import com.sanri.tools.modules.core.security.dtos.RoleInfo;
import com.sanri.tools.modules.core.security.entitys.ToolRole;
import com.sanri.tools.modules.core.service.file.FileManager;
import com.sanri.tools.modules.security.service.dtos.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class RoleRepository implements InitializingBean {
    @Autowired
    private FileManager fileManager;

    /**
     * 所有角色信息
     */
    private static final Map<String,RoleInfo> roleInfoMap = new HashMap<>();

    @SerializerToFile
    public void addRole(RoleInfo roleInfo){
        roleInfoMap.put(roleInfo.getToolRole().getRolename(), roleInfo);
    }

    @SerializerToFile
    public void deleteRole(String rolename){
        roleInfoMap.remove(rolename);
    }

    /**
     * 是否存在角色
     * @param rolename 角色名称
     */
    public boolean existRole(String rolename){
        return roleInfoMap.containsKey(rolename);
    }

    /**
     * 角色列表
     */
    public Set<String> findRoles(){
        return roleInfoMap.keySet();
    }

    /**
     * 获取某一个角色信息
     * @param rolename 角色名称
     */
    public RoleInfo getRole(String rolename){
        return roleInfoMap.get(rolename);
    }

    /**
     * 批量获取角色列表
     * @param rolenames 角色名称列表
     * @return
     */
    public List<RoleInfo> getRoles(List<String> rolenames){
        return rolenames.stream().map(roleInfoMap::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @SerializerToFile
    public void changeGroups(String rolename,List<String> groups){
        final RoleInfo roleInfo = roleInfoMap.get(rolename);
        roleInfo.setGroups(groups);
    }

    @SerializerToFile
    public void changeResources(String rolename,List<String> resources){
        final RoleInfo roleInfo = roleInfoMap.get(rolename);
        roleInfo.setResources(resources);
    }

    public void serializer() throws IOException {
        List<String> roleSerializer = new ArrayList<>();
        for (RoleInfo value : roleInfoMap.values()) {
            final ToolRole toolRole = value.getToolRole();
            final List<String> groups = value.getGroups();
            final List<String> resources = value.getResources();
            roleSerializer.add(toolRole.getRolename()+":"+StringUtils.join(groups,",")+":"+StringUtils.join(resources,","));
        }
        fileManager.writeConfig("security","roles",StringUtils.join(roleSerializer,'\n'));
    }

    /**
     * $configDir[Root]
     *   security[Dir]
     *     roles[File]:     role1:group1,group2:resource1,resource2
     *                      role2:group1,group2:resource1,resource2
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        final String readConfig = fileManager.readConfig("security", "roles");
        if (StringUtils.isNotBlank(readConfig)) {
            final String[] roles = StringUtils.split(readConfig, '\n');
            for (String role : roles) {
                final String[] splitPreserveAllTokens = StringUtils.splitPreserveAllTokens(role, ":");
                if (splitPreserveAllTokens.length != 3){
                    log.warn("角色配置错误:{}",role);
                    continue;
                }
                final ToolRole toolRole = new ToolRole(splitPreserveAllTokens[0]);

                final RoleInfo roleInfo = new RoleInfo(toolRole);

                // 角色组织关联
                final String[] groups = StringUtils.split(splitPreserveAllTokens[1],',');
                for (String groupPath : groups) {
                    roleInfo.addGroup(groupPath);
                }

                // 角色资源关联
                final String[] resources = StringUtils.split(splitPreserveAllTokens[2],',');
                for (String resourceName : resources) {
                    roleInfo.addResource(resourceName);
                }

                roleInfoMap.put(toolRole.getRolename(),roleInfo);
            }
        }
    }

    /**
     * 查询组织下的角色
     * @param path          组织路径
     * @param includeChild  是否包含子组织
     * @return
     */
    public Set<String> findRolesByGroup(Path findPath, boolean includeChild) {
        Set<String> rolenames = new HashSet<>();
        A: for (RoleInfo value : roleInfoMap.values()) {
            final List<String> groups = value.getGroups();
            for (String group : groups) {
                final Path roleGroupPath = Paths.get(group);
                if (!includeChild && roleGroupPath.equals(findPath)){
                    rolenames.add(value.getToolRole().getRolename());
                    continue A;
                }else if (includeChild && roleGroupPath.startsWith(findPath)){
                    rolenames.add(value.getToolRole().getRolename());
                    continue A;
                }
            }
        }
        return rolenames;
    }
}
