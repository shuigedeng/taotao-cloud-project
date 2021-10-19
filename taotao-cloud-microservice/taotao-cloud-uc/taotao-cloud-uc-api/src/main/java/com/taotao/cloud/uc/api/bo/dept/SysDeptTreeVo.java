package com.taotao.cloud.uc.api.bo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "SysDeptTreeVo", description = "部门树VO")
public class SysDeptTreeVo implements Serializable {

	@Serial
	private static final long serialVersionUID = -4546704465269983480L;

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的key")
	private int key;

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的value")
	private String value;

	@Schema(description = "对应depart_name字段,前端数据树中的title")
	private String title;

	@Schema(description = "部门主键ID")
	private Integer deptId;

	@Schema(description = "部门名称")
	private String name;

	@Schema(description = "上级部门")
	private Integer parentId;

	@Schema(description = "排序")
	private Integer sort;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "修改时间")
	private LocalDateTime updateTime;

	@Schema(description = "是否删除  -1：已删除  0：正常")
	private String delFlag;

	@Schema(description = "上级部门")
	private String parentName;

	@Schema(description = "等级")
	private Integer level;

	@Schema(description = "children")
	private List<SysDeptTreeVo> children;

	public SysDeptTreeVo() {
	}

	public SysDeptTreeVo(int key, String value, String title, Integer deptId, String name,
		Integer parentId, Integer sort, String remark, LocalDateTime createTime,
		LocalDateTime updateTime, String delFlag, String parentName, Integer level,
		List<SysDeptTreeVo> children) {
		this.key = key;
		this.value = value;
		this.title = title;
		this.deptId = deptId;
		this.name = name;
		this.parentId = parentId;
		this.sort = sort;
		this.remark = remark;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.delFlag = delFlag;
		this.parentName = parentName;
		this.level = level;
		this.children = children;
	}

	@Override
	public String toString() {
		return "SysDeptTreeVo{" +
			"key=" + key +
			", value='" + value + '\'' +
			", title='" + title + '\'' +
			", deptId=" + deptId +
			", name='" + name + '\'' +
			", parentId=" + parentId +
			", sort=" + sort +
			", remark='" + remark + '\'' +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", delFlag='" + delFlag + '\'' +
			", parentName='" + parentName + '\'' +
			", level=" + level +
			", children=" + children +
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
		SysDeptTreeVo that = (SysDeptTreeVo) o;
		return key == that.key && Objects.equals(value, that.value)
			&& Objects.equals(title, that.title) && Objects.equals(deptId,
			that.deptId) && Objects.equals(name, that.name) && Objects.equals(
			parentId, that.parentId) && Objects.equals(sort, that.sort)
			&& Objects.equals(remark, that.remark) && Objects.equals(createTime,
			that.createTime) && Objects.equals(updateTime, that.updateTime)
			&& Objects.equals(delFlag, that.delFlag) && Objects.equals(
			parentName, that.parentName) && Objects.equals(level, that.level)
			&& Objects.equals(children, that.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value, title, deptId, name, parentId, sort, remark, createTime,
			updateTime, delFlag, parentName, level, children);
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<SysDeptTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<SysDeptTreeVo> children) {
		this.children = children;
	}

	public static SysDeptTreeVoBuilder builder() {
		return new SysDeptTreeVoBuilder();
	}

	public static final class SysDeptTreeVoBuilder {

		private int key;
		private String value;
		private String title;
		private Integer deptId;
		private String name;
		private Integer parentId;
		private Integer sort;
		private String remark;
		private LocalDateTime createTime;
		private LocalDateTime updateTime;
		private String delFlag;
		private String parentName;
		private Integer level;
		private List<SysDeptTreeVo> children;

		private SysDeptTreeVoBuilder() {
		}

		public static SysDeptTreeVoBuilder aSysDeptTreeVo() {
			return new SysDeptTreeVoBuilder();
		}

		public SysDeptTreeVoBuilder key(int key) {
			this.key = key;
			return this;
		}

		public SysDeptTreeVoBuilder value(String value) {
			this.value = value;
			return this;
		}

		public SysDeptTreeVoBuilder title(String title) {
			this.title = title;
			return this;
		}

		public SysDeptTreeVoBuilder deptId(Integer deptId) {
			this.deptId = deptId;
			return this;
		}

		public SysDeptTreeVoBuilder name(String name) {
			this.name = name;
			return this;
		}

		public SysDeptTreeVoBuilder parentId(Integer parentId) {
			this.parentId = parentId;
			return this;
		}

		public SysDeptTreeVoBuilder sort(Integer sort) {
			this.sort = sort;
			return this;
		}

		public SysDeptTreeVoBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public SysDeptTreeVoBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public SysDeptTreeVoBuilder updateTime(LocalDateTime updateTime) {
			this.updateTime = updateTime;
			return this;
		}

		public SysDeptTreeVoBuilder delFlag(String delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public SysDeptTreeVoBuilder parentName(String parentName) {
			this.parentName = parentName;
			return this;
		}

		public SysDeptTreeVoBuilder level(Integer level) {
			this.level = level;
			return this;
		}

		public SysDeptTreeVoBuilder children(List<SysDeptTreeVo> children) {
			this.children = children;
			return this;
		}

		public SysDeptTreeVo build() {
			SysDeptTreeVo sysDeptTreeVo = new SysDeptTreeVo();
			sysDeptTreeVo.setKey(key);
			sysDeptTreeVo.setValue(value);
			sysDeptTreeVo.setTitle(title);
			sysDeptTreeVo.setDeptId(deptId);
			sysDeptTreeVo.setName(name);
			sysDeptTreeVo.setParentId(parentId);
			sysDeptTreeVo.setSort(sort);
			sysDeptTreeVo.setRemark(remark);
			sysDeptTreeVo.setCreateTime(createTime);
			sysDeptTreeVo.setUpdateTime(updateTime);
			sysDeptTreeVo.setDelFlag(delFlag);
			sysDeptTreeVo.setParentName(parentName);
			sysDeptTreeVo.setLevel(level);
			sysDeptTreeVo.setChildren(children);
			return sysDeptTreeVo;
		}
	}
}
