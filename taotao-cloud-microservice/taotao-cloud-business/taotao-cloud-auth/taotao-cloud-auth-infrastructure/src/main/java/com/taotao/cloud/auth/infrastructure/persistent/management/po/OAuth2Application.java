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

package com.taotao.cloud.auth.infrastructure.persistent.management.po;

import com.google.common.base.MoreObjects;
import com.taotao.boot.security.spring.constants.OAuth2Constants;
import com.taotao.boot.security.spring.enums.ApplicationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UuidGenerator;

/**
 * <p>OAuth2 应用 </p>
 * <p>
 * Spring Authorization Server 默认的 RegisteredClient 不便于扩展。增加该类用于存储标准 RegisteredClient 表结构以外的扩展信息。
 *
 * @since : 2022/3/1 16:45
 */
@Schema(name = "OAuth2应用实体")
@Entity
@Table(
	name = "oauth2_application",
	indexes = {
		@Index(name = "oauth2_application_id_idx", columnList = "application_id"),
		@Index(name = "oauth2_application_cid_idx", columnList = "client_id")
	})
@Cacheable
@org.hibernate.annotations.Cache(
	usage = CacheConcurrencyStrategy.READ_WRITE,
	region = OAuth2Constants.REGION_OAUTH2_APPLICATION)
public class OAuth2Application extends AbstractOAuth2RegisteredClient {

	@Schema(name = "应用ID")
	@Id
	@UuidGenerator
	@Column(name = "application_id", length = 64)
	private String applicationId;

	@Schema(name = "应用名称", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "应用名称不能为空")
	@Column(name = "application_name", length = 128)
	private String applicationName;

	@Schema(name = "应用简称", title = "应用的简称、别名、缩写等信息")
	@Column(name = "abbreviation", length = 64)
	private String abbreviation;

	@Schema(name = "Logo", title = "Logo存储信息，可以是URL或者路径等")
	@Column(name = "logo", length = 1024)
	private String logo;

	@Schema(name = "主页信息", title = "应用相关的主页信息方便查询")
	@Column(name = "homepage", length = 1024)
	private String homepage;

	@Schema(name = "应用类型", title = "用于区分不同类型的应用")
	@Column(name = "application_type")
	@Enumerated(EnumType.ORDINAL)
	private ApplicationType applicationType = ApplicationType.WEB;

	@Schema(name = "应用对应Scope", title = "传递应用对应Scope ID数组")
	@org.hibernate.annotations.Cache(
		usage = CacheConcurrencyStrategy.READ_WRITE,
		region = OAuth2Constants.REGION_OAUTH2_APPLICATION_SCOPE)
	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(
		name = "oauth2_application_scope",
		joinColumns = {@JoinColumn(name = "application_id")},
		inverseJoinColumns = {@JoinColumn(name = "scope_id")},
		uniqueConstraints = {@UniqueConstraint(columnNames = {"application_id", "scope_id"})},
		indexes = {
			@Index(name = "oauth2_application_scope_aid_idx", columnList = "application_id"),
			@Index(name = "oauth2_application_scope_sid_idx", columnList = "scope_id")
		})
	private Set<OAuth2Scope> scopes = new HashSet<>();

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	@Override
	public Set<OAuth2Scope> getScopes() {
		return scopes;
	}

	public void setScopes(Set<OAuth2Scope> scopes) {
		this.scopes = scopes;
	}

	@Override
	public String getId() {
		return getApplicationId();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		OAuth2Application that = (OAuth2Application) o;
		return Objects.equals(applicationId, that.applicationId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(applicationId);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("applicationId", applicationId)
			.add("applicationName", applicationName)
			.add("abbreviation", abbreviation)
			.add("logo", logo)
			.add("homepage", homepage)
			.add("applicationType", applicationType)
			.toString();
	}
}
