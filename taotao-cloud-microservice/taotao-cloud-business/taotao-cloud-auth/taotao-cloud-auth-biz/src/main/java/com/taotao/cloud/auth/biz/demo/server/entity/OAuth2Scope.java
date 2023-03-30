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

package com.taotao.cloud.auth.biz.demo.server.entity;

import cn.herodotus.engine.data.core.entity.BaseSysEntity;
import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import com.google.common.base.MoreObjects;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UuidGenerator;

/**
 * Description : Oauth Scope
 *
 * @author : gengwei.zheng
 * @date : 2020/3/19 14:15
 */
@Entity
@Table(
        name = "oauth2_scope",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"scope_code"})},
        indexes = {
            @Index(name = "oauth2_scope_id_idx", columnList = "scope_id"),
            @Index(name = "oauth2_scope_code_idx", columnList = "scope_code")
        })
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE,
        region = OAuth2Constants.REGION_OAUTH2_SCOPE)
public class OAuth2Scope extends BaseSysEntity {

    @Id
    @UuidGenerator
    @Column(name = "scope_id", length = 64)
    private String scopeId;

    @Column(name = "scope_code", length = 128, unique = true)
    private String scopeCode;

    @Column(name = "scope_name", length = 128)
    private String scopeName;

    @org.hibernate.annotations.Cache(
            usage = CacheConcurrencyStrategy.READ_WRITE,
            region = OAuth2Constants.REGION_OAUTH2_AUTHORITY)
    @ManyToMany(
            cascade = {
                CascadeType.PERSIST,
                CascadeType.DETACH,
                CascadeType.REMOVE,
                CascadeType.MERGE
            },
            fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "oauth2_scope_authority",
            joinColumns = {@JoinColumn(name = "scope_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"scope_id", "authority_id"})},
            indexes = {
                @Index(name = "oauth2_scope_authority_sid_idx", columnList = "scope_id"),
                @Index(name = "oauth2_scope_authority_aid_idx", columnList = "authority_id")
            })
    private Set<OAuth2Authority> authorities = new HashSet<>();

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getScopeCode() {
        return scopeCode;
    }

    public void setScopeCode(String scopeCode) {
        this.scopeCode = scopeCode;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public Set<OAuth2Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<OAuth2Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OAuth2Scope that = (OAuth2Scope) o;

        return new EqualsBuilder().append(getScopeId(), that.getScopeId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getScopeId()).toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("scopeId", scopeId)
                .add("scopeCode", scopeCode)
                .add("scopeName", scopeName)
                .toString();
    }
}
