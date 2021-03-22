package com.taotao.cloud.uc.api.vo.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源VO
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ResourceVO", description = "资源VO")
public class ResourceVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "资源名称")
	private String name;

	@Schema(description = "资源类型 1：目录 2：菜单 3：按钮")
	private Byte type;

	@Schema(description = "权限标识")
	private String perms;

	@Schema(description = "前端path / 即跳转路由")
	private String path;

	@Schema(description = "菜单组件")
	private String component;

	@Schema(description = "父菜单ID")
	private Long parentId;

	@Schema(description = "图标")
	private String icon;

	@Schema(description = "是否缓存页面: 0:否 1:是 (默认值0)")
	private Boolean keepAlive;

	@Schema(description = "是否隐藏路由菜单: 0否,1是（默认值0）")
	private Boolean hidden;

	@Schema(description = "聚合路由 0否,1是（默认值0）")
	private Boolean alwaysShow;

	@Schema(description = "重定向")
	private String redirect;

	@Schema(description = "是否为外链 0否,1是（默认值0）")
	private Boolean isFrame;

	@Schema(description = "排序值")
	private Integer sortNum;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;
}
