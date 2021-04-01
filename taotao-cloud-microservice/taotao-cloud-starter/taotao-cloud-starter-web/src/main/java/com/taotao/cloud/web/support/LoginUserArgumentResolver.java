package com.taotao.cloud.web.support;

import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.SecurityUtil;
import com.taotao.cloud.web.annotation.EnableUser;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 通过header里的token获取用户信息
 *
 * @author pangu
 * @link https://my.oschina.net/u/4149877/blog/3143391/print
 * @link https://blog.csdn.net/aiyaya_/article/details/79221733
 */
@Slf4j
@AllArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean isHasEnableUserAnn = parameter.hasParameterAnnotation(EnableUser.class);
		boolean isHasLoginUserParameter = parameter.getParameterType()
			.isAssignableFrom(SecurityUser.class);
		return isHasEnableUserAnn && isHasLoginUserParameter;
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter,
		ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest,
		WebDataBinderFactory webDataBinderFactory) throws Exception {
		EnableUser user = methodParameter.getParameterAnnotation(EnableUser.class);
		boolean value = user.value();
		HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
		SecurityUser loginUser = SecurityUtil.getUser();

		/**
		 * 根据value状态获取更多用户信息，待实现
		 */
		return loginUser;
	}
}
