package com.taotao.cloud.auth.biz.utils;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 访问被拒绝时的处理逻辑
 *
 * @author n1
 * @see AccessDeniedException
 * @since 2021 /3/26 14:39
 */
public class SimpleAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		//this.write(request, response);
	}

	//@Override
	//protected Map<String, Object> body(HttpServletRequest request) {
	//    Map<String, Object> map = new LinkedHashMap<>(3);
	//    map.put("code", HttpStatus.FORBIDDEN.value());
	//    map.put("uri", request.getRequestURI());
	//    map.put("message", HttpStatus.FORBIDDEN.getReasonPhrase());
	//    return map;
	//}
}
