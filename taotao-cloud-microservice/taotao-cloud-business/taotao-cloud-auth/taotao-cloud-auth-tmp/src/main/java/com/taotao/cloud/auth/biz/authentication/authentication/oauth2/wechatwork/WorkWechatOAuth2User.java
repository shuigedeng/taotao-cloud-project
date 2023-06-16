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

package com.taotao.cloud.auth.biz.authentication.authentication.oauth2.wechatwork;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.taotao.cloud.auth.biz.authentication.utils.AuthorityUtils;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Data
public class WorkWechatOAuth2User implements OAuth2User {

	// 统一赋予USER角色
	private Set<GrantedAuthority> authorities =  AuthorityUtils.createAuthorityList("ROLE_USER");
	private Map<String, Object> attributes = new HashMap<>();
	private String nameAttributeKey;

    private Integer errcode;
    private String errmsg;

    @JsonAlias("OpenId")
    private String openId;

    @JsonAlias("UserId")
    private String userId;

    @Override
    public Map<String, Object> getAttributes() {
        // todo 这里放一些有用的额外参数
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // todo 微信用户你想赋权的可以在这里或者set方法中实现。
        return this.authorities;
    }

    @Override
    public String getName() {
        return Optional.ofNullable(userId).orElse(openId);
    }
}
