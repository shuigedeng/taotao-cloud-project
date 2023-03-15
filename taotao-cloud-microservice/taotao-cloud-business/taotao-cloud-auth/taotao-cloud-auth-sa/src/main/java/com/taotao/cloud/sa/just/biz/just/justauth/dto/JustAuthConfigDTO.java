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
@Schema(description = "JustAuthConfigDTO对象", description = "租户第三方登录功能配置表")
public class JustAuthConfigDTO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "登录开关")
	private Boolean enabled;

	@Schema(description = "配置类")
	private String enumClass;

	@Schema(description = "Http超时")
	private Integer httpTimeout;

	@Schema(description = "缓存类型")
	private String cacheType;

	@Schema(description = "缓存前缀")
	private String cachePrefix;

	@Schema(description = "缓存超时")
	private Integer cacheTimeout;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "备注")
	private String remark;
}
