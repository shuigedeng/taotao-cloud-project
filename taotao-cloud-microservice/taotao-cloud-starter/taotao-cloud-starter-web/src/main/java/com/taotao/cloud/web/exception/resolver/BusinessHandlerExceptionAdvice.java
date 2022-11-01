package com.taotao.cloud.web.exception.resolver;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.context.TraceContextHolder;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.exception.FeignErrorException;
import com.taotao.cloud.common.exception.IdempotencyException;
import com.taotao.cloud.common.exception.LockException;
import com.taotao.cloud.common.exception.MessageException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.IdGeneratorUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.TraceUtils;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration;
import com.taotao.cloud.idempotent.exception.IdempotentException;
import com.taotao.cloud.limit.ext.LimitException;
import com.taotao.cloud.limit.ratelimiter.RateLimitException;
import com.taotao.cloud.web.annotation.BusinessApi;
import com.taotao.cloud.web.exception.handler.ExceptionHandler;
import feign.codec.DecodeException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
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
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 全局异常处理
 */
@AutoConfiguration
//@ConditionalOnExpression("!'${security.oauth2.client.clientId}'.isEmpty()")
//@RestControllerAdvice
//@RestControllerAdvice(basePackages = {"com.taotao.cloud.**.biz.controller.business"})
@RestControllerAdvice(annotations = BusinessApi.class)
public class BusinessHandlerExceptionAdvice implements InitializingBean {

	private final List<ExceptionHandler> exceptionHandler;

	@Value("${spring.profiles.active:prod}")
	private String profile;
	@Autowired
	private AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;

	public static final String PROD_ERR_MSG = "系统异常，请联系管理员";

	public static final String NLP_MSG = "空指针异常!";

