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
package com.taotao.cloud.uc.api.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 字典添加对象
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/30 08:49
 */
@Schema(description = "字典添加对象")
public record DictSaveDTO(
	/**
	 * 字典名称
	 */
	@Schema(description = "字典名称", required = true)
	@NotBlank(message = "字典名称不能为空")
	@Size(max = 10, message = "字典名称不能超过10个字符")
	String dictName,

	/**
	 * 字典编码
	 */
	@Schema(description = "字典编码", required = true)
	@NotBlank(message = "字典编码不能为空")
	@Size(max = 10, message = "字典编码不能超过10个字符")
	String dictCode,

	/**
	 * 描述
	 */
	@Schema(description = "描述")
	String description,

	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	Integer dictSort,

	/**
	 * 备注信息
	 */
	@Schema(description = "备注信息")
	String remark) implements Serializable {

	public static final long serialVersionUID = -7605952923416404638L;


}
