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

package com.taotao.cloud.gateway.error;

import cn.hutool.core.util.StrUtil;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.model.Code;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.log.LogUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webflux.autoconfigure.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.webflux.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

/**
 * 自定义异常处理
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:12
 */
public class JsonErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

	public JsonErrorWebExceptionHandler(
		ErrorAttributes errorAttributes,
		Resources resources,
		ErrorProperties errorProperties,
		ApplicationContext applicationContext) {
		super(errorAttributes, resources, errorProperties, applicationContext);
	}

	// @Override
	// protected Map<String, Object> getErrorAttributes(ServerRequest request,
	//	boolean includeStackTrace) {
	//	Throwable error = super.getError(request);
	//	LogUtils.error(error.getMessage(), error);
	//	return responseError(error.getMessage());
	// }

	@Override
	protected Map<String, Object> getErrorAttributes(
		ServerRequest request, ErrorAttributeOptions options) {
		Throwable error = super.getError(request);

		LogUtils.error(
			error,
			"请求发生异常，请求URI：{}，请求方法：{}，异常信息：{}",
			request.path(),
			request.method().name(),
			error.getMessage());

		String message = ResultEnum.FAILED.getDesc();
		Code code = ResultEnum.FAILED.code();

		if (error instanceof NotFoundException notFoundException) {
			String serverId =
				StringUtils.substringAfterLast(
					error.getMessage(), "Unable to find instance for ");
			serverId = StrUtil.replace(serverId, "\"", StringUtils.EMPTY);
			LogUtils.error(notFoundException, String.format("无法找到%s服务, 服务不可用", serverId));
		}
		if (error instanceof TimeoutException timeoutException) {
			String serverId =
				StringUtils.substringAfterLast(error.getMessage(), "connection refuse");
			serverId = StrUtil.replace(serverId, "\"", StringUtils.EMPTY);
			LogUtils.error(timeoutException, String.format("访问服务超时%s服务", serverId));
		}
		if (StrUtil.containsIgnoreCase(error.getMessage(), "connection refused")) {
			String serverId =
				StringUtils.substringAfterLast(error.getMessage(), "connection refuse");
			serverId = StrUtil.replace(serverId, "\"", StringUtils.EMPTY);
			LogUtils.error(String.format("目标服务拒绝连接%s服务", serverId));
		}
		if (error instanceof MethodNotAllowedException methodNotAllowedException) {
			message = methodNotAllowedException.getMessage();
			LogUtils.error(methodNotAllowedException, "请求方式错误" + message);
		}
		if (error
			instanceof
			UnsupportedMediaTypeStatusException unsupportedMediaTypeStatusException) {
			message = unsupportedMediaTypeStatusException.getMessage();
			LogUtils.error(unsupportedMediaTypeStatusException, "不支持的媒体类型" + message);
		}
		if (error instanceof ServerErrorException serverErrorException) {
			message = serverErrorException.getMessage();
			LogUtils.error(serverErrorException, "服务内部错误" + message);
		}

		if (error instanceof ResponseStatusException responseStatusException) {
			LogUtils.error(responseStatusException, "请求返回状态错误");

			HttpStatus httpStatus =
				HttpStatus.resolve(responseStatusException.getStatusCode().value());

			if (HttpStatus.NOT_FOUND == httpStatus) {
				LogUtils.error(responseStatusException, "未找到该资源");
				message = ResultEnum.REQUEST_NOT_FOUND.getDesc();
				code = ResultEnum.REQUEST_NOT_FOUND.code();
			}

			if (HttpStatus.GATEWAY_TIMEOUT == httpStatus) {
				LogUtils.error(responseStatusException, "调用后台服务超时了");
				message = ResultEnum.TIMEOUT_ERROR.getDesc();
				code = ResultEnum.TIMEOUT_ERROR.code();
			}
		}

		return responseError(message, code);
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction( ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}

	@Override
	protected int getHttpStatus(Map<String, Object> errorAttributes) {
		return HttpStatus.OK.value();
	}

	/**
	 * 构建返回的JSON数据格式
	 *
	 * @param message 异常信息
	 */
	public static Map<String, Object> responseError(String message, Code code) {
		Result<String> result = Result.fail(code, message);
		Map<String, Object> res = new HashMap<>();
		res.put("status", result.getStatus());
		res.put("code", result.getCode());
		res.put("message", result.getMessage());
		res.put("timestamp", result.getTimestamp());
		res.put("version", result.getVersion());
		res.put("requestId", result.getRequestId());

		// Map<String, Object> map = BeanUtil.beanToMap(result, false, false);
		// LocalDateTime timestamp = (LocalDateTime) map
		//	.getOrDefault("timestamp", LocalDateTime.now());
		// map.put("timestamp", timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd
		// HH:mm:ss")));
		return res;
	}
}
