/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 通用基础查询
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:09:09
 */
@Schema(name = "BaseQuery", description = "通用基础查询Query")
public record BaseQuery(
	/**
	 * 相同查询参数
	 */
	@Schema(description = "相同查询参数")
	List<EqDTO> eqQuery,
	/**
	 * 时间范围查询参数
	 */
	@Schema(description = "时间范围查询参数")
	List<DateTimeBetweenDTO> dateTimeBetweenQuery,
	/**
	 * 排序查询参数
	 */
	@Schema(description = "排序查询参数")
	List<SortDTO> sortQuery,
	/**
	 * execl查询参数
	 */
	@Schema(description = "execl查询参数")
	ExeclDTO execlQuery,
	/**
	 * 模糊查询参数
	 */
	@Schema(description = "模糊查询参数")
	List<LikeDTO> likeQuery,
	/**
	 * 包含查询参数
	 */
	@Schema(description = "包含查询参数")
	List<InDTO> inQuery,
	/**
	 * 不包含查询参数
	 */
	@Schema(description = "不包含查询参数")
	List<NotInDTO> notInQuery) implements Serializable {

	static final long serialVersionUID = -2483306509077581330L;

	@Schema(name = "NotInDTO", description = "不包含查询DTO")
	public static record NotInDTO(
		/**
		 * 字段名称
		 */
		@Schema(description = "字段名称")
		String filed,

		/**
		 * 字段值
		 */
		@Schema(description = "字段值")
		Object[] values) {

	}

	@Schema(name = "InDTO", description = "包含查询DTO")
	public static record InDTO(
		/**
		 * 字段名称
		 */
		@Schema(description = "字段名称")
		String filed,

		/**
		 * 字段值
		 */
		@Schema(description = "字段值")
		Object[] values) {
	}

	@Schema(name = "EqDTO", description = "相同参数查询DTO")
	public static record EqDTO(
		/**
		 * 字段名称
		 */
		@Schema(description = "字段名称")
		String filed,

		/**
		 * 字段值
		 */
		@Schema(description = "字段值")
		Object value) {

	}

	@Schema(name = "LikeDTO", description = "模糊查询DTO")
	public static record LikeDTO(
		/**
		 * 字段名称
		 */
		@Schema(description = "字段名称")
		String filed,

		/**
		 * 字段值
		 */
		@Schema(description = "字段值")
		String value) {

	}


	@Schema(name = "ExeclDTO", description = "ExeclDTO")
	public static record ExeclDTO(
		/**
		 * 下载文件名称
		 */
		@Schema(description = "下载文件名称")
		String fileName,
		/**
		 * 标题
		 */
		@Schema(description = "标题")
		String title,
		/**
		 * 排序
		 */
		@Schema(description = "类型,默认HSSF", allowableValues = "HSSF,XSSF", example = "createTime")
		String type,
		/**
		 * sheetName
		 */
		@Schema(description = "sheetName")
		String sheetName) {

	}

	@Schema(name = "SortDTO", description = "排序DTO")
	public static record SortDTO(
		/**
		 * 排序
		 */
		@Schema(description = "排序字段名称,默认createTime", allowableValues = "id,createTime,updateTime", example = "createTime")
		String filed,

		/**
		 * 排序规则
		 */
		@Schema(description = "排序规则, 默认desc", allowableValues = "desc,asc", example = "desc")
		String order) {

	}

	@Schema(name = "DateTimeBetweenDTO", description = "时间范围DTO")
	public static record DateTimeBetweenDTO(
		/**
		 * 字段名称
		 */
		@Schema(description = "字段名称")
		String filed,

		/**
		 * 开始时间
		 */
		@Schema(description = "开始时间 时间格式:yyyy-MM-dd HH:mm:ss")
		LocalDateTime startTime,

		/**
		 * 结束时间
		 */
		@Schema(description = "结束时间 时间格式:yyyy-MM-dd HH:mm:ss")
		LocalDateTime endTime) {

	}
}
