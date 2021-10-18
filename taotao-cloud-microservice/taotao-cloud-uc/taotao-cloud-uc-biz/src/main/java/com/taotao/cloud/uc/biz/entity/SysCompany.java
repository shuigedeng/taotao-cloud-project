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
package com.taotao.cloud.uc.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

/**
 * 公司表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:11:38
 */
@Entity
@TableName(SysCompany.TABLE_NAME)
@Table(name = SysCompany.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SysCompany.TABLE_NAME, comment = "公司表")
public class SysCompany extends BaseSuperEntity<Long> {

	public static final String TABLE_NAME = "uc_sys_company";

	/**
	 * 租户id
	 */
	@Column(name = "tenant_id", nullable = false, unique = true, columnDefinition = "varchar(32) NOT NULL COMMENT '租户id'")
	private String tenantId;

	/**
	 * 租户密钥
	 */
	@Column(name = "tenant_secret", nullable = false, columnDefinition = "varchar(256)  NOT NULL COMMENT '租户密钥'")
	private String tenantSecret;

	/**
	 * 公司名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '公司名称'")
	private String name;

	/**
	 * 企业全称
	 */
	@Column(name = "full_name", nullable = false, columnDefinition = "varchar(64) not null comment '企业全称'")
	private String fullName;

	/**
	 * 信用代码
	 */
	@Pattern(regexp = "^|[a-zA-Z0-9]{18}$", message = "信用代码格式错误")
	@Column(name = "credit_code", nullable = false, columnDefinition = "varchar(18) not null comment '信用代码'")
	private String creditCode;

	/**
	 * 邮箱
	 */
	@Column(name = "email", unique = true, columnDefinition = "varchar(30) comment '邮箱'")
	private String email;

	/**
	 * 联系人
	 */
	@Column(name = "username", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '联系人'")
	private String username;

	/**
	 * 联系人手机号
	 */
	@Column(name = "phone", unique = true, nullable = false, columnDefinition = "varchar(14) not null comment '联系人手机号'")
	private String phone;

	/**
	 * 联系人地址
	 */
	@Column(name = "address", nullable = false, columnDefinition = "varchar(32) not null comment '联系人地址'")
	private String address;

	/**
	 * 请求域名
	 */
	@Column(name = "domain", columnDefinition = "varchar(32) comment '请求域名'")
	private String domain;

	/**
	 * 公司网址
	 */
	@Column(name = "webSite", columnDefinition = "varchar(32) comment '公司网址'")
	private String webSite;

	/**
	 * 所在地区
	 */
	@Column(name = "region_info", columnDefinition = "varchar(32) comment '所在地区'")
	private String regionInfo;

	/**
	 * 公司类型
	 */
	@Column(name = "type", nullable = false, columnDefinition = "tinyint(1) unsigned not null default 1 comment '公司类型 1运营商 2供应商'")
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

	public SysCompany() {
	}

	public SysCompany(String tenantId, String tenantSecret, String name, String fullName,
		String creditCode, String email, String username, String phone, String address,
		String domain, String webSite, String regionInfo, byte type) {
		this.tenantId = tenantId;
		this.tenantSecret = tenantSecret;
		this.name = name;
		this.fullName = fullName;
		this.creditCode = creditCode;
		this.email = email;
		this.username = username;
		this.phone = phone;
		this.address = address;
		this.domain = domain;
		this.webSite = webSite;
		this.regionInfo = regionInfo;
		this.type = type;
	}

}
