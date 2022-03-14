package com.taotao.cloud.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 会员注册DTO
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-14 11:25:41
 */
@Data
@Schema(name = "MemberDTO", description = "会员注册DTO")
public class MemberDTO implements Serializable {

	private static final long serialVersionUID = -1972549738577159538L;

	@Schema(description = "用户昵称", required = true)
	@NotBlank(message = "用户昵称不能超过为空")
	@Length(max = 20, message = "用户昵称不能超过20个字符")
	@Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9\\*]*$", message = "用户昵称限制格式错误：最多20字符，包含文字、字母和数字")
	private String nickname;

	@Schema(description = "用户密码", required = true)
	@NotBlank(message = "用户密码不能超过为空")
	@Length(max = 18, message = "密码不能超过20个字符")
	@Length(min = 6, message = "密码不能小于6个字符")
	@Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$", message = "密码格式错误：密码至少包含 数字和英文，长度6-20个字符")
	private String password;

	@Schema(description = "手机号", required = true)
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码格式错误")
	private String phone;

	@Override
	public String toString() {
		return "MemberDTO{" +
			"nickname='" + nickname + '\'' +
			", password='" + password + '\'' +
			", phone='" + phone + '\'' +
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
		MemberDTO memberDTO = (MemberDTO) o;
		return Objects.equals(nickname, memberDTO.nickname) && Objects.equals(
			password, memberDTO.password) && Objects.equals(phone, memberDTO.phone);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nickname, password, phone);
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public static MemberDTOBuilder builder() {
		return new MemberDTOBuilder();
	}

	public static final class MemberDTOBuilder {

		private String nickname;
		private String password;
		private String phone;

		private MemberDTOBuilder() {
		}

		public static MemberDTOBuilder aMemberDTO() {
			return new MemberDTOBuilder();
		}

		public MemberDTOBuilder nickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public MemberDTOBuilder password(String password) {
			this.password = password;
			return this;
		}

		public MemberDTOBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public MemberDTO build() {
			MemberDTO memberDTO = new MemberDTO();
			memberDTO.setNickname(nickname);
			memberDTO.setPassword(password);
			memberDTO.setPhone(phone);
			return memberDTO;
		}
	}
}
