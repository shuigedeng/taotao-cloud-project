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
package com.taotao.cloud.health.strategy;

import com.taotao.cloud.health.model.Report;

/**
 * WarnStrategy
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:25:31
 */
public interface WarnStrategy {
	/**
	 * 分析
	 *
	 * @param report 报告
	 * @return {@link Report }
	 * @since 2022-04-27 17:25:32
	 */
	Report analyse(Report report);

	/**
	 * 分析文本
	 *
	 * @param report 报告
	 * @return {@link String }
	 * @since 2022-04-27 17:25:32
	 */
	String analyseText(Report report);
}
