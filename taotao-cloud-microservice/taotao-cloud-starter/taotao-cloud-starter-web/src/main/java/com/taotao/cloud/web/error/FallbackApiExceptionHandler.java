package com.taotao.cloud.web.error;

/**
 * FallbackApiExceptionHandler 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-01-12 09:25:53
 */
public interface FallbackApiExceptionHandler {

	ApiErrorResponse handle(Throwable exception);
}
