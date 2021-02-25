package com.taotao.cloud.uc.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * dengtao
 *
 * @author dengtao
 * @date 2020/6/15 11:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户注册VO")
public class MenuDTO {

    private static final long serialVersionUID = 1L;

    private Integer menuId;
    private String name;
    private String perms;
    private String path;
    private Boolean isFrame;
    private Integer parentId;
    private String component;
    private String icon;
    private Integer sort;
    private Integer type;
    private String delFlag;
    private Boolean keepAlive;
    private Boolean hidden;
    private Boolean alwaysShow;
    private String redirect;

}
