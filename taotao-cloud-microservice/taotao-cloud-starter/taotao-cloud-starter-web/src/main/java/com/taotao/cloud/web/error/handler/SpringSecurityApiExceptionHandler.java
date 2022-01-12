package com.taotao.cloud.web.error.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.taotao.cloud.web.error.ApiErrorResponse;
import com.taotao.cloud.web.error.ErrorHandlingProperties;
import com.taotao.cloud.web.error.mapper.ErrorCodeMapper;
import com.taotao.cloud.web.error.mapper.ErrorMessageMapper;
import com.taotao.cloud.web.error.mapper.HttpStatusMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * SpringSecurityApiExceptionHandler 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-01-12 09:06:41
 */
public class SpringSecurityApiExceptionHandler extends AbstractApiExceptionHandler {

	private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_TO_STATUS_MAPPING;

	static {
		EXCEPTION_TO_STATUS_MAPPING = new HashMap<>();
		EXCEPTION_TO_STATUS_MAPPING.put(AccessDeniedException.class, FORBIDDEN);
		EXCEPTION_TO_STATUS_MAPPING.put(AccountExpiredException.class, BAD_REQUEST);
		EXCEPTION_TO_STATUS_MAPPING.put(AuthenticationCredentialsNotFoundException.class,
			UNAUTHORIZED);
		EXCEPTION_TO_STATUS_MAPPING.put(AuthenticationServiceException.class,
			INTERNAL_SERVER_ERROR);
		EXCEPTION_TO_STATUS_MAPPING.put(BadCredentialsException.class, BAD_REQUEST);
		EXCEPTION_TO_STATUS_MAPPING.put(UsernameNotFoundException.class, BAD_REQUEST);
		EXCEPTION_TO_STATUS_MAPPING.put(InsufficientAuthenticationException.class, UNAUTHORIZED);
		EXCEPTION_TO_STATUS_MAPPING.put(LockedException.class, BAD_REQUEST);
		EXCEPTION_TO_STATUS_MAPPING.put(DisabledException.class, BAD_REQUEST);
	}

	public SpringSecurityApiExceptionHandler(ErrorHandlingProperties properties,
		HttpStatusMapper httpStatusMapper,
		ErrorCodeMapper errorCodeMapper,
		ErrorMessageMapper errorMessageMapper) {
		super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
	}

	@Override
	public boolean canHandle(Throwable exception) {
		return EXCEPTION_TO_STATUS_MAPPING.containsKey(exception.getClass());
	}

	@Override
	public ApiErrorResponse handle(Throwable exception) {
		HttpStatus httpStatus = EXCEPTION_TO_STATUS_MAPPING.getOrDefault(exception.getClass(),
			INTERNAL_SERVER_ERROR);
		return new ApiErrorResponse(getHttpStatus(exception, httpStatus),
			getErrorCode(exception),
			getErrorMessage(exception));
	}
}
