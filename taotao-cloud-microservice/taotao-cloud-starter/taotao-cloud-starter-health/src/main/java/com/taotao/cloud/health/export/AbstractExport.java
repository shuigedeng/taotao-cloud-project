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
package com.taotao.cloud.health.export;

import com.taotao.cloud.health.model.Report;

/**
 * AbstractExport
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:26:29
 */
public class AbstractExport implements AutoCloseable {

	/**
	 * start
	 *
	 * @since 2022-04-27 17:26:29
	 */
	public void start() {

	}

	/**
	 * run
	 *
	 * @param report report
	 * @since 2022-04-27 17:26:29
	 */
	public void run(Report report) {

	}

	/**
	 * 关闭
	 *
	 * @since 2022-04-27 17:26:29
	 */
	@Override
	public void close() {

	}
}
