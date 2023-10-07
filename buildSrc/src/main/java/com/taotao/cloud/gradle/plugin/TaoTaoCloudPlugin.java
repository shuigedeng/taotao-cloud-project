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

package com.taotao.cloud.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * TaoTaoCloudPlugin
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/2/4 下午2:54
 */
public class TaoTaoCloudPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		String name = project.getName();
		Object version = project.getVersion();

		if (name.contains("starter")) {
			// System.out.println(project.getName());
		}
	}
}
