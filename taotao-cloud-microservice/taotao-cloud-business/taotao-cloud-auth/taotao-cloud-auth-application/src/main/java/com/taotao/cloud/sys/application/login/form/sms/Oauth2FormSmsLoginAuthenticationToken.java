/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.auth.biz.authentication.login.form.sms;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class Oauth2FormSmsLoginAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;
    private String captcha;
    private String type;

    /**
     * 此构造函数用来初始化未授信凭据.
     *
     * @param principal the principal
     * @param captcha the captcha
     */
    public Oauth2FormSmsLoginAuthenticationToken(Object principal, String captcha, String type) {
        super(null);
        this.principal = principal;
        this.captcha = captcha;
        this.type = type;
        setAuthenticated(false);
    }

    /**
     * 此构造函数用来初始化授信凭据.
     *
     * @param principal the principal
     * @param captcha the captcha
     * @param authorities the authorities
     */
    public Oauth2FormSmsLoginAuthenticationToken(
            Object principal, String captcha, String type, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.captcha = captcha;
        this.type = type;
        // must use super, as we override
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.captcha;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a"
                    + " GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        captcha = null;
    }

    public String getType() {
        return type;
    }

    public static Oauth2FormSmsLoginAuthenticationToken unauthenticated(Object principal, String captcha, String type) {
        return new Oauth2FormSmsLoginAuthenticationToken(principal, captcha, type);
    }

    public static Oauth2FormSmsLoginAuthenticationToken authenticated(
            Object principal, String captcha, String type, Collection<? extends GrantedAuthority> authorities) {
        return new Oauth2FormSmsLoginAuthenticationToken(principal, captcha, type, authorities);
    }
}
