/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.web.base.dto;

import com.taotao.cloud.web.validation.constraints.StringEnums;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;

/**
 * 批量操作DTO
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/11 15:07
 */
@Schema(description = "批量操作DTO")
public record BatchDTO<SaveDTO, UpdateDTO, I extends Serializable>(
	/**
	 * 请求方式
	 */
	@Schema(description = "请求方式", allowableValues = {"create", "update", "delete"}, required = true)
	@NotBlank(message = "请求方式不能为空")
	@StringEnums(enumList = {"create", "update", "delete"}, message = "请求方式错误")
	String method,

	/**
	 * 批量添加数据DTO
	 */
	@Schema(description = "批量添加数据DTO")
	List<SaveDTO> batchCreate,

	/**
	 * 批量更新数据DTO
	 */
	@Schema(description = "批量更新数据DTO")
	List<BatchUpdate<UpdateDTO, I>> batchUpdate,

	/**
	 * 批量删除数据DTO
	 */
	@Schema(description = "批量删除数据DTO")
	List<I> batchDelete) implements Serializable {

	@Serial
	private static final long serialVersionUID = -4194344880194881367L;

	/**
	 * BatchUpdate
	 *
	 * @author shuigedeng
	 * @version 2021.10
	 * @since 2021-10-11 15:21:31
	 */
	@Schema(description = "批量更新操作DTO")
	public static record BatchUpdate<UpdateDTO, I extends Serializable>(
		/**
		 * id
		 */
		@Schema(description = "id")
		I id,

		/**
		 * 更新数据对象
		 */
		@Schema(description = "更新数据对象")
		UpdateDTO updateDTO) {

	}
}
