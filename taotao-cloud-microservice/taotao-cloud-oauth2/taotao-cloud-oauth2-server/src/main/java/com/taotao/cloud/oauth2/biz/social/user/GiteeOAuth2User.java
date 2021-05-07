package com.taotao.cloud.oauth2.biz.social.user;

import lombok.Data;

@Data
public class GiteeOAuth2User implements CustomOAuth2User {

    private static final long serialVersionUID = 1L;
    public static final String TYPE = "gitee";

    private String id;
    private String name;
    private String avatarUrl;

    @Override
    public String getName() {
        return this.id;
    }

    @Override
    public String getNickname() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatarUrl;
    }
}
