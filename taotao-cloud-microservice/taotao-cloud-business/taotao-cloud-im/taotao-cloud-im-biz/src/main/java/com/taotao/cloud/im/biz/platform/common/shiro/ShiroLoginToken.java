package com.taotao.cloud.im.biz.platform.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 */
public class ShiroLoginToken implements AuthenticationToken {
    private static final long serialVersionUID = 1L;

    private String token;

    public ShiroLoginToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
