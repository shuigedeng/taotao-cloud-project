package com.taotao.cloud.uc.api.dto.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "资源DTO")
public class ResourceDTO implements Serializable {

    private static final long serialVersionUID = -1972549738577159538L;

    @NotBlank(message = "资源名称不能超过为空")
    @Length(max = 20, message = "资源名称不能超过20个字符")
    @ApiModelProperty(value = "资源名称")
    private String name;

    @NotBlank(message = "资源类型不能超过为空")
    @ApiModelProperty(value = "资源类型 1：目录 2：菜单 3：按钮")
    private Byte type;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "前端path / 即跳转路由")
    private String path;

    @ApiModelProperty(value = "菜单组件")
    private String component;

    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否缓存页面: 0:否 1:是 (默认值0)")
    private Boolean keepAlive;

    @ApiModelProperty(value = "是否隐藏路由菜单: 0否,1是（默认值0）")
    private Boolean hidden;

    @ApiModelProperty(value = "聚合路由 0否,1是（默认值0）")
    private Boolean alwaysShow;

    @ApiModelProperty(value = "重定向")
    private String redirect;

    @ApiModelProperty(value = " 是否为外链 0否,1是（默认值0）")
    private Boolean isFrame;

    @ApiModelProperty(value = "排序值")
    private Integer sortNum;
}
