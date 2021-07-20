package com.taotao.cloud.standalone.system.modules.security.social.github.api;

/**
 * @Classname GitHub
 * @Description TODO
 * @Author shuigedeng
 * @since 2019-07-08 22:04
 * @Version 1.0
 */
public interface GitHub {

    /**
     * 获取用户信息
     *
     * @return
     */
    GitHubUserInfo getUserInfo();
}
