package com.taotao.cloud.uc.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "MenuMetaVo", description = "菜单元数据VO")
public class MenuMetaVo {

	@Schema(description = "名称")
	private String title;

	@Schema(description = "icon")
	private String icon;
}
