//package com.taotao.cloud.auth.api.tmp.config;
//
//import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//
//public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
//
//	@Override
//	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
//		Authentication authentication) throws IOException, ServletException {
//		if (!response.isCommitted()) {
////            R<String> r = R.okMsg("退出成功");
////            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
////            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
////            response.setStatus(HttpServletResponse.SC_OK);
////            response.getWriter().write(new ObjectMapper().writeValueAsString(r));
//		}
//	}
//}
