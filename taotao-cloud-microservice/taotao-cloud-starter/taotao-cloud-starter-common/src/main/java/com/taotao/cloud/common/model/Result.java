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
import com.taotao.cloud.common.utils.common.IdGeneratorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.slf4j.MDC;

/**
 * 返回实体类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/29 15:15
 */
@Schema(description = "返回结果对象")
public record Result<T>(
	/**
	 * 状态码
	 */
	@Schema(description = "状态码")
	int code,
	/**
	 * 返回数据
	 */
	@Schema(description = "返回数据")
	T data,
	/**
	 * 是否成功
	 */
	@Schema(description = "是否成功")
	boolean success,
	/**
	 * 异常消息体
	 */
	@Schema(description = "异常消息体")
	String errorMsg,
	/**
	 * 请求id
	 */
	@Schema(description = "请求id")
	String requestId,
	/**
	 * 请求结束时间
	 */
	@Schema(description = "请求结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime timestamp) implements Serializable {

	@Serial
	private static final long serialVersionUID = -3685249101751401211L;

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
		return new Result<>(code,
			data,
			success,
			errorMsg
			, StrUtil.isNotBlank(MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID)) ? MDC.get(
			CommonConstant.TAOTAO_CLOUD_TRACE_ID) : IdGeneratorUtil.getIdStr(),
			LocalDateTime.now());
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
		return of(resultEnum.getCode(), resultEnum.getDesc(), CommonConstant.SUCCESS, "");
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
			ResultEnum.ERROR.getDesc());
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
		return of(resultEnum.getCode(), null, CommonConstant.ERROR, resultEnum.getDesc());
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
		return of(resultEnum.getCode(), null, CommonConstant.ERROR, resultEnum.getDesc());
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

}
