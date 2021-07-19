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
package com.taotao.cloud.uc.api.query.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 字典查询query
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/30 08:49
 */
@Data
@Builder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name = "DictQuery", description = "字典查询query")
public class DictQuery implements Serializable {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "字典名称")
	private String dictName;

	@Schema(description = "字典编码")
	private String dictCode;

	@Schema(description = "描述")
	private String description;

	@Schema(description = "备注信息")
	private String remark;
}
