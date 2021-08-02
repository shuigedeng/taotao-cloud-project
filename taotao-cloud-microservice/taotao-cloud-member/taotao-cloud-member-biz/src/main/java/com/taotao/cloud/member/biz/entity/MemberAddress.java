package com.taotao.cloud.member.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员收货地址
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Entity
@Table(name = "tt_member_address")
@org.hibernate.annotations.Table(appliesTo = "tt_member_address", comment = "会员收货地址")
public class MemberAddress extends BaseEntity {

	/**
	 * 会员id
	 *
	 * @see Member
	 */
	@Column(name = "member_user_id", nullable = false, columnDefinition = "int(11) not null comment '会员id'")
	private Long memberUserId;

	/**
	 * 收货人姓名
	 */
	@Column(name = "receiver_name", nullable = false, columnDefinition = "varchar(32) not null comment '收货人姓名'")
	private String receiverName;

	/**
	 * 手机号
	 */
	@Column(name = "phone", unique = true, nullable = false, columnDefinition = "varchar(14) not null comment '手机号'")
	private String phone;

	/**
	 * 省份
	 */
	@Column(name = "province", nullable = false, columnDefinition = "varchar(32) not null COMMENT '省'")
	private String province;

	/**
	 * 市
	 */
	@Column(name = "city", nullable = false, columnDefinition = "varchar(32) not null COMMENT '市'")
	private String city;

	/**
	 * 区县
	 */
	@Column(name = "area", nullable = false, columnDefinition = "varchar(32) not null COMMENT '区县'")
	private String area;

	/**
	 * 省code
	 */
	@Column(name = "province_code", nullable = false, columnDefinition = "varchar(32) not null COMMENT '省code'")
	private String provinceCode;

	/**
	 * 市code
	 */
	@Column(name = "city_code", nullable = false, columnDefinition = "varchar(32) not null COMMENT '市code'")
	private String cityCode;

	/**
	 * 区、县code
	 */
	@Column(name = "area_code", nullable = false, columnDefinition = "varchar(32) not null COMMENT '区、县code'")
	private String areaCode;

	/**
	 * 街道地址
	 */
	@Column(name = "address", nullable = false, columnDefinition = "varchar(255) not null COMMENT '街道地址'")
	private String address;

	/**
	 * 邮政编码
	 */
	@Column(name = "postal_code", columnDefinition = "int(11) comment '邮政编码'")
	private Long postalCode;

