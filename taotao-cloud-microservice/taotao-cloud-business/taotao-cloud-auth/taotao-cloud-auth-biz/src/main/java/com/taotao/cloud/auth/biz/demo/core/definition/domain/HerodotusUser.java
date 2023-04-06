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

package com.taotao.cloud.auth.biz.demo.core.definition.domain;

import cn.herodotus.engine.oauth2.core.jackson2.HerodotusUserDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * @author gengwei.zheng
 */
@JsonDeserialize(using = HerodotusUserDeserializer.class)
public class HerodotusUser implements UserDetails, CredentialsContainer {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String userId;

    private String password;

    private String username;

    private Set<GrantedAuthority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private Set<String> roles;

    private String employeeId;

    private String avatar;

    public HerodotusUser() {}

    /** Calls the more complex constructor with all boolean arguments set to {@code true}. */
    public HerodotusUser(
            String userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(userId, username, password, authorities, null);
    }

    /** Calls the more complex constructor with all boolean arguments set to {@code true}. */
    public HerodotusUser(
            String userId,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Set<String> roles) {
        this(userId, username, password, true, true, true, true, authorities, roles, null, null);
    }

    /**
     * Construct the <code>User</code> with the details required by <code>
     * org.springframework.security.authentication.dao.DaoAuthenticationProvider</code>
     *
     * @param username the username presented to the <code>DaoAuthenticationProvider</code>
     * @param password the password that should be presented to the <code>DaoAuthenticationProvider
     *     </code>
     * @param enabled set to <code>true</code> if the user is enabled
     * @param accountNonExpired set to <code>true</code> if the account has not expired
     * @param credentialsNonExpired set to <code>true</code> if the credentials have not expired
     * @param accountNonLocked set to <code>true</code> if the account is not locked
     * @param authorities the authorities that should be granted to the caller if they presented the
     *     correct username and password and the user is enabled. Not null.
     * @throws IllegalArgumentException if a <code>null</code> value was passed either as a
     *     parameter or as an element in the <code>GrantedAuthority</code> collection
     */
    public HerodotusUser(
            String userId,
            String username,
            String password,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities,
            Set<String> roles,
            String employeeId,
            String avatar) {
        Assert.isTrue(
                username != null && !"".equals(username) && password != null,
                "Cannot pass null or empty values to constructor");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
        this.roles = CollectionUtils.isNotEmpty(roles) ? roles : new HashSet<>();
        this.employeeId = employeeId;
        this.avatar = avatar;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    public String getUserId() {
        return userId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getAvatar() {
        return avatar;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per
        // UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to
            // the set. If the authority is null, it is a custom authority and should
            // precede others.
            if (g2.getAuthority() == null) {
                return -1;
            }
            if (g1.getAuthority() == null) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

    /**
     * Returns {@code true} if the supplied object is a {@code User} instance with the same {@code
     * username} value.
     *
     * <p>In other words, the objects are equal if they have the same username, representing the
     * same principal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HerodotusUser that = (HerodotusUser) o;
        return Objects.equal(userId, that.userId) && Objects.equal(username, that.username);
    }

    /** Returns the hashcode of the {@code username}. */
    @Override
    public int hashCode() {
        return Objects.hashCode(userId, username);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", userId)
                .add("password", "[PROTECTED]")
                .add("username", username)
                .add("accountNonExpired", accountNonExpired)
                .add("accountNonLocked", accountNonLocked)
                .add("credentialsNonExpired", credentialsNonExpired)
                .add("enabled", enabled)
                .add("employeeId", employeeId)
                .add("avatar", avatar)
                .toString();
    }
}
