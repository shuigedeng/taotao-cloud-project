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
package com.taotao.cloud.logger.aspect;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.StrPool;
import com.taotao.cloud.common.context.TenantContextHolder;
import com.taotao.cloud.common.enums.LogOperateTypeEnum;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.date.DateUtil;
import com.taotao.cloud.common.utils.ip.IPUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.servlet.RequestUtil;
import com.taotao.cloud.ip2region.model.Ip2regionSearcher;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.logger.event.RequestLoggerEvent;
import com.taotao.cloud.logger.properties.RequestLoggerProperties;
import io.swagger.v3.oas.annotations.Operation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


/**
 * 日志切面
 * <p>
 * ①切面注解得到请求数据 -> ②发布监听事件 -> ③异步监听日志入库
 * </p>
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/6/3 11:47
 */
@Aspect
public class RequestLoggerAspect {

	@Value("${spring.application.name}")
	private String applicationName;

	private static final String DEFAULT_SOURCE = "taotao_cloud_request_log";
	private static final String FORM_DATA_CONTENT_TYPE = "multipart/form-data";

	@Autowired
	private RequestLoggerProperties requestLoggerProperties;
	@Autowired
	private ApplicationEventPublisher publisher;

	/**
	 * 用于SpEL表达式解析.
	 */
	private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

	/**
	 * 用于获取方法参数定义名字.
	 */
	private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

	/**
	 * log实体类
	 */
	private final TransmittableThreadLocal<com.taotao.cloud.logger.model.RequestLogger> REQUEST_LOG_THREAD_LOCAL = new TransmittableThreadLocal<>();

	public RequestLoggerAspect() {
	}

	/***
	 * 定义controller切入点拦截规则：拦截标记SysLog注解和指定包下的方法
	 * 2个表达式加起来才能拦截所有Controller 或者继承了BaseController的方法
	 *
	 * execution(public * com.taotao.cloud.*.biz.controller.*(..)) 解释：
	 *
	 * 第一个* 任意返回类型
	 * 第二个* com.taotao.cloud.*.biz.controller包下的所有类
	 * 第三个* 类下的所有方法
	 * ()中间的.. 任意参数
	 *
	 * &#064;annotation(com.taotao.cloud.logger.annotation.RequestLogger)  解释：
	 */
	@Pointcut("@annotation(com.taotao.cloud.logger.annotation.RequestLogger)")
	public void requestLogAspect() {

	}

