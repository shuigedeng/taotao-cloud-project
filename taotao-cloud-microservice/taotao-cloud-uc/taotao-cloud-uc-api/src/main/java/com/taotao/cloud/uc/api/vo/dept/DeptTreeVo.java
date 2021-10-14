package com.taotao.cloud.uc.api.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;

/**
 * 构建部门树vo
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@Schema(name = "DeptTreeVo", description = "部门树VO")
public class DeptTreeVo {

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的key")
	private Long key;

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的value")
	private String value;

	@Schema(description = "对应depart_name字段,前端数据树中的title")
	private String title;

	@Schema(description = "部门树children")
	private List<DeptTreeVo> children;

	public DeptTreeVo() {
	}

	public DeptTreeVo(Long key, String value, String title,
		List<DeptTreeVo> children) {
		this.key = key;
		this.value = value;
		this.title = title;
		this.children = children;
	}

	@Override
	public String
	toString() {
		return "DeptTreeVo{" +
			"key=" + key +
			", value='" + value + '\'' +
			", title='" + title + '\'' +
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
		DeptTreeVo that = (DeptTreeVo) o;
		return Objects.equals(key, that.key) && Objects.equals(value, that.value)
			&& Objects.equals(title, that.title) && Objects.equals(children,
			that.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value, title, children);
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
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

	public List<DeptTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<DeptTreeVo> children) {
		this.children = children;
	}

	public static DeptTreeVoBuilder builder() {
		return new DeptTreeVoBuilder();
	}

	public static final class DeptTreeVoBuilder {

		private Long key;
		private String value;
		private String title;
		private List<DeptTreeVo> children;

		private DeptTreeVoBuilder() {
		}

		public static DeptTreeVoBuilder aDeptTreeVo() {
			return new DeptTreeVoBuilder();
		}

		public DeptTreeVoBuilder key(Long key) {
			this.key = key;
			return this;
		}

		public DeptTreeVoBuilder value(String value) {
			this.value = value;
			return this;
		}

		public DeptTreeVoBuilder title(String title) {
			this.title = title;
			return this;
		}

		public DeptTreeVoBuilder children(List<DeptTreeVo> children) {
			this.children = children;
			return this;
		}

		public DeptTreeVo build() {
			DeptTreeVo deptTreeVo = new DeptTreeVo();
			deptTreeVo.setKey(key);
			deptTreeVo.setValue(value);
			deptTreeVo.setTitle(title);
			deptTreeVo.setChildren(children);
			return deptTreeVo;
		}
	}
}
