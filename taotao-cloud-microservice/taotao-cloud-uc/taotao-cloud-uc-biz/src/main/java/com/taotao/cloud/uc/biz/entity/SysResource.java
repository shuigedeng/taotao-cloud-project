package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资源表
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
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
	@Column(name = "keep_alive", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否缓存页面: 0:否 1:是 (默认值0)'")
	private Boolean keepAlive = false;

	/**
	 * 是否隐藏路由菜单: 0否,1是（默认值0）
	 */
	@Column(name = "hidden", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否隐藏路由菜单: 0否,1是（默认值0)'")
	private Boolean hidden = false;

	/**
	 * 聚合路由 0否,1是（默认值0）
	 */
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
	@Column(name = "is_frame", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否为外链 0否,1是（默认值0)'")
	private Boolean isFrame = false;

	/**
	 * 排序值
	 */
	@Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
	private Integer sortNum = 0;

	/**
	 * @see com.taotao.cloud.common.enums.ResourceTypeEnum
	 */
	@Column(name = "type", nullable = false, columnDefinition = "tinyint(1) unsigned not null default 1 comment '资源类型 (1:一级(左侧)菜单 2:二级(顶部)菜单 3：按钮)'")
	private byte type = 1;

	/**
	 * 租户id
	 */
	@Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
	private String tenantId;


	@Override
	public String toString() {
		return "SysResource{" +
			"name='" + name + '\'' +
			", perms='" + perms + '\'' +
			", path='" + path + '\'' +
			", component='" + component + '\'' +
			", parentId=" + parentId +
			", icon='" + icon + '\'' +
			", keepAlive=" + keepAlive +
			", hidden=" + hidden +
			", alwaysShow=" + alwaysShow +
			", redirect='" + redirect + '\'' +
			", isFrame=" + isFrame +
			", sortNum=" + sortNum +
			", type=" + type +
			", tenantId='" + tenantId + '\'' +
			"} " + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		SysResource that = (SysResource) o;
		return type == that.type && Objects.equals(name, that.name)
			&& Objects.equals(perms, that.perms) && Objects.equals(path,
			that.path) && Objects.equals(component, that.component)
			&& Objects.equals(parentId, that.parentId) && Objects.equals(icon,
			that.icon) && Objects.equals(keepAlive, that.keepAlive)
			&& Objects.equals(hidden, that.hidden) && Objects.equals(alwaysShow,
			that.alwaysShow) && Objects.equals(redirect, that.redirect)
			&& Objects.equals(isFrame, that.isFrame) && Objects.equals(sortNum,
			that.sortNum) && Objects.equals(tenantId, that.tenantId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name, perms, path, component, parentId, icon,
			keepAlive,
			hidden, alwaysShow, redirect, isFrame, sortNum, type, tenantId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Boolean getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(Boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public Boolean getAlwaysShow() {
		return alwaysShow;
	}

	public void setAlwaysShow(Boolean alwaysShow) {
		this.alwaysShow = alwaysShow;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public Boolean getFrame() {
		return isFrame;
	}

	public void setFrame(Boolean frame) {
		isFrame = frame;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public SysResource() {
	}

	public SysResource(String name, String perms, String path, String component,
		Long parentId, String icon, Boolean keepAlive, Boolean hidden, Boolean alwaysShow,
		String redirect, Boolean isFrame, Integer sortNum, byte type, String tenantId) {
		this.name = name;
		this.perms = perms;
		this.path = path;
		this.component = component;
		this.parentId = parentId;
		this.icon = icon;
		this.keepAlive = keepAlive;
		this.hidden = hidden;
		this.alwaysShow = alwaysShow;
		this.redirect = redirect;
		this.isFrame = isFrame;
		this.sortNum = sortNum;
		this.type = type;
		this.tenantId = tenantId;
	}

	public SysResource(Long id, Long createBy, Long lastModifiedBy, LocalDateTime createTime,
		LocalDateTime lastModifiedTime, int version, Boolean delFlag,
		String name, String perms, String path, String component, Long parentId,
		String icon, Boolean keepAlive, Boolean hidden, Boolean alwaysShow,
		String redirect, Boolean isFrame, Integer sortNum, byte type, String tenantId) {
		super(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version, delFlag);
		this.name = name;
		this.perms = perms;
		this.path = path;
		this.component = component;
		this.parentId = parentId;
		this.icon = icon;
		this.keepAlive = keepAlive;
		this.hidden = hidden;
		this.alwaysShow = alwaysShow;
		this.redirect = redirect;
		this.isFrame = isFrame;
		this.sortNum = sortNum;
		this.type = type;
		this.tenantId = tenantId;
	}

	public static SysResourceBuilder builder() {
		return new SysResourceBuilder();
	}

	public static final class SysResourceBuilder {

		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;
		private String name;
		private String perms;
		private String path;
		private String component;
		private Long parentId = 0L;
		private String icon;
		private Boolean keepAlive = false;
		private Boolean hidden = false;
		private Boolean alwaysShow = false;
		private String redirect;
		private Boolean isFrame = false;
		private Integer sortNum = 0;
		private byte type = 1;
		private String tenantId;

		private SysResourceBuilder() {
		}

		public static SysResourceBuilder aSysResource() {
			return new SysResourceBuilder();
		}

		public SysResourceBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysResourceBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public SysResourceBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public SysResourceBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public SysResourceBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public SysResourceBuilder version(int version) {
			this.version = version;
			return this;
		}

		public SysResourceBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public SysResourceBuilder name(String name) {
			this.name = name;
			return this;
		}

		public SysResourceBuilder perms(String perms) {
			this.perms = perms;
			return this;
		}

		public SysResourceBuilder path(String path) {
			this.path = path;
			return this;
		}

		public SysResourceBuilder component(String component) {
			this.component = component;
			return this;
		}

		public SysResourceBuilder parentId(Long parentId) {
			this.parentId = parentId;
			return this;
		}

		public SysResourceBuilder icon(String icon) {
			this.icon = icon;
			return this;
		}

		public SysResourceBuilder keepAlive(Boolean keepAlive) {
			this.keepAlive = keepAlive;
			return this;
		}

		public SysResourceBuilder hidden(Boolean hidden) {
			this.hidden = hidden;
			return this;
		}

		public SysResourceBuilder alwaysShow(Boolean alwaysShow) {
			this.alwaysShow = alwaysShow;
			return this;
		}

		public SysResourceBuilder redirect(String redirect) {
			this.redirect = redirect;
			return this;
		}

		public SysResourceBuilder isFrame(Boolean isFrame) {
			this.isFrame = isFrame;
			return this;
		}

		public SysResourceBuilder sortNum(Integer sortNum) {
			this.sortNum = sortNum;
			return this;
		}

		public SysResourceBuilder type(byte type) {
			this.type = type;
			return this;
		}

		public SysResourceBuilder tenantId(String tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public SysResource build() {
			SysResource sysResource = new SysResource(name, perms, path, component, parentId, icon,
				keepAlive, hidden, alwaysShow, redirect, isFrame, sortNum, type, tenantId);
			sysResource.setId(id);
			sysResource.setCreateBy(createBy);
			sysResource.setLastModifiedBy(lastModifiedBy);
			sysResource.setCreateTime(createTime);
			sysResource.setLastModifiedTime(lastModifiedTime);
			sysResource.setVersion(version);
			sysResource.setDelFlag(delFlag);
			return sysResource;
		}
	}
}
