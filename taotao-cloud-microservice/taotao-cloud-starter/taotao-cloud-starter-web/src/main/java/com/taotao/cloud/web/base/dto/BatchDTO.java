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
package com.taotao.cloud.web.base.dto;

import com.taotao.cloud.web.mvc.constraints.StringEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;

/**
 * 批量操作DTO
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/10/11 15:07
 */
@Schema(name = "BatchDTO", description = "批量操作DTO")
public class BatchDTO<SaveDTO, UpdateDTO, I extends Serializable> implements
	Serializable {

	@Serial
	private static final long serialVersionUID = -4194344880194881367L;

	/**
	 * 请求方式
	 */
	@Schema(description = "请求方式", allowableValues = {"create", "update", "delete"}, required = true)
	@NotBlank(message = "请求方式不能为空")
	@StringEnums(enumList = {"create", "update", "delete"}, message = "请求方式错误")
	private String method;

	/**
	 * 批量添加数据DTO
	 */
	@Schema(description = "批量添加数据DTO")
	private List<SaveDTO> batchCreate;

	/**
	 * 批量更新数据DTO
	 */
	@Schema(description = "批量更新数据DTO")
	private List<BatchUpdate<UpdateDTO, I>> batchUpdate;

	/**
	 * 批量删除数据DTO
	 */
	@Schema(description = "批量删除数据DTO")
	private List<I> batchDelete;


	/**
	 * BatchUpdate
	 *
	 * @author shuigedeng
	 * @version 2021.10
	 * @since 2021-10-11 15:21:31
	 */
	@Schema(name = "BatchUpdate", description = "批量更新操作DTO")
	public static class BatchUpdate<UpdateDTO, I extends Serializable> {

		/**
		 * id
		 */
		@Schema(description = "id")
		private I id;

		/**
		 * 更新数据对象
		 */
		@Schema(description = "更新数据对象")
		private UpdateDTO updateDTO;


		public I getId() {
			return id;
		}

		public void setId(I id) {
			this.id = id;
		}

		public UpdateDTO getUpdateDTO() {
			return updateDTO;
		}

		public void setUpdateDTO(UpdateDTO updateDTO) {
			this.updateDTO = updateDTO;
		}
	}


	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<SaveDTO> getBatchCreate() {
		return batchCreate;
	}

	public void setBatchCreate(List<SaveDTO> batchCreate) {
		this.batchCreate = batchCreate;
	}

	public List<BatchUpdate<UpdateDTO, I>> getBatchUpdate() {
		return batchUpdate;
	}

	public void setBatchUpdate(
		List<BatchUpdate<UpdateDTO, I>> batchUpdate) {
		this.batchUpdate = batchUpdate;
	}

	public List<I> getBatchDelete() {
		return batchDelete;
	}

	public void setBatchDelete(List<I> batchDelete) {
		this.batchDelete = batchDelete;
	}
}
