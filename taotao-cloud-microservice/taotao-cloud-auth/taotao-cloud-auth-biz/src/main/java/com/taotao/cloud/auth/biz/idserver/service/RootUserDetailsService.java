package com.taotao.cloud.auth.biz.idserver.service;


import com.taotao.cloud.auth.biz.idserver.entity.UserInfo;
import com.taotao.cloud.auth.biz.idserver.enumate.RootUserConstants;

/**
 * The interface Root user details service.
 *
 * @author felord.cn
 * @see cn.felord.idserver.enumate.RootUserConstants
 * @since 1.0.0
 */
@FunctionalInterface
public interface RootUserDetailsService {


    default UserInfo loadRootUserByUsername(String username) {
        if (!RootUserConstants.ROOT_USERNAME.val().equals(username)) {
            throw new IllegalArgumentException("仅提供给root用户");
        }
        return this.doLoadRootUser(username);
    }

    /**
     * Load user by username user details.
     *
     * @param username the username
     * @return the user details
     */
    UserInfo doLoadRootUser(String username);
}
