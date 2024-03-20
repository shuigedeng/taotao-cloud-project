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

package com.taotao.cloud.auth.biz.authentication.login.extension.email;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class EmailAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private String emailCode;

    public EmailAuthenticationToken(Object principal, String emailCode) {
        super(null);
        this.emailCode = emailCode;
        this.principal = principal;
        setAuthenticated(false);
    }

    public EmailAuthenticationToken(
            Object principal, String emailCode, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.emailCode = emailCode;
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public static EmailAuthenticationToken unauthenticated(Object principal, String emailCode) {
        return new EmailAuthenticationToken(principal, emailCode);
    }

    public static EmailAuthenticationToken authenticated(
            Object principal, String emailCode, Collection<? extends GrantedAuthority> authorities) {
        return new EmailAuthenticationToken(principal, emailCode, authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }
}
