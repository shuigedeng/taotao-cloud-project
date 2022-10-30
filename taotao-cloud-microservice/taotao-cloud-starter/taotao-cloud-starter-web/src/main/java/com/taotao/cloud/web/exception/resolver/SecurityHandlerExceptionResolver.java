package com.taotao.cloud.web.exception.resolver;

import com.taotao.cloud.web.exception.GlobalExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hccake
 */
@Order(-1000)
@RestControllerAdvice
public class SecurityHandlerExceptionResolver {

	private final GlobalExceptionHandler globalExceptionHandler;

	public SecurityHandlerExceptionResolver(GlobalExceptionHandler globalExceptionHandler) {
		this.globalExceptionHandler = globalExceptionHandler;
	}

	/**
	 * AccessDeniedException
	 *
	 * @param e the e
	 * @return R
	 */
	//@ExceptionHandler(AccessDeniedException.class)
	//@ResponseStatus(HttpStatus.FORBIDDEN)
	//public R<String> handleAccessDeniedException(AccessDeniedException e) {
	//	String msg = SpringSecurityMessageSource.getAccessor()
	//		.getMessage("AbstractAccessDecisionManager.accessDenied",
	//			e.getMessage());
	//	log.error("拒绝授权异常信息 ex={}", msg);
	//	globalExceptionHandler.handle(e);
	//	return R.failed(SystemResultCode.FORBIDDEN, e.getLocalizedMessage());
	//}

}
