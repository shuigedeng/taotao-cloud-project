package com.taotao.cloud.sa.just.biz.just.justauth.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 第三方用户信息
 * </p>
 *
 * @since 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "JustAuthSocial对象", description = "第三方用户信息")
public class QueryJustAuthSocialDTO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "第三方ID")
	private String uuid;

	@Schema(description = "第三方来源")
	private String source;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "用户昵称")
	private String nickname;

	@Schema(description = "开始时间")
	private String beginDateTime;

	@Schema(description = "结束时间")
	private String endDateTime;

}
