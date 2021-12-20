package com.taotao.cloud.store.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 运费模板子配置
 *
 * @since 2020/11/17 4:27 下午
 */
@Entity
@Table(name = FreightTemplateChild.TABLE_NAME)
@TableName(FreightTemplateChild.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = FreightTemplateChild.TABLE_NAME, comment = "运费模板子配置表")
public class FreightTemplateChild extends BaseSuperEntity<FreightTemplateChild, Long> {

	public static final String TABLE_NAME = "li_freight_template_child";

	@Column(name = "freight_template_id", nullable = false, columnDefinition = "varchar(32) not null comment '店铺模板ID'")
	private String freightTemplateId;

	@Column(name = "first_company", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '首重/首件'")
	private BigDecimal firstCompany;

	@Column(name = "first_price", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '运费'")
	private BigDecimal firstPrice;

	@Column(name = "continued_company", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '续重/续件'")
	private BigDecimal continuedCompany;

	@Column(name = "continued_price", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '续费'")
	private BigDecimal continuedPrice;

	@Column(name = "area", nullable = false, columnDefinition = "varchar(32) not null comment '地址，示例参数：上海,江苏,浙江'")
	private String area;

	@Column(name = "area_id", nullable = false, columnDefinition = "varchar(32) not null comment '地区ID，示例参数：1,2,3,4'")
	private String areaId;

	public String getFreightTemplateId() {
		return freightTemplateId;
	}

	public void setFreightTemplateId(String freightTemplateId) {
		this.freightTemplateId = freightTemplateId;
	}

	public BigDecimal getFirstCompany() {
		return firstCompany;
	}

	public void setFirstCompany(BigDecimal firstCompany) {
		this.firstCompany = firstCompany;
	}

	public BigDecimal getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}

	public BigDecimal getContinuedCompany() {
		return continuedCompany;
	}

	public void setContinuedCompany(BigDecimal continuedCompany) {
		this.continuedCompany = continuedCompany;
	}

	public BigDecimal getContinuedPrice() {
		return continuedPrice;
	}

	public void setContinuedPrice(BigDecimal continuedPrice) {
		this.continuedPrice = continuedPrice;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
}
