package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

/**
 * 公司表
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/16 13:43
 */
@Entity
@Table(name = "tt_sys_company")
@org.hibernate.annotations.Table(appliesTo = "tt_company", comment = "公司表")
public class SysCompany extends BaseEntity {

	private static final long serialVersionUID = -8206387430513096669L;

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

	@Override
	public String toString() {
		return "Company{" +
			"tenantId='" + tenantId + '\'' +
			", tenantSecret='" + tenantSecret + '\'' +
			", name='" + name + '\'' +
			", fullName='" + fullName + '\'' +
			", creditCode='" + creditCode + '\'' +
			", email='" + email + '\'' +
			", username='" + username + '\'' +
			", phone='" + phone + '\'' +
			", address='" + address + '\'' +
			", domain='" + domain + '\'' +
			", webSite='" + webSite + '\'' +
			", regionInfo='" + regionInfo + '\'' +
			", type=" + type +
			"} " + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysCompany sysCompany = (SysCompany) o;
		return type == sysCompany.type && Objects.equals(tenantId, sysCompany.tenantId)
			&& Objects.equals(tenantSecret, sysCompany.tenantSecret)
			&& Objects.equals(name, sysCompany.name) && Objects.equals(fullName,
			sysCompany.fullName) && Objects.equals(creditCode, sysCompany.creditCode)
			&& Objects.equals(email, sysCompany.email) && Objects.equals(username,
			sysCompany.username) && Objects.equals(phone, sysCompany.phone)
			&& Objects.equals(address, sysCompany.address) && Objects.equals(
			domain, sysCompany.domain) && Objects.equals(webSite, sysCompany.webSite)
			&& Objects.equals(regionInfo, sysCompany.regionInfo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tenantId, tenantSecret, name, fullName, creditCode, email, username,
			phone,
			address, domain, webSite, regionInfo, type);
	}

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

	public SysCompany(Long id, Long createBy, Long lastModifiedBy, LocalDateTime createTime,
		LocalDateTime lastModifiedTime, int version, Boolean delFlag,
		String tenantId, String tenantSecret, String name, String fullName,
		String creditCode, String email, String username, String phone, String address,
		String domain, String webSite, String regionInfo, byte type) {
		super(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version, delFlag);
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

	public static SysCompanyBuilder builder() {
		return new SysCompanyBuilder();
	}

	public static final class SysCompanyBuilder {

		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;
		private String tenantId;
		private String tenantSecret;
		private String name;
		private String fullName;
		private String creditCode;
		private String email;
		private String username;
		private String phone;
		private String address;
		private String domain;
		private String webSite;
		private String regionInfo;
		private byte type = 1;

		private SysCompanyBuilder() {
		}

		public static SysCompanyBuilder aSysCompany() {
			return new SysCompanyBuilder();
		}

		public SysCompanyBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysCompanyBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public SysCompanyBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public SysCompanyBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public SysCompanyBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public SysCompanyBuilder version(int version) {
			this.version = version;
			return this;
		}

		public SysCompanyBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public SysCompanyBuilder tenantId(String tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public SysCompanyBuilder tenantSecret(String tenantSecret) {
			this.tenantSecret = tenantSecret;
			return this;
		}

		public SysCompanyBuilder name(String name) {
			this.name = name;
			return this;
		}

		public SysCompanyBuilder fullName(String fullName) {
			this.fullName = fullName;
			return this;
		}

		public SysCompanyBuilder creditCode(String creditCode) {
			this.creditCode = creditCode;
			return this;
		}

		public SysCompanyBuilder email(String email) {
			this.email = email;
			return this;
		}

		public SysCompanyBuilder username(String username) {
			this.username = username;
			return this;
		}

		public SysCompanyBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public SysCompanyBuilder address(String address) {
			this.address = address;
			return this;
		}

		public SysCompanyBuilder domain(String domain) {
			this.domain = domain;
			return this;
		}

		public SysCompanyBuilder webSite(String webSite) {
			this.webSite = webSite;
			return this;
		}

		public SysCompanyBuilder regionInfo(String regionInfo) {
			this.regionInfo = regionInfo;
			return this;
		}

		public SysCompanyBuilder type(byte type) {
			this.type = type;
			return this;
		}

		public SysCompany build() {
			SysCompany sysCompany = new SysCompany();
			sysCompany.setId(id);
			sysCompany.setCreateBy(createBy);
			sysCompany.setLastModifiedBy(lastModifiedBy);
			sysCompany.setCreateTime(createTime);
			sysCompany.setLastModifiedTime(lastModifiedTime);
			sysCompany.setVersion(version);
			sysCompany.setDelFlag(delFlag);
			sysCompany.setTenantId(tenantId);
			sysCompany.setTenantSecret(tenantSecret);
			sysCompany.setName(name);
			sysCompany.setFullName(fullName);
			sysCompany.setCreditCode(creditCode);
			sysCompany.setEmail(email);
			sysCompany.setUsername(username);
			sysCompany.setPhone(phone);
			sysCompany.setAddress(address);
			sysCompany.setDomain(domain);
			sysCompany.setWebSite(webSite);
			sysCompany.setRegionInfo(regionInfo);
			sysCompany.setType(type);
			return sysCompany;
		}
	}
}
