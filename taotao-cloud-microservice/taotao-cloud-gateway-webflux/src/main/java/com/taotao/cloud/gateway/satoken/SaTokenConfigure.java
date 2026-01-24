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

package com.taotao.cloud.gateway.satoken;
//
// import cn.dev33.satoken.context.SaHolder;
// import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
// import cn.dev33.satoken.reactor.filter.SaReactorFilter;
// import cn.dev33.satoken.router.SaHttpMethod;
// import cn.dev33.satoken.router.SaRouter;
// import cn.dev33.satoken.stp.StpUtil;
// import cn.dev33.satoken.util.SaResult;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.server.ServerWebExchange;
//
// @ConditionalOnClass(SaReactorFilter.class)
// @Configuration
// public class SaTokenConfigure {
//
//	// 注册 Sa-Token全局过滤器
//	@Bean
//	public SaReactorFilter getSaReactorFilter() {
//		return new SaReactorFilter()
//			// 拦截地址
//			.addInclude("/**")
//			// 开放地址
//			.addExclude("/favicon.ico")
//			// 鉴权方法：每次访问进入
//			.setAuth(obj -> {
//				// 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
//				SaRouter.match("/**", "/meta-auth/phoneLogin", r -> StpUtil.checkLogin());
//				// 角色认证 -- 拦截以 admin 开头的路由，必须具备 admin 角色或者 super-admin 角色才可以通过认证
//				SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr("admin", "super-admin"));
//				// 权限认证 -- 不同模块, 校验不同权限
//				SaRouter.match("/meta-system/**", r -> StpUtil.checkPermission("system-no"));
//				SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
//				SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//				SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
//			})
//			// 异常处理方法：每次setAuth函数出现异常时进入
//			.setError(e -> {
//				// 设置错误返回格式为JSON
//				ServerWebExchange exchange = SaReactorSyncHolder.getContext();
//				exchange.getResponse().getHeaders()
//					.set("Content-Type", "application/json; charset=utf-8");
////                    return new ResultJsonUtil().fail(e.getMessage());
//				return SaResult.error(e.getMessage());
//			})
//			.setBeforeAuth(obj -> {
//				// ---------- 设置跨域响应头 ----------
//				SaHolder.getResponse()
//					// 允许指定域访问跨域资源
//					.setHeader("Access-Control-Allow-Origin", "*")
//					// 允许所有请求方式
//					.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
//					// 有效时间
//					.setHeader("Access-Control-Max-Age", "3600")
//					// 允许的header参数
//					.setHeader("Access-Control-Allow-Headers", "*");
//
//				// 如果是预检请求，则立即返回到前端
//				SaRouter.match(SaHttpMethod.OPTIONS)
//					.free(r -> LogUtils.info("--------OPTIONS预检请求，不做处理"))
//					.back();
//			});
//	}
//
//
// }
