package com.taotao.cloud.schedule.dynamicschedule.exception;

import com.example.dynamicschedule.base.ResultDTO;
import com.example.dynamicschedule.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常处理器
 *
 */
@RestControllerAdvice
@Slf4j
public class GenericExceptionHandler {

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(GenericException.class)
	public ResultDTO handleRRException(GenericException e){
		if (e instanceof GenericException) {
			GenericException e1 = (GenericException) e;
			log.error(e1.getCode()+"");
			return ResultUtils.getResultDTO(e1.getCode(), e1.getMessage(), null);
		}
		log.error(e.getCause().getMessage());
		log.error(e.getMessage());

		return ResultUtils.getFail(null);

	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResultDTO handlerNoFoundException(Exception e) {
		log.error(e.getMessage(), e);
		return ResultUtils.getResultDTO(404, "路径不存在，请检查路径是否正确",null);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResultDTO handleDuplicateKeyException(DuplicateKeyException e){
		log.error(e.getMessage(), e);
		return ResultUtils.getResultDTO(99,"数据库中已存在该记录",null);
	}



	@ExceptionHandler(Exception.class)
	public ResultDTO handleException(Exception e){
		log.error(e.getMessage(), e);
		return ResultUtils.getFail(null);
	}
}
