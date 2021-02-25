package com.taotao.cloud.standalone.system.modules.security.social.gitee.connect;

import com.taotao.cloud.standalone.system.modules.security.social.gitee.api.Gitee;
import com.taotao.cloud.standalone.system.modules.security.social.gitee.api.GiteeUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @Classname GiteeAdapter
 * @Description
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-07-08 21:49
 * @Version 1.0
 */
public class GiteeAdapter implements ApiAdapter<Gitee> {

    @Override
    public boolean test(Gitee api) {
        return true;
    }

    @Override
    public void setConnectionValues(Gitee api, ConnectionValues values) {
        GiteeUserInfo userInfo = api.getUserInfo();
        values.setProviderUserId(String.valueOf(userInfo.getId()));
        values.setDisplayName(userInfo.getName());
        values.setProfileUrl(userInfo.getHtmlUrl());
        values.setImageUrl(userInfo.getAvatarUrl());
    }

    @Override
    public UserProfile fetchUserProfile(Gitee api) {
        return null;
    }

    @Override
    public void updateStatus(Gitee api, String message) {

    }
}