	/**
	 * 默认收货地址 1=>默认 0非默认
	 */
	@Column(name = "is_default", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 COMMENT '默认收货地址'")
	private Boolean isDefault = false;

	@Override
	public String toString() {
		return "MemberAddress{" +
			"memberUserId=" + memberUserId +
			", receiverName='" + receiverName + '\'' +
			", phone='" + phone + '\'' +
			", province='" + province + '\'' +
			", city='" + city + '\'' +
			", area='" + area + '\'' +
			", provinceCode='" + provinceCode + '\'' +
			", cityCode='" + cityCode + '\'' +
			", areaCode='" + areaCode + '\'' +
			", address='" + address + '\'' +
			", postalCode=" + postalCode +
			", isDefault=" + isDefault +
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
		if (!super.equals(o)) {
			return false;
		}
		MemberAddress that = (MemberAddress) o;
		return Objects.equals(memberUserId, that.memberUserId) && Objects.equals(
			receiverName, that.receiverName) && Objects.equals(phone, that.phone)
			&& Objects.equals(province, that.province) && Objects.equals(city,
			that.city) && Objects.equals(area, that.area) && Objects.equals(
			provinceCode, that.provinceCode) && Objects.equals(cityCode, that.cityCode)
			&& Objects.equals(areaCode, that.areaCode) && Objects.equals(address,
			that.address) && Objects.equals(postalCode, that.postalCode)
			&& Objects.equals(isDefault, that.isDefault);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), memberUserId, receiverName, phone, province, city,
			area,
			provinceCode, cityCode, areaCode, address, postalCode, isDefault);
	}

	public Long getMemberUserId() {
		return memberUserId;
	}

	public void setMemberUserId(Long memberUserId) {
		this.memberUserId = memberUserId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
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

	public Long getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Long postalCode) {
		this.postalCode = postalCode;
	}

	public Boolean getDefault() {
		return isDefault;
	}

	public void setDefault(Boolean aDefault) {
		isDefault = aDefault;
	}

	public MemberAddress() {
	}

	public MemberAddress(Long memberUserId, String receiverName, String phone,
		String province, String city, String area, String provinceCode, String cityCode,
		String areaCode, String address, Long postalCode, Boolean isDefault) {
		this.memberUserId = memberUserId;
		this.receiverName = receiverName;
		this.phone = phone;
		this.province = province;
		this.city = city;
		this.area = area;
		this.provinceCode = provinceCode;
		this.cityCode = cityCode;
		this.areaCode = areaCode;
		this.address = address;
		this.postalCode = postalCode;
		this.isDefault = isDefault;
	}

	public MemberAddress(Long id, Long createBy, Long lastModifiedBy,
		LocalDateTime createTime, LocalDateTime lastModifiedTime, int version,
		Boolean delFlag, Long memberUserId, String receiverName, String phone,
		String province, String city, String area, String provinceCode, String cityCode,
		String areaCode, String address, Long postalCode, Boolean isDefault) {
		super(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version, delFlag);
		this.memberUserId = memberUserId;
		this.receiverName = receiverName;
		this.phone = phone;
		this.province = province;
		this.city = city;
		this.area = area;
		this.provinceCode = provinceCode;
		this.cityCode = cityCode;
		this.areaCode = areaCode;
		this.address = address;
		this.postalCode = postalCode;
		this.isDefault = isDefault;
	}

	public static MemberAddressBuilder builder() {
		return new MemberAddressBuilder();
	}

	public static final class MemberAddressBuilder {

		private Long memberUserId;
		private String receiverName;
		private String phone;
		private String province;
		private String city;
		private String area;
		private String provinceCode;
		private String cityCode;
		private String areaCode;
		private String address;
		private Long postalCode;
		private Boolean isDefault = false;
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private MemberAddressBuilder() {
		}

		public static MemberAddressBuilder aMemberAddress() {
			return new MemberAddressBuilder();
		}

		public MemberAddressBuilder memberUserId(Long memberUserId) {
			this.memberUserId = memberUserId;
			return this;
		}

		public MemberAddressBuilder receiverName(String receiverName) {
			this.receiverName = receiverName;
			return this;
		}

		public MemberAddressBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public MemberAddressBuilder province(String province) {
			this.province = province;
			return this;
		}

		public MemberAddressBuilder city(String city) {
			this.city = city;
			return this;
		}

		public MemberAddressBuilder area(String area) {
			this.area = area;
			return this;
		}

		public MemberAddressBuilder provinceCode(String provinceCode) {
			this.provinceCode = provinceCode;
			return this;
		}

		public MemberAddressBuilder cityCode(String cityCode) {
			this.cityCode = cityCode;
			return this;
		}

		public MemberAddressBuilder areaCode(String areaCode) {
			this.areaCode = areaCode;
			return this;
		}

		public MemberAddressBuilder address(String address) {
			this.address = address;
			return this;
		}

		public MemberAddressBuilder postalCode(Long postalCode) {
			this.postalCode = postalCode;
			return this;
		}

		public MemberAddressBuilder isDefault(Boolean isDefault) {
			this.isDefault = isDefault;
			return this;
		}

		public MemberAddressBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public MemberAddressBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public MemberAddressBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public MemberAddressBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public MemberAddressBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public MemberAddressBuilder version(int version) {
			this.version = version;
			return this;
		}

		public MemberAddressBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public MemberAddress build() {
			MemberAddress memberAddress = new MemberAddress(memberUserId, receiverName, phone,
				province, city, area, provinceCode, cityCode, areaCode, address, postalCode,
				isDefault);
			memberAddress.setId(id);
			memberAddress.setCreateBy(createBy);
			memberAddress.setLastModifiedBy(lastModifiedBy);
			memberAddress.setCreateTime(createTime);
			memberAddress.setLastModifiedTime(lastModifiedTime);
			memberAddress.setVersion(version);
			memberAddress.setDelFlag(delFlag);
			return memberAddress;
		}
	}
}
