package com.taotao.cloud.store.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * 运费模板子配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-12 21:24:28
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = FreightTemplateChild.TABLE_NAME)
@TableName(FreightTemplateChild.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = FreightTemplateChild.TABLE_NAME, comment = "运费模板子配置表")
public class FreightTemplateChild extends BaseSuperEntity<FreightTemplateChild, Long> {

	public static final String TABLE_NAME = "tt_freight_template_child";

	@Column(name = "freight_template_id", columnDefinition = "bigint not null comment '店铺模板ID'")
	private Long freightTemplateId;

	@Column(name = "first_company", columnDefinition = "decimal(10,2) not null default 0 comment '首重/首件'")
	private BigDecimal firstCompany;

	@Column(name = "first_price", columnDefinition = "decimal(10,2) not null default 0 comment '运费'")
	private BigDecimal firstPrice;

	@Column(name = "continued_company", columnDefinition = "decimal(10,2) not null default 0 comment '续重/续件'")
	private BigDecimal continuedCompany;

	@Column(name = "continued_price", columnDefinition = "decimal(10,2) not null default 0 comment '续费'")
	private BigDecimal continuedPrice;

	@Column(name = "area", columnDefinition = "varchar(32) not null comment '地址，示例参数：上海,江苏,浙江'")
	private String area;

	@Column(name = "area_id", columnDefinition = "varchar(32) not null comment '地区ID，示例参数：1,2,3,4'")
	private String areaId;
}
