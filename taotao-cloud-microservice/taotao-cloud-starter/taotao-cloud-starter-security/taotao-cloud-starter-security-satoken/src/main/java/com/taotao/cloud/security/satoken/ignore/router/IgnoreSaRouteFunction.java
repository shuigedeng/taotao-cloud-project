package com.taotao.cloud.security.satoken.ignore.router;

import cn.dev33.satoken.fun.SaFunction;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.taotao.cloud.security.satoken.ignore.annotation.IgnoreAuth;
import java.util.Objects;
import org.springframework.web.method.HandlerMethod;

/**
 * 鉴权路由配置类
 *
 * @author xxm
 * @date 2021/8/2
 */
public class IgnoreSaRouteFunction implements SaFunction {

	private final Object handler;

	public IgnoreSaRouteFunction(Object handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		// 注解跳过鉴权
		if (handler instanceof HandlerMethod handlerMethod) {
			IgnoreAuth ignoreAuth = handlerMethod.getMethodAnnotation(IgnoreAuth.class);
			if (Objects.nonNull(ignoreAuth) && ignoreAuth.ignore()) {
				SaRouter.stop();
			}
		}
		StpUtil.checkLogin();
	}
}