	@Before(value = "requestLogAspect()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		if (requestLoggerProperties.getEnabled()) {
			tryCatch(val -> {
				RequestLogger requestLoggerAnnotation = getTargetAnnotation(joinPoint);
				if (check(joinPoint, requestLoggerAnnotation)) {
					return;
				}

				com.taotao.cloud.logger.model.RequestLogger requestLogger = buildRequestLog(
					joinPoint,
					requestLoggerAnnotation);
				REQUEST_LOG_THREAD_LOCAL.set(requestLogger);
			});
		}
	}

	@AfterReturning(returning = "ret", pointcut = "requestLogAspect()")
	public void doAfterReturning(JoinPoint joinPoint, Object ret) {
		tryCatch(p -> {
			RequestLogger requestOperateLog = getTargetAnnotation(joinPoint);
			if (check(joinPoint, requestOperateLog)) {
				return;
			}

			com.taotao.cloud.logger.model.RequestLogger requestLogger = getRequestLogger();
			if (Objects.nonNull(ret)) {
				try {
					Result<?> r = Convert.convert(Result.class, ret);
					if (r.code() == HttpStatus.OK.value()) {
						requestLogger.setOperateType(LogOperateTypeEnum.OPERATE_RECORD.getCode());
					} else {
						requestLogger.setOperateType(LogOperateTypeEnum.EXCEPTION_RECORD.getCode());
						requestLogger.setExDetail(r.errorMsg());
					}
					if (requestOperateLog.response()) {
						requestLogger.setResult(getText(r.toString()));
					}
				} catch (ConvertException e) {
					LogUtil.error(e);
				}
			}
			requestLogger.setTenantId(TenantContextHolder.getTenant());
			requestLogger.setEndTime(Timestamp.valueOf(LocalDateTime.now()).getTime());
			long endTime = Instant.now().toEpochMilli();
			requestLogger.setConsumingTime(endTime - requestLogger.getStartTime());
			requestLogger.setResult(
				getText(String.valueOf(ret == null ? StrPool.EMPTY : ret)));

			publisher.publishEvent(new RequestLoggerEvent(requestLogger));
			REQUEST_LOG_THREAD_LOCAL.remove();
		});
	}

	@AfterThrowing(pointcut = "requestLogAspect()", throwing = "e")
	public void doAfterThrowable(JoinPoint joinPoint, Throwable e) {
		tryCatch(p -> {
			RequestLogger requestOperateLog = getTargetAnnotation(joinPoint);
			if (check(joinPoint, requestOperateLog)) {
				return;
			}
			com.taotao.cloud.logger.model.RequestLogger requestLogger = getRequestLogger();
			requestLogger.setOperateType(LogOperateTypeEnum.EXCEPTION_RECORD.getCode());
			String stackTrace = LogUtil.getStackTrace(e);
			requestLogger.setExDetail(stackTrace.replaceAll("\"", "'")
				.replace("\n", ""));
			requestLogger.setExDesc(e.getMessage().replaceAll("\"", "'")
				.replace("\n", ""));

			if (!requestOperateLog.request() && requestOperateLog.requestByError()
				&& StrUtil.isEmpty(requestLogger.getParams())) {
				Object[] args = joinPoint.getArgs();
				HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
					RequestContextHolder.getRequestAttributes())).getRequest();
				String strArgs = getArgs(args, request);
				requestLogger.setParams(getText(strArgs));
			}

			publisher.publishEvent(new RequestLoggerEvent(requestLogger));
			REQUEST_LOG_THREAD_LOCAL.remove();
		});
	}

	private String getArgs(Object[] args, HttpServletRequest request) {
		String strArgs = StrPool.EMPTY;
		Object[] params = Arrays.stream(args)
			.filter(item -> !(item instanceof ServletRequest || item instanceof ServletResponse))
			.toArray();

		try {
			if (!request.getContentType().contains(FORM_DATA_CONTENT_TYPE)) {
				strArgs = JsonUtil.toJSONString(params);
			}
		} catch (Exception e) {
			try {
				strArgs = Arrays.toString(params);
			} catch (Exception ex) {
				LogUtil.error("解析参数异常", ex);
			}
		}
		return strArgs;
	}

	@NonNull
	private com.taotao.cloud.logger.model.RequestLogger buildRequestLog(JoinPoint joinPoint,
																		RequestLogger requestLoggerAnnotation) {
		com.taotao.cloud.logger.model.RequestLogger requestLogger = new com.taotao.cloud.logger.model.RequestLogger();
		ServletRequestAttributes attributes = (ServletRequestAttributes) Objects
			.requireNonNull(RequestContextHolder.getRequestAttributes());
		RequestContextHolder.setRequestAttributes(attributes, true);
		HttpServletRequest request = attributes.getRequest();
		requestLogger.setTraceId(MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID));
		requestLogger.setApplicationName(applicationName);
		requestLogger.setUsername(SecurityUtil.getUsername());
		requestLogger.setUserId(String.valueOf(SecurityUtil.getUserId()));
		requestLogger.setClientId(SecurityUtil.getClientId());
		String ip = RequestUtil.getRemoteAddr(request);
		requestLogger.setIp(ip);
		requestLogger.setStartTime(Timestamp.valueOf(LocalDateTime.now()).getTime());
		requestLogger.setUrl(URLUtil.getPath(request.getRequestURI()));
		requestLogger.setMethod(request.getMethod());
		Object[] args = joinPoint.getArgs();
		List<String> argsList = new ArrayList<>();
		if (ArrayUtil.isNotEmpty(args)) {
			for (Object arg : args) {
				argsList.add(JsonUtil.toJSONString(arg));
			}
		}
		requestLogger.setArgs(argsList.toString().replaceAll("\"", "'")
			.replace("\n", ""));
		requestLogger.setBrowser(request.getHeader("user-agent").replaceAll("\"", "'")
			.replace("\n", ""));
		requestLogger.setClasspath(joinPoint.getTarget().getClass().getName().replaceAll("\"", "'")
			.replace("\n", ""));
		String name = joinPoint.getSignature().getName();
		requestLogger.setMethodName(name);
		requestLogger.setParams(JsonUtil.toJSONString(RequestUtil.getAllRequestParam(request))
			.replaceAll("\"", "'")
			.replace("\n", ""));
		requestLogger.setHeaders(JsonUtil.toJSONString(RequestUtil.getAllRequestHeaders(request)));
		requestLogger.setRequestType(LogUtil.getRequestType(name));
		requestLogger.setSource(DEFAULT_SOURCE);
		requestLogger.setCtime(
			String.valueOf(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
		requestLogger.setLogday(DateUtil.getCurrentDate());

		Ip2regionSearcher ip2regionSearcher = ContextUtil.getBean(Ip2regionSearcher.class, true);
		if (ip2regionSearcher != null) {
			requestLogger.setLocation(ip2regionSearcher.getAddressAndIsp(ip));
		} else {
			requestLogger.setLocation(IPUtil.getCityInfo(ip));
		}

		String uaStr = request.getHeader("user-agent");
		requestLogger.setOs(UserAgentUtil.parse(uaStr).getOs().toString());

		setDescription(joinPoint, requestLoggerAnnotation, requestLogger);
		return requestLogger;
	}

	/**
	 * 监测是否需要记录日志
	 *
	 * @param joinPoint               端点
	 * @param requestLoggerAnnotation 操作日志
	 * @return true 表示需要记录日志
	 */
	private boolean check(JoinPoint joinPoint, RequestLogger requestLoggerAnnotation) {
		if (requestLoggerAnnotation == null || !requestLoggerAnnotation.enabled()) {
			return true;
		}
		// 读取目标类上的注解
		RequestLogger targetClass = joinPoint.getTarget().getClass()
			.getAnnotation(RequestLogger.class);
		// 加上 RequestLogger == null 会导致父类上的方法永远需要记录日志
		return targetClass != null && !targetClass.enabled();
	}

	private void tryCatch(Consumer<String> consumer) {
		try {
			consumer.accept("");
		} catch (Exception e) {
			LogUtil.error("记录操作日志异常", e);
			REQUEST_LOG_THREAD_LOCAL.remove();
		}
	}

	/**
	 * 优先从子类获取 @RequestLogger： 1，若子类重写了该方法，有标记就记录日志，没标记就忽略日志 2，若子类没有重写该方法，就从父类获取，父类有标记就记录日志，没标记就忽略日志
	 */
	public static RequestLogger getTargetAnnotation(JoinPoint point) {
		try {
			RequestLogger annotation = null;
			if (point.getSignature() instanceof MethodSignature) {
				Method method = ((MethodSignature) point.getSignature()).getMethod();
				if (method != null) {
					annotation = method.getAnnotation(RequestLogger.class);
				}
			}
			return annotation;
		} catch (Exception e) {
			LogUtil.error("获取 {}.{} 的 @RequestOperateLog 注解失败", e,
				point.getSignature().getDeclaringTypeName(),
				point.getSignature().getName());
			return null;
		}
	}

	private com.taotao.cloud.logger.model.RequestLogger getRequestLogger() {
		com.taotao.cloud.logger.model.RequestLogger requestLogger = REQUEST_LOG_THREAD_LOCAL.get();
		if (requestLogger == null) {
			return new com.taotao.cloud.logger.model.RequestLogger();
		}
		return requestLogger;
	}

	/**
	 * 截取指定长度的字符串
	 *
	 * @param val 参数
	 * @return 截取文本
	 */
	private String getText(String val) {
		return StrUtil.sub(val, 0, 65535);
	}

	private void setDescription(JoinPoint joinPoint, RequestLogger requestLoggerAnnotation,
								com.taotao.cloud.logger.model.RequestLogger requestLogger) {
		StringBuilder controllerDescription = new StringBuilder();
		Operation api = joinPoint.getTarget().getClass().getAnnotation(Operation.class);
		if (api != null) {
			String summary = api.summary();
			if (StringUtil.isNotBlank(summary)) {
				controllerDescription.append("-").append(summary);
			}

			String[] tags = api.tags();
			if (ArrayUtil.isNotEmpty(tags)) {
				controllerDescription.append("-").append(tags[0]);
			}

			String description = api.description();
			if (StringUtil.isNotBlank(description)) {
				controllerDescription.append("-").append(description);
			}
		}

		String controllerMethodDescription = getDescribe(requestLoggerAnnotation);
		if (StrUtil.isNotBlank(controllerMethodDescription) && StrUtil.contains(
			controllerMethodDescription, StrPool.HASH)) {
			//获取方法参数值
			Object[] args = joinPoint.getArgs();
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
			controllerMethodDescription = getValBySpEl(controllerMethodDescription, methodSignature,
				args);
		}

		if (requestLoggerAnnotation.controllerApiValue() && StringUtil.isNotBlank(
			controllerDescription)) {
			requestLogger.setDescription(
				controllerDescription + "-" + controllerMethodDescription);
		} else {
			requestLogger.setDescription(controllerDescription.toString());
		}
	}

	/**
	 * 解析spEL表达式
	 */
	private String getValBySpEl(String spEl, MethodSignature methodSignature, Object[] args) {
		try {
			//获取方法形参名数组
			String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
			if (paramNames != null && paramNames.length > 0) {
				Expression expression = spelExpressionParser.parseExpression(spEl);
				// spring的表达式上下文对象
				EvaluationContext context = new StandardEvaluationContext();
				// 给上下文赋值
				for (int i = 0; i < args.length; i++) {
					context.setVariable(paramNames[i], args[i]);
					context.setVariable("p" + i, args[i]);
				}
				Object value = expression.getValue(context);
				return value == null ? spEl : value.toString();
			}
		} catch (Exception e) {
			LogUtil.error("解析操作日志的el表达式出错", e);
		}
		return spEl;
	}

	public static String getDescribe(JoinPoint point) {
		RequestLogger annotation = getTargetAnnotation(point);
		if (annotation == null) {
			return StrPool.EMPTY;
		}
		return annotation.value();
	}

	public static String getDescribe(RequestLogger annotation) {
		if (annotation == null) {
			return StrPool.EMPTY;
		}
		return annotation.value();
	}
}
