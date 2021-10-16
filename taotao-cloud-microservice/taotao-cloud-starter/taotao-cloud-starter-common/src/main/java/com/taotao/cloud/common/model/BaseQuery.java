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
package com.taotao.cloud.common.model;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.cloud.common.utils.AntiSqlFilterUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

/**
 * 通用基础查询
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:09:09
 */
@Schema(name = "BaseQuery", description = "通用基础查询Query")
public class BaseQuery implements Serializable {

	@Serial
	private static final long serialVersionUID = -2483306509077581330L;

	/**
	 * 相同查询参数
	 */
	@Schema(description = "相同查询参数")
	private List<EqDTO> eqQuery;

	/**
	 * 时间范围查询参数
	 */
	@Schema(description = "时间范围查询参数")
	private List<DateTimeBetweenDTO> dateTimeBetweenQuery;

	/**
	 * 排序查询参数
	 */
	@Schema(description = "排序查询参数")
	private List<SortDTO> sortQuery;

	/**
	 * execl查询参数
	 */
	@Schema(description = "execl查询参数")
	private ExeclDTO execlQuery;

	/**
	 * 模糊查询参数
	 */
	@Schema(description = "模糊查询参数")
	private List<LikeDTO> likeQuery;

	/**
	 * 包含查询参数
	 */
	@Schema(description = "包含查询参数")
	private List<InDTO> inQuery;

	/**
	 * 不包含查询参数
	 */
	@Schema(description = "不包含查询参数")
	private List<NotInDTO> notInQuery;

	@Schema(name = "NotInDTO", description = "不包含查询DTO")
	public static class NotInDTO {

		/**
		 * 字段名称
		 */
		@Schema(description = "字段名称")
		private String filed;

		/**
		 * 字段值
		 */
		@Schema(description = "字段值")
		private Object[] values;

		public String getFiled() {
			return filed;
		}

		public void setFiled(String filed) {
			this.filed = filed;
		}

		public Object[] getValues() {
			return values;
		}

		public void setValues(Object[] values) {
			this.values = values;
		}
	}

	@Schema(name = "InDTO", description = "包含查询DTO")
	public static class InDTO {

		/**
		 * 字段名称
		 */
		@Schema(description = "字段名称")
		private String filed;

		/**
		 * 字段值
		 */
		@Schema(description = "字段值")
		private Object[] values;

		public String getFiled() {
			return filed;
		}

		public void setFiled(String filed) {
			this.filed = filed;
		}

		public Object[] getValues() {
			return values;
		}

		public void setValues(Object[] values) {
			this.values = values;
		}
	}

	@Schema(name = "EqDTO", description = "相同参数查询DTO")
	public static class EqDTO {

		/**
		 * 字段名称
		 */
		@Schema(description = "字段名称")
		private String filed;

		/**
		 * 字段值
		 */
		@Schema(description = "字段值")
		private Object value;

		public String getFiled() {
			return filed;
		}

		public void setFiled(String filed) {
			this.filed = filed;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}

	@Schema(name = "LikeDTO", description = "模糊查询DTO")
	public static class LikeDTO {

		/**
		 * 字段名称
		 */
		@Schema(description = "字段名称")
		private String filed;

		/**
		 * 字段值
		 */
		@Schema(description = "字段值")
		private String value;

		public String getFiled() {
			return filed;
		}

		public void setFiled(String filed) {
			this.filed = filed;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}


	@Schema(name = "ExeclDTO", description = "ExeclDTO")
	public static class ExeclDTO {

		/**
		 * 下载文件名称
		 */
		@Schema(description = "下载文件名称")
		private String fileName = "临时文件";

		/**
		 * 标题
		 */
		@Schema(description = "标题")
		private String title = "title";

		/**
		 * 排序
		 */
		@Schema(description = "类型,默认HSSF", allowableValues = "HSSF,XSSF", example = "createTime")
		private String type = "HSSF";

		/**
		 * sheetName
		 */
		@Schema(description = "sheetName")
		private String sheetName = "sheetName";

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSheetName() {
			return sheetName;
		}

		public void setSheetName(String sheetName) {
			this.sheetName = sheetName;
		}
	}

	@Schema(name = "SortDTO", description = "排序DTO")
	public static class SortDTO {

		/**
		 * 排序
		 */
		@Schema(description = "排序字段名称,默认createTime", allowableValues = "id,createTime,updateTime", example = "createTime")
		private String filed = "createTime";

		/**
		 * 排序规则
		 */
		@Schema(description = "排序规则, 默认desc", allowableValues = "desc,asc", example = "desc")
		private String order = "desc";

		public String getFiled() {
			return filed;
		}

		public void setFiled(String filed) {
			this.filed = filed;
		}

		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}
	}

	@Schema(name = "DateTimeBetweenDTO", description = "时间范围DTO")
	public static class DateTimeBetweenDTO {

		/**
		 * 字段名称
		 */
		@Schema(description = "字段名称")
		private String filed;

		/**
		 * 开始时间
		 */
		@Schema(description = "开始时间 时间格式:yyyy-MM-dd HH:mm:ss")
		private LocalDateTime startTime;

		/**
		 * 结束时间
		 */
		@Schema(description = "结束时间 时间格式:yyyy-MM-dd HH:mm:ss")
		private LocalDateTime endTime;

		public String getFiled() {
			return filed;
		}

		public void setFiled(String filed) {
			this.filed = filed;
		}

		public LocalDateTime getStartTime() {
			return startTime;
		}

		public void setStartTime(LocalDateTime startTime) {
			this.startTime = startTime;
		}

		public LocalDateTime getEndTime() {
			return endTime;
		}

		public void setEndTime(LocalDateTime endTime) {
			this.endTime = endTime;
		}
	}

	public List<EqDTO> getEqQuery() {
		return eqQuery;
	}

	public void setEqQuery(List<EqDTO> eqQuery) {
		this.eqQuery = eqQuery;
	}

	public List<DateTimeBetweenDTO> getDateTimeBetweenQuery() {
		return dateTimeBetweenQuery;
	}

	public void setDateTimeBetweenQuery(
		List<DateTimeBetweenDTO> dateTimeBetweenQuery) {
		this.dateTimeBetweenQuery = dateTimeBetweenQuery;
	}

	public List<SortDTO> getSortQuery() {
		return sortQuery;
	}

	public void setSortQuery(List<SortDTO> sortQuery) {
		this.sortQuery = sortQuery;
	}

	public ExeclDTO getExeclQuery() {
		return execlQuery;
	}

	public void setExeclQuery(ExeclDTO execlQuery) {
		this.execlQuery = execlQuery;
	}

	public List<LikeDTO> getLikeQuery() {
		return likeQuery;
	}

	public void setLikeQuery(List<LikeDTO> likeQuery) {
		this.likeQuery = likeQuery;
	}

	public List<InDTO> getInQuery() {
		return inQuery;
	}

	public void setInQuery(List<InDTO> inQuery) {
		this.inQuery = inQuery;
	}

	public List<NotInDTO> getNotInQuery() {
		return notInQuery;
	}

	public void setNotInQuery(List<NotInDTO> notInQuery) {
		this.notInQuery = notInQuery;
	}
}