	public BusinessHandlerExceptionAdvice(List<ExceptionHandler> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(BusinessHandlerExceptionAdvice.class, StarterName.WEB_STARTER);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({BaseException.class})
	public Result<String> baseException(NativeWebRequest req, BaseException e) {
		handleExceptions(req, e);
		return Result.fail(e.getMessage(), e.getCode());
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({FeignErrorException.class})
	public Result<String> feignException(NativeWebRequest req, FeignErrorException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.INNER_ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({LockException.class})
	public Result<String> lockException(NativeWebRequest req, LockException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({IdempotencyException.class})
	public Result<String> idempotencyException(NativeWebRequest req, IdempotencyException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({BusinessException.class})
	public Result<String> businessException(NativeWebRequest req, BusinessException e) {
		handleExceptions(req, e);
		return Result.fail(e.getMessage(), e.getCode());
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({IllegalArgumentException.class})
	public Result<String> illegalArgumentException(NativeWebRequest req,
												   IllegalArgumentException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.ILLEGAL_ARGUMENT_ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({AccessDeniedException.class})
	public Result<String> badMethodExpressException(NativeWebRequest req, AccessDeniedException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.FORBIDDEN);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({MessageException.class})
	public Result<String> badMessageException(NativeWebRequest req, MessageException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.MESSAGE_SEND_ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({UsernameNotFoundException.class})
	public Result<String> badUsernameNotFoundException(NativeWebRequest req,
													   UsernameNotFoundException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({HttpRequestMethodNotSupportedException.class})
	public Result<String> handleHttpRequestMethodNotSupportedException(NativeWebRequest req,
																	   HttpRequestMethodNotSupportedException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.METHOD_NOT_SUPPORTED_ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({HttpMediaTypeNotSupportedException.class})
	public Result<String> handleHttpMediaTypeNotSupportedException(NativeWebRequest req,
																   HttpMediaTypeNotSupportedException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.MEDIA_TYPE_NOT_SUPPORTED_ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({SQLException.class})
	public Result<String> handleSqlException(NativeWebRequest req, SQLException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({SQLIntegrityConstraintViolationException.class})
	public Result<String> handleSqlException(NativeWebRequest req,
											 SQLIntegrityConstraintViolationException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({PersistenceException.class})
	public Result<String> handleSqlException(NativeWebRequest req, PersistenceException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({DataIntegrityViolationException.class})
	public Result<String> handleDataIntegrityViolationException(NativeWebRequest req,
																DataIntegrityViolationException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({DecodeException.class})
	public Result<String> handleDecodeException(NativeWebRequest req,
												DecodeException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	/**
	 * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler(value = BindException.class)
	public Result<Map<String, String>> handleBindException(NativeWebRequest req, BindException e) {
		handleExceptions(req, e);
		BindingResult bindingResult = e.getBindingResult();
		return Result.fail(getErrors(bindingResult));
	}

	/**
	 * RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<Map<String, String>> handleMethodArgumentNotValidException(NativeWebRequest req,
																			 MethodArgumentNotValidException e) {
		handleExceptions(req, e);
		BindingResult bindingResult = e.getBindingResult();
		return Result.fail(getErrors(bindingResult));
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public Result<String> requestTypeMismatch(NativeWebRequest req,
											  MethodArgumentTypeMismatchException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.METHOD_ARGUMENTS_TYPE_MISMATCH);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({MissingServletRequestParameterException.class})
	public Result<String> requestMissingServletRequest(NativeWebRequest req,
													   MissingServletRequestParameterException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.MISSING_SERVLET_REQUEST_PARAMETER);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({HttpMessageNotReadableException.class})
	public Result<String> httpMessageNotReadableException(NativeWebRequest req,
														  HttpMessageNotReadableException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.HTTP_MESSAGE_NOT_READABLE);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
	public Result<String> handleException(NativeWebRequest req, ValidationException e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.VERIFY_ARGUMENT_ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({LimitException.class})
	public Result<String> limitException(NativeWebRequest req, LimitException e) {
		handleExceptions(req, e);
		return Result.fail(e.getMessage(), e.getCode());
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({IdempotentException.class})
	public Result<String> idempotentException(NativeWebRequest req, IdempotentException e) {
		handleExceptions(req, e);
		return Result.fail(e.getMessage(), e.getCode());
	}

	/**
	 * RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
	public Result<Map<String, String>> handleException(NativeWebRequest req,
													   ConstraintViolationException e) {
		handleExceptions(req, e);
		return Result.fail(getErrors(e));
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public Result<String> handleException(NativeWebRequest req, Exception e) {
		handleExceptions(req, e);
		return Result.fail(ResultEnum.ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(Error.class)
	public Result<String> handleThrowable(NativeWebRequest req, Error e) {
		handleExceptions(req, new Exception(e.getMessage()));
		return Result.fail(ResultEnum.ERROR);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(UndeclaredThrowableException.class)
	public Result<String> handleUndeclaredThrowableException(NativeWebRequest req,
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

		return Result.fail(errMsg, 429);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(BlockException.class)
	public Result<String> handleBlockException(NativeWebRequest req, BlockException e) {
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
		return Result.fail(errMsg, 429);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(FlowException.class)
	public Result<String> handleFlowException(NativeWebRequest req, FlowException e) {
		handleExceptions(req, e);
		return Result.fail("被限流了", 429);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(DegradeException.class)
	public Result<String> handleDegradeException(NativeWebRequest req, DegradeException e) {
		handleExceptions(req, e);
		return Result.fail("服务降级了", 429);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(ParamFlowException.class)
	public Result<String> handleParamFlowException(NativeWebRequest req, ParamFlowException e) {
		handleExceptions(req, e);
		return Result.fail("服务热点降级了", 429);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(SystemBlockException.class)
	public Result<String> handleSystemBlockException(NativeWebRequest req, SystemBlockException e) {
		handleExceptions(req, e);
		return Result.fail("系统过载保护", 429);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(AuthorityException.class)
	public Result<String> handleAuthorityException(NativeWebRequest req, AuthorityException e) {
		handleExceptions(req, e);
		return Result.fail("限流权限控制异常", 429);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = RateLimitException.class)
	public Result<String> rateLimitException(RateLimitException e) {
		return Result.fail("限流权限控制异常", 429);
	}

	// mysql exception

	/**
	 * 主键或UNIQUE索引，数据重复异常
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler(DuplicateKeyException.class)
	public Result<String> handleDuplicateKeyException(DuplicateKeyException e,
													  HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		LogUtils.error("请求地址'{}',数据库中已存在记录'{}'", requestURI, e.getMessage());
		return Result.fail("数据库中已存在该记录，请联系管理员确认");
	}

	/**
	 * Mybatis系统异常 通用处理
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler(MyBatisSystemException.class)
	public Result<String> handleCannotFindDataSourceException(MyBatisSystemException e,
															  HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String message = e.getMessage();
		if (message.contains("CannotFindDataSourceException")) {
			LogUtils.error("请求地址'{}', 未找到数据源", requestURI);
			return Result.fail("未找到数据源，请联系管理员确认");
		}
		LogUtils.error("请求地址'{}', Mybatis系统异常", requestURI, e);
		return Result.fail(message);
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
