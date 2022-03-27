package com.taotao.cloud.sys.api.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 菜单DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@Schema(name = "MenuMetaVo", description = "菜单元数据VO")
public class MenuMetaVO {

	@Schema(description = "名称")
	private String title;

	@Schema(description = "icon")
	private String icon;
}
