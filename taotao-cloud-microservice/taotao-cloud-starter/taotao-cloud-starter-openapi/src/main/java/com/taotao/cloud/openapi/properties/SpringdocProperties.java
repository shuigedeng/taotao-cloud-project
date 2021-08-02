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

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
@ConfigurationProperties("taotao.cloud.springdoc")
public class SpringdocProperties {

	/**
	 * 是否开启springdoc
	 **/
	private Boolean enabled;

	/**
	 * 标题
	 **/
	private String title = "";

	/**
	 * 描述
	 **/
	private String description = "";

	/**
	 * 版本
	 **/
	private String version = "";

	/**
	 * 许可证
	 **/
	private String license = "";

	/**
	 * 许可证URL
	 **/
	private String licenseUrl = "";

	/**
	 * 服务条款URL
	 **/
	private String termsOfServiceUrl = "";

	/**
	 * 联系人
	 **/
	private Contact contact = new Contact();

	/**
	 * swagger会解析的包路径
	 **/
	private String basePackage = "";

	/**
	 * swagger会解析的url规则
	 **/
	private List<String> basePath = new ArrayList<>();

	/**
	 * 在basePath基础上需要排除的url规则
	 **/
	private List<String> excludePath = new ArrayList<>();

	/**
	 * 分组文档
	 **/
	private Map<String, DocketInfo> docket = new LinkedHashMap<>();

	/**
	 * host信息
	 **/
	private String host = "";

	/**
	 * 全局参数配置
	 **/
	private List<GlobalOperationParameter> globalOperationParameters;

	public static class GlobalOperationParameter {

		/**
		 * 参数名
		 **/
		private String name;

		/**
		 * 描述信息
		 **/
		private String description;

		/**
		 * 指定参数类型
		 **/
		private String modelRef;

		/**
		 * 参数放在哪个地方:header,query,path,body.form
		 **/
		private String parameterType;

		/**
		 * 参数是否必须传
		 **/
		private String required;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getModelRef() {
			return modelRef;
		}

		public void setModelRef(String modelRef) {
			this.modelRef = modelRef;
		}

		public String getParameterType() {
			return parameterType;
		}

		public void setParameterType(String parameterType) {
			this.parameterType = parameterType;
		}

		public String getRequired() {
			return required;
		}

		public void setRequired(String required) {
			this.required = required;
		}
	}

	public static class DocketInfo {

		/**
		 * 标题
		 **/
		private String title = "";

		/**
		 * 描述
		 **/
		private String description = "";

		/**
		 * 版本
		 **/
		private String version = "";

		/**
		 * 许可证
		 **/
		private String license = "";

		/**
		 * 许可证URL
		 **/
		private String licenseUrl = "";

		/**
		 * 服务条款URL
		 **/
		private String termsOfServiceUrl = "";

		private Contact contact = new Contact();

		/**
		 * swagger会解析的包路径
		 **/
		private String basePackage = "";

		/**
		 * swagger会解析的url规则
		 **/
		private List<String> basePath = new ArrayList<>();
		/**
		 * 在basePath基础上需要排除的url规则
		 **/
		private List<String> excludePath = new ArrayList<>();

		private List<GlobalOperationParameter> globalOperationParameters;

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

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getLicense() {
			return license;
		}

		public void setLicense(String license) {
			this.license = license;
		}

		public String getLicenseUrl() {
			return licenseUrl;
		}

		public void setLicenseUrl(String licenseUrl) {
			this.licenseUrl = licenseUrl;
		}

		public String getTermsOfServiceUrl() {
			return termsOfServiceUrl;
		}

		public void setTermsOfServiceUrl(String termsOfServiceUrl) {
			this.termsOfServiceUrl = termsOfServiceUrl;
		}

		public Contact getContact() {
			return contact;
		}

		public void setContact(Contact contact) {
			this.contact = contact;
		}

		public String getBasePackage() {
			return basePackage;
		}

		public void setBasePackage(String basePackage) {
			this.basePackage = basePackage;
		}

		public List<String> getBasePath() {
			return basePath;
		}

		public void setBasePath(List<String> basePath) {
			this.basePath = basePath;
		}

		public List<String> getExcludePath() {
			return excludePath;
		}

		public void setExcludePath(List<String> excludePath) {
			this.excludePath = excludePath;
		}

		public List<GlobalOperationParameter> getGlobalOperationParameters() {
			return globalOperationParameters;
		}

		public void setGlobalOperationParameters(
			List<GlobalOperationParameter> globalOperationParameters) {
			this.globalOperationParameters = globalOperationParameters;
		}
	}

	public static class Contact {

		/**
		 * 联系人
		 **/
		private String name = "";
		/**
		 * 联系人url
		 **/
		private String url = "";
		/**
		 * 联系人email
		 **/
		private String email = "";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}
}
