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
//import java.util.List;
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
///**
// * 续签配置
// *
// * @author shuigedeng
// * @version 1.0.0
// * @since 2020/5/2 11:21
// */
//@Data
//@ConfigurationProperties(prefix = "taotao.cloud.oauth2.security.auth.renew")
//public class RenewProperties {
//
//	/**
//	 * 是否开启token自动续签（目前只有redis实现）
//	 */
//	private Boolean enabled = false;
//
//	/**
//	 * 白名单，配置需要自动续签的应用id（与黑名单互斥，只能配置其中一个），不配置默认所有应用都生效 配置enable为true时才生效
//	 */
//	private List<String> includeClientIds = new ArrayList<>();
//
//	/**
//	 * 黑名单，配置不需要自动续签的应用id（与白名单互斥，只能配置其中一个） 配置enable为true时才生效
//	 */
//	private List<String> exclusiveClientIds = new ArrayList<>();
//
//	/**
//	 * 续签时间比例，当前剩余时间小于小于过期总时长的50%则续签
//	 */
//	private Double timeRatio = 0.5;
//}
