package com.taotao.cloud.sys.biz.entity.config;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 物流公司设置
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = LogisticsConfig.TABLE_NAME)
@TableName(LogisticsConfig.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = LogisticsConfig.TABLE_NAME, comment = "物流公司表")
public class LogisticsConfig extends BaseSuperEntity<LogisticsConfig, Long> {

	public static final String TABLE_NAME = "tt_logistics_config";

	/**
	 * 物流公司名称
	 */
	@Column(name = "name", columnDefinition = "varchar(255) not null COMMENT '物流公司名称'")
	private String name;

	/**
	 * 物流公司code
	 */
	@Column(name = "code", columnDefinition = "varchar(255) not null COMMENT '物流公司code'")
	private String code;

	/**
	 * 物流公司联系人
	 */
	@Column(name = "contact_name", columnDefinition = "varchar(32) not null COMMENT '物流公司联系人'")
	private String contactName;

	/**
	 * 物流公司联系电话
	 */
	@Column(name = "contact_mobile", columnDefinition = "varchar(32) not null COMMENT '物流公司联系电话'")
	private String contactMobile;

	/**
	 * 支持电子面单
	 */
	@Column(name = "stand_by", columnDefinition = "varchar(255) not null COMMENT '支持电子面单'")
	private String standBy;

	/**
	 * 物流公司电子面单表单
	 */
	@Column(name = "form_items", columnDefinition = "varchar(255) not null COMMENT '物流公司电子面单表单'")
	private String formItems;

	/**
	 * 禁用状态 OPEN：开启，CLOSE：禁用
	 */
	@Column(name = "disabled", columnDefinition = "varchar(12) not null COMMENT '禁用状态 OPEN：开启，CLOSE：禁用'")
	private String disabled;

}
