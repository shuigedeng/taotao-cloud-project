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
package com.taotao.cloud.sys.api.vo.region;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * QueryRegionByParentIdVO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:31:45
 */
@Schema( description = "查询应用列表数据VO")
public record RegionParentVO(
	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	Long id,
	/**
	 * 名称
	 */
	@Schema(description = "名称")
	String label,
	/**
	 * 应用名称
	 */
	@Schema(description = "应用名称")
	String value,
	/**
	 * 子数据
	 */
	@Schema(description = "子数据")
	List<RegionParentVO> children) {
}
