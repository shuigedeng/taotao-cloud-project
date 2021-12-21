package com.taotao.cloud.member.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @since 2021/11/8
 **/
@Schema(description = "MemberVO")
public class MemberVO implements Serializable {

	private static final long serialVersionUID = 1810890757303309436L;

	@Schema(description = "唯一标识", hidden = true)
	private String id;

	@Schema(description = "会员用户名")
	private String username;

	@Schema(description = "昵称")
	private String nickName;

	@Schema(description = "会员性别,1为男，0为女")
	private Integer sex;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "会员生日")
	private Date birthday;

	@Schema(description = "会员地址ID")
	private String regionId;

	@Schema(description = "会员地址")
	private String region;

	@Schema(description = "手机号码", required = true)
	//@Sensitive(strategy = SensitiveStrategy.PHONE)
	private String mobile;

	@Schema(description = "积分数量")
	private Long point;

	@Schema(description = "积分总数量")
	private Long totalPoint;

	@Schema(description = "会员头像")
	private String face;

	@Schema(description = "会员状态")
	private Boolean disabled;

	@Schema(description = "是否开通店铺")
	private Boolean haveStore;

	@Schema(description = "店铺ID")
	private String storeId;

	@Schema(description = "openId")
	private String openId;

	/**
	 * @see ClientTypeEnum
	 */
	@Schema(description = "客户端")
	private String clientEnum;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "最后一次登录时间")
	private Date lastLoginDate;

	@Schema(description = "会员等级ID")
	private String gradeId;

	@Schema(description = "经验值数量")
	private Long experience;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "创建时间", hidden = true)
	private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getClientEnum() {
		return clientEnum;
	}

	public void setClientEnum(String clientEnum) {
		this.clientEnum = clientEnum;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public Long getExperience() {
		return experience;
	}

	public void setExperience(Long experience) {
		this.experience = experience;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
