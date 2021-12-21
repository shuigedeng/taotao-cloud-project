package com.taotao.cloud.member.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 会员修改 用于后台的用户信息修改
 *
 * 
 * @since 2020/12/15 9:57
 */
@Schema(description = "会员修改")
public class ManagerMemberEditDTO {

	@Schema(description = "会员用户名,用户名不能进行修改", required = true)
	@NotNull(message = "会员用户名不能为空")
	private String username;

	@Schema(description = "会员密码")
	private String password;

	@Schema(description = "昵称")
	@Length(min = 2, max = 20, message = "会员昵称必须为2到20位之间")
	private String nickName;

	@Schema(description = "地区")
	private String region;

	@Schema(description = "地区ID")
	private String regionId;

	@Min(message = "必须为数字且1为男,0为女", value = 0)
	@Max(message = "必须为数字且1为男,0为女", value = 1)
	@NotNull(message = "会员性别不能为空")
	@Schema(description = "会员性别,1为男，0为女")
	private Integer sex;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "会员生日")
	private Date birthday;

	@Schema(description = "会员头像")
	private String face;

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
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

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}
}
