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
import lombok.Builder;
import lombok.Data;
import org.slf4j.MDC;

/**
 * 返回实体类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/29 15:15
 */
@Data
@Builder
@Schema(name = "Result", description = "返回结果对象")
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
		return Result.<T>builder()
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
}
