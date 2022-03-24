//package com.taotao.cloud.oauth2.api.oauth2.security;
//
//import com.taotao.cloud.common.enums.ResultEnum;
//import com.taotao.cloud.common.utils.LogUtil;
//import com.taotao.cloud.core.utils.ResponseUtil;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
//import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//
//
///**
// * 接口需要特定的权限，但是当前用户是匿名用户或者是记住我的用户
// */
//public class CustomizedAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//	private final RequestMatcher authorizationCodeGrantRequestMatcher = new AuthorizationCodeGrantRequestMatcher();
//	private final AuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(
//		DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL);
//
//	@Override
//	public void commence(HttpServletRequest request, HttpServletResponse response,
//		AuthenticationException authException) throws IOException, ServletException {
//		LogUtil.error("认证失败", authException);
//
//		if (StringUtils.equals(request.getServletPath(),
//			DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL)) {
//			loginUrlAuthenticationEntryPoint.commence(request, response, authException);
//			return;
//		}
//
//		// 触发重定向到登陆页面
//		if (authorizationCodeGrantRequestMatcher.matches(request)) {
//			loginUrlAuthenticationEntryPoint.commence(request, response, authException);
//			return;
//		}
//		LogUtil.error("用户未认证", authException);
//		ResponseUtil.fail(response, ResultEnum.UNAUTHORIZED);
//	}
//
//	private class AuthorizationCodeGrantRequestMatcher implements RequestMatcher {
//
//		/**
//		 * <ol>
//		 *     <li>授权码模式 URI</li>
//		 *     <li>隐式授权模式 URI</li>
//		 * </ol>
//		 */
//		private final Set<String> SUPPORT_URIS = new HashSet<>(
//			Arrays.asList("response_type=code", "response_type=token"));
//
//		@Override
//		public boolean matches(HttpServletRequest request) {
//			String servletPath = request.getServletPath();
//			if (StringUtils.equals(servletPath, "/oauth2/authorize")) {
//				final String queryString = request.getQueryString();
//				return SUPPORT_URIS.stream().anyMatch(
//					supportUri -> StringUtils.indexOf(queryString, supportUri)
//						!= StringUtils.INDEX_NOT_FOUND);
//			}
//
//			return false;
//		}
//	}
//
//}
