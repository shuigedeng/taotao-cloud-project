package com.taotao.cloud.web.error.handler;

import com.taotao.cloud.web.error.ApiExceptionHandler;
import com.taotao.cloud.web.error.mapper.ErrorCodeMapper;
import com.taotao.cloud.web.error.mapper.ErrorMessageMapper;
import com.taotao.cloud.web.error.mapper.HttpStatusMapper;
import org.springframework.http.HttpStatus;

/**
 * AbstractApiExceptionHandler 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-01-12 09:06:41
 */
public abstract class AbstractApiExceptionHandler implements ApiExceptionHandler {

	protected final HttpStatusMapper httpStatusMapper;
	protected final ErrorCodeMapper errorCodeMapper;
	protected final ErrorMessageMapper errorMessageMapper;

	public AbstractApiExceptionHandler(HttpStatusMapper httpStatusMapper,
		ErrorCodeMapper errorCodeMapper,
		ErrorMessageMapper errorMessageMapper) {
		this.httpStatusMapper = httpStatusMapper;
		this.errorCodeMapper = errorCodeMapper;
		this.errorMessageMapper = errorMessageMapper;
	}

	protected HttpStatus getHttpStatus(Throwable exception, HttpStatus defaultHttpStatus) {
		return httpStatusMapper.getHttpStatus(exception, defaultHttpStatus);
	}

	protected String getErrorCode(Throwable exception) {
		return errorCodeMapper.getErrorCode(exception);
	}

	protected String getErrorMessage(Throwable exception) {
		return errorMessageMapper.getErrorMessage(exception);
	}
}
