/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.web.strategy;

import org.apache.poi.ss.formula.functions.T;

/**
 * 业务处理策略接口
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:15:21
 */
@FunctionalInterface
public interface BusinessHandler {

	/**
	 * 业务处理
	 *
	 * @param t 业务实体返回参数
	 * @return R
	 * @since 2021-09-02 22:15:31
	 */
	<R> R businessHandler(T t);
}
