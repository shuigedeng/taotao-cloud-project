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
package com.taotao.cloud.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * MyPlugin
 *
 * @author shuigedeng
 * @since 2021/2/4 下午2:54
 * @version 1.0.0
 */
public class MyPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		System.out.println(project.getName());
		System.out.println("33333333333333");
	}
}
