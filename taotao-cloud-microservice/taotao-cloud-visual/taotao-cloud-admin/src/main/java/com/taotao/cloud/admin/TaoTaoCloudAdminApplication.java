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
package com.taotao.cloud.admin;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * TaoTaoCloudAdminServerApplication
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/26 下午7:55
 */
@EnableDiscoveryClient
@EnableAdminServer
@SpringBootApplication
public class TaoTaoCloudAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudAdminApplication.class, args);
	}

	@Configuration
	public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

		private final String adminContextPath;

		public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
			this.adminContextPath = adminServerProperties.getContextPath();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
			successHandler.setTargetUrlParameter("redirectTo");
			successHandler.setDefaultTargetUrl(adminContextPath + "/");
			http.authorizeRequests()
				//1.配置所有静态资源和登录页可以公开访问
				.antMatchers(adminContextPath + "/assets/**").permitAll()
				.antMatchers(adminContextPath + "/login").permitAll()
				.antMatchers("/actuator/**").permitAll()
				.antMatchers("/actuator").permitAll()
				.antMatchers("/instances").permitAll()
				.antMatchers("/instances/**").permitAll()
				.anyRequest().authenticated()
				.and()
				//2.配置登录和登出路径
				.formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler)
				.and()
				.logout().logoutUrl(adminContextPath + "/logout").and()
				//3.开启http basic支持，admin-client注册时需要使用
				.httpBasic().and()
				.csrf()
				//4.开启基于cookie的csrf保护
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				//5.忽略这些路径的csrf保护以便admin-client注册
				.ignoringAntMatchers(
					adminContextPath + "/instances",
					adminContextPath + "/actuator/**"
				);
		}
	}
}
