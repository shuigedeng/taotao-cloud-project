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
package com.taotao.cloud.sys.api.dto.region;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * 地区添加对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/9/30 08:49
 */
@Schema(description = "地区添加对象")
public record RegionSaveDTO(
	/**
	 * 地区编码
	 */
	@Schema(description = "地区编码")
	String code,

	/**
	 * 地区名称
	 */
	@Schema(description = "地区名称")
	String name,

	/**
	 * 地区级别（1:省份province,2:市city,3:区县district,4:街道street）
	 */
	@Schema(description = "地区级别（1:省份province,2:市city,3:区县district,4:街道street）")
	Integer level,

	/**
	 * 城市编码
	 */
	@Schema(description = "城市编码")
	String cityCode,

	/**
	 * 城市中心经度
	 */
	@Schema(description = "城市中心经度")
	String lng,

	/**
	 * 城市中心纬度
	 */
	@Schema(description = "城市中心纬度")
	String lat,

	/**
	 * 地区父节点
	 */
	@Schema(description = "地区父节点")
	Long parentId) implements Serializable {

	static final long serialVersionUID = -7605952923416404638L;


}
