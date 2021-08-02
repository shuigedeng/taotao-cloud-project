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

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.IdGeneratorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import org.slf4j.MDC;

/**
 * 返回实体类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/29 15:15
 */
@Schema(description = "返回结果对象")
public class Result<T> implements Serializable {

	private static final long serialVersionUID = -3685249101751401211L;

	@Schema(description = "状态码", required = true)
	private int code;

	@Schema(description = "返回数据")
	private T data;

	@Schema(description = "消息体 error success")
	private String message;

	@Schema(description = "请求id")
	private String requestId;

	@Schema(description = "请求结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;

	public Result() {
	}

	public Result(int code, T data, String message, String requestId, LocalDateTime timestamp) {
		this.code = code;
		this.data = data;
		this.message = message;
		this.requestId = requestId;
		this.timestamp = timestamp;
	}

	public static <T> Result<T> of(int code, T data, String msg) {
		return Result
			.<T>builder()
			.code(code)
			.data(data)
			.message(msg)
			.timestamp(LocalDateTime.now())
			.requestId(StrUtil.isNotBlank(MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID)) ? MDC
				.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID) : IdGeneratorUtil.getIdStr())
			.build();
	}

	public static <T> Result<T> success(T data) {
		return of(ResultEnum.SUCCESS.getCode(), data, CommonConstant.SUCCESS);
	}

	public static <T> Result<T> success(T data, int code) {
		return of(code, data, CommonConstant.SUCCESS);
	}

	public static Result<String> success(String data, ResultEnum resultEnum) {
		return of(resultEnum.getCode(), resultEnum.getData(), CommonConstant.SUCCESS);
	}

	public static Result<String> fail() {
		return of(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getData(), CommonConstant.ERROR);
	}

	public static <T> Result<T> fail(T data) {
		return of(ResultEnum.ERROR.getCode(), data, CommonConstant.ERROR);
	}

	public static <T> Result<T> fail(T data, int code) {
		return of(code, data, CommonConstant.ERROR);
	}

	public static Result<String> fail(ResultEnum resultEnum) {
		return of(resultEnum.getCode(), resultEnum.getData(), CommonConstant.ERROR);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Result{" +
			"code=" + code +
			", data=" + data +
			", message='" + message + '\'' +
			", requestId='" + requestId + '\'' +
			", timestamp=" + timestamp +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Result<?> result = (Result<?>) o;
		return code == result.code && Objects.equals(data, result.data)
			&& Objects.equals(message, result.message) && Objects.equals(
			requestId, result.requestId) && Objects.equals(timestamp, result.timestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, data, message, requestId, timestamp);
	}

	public static <T> ResultBuilder<T> builder() {
		return new ResultBuilder<>();
	}

	public static final class ResultBuilder<T> {

		private int code;
		private T data;
		private String message;
		private String requestId;
		private LocalDateTime timestamp;

		private ResultBuilder() {
		}

		public ResultBuilder<T>  code(int code) {
			this.code = code;
			return this;
		}

		public ResultBuilder<T>  data(T data) {
			this.data = data;
			return this;
		}

		public ResultBuilder<T>  message(String message) {
			this.message = message;
			return this;
		}

		public ResultBuilder<T> requestId(String requestId) {
			this.requestId = requestId;
			return this;
		}

		public ResultBuilder<T> timestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public <T> Result<T> build() {
			Result<T> result = new Result<>();
			result.setCode(code);
			result.setData(data);
			result.setMessage(message);
			result.setRequestId(requestId);
			result.setTimestamp(timestamp);
			return result;
		}
	}
}
