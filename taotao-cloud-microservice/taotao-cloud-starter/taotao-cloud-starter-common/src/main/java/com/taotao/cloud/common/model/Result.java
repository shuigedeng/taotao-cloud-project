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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@JsonInclude(Include.ALWAYS)
public class Result<T> implements Serializable {

	private static final long serialVersionUID = -3685249101751401211L;

	/**
	 * 状态码
	 */
	@Schema(description = "状态码", required = true)
	private int code;

	/**
	 * 返回数据
	 */
	@Schema(description = "返回数据")
	private T data;

	/**
	 * 是否成功
	 */
	@Schema(description = "是否成功")
	private boolean success;

	/**
	 * 异常消息体
	 */
	@Schema(description = "异常消息体")
	private String errorMsg;

	/**
	 * 请求id
	 */
	@Schema(description = "请求id")
	private String requestId;

	/**
	 * 请求结束时间
	 */
	@Schema(description = "请求结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;

	public Result() {
	}

	public Result(int code, T data, boolean success, String errorMsg, String requestId,
		LocalDateTime timestamp) {
		this.code = code;
		this.data = data;
		this.success = success;
		this.errorMsg = errorMsg;
		this.requestId = requestId;
		this.timestamp = timestamp;
	}


	/**
	 * of
	 *
	 * @param code     code
	 * @param data     data
	 * @param success  success
	 * @param errorMsg errorMsg
	 * @param <T>      T
	 * @return {@link Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:12:35
	 */
	public static <T> Result<T> of(int code, T data, boolean success, String errorMsg) {
		return Result
			.<T>builder()
			.code(code)
			.data(data)
			.success(success)
			.errorMsg(errorMsg)
			.timestamp(LocalDateTime.now())
			.requestId(StrUtil.isNotBlank(MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID)) ? MDC
				.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID) : IdGeneratorUtil.getIdStr())
			.build();
	}

	/**
	 * success
	 *
	 * @param data data
	 * @param <T>  T
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:15:21
	 */
	public static <T> Result<T> success(T data) {
		return of(ResultEnum.SUCCESS.getCode(), data, CommonConstant.SUCCESS, "");
	}

	/**
	 * success
	 *
	 * @param <T> T
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:15:07
	 */
	public static <T> Result<T> success() {
		return of(ResultEnum.SUCCESS.getCode(), null, CommonConstant.SUCCESS, "");
	}

	/**
	 * success
	 *
	 * @param data data
	 * @param code code
	 * @param <T>  T
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:14:58
	 */
	public static <T> Result<T> success(T data, int code) {
		return of(code, data, CommonConstant.SUCCESS, "");
	}

	/**
	 * success
	 *
	 * @param data       data
	 * @param resultEnum resultEnum
	 * @return {@link Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:13:07
	 */
	public static Result<String> success(String data, ResultEnum resultEnum) {
		return of(resultEnum.getCode(), resultEnum.getData(), CommonConstant.SUCCESS, "");
	}

	/**
	 * fail
	 *
	 * @return {@link Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:13:14
	 */
	public static Result<String> fail() {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR,
			ResultEnum.ERROR.getData());
	}

	/**
	 * fail
	 *
	 * @param errorMsg errorMsg
	 * @param <T>      T
	 * @return {@link Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:13:19
	 */
	public static <T> Result<T> fail(String errorMsg) {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR, errorMsg);
	}

	/**
	 * fail
	 *
	 * @param data data
	 * @param code code
	 * @param <T>  T
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:14:41
	 */
	public static <T> Result<T> fail(String data, int code) {
		return of(code, null, CommonConstant.ERROR, data);
	}

	/**
	 * fail
	 *
	 * @param resultEnum resultEnum
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:14:32
	 */
	public static Result<String> fail(ResultEnum resultEnum) {
		return of(resultEnum.getCode(), null, CommonConstant.ERROR, resultEnum.getData());
	}

	/**
	 * fail
	 *
	 * @param throwable throwable
	 * @param <T>       T
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:14:24
	 */
	public static <T> Result<T> fail(Throwable throwable) {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR, throwable.getMessage());
	}

	/**
	 * validFail
	 *
	 * @param resultEnum resultEnum
	 * @param <T>        T
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:14:18
	 */
	public static <T> Result<T> validFail(ResultEnum resultEnum) {
		return of(resultEnum.getCode(), null, CommonConstant.ERROR, resultEnum.getData());
	}


	/**
	 * validFail
	 *
	 * @param errorMsg errorMsg
	 * @param args     args
	 * @param <T>      T
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:14:11
	 */
	public static <T> Result<T> validFail(String errorMsg, Object... args) {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR, errorMsg);
	}

	/**
	 * validFail
	 *
	 * @param errorMsg errorMsg
	 * @param <T>      T
	 * @return {@link com.taotao.cloud.common.model.Result }
	 * @author shuigedeng
	 * @since 2021-09-02 19:14:03
	 */
	public static <T> Result<T> validFail(String errorMsg) {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR, errorMsg);
	}

	@Override
	public String toString() {
		return "Result{" +
			"code=" + code +
			", data=" + data +
			", success=" + success +
			", errorMsg='" + errorMsg + '\'' +
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
		return code == result.code && success == result.success && Objects.equals(data,
			result.data) && Objects.equals(errorMsg, result.errorMsg)
			&& Objects.equals(requestId, result.requestId) && Objects.equals(
			timestamp, result.timestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, data, success, errorMsg, requestId, timestamp);
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

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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

	public static ResultBuilder builder() {
		return new ResultBuilder();
	}

	public static final class ResultBuilder<T> {

		private int code;
		private T data;
		private boolean success;
		private String errorMsg;
		private String requestId;
		private LocalDateTime timestamp;

		private ResultBuilder() {
		}

		public ResultBuilder<T> code(int code) {
			this.code = code;
			return this;
		}

		public ResultBuilder data(T data) {
			this.data = data;
			return this;
		}

		public ResultBuilder success(boolean success) {
			this.success = success;
			return this;
		}

		public ResultBuilder errorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
			return this;
		}

		public ResultBuilder requestId(String requestId) {
			this.requestId = requestId;
			return this;
		}

		public ResultBuilder timestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Result build() {
			Result result = new Result();
			result.setCode(code);
			result.setData(data);
			result.setSuccess(success);
			result.setErrorMsg(errorMsg);
			result.setRequestId(requestId);
			result.setTimestamp(timestamp);
			return result;
		}
	}
}
