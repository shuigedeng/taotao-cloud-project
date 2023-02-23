package com.taotao.cloud.auth.biz.utils;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 处理认证失败的逻辑
 *
 * @author n1
 * @see AuthenticationException
 * @since 2021 /3/26 14:43
 */
public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		//String message = exceptionMessage(authException);
		//request.setAttribute("exMsg", message);
		//this.write(request, response);
	}

	//@Override
	//protected Map<String, Object> body(HttpServletRequest request) {
	//    Map<String, Object> map = new LinkedHashMap<>(3);
	//    String exMsg = (String) request.getAttribute("exMsg");
	//    map.put("code", HttpStatus.UNAUTHORIZED.value());
	//    map.put("uri", request.getRequestURI());
	//    map.put("message", exMsg);
	//    return map;
	//}


}
