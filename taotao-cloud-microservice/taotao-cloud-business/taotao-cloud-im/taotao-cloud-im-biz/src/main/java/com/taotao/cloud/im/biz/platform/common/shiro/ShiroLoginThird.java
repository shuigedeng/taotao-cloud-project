package com.taotao.cloud.im.biz.platform.common.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 */
@Data
public class ShiroLoginThird implements AuthenticationToken {

    private String openId;

    public ShiroLoginThird(String openId) {
        this.openId = openId;
    }

    @Override
    public Object getPrincipal() {
        return openId;
    }

    @Override
    public Object getCredentials() {
        return openId;
    }

}
