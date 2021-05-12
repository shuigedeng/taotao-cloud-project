package com.taotao.cloud.oauth2.api.tmp.social.user;

import lombok.Data;

import java.util.Map;

@Data
public class QQOAuth2User implements CustomOAuth2User {
    public static final String TYPE = "qq";
    public static final String APP_ID = "appid";
    public static final String SECRET = "secret";

    private Map<String, Object> attributes;
    private String openid;
    private String nickname;
    private String figureurl;

    @Override
    public String getName() {
        return this.openid;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getAvatar() {
        return figureurl;
    }
}
