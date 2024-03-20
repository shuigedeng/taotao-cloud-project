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

package com.taotao.cloud.auth.biz.authentication.login.social.github;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class GithubOAuth2User implements OAuth2User, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 统一赋予USER角色
    private Set<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
    private Map<String, Object> attributes = new HashMap<>();
    private String nameAttributeKey;

    @JsonProperty("gists_url")
    private String gistsUrl;

    @JsonProperty("repos_url")
    private String reposUrl;

    @JsonProperty("following_url")
    private String followingUrl;

    @JsonProperty("twitter_username")
    private String twitterUsername;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("login")
    private String login;

    @JsonProperty("type")
    private String type;

    @JsonProperty("blog")
    private String blog;

    @JsonProperty("subscriptions_url")
    private String subscriptionsUrl;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("site_admin")
    private boolean siteAdmin;

    @JsonProperty("company")
    private String company;

    @JsonProperty("id")
    private int id;

    @JsonProperty("public_repos")
    private int publicRepos;

    @JsonProperty("gravatar_id")
    private String gravatarId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("organizations_url")
    private String organizationsUrl;

    @JsonProperty("hireable")
    private boolean hireable;

    @JsonProperty("starred_url")
    private String starredUrl;

    @JsonProperty("followers_url")
    private String followersUrl;

    @JsonProperty("public_gists")
    private int publicGists;

    @JsonProperty("url")
    private String url;

    @JsonProperty("received_events_url")
    private String receivedEventsUrl;

    @JsonProperty("followers")
    private int followers;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("events_url")
    private String eventsUrl;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("following")
    private int following;

    @JsonProperty("name")
    private String name;

    @JsonProperty("location")
    private String location;

    @JsonProperty("node_id")
    private String nodeId;

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
        return this.authorities;
    }
}
