package com.taotao.cloud.web.error;

import com.taotao.cloud.web.error.handler.ConstraintViolationApiExceptionHandler;
import com.taotao.cloud.web.error.handler.HttpMessageNotReadableApiExceptionHandler;
import com.taotao.cloud.web.error.handler.MethodArgumentNotValidApiExceptionHandler;
import com.taotao.cloud.web.error.handler.ObjectOptimisticLockingFailureApiExceptionHandler;
import com.taotao.cloud.web.error.handler.SpringSecurityApiExceptionHandler;
import com.taotao.cloud.web.error.handler.TypeMismatchApiExceptionHandler;
import com.taotao.cloud.web.error.mapper.ErrorCodeMapper;
import com.taotao.cloud.web.error.mapper.ErrorMessageMapper;
import com.taotao.cloud.web.error.mapper.HttpStatusMapper;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * ErrorHandlingConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-01-12 09:00:17
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(ErrorHandlingProperties.class)
@ConditionalOnProperty(value = "taotao.cloud.web.error.handling.enabled", matchIfMissing = true)
@PropertySource("classpath:/taotao-cloud-web-error-handling-properties.properties")
public class ErrorHandlingConfiguration {

	@Bean
	public ErrorHandlingControllerAdvice errorHandlingControllerAdvice(
		ErrorHandlingProperties properties,
		List<ApiExceptionHandler> handlers,
		FallbackApiExceptionHandler fallbackApiExceptionHandler) {
		return new ErrorHandlingControllerAdvice(properties,
			handlers,
			fallbackApiExceptionHandler);
	}

	@Bean
	public ApiErrorResponseSerializer apiErrorResponseSerializer(
		ErrorHandlingProperties properties) {
		return new ApiErrorResponseSerializer(properties);
	}

	@Bean
	public HttpStatusMapper httpStatusMapper(ErrorHandlingProperties properties) {
		return new HttpStatusMapper(properties);
	}

	@Bean
	public ErrorCodeMapper errorCodeMapper(ErrorHandlingProperties properties) {
		return new ErrorCodeMapper(properties);
	}

	@Bean
	public ErrorMessageMapper errorMessageMapper(ErrorHandlingProperties properties) {
		return new ErrorMessageMapper(properties);
	}

	@Bean
	public FallbackApiExceptionHandler defaultHandler(HttpStatusMapper httpStatusMapper,
		ErrorCodeMapper errorCodeMapper,
		ErrorMessageMapper errorMessageMapper) {
		return new DefaultFallbackApiExceptionHandler(httpStatusMapper,
			errorCodeMapper,
			errorMessageMapper);
	}

	@Bean
	public TypeMismatchApiExceptionHandler typeMismatchApiExceptionHandler(
		ErrorHandlingProperties properties,
		HttpStatusMapper httpStatusMapper,
		ErrorCodeMapper errorCodeMapper,
		ErrorMessageMapper errorMessageMapper) {
		return new TypeMismatchApiExceptionHandler(properties, httpStatusMapper, errorCodeMapper,
			errorMessageMapper);
	}

	@Bean
	public ConstraintViolationApiExceptionHandler constraintViolationApiExceptionHandler(
		ErrorHandlingProperties properties,
		HttpStatusMapper httpStatusMapper,
		ErrorCodeMapper errorCodeMapper,
		ErrorMessageMapper errorMessageMapper) {
		return new ConstraintViolationApiExceptionHandler(properties, httpStatusMapper,
			errorCodeMapper, errorMessageMapper);
	}

	@Bean
	public HttpMessageNotReadableApiExceptionHandler httpMessageNotReadableApiExceptionHandler(
		ErrorHandlingProperties properties,
		HttpStatusMapper httpStatusMapper,
		ErrorCodeMapper errorCodeMapper,
		ErrorMessageMapper errorMessageMapper) {
		return new HttpMessageNotReadableApiExceptionHandler(properties, httpStatusMapper,
			errorCodeMapper, errorMessageMapper);
	}

	@Bean
	public MethodArgumentNotValidApiExceptionHandler methodArgumentNotValidApiExceptionHandler(
		ErrorHandlingProperties properties,
		HttpStatusMapper httpStatusMapper,
		ErrorCodeMapper errorCodeMapper,
		ErrorMessageMapper errorMessageMapper) {
		return new MethodArgumentNotValidApiExceptionHandler(properties, httpStatusMapper,
			errorCodeMapper, errorMessageMapper);
	}

	@Bean
	@ConditionalOnClass(name = "org.springframework.security.access.AccessDeniedException")
	public SpringSecurityApiExceptionHandler springSecurityApiExceptionHandler(
		ErrorHandlingProperties properties,
		HttpStatusMapper httpStatusMapper,
		ErrorCodeMapper errorCodeMapper,
		ErrorMessageMapper errorMessageMapper) {
		return new SpringSecurityApiExceptionHandler(properties, httpStatusMapper, errorCodeMapper,
			errorMessageMapper);
	}

	@Bean
	@ConditionalOnClass(name = "org.springframework.orm.ObjectOptimisticLockingFailureException")
	public ObjectOptimisticLockingFailureApiExceptionHandler objectOptimisticLockingFailureApiExceptionHandler(
		ErrorHandlingProperties properties,
		HttpStatusMapper httpStatusMapper,
		ErrorCodeMapper errorCodeMapper,
		ErrorMessageMapper errorMessageMapper) {
		return new ObjectOptimisticLockingFailureApiExceptionHandler(properties, httpStatusMapper,
			errorCodeMapper, errorMessageMapper);
	}

}
