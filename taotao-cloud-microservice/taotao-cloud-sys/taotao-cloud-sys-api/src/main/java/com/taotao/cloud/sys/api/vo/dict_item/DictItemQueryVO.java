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
package com.taotao.cloud.sys.api.vo.dict_item;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典项查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:32:25
 */
@Schema( description = "字典项查询对象")
public record DictItemQueryVO(
	/**
	 * id
	 */
	@Schema(description = "id")
	Long id,
	/**
	 * 字典id
	 */
	@Schema(description = "字典id")
	Long dictId,
	/**
	 * 字典项文本
	 */
	@Schema(description = "字典项文本")
	String itemText,
	/**
	 * 字典项值
	 */
	@Schema(description = "字典项值")
	String itemValue,
	/**
	 * 描述
	 */
	@Schema(description = "描述")
	String description,
	/**
	 * 状态(1不启用 2启用)
	 */
	@Schema(description = "状态(1不启用 2启用)")
	Integer status,
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	LocalDateTime createTime,
	/**
	 * 最后修改时间
	 */
	@Schema(description = "最后修改时间")
	LocalDateTime lastModifiedTime) implements Serializable {

	@Serial
	static final long serialVersionUID = 5126530068827085130L;


}
