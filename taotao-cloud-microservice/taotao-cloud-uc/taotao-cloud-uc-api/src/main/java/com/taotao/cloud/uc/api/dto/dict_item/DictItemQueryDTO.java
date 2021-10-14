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
package com.taotao.cloud.uc.api.dto.dict_item;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 字典项查询对象
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/30 08:49
 */
@Schema(name = "DictItemQueryDTO", description = "字典项查询对象")
public class DictItemQueryDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	/**
	 * 字典id
	 */
	@Schema(description = "字典id", required = true)
	@NotNull(message = "字典id不能为空")
	private Long dictId;
	/**
	 * 字典项文本
	 */
	@Schema(description = "字典项文本", required = true)
	@NotBlank(message = "字典项文本不能为空")
	@Size(max = 1000, message = "字典项文本不能超过1000个字符")
	private String itemText;
	/**
	 * 字典项值
	 */
	@Schema(description = "字典项值", required = true)
	@NotBlank(message = "字典项值不能为空")
	private String itemValue;
	/**
	 * 描述
	 */
	@Schema(description = "描述")
	private String description;

	/**
	 * 字典状态 1不启用 2启用
	 */
	@Schema(description = "字典状态 1不启用 2启用", required = true)
	@NotBlank(message = "字典状态不能为空")
	//@IntEnums(value = {1, 2})
	private Integer status;

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

	public DictItemQueryDTO() {
	}

	public DictItemQueryDTO(Long dictId, String itemText, String itemValue, String description,
		Integer status) {
		this.dictId = dictId;
		this.itemText = itemText;
		this.itemValue = itemValue;
		this.description = description;
		this.status = status;
	}

}
