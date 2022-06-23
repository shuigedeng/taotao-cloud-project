package com.taotao.cloud.member.api.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 会员修改 用于后台的用户信息修改
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-14 11:23:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员修改")
public class ManagerMemberEditDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

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
}
