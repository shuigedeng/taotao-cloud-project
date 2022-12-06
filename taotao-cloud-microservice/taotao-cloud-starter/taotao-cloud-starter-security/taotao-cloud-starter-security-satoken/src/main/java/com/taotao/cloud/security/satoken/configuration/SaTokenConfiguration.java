package com.taotao.cloud.security.satoken.configuration;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.dev33.satoken.util.SaFoxUtil;
import com.taotao.cloud.security.satoken.current.GlobalException;
import com.taotao.cloud.security.satoken.current.NotFoundHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * [Sa-Token 权限认证] 配置类
 *
 * @author kong
 */
@ConditionalOnWebApplication(type = Type.SERVLET)
@Configuration
@Import({MySaTokenListener.class, SaLogForSlf4j.class, StpInterfaceImpl.class, StpUserUtil.class,
	GlobalException.class, NotFoundHandle.class})
public class SaTokenConfiguration implements WebMvcConfigurer {

	/**
	 * 注册 Sa-Token 拦截器打开注解鉴权功能
	 */
	// @Override
	// public void addInterceptors(InterceptorRegistry registry) {
	// 	// 注册 Sa-Token 拦截器打开注解鉴权功能
	// 	registry.addInterceptor(new SaInterceptor(handle -> {
	// 		// SaManager.getLog().debug("----- 请求path={}  提交token={}", SaHolder.getRequest().getRequestPath(), StpUtil.getTokenValue());
	//
	// 		// 指定一条 match 规则
	// 		SaRouter
	// 			.match("/user/**")    // 拦截的 path 列表，可以写多个
	// 			.notMatch("/user/doLogin", "/user/doLogin2")     // 排除掉的 path 列表，可以写多个
	// 			.check(r -> StpUtil.checkLogin());        // 要执行的校验动作，可以写完整的 lambda 表达式
	//
	// 		// 权限校验 -- 不同模块认证不同权限
	// 		SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
	// 		SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
	// 		SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
	// 		SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
	// 		SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
	//
	// 		// 甚至你可以随意的写一个打印语句
	// 		SaRouter.match("/router/print", r -> System.out.println("----啦啦啦----"));
	//
	// 		// 写一个完整的 lambda
	// 		SaRouter.match("/router/print2", r -> {
	// 			System.out.println("----啦啦啦2----");
	// 			// ... 其它代码
	// 		});
	//
	// 		/*
	// 		 * 相关路由都定义在 com.pj.cases.use.RouterCheckController 中
	// 		 */
	//
	// 		// 将match中的SaFunction function参数设置为IgnoreSaRouteFunction对象,就可以实现添加注解忽略权限验证
	// 		SaRouter.match("/**", "/login", new IgnoreSaRouteFunction(handle));
	//
	// 	})).addPathPatterns("/**");
	//
	// 	registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");
	// }

	/**
	 * 注册 [Sa-Token 全局过滤器]
	 */
	// @Bean
	// public SaServletFilter getSaServletFilter() {
	// 	return new SaServletFilter()
	//
	// 		// 指定 [拦截路由] 与 [放行路由]
	// 		.addInclude("/**")// .addExclude("/favicon.ico")
	//
	// 		// 认证函数: 每次请求执行
	// 		.setAuth(obj -> {
	// 			// System.out.println("---------- sa全局认证 " + SaHolder.getRequest().getRequestPath());
	// 			// SaManager.getLog().debug("----- 请求path={}  提交token={}", SaHolder.getRequest().getRequestPath(), StpUtil.getTokenValue());
	//
	// 			// 权限校验 -- 不同模块认证不同权限
	// 			//		这里你可以写和拦截器鉴权同样的代码，不同点在于：
	// 			// 		校验失败后不会进入全局异常组件，而是进入下面的 .setError 函数
	// 			SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
	// 			SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
	// 			SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
	// 			SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
	// 			SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
	// 		})
	//
	// 		// 异常处理函数：每次认证函数发生异常时执行此函数
	// 		.setError(e -> {
	// 			System.out.println("---------- sa全局异常 ");
	// 			return SaResult.error(e.getMessage());
	// 		})
	//
	// 		// 前置函数：在每次认证函数之前执行
	// 		.setBeforeAuth(r -> {
	// 			// ---------- 设置一些安全响应头 ----------
	// 			SaHolder.getResponse()
	// 				// 服务器名称
	// 				.setServer("sa-server")
	// 				// 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
	// 				.setHeader("X-Frame-Options", "SAMEORIGIN")
	// 				// 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
	// 				.setHeader("X-XSS-Protection", "1; mode=block")
	// 				// 禁用浏览器内容嗅探
	// 				.setHeader("X-Content-Type-Options", "nosniff")
	// 			;
	// 		})
	// 		;
	// }

	/**
	 * 重写 Sa-Token 框架内部算法策略
	 */
	@Autowired
	public void rewriteSaStrategy() {
		// 重写Sa-Token的注解处理器，增加注解合并功能
		SaStrategy.me.getAnnotation = AnnotatedElementUtils::getMergedAnnotation;

		// 重写 Token 生成策略 
		SaStrategy.me.createToken = (loginId, loginType) -> {
			return SaFoxUtil.getRandomString(60);    // 随机60位长度字符串
		};


	}

	/**
	 * Sa-Token 整合 jwt
	 */
	@Bean
	public StpLogic getStpLogicJwt() {
		return new StpLogicJwtForSimple();
	}

}
