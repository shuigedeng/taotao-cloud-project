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

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * MyTask
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/11/12 16:51
 */
public class MyTask extends DefaultTask {

	public MyTask() {
		super.setDescription("test build src");
	}

	@TaskAction
	public void start() {
		System.out.println("MyTask start");
	}

}
