package com.taotao.cloud.system.biz.entity;

import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 物流公司表
 *
 * @author shuigedeng
 * @since 2020/11/13 09:46
 * @version 1.0.0
 */
@Entity
@Table(name = "tt_express_company")
@org.hibernate.annotations.Table(appliesTo = "tt_express_company", comment = "物流公司表")
public class ExpressCompany extends JpaSuperEntity {

	private static final long serialVersionUID = 6887296988458221221L;

	/**
	 * 物流公司名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null COMMENT '物流公司名称'")
	private String name;

	/**
	 * 物流公司编码
	 */
	@Column(name = "code", nullable = false, columnDefinition = "varchar(32) not null COMMENT '物流公司编码'")
	private String code;

	/**
	 * 物流公司联系人
	 */
	@Column(name = "contact_name", nullable = false, columnDefinition = "varchar(32) not null COMMENT '物流公司联系人'")
	private String contactName;

	/**
	 * 物流公司联系电话
	 */
	@Column(name = "contact_mobile", nullable = false, columnDefinition = "varchar(32) not null COMMENT '物流公司联系电话'")
	private String contactMobile;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
}
