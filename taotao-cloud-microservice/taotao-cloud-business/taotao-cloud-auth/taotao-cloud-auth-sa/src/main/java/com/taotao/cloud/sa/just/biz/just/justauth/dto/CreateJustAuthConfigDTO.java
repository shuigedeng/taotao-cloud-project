package com.taotao.cloud.sa.just.biz.just.justauth.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

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
public class CreateJustAuthConfigDTO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "登录开关")
	@NotBlank(message = "登录开关不能为空")
	@Length(min = 1, max = 1)
	private Boolean enabled;

	@Schema(description = "配置类")
	@Length(min = 1, max = 255)
	private String enumClass;

	@Schema(description = "Http超时")
	@Min(0L)
	@Max(2147483647L)
	@Length(min = 1, max = 19)
	private Integer httpTimeout;

	@Schema(description = "缓存类型")
	@Length(min = 1, max = 32)
	private String cacheType;

	@Schema(description = "缓存前缀")
	@Length(min = 1, max = 100)
	private String cachePrefix;

	@Schema(description = "缓存超时")
	@Min(0L)
	@Max(2147483647L)
	@Length(min = 1, max = 255)
	private Integer cacheTimeout;

	@Schema(description = "状态")
	@NotBlank(message = "状态不能为空")
	@Min(0L)
	@Max(2147483647L)
	@Length(min = 1, max = 3)
	private Integer status;

	@Schema(description = "备注")
	@Length(min = 1, max = 255)
	private String remark;
}
