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
package com.taotao.cloud.common.enums;

/**
 * EnvironmentTypeEnum
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:27
 **/
public enum EnvironmentTypeEnum {
	//开发环境
	DEV("开发"),
	TEST("测试"),
	//预生产
	PRE("预生产"),
	//生产环境
	PRD("生产"),
	DOCKER("docker");

	private String name;

	EnvironmentTypeEnum(String name) {
		this.name = name;
	}
}
