package com.taotao.cloud.standalone.system.modules.security.social.github.connect;

import com.taotao.cloud.standalone.system.modules.security.social.github.api.GitHub;
import com.taotao.cloud.standalone.system.modules.security.social.github.api.GitHubUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @Classname GiteeAdapter
 * @Description
 * @Author shuigedeng
 * @since 2019-07-08 21:49
 * @Version 1.0
 */
public class GitHubAdapter implements ApiAdapter<GitHub> {

    @Override
    public boolean test(GitHub api) {
        return true;
    }

    @Override
    public void setConnectionValues(GitHub api, ConnectionValues values) {
        GitHubUserInfo userInfo = api.getUserInfo();
        values.setProviderUserId(String.valueOf(userInfo.getId()));
        values.setDisplayName(userInfo.getUsername());
        values.setProfileUrl("https://github.com/" + userInfo.getId());
        values.setImageUrl(userInfo.getAvatarUrl());
    }

    @Override
    public UserProfile fetchUserProfile(GitHub api) {
        return null;
    }

    @Override
    public void updateStatus(GitHub api, String message) {

    }
}
