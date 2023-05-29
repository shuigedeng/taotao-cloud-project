/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.sys.api.enums.ResourceTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 菜单表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:08:15
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Resource.TABLE_NAME)
@TableName(Resource.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = Resource.TABLE_NAME, comment = "资源表")
public class Resource extends BaseSuperEntity<Resource, Long> {

    public static final String TABLE_NAME = "tt_resource";

    /** 菜单标题 */
    @Column(name = "name", unique = true, columnDefinition = "varchar(32) not null comment '菜单名称'")
    private String name;

    /** 权限标识 */
    @Column(name = "permission", columnDefinition = "varchar(255) comment '权限标识'")
    private String permission;

    /** 前端path / 即跳转路由 */
    @Column(name = "path", columnDefinition = "varchar(255) comment '前端path / 即跳转路由'")
    private String path;

    /** 菜单组件 */
    @Column(name = "component", columnDefinition = "varchar(255) comment '菜单组件'")
    private String component;

    /** 父菜单ID */
    @Column(name = "parent_id", columnDefinition = "bigint not null default 0 comment '父菜单ID'")
    private Long parentId;

    /** 图标 */
    @Column(name = "icon", columnDefinition = "varchar(255) comment '图标'")
    private String icon;

    /** 排序值 */
    @Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
    private Integer sortNum;

    /** 是否缓存页面: 0:否 1:是 (默认值0) */
    @Column(name = "keep_alive", columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否缓存页面: 0:否 1:是 (默认值0)'")
    private Boolean keepAlive;

    /**
     * 菜单类型 1:目录 2:菜单 3：资源(分页查询操作、操作按钮、删除按钮、查询按钮、等等) 资源 (包括分页、各种按钮、删除 等等 对应的是请求路径如：/api/menu/find)
     *
     * @see ResourceTypeEnum
     */
    @Column(name = "type", columnDefinition = "int not null comment '菜单类型 (1:目录 2:菜单 3：资源)'")
    private Integer type;

    /** url请求Id (type=3 时, 此id有值) */
    @Column(name = "request_path_id", columnDefinition = "bigint null comment 'url请求Id (type=3时, 此id有值)'")
    private Long requestPathId;

    /** 是否隐藏路由菜单: 0否,1是（默认值0） */
    @Column(name = "hidden", columnDefinition = "boolean DEFAULT false comment '是否隐藏路由菜单: 0否,1是（默认值0)'")
    private Boolean hidden;

    /** 重定向 */
    @Column(name = "redirect", columnDefinition = "varchar(255) comment '重定向'")
    private String redirect;

    /** 是否为外链 */
    @Column(name = "target", columnDefinition = "varchar(32) comment '是否为外链'")
    private String target;

    /** 租户id */
    @Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
    private String tenantId;

    @Builder
    public Resource(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            String name,
            String permission,
            String path,
            String component,
            Long parentId,
            String icon,
            Integer sortNum,
            Boolean keepAlive,
            Integer type,
            Boolean hidden,
            String redirect,
            String target,
            String tenantId) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.name = name;
        this.permission = permission;
        this.path = path;
        this.component = component;
        this.parentId = parentId;
        this.icon = icon;
        this.sortNum = sortNum;
        this.keepAlive = keepAlive;
        this.type = type;
        this.hidden = hidden;
        this.redirect = redirect;
        this.target = target;
        this.tenantId = tenantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Resource resource = (Resource) o;
        return getId() != null && Objects.equals(getId(), resource.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
