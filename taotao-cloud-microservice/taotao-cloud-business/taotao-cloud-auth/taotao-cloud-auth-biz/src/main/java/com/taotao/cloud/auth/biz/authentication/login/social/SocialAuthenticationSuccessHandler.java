/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.auth.biz.authentication.login.social;

import com.taotao.cloud.auth.biz.authentication.token.JwtTokenGenerator;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * LoginAuthenticationSuccessHandler
 *
 * @author shuigedeng
 * @version 2023.07
 * @see AuthenticationSuccessHandler
 * @since 2023-07-10 17:40:09
 */
public class SocialAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	/**
	 * jwt令牌生成器
	 */
	private final JwtTokenGenerator jwtTokenGenerator;

	/**
	 * 社交身份验证成功处理程序
	 *
	 * @param jwtTokenGenerator jwt令牌生成器
	 * @return
	 * @since 2023-07-10 17:40:09
	 */
	public SocialAuthenticationSuccessHandler(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

	/**
	 * 关于身份验证成功
	 *
	 * @param request        请求
	 * @param response       响应
	 * @param authentication 身份验证
	 * @since 2023-07-10 17:40:10
	 */
	@Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

//		// 1.三方用户未被绑定，且当前有登录用户，直接绑定，跳主页，提示绑定成功（绑定操作）
//		// 2.三方用户未被绑定，且当前无登录用户，跳绑定页面（登录&绑定操作）
//		// 3.三方用户已被绑定，找到用户直接登录，跳主页（三方登录操作）
//
//		OAuth2AuthenticationToken oauth2Auth = (OAuth2AuthenticationToken) authentication;
//
//		String authorizationUri = clientRegistrationRepository.findByRegistrationId(oauth2Auth.getAuthorizedClientRegistrationId()).getProviderDetails().getAuthorizationUri();
//		String type = ThirdPlatformType.parse(authorizationUri).toString();
//		String identity = authentication.getName();
//		Optional<UserToAuthPO> bind = userToAuthDao.findByTypeAndIdentity(type, identity);
//
//		// 1.三方用户未被绑定，且当前有登录用户，直接绑定，返回success，跳主页
//		if(!bind.isPresent() && TempAuthContext.notEmpty()){
//			// 还原已登录用户的认证信息
//			Authentication auth = TempAuthContext.get();
//			SecurityContextHolder.getContext().setAuthentication(auth);
//			String username = auth.getName();
//			// 绑定
//			userToAuthDao.save(new UserToAuthPO().setType(type).setIdentity(identity).setUserId(username).setTime(LocalDateTime.now().toString()));
//
//			getRedirectStrategy().sendRedirect(request, response, WebAttributes.bindSuccess);
//			return;
//		}
//
//		// 2.三方用户未被绑定，且当前无登录用户，跳绑定页面
//		if(!bind.isPresent() && !TempAuthContext.notEmpty()){
//			// 将三方认证信息先存起来，以便用户登录后绑定
//			HttpSession session = request.getSession(true);
//			session.setAttribute(WebAttributes.THIRD_TYPE, type);
//			session.setAttribute(WebAttributes.THIRD_AUTHENTICATION, SecurityContextHolder.getContext().getAuthentication());
//			// 置空三方认证信息，防止它存到session去（HttpSessionSecurityContextRepository.SaveToSessionResponseWrapper.saveContext）
//			SecurityContextHolder.getContext().setAuthentication(null);
//			// 跳绑定页面
//			getRedirectStrategy().sendRedirect(request, response, WebAttributes.bindPage);
//			return;
//		}
//
//		// 3.三方用户已被绑定，找到用户直接登录，跳主页
//		String username = bind.get().getUserId();
//		UserDetails user = userDetailsService.loadUserByUsername(username);
//		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), user.getAuthorities());
//		result.setDetails(authentication.getDetails());
//		SecurityContextHolder.getContext().setAuthentication(result);
//
//		getRedirectStrategy().sendRedirect(request, response, WebAttributes.oauth2LoginSuccess);

//		clearAuthenticationAttributes(request);

        LogUtils.error("第三方用户认证成功", authentication);
        ResponseUtils.success(
                response, jwtTokenGenerator.socialTokenResponse((OAuth2User) authentication.getPrincipal()));
    }
}
