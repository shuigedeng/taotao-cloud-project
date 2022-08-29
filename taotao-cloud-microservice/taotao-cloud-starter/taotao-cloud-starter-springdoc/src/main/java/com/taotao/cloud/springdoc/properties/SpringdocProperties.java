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
package com.taotao.cloud.springdoc.properties;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.common.PropertyUtils;
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
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * SpringdocProperties
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:11
 */
@RefreshScope
@ConfigurationProperties(SpringdocProperties.PREFIX)
public class SpringdocProperties {

	public static final String PREFIX = "taotao.cloud.springdoc";

	/**
	 * 是否开启springdoc
	 */
	private Boolean enabled = false;
	/**
	 * group default applicationName
	 */
	private String group = PropertyUtils.getProperty(CommonConstant.SPRING_APP_NAME_KEY);

	/**
	 * pathsToMatch default /**
	 */
	private String[] pathsToMatch = new String[]{"/**"};

	/**
	 * The Paths to exclude.
	 */
	private  String[] pathsToExclude = new String[]{"/actuator/**"};

	/**
	 * The Packages to scan.
	 */
	private  String[] packagesToScan = new String[]{"com.taotao.cloud.*.biz.api.controller"};

	/**
	 * The Packages to exclude.
	 */
	private  String[] packagesToExclude;

	/**
	 * version default taotaoCloudVersion
	 */
	private String version = PropertyUtils.getProperty("taotaoCloudVersion");
	/**
	 * SecuritySchemes
	 */
	private Map<String, SecurityScheme> securitySchemes = new HashMap<>();
	/**
	 * Headers
	 */
	private Map<String, Header> headers = new HashMap<>();
	/**
	 * Headers
	 */
	private List<Server> servers = new ArrayList<>();
	/**
	 * title
	 */
	private String title =
		PropertyUtils.getProperty(CommonConstant.SPRING_APP_NAME_KEY).toUpperCase() + " API";
	/**
	 * description
	 */
	private String description = "TAOTAO CLOUD 电商及大数据平台";
	/**
	 * contact
	 */
	private Contact contact;
	/**
	 * termsOfService
	 */
	private String termsOfService = "http://taotaocloud.com/terms/";
	/**
	 * license
	 */
	private License license;
	/**
	 * externalDescription
	 */
	private String externalDescription = "TaoTao Cloud Wiki Documentation";
	/**
	 * externalUrl
	 */
	private String externalUrl = "https://github.com/shuigedeng/taotao-cloud-project/wiki";
	/**
	 * openapi
	 */
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
		return securitySchemes;
	}

	public void setSecuritySchemes(
		Map<String, SecurityScheme> securitySchemes) {
		this.securitySchemes = securitySchemes;
	}

	public Map<String, Header> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Header> headers) {
		this.headers = headers;
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

	public String[] getPathsToExclude() {
		return pathsToExclude;
	}

	public void setPathsToExclude(String[] pathsToExclude) {
		this.pathsToExclude = pathsToExclude;
	}

	public String[] getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public String[] getPackagesToExclude() {
		return packagesToExclude;
	}

	public void setPackagesToExclude(String[] packagesToExclude) {
		this.packagesToExclude = packagesToExclude;
	}
}
