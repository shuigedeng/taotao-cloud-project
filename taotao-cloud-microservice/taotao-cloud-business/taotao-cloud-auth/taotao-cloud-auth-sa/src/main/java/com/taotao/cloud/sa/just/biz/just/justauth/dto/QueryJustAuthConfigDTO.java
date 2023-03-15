package com.taotao.cloud.sa.just.biz.just.justauth.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户第三方登录功能配置表
 * </p>
 *
 * @since 2022-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "JustAuthConfig对象", description = "租户第三方登录功能配置表")
public class QueryJustAuthConfigDTO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "登录开关")
	private Boolean enabled;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "开始时间")
	private String beginDateTime;

	@Schema(description = "结束时间")
	private String endDateTime;

}
