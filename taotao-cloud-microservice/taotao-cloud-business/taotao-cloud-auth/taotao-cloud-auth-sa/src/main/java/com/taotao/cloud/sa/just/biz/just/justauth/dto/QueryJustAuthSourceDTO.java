package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户第三方登录信息配置表
 * </p>
 *
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "JustAuthSource对象", description = "租户第三方登录信息配置表")
public class QueryJustAuthSourceDTO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "名称")
	private String sourceName;

	@Schema(description = "登录类型")
	private String sourceType;

	@Schema(description = "状态")
	@TableField("status")
	private Integer status;

	@Schema(description = "开始时间")
	private String beginDateTime;

	@Schema(description = "结束时间")
	private String endDateTime;

}
