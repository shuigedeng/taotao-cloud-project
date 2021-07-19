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
package com.taotao.cloud.common.utils;

import lombok.experimental.UtilityClass;

/**
 * 高效分布式ID生成算法(sequence),基于Snowflake算法优化实现64位自增ID算法。 其中解决时间回拨问题的优化方案如下： 1.
 * 如果发现当前时间少于上次生成id的时间(时间回拨)，着计算回拨的时间差 2. 如果时间差(offset)小于等于5ms，着等待 offset * 2 的时间再生成 3.
 * 如果offset大于5，则直接抛出异常
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2019/3/5
 */
@UtilityClass
public class IdGeneratorUtil {

	private final SequenceUtil WORKER = new SequenceUtil();

	/**
	 * 获取id
	 *
	 * @return long
	 * @author shuigedeng
	 * @since 2021/2/25 16:18
	 */
	public long getId() {
		return WORKER.nextId();
	}

	/**
	 * 获取id 字符串
	 *
	 * @return java.lang.String
	 * @author shuigedeng
	 * @since 2021/2/25 16:19
	 */
	public String getIdStr() {
		return String.valueOf(WORKER.nextId());
	}
}
