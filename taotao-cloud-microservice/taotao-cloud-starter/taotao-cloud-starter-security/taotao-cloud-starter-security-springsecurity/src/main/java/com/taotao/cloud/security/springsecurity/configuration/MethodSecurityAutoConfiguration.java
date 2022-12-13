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
package com.taotao.cloud.security.springsecurity.configuration;

import cn.hutool.core.collection.CollectionUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import java.io.Serializable;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * MethodSecurityConfig
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/13 15:40
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class MethodSecurityAutoConfiguration extends GlobalMethodSecurityConfiguration implements
	ApplicationContextAware, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(MethodSecurityAutoConfiguration.class,
			StarterName.SECURITY_SPRINGSECURITY_STARTER);
	}

	private ApplicationContext applicationContext;

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setPermissionEvaluator(new StandardPermissionEvaluator());
		expressionHandler.setApplicationContext(this.applicationContext);
		return expressionHandler;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean(name = "pms")
	public PermissionVerifier permissionVerifier() {
		return new PermissionVerifier();
	}

	/**
	 * 权限验证器
	 *
	 * @author shuigedeng
	 * @version 2022.06
	 * @since 2022-06-08 11:28:55
	 */
	public static class PermissionVerifier {

		//@PreAuthorize("@pms.hasPermission(#request, authentication, 'export')")
		public boolean hasPermission(HttpServletRequest req, Authentication authentication,
			String permission) {
			return false;
		}

		//@PreAuthorize("@pms.hasPermission('export')")
		public boolean hasPermission(String permission) {
			// 内部调用直接跳过
			String header = RequestUtils.getHeader(CommonConstant.TAOTAO_CLOUD_FROM_INNER);
			if (Boolean.TRUE.equals(Boolean.valueOf(header))) {
				return true;
			}

			Collection<? extends GrantedAuthority> authorities = SecurityUtils.getAuthentication()
				.getAuthorities();
			if (CollectionUtil.isEmpty(authorities)) {
				return false;
			}

			for (GrantedAuthority grantedAuthority : authorities) {
				String authority = grantedAuthority.getAuthority();
				if (authority.contains(permission)) {
					return true;
				}
			}
			return false;
		}
	}

	public static class StandardPermissionEvaluator implements PermissionEvaluator {

		/**
		 * 用于SpEL表达式解析.
		 */
		private final static SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

		//普通的targetDomainObject判断 @PreAuthorize("hasPermission(#batchDTO, 'batch')")
		@Override
		public boolean hasPermission(Authentication auth, Object targetDomainObject,
			Object permission) {
			if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
				return false;
			}
			String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
			return hasPrivilege(auth, targetType, permission.toString().toUpperCase());
		}

		//用于ACL的访问控制 @PreAuthorize("hasPermission(1, #batchDTO, 'batch')")
		@Override
		public boolean hasPermission(Authentication auth, Serializable targetId, String targetType,
			Object permission) {

			if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
				return false;
			}
			return hasPrivilege(auth, targetType.toUpperCase(),
				permission.toString().toUpperCase());
		}

		private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
			//内部微服务调用直接返回true
			String header = RequestUtils.getHeader(CommonConstant.TAOTAO_CLOUD_FROM_INNER);
			if (Boolean.TRUE.equals(Boolean.valueOf(header))) {
				return true;
			}

			for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
				if (grantedAuth.getAuthority().contains(permission)) {
					return true;
				}
			}
			return false;
		}

		public boolean checkInner() {
			HttpServletRequest request = RequestUtils.getRequest();

			return true;
		}
	}
}
