package com.taotao.cloud.uc.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户更新DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "UpdateUserDTO", description = "用户更新DTO")
public class UpdateUserDTO implements Serializable {

	private static final long serialVersionUID = 7527760213215827929L;

	@Schema(description = "昵称", required = true)
	@NotBlank(message = "昵称不能为空")
	@Max(value = 10, message = "昵称不能超过10个字符")
	private String nickname;

	@Schema(description = "真实用户名", required = true)
	@NotBlank(message = "真实用户名不能为空")
	@Max(value = 10, message = "真实用户名不能超过10个字符")
	private String username;

	@Schema(description = "手机号", required = true)
	@NotBlank(message = "真实用户名不能为空")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码不正确")
	private String phone;

	@Schema(description = "邮箱")
	@Email(message = "邮箱格式错误")
	private String email;

	@Schema(description = "头像")
	private String avatar;

	@Schema(description = "部门ID")
	private Integer deptId;

	@Schema(description = "岗位ID")
	private Integer jobId;

	@Schema(description = "是否锁定用户")
	private Boolean lockFlag;

	@Schema(description = "是否删除用户")
	private Integer delFlag;

	@Schema(description = "角色id列表")
	private List<Integer> roleList;

	@Override
	public String toString() {
		return "UpdateUserDTO{" +
			"nickname='" + nickname + '\'' +
			", username='" + username + '\'' +
			", phone='" + phone + '\'' +
			", email='" + email + '\'' +
			", avatar='" + avatar + '\'' +
			", deptId=" + deptId +
			", jobId=" + jobId +
			", lockFlag=" + lockFlag +
			", delFlag=" + delFlag +
			", roleList=" + roleList +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UpdateUserDTO that = (UpdateUserDTO) o;
		return Objects.equals(nickname, that.nickname) && Objects.equals(username,
			that.username) && Objects.equals(phone, that.phone)
			&& Objects.equals(email, that.email) && Objects.equals(avatar,
			that.avatar) && Objects.equals(deptId, that.deptId)
			&& Objects.equals(jobId, that.jobId) && Objects.equals(lockFlag,
			that.lockFlag) && Objects.equals(delFlag, that.delFlag)
			&& Objects.equals(roleList, that.roleList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nickname, username, phone, email, avatar, deptId, jobId, lockFlag,
			delFlag,
			roleList);
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public Boolean getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(Boolean lockFlag) {
		this.lockFlag = lockFlag;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public List<Integer> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Integer> roleList) {
		this.roleList = roleList;
	}

	public UpdateUserDTO() {
	}

	public UpdateUserDTO(String nickname, String username, String phone, String email,
		String avatar, Integer deptId, Integer jobId, Boolean lockFlag, Integer delFlag,
		List<Integer> roleList) {
		this.nickname = nickname;
		this.username = username;
		this.phone = phone;
		this.email = email;
		this.avatar = avatar;
		this.deptId = deptId;
		this.jobId = jobId;
		this.lockFlag = lockFlag;
		this.delFlag = delFlag;
		this.roleList = roleList;
	}

	public static UpdateUserDTOBuilder builder() {
		return new UpdateUserDTOBuilder();
	}

	public static final class UpdateUserDTOBuilder {

		private String nickname;
		private String username;
		private String phone;
		private String email;
		private String avatar;
		private Integer deptId;
		private Integer jobId;
		private Boolean lockFlag;
		private Integer delFlag;
		private List<Integer> roleList;

		private UpdateUserDTOBuilder() {
		}

		public static UpdateUserDTOBuilder anUpdateUserDTO() {
			return new UpdateUserDTOBuilder();
		}

		public UpdateUserDTOBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public UpdateUserDTOBuilder username(String username) {
			this.username = username;
			return this;
		}

		public UpdateUserDTOBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public UpdateUserDTOBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UpdateUserDTOBuilder avatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public UpdateUserDTOBuilder deptId(Integer deptId) {
			this.deptId = deptId;
			return this;
		}

		public UpdateUserDTOBuilder jobId(Integer jobId) {
			this.jobId = jobId;
			return this;
		}

		public UpdateUserDTOBuilder lockFlag(Boolean lockFlag) {
			this.lockFlag = lockFlag;
			return this;
		}

		public UpdateUserDTOBuilder delFlag(Integer delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public UpdateUserDTOBuilder roleList(List<Integer> roleList) {
			this.roleList = roleList;
			return this;
		}

		public UpdateUserDTO build() {
			UpdateUserDTO updateUserDTO = new UpdateUserDTO();
			updateUserDTO.setNickname(nickname);
			updateUserDTO.setUsername(username);
			updateUserDTO.setPhone(phone);
			updateUserDTO.setEmail(email);
			updateUserDTO.setAvatar(avatar);
			updateUserDTO.setDeptId(deptId);
			updateUserDTO.setJobId(jobId);
			updateUserDTO.setLockFlag(lockFlag);
			updateUserDTO.setDelFlag(delFlag);
			updateUserDTO.setRoleList(roleList);
			return updateUserDTO;
		}
	}
}
