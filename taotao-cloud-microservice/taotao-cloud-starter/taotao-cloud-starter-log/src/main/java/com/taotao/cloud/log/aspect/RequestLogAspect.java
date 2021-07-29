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
package com.taotao.cloud.log.aspect;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.URLUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.baomidou.mybatisplus.extension.api.R;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.context.TenantContextHolder;
import com.taotao.cloud.common.enums.LogOperateTypeEnum;
import com.taotao.cloud.common.utils.DateUtil;
import com.taotao.cloud.common.utils.DateUtils;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.RequestUtil;
import com.taotao.cloud.common.utils.SecurityUtil;
import com.taotao.cloud.log.event.RequestLogEvent;
import com.taotao.cloud.log.model.RequestLog;
import com.taotao.cloud.log.properties.RequestLogProperties;
import com.taotao.cloud.log.utils.LoggerUtil;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 日志切面
 * <p>
 * ①切面注解得到请求数据 -> ②发布监听事件 -> ③异步监听日志入库
 * </p>
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/3 11:47
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class RequestLogAspect {

	@Value("${spring.application.name}")
	private String applicationName;

	private static final String DEFAULT_SOURCE = "taotao_cloud_request_log";

	@Resource
	private RequestLogProperties requestLogProperties;

	private final ApplicationEventPublisher publisher;

	/**
	 * log实体类
	 **/
	private final TransmittableThreadLocal<RequestLog> SYS_LOG_THREAD_LOCAL = new TransmittableThreadLocal<>();

	/**
	 * 事件发布是由ApplicationContext对象管控的，我们发布事件前需要注入ApplicationContext对象调用publishEvent方法完成事件发布
	 *
	 * @param publisher publisher
	 */
	public RequestLogAspect(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	/***
	 * 定义controller切入点拦截规则，拦截SysLog注解的方法
	 */
	@Pointcut("@annotation(com.taotao.cloud.log.annotation.RequestOperateLog)")
	public void requestLogAspect() {

	}

	/***
	 * 拦截控制层的操作日志
	 * @param joinPoint joinPoint
	 */
	@Before(value = "requestLogAspect()")
	public void recordLog(JoinPoint joinPoint) throws Throwable {
		if (requestLogProperties.getEnabled()) {
			RequestLog requestLog = new RequestLog();
			ServletRequestAttributes attributes = (ServletRequestAttributes) Objects
				.requireNonNull(RequestContextHolder.getRequestAttributes());
			RequestContextHolder.setRequestAttributes(attributes, true);
			HttpServletRequest request = attributes.getRequest();
			requestLog.setApplicationName(applicationName);
			requestLog.setRequestStartTime(Timestamp.valueOf(LocalDateTime.now()).getTime());
			requestLog.setTraceId(MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID));
			requestLog.setRequestIp(RequestUtil.getRemoteAddr(request));
//			requestLog.setClientId(SecurityUtil.getClientId());
			requestLog.setUserId(SecurityUtil.getUserId());
			requestLog.setUsername(SecurityUtil.getUsername());
			requestLog.setRequestUrl(URLUtil.getPath(request.getRequestURI()));
			requestLog.setRequestMethod(request.getMethod());
			Object[] args = joinPoint.getArgs();
			requestLog.setRequestArgs(Arrays.toString(args).replaceAll("\"", "'")
				.replace("\n", ""));
			requestLog.setRequestUa(request.getHeader("user-agent").replaceAll("\"", "'")
				.replace("\n", ""));
			requestLog.setClasspath(joinPoint.getTarget().getClass().getName().replaceAll("\"", "'")
				.replace("\n", ""));
			String name = joinPoint.getSignature().getName();
			requestLog.setRequestMethodName(name);
			requestLog
				.setRequestParams(JsonUtil.toJSONString(RequestUtil.getAllRequestParam(request))
					.replaceAll("\"", "'")
					.replace("\n", ""));
			requestLog
				.setRequestHeaders(
					JsonUtil.toJSONString(RequestUtil.getAllRequestHeaders(request)));
			requestLog.setRequestType(LogUtil.getOperateType(name));
			requestLog.setDescription(LoggerUtil.getControllerMethodDescription(joinPoint));
			requestLog.setSource(DEFAULT_SOURCE);
			requestLog.setCtime(
				String.valueOf(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
			requestLog.setLogday(DateUtil.getCurrentDate());
			SYS_LOG_THREAD_LOCAL.set(requestLog);
		}
	}

	@AfterReturning(returning = "ret", pointcut = "requestLogAspect()")
	public void doAfterReturning(Object ret) {
		RequestLog requestLog = SYS_LOG_THREAD_LOCAL.get();
		if (Objects.nonNull(requestLog)) {
			R r = Convert.convert(R.class, ret);
			if (r.getCode() == HttpStatus.OK.value()) {
				requestLog.setOperateType(LogOperateTypeEnum.OPERATE_RECORD.getValue());
			} else {
				requestLog.setOperateType(LogOperateTypeEnum.EXCEPTION_RECORD.getValue());
				requestLog.setExDetail(r.getMsg());
			}
			requestLog.setTenantId(TenantContextHolder.getTenant());
			requestLog.setRequestEndTime(Timestamp.valueOf(LocalDateTime.now()).getTime());
			long endTime = Instant.now().toEpochMilli();
			requestLog.setRequestConsumingTime(endTime - requestLog.getRequestStartTime());

			publisher.publishEvent(new RequestLogEvent(requestLog));
			SYS_LOG_THREAD_LOCAL.remove();
		}
	}

	@AfterThrowing(pointcut = "requestLogAspect()", throwing = "e")
	public void doAfterThrowable(Throwable e) {
		RequestLog requestLog = SYS_LOG_THREAD_LOCAL.get();
		if (Objects.nonNull(requestLog)) {
			requestLog.setOperateType(LogOperateTypeEnum.EXCEPTION_RECORD.getValue());
			String stackTrace = LogUtil.getStackTrace(e);
			requestLog.setExDetail(stackTrace.replaceAll("\"", "'")
				.replace("\n", ""));
			requestLog.setExDesc(e.getMessage().replaceAll("\"", "'")
				.replace("\n", ""));
			publisher.publishEvent(new RequestLogEvent(requestLog));
			SYS_LOG_THREAD_LOCAL.remove();
		}
	}
}
