package com.taotao.cloud.sys.api.web.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

import lombok.*;

/**
 * 菜单DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "菜单元数据VO")
public class MenuMetaVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4132785717179910025L;

	@Schema(description = "名称")
	private String title;

	@Schema(description = "icon")
	private String icon;
}
