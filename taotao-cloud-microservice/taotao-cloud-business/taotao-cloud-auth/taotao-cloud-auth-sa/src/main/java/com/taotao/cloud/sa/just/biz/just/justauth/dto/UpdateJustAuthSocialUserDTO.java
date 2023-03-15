package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 第三方用户绑定
 * </p>
 *
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "JustAuthSocialUser对象", description = "第三方用户绑定")
public class UpdateJustAuthSocialUserDTO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "用户id")
	@Min(-9223372036854775808L)
	@Max(9223372036854775807L)
	@Length(min = 1, max = 19)
	private Long userId;

	@Schema(description = "第三方用户id")
	@Min(-9223372036854775808L)
	@Max(9223372036854775807L)
	@Length(min = 1, max = 19)
	private Long socialId;
}
