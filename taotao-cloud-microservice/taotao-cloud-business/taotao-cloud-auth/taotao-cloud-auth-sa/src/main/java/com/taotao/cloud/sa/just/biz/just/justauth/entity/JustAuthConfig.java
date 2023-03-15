package com.taotao.cloud.sa.just.biz.just.justauth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.data.mybatisplus.base.entity.MpSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
@TableName("t_just_auth_config")
@Schema(description = "租户第三方登录功能配置表")
public class JustAuthConfig extends MpSuperEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@Schema(description = "JustAuth开关")
	@TableField("enabled")
	private Boolean enabled;

	@Schema(description = "自定义扩展第三方登录的配置类")
	@TableField("enum_class")
	private String enumClass;

	@Schema(description = "Http请求的超时时间")
	@TableField("http_timeout")
	private Integer httpTimeout;

	@Schema(description = "缓存类型")
	@TableField("cache_type")
	private String cacheType;

	@Schema(description = "缓存前缀")
	@TableField("cache_prefix")
	private String cachePrefix;

	@Schema(description = "缓存超时时间")
	@TableField("cache_timeout")
	private Integer cacheTimeout;

	@Schema(description = "状态")
	@TableField("status")
	private Integer status;

	@Schema(description = "备注")
	@TableField("remark")
	private String remark;


}
