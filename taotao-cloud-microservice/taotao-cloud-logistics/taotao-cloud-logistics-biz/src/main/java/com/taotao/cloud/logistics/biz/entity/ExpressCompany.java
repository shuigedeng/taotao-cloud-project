package com.taotao.cloud.logistics.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 物流公司表
 *
 * @author dengtao
 * @date 2020/11/13 09:46
 * @since v1.0
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_express_company")
@org.hibernate.annotations.Table(appliesTo = "tt_express_company", comment = "物流公司表")
public class ExpressCompany extends BaseEntity {

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
}
