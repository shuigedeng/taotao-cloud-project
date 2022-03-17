package com.taotao.cloud.sys.biz.entity.config;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 物流公司设置
 */
@Entity
@Table(name = LogisticsConfig.TABLE_NAME)
@TableName(LogisticsConfig.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = LogisticsConfig.TABLE_NAME, comment = "物流公司表")
public class LogisticsConfig extends BaseSuperEntity<LogisticsConfig, Long> {

	public static final String TABLE_NAME = "tt_logistics_config";


	@Column(name = "name", nullable = false, columnDefinition = "varchar(255) not null COMMENT '物流公司名称'")
	private String name;

	@Column(name = "code", nullable = false, columnDefinition = "varchar(255) not null COMMENT '物流公司code'")
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


	@Column(name = "stand_by", nullable = false, columnDefinition = "varchar(255) not null COMMENT '支持电子面单'")
	private String standBy;

	@Column(name = "form_items", nullable = false, columnDefinition = "varchar(255) not null COMMENT '物流公司电子面单表单'")
	private String formItems;

	@Column(name = "disabled", nullable = false, columnDefinition = "varchar(12) not null COMMENT '禁用状态 OPEN：开启，CLOSE：禁用'")
	private String disabled;

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

	public String getStandBy() {
		return standBy;
	}

	public void setStandBy(String standBy) {
		this.standBy = standBy;
	}

	public String getFormItems() {
		return formItems;
	}

	public void setFormItems(String formItems) {
		this.formItems = formItems;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
}
