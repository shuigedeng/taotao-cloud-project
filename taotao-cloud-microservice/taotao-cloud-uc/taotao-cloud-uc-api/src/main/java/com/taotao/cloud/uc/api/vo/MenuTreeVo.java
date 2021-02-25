package com.taotao.cloud.uc.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 菜单树DTO
 *
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户注册VO")
public class MenuTreeVo {

    /**
     * 对应SysDepart中的id字段,前端数据树中的key
     */
    private Long key;

    /**
     * 对应SysDepart中的id字段,前端数据树中的value
     */
    private String value;

    /**
     * 对应depart_name字段,前端数据树中的title
     */
    private String title;

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Integer menuId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父菜单ID
     */
    private Integer parentMenuId;

    /**
     * 菜单类型 （类型   0：目录   1：菜单   2：按钮）
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 父菜单名称
     */
    private String parentName;

    /**
     * 菜单等级
     */
    private Integer level;

    private List<MenuTreeVo> children;
}
