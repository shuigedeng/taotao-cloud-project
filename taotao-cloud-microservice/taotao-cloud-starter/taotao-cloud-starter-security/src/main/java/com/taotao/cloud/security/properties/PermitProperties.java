///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.security.properties;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
///**
// * 配置需要放权的url白名单
// *
// * @author shuigedeng
// * @version 1.0.0
// * @since 2020/5/2 11:21
// */
//@Data
//@ConfigurationProperties(prefix = "taotao.cloud.oauth2.security.ignore")
//public class PermitProperties {
//
//	/**
//	 * 监控中心和swagger需要访问的url
//	 */
//	private static final String[] ENDPOINTS = {
//		"/actuator/**",
//		"/fallback",
//		"/favicon.ico",
//		"/swagger-resources/**",
//		"/webjars/**",
//		"/druid/**",
//		"/*/*.html",
//		"/*/*.css",
//		"/*/*.js",
//		"/*.js",
//		"/*.css",
//		"/*.html",
//		"/*/favicon.ico",
//		"/*/api-docs",
//		"/css/**",
//		"/js/**",
//		"/images/**"
//	};
//
//	/**
//	 * 设置不用认证的url
//	 */
//	private String[] httpUrls = {};
//
//	public String[] getUrls() {
//		if (httpUrls == null || httpUrls.length == 0) {
//			return ENDPOINTS;
//		}
//		List<String> list = new ArrayList<>();
//		list.addAll(Arrays.asList(ENDPOINTS));
//		list.addAll(Arrays.asList(httpUrls));
//		return list.toArray(new String[list.size()]);
//	}
//}
