/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.uc.api.vo.dictItem;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 字典项VO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:32:25
 */
@Schema(name = "DictItemVO", description = "字典项VO")
public class DictItemVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;
	/**
	 * id
	 */
	@Schema(description = "id")
	private Long id;
	/**
	 * 字典id
	 */
	@Schema(description = "字典id")
	private Long dictId;
	/**
	 * 字典项文本
	 */
	@Schema(description = "字典项文本")
	private String itemText;
	/**
	 * 字典项值
	 */
	@Schema(description = "字典项值")
	private String itemValue;
	/**
	 * 描述
	 */
	@Schema(description = "描述")
	private String description;
	/**
	 * 状态(1不启用 2启用)
	 */
	@Schema(description = "状态(1不启用 2启用)")
	private Integer status;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 最后修改时间
	 */
	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;

	public DictItemVO() {
	}

	public DictItemVO(Long id, Long dictId, String itemText, String itemValue,
		String description, Integer status, LocalDateTime createTime,
		LocalDateTime lastModifiedTime) {
		this.id = id;
		this.dictId = dictId;
		this.itemText = itemText;
		this.itemValue = itemValue;
		this.description = description;
		this.status = status;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	@Override
	public String toString() {
		return "DictItemVO{" +
			"id=" + id +
			", dictId=" + dictId +
			", itemText='" + itemText + '\'' +
			", itemValue='" + itemValue + '\'' +
			", description='" + description + '\'' +
			", status=" + status +
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
		DictItemVO that = (DictItemVO) o;
		return Objects.equals(id, that.id) && Objects.equals(dictId, that.dictId)
			&& Objects.equals(itemText, that.itemText) && Objects.equals(
			itemValue, that.itemValue) && Objects.equals(description, that.description)
			&& Objects.equals(status, that.status) && Objects.equals(createTime,
			that.createTime) && Objects.equals(lastModifiedTime, that.lastModifiedTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, dictId, itemText, itemValue, description, status, createTime,
			lastModifiedTime);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public static DictItemVOBuilder builder() {
		return new DictItemVOBuilder();
	}


	public static final class DictItemVOBuilder {

		private Long id;
		private Long dictId;
		private String itemText;
		private String itemValue;
		private String description;
		private Integer status;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;

		private DictItemVOBuilder() {
		}

		public static DictItemVOBuilder aDictItemVO() {
			return new DictItemVOBuilder();
		}

		public DictItemVOBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public DictItemVOBuilder dictId(Long dictId) {
			this.dictId = dictId;
			return this;
		}

		public DictItemVOBuilder itemText(String itemText) {
			this.itemText = itemText;
			return this;
		}

		public DictItemVOBuilder itemValue(String itemValue) {
			this.itemValue = itemValue;
			return this;
		}

		public DictItemVOBuilder description(String description) {
			this.description = description;
			return this;
		}

		public DictItemVOBuilder status(Integer status) {
			this.status = status;
			return this;
		}

		public DictItemVOBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public DictItemVOBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public DictItemVO build() {
			DictItemVO dictItemVO = new DictItemVO();
			dictItemVO.setId(id);
			dictItemVO.setDictId(dictId);
			dictItemVO.setItemText(itemText);
			dictItemVO.setItemValue(itemValue);
			dictItemVO.setDescription(description);
			dictItemVO.setStatus(status);
			dictItemVO.setCreateTime(createTime);
			dictItemVO.setLastModifiedTime(lastModifiedTime);
			return dictItemVO;
		}
	}
}
