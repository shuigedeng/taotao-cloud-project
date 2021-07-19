package com.taotao.cloud.uc.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 重复校验DTO
 *
 * @author shuigedeng
 * @since 2020/5/2 16:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RepeatCheckDTO", description = "重复检查DTO")
public class RepeatCheckDTO {

	@Schema(description = "字段值 邮箱 手机号 用户名", required = true)
	private String fieldVal;

	@Schema(description = "指用户id 主要作用编辑情况过滤自己的校验", required = true)
	private Integer dataId;

}
