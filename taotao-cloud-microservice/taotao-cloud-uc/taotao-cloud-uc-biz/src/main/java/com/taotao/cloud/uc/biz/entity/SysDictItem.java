package com.taotao.cloud.uc.biz.entity;// package com.taotao.cloud.uc.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import com.taotao.cloud.uc.biz.entity.SysDict.SysDictBuilder;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 字典项表
 *
 * // @SQLDelete(sql = "update sys_dict_item set del_flag = 1 where id = ?")
 * // @Where(clause = "del_flag = 1")
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@Entity
@Table(name = "tt_sys_dict_item")
@org.hibernate.annotations.Table(appliesTo = "tt_sys_dict_item", comment = "字典项表")
public class SysDictItem extends BaseEntity {

	/**
	 * 字典id
	 *
	 * @see SysDict
	 */
	@Column(name = "dict_id", nullable = false, columnDefinition = "bigint not null comment '字典id'")
	private Long dictId;

	/**
	 * 字典项文本
	 */
	@Column(name = "item_text", nullable = false, columnDefinition = "varchar(2000) not null comment '字典项文本'")
	private String itemText;

	/**
	 * 字典项值
	 */
	@Column(name = "item_value", nullable = false, columnDefinition = "varchar(2000) not null comment '字典项文本'")
	private String itemValue;

	/**
	 * 描述
	 */
	@Column(name = "description", columnDefinition = "varchar(255) comment '描述'")
	private String description;

	/**
	 * 状态 0不启用 1启用 默认值(1)
	 */
	@Column(name = "status", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '状态 0不启用 1启用 默认值(1)'")
	private Boolean status = true;

	/**
	 * 排序值
	 */
	@Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
	private Integer sortNum = 0;

	public SysDictItem() {
	}

	public SysDictItem(Long dictId, String itemText, String itemValue, String description,
		Boolean status, Integer sortNum) {
		this.dictId = dictId;
		this.itemText = itemText;
		this.itemValue = itemValue;
		this.description = description;
		this.status = status;
		this.sortNum = sortNum;
	}

	public SysDictItem(Long id, Long createBy, Long lastModifiedBy, LocalDateTime createTime,
		LocalDateTime lastModifiedTime, int version, Boolean delFlag,
		Long dictId, String itemText, String itemValue, String description,
		Boolean status, Integer sortNum) {
		super(id, createBy, lastModifiedBy, createTime, lastModifiedTime, version, delFlag);
		this.dictId = dictId;
		this.itemText = itemText;
		this.itemValue = itemValue;
		this.description = description;
		this.status = status;
		this.sortNum = sortNum;
	}

	@Override
	public String toString() {
		return "SysDictItem{" +
			"dictId=" + dictId +
			", itemText='" + itemText + '\'' +
			", itemValue='" + itemValue + '\'' +
			", description='" + description + '\'' +
			", status=" + status +
			", sortNum=" + sortNum +
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
		SysDictItem that = (SysDictItem) o;
		return Objects.equals(dictId, that.dictId) && Objects.equals(itemText,
			that.itemText) && Objects.equals(itemValue, that.itemValue)
			&& Objects.equals(description, that.description) && Objects.equals(
			status, that.status) && Objects.equals(sortNum, that.sortNum);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), dictId, itemText, itemValue, description, status,
			sortNum);
	}

	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public String getItemText() {
		return itemText;
	}

	public void setItemText(String itemText) {
		this.itemText = itemText;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public static SysDictItemBuilder builder() {
		return new SysDictItemBuilder();
	}
	public static final class SysDictItemBuilder {

		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;
		private Long dictId;
		private String itemText;
		private String itemValue;
		private String description;
		private Boolean status = true;
		private Integer sortNum = 0;

		private SysDictItemBuilder() {
		}

		public static SysDictItemBuilder aSysDictItem() {
			return new SysDictItemBuilder();
		}

		public SysDictItemBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public SysDictItemBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public SysDictItemBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public SysDictItemBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public SysDictItemBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public SysDictItemBuilder version(int version) {
			this.version = version;
			return this;
		}

		public SysDictItemBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public SysDictItemBuilder dictId(Long dictId) {
			this.dictId = dictId;
			return this;
		}

		public SysDictItemBuilder itemText(String itemText) {
			this.itemText = itemText;
			return this;
		}

		public SysDictItemBuilder itemValue(String itemValue) {
			this.itemValue = itemValue;
			return this;
		}

		public SysDictItemBuilder description(String description) {
			this.description = description;
			return this;
		}

		public SysDictItemBuilder status(Boolean status) {
			this.status = status;
			return this;
		}

		public SysDictItemBuilder sortNum(Integer sortNum) {
			this.sortNum = sortNum;
			return this;
		}

		public SysDictItem build() {
			SysDictItem sysDictItem = new SysDictItem();
			sysDictItem.setId(id);
			sysDictItem.setCreateBy(createBy);
			sysDictItem.setLastModifiedBy(lastModifiedBy);
			sysDictItem.setCreateTime(createTime);
			sysDictItem.setLastModifiedTime(lastModifiedTime);
			sysDictItem.setVersion(version);
			sysDictItem.setDelFlag(delFlag);
			sysDictItem.setDictId(dictId);
			sysDictItem.setItemText(itemText);
			sysDictItem.setItemValue(itemValue);
			sysDictItem.setDescription(description);
			sysDictItem.setStatus(status);
			sysDictItem.setSortNum(sortNum);
			return sysDictItem;
		}
	}
}
