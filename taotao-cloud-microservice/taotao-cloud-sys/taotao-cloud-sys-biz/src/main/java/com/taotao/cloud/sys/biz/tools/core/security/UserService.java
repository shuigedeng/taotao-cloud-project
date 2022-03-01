package com.taotao.cloud.sys.biz.tools.core.security;


import com.taotao.cloud.sys.biz.tools.core.exception.SystemMessage;
import com.taotao.cloud.sys.biz.tools.core.security.dtos.ThinUser;
import com.taotao.cloud.sys.biz.tools.core.security.entitys.UserProfile;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 这里只做一个接口, 实现由安全服务来实现, 然后注入到需要使用安全的服务里面
 */
public interface UserService {

    /**
     * 当前登录人名称
     */
    String username();

    /**
     * 当前登录人
     * @return
     */
    ThinUser user();

    /**
     * 修改密码
     * @param oldPassword
     */
    void changePassword(String oldPassword,String password);

    /**
     * 登录人个人信息
     */
    UserProfile profile() throws IOException;

    /**
     * 查询可授权角色列表
     */
    List<String> queryAccessRoles();

    /**
     * 查询可授权资源列表
     */
    List<String> queryAccessResources();

    /**
     * 查询可授权用户列表
     */
    List<String> queryAccessUsers();

    /**
     * 查询可授权分组列表
     * @return
     */
    List<String> queryAccessGroups();

    /**
     * 检查用户是否有权限访问给定用户
     * @param username
     */
    public default void checkUserAccess(String username){
        final List<String> accessUsers = this.queryAccessUsers();
        if (!accessUsers.contains(username)){
            throw SystemMessage.ACCESS_DENIED_ARGS.exception("你无权限操作此数据:"+username);
        }
    }

    /**
     * 检查是否有所有组织的访问权限
     * @param groups 组织列表
     */
    public default void checkGroupAccess(String...groups){
        final List<String> accessGroups = this.queryAccessGroups();
        A:for (String group : groups) {
            final Path checkGroupPath = Paths.get(group);
            for (String accessGroup : accessGroups) {
                final Path accessGroupPath = Paths.get(accessGroup);
                if (checkGroupPath.startsWith(accessGroup)){
                    continue A;
                }
            }
            throw SystemMessage.ACCESS_DENIED_ARGS.exception("你无权限操作此数据:"+group);
        }
    }

    /**
     * 检查用户是否有权限访问指定角色
     * @param roles
     */
    public default void checkRoleAccess(String...roles){
        final List<String> accessRoles = this.queryAccessRoles();
        for (String role : roles) {
            if (!accessRoles.contains(role)){
                throw SystemMessage.ACCESS_DENIED_ARGS.exception("你无权限操作此数据:"+role);
            }
        }
    }

    /**
     * 检查用户是否有权限访问指定资源
     * @param resources
     */
    public default void checkResourceAccess(String...resources){
        final List<String> accessResources = this.queryAccessResources();
        for (String resource : resources) {
            if (!accessResources.contains(resource)){
                throw SystemMessage.ACCESS_DENIED_ARGS.exception("你无权限操作此数据:"+resource);
            }
        }
    }
}
