package com.taotao.cloud.oauth2.api.tmp.social.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@Getter
public class UserDetailOAuthUser implements CustomOAuth2User {
    private final UserDetails userDetails;
    private final CustomOAuth2User customOAuth2User;

    @Override
    public String getName() {
        return userDetails.getUsername();
    }

    @Override
    public String getNickname() {
        return customOAuth2User.getNickname();
    }

    @Override
    public String getAvatar() {
        return customOAuth2User.getAvatar();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return customOAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

}
