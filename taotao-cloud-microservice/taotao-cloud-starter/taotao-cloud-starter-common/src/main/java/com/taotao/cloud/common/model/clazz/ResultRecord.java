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
package com.taotao.cloud.common.model.clazz;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.common.IdGeneratorUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.slf4j.MDC;

/**
 * 返回实体类
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 15:15
 */
@Schema(description = "返回结果对象")
public record ResultRecord<T>(
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
	 * 构造返回对象
	 *
	 * @param code     code
	 * @param data     数据
	 * @param success  是否成功
	 * @param errorMsg 失败消息
	 * @return 返回结果
	 * @since 2021-09-02 19:12:35
	 */
	public static <T> ResultRecord<T> of(int code, T data, boolean success, String errorMsg) {
		return new ResultRecord<>(code,
			data,
			success,
			errorMsg
			, StrUtil.isNotBlank(MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID)) ? MDC.get(
			CommonConstant.TAOTAO_CLOUD_TRACE_ID) : IdGeneratorUtils.getIdStr(),
			LocalDateTime.now());
	}

	/**
	 * 成功返回
	 *
	 * @param data 数据
	 * @return 返回结果
	 * @since 2021-09-02 19:15:21
	 */
	public static <T> ResultRecord<T> success(T data) {
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
	public static <T> ResultRecord<T> success(T data, int code) {
		return of(code, data, CommonConstant.SUCCESS, "");
	}

	/**
	 * 成功返回
	 *
	 * @param data       数据
	 * @param resultEnum 枚举
	 * @return 返回结果
	 * @since 2021-09-02 19:13:07
	 */
	public static ResultRecord<String> success(String data, ResultEnum resultEnum) {
		return of(resultEnum.getCode(), resultEnum.getDesc(), CommonConstant.SUCCESS, "");
	}

	/**
	 * 失败返回
	 *
	 * @return 返回结果
	 * @since 2021-09-02 19:13:14
	 */
	public static ResultRecord<String> fail() {
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
	public static <T> ResultRecord<T> fail(String errorMsg) {
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
	public static <T> ResultRecord<T> fail(String data, int code) {
		return of(code, null, CommonConstant.ERROR, data);
	}

	/**
	 * 失败返回
	 *
	 * @param resultEnum 枚举
	 * @return 返回结果
	 * @since 2021-09-02 19:14:32
	 */
	public static ResultRecord<String> fail(ResultEnum resultEnum) {
		return of(resultEnum.getCode(), null, CommonConstant.ERROR, resultEnum.getDesc());
	}

	/**
	 * 失败返回
	 *
	 * @param throwable 异常信息
	 * @return 返回结果
	 * @since 2021-09-02 19:14:24
	 */
	public static <T> ResultRecord<T> fail(Throwable throwable) {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR, throwable.getMessage());
	}

	/**
	 * 校验失败返回
	 *
	 * @param resultEnum 枚举
	 * @return 返回结果
	 * @since 2021-09-02 19:14:18
	 */
	public static <T> ResultRecord<T> validFail(ResultEnum resultEnum) {
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
	public static <T> ResultRecord<T> validFail(String errorMsg, Object... args) {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR, errorMsg);
	}

	/**
	 * 校验失败返回
	 *
	 * @param errorMsg 失败消息
	 * @return 返回结果
	 * @since 2021-09-02 19:14:03
	 */
	public static <T> ResultRecord<T> validFail(String errorMsg) {
		return of(ResultEnum.ERROR.getCode(), null, CommonConstant.ERROR, errorMsg);
	}

}
