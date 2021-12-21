package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * 会员
 *
 * @since 2020-02-25 14:10:16
 */
@Entity
@Table(name = Member.TABLE_NAME)
@TableName(Member.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Member.TABLE_NAME, comment = "会员表")
public class Member extends BaseSuperEntity<Member, Long> {

	public static final String TABLE_NAME = "li_member";

	@Column(name = "nickname", nullable = false, columnDefinition = "varchar(32) not null comment '昵称'")
	private String nickname;

	@Column(name = "username", nullable = false, columnDefinition = "varchar(32) not null comment '会员用户名'")
	private String username;

	@Column(name = "password", nullable = false, columnDefinition = "varchar(32) not null comment '会员密码'")
	private String password;

	@NotEmpty(message = "手机号码不能为空")
	//@Sensitive(strategy = SensitiveStrategy.PHONE)
	@Column(name = "mobile", nullable = false, columnDefinition = "varchar(32) not null comment '手机号码'")
	private String mobile;

	@Min(message = "会员性别参数错误", value = 0)
	@Column(name = "sex", nullable = false, columnDefinition = "int not null comment default 0 '会员性别,1为男，0为女'")
	private Integer sex;

	@Column(name = "birthday", nullable = false, columnDefinition = "varchar(32) not null comment '会员生日'")
	private String birthday;

	@Column(name = "region_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员地址ID'")
	private String regionId;

	@Column(name = "region", nullable = false, columnDefinition = "varchar(32) not null comment '会员地址'")
	private String region;

	@Column(name = "province_code", columnDefinition = "varchar(32) COMMENT '省code'")
	private String provinceCode;

	@Column(name = "city_code", columnDefinition = "varchar(32) COMMENT '市code'")
	private String cityCode;

	@Column(name = "area_code", columnDefinition = "varchar(32) COMMENT '区、县code'")
	private String areaCode;

	@Min(message = "必须为数字", value = 0)
	@Column(name = "point", nullable = false, columnDefinition = "varchar(32) not null comment '积分数量'")
	private Long point;

	@Min(message = "必须为数字", value = 0)
	@Column(name = "total_point", nullable = false, columnDefinition = "varchar(32) not null comment '积分总数量'")
	private Long totalPoint;

	@Column(name = "face", nullable = false, columnDefinition = "varchar(32) not null comment '会员头像'")
	private String face;

	@Column(name = "disabled", nullable = false, columnDefinition = "boolean not null default false comment '会员状态'")
	private Boolean disabled;

	@Column(name = "have_store", nullable = false, columnDefinition = "boolean not null default false  comment '是否开通店铺'")
	private Boolean haveStore;

	@Column(name = "store_id", nullable = false, columnDefinition = "varchar(32) not null comment '店铺ID'")
	private String storeId;

	/**
	 * @see ClientTypeEnum
	 */
	@Column(name = "client_enum", nullable = false, columnDefinition = "varchar(32) not null comment '客户端'")
	private String clientEnum;

	@Column(name = "last_login_date", nullable = false, columnDefinition = "TIMESTAMP comment '最后一次登录时间'")
	private LocalDateTime lastLoginDate;

	@Column(name = "last_login_ip", columnDefinition = "varchar(12) DEFAULT '' COMMENT '最后一次登陆ip'")
	private String lastLoginIp = "";

	@Column(name = "is_lock", nullable = false, columnDefinition = "int NOT NULL DEFAULT 1 comment '是否锁定 1-正常，2-锁定'")
	private Integer isLock = 1;

	@Column(name = "status", nullable = false, columnDefinition = "int NOT NULL DEFAULT 1 comment '状态 1:启用, 0:停用'")
	private Integer status = 1;

	@Column(name = "grade_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员等级ID 用户等级 0:普通用户 1:vip'")
	private String gradeId;

	@Column(name = "type", nullable = false, columnDefinition = "int not null default 1 comment '用户类型 1个人用户 2企业用户'")
	private Integer type = 1;

	@Column(name = "create_ip", columnDefinition = "varchar(12) DEFAULT '' COMMENT '创建ip'")
	private String createIp = "";

	@Min(message = "必须为数字", value = 0)
	@Column(name = "experience", nullable = false, columnDefinition = "bigint not null defaultt 0 comment '经验值数量'")
	private Long experience;


	public Member(String username, String password, String mobile) {
		this.username = username;
		this.password = password;
		this.mobile = mobile;
		this.nickname = mobile;
		this.disabled = true;
		this.haveStore = false;
		this.sex = 0;
		this.point = 0L;
		this.totalPoint = 0L;
		this.lastLoginDate = LocalDateTime.now();
	}

	public Member(String username, String password, String mobile, String nickname, String face) {
		this.username = username;
		this.password = password;
		this.mobile = mobile;
		this.nickname = nickname;
		this.disabled = true;
		this.haveStore = false;
		this.face = face;
		this.sex = 0;
		this.point = 0L;
		this.totalPoint = 0L;
		this.lastLoginDate = LocalDateTime.now();
	}

	public Member(String username, String password, String face, String nickname, Integer sex) {
		this.username = username;
		this.password = password;
		this.mobile = "";
		this.nickname = nickname;
		this.disabled = true;
		this.haveStore = false;
		this.face = face;
		this.sex = sex;
		this.point = 0L;
		this.totalPoint = 0L;
		this.lastLoginDate = LocalDateTime.now();
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
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

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Long getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(Long totalPoint) {
		this.totalPoint = totalPoint;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getHaveStore() {
		return haveStore;
	}

	public void setHaveStore(Boolean haveStore) {
		this.haveStore = haveStore;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getClientEnum() {
		return clientEnum;
	}

	public void setClientEnum(String clientEnum) {
		this.clientEnum = clientEnum;
	}

	public LocalDateTime getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(LocalDateTime lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Integer getIsLock() {
		return isLock;
	}

	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public Long getExperience() {
		return experience;
	}

	public void setExperience(Long experience) {
		this.experience = experience;
	}
}
