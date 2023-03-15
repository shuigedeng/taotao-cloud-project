package com.taotao.cloud.sa.just.biz.just.justauth.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 第三方用户绑定
 * </p>
 *
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "JustAuthSocialUserDTO对象", description = "第三方用户绑定")
public class JustAuthSocialUserDTO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "用户id")
	private Long userId;

	@Schema(description = "第三方用户id")
	private Long socialId;
}
