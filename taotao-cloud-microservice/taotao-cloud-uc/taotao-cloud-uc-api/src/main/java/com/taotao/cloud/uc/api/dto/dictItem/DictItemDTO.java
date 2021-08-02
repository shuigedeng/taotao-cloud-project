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
package com.taotao.cloud.uc.api.dto.dictItem;

import com.taotao.cloud.uc.api.vo.DeptTreeVo.DeptTreeVoBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 添加字典实体对象
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/30 08:49
 */
@Schema(name = "DictItemDTO", description = "添加字典项对象DTO")
public class DictItemDTO implements Serializable {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "字典id", required = true)
	@NotNull(message = "字典id不能为空")
	private Long dictId;

	@Schema(description = "字典项文本", required = true)
	@NotBlank(message = "字典项文本不能为空")
	@Size(max = 1000, message = "字典项文本不能超过1000个字符")
	private String itemText;

	@Schema(description = "字典项值", required = true)
	@NotBlank(message = "字典项值不能为空")
	private String itemValue;

	@Schema(description = "描述")
	private String description;

	@Schema(description = "状态 1不启用 2启用", required = true)
	@NotBlank(message = "字典状态不能为空")
//	@IntEnums(value = {1, 2})
	private Integer status;

	@Override
	public String toString() {
		return "DictItemDTO{" +
			"dictId=" + dictId +
			", itemText='" + itemText + '\'' +
			", itemValue='" + itemValue + '\'' +
			", description='" + description + '\'' +
			", status=" + status +
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
		DictItemDTO that = (DictItemDTO) o;
		return Objects.equals(dictId, that.dictId) && Objects.equals(itemText,
			that.itemText) && Objects.equals(itemValue, that.itemValue)
			&& Objects.equals(description, that.description) && Objects.equals(
			status, that.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dictId, itemText, itemValue, description, status);
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

	public DictItemDTO() {
	}

	public DictItemDTO(Long dictId, String itemText, String itemValue, String description,
		Integer status) {
		this.dictId = dictId;
		this.itemText = itemText;
		this.itemValue = itemValue;
		this.description = description;
		this.status = status;
	}

	public static DictItemDTOBuilder builder() {
		return new DictItemDTOBuilder();
	}



	public static final class DictItemDTOBuilder {

		private Long dictId;
		private String itemText;
		private String itemValue;
		private String description;
		//	@IntEnums(value = {1, 2})
		private Integer status;

		private DictItemDTOBuilder() {
		}

		public static DictItemDTOBuilder aDictItemDTO() {
			return new DictItemDTOBuilder();
		}

		public DictItemDTOBuilder dictId(Long dictId) {
			this.dictId = dictId;
			return this;
		}

		public DictItemDTOBuilder itemText(String itemText) {
			this.itemText = itemText;
			return this;
		}

		public DictItemDTOBuilder itemValue(String itemValue) {
			this.itemValue = itemValue;
			return this;
		}

		public DictItemDTOBuilder description(String description) {
			this.description = description;
			return this;
		}

		public DictItemDTOBuilder status(Integer status) {
			this.status = status;
			return this;
		}

		public DictItemDTO build() {
			DictItemDTO dictItemDTO = new DictItemDTO();
			dictItemDTO.setDictId(dictId);
			dictItemDTO.setItemText(itemText);
			dictItemDTO.setItemValue(itemValue);
			dictItemDTO.setDescription(description);
			dictItemDTO.setStatus(status);
			return dictItemDTO;
		}
	}
}
