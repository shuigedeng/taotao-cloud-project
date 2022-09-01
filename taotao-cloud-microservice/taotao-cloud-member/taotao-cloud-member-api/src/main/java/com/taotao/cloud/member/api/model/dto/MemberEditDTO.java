package com.taotao.cloud.member.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 会员信息修改DTO
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-14 11:25:33
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员信息修改DTO")
public class MemberEditDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "昵称", required = true)
	@Size(min = 2, max = 20, message = "会员昵称必须为2到20位之间")
	private String nickName;

	@Schema(description = "会员地址ID")
	private String regionId;

	@Schema(description = "会员地址")
	private String region;

	@Min(message = "必须为数字且1为男,0为女", value = 0)
	@Max(message = "必须为数字且1为男,0为女", value = 1)
	@NotNull(message = "会员性别不能为空")
	@Schema(description = "会员性别,1为男，0为女", required = true)
	private Integer sex;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "会员生日")
	private Date birthday;

	@Schema(description = "详细地址")
	private String address;

	@Schema(description = "会员头像")
	private String face;
}
