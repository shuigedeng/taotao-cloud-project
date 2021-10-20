package com.taotao.cloud.uc.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 重复校验DTO
 *
 * @author shuigedeng
 * @since 2020/5/2 16:40
 */
@Schema(description = "重复检查DTO")
public record RepeatCheckDTO(
	@Schema(description = "字段值 邮箱 手机号 用户名", required = true)
	String fieldVal,

	@Schema(description = "指用户id 主要作用编辑情况过滤自己的校验", required = true)
	Integer dataId) implements Serializable {

	@Serial
	private static final long serialVersionUID = -5002412807608124376L;

}
