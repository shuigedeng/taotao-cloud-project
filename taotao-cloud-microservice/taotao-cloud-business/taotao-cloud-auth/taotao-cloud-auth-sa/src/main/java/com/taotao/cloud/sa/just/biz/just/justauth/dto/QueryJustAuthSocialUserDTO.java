package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import io.swagger.annotations.ApiModel;
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
public class QueryJustAuthSocialUserDTO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户id")
	@Length(min = 1, max = 19)
	private Long userId;

	@Schema(description = "第三方用户id")
	@Length(min = 1, max = 19)
	private Long socialId;

	@Schema(description = "开始时间")
	private String beginDateTime;

	@Schema(description = "结束时间")
	private String endDateTime;

}
