package com.taotao.cloud.store.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.store.api.enums.FreightTemplateEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 运费模板
 *
 * @since 2020/11/17 4:27 下午
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = FreightTemplate.TABLE_NAME)
@TableName(FreightTemplate.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = FreightTemplate.TABLE_NAME, comment = "运费模板表")
public class FreightTemplate extends BaseSuperEntity<FreightTemplate, Long> {

	public static final String TABLE_NAME = "li_freight_template";

	@Column(name = "store_id", columnDefinition = "bigint not null comment '店铺ID'")
	private Long storeId;

	@Column(name = "name", columnDefinition = "varchar(32) not null comment '模板名称'")
	private String name;

	/**
	 * @see FreightTemplateEnum
	 */
	@Column(name = "pricing_method", columnDefinition = "varchar(32) not null comment '计价方式：按件、按重量 WEIGHT,NUM,FREE'")
	private String pricingMethod;
}
