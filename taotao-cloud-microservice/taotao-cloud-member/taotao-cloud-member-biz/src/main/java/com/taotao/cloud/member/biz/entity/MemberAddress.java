package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员收货地址表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 14:55:28
 */
@Entity
@Table(name = MemberAddress.TABLE_NAME)
@TableName(MemberAddress.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberAddress.TABLE_NAME, comment = "会员收货地址表")
public class MemberAddress extends BaseSuperEntity<MemberAddress, Long> {

	public static final String TABLE_NAME = "tt_member_address";

	/**
	 * 会员ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String memberId;

	/**
	 * 收货人姓名
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(64) not null comment '收货人姓名'")
	private String name;

	/**
	 * 手机号码
	 */
	@Column(name = "mobile", nullable = false, columnDefinition = "varchar(64) not null comment '手机号码'")
	private String mobile;

	/**
	 * 地址名称，逗号分割
	 */
	@Column(name = "consignee_address_path", nullable = false, columnDefinition = "varchar(32) not null comment '地址名称，逗号分割'")
	private String consigneeAddressPath;

	/**
	 * 地址id,逗号分割
	 */
	@Column(name = "consignee_address_id_path", nullable = false, columnDefinition = "varchar(32) not null comment '地址id,逗号分割'")
	private String consigneeAddressIdPath;

	/**
	 * 省
	 */
	@Column(name = "province", nullable = false, columnDefinition = "varchar(64) not null COMMENT '省'")
	private String province;

	/**
	 * 市
	 */
	@Column(name = "city", nullable = false, columnDefinition = "varchar(64) not null COMMENT '市'")
	private String city;

	/**
	 * 区县
	 */
	@Column(name = "area", nullable = false, columnDefinition = "varchar(64) not null COMMENT '区县'")
	private String area;

	/**
	 * 省code
	 */
	@Column(name = "province_code", nullable = false, columnDefinition = "varchar(64) not null COMMENT '省code'")
	private String provinceCode;

	/**
	 * 市code
	 */
	@Column(name = "city_code", nullable = false, columnDefinition = "varchar(64) not null COMMENT '市code'")
	private String cityCode;

	/**
	 * 区县code
	 */
	@Column(name = "area_code", nullable = false, columnDefinition = "varchar(64) not null COMMENT '区县code'")
	private String areaCode;

	/**
	 * 街道地址
	 */
	@Column(name = "address", nullable = false, columnDefinition = "varchar(255) not null COMMENT '街道地址'")
	private String address;

	/**
	 * 详细地址
	 */
	@Column(name = "detail", nullable = false, columnDefinition = "varchar(255) not null comment '详细地址'")
	private String detail;

	/**
	 * 是否为默认收货地址
	 */
	@Column(name = "defaulted", nullable = false, columnDefinition = "boolean not null default true comment '是否为默认收货地址'")
	private Boolean defaulted;

	/**
	 * 地址别名
	 */
	@Column(name = "alias", columnDefinition = "varchar(64) comment '地址别名'")
	private String alias;

	/**
	 * 经度
	 */
	@Column(name = "lon", columnDefinition = "varchar(32) comment '经度'")
	private String lon;

	/**
	 * 纬度
	 */
	@Column(name = "lat", columnDefinition = "varchar(32) comment '纬度'")
	private String lat;

	/**
	 * 邮政编码
	 */
	@Column(name = "postal_code", columnDefinition = "varchar(64) comment '邮政编码'")
	private String postalCode;

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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Boolean getDefaulted() {
		return defaulted;
	}

	public void setDefaulted(Boolean defaulted) {
		this.defaulted = defaulted;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}
