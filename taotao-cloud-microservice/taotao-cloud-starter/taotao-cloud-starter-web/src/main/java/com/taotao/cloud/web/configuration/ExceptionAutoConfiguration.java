// /*
//  * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  *      https://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
// package com.taotao.cloud.web.configuration;
//
// import cn.hutool.core.util.StrUtil;
// import com.alibaba.csp.sentinel.slots.block.BlockException;
// import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
// import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
// import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
// import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
// import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
// import com.taotao.cloud.common.constant.StarterName;
// import com.taotao.cloud.common.enums.ResultEnum;
// import com.taotao.cloud.common.exception.BaseException;
// import com.taotao.cloud.common.exception.BusinessException;
// import com.taotao.cloud.common.exception.FeignErrorException;
// import com.taotao.cloud.common.exception.IdempotencyException;
// import com.taotao.cloud.common.exception.LockException;
// import com.taotao.cloud.common.exception.MessageException;
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.common.utils.log.LogUtils;
// import com.taotao.cloud.idempotent.exception.IdempotentException;
// import com.taotao.cloud.limit.ext.LimitException;
// import com.taotao.cloud.limit.ratelimiter.RateLimitException;
// import com.taotao.cloud.web.annotation.BusinessApi;
// import feign.codec.DecodeException;
// import java.lang.reflect.UndeclaredThrowableException;
// import java.sql.SQLException;
// import java.sql.SQLIntegrityConstraintViolationException;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Objects;
// import java.util.Set;
// import javax.servlet.http.HttpServletRequest;
// import javax.validation.ConstraintViolation;
// import javax.validation.ConstraintViolationException;
// import javax.validation.ValidationException;
// import org.apache.ibatis.exceptions.PersistenceException;
// import org.mybatis.spring.MyBatisSystemException;
// import org.springframework.beans.factory.InitializingBean;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.autoconfigure.AutoConfiguration;
// import org.springframework.core.MethodParameter;
// import org.springframework.dao.DataIntegrityViolationException;
// import org.springframework.dao.DuplicateKeyException;
// import org.springframework.http.converter.HttpMessageNotReadableException;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.validation.BindException;
// import org.springframework.validation.BindingResult;
// import org.springframework.validation.FieldError;
// import org.springframework.web.HttpMediaTypeNotSupportedException;
// import org.springframework.web.HttpRequestMethodNotSupportedException;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.MissingServletRequestParameterException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.context.request.NativeWebRequest;
// import org.springframework.web.method.HandlerMethod;
// import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
// import org.springframework.web.servlet.HandlerExecutionChain;
// import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
//
// /**
//  * 全局统一返回值 包装器
//  *
//  * @author shuigedeng
//  * @version 2021.9
//  * @since 2021-09-02 21:26:19
//  */
// @AutoConfiguration
// //@ConditionalOnExpression("!'${security.oauth2.client.clientId}'.isEmpty()")
// //@RestControllerAdvice
// //@RestControllerAdvice(basePackages = {"com.taotao.cloud.**.biz.controller.business"})
// @RestControllerAdvice(annotations = BusinessApi.class)
// public class ExceptionAutoConfiguration implements InitializingBean {
//
// 	@Autowired
// 	@Qualifier("requestMappingHandlerMapping")
// 	private RequestMappingHandlerMapping mapping;
//
// 	@Override
// 	public void afterPropertiesSet() throws Exception {
// 		LogUtils.started(ExceptionAutoConfiguration.class, StarterName.WEB_STARTER);
// 	}
//
// 	@ExceptionHandler({BaseException.class})
// 	public Result<String> baseException(NativeWebRequest req, BaseException e) {
// 		printLog(req, e);
// 		return Result.fail(e.getMessage(), e.getCode());
// 	}
//
// 	@ExceptionHandler({FeignErrorException.class})
// 	public Result<String> feignException(NativeWebRequest req, FeignErrorException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.INNER_ERROR);
// 	}
//
// 	@ExceptionHandler({LockException.class})
// 	public Result<String> lockException(NativeWebRequest req, LockException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.ERROR);
// 	}
//
// 	@ExceptionHandler({IdempotencyException.class})
// 	public Result<String> idempotencyException(NativeWebRequest req, IdempotencyException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.ERROR);
// 	}
//
// 	@ExceptionHandler({BusinessException.class})
// 	public Result<String> businessException(NativeWebRequest req, BusinessException e) {
// 		printLog(req, e);
// 		return Result.fail(e.getMessage(), e.getCode());
// 	}
//
// 	@ExceptionHandler({IllegalArgumentException.class})
// 	public Result<String> illegalArgumentException(NativeWebRequest req,
// 		IllegalArgumentException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.ILLEGAL_ARGUMENT_ERROR);
// 	}
//
// 	@ExceptionHandler({AccessDeniedException.class})
// 	public Result<String> badMethodExpressException(NativeWebRequest req, AccessDeniedException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.FORBIDDEN);
// 	}
//
// 	@ExceptionHandler({MessageException.class})
// 	public Result<String> badMessageException(NativeWebRequest req, MessageException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.MESSAGE_SEND_ERROR);
// 	}
//
// 	@ExceptionHandler({UsernameNotFoundException.class})
// 	public Result<String> badUsernameNotFoundException(NativeWebRequest req,
// 		UsernameNotFoundException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
// 	}
//
// 	@ExceptionHandler({HttpRequestMethodNotSupportedException.class})
// 	public Result<String> handleHttpRequestMethodNotSupportedException(NativeWebRequest req,
// 		HttpRequestMethodNotSupportedException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.METHOD_NOT_SUPPORTED_ERROR);
// 	}
//
// 	@ExceptionHandler({HttpMediaTypeNotSupportedException.class})
// 	public Result<String> handleHttpMediaTypeNotSupportedException(NativeWebRequest req,
// 		HttpMediaTypeNotSupportedException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.MEDIA_TYPE_NOT_SUPPORTED_ERROR);
// 	}
//
// 	@ExceptionHandler({SQLException.class})
// 	public Result<String> handleSqlException(NativeWebRequest req, SQLException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.ERROR);
// 	}
//
// 	@ExceptionHandler({SQLIntegrityConstraintViolationException.class})
// 	public Result<String> handleSqlException(NativeWebRequest req,
// 		SQLIntegrityConstraintViolationException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.ERROR);
// 	}
//
// 	@ExceptionHandler({PersistenceException.class})
// 	public Result<String> handleSqlException(NativeWebRequest req, PersistenceException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.ERROR);
// 	}
//
// 	@ExceptionHandler({DataIntegrityViolationException.class})
// 	public Result<String> handleDataIntegrityViolationException(NativeWebRequest req,
// 		DataIntegrityViolationException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.ERROR);
// 	}
//
// 	@ExceptionHandler({DecodeException.class})
// 	public Result<String> handleDecodeException(NativeWebRequest req,
// 		DecodeException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.ERROR);
// 	}
//
// 	/**
// 	 * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
// 	 */
// 	@ExceptionHandler(value = BindException.class)
// 	public Result<Map<String, String>> handleBindException(NativeWebRequest req, BindException e) {
// 		printLog(req, e);
// 		BindingResult bindingResult = e.getBindingResult();
// 		return Result.fail(getErrors(bindingResult));
// 	}
//
// 	/**
// 	 * RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常
// 	 */
// 	@ExceptionHandler(MethodArgumentNotValidException.class)
// 	public Result<Map<String, String>> handleMethodArgumentNotValidException(NativeWebRequest req,
// 		MethodArgumentNotValidException e) {
// 		printLog(req, e);
// 		BindingResult bindingResult = e.getBindingResult();
// 		return Result.fail(getErrors(bindingResult));
// 	}
//
// 	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
// 	public Result<String> requestTypeMismatch(NativeWebRequest req,
// 		MethodArgumentTypeMismatchException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.METHOD_ARGUMENTS_TYPE_MISMATCH);
// 	}
//
// 	@ExceptionHandler({MissingServletRequestParameterException.class})
// 	public Result<String> requestMissingServletRequest(NativeWebRequest req,
// 		MissingServletRequestParameterException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.MISSING_SERVLET_REQUEST_PARAMETER);
// 	}
//
// 	@ExceptionHandler({HttpMessageNotReadableException.class})
// 	public Result<String> httpMessageNotReadableException(NativeWebRequest req,
// 		HttpMessageNotReadableException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.HTTP_MESSAGE_NOT_READABLE);
// 	}
//
// 	@ExceptionHandler(ValidationException.class)
// 	public Result<String> handleException(NativeWebRequest req, ValidationException e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.VERIFY_ARGUMENT_ERROR);
// 	}
//
// 	@ExceptionHandler({LimitException.class})
// 	public Result<String> limitException(NativeWebRequest req, LimitException e) {
// 		printLog(req, e);
// 		return Result.fail(e.getMessage(), e.getCode());
// 	}
//
// 	@ExceptionHandler({IdempotentException.class})
// 	public Result<String> idempotentException(NativeWebRequest req, IdempotentException e) {
// 		printLog(req, e);
// 		return Result.fail(e.getMessage(), e.getCode());
// 	}
//
// 	/**
// 	 * RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
// 	 */
// 	@ExceptionHandler(ConstraintViolationException.class)
// 	public Result<Map<String, String>> handleException(NativeWebRequest req,
// 		ConstraintViolationException e) {
// 		printLog(req, e);
// 		return Result.fail(getErrors(e));
// 	}
//
// 	@ExceptionHandler(Exception.class)
// 	public Result<String> handleException(NativeWebRequest req, Exception e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.ERROR);
// 	}
//
// 	@ExceptionHandler(Error.class)
// 	public Result<String> handleThrowable(NativeWebRequest req, Error e) {
// 		printLog(req, e);
// 		return Result.fail(ResultEnum.ERROR);
// 	}
//
// 	@ExceptionHandler(UndeclaredThrowableException.class)
// 	public Result<String> handleUndeclaredThrowableException(NativeWebRequest req,
// 		UndeclaredThrowableException ex) {
// 		printLog(req, ex);
// 		Throwable e = ex.getCause();
//
// 		LogUtils.error("WebmvcHandler sentinel 降级 资源名称");
// 		String errMsg = e.getMessage();
// 		if (e instanceof FlowException) {
// 			errMsg = "被限流了";
// 		}
// 		if (e instanceof DegradeException) {
// 			errMsg = "服务降级了";
// 		}
// 		if (e instanceof ParamFlowException) {
// 			errMsg = "服务热点降级了";
// 		}
// 		if (e instanceof SystemBlockException) {
// 			errMsg = "系统过载保护";
// 		}
// 		if (e instanceof AuthorityException) {
// 			errMsg = "限流权限控制异常";
// 		}
//
// 		return Result.fail(errMsg, 429);
// 	}
//
// 	@ExceptionHandler(BlockException.class)
// 	public Result<String> handleBlockException(NativeWebRequest req, BlockException e) {
// 		printLog(req, e);
// 		LogUtils.error("WebmvcHandler sentinel 降级 资源名称{}", e, e.getRule().getResource());
// 		String errMsg = e.getMessage();
// 		if (e instanceof FlowException) {
// 			errMsg = "被限流了";
// 		}
// 		if (e instanceof DegradeException) {
// 			errMsg = "服务降级了";
// 		}
// 		if (e instanceof ParamFlowException) {
// 			errMsg = "服务热点降级了";
// 		}
// 		if (e instanceof SystemBlockException) {
// 			errMsg = "系统过载保护";
// 		}
// 		if (e instanceof AuthorityException) {
// 			errMsg = "限流权限控制异常";
// 		}
// 		return Result.fail(errMsg, 429);
// 	}
//
// 	@ExceptionHandler(FlowException.class)
// 	public Result<String> handleFlowException(NativeWebRequest req, FlowException e) {
// 		printLog(req, e);
// 		return Result.fail("被限流了", 429);
// 	}
//
// 	@ExceptionHandler(DegradeException.class)
// 	public Result<String> handleDegradeException(NativeWebRequest req, DegradeException e) {
// 		printLog(req, e);
// 		return Result.fail("服务降级了", 429);
// 	}
//
// 	@ExceptionHandler(ParamFlowException.class)
// 	public Result<String> handleParamFlowException(NativeWebRequest req, ParamFlowException e) {
// 		printLog(req, e);
// 		return Result.fail("服务热点降级了", 429);
// 	}
//
// 	@ExceptionHandler(SystemBlockException.class)
// 	public Result<String> handleSystemBlockException(NativeWebRequest req, SystemBlockException e) {
// 		printLog(req, e);
// 		return Result.fail("系统过载保护", 429);
// 	}
//
// 	@ExceptionHandler(AuthorityException.class)
// 	public Result<String> handleAuthorityException(NativeWebRequest req, AuthorityException e) {
// 		printLog(req, e);
// 		return Result.fail("限流权限控制异常", 429);
// 	}
//
// 	@ExceptionHandler(value = RateLimitException.class)
// 	public Result<String> rateLimitException(RateLimitException e) {
// 		return Result.fail("限流权限控制异常", 429);
// 	}
//
// 	// mysql exception
//
// 	/**
// 	 * 主键或UNIQUE索引，数据重复异常
// 	 */
// 	@ExceptionHandler(DuplicateKeyException.class)
// 	public Result<String> handleDuplicateKeyException(DuplicateKeyException e,
// 		HttpServletRequest request) {
// 		String requestURI = request.getRequestURI();
// 		LogUtils.error("请求地址'{}',数据库中已存在记录'{}'", requestURI, e.getMessage());
// 		return Result.fail("数据库中已存在该记录，请联系管理员确认");
// 	}
//
// 	/**
// 	 * Mybatis系统异常 通用处理
// 	 */
// 	@ExceptionHandler(MyBatisSystemException.class)
// 	public Result<String> handleCannotFindDataSourceException(MyBatisSystemException e,
// 		HttpServletRequest request) {
// 		String requestURI = request.getRequestURI();
// 		String message = e.getMessage();
// 		if (message.contains("CannotFindDataSourceException")) {
// 			LogUtils.error("请求地址'{}', 未找到数据源", requestURI);
// 			return Result.fail("未找到数据源，请联系管理员确认");
// 		}
// 		LogUtils.error("请求地址'{}', Mybatis系统异常", requestURI, e);
// 		return Result.fail(message);
// 	}
//
// 	/**
// 	 * 获取请求路径
// 	 *
// 	 * @param request 请求对象
// 	 * @return 请求路径
// 	 * @since 2021-09-02 21:27:08
// 	 */
// 	private String uri(NativeWebRequest request) {
// 		HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
// 		if (Objects.nonNull(nativeRequest)) {
// 			return nativeRequest.getRequestURI();
// 		} else {
// 			return "--";
// 		}
// 	}
//
// 	/**
// 	 * 获取请求参数
// 	 *
// 	 * @param request 请求对象
// 	 * @return 请求参数
// 	 * @since 2021-09-02 21:27:14
// 	 */
// 	private String query(NativeWebRequest request) {
// 		HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
// 		if (Objects.nonNull(nativeRequest)) {
// 			String queryString = nativeRequest.getQueryString();
// 			if (StrUtil.isNotBlank(queryString)) {
// 				return queryString;
// 			}
// 		}
// 		return "--";
// 	}
//
// 	/**
// 	 * 获取Binding错误数据
// 	 *
// 	 * @param result 请求对象
// 	 * @return 错误数据
// 	 * @since 2021-09-02 21:27:21
// 	 */
// 	private String getErrors(BindingResult result) {
// 		String errorMsg = "系统异常";
//
// 		Map<String, String> map = new HashMap<>();
// 		List<FieldError> list = result.getFieldErrors();
// 		for (FieldError error : list) {
// 			map.put(error.getField(), error.getDefaultMessage());
//
// 			errorMsg = error.getDefaultMessage();
// 		}
// 		//return JsonUtil.toJSONString(map);
// 		return errorMsg;
// 	}
//
// 	/**
// 	 * 获取校验错误数据
// 	 *
// 	 * @param e 异常信息
// 	 * @return 校验错误数据
// 	 * @since 2021-09-02 21:27:27
// 	 */
// 	private String getErrors(ConstraintViolationException e) {
// 		String errorMsg = "系统异常";
// 		Map<String, String> map = new HashMap<>();
// 		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
// 		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
// 			String property = constraintViolation.getPropertyPath().toString();
// 			String message = constraintViolation.getMessage();
// 			map.put(property, message);
//
// 			errorMsg = message;
// 		}
// 		//return JsonUtil.toJSONString(map);
// 		return errorMsg;
// 	}
//
// 	/**
// 	 * 打印日志
// 	 *
// 	 * @param req 请求对象
// 	 * @param e   异常信息
// 	 * @since 2021-09-02 21:27:34
// 	 */
// 	private void printLog(NativeWebRequest req, Throwable e) {
// 		try {
// 			//RequestMappingHandlerMapping mapping = ContextUtils.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
// 			HandlerExecutionChain chain = mapping.getHandler(
// 				(HttpServletRequest) req.getNativeRequest());
// 			Object handler = chain.getHandler();
// 			if (handler instanceof HandlerMethod handlerMethod) {
// 				MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
// 				Object bean = handlerMethod.getBean();
// 			}
// 		} catch (Exception ex) {
// 			LogUtils.error(e);
// 		}
//
// 		LogUtils.error(e);
// 		LogUtils.error("【全局异常拦截】{}: 请求路径: {}, 请求参数: {}, 异常信息 {} ", e,
// 			e.getClass().getName(), uri(req), query(req), e.getMessage());
// 	}
// }
//
