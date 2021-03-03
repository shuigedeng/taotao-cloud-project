package com.taotao.cloud.auth.client.security;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;


/**
 * 接口无权访问处理器 {@link ExceptionTranslationFilter#handleSpringSecurityException}
 *
 * @author zyc
 */
public class CustomizedAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType("text/plain");
		response.getWriter().write("用户未授权");
	}
}
