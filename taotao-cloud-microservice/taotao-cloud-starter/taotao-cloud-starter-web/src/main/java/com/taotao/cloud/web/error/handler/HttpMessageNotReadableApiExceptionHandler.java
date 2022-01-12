package com.taotao.cloud.web.error.handler;

import com.taotao.cloud.web.error.ApiErrorResponse;
import com.taotao.cloud.web.error.ApiExceptionHandler;
import com.taotao.cloud.web.error.ErrorHandlingProperties;
import com.taotao.cloud.web.error.mapper.ErrorCodeMapper;
import com.taotao.cloud.web.error.mapper.ErrorMessageMapper;
import com.taotao.cloud.web.error.mapper.HttpStatusMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * {@link ApiExceptionHandler} for {@link
 * HttpMessageNotReadableException}. This typically happens when Spring can't properly decode the
 * incoming request to JSON.
 */
public class HttpMessageNotReadableApiExceptionHandler extends AbstractApiExceptionHandler {

	public HttpMessageNotReadableApiExceptionHandler(ErrorHandlingProperties properties,
		HttpStatusMapper httpStatusMapper,
		ErrorCodeMapper errorCodeMapper,
		ErrorMessageMapper errorMessageMapper) {
		super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
	}

	@Override
	public boolean canHandle(Throwable exception) {
		return exception instanceof HttpMessageNotReadableException;
	}

	@Override
	public ApiErrorResponse handle(Throwable exception) {
		return new ApiErrorResponse(getHttpStatus(exception, HttpStatus.BAD_REQUEST),
			getErrorCode(exception),
			getErrorMessage(exception));
	}

}
