package com.taotao.cloud.uc.api.vo.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 字典VO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(description = "字典VO")
public class DictVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "id")
	private Long id;
	@Schema(description = "字典名称")
	private String dictName;
	@Schema(description = "字典编码")
	private String dictCode;
	@Schema(description = "描述")
	private String description;
	@Schema(description = "排序值")
	private Integer dictSort;
	@Schema(description = "备注信息")
	private String remark;
	@Schema(description = "创建时间")
	private LocalDateTime createTime;
	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;

	public DictVO() {
	}

	public DictVO(Long id, String dictName, String dictCode, String description,
		Integer dictSort, String remark, LocalDateTime createTime,
		LocalDateTime lastModifiedTime) {
		this.id = id;
		this.dictName = dictName;
		this.dictCode = dictCode;
		this.description = description;
		this.dictSort = dictSort;
		this.remark = remark;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}


	@Override
	public String toString() {
		return "DictVO{" +
			"id=" + id +
			", dictName='" + dictName + '\'' +
			", dictCode='" + dictCode + '\'' +
			", description='" + description + '\'' +
			", dictSort=" + dictSort +
			", remark='" + remark + '\'' +
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
		DictVO dictVO = (DictVO) o;
		return Objects.equals(id, dictVO.id) && Objects.equals(dictName,
			dictVO.dictName) && Objects.equals(dictCode, dictVO.dictCode)
			&& Objects.equals(description, dictVO.description)
			&& Objects.equals(dictSort, dictVO.dictSort) && Objects.equals(
			remark, dictVO.remark) && Objects.equals(createTime, dictVO.createTime)
			&& Objects.equals(lastModifiedTime, dictVO.lastModifiedTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, dictName, dictCode, description, dictSort, remark, createTime,
			lastModifiedTime);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDictSort() {
		return dictSort;
	}

	public void setDictSort(Integer dictSort) {
		this.dictSort = dictSort;
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

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public static DictVOBuilder builder() {
		return new DictVOBuilder();
	}


	public static final class DictVOBuilder {

		private Long id;
		private String dictName;
		private String dictCode;
		private String description;
		private Integer dictSort;
		private String remark;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;

		private DictVOBuilder() {
		}

		public static DictVOBuilder aDictVO() {
			return new DictVOBuilder();
		}

		public DictVOBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public DictVOBuilder dictName(String dictName) {
			this.dictName = dictName;
			return this;
		}

		public DictVOBuilder dictCode(String dictCode) {
			this.dictCode = dictCode;
			return this;
		}

		public DictVOBuilder description(String description) {
			this.description = description;
			return this;
		}

		public DictVOBuilder dictSort(Integer dictSort) {
			this.dictSort = dictSort;
			return this;
		}

		public DictVOBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public DictVOBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public DictVOBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public DictVO build() {
			DictVO dictVO = new DictVO();
			dictVO.setId(id);
			dictVO.setDictName(dictName);
			dictVO.setDictCode(dictCode);
			dictVO.setDescription(description);
			dictVO.setDictSort(dictSort);
			dictVO.setRemark(remark);
			dictVO.setCreateTime(createTime);
			dictVO.setLastModifiedTime(lastModifiedTime);
			return dictVO;
		}
	}
}
