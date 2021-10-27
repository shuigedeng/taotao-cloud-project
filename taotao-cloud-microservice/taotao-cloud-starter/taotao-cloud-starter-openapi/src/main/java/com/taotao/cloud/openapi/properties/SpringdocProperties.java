/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.openapi.properties;

import com.taotao.cloud.core.utils.PropertyUtil;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SpringdocProperties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:11
 */
@ConfigurationProperties(SpringdocProperties.PREFIX)
public class SpringdocProperties {

	public static final String PREFIX = "taotao.cloud.springdoc";

	/**
	 * 是否开启springdoc
	 **/
	private Boolean enabled = true;
	/**
	 * group default applicationName
	 **/
	private String group = PropertyUtil.getProperty("spring.application.name");

	/**
	 * pathsToMatch default /**
	 **/
	private String[] pathsToMatch = new String[]{"/**"};

	/**
	 * version default taotaoCloudVersion
	 **/
	private String version = PropertyUtil.getProperty("taotaoCloudVersion");
	/**
	 * SecuritySchemes
	 **/
	private Map<String, SecurityScheme> SecuritySchemes = new HashMap<>();
	/**
	 * Headers
	 **/
	private Map<String, Header> Headers = new HashMap<>();
	/**
	 * Headers
	 **/
	private List<Server> servers = new ArrayList<>();
	/**
	 * title
	 **/
	private String title =
		PropertyUtil.getProperty("spring.application.name").toUpperCase() + " API";
	/**
	 * description
	 **/
	private String description = "TAOTAO CLOUD 电商及大数据平台";
	/**
	 * contact
	 **/
	private Contact contact;
	/**
	 * termsOfService
	 **/
	private String termsOfService = "http://taotaocloud.com/terms/";
	/**
	 * license
	 **/
	private License license;
	/**
	 * externalDescription
	 **/
	private String externalDescription = "TaoTao Cloud Wiki Documentation";
	/**
	 * externalUrl
	 **/
	private String externalUrl = "https://github.com/shuigedeng/taotao-cloud-project/wiki";
	/**
	 * openapi
	 **/
	private String openapi = "3.0.1";

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String[] getPathsToMatch() {
		return pathsToMatch;
	}

	public void setPathsToMatch(String[] pathsToMatch) {
		this.pathsToMatch = pathsToMatch;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, SecurityScheme> getSecuritySchemes() {
		return SecuritySchemes;
	}

	public void setSecuritySchemes(
		Map<String, SecurityScheme> securitySchemes) {
		SecuritySchemes = securitySchemes;
	}

	public Map<String, Header> getHeaders() {
		return Headers;
	}

	public void setHeaders(Map<String, Header> headers) {
		Headers = headers;
	}

	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getTermsOfService() {
		return termsOfService;
	}

	public void setTermsOfService(String termsOfService) {
		this.termsOfService = termsOfService;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public String getExternalDescription() {
		return externalDescription;
	}

	public void setExternalDescription(String externalDescription) {
		this.externalDescription = externalDescription;
	}

	public String getExternalUrl() {
		return externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}

	public String getOpenapi() {
		return openapi;
	}

	public void setOpenapi(String openapi) {
		this.openapi = openapi;
	}
}
