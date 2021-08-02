package com.taotao.cloud.uc.api.vo.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 资源VO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
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

	public ResourceVO() {
	}

	public ResourceVO(Long id, String name, Byte type, String perms, String path,
		String component, Long parentId, String icon, Boolean keepAlive, Boolean hidden,
		Boolean alwaysShow, String redirect, Boolean isFrame, Integer sortNum,
		LocalDateTime createTime, LocalDateTime lastModifiedTime) {
		this.id = id;
		this.name = name;
		this.type = type;
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
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	@Override
	public String toString() {
		return "ResourceVO{" +
			"id=" + id +
			", name='" + name + '\'' +
			", type=" + type +
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
			", createTime=" + createTime +
			", lastModifiedTime=" + lastModifiedTime +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ResourceVO that = (ResourceVO) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name)
			&& Objects.equals(type, that.type) && Objects.equals(perms,
			that.perms) && Objects.equals(path, that.path) && Objects.equals(
			component, that.component) && Objects.equals(parentId, that.parentId)
			&& Objects.equals(icon, that.icon) && Objects.equals(keepAlive,
			that.keepAlive) && Objects.equals(hidden, that.hidden)
			&& Objects.equals(alwaysShow, that.alwaysShow) && Objects.equals(
			redirect, that.redirect) && Objects.equals(isFrame, that.isFrame)
			&& Objects.equals(sortNum, that.sortNum) && Objects.equals(
			createTime, that.createTime) && Objects.equals(lastModifiedTime,
			that.lastModifiedTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, type, perms, path, component, parentId, icon, keepAlive,
			hidden,
			alwaysShow, redirect, isFrame, sortNum, createTime, lastModifiedTime);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
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

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public static ResourceVOBuilder builder() {
		return new ResourceVOBuilder();
	}


	public static final class ResourceVOBuilder {

		private Long id;
		private String name;
		private Byte type;
		private String perms;
		private String path;
		private String component;
		private Long parentId;
		private String icon;
		private Boolean keepAlive;
		private Boolean hidden;
		private Boolean alwaysShow;
		private String redirect;
		private Boolean isFrame;
		private Integer sortNum;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;

		private ResourceVOBuilder() {
		}

		public static ResourceVOBuilder aResourceVO() {
			return new ResourceVOBuilder();
		}

		public ResourceVOBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public ResourceVOBuilder name(String name) {
			this.name = name;
			return this;
		}

		public ResourceVOBuilder type(Byte type) {
			this.type = type;
			return this;
		}

		public ResourceVOBuilder perms(String perms) {
			this.perms = perms;
			return this;
		}

		public ResourceVOBuilder path(String path) {
			this.path = path;
			return this;
		}

		public ResourceVOBuilder component(String component) {
			this.component = component;
			return this;
		}

		public ResourceVOBuilder parentId(Long parentId) {
			this.parentId = parentId;
			return this;
		}

		public ResourceVOBuilder icon(String icon) {
			this.icon = icon;
			return this;
		}

		public ResourceVOBuilder keepAlive(Boolean keepAlive) {
			this.keepAlive = keepAlive;
			return this;
		}

		public ResourceVOBuilder hidden(Boolean hidden) {
			this.hidden = hidden;
			return this;
		}

		public ResourceVOBuilder alwaysShow(Boolean alwaysShow) {
			this.alwaysShow = alwaysShow;
			return this;
		}

		public ResourceVOBuilder redirect(String redirect) {
			this.redirect = redirect;
			return this;
		}

		public ResourceVOBuilder isFrame(Boolean isFrame) {
			this.isFrame = isFrame;
			return this;
		}

		public ResourceVOBuilder sortNum(Integer sortNum) {
			this.sortNum = sortNum;
			return this;
		}

		public ResourceVOBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public ResourceVOBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public ResourceVO build() {
			return new ResourceVO(id, name, type, perms, path, component, parentId, icon, keepAlive,
				hidden, alwaysShow, redirect, isFrame, sortNum, createTime, lastModifiedTime);
		}
	}
}

