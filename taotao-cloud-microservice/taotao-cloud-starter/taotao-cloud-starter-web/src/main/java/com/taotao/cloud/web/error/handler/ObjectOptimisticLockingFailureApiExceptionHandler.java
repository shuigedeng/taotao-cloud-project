package com.taotao.cloud.web.error.handler;

import com.taotao.cloud.web.error.ApiErrorResponse;
import com.taotao.cloud.web.error.ErrorHandlingProperties;
import com.taotao.cloud.web.error.mapper.ErrorCodeMapper;
import com.taotao.cloud.web.error.mapper.ErrorMessageMapper;
import com.taotao.cloud.web.error.mapper.HttpStatusMapper;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

/**
 * ObjectOptimisticLockingFailureApiExceptionHandler 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-01-12 09:06:41
 */
public class ObjectOptimisticLockingFailureApiExceptionHandler extends AbstractApiExceptionHandler {

	public ObjectOptimisticLockingFailureApiExceptionHandler(ErrorHandlingProperties properties,
		HttpStatusMapper httpStatusMapper,
		ErrorCodeMapper errorCodeMapper,
		ErrorMessageMapper errorMessageMapper) {
		super(httpStatusMapper, errorCodeMapper, errorMessageMapper);
	}

	@Override
	public boolean canHandle(Throwable exception) {
		return exception instanceof ObjectOptimisticLockingFailureException;
	}

	@Override
	public ApiErrorResponse handle(Throwable exception) {
		ApiErrorResponse response = new ApiErrorResponse(
			getHttpStatus(exception, HttpStatus.CONFLICT),
			getErrorCode(exception),
			getErrorMessage(exception));
		ObjectOptimisticLockingFailureException ex = (ObjectOptimisticLockingFailureException) exception;
		response.addErrorProperty("identifier", ex.getIdentifier());
		response.addErrorProperty("persistentClassName", ex.getPersistentClassName());
		return response;
	}
}
