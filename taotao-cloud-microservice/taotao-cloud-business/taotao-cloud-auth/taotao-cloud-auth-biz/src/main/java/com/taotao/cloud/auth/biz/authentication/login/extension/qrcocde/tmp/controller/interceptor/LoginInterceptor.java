package com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.controller.interceptor;

import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.entity.User;
import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.service.UserService;
import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.utils.CommonUtil;
import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.utils.HostHolder;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;


public class LoginInterceptor implements HandlerInterceptor {

	private HostHolder hostHolder;

	private RedisRepository cacheStore;

	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String accessToken = request.getHeader("access_token");
		// access_token 存在
		if (StringUtils.isNotEmpty(accessToken)) {
			String userId = (String) cacheStore.get(CommonUtil.buildAccessTokenKey(accessToken));
			User user = userService.getCurrentUser(userId);
			hostHolder.setUser(user);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		hostHolder.clear();
	}
}
