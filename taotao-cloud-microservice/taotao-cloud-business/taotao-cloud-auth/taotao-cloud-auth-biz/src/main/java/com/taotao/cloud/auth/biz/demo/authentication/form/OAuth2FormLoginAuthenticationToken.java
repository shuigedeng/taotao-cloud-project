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

package com.taotao.cloud.auth.biz.demo.authentication.form;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Description: OAuth2 (Security) 表单登录 Token
 *
 * @author : gengwei.zheng
 * @date : 2022/4/12 10:24
 * @see UsernamePasswordAuthenticationToken
 */
public class OAuth2FormLoginAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public OAuth2FormLoginAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public OAuth2FormLoginAuthenticationToken(
            Object principal,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
