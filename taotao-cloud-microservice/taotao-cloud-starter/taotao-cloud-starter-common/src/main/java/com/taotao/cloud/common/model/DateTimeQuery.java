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

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 基础时间查询
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:07:59
 */
@Schema(name = "DateTimeQuery", description = "基础时间查询对象")
public class DateTimeQuery implements Serializable {

	private static final long serialVersionUID = -2483306509077581330L;

	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间 时间格式:yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@Schema(description = "结束时间 时间格式:yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

	public DateTimeQuery() {
	}

	public DateTimeQuery(LocalDateTime startTime, LocalDateTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
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

	/**
	 * builder
	 *
	 * @return {@link BaseDateTimeQueryBuilder }
	 * @author shuigedeng
	 * @since 2021-09-02 19:08:24
	 */
	public static BaseDateTimeQueryBuilder builder() {
		return new BaseDateTimeQueryBuilder();
	}

	public static final class BaseDateTimeQueryBuilder {

		private LocalDateTime startTime;
		private LocalDateTime endTime;

		private BaseDateTimeQueryBuilder() {
		}

		public static BaseDateTimeQueryBuilder aBaseDateTimeQuery() {
			return new BaseDateTimeQueryBuilder();
		}

		public BaseDateTimeQueryBuilder startTime(LocalDateTime startTime) {
			this.startTime = startTime;
			return this;
		}

		public BaseDateTimeQueryBuilder endTime(LocalDateTime endTime) {
			this.endTime = endTime;
			return this;
		}

		public DateTimeQuery build() {
			DateTimeQuery dateTimeQuery = new DateTimeQuery();
			dateTimeQuery.setStartTime(startTime);
			dateTimeQuery.setEndTime(endTime);
			return dateTimeQuery;
		}
	}
}
