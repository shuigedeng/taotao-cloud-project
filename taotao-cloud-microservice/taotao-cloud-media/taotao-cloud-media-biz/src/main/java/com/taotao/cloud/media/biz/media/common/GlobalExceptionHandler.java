package com.taotao.cloud.media.biz.media.common;

import com.taotao.cloud.common.utils.log.LogUtils;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常处理
 * 
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ResponseBody
	@ExceptionHandler(RuntimeException.class)
	public AjaxResult globalException(HttpServletResponse response, RuntimeException ex) {
		LogUtils.info("请求错误：" + ex.getMessage());
		return AjaxResult.error(ex.getMessage());
	}

}
