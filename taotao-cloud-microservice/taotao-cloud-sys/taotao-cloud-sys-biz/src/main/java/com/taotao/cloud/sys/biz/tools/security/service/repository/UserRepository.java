package com.taotao.cloud.sys.biz.tools.security.service.repository;

import com.alibaba.fastjson.JSON;
import com.sanri.tools.modules.core.aspect.SerializerToFile;
import com.sanri.tools.modules.core.exception.SystemMessage;
import com.sanri.tools.modules.core.exception.ToolException;
import com.sanri.tools.modules.core.security.dtos.ThinUser;
import com.sanri.tools.modules.core.security.entitys.ToolUser;
import com.sanri.tools.modules.core.security.entitys.UserProfile;
import com.sanri.tools.modules.core.service.file.FileManager;
import com.sanri.tools.modules.security.service.dtos.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UserRepository implements InitializingBean {
    @Autowired
    private FileManager fileManager;

    /**
     * 所有用户信息存储
     */
    static final Map<String, SecurityUser> USERS = new ConcurrentHashMap<>();

    @SerializerToFile
    public void addUser(ThinUser thinUser){
        final ToolUser toolUser = thinUser.getToolUser();
        final SecurityUser securityUser = new SecurityUser(toolUser);
        securityUser.setRoles(thinUser.getRoles());
        securityUser.setGroups(thinUser.getGroups());
        USERS.put(toolUser.getUsername(),securityUser);
    }

    @SerializerToFile
    public void deleteUser(String username) throws IOException {
        USERS.remove(username);

        // 删除用户文件夹
        final File usersDir = fileManager.mkConfigDir("security/users");
        FileUtils.deleteDirectory(new File(usersDir,username));
    }

    /**
     * 是否存在用户
     * @param username 用户名
     */
    public boolean existUser(String username){
        return USERS.containsKey(username);
    }

    /**
     * 用户列表
     */
    public List<SecurityUser> findUsers() {
        return new ArrayList<>(USERS.values());
    }

    /**
     * 查询某个用户信息
     * @param username 用户名
     */
    public SecurityUser getUser(String username){
        return USERS.get(username);
    }

    /**
     * 获取用户属性信息
     * @param username 用户名
     * @return
     * @throws IOException
     */
    public UserProfile profile(String username) throws IOException {
        final File usersDir = fileManager.mkConfigDir("security/users");
        final File file = new File(usersDir, username);
        if (file.exists()){
            final File profile = new File(file, "profile");
            if (profile.exists()) {
                final String profileJSON = FileUtils.readFileToString(profile, StandardCharsets.UTF_8);
                return JSON.parseObject(profileJSON, UserProfile.class);
            }
        }
        return null;
    }

    /**
     * 批量获取用户列表
     * @param usernames 用户名列表
     */
    public List<? extends ThinUser> getUsers(List<String> usernames){
        return usernames.stream().map(USERS::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @SerializerToFile
    public void changePassword(String username,String password){
        final SecurityUser thinUser = USERS.get(username);
        thinUser.getToolUser().setPassword(password);
    }

    @SerializerToFile
    public void changeGroups(String username,List<String> groups){
        final SecurityUser securityUser = USERS.get(username);
        securityUser.setGroups(groups);
    }

    @SerializerToFile
    public void changeRoles(String username,String... roles){
        final SecurityUser securityUser = USERS.get(username);
        securityUser.setRoles(Arrays.asList(roles));
    }

    public void serializer() throws IOException {
        final File usersDir = fileManager.mkConfigDir("security/users");
        for (SecurityUser value : USERS.values()) {
            final String username = value.getUsername();
            final File baseFile = new File(usersDir, username+"/base");

            final String password = value.getPassword();
            final List<String> roles = value.getRoles();
            final List<String> groups = value.getGroups();

            String userBaseInfo = username + ":" + password +
                    ":" +
                    groups.stream().collect(Collectors.joining(",")) +
                    ":" +
                    roles.stream().collect(Collectors.joining(","));
            FileUtils.writeStringToFile(baseFile, userBaseInfo,StandardCharsets.UTF_8);
        }
    }

    /**
     * $configDir[Root]
     *   security[Dir]
     *    users[Dir]          所有用户的目录
     *     user1[Dir]        用户名
     *       base[File]      基础信息 用户名:密码:分组路径列表:角色列表 例 user1:123:sanri/dev,hd/dev,hd/test:role1,admin
     *       profile[File]    自定义信息,暂无
     *       ...
     *     user2[Dir]
     *       base[File]
     *       profile[File]
     *
     * 一个用户可以有多个分组, 多个角色
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 读取所有用户信息
        final File usersDir = fileManager.mkConfigDir("security/users");

        final File[] files = usersDir.listFiles();
        for (File userDir : files) {
            final String username = userDir.getName();
            // 初始化只加载 base 信息
            final File base = new File(userDir, "base");
            final String[] userFields = StringUtils.splitPreserveAllTokens(FileUtils.readFileToString(base, StandardCharsets.UTF_8), ":", 4);
            final ToolUser toolUser = new ToolUser(userFields[0], userFields[1]);
            final SecurityUser securityUser = new SecurityUser(toolUser);

            // 用户分组信息添加
            if (StringUtils.isNotBlank(userFields[2])){
                final String[] groups = StringUtils.splitPreserveAllTokens(userFields[2], ",");
                for (String groupPath : groups) {
                    securityUser.addGroup(groupPath);
                }
            }

            // 用户角色信息添加
            if (StringUtils.isNotBlank(userFields[3])){
                final String[] roles = StringUtils.splitPreserveAllTokens(userFields[3], ",");
                for (String role : roles) {
                    securityUser.addRole(role);
                }
            }

            USERS.put(username,securityUser);
        }
    }

    /**
     * 查找组织的用户
     * @param path 查找路径
     * @param includeChild 是否包含子路径
     * @return
     */
    public Set<String> findUsersByGroup(Path findPath,boolean includeChild) {
        Set<String> usernames = new HashSet<>();
        A: for (SecurityUser value : USERS.values()) {
            final List<String> groups = value.getGroups();
            for (String group : groups) {
                final Path userGroupPath = Paths.get(group);
                if (!includeChild && userGroupPath.equals(findPath)){
                    usernames.add(value.getUsername());
                    continue A;
                }else if (includeChild && userGroupPath.startsWith(findPath)){
                    usernames.add(value.getUsername());
                    continue A;
                }
            }
        }
        return usernames;
    }
}
