package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资源表
 *
 * @author dengtao
 * @date 2020/6/15 11:00
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_sys_resource")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_resource", comment = "资源表")
public class SysResource extends BaseEntity {

	/**
	 * 资源名称
	 */
	@Column(name = "name", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '资源名称'")
	private String name;

	/**
	 * 权限标识
	 */
	@Column(name = "perms", columnDefinition = "varchar(255) comment '权限标识'")
	private String perms;

	/**
	 * 前端path / 即跳转路由
	 */
	@Column(name = "path", columnDefinition = "varchar(255) comment '前端path / 即跳转路由'")
	private String path;

	/**
	 * 菜单组件
	 */
	@Column(name = "component", columnDefinition = "varchar(255) comment '菜单组件'")
	private String component;

	/**
	 * 父菜单ID
	 */
	@Builder.Default
	@Column(name = "parent_id", columnDefinition = "bigint not null default 0 comment '父菜单ID'")
	private Long parentId = 0L;

	/**
	 * 图标
	 */
	@Column(name = "icon", columnDefinition = "varchar(255) comment '图标'")
	private String icon;

	/**
	 * 是否缓存页面: 0:否 1:是 (默认值0)
	 */
	@Builder.Default
	@Column(name = "keep_alive", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否缓存页面: 0:否 1:是 (默认值0)'")
	private Boolean keepAlive = false;

	/**
	 * 是否隐藏路由菜单: 0否,1是（默认值0）
	 */
	@Builder.Default
	@Column(name = "hidden", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否隐藏路由菜单: 0否,1是（默认值0)'")
	private Boolean hidden = false;

	/**
	 * 聚合路由 0否,1是（默认值0）
	 */
	@Builder.Default
	@Column(name = "always_show", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '聚合路由 0否,1是（默认值0)'")
	private Boolean alwaysShow = false;

	/**
	 * 重定向
	 */
	@Column(name = "redirect", columnDefinition = "varchar(255) comment '重定向'")
	private String redirect;

	/**
	 * 是否为外链 0否,1是（默认值0）
	 */
	@Builder.Default
	@Column(name = "is_frame", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否为外链 0否,1是（默认值0)'")
	private Boolean isFrame = false;

	/**
	 * 排序值
	 */
	@Builder.Default
	@Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
	private Integer sortNum = 0;

	/**
	 * @see com.taotao.cloud.common.enums.ResourceTypeEnum
	 */
	@Builder.Default
	@Column(name = "type", nullable = false, columnDefinition = "tinyint(1) unsigned not null default 1 comment '资源类型 (1:一级(左侧)菜单 2:二级(顶部)菜单 3：按钮)'")
	private byte type = 1;

	/**
	 * 租户id
	 */
	@Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
	private String tenantId;
}
