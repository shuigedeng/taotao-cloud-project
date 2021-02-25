package com.taotao.cloud.uc.biz.mybatis.entity;// package com.taotao.cloud.uc.biz.entity;
//
// import com.baomidou.mybatisplus.annotation.TableName;
// import com.taotao.cloud.data.mybatis.plus.mapper.SuperEntity;
// import lombok.*;
// import lombok.experimental.Accessors;
// import lombok.experimental.SuperBuilder;
//
// /**
//  * 菜单表
//  *
//  * @author dengtao
//  * @date 2020/6/15 11:00
//  */
// @Data
// @SuperBuilder
// @Accessors(chain= true)
// @EqualsAndHashCode(callSuper = false)
// @ToString(callSuper = true)
// @NoArgsConstructor
// @TableName("sys_menu")
// public class SysMenu extends SuperEntity {
//
//     /**
//      * 菜单名称
//      */
//     private String name;
//
//     /**
//      * 菜单权限标识
//      */
//     private String perms;
//
//     /**
//      * 前端path / 即跳转路由
//      */
//     private String path;
//
//     /**
//      * 菜单组件
//      */
//     private String component;
//
//     /**
//      * 父菜单ID
//      */
//     private Integer parentId;
//
//     /**
//      * 图标
//      */
//     private String icon;
//
//     /**
//      * 是否缓存页面: 0:不是  1:是（默认值1）
//      */
//     private Boolean keepAlive;
//
//     /**
//      * 是否隐藏路由菜单: 0否,1是（默认值0）
//      */
//     private Boolean hidden;
//
//     /**
//      * 聚合路由
//      */
//     private Boolean alwaysShow;
//
//     /**
//      * 重定向
//      */
//     private String redirect;
//
//     /**
//      * 是否为外链
//      */
//     private Boolean isFrame;
//
//     /**
//      * 排序
//      */
//     private Integer sort;
//
//     /**
//      * 菜单类型 （类型   0：目录   1：菜单   2：按钮）
//      */
//     private Integer type;
// }
