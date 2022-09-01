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
package com.taotao.cloud.common.model;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.common.IdGeneratorUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.MDC;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 返回实体类
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 15:15
 */
@Schema(description = "返回结果对象")
public class ResultClass<T> implements Serializable {
	/**
	 * 状态码
	 */
	@Schema(description = "状态码")
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
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;

	@Serial
	private static final long serialVersionUID = -3685249101751401211L;

	/**
	 * 成功返回
	 *
	 * @param data 数据
	 * @return 返回结果
	 * @since 2021-09-02 19:15:21
	 */
	public static <T> ResultClass<T> success(T data) {
		return of(ResultEnum.SUCCESS.getCode(), data, CommonConstant.SUCCESS, "");
	}

	/**
	 * 成功返回
	 *
	 * @param data 数据
	 * @param code code
	 * @return 返回结果
	 * @since 2021-09-02 19:14:58
	 */
	public static <T> ResultClass<T> success(T data, int code) {
		return of(code, data, CommonConstant.SUCCESS, "");
	}

	/**
	 * 成功返回
	 *
	 * @param resultEnum 枚举
	 * @return 返回结果
	 * @since 2021-09-02 19:13:07
	 */
	public static ResultClass<String> success(ResultEnum resultEnum) {
		return of(resultEnum.getCode(), resultEnum.getDesc(), CommonConstant.SUCCESS, "");
	}

	/**
	 * 失败返回
	 *
	 * @return 返回结果
	 * @since 2021-09-02 19:13:14
	 */
	public static ResultClass<String> fail() {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR,
			ResultEnum.ERROR.getDesc());
	}

	/**
	 * 失败返回
	 *
	 * @param errorMsg 失败消息
	 * @return 返回结果
	 * @since 2021-09-02 19:13:19
	 */
	public static <T> ResultClass<T> fail(String errorMsg) {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR, errorMsg);
	}

	/**
	 * 失败返回
	 *
	 * @param data 数据
	 * @param code code
	 * @return 返回结果
	 * @since 2021-09-02 19:14:41
	 */
	public static <T> ResultClass<T> fail(String data, int code) {
		return of(code, null, CommonConstant.ERROR, data);
	}

	/**
	 * 失败返回
	 *
	 * @param resultEnum 枚举
	 * @return 返回结果
	 * @since 2021-09-02 19:14:32
	 */
	public static ResultClass<String> fail(ResultEnum resultEnum) {
		return of(resultEnum.getCode(), null, CommonConstant.ERROR, resultEnum.getDesc());
	}

	/**
	 * 失败返回
	 *
	 * @param throwable 异常信息
	 * @return 返回结果
	 * @since 2021-09-02 19:14:24
	 */
	public static <T> ResultClass<T> fail(Throwable throwable) {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR, throwable.getMessage());
	}

	/**
	 * 校验失败返回
	 *
	 * @param resultEnum 枚举
	 * @return 返回结果
	 * @since 2021-09-02 19:14:18
	 */
	public static <T> ResultClass<T> validFail(ResultEnum resultEnum) {
		return of(resultEnum.getCode(), null, CommonConstant.ERROR, resultEnum.getDesc());
	}

	/**
	 * 校验失败返回
	 *
	 * @param errorMsg 失败消息
	 * @param args     参数
	 * @return 返回结果
	 * @since 2021-09-02 19:14:11
	 */
	public static <T> ResultClass<T> validFail(String errorMsg, Object... args) {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR, errorMsg);
	}

	/**
	 * 校验失败返回
	 *
	 * @param errorMsg 失败消息
	 * @return 返回结果
	 * @since 2021-09-02 19:14:03
	 */
	public static <T> ResultClass<T> validFail(String errorMsg) {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR, errorMsg);
	}

	/**
	 * of
	 *
	 * @param code     code
	 * @param data     data
	 * @param success  success
	 * @param errorMsg errorMsg
	 * @param <T>      T
	 * @return {@link ResultClass }
	 * @since 2021-09-02 19:12:35
	 */
	public static <T> ResultClass<T> of(int code, T data, boolean success, String errorMsg) {
		return ResultClass
			.<T>builder()
			.code(code)
			.data(data)
			.success(success)
			.errorMsg(errorMsg)
			.timestamp(LocalDateTime.now())
			.requestId(StrUtil.isNotBlank(MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID)) ? MDC
				.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID) : IdGeneratorUtils.getIdStr())
			.build();
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

	public static <T> ResultBuilder<T> builder(){
		return new ResultBuilder<>();
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

		public ResultBuilder<T> data(T data) {
			this.data = data;
			return this;
		}

		public  ResultBuilder<T> success(boolean success) {
			this.success = success;
			return this;
		}

		public ResultBuilder<T> errorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
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

		public ResultClass<T> build() {
			ResultClass<T> result = new ResultClass<T>();
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
