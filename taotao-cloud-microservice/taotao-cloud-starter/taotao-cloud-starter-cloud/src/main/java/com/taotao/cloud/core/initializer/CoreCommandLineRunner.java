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
package com.taotao.cloud.core.initializer;

import com.taotao.cloud.common.utils.LogUtil;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.boot.CommandLineRunner;

/**
 * CoreCommandLineRunner
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/06/22 10:53
 */
public class CoreCommandLineRunner implements CommandLineRunner {

	@Override
	public void run(String... args) {
		String strArgs = Arrays.stream(args).collect(Collectors.joining("|"));
		LogUtil.info("Application started with arguments:" + strArgs);
	}
}
