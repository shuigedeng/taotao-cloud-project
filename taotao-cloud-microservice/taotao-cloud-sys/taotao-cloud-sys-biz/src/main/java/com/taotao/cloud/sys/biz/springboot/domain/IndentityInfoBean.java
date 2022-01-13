package com.taotao.cloud.sys.biz.springboot.domain;

import java.io.Serializable;
/**
 * 
 * @author duhongming
 *
 */
public class IndentityInfoBean implements Serializable{

	private static final long serialVersionUID = 3414822841504262315L;
		/**
		 *  身份证真伪
		 */
		private boolean verify;
		/**
		 *  出生日期
		 */
		private String birthDate;
		/**
		 *  年龄
		 */
		private Integer age;
		/**
		 *  出生省份
		 */
		private String province;
		/**
		 *  出生地址详细信息
		 */
		private String addressDetail;
		/**
		 *  出生年
		 */
		private Short birthYear;
		/**
		 *  出生月
		 */
		private Short birthMonth;
		/** 出生日
		 * 
		 */
		private Short birthDay;
		/**
		 *  性别
		 */
		private String sex;
		/**
		 *  生肖
		 */
		private String zodiac;
		/**
		 *  星座
		 */
		private String constellation;
		
		
		public IndentityInfoBean() {
			super();
		}
		
		public boolean isVerify() {
			return verify;
		}
		public void setVerify(boolean verify) {
			this.verify = verify;
		}
		public String getBirthDate() {
			return birthDate;
		}
		public void setBirthDate(String birthDate) {
			this.birthDate = birthDate;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getAddressDetail() {
			return addressDetail;
		}
		public void setAddressDetail(String addressDetail) {
			this.addressDetail = addressDetail;
		}
		public Short getBirthYear() {
			return birthYear;
		}
		public void setBirthYear(Short birthYear) {
			this.birthYear = birthYear;
		}
		public Short getBirthMonth() {
			return birthMonth;
		}
		public void setBirthMonth(Short birthMonth) {
			this.birthMonth = birthMonth;
		}
		public Short getBirthDay() {
			return birthDay;
		}
		public void setBirthDay(Short birthDay) {
			this.birthDay = birthDay;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getZodiac() {
			return zodiac;
		}
		public void setZodiac(String zodiac) {
			this.zodiac = zodiac;
		}
		public String getConstellation() {
			return constellation;
		}
		public void setConstellation(String constellation) {
			this.constellation = constellation;
		}
		
}

