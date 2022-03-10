/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.security.configuration;

import cn.hutool.core.collection.CollectionUtil;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import java.io.Serializable;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
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
 * @version 1.0.0
 * @since 2021/10/13 15:40
 */
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration implements
	ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler(){
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
		expressionHandler.setApplicationContext(this.applicationContext);
		return expressionHandler;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean(name = "permissionVerifier")
	public PermissionVerifier permissionVerifier(){
		return new PermissionVerifier();
	}

	public static class PermissionVerifier{
		//@PreAuthorize("@permissionVerifier.hasPermission(#request, authentication, 'export')")
		public boolean hasPermission( HttpServletRequest req, Authentication authentication, String permission){
			return false;
		}

		//@PreAuthorize("@permissionVerifier.hasPermission('export')")
		public boolean hasPermission(String permission){
			Collection<? extends GrantedAuthority> authorities = SecurityUtil.getAuthentication()
				.getAuthorities();
			if(CollectionUtil.isEmpty(authorities)){
				return false;
			}

			for (GrantedAuthority grantedAuthority : authorities) {
				String authority = grantedAuthority.getAuthority();
				if(authority.contains(permission)){
					return true;
				}
			}
			return false;
		}
	}

	public static class CustomPermissionEvaluator implements PermissionEvaluator {

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
			return hasPrivilege(auth, targetType.toUpperCase(), permission.toString().toUpperCase());
		}

		private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
			for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
				if (grantedAuth.getAuthority().contains(permission)) {
					return true;
				}
			}
			return false;
		}
	}


}
