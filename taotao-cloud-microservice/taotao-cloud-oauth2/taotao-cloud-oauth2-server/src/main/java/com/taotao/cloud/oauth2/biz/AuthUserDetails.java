package com.taotao.cloud.oauth2.biz;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * username 存的 才是 userId !!!!!
 */
@Getter
@Setter
public class AuthUserDetails extends User {

    // 账号
    private String login;
    // 昵称
    private String nickname;
    // 手机号
    private String phone;

    public AuthUserDetails(String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
    }
}
