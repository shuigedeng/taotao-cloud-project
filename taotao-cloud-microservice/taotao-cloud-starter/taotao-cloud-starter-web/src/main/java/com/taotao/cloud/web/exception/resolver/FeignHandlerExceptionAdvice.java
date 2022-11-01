package com.taotao.cloud.web.exception.resolver;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.context.TraceContextHolder;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.common.IdGeneratorUtils;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.TraceUtils;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration;
import com.taotao.cloud.feign.annotation.FeignApi;
import com.taotao.cloud.feign.model.FeignExceptionResult;
import com.taotao.cloud.web.exception.handler.ExceptionHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.util.NestedServletException;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

/**
 * 全局异常处理
 */
@AutoConfiguration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = {FeignApi.class})
//@RestControllerAdvice(basePackages = {"com.taotao.cloud.*.biz.controller.feign"})
public class FeignHandlerExceptionAdvice implements InitializingBean {

	private final List<ExceptionHandler> exceptionHandler;

	@Value("${spring.profiles.active:prod}")
	private String profile;

	@Autowired
	private AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;

	public static final String PROD_ERR_MSG = "系统异常，请联系管理员";

	public static final String NLP_MSG = "空指针异常!";

	public FeignHandlerExceptionAdvice(List<ExceptionHandler> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(FeignHandlerExceptionAdvice.class, StarterName.WEB_STARTER);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleBusinessException(NativeWebRequest req, BusinessException e) {
		handleExceptions(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult(e.getMessage()));
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(NestedServletException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleNestedServletException(NativeWebRequest req, NestedServletException e) {
		handleExceptions(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult(e.getMessage()));
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleException(NativeWebRequest req, Exception e) {
		handleExceptions(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult(e.getMessage()));
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(UndeclaredThrowableException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleUndeclaredThrowableException(NativeWebRequest req,
													 UndeclaredThrowableException ex) {
		handleExceptions(req, ex);
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

	@org.springframework.web.bind.annotation.ExceptionHandler(BlockException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleBlockException(NativeWebRequest req, BlockException e) {
		handleExceptions(req, e);
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

	@org.springframework.web.bind.annotation.ExceptionHandler(FlowException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleFlowException(NativeWebRequest req, FlowException e) {
		handleExceptions(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult("被限流了"));
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(DegradeException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleDegradeException(NativeWebRequest req, DegradeException e) {
		handleExceptions(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult("服务降级了"));
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(ParamFlowException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleParamFlowException(NativeWebRequest req, ParamFlowException e) {
		handleExceptions(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult("服务热点降级了"));
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(SystemBlockException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleSystemBlockException(NativeWebRequest req, SystemBlockException e) {
		handleExceptions(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult("系统过载保护"));
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(AuthorityException.class)
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String handleAuthorityException(NativeWebRequest req, AuthorityException e) {
		handleExceptions(req, e);
		return JsonUtils.toJSONString(new FeignExceptionResult("限流权限控制异常"));
	}

	public void handleExceptions(NativeWebRequest req, Exception e) {
		String traceId = TraceContextHolder.getTraceId();
		if (traceId == null) {
			traceId = TraceUtils.getTraceId();
		}
		if (traceId == null) {
			traceId = IdGeneratorUtils.getIdStr();
		}

		String finalTraceId = traceId;

		asyncThreadPoolTaskExecutor.submit(() -> {
			exceptionHandler.forEach(handler -> {
				handler.handle(req, e, finalTraceId);
			});
		});
	}
}
