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
package com.taotao.cloud.security.component;

import cn.hutool.core.collection.CollectionUtil;
import com.taotao.cloud.common.utils.SecurityUtil;
import java.util.Collection;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * MethodSecurityConfig
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/10/13 15:40
 */
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration implements
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


}
