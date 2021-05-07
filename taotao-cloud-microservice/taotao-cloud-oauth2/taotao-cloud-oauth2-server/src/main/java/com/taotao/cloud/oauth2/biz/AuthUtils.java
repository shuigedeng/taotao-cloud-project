package com.taotao.cloud.oauth2.biz;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUtils {

    public static UserDetails translate(AuthUser user) {
        if (user == null) {
            return null;
        }
        AuthUserDetails userDetails = new AuthUserDetails(user.getId(),
                user.getPassword(),
                user.getEnabled(),
                AuthorityUtils.commaSeparatedStringToAuthorityList("USER"));
        userDetails.setLogin(user.getUsername());
        userDetails.setNickname(user.getNickname());
        userDetails.setPhone(user.getPhone());
        return userDetails;
    }
}
