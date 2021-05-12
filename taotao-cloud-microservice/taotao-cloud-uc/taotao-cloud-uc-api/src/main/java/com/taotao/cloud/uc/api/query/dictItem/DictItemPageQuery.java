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
package com.taotao.cloud.uc.api.query.dictItem;

import com.taotao.cloud.common.model.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 字典项分页查询query
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/9/30 08:49
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DictItemPageQuery", description = "字典项分页查询query")
public class DictItemPageQuery extends BasePageQuery {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "字典id")
	private Long dictId;
	@Schema(description = "字典项文本")
	private String itemText;
	@Schema(description = "字典项值")
	private String itemValue;
	@Schema(description = "描述")
	private String description;
	@Schema(description = "状态(1不启用 2启用)")
	private Boolean status;
}
