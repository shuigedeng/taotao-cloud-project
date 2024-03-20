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

package com.taotao.cloud.auth.biz.authentication.login.form.qrcode.service.impl;

import com.taotao.cloud.auth.biz.authentication.login.form.qrcode.service.OAuth2FormQrcodeUserDetailsService;
import java.util.Set;

import com.taotao.cloud.security.springsecurity.model.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultOAuth2FormQrcodeUserDetailsService implements OAuth2FormQrcodeUserDetailsService {
    @Override
    public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
        return SecurityUser.builder()
                .account("admin")
                .userId(1L)
                .username("admin")
                .nickname("admin")
                .password("$2a$10$ofQ95D2nNs1JC.JiPaGo3O11.P7sP3TkcRyXBpyfskwBDJRAh0caG")
                .phone("15730445331")
                .mobile("15730445331")
                .email("981376578@qq.com")
                .sex(1)
                .status(1)
                .type(2)
                .permissions(Set.of("xxx", "sldfl"))
                .build();
    }
}
