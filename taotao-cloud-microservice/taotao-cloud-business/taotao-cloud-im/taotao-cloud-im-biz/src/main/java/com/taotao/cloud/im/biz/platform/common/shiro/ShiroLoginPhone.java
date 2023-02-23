package com.taotao.cloud.im.biz.platform.common.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 */
@Data
public class ShiroLoginPhone implements AuthenticationToken {

    private String phone;

    public ShiroLoginPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }

    @Override
    public Object getCredentials() {
        return phone;
    }

}
