package com.taotao.cloud.sys.api.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 菜单DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MenuMetaVo", description = "菜单元数据VO")
public class MenuMetaVO {

	@Schema(description = "名称")
	private String title;

	@Schema(description = "icon")
	private String icon;
}
