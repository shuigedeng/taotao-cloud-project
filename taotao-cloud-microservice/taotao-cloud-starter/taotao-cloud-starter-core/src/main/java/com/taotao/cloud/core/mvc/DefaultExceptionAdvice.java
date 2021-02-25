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
package com.taotao.cloud.core.mvc;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.exception.FeignException;
import com.taotao.cloud.common.exception.IdempotencyException;
import com.taotao.cloud.common.exception.LockException;
import com.taotao.cloud.common.exception.MessageException;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 异常通用处理
 *
 * @author dengtao
 * @date 2020/5/2 09:12
 * @since v1.0
 */
@RestControllerAdvice
@ConditionalOnExpression("!'${security.oauth2.client.clientId}'.isEmpty()")
public class DefaultExceptionAdvice {

	@ExceptionHandler({BaseException.class})
	public Result<String> baseException(NativeWebRequest req, BaseException e) {
		LogUtil.error("【全局异常拦截】BaseException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(e.getCode(), e.getMessage());
	}

	@ExceptionHandler({FeignException.class})
	public Result<String> feignException(NativeWebRequest req, BaseException e) {
		LogUtil.error("【全局异常拦截】FeignException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(e.getCode(), e.getMessage());
	}

	@ExceptionHandler({LockException.class})
	public Result<String> lockException(NativeWebRequest req, LockException e) {
		LogUtil.error("【全局异常拦截】LockException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(e.getCode(), e.getMessage());
	}

	@ExceptionHandler({IdempotencyException.class})
	public Result<String> idempotencyException(NativeWebRequest req, IdempotencyException e) {
		LogUtil.error("【全局异常拦截】IdempotencyException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(e.getCode(), e.getMessage());
	}

	@ExceptionHandler({BusinessException.class})
	public Result<String> businessException(NativeWebRequest req, BusinessException e) {
		LogUtil.error("【全局异常拦截】BusinessException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(e.getCode(), e.getMessage());
	}

	@ExceptionHandler({IllegalArgumentException.class})
	public Result<String> illegalArgumentException(NativeWebRequest req, IllegalArgumentException e) {
		LogUtil.error("【全局异常拦截】IllegalArgumentException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(ResultEnum.ILLEGAL_ARGUMENT_ERROR);
	}

	@ExceptionHandler({AccessDeniedException.class})
	public Result<String> badMethodExpressException(NativeWebRequest req, AccessDeniedException e) {
		LogUtil.error("【全局异常拦截】AccessDeniedException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(ResultEnum.FORBIDDEN);
	}

	@ExceptionHandler({MessageException.class})
	public Result<String> badMessageException(NativeWebRequest req, MessageException e) {
		LogUtil.error("【全局异常拦截】MessageException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(ResultEnum.MESSAGE_SEND_ERROR);
	}

	@ExceptionHandler({UsernameNotFoundException.class})
	public Result<String> badUsernameNotFoundException(NativeWebRequest req, UsernameNotFoundException e) {
		LogUtil.error("【全局异常拦截】UsernameNotFoundException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
	}

	@ExceptionHandler({HttpRequestMethodNotSupportedException.class})
	public Result<String> handleHttpRequestMethodNotSupportedException(NativeWebRequest req, HttpRequestMethodNotSupportedException e) {
		LogUtil.error("【全局异常拦截】HttpRequestMethodNotSupportedException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(ResultEnum.METHOD_NOT_SUPPORTED_ERROR);
	}

	@ExceptionHandler({HttpMediaTypeNotSupportedException.class})
	public Result<String> handleHttpMediaTypeNotSupportedException(NativeWebRequest req, HttpMediaTypeNotSupportedException e) {
		LogUtil.error("【全局异常拦截】HttpMediaTypeNotSupportedException: 请求路径: {0}, 请求参数: {1}, ContentType: {2} 异常信息 {3} ", e,
			uri(req), query(req), e.getContentType(), e.getMessage());
		return Result.failed(ResultEnum.MEDIA_TYPE_NOT_SUPPORTED_ERROR);
	}

	@ExceptionHandler({SQLException.class})
	public Result<String> handleSqlException(NativeWebRequest req, SQLException e) {
		LogUtil.error("【全局异常拦截】SQLException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(ResultEnum.SQL_ERROR);
	}

	@ExceptionHandler({DataIntegrityViolationException.class})
	public Result<String> handleDataIntegrityViolationException(NativeWebRequest req,
																DataIntegrityViolationException e) {
		LogUtil.error("【全局异常拦截】DataIntegrityViolationException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(ResultEnum.SQL_ERROR);
	}

	/**
	 * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
	 */
	@ExceptionHandler(value = BindException.class)
	public Result<ErrorMsg> handleBindException(NativeWebRequest req, BindException e) {
		LogUtil.error("【全局异常拦截】BindException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		BindingResult bindingResult = e.getBindingResult();
		ErrorMsg errorMsg = new ErrorMsg();
		errorMsg.setError(getErrors(bindingResult));
		return Result.failed(errorMsg, ResultEnum.VERIFY_ARGUMENT_ERROR);
	}

	/**
	 * @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<ErrorMsg> handleMethodArgumentNotValidException(NativeWebRequest req, MethodArgumentNotValidException e) {
		LogUtil.error("【全局异常拦截】MethodArgumentNotValidException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		BindingResult bindingResult = e.getBindingResult();
		ErrorMsg errorMsg = new ErrorMsg();
		errorMsg.setError(getErrors(bindingResult));
		return Result.failed(errorMsg, ResultEnum.VERIFY_ARGUMENT_ERROR);
	}

	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public Result<String> requestTypeMismatch(NativeWebRequest req, MethodArgumentTypeMismatchException e) {
		LogUtil.error("【全局异常拦截】MethodArgumentTypeMismatchException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(e.getMessage(), ResultEnum.METHOD_ARGUMETN_TYPE_MISMATCH);
	}

	@ExceptionHandler({MissingServletRequestParameterException.class})
	public Result<String> requestMissingServletRequest(NativeWebRequest req, MissingServletRequestParameterException e) {
		LogUtil.error("【全局异常拦截】MissingServletRequestParameterException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(e.getMessage(), ResultEnum.MISSING_SERVLET_REQUESET_PARAMETER);
	}

	@ExceptionHandler({HttpMessageNotReadableException.class})
	public Result<String> httpMessageNotReadableException(NativeWebRequest req, HttpMessageNotReadableException e) {
		LogUtil.error("【全局异常拦截】HttpMessageNotReadableException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		Throwable throwable = e.getRootCause();
		assert throwable != null;
		return Result.failed(throwable.getMessage(), ResultEnum.HTTP_MESSAGE_NOT_READABLE);
	}

	@ExceptionHandler(ValidationException.class)
	public Result<String> handleException(NativeWebRequest req, ValidationException e) {
		LogUtil.error("【全局异常拦截】ValidationException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(e.getMessage(), ResultEnum.VERIFY_ARGUMENT_ERROR);
	}

	/**
	 * @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public Result<ErrorMsg> handleException(NativeWebRequest req, ConstraintViolationException e) {
		LogUtil.error("【全局异常拦截】ConstraintViolationException: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		ErrorMsg errorMsg = new ErrorMsg();
		errorMsg.setError(getErrors(e));
		return Result.failed(errorMsg, ResultEnum.VERIFY_ARGUMENT_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public Result<String> handleException(NativeWebRequest req, Exception e) {
		LogUtil.error("【全局异常拦截】Exception: 请求路径: {0}, 请求参数: {1}, 异常信息 {2} ", e,
			uri(req), query(req), e.getMessage());
		return Result.failed(ResultEnum.ERROR);
	}

	/**
	 * 获取请求路径
	 *
	 * @param request request
	 * @author dengtao
	 * @date 2020/9/29 13:41
	 * @since v1.0
	 */
	private String uri(NativeWebRequest request) {
		HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
		if (Objects.nonNull(nativeRequest)) {
			return nativeRequest.getRequestURI();
		} else {
			return "--";
		}
	}

	/**
	 * 获取请求参数
	 *
	 * @param request request
	 * @author dengtao
	 * @date 2020/9/29 13:41
	 * @since v1.0
	 */
	private String query(NativeWebRequest request) {
		HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
		if (Objects.nonNull(nativeRequest)) {
			String queryString = nativeRequest.getQueryString();
			if (StrUtil.isNotBlank(queryString)) {
				return queryString;
			}
		}
		return "--";
	}

	private Map<String, String> getErrors(BindingResult result) {
		Map<String, String> map = new HashMap<>();
		List<FieldError> list = result.getFieldErrors();
		for (FieldError error : list) {
			map.put(error.getField(), error.getDefaultMessage());
		}
		return map;
	}

	private Map<String, String> getErrors(ConstraintViolationException e) {
		Map<String, String> map = new HashMap<>();
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
			String property = constraintViolation.getPropertyPath().toString();
			String message = constraintViolation.getMessage();
			map.put(property, message);
		}
		return map;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ErrorMsg {
		private Map<String, String> error;
	}
}
