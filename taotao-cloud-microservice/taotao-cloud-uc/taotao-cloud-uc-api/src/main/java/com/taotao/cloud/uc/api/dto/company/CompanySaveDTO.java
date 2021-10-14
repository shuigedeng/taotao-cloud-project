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
package com.taotao.cloud.uc.api.dto.company;


import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.Pattern;

/**
 * 公司新增对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:31:52
 */
@Schema(name = "CompanySaveDTO", description = "公司新增对象")
public class CompanySaveDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 5126530068827085130L;

	/**
	 * 租户id
	 */
	@Schema(description = "租户id")
	private String tenantId;

	/**
	 * 租户密钥
	 */
	@Schema(description = "租户密钥")
	private String tenantSecret;

	/**
	 * 公司名称
	 */
	@Schema(description = "公司名称")
	private String name;

	/**
	 * 企业全称
	 */
	@Schema(description = "企业全称")
	private String fullName;

	/**
	 * 信用代码
	 */
	@Pattern(regexp = "^|[a-zA-Z0-9]{18}$", message = "信用代码格式错误")
	@Schema(description = "信用代码")
	private String creditCode;

	/**
	 * 邮箱
	 */
	@Schema(description = "邮箱")
	private String email;

	/**
	 * 联系人
	 */
	@Schema(description = "联系人")
	private String username;

	/**
	 * 联系人手机号
	 */
	@Schema(description = "联系人手机号")
	private String phone;

	/**
	 * 联系人地址
	 */
	@Schema(description = "联系人地址")
	private String address;

	/**
	 * 请求域名
	 */
	@Schema(description = "请求域名")
	private String domain;

	/**
	 * 公司网址
	 */
	@Schema(description = "公司网址")
	private String webSite;

	/**
	 * 所在地区
	 */
	@Schema(description = "所在地区")
	private String regionInfo;

	/**
	 * 公司类型
	 */
	@Schema(description = "公司类型")
	private byte type = 1;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantSecret() {
		return tenantSecret;
	}

	public void setTenantSecret(String tenantSecret) {
		this.tenantSecret = tenantSecret;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getRegionInfo() {
		return regionInfo;
	}

	public void setRegionInfo(String regionInfo) {
		this.regionInfo = regionInfo;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}
}
