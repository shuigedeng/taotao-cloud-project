package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员收货地址
 *
 * @since 2020-02-25 14:10:16
 */
@Entity
@Table(name = MemberAddress.TABLE_NAME)
@TableName(MemberAddress.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberAddress.TABLE_NAME, comment = "会员收货地址表")
public class MemberAddress extends BaseSuperEntity<MemberAddress, Long> {

	public static final String TABLE_NAME = "li_member_address";

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员ID'")
	private String memberId;

	@Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '收货人姓名'")
	private String name;

	//@Phone
	//@Sensitive(strategy = SensitiveStrategy.PHONE)
	@Column(name = "mobile", nullable = false, columnDefinition = "varchar(32) not null comment '手机号码'")
	private String mobile;

	@Column(name = "consignee_address_path", nullable = false, columnDefinition = "varchar(32) not null comment '地址名称， 逗号分割'")
	private String consigneeAddressPath;

	@Column(name = "consignee_address_id_path", nullable = false, columnDefinition = "varchar(32) not null comment '地址id， 逗号分割'")
	private String consigneeAddressIdPath;

	@Column(name = "detail", nullable = false, columnDefinition = "varchar(32) not null comment '详细地址'")
	private String detail;

	@Column(name = "city", nullable = false, columnDefinition = "varchar(32) not null COMMENT '市'")
	private String city;

	@Column(name = "area", nullable = false, columnDefinition = "varchar(32) not null COMMENT '区县'")
	private String area;

	@Column(name = "province_code", nullable = false, columnDefinition = "varchar(32) not null COMMENT '省code'")
	private String provinceCode;

	@Column(name = "city_code", nullable = false, columnDefinition = "varchar(32) not null COMMENT '市code'")
	private String cityCode;

	@Column(name = "area_code", nullable = false, columnDefinition = "varchar(32) not null COMMENT '区、县code'")
	private String areaCode;

	@Column(name = "address", nullable = false, columnDefinition = "varchar(255) not null COMMENT '街道地址'")
	private String address;

	@Column(name = "is_default", nullable = false, columnDefinition = "boolean not null default false comment '是否为默认收货地址'")
	private Boolean isDefault;

	@Column(name = "alias", nullable = false, columnDefinition = "varchar(32) not null comment '地址别名'")
	private String alias;

	@Column(name = "lon", nullable = false, columnDefinition = "varchar(32) not null comment '经度'")
	private String lon;

	@Column(name = "lat", nullable = false, columnDefinition = "varchar(32) not null comment '纬度'")
	private String lat;

	@Column(name = "postal_code", columnDefinition = "int(11) comment '邮政编码'")
	private Long postalCode;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getConsigneeAddressPath() {
		return consigneeAddressPath;
	}

	public void setConsigneeAddressPath(String consigneeAddressPath) {
		this.consigneeAddressPath = consigneeAddressPath;
	}

	public String getConsigneeAddressIdPath() {
		return consigneeAddressIdPath;
	}

	public void setConsigneeAddressIdPath(String consigneeAddressIdPath) {
		this.consigneeAddressIdPath = consigneeAddressIdPath;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getDefault() {
		return isDefault;
	}

	public void setDefault(Boolean aDefault) {
		isDefault = aDefault;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public Long getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Long postalCode) {
		this.postalCode = postalCode;
	}
}
