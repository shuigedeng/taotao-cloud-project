package com.taotao.cloud.uc.api.dto.user;

import com.taotao.cloud.web.mvc.constraints.IntEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * 用户DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(description = "用户DTO")
public class UserDTO implements Serializable {

	private static final long serialVersionUID = -1972549738577159538L;

	@Schema(description = "用户昵称", required = true)
	@NotBlank(message = "用户名不能超过为空")
	@Length(max = 20, message = "用户名不能超过20个字符")
	@Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "用户昵称限制格式错误：最多20字符，包含文字、字母和数字")
	private String nickname;

	@Schema(description = "用户真实姓名", required = true)
	@NotBlank(message = "用户真实姓名不能超过为空")
	@Length(max = 20, message = "用户真实姓名不能超过20个字符")
	@Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "用户真实姓名格式错误：最多20字符，包含文字、字母和数字")
	private String username;

	@Schema(description = "用户类型 1前端用户 2商户用户 3后台管理用户", required = true)
	@NotNull(message = "用户类型不能为空")
	@IntEnums(value = {1, 2, 3})
	private Integer type;

	@Schema(description = "性别 1男 2女 0未知", required = true)
	@NotNull(message = "用户性别不能为空")
	@IntEnums(value = {0, 1, 2})
	private Integer sex;

	@Schema(description = "手机号", required = true)
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码格式错误")
	private String phone;

	@Schema(description = "邮箱")
	@Email(message = "邮箱格式错误")
	private String email;

	@Schema(description = "部门ID")
	private Integer deptId;

	@Schema(description = "岗位ID")
	private Integer jobId;

	@Schema(description = "头像")
	private String avatar;

	@Override
	public String toString() {
		return "UserDTO{" +
			"nickname='" + nickname + '\'' +
			", username='" + username + '\'' +
			", type=" + type +
			", sex=" + sex +
			", phone='" + phone + '\'' +
			", email='" + email + '\'' +
			", deptId=" + deptId +
			", jobId=" + jobId +
			", avatar='" + avatar + '\'' +
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
		UserDTO userDTO = (UserDTO) o;
		return Objects.equals(nickname, userDTO.nickname) && Objects.equals(
			username, userDTO.username) && Objects.equals(type, userDTO.type)
			&& Objects.equals(sex, userDTO.sex) && Objects.equals(phone,
			userDTO.phone) && Objects.equals(email, userDTO.email)
			&& Objects.equals(deptId, userDTO.deptId) && Objects.equals(jobId,
			userDTO.jobId) && Objects.equals(avatar, userDTO.avatar);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nickname, username, type, sex, phone, email, deptId, jobId, avatar);
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserDTO() {
	}

	public UserDTO(String nickname, String username, Integer type, Integer sex, String phone,
		String email, Integer deptId, Integer jobId, String avatar) {
		this.nickname = nickname;
		this.username = username;
		this.type = type;
		this.sex = sex;
		this.phone = phone;
		this.email = email;
		this.deptId = deptId;
		this.jobId = jobId;
		this.avatar = avatar;
	}

	public static UserDTOBuilder builder() {
		return new UserDTOBuilder();
	}

	public static final class UserDTOBuilder {

		private String nickname;
		private String username;
		private Integer type;
		private Integer sex;
		private String phone;
		private String email;
		private Integer deptId;
		private Integer jobId;
		private String avatar;

		private UserDTOBuilder() {
		}

		public static UserDTOBuilder anUserDTO() {
			return new UserDTOBuilder();
		}

		public UserDTOBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public UserDTOBuilder username(String username) {
			this.username = username;
			return this;
		}

		public UserDTOBuilder type(Integer type) {
			this.type = type;
			return this;
		}

		public UserDTOBuilder sex(Integer sex) {
			this.sex = sex;
			return this;
		}

		public UserDTOBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public UserDTOBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserDTOBuilder deptId(Integer deptId) {
			this.deptId = deptId;
			return this;
		}

		public UserDTOBuilder jobId(Integer jobId) {
			this.jobId = jobId;
			return this;
		}

		public UserDTOBuilder avatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public UserDTO build() {
			UserDTO userDTO = new UserDTO();
			userDTO.setNickname(nickname);
			userDTO.setUsername(username);
			userDTO.setType(type);
			userDTO.setSex(sex);
			userDTO.setPhone(phone);
			userDTO.setEmail(email);
			userDTO.setDeptId(deptId);
			userDTO.setJobId(jobId);
			userDTO.setAvatar(avatar);
			return userDTO;
		}
	}
}
