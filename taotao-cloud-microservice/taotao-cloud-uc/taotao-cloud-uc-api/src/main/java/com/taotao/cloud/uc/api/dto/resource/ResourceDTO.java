package com.taotao.cloud.uc.api.dto.resource;

import com.taotao.cloud.core.mvc.constraints.IntEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 资源DTO
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ResourceDTO", description = "添加资源对象DTO")
public class ResourceDTO implements Serializable {

	private static final long serialVersionUID = -1972549738577159538L;

	@Schema(description = "资源名称", required = true)
	@NotBlank(message = "资源名称不能超过为空")
	@Length(max = 20, message = "资源名称不能超过20个字符")
	private String name;

	@Schema(description = "资源类型 1：目录 2：菜单 3：按钮", required = true)
	@NotBlank(message = "资源类型不能超过为空")
	@IntEnums(value = {1, 2, 3})
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
}
