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
package com.taotao.cloud.web.configuration;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.feign.annotation.FeignApi;
import com.taotao.cloud.feign.model.FeignExceptionResult;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.NestedServletException;

/**
 * feign统一返回异常 包装器
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:26:19
 */
@AutoConfiguration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = {FeignApi.class})
//@RestControllerAdvice(basePackages = {"com.taotao.cloud.*.biz.controller.feign"})
public class FeignExceptionAutoConfiguration implements InitializingBean {

	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	private RequestMappingHandlerMapping mapping;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(FeignExceptionAutoConfiguration.class, StarterName.WEB_STARTER);
	}

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleBusinessException(NativeWebRequest req, BusinessException e) {
		printLog(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult(e.getMessage()));
	}

	@ExceptionHandler(NestedServletException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleNestedServletException(NativeWebRequest req, NestedServletException e) {
		printLog(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult(e.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleException(NativeWebRequest req, Exception e) {
		printLog(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult(e.getMessage()));
	}

	@ExceptionHandler(UndeclaredThrowableException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleUndeclaredThrowableException(NativeWebRequest req,
		UndeclaredThrowableException ex) {
		printLog(req, ex);
		Throwable e = ex.getCause();

		LogUtils.error("WebmvcHandler sentinel 降级 资源名称");
		String errMsg = e.getMessage();
		if (e instanceof FlowException) {
			errMsg = "被限流了";
		}
		if (e instanceof DegradeException) {
			errMsg = "服务降级了";
		}
		if (e instanceof ParamFlowException) {
			errMsg = "服务热点降级了";
		}
		if (e instanceof SystemBlockException) {
			errMsg = "系统过载保护";
		}
		if (e instanceof AuthorityException) {
			errMsg = "限流权限控制异常";
		}

		return JsonUtils.toJSONString(new FeignExceptionResult(errMsg));
	}

	@ExceptionHandler(BlockException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleBlockException(NativeWebRequest req, BlockException e) {
		printLog(req, e);
		LogUtils.error("WebmvcHandler sentinel 降级 资源名称{}", e, e.getRule().getResource());
		String errMsg = e.getMessage();
		if (e instanceof FlowException) {
			errMsg = "被限流了";
		}
		if (e instanceof DegradeException) {
			errMsg = "服务降级了";
		}
		if (e instanceof ParamFlowException) {
			errMsg = "服务热点降级了";
		}
		if (e instanceof SystemBlockException) {
			errMsg = "系统过载保护";
		}
		if (e instanceof AuthorityException) {
			errMsg = "限流权限控制异常";
		}
		return JsonUtils.toJSONString(new FeignExceptionResult(errMsg));
	}

	@ExceptionHandler(FlowException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleFlowException(NativeWebRequest req, FlowException e) {
		printLog(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult("被限流了"));
	}

	@ExceptionHandler(DegradeException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleDegradeException(NativeWebRequest req, DegradeException e) {
		printLog(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult("服务降级了"));
	}

	@ExceptionHandler(ParamFlowException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleParamFlowException(NativeWebRequest req, ParamFlowException e) {
		printLog(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult("服务热点降级了"));
	}

	@ExceptionHandler(SystemBlockException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleSystemBlockException(NativeWebRequest req, SystemBlockException e) {
		printLog(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult("系统过载保护"));
	}

	@ExceptionHandler(AuthorityException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleAuthorityException(NativeWebRequest req, AuthorityException e) {
		printLog(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult("限流权限控制异常"));
	}

	/**
	 * 获取请求路径
	 *
	 * @param request 请求对象
	 * @return 请求路径
	 * @since 2021-09-02 21:27:08
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
	 * @param request 请求对象
	 * @return 请求参数
	 * @since 2021-09-02 21:27:14
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

	/**
	 * 获取Binding错误数据
	 *
	 * @param result 请求对象
	 * @return 错误数据
	 * @since 2021-09-02 21:27:21
	 */
	private String getErrors(BindingResult result) {
		String errorMsg = "系统异常";

		Map<String, String> map = new HashMap<>();
		List<FieldError> list = result.getFieldErrors();
		for (FieldError error : list) {
			map.put(error.getField(), error.getDefaultMessage());

			errorMsg = error.getDefaultMessage();
		}
		//return JsonUtil.toJSONString(map);
		return errorMsg;
	}

	/**
	 * 获取校验错误数据
	 *
	 * @param e 异常信息
	 * @return 校验错误数据
	 * @since 2021-09-02 21:27:27
	 */
	private String getErrors(ConstraintViolationException e) {
		String errorMsg = "系统异常";
		Map<String, String> map = new HashMap<>();
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
			String property = constraintViolation.getPropertyPath().toString();
			String message = constraintViolation.getMessage();
			map.put(property, message);

			errorMsg = message;
		}
		//return JsonUtil.toJSONString(map);
		return errorMsg;
	}

	/**
	 * 打印日志
	 *
	 * @param req 请求对象
	 * @param e   异常信息
	 * @since 2021-09-02 21:27:34
	 */
	private void printLog(NativeWebRequest req, Throwable e) {
		try {
			//RequestMappingHandlerMapping mapping = ContextUtils.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
			HandlerExecutionChain chain = mapping.getHandler(
				(HttpServletRequest) req.getNativeRequest());
			Object handler = chain.getHandler();
			if (handler instanceof HandlerMethod handlerMethod) {
				MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
				Object bean = handlerMethod.getBean();
			}
		} catch (Exception ex) {
			LogUtils.error(e);
		}

		LogUtils.error(e);
		LogUtils.error("【feign请求异常拦截】{}: 请求路径: {}, 请求参数: {}, 异常信息 {} ", e,
			e.getClass().getName(), uri(req), query(req), e.getMessage());
	}
}

