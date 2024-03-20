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

package com.taotao.cloud.auth.biz.authentication.login.social.dingtalk;

import com.taotao.cloud.auth.biz.authentication.utils.AuthorityUtils;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
@EqualsAndHashCode(callSuper = false)
public class DingtalkOAuth2User implements OAuth2User, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 统一赋予USER角色
    private Set<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
    private Map<String, Object> attributes = new HashMap<>();
    private String nameAttributeKey;

    private String accessToken;

    /**
     * 所选企业corpId
     */
    private String corpId;

    /**
     * 超时时间
     */
    private Long expireIn;

    private String refreshToken;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 个人邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 昵称
     */
    private String nick;

    /**
     * openId
     */
    private String openId;

    /**
     * 手机号对应的国家号
     */
    private String stateCode;

    /**
     * unionId
     */
    private String unionId;

    @Override
    public String getName() {
        return nameAttributeKey;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
