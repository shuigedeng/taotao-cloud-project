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
package com.taotao.cloud.uc.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ApplicationDTO
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/03/12 16:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "QueryRegionByParentIdVO", description = "查询应用列表数据VO")
public class QueryRegionByParentIdVO {

	@Schema(description = "主键ID")
	private String id;

	@Schema(description = "名称")
	private String label;

	@Schema(description = "应用名称")
	private String value;

	@Schema(description = "子数据")
	private List<QueryRegionByParentIdVO> children;
}
