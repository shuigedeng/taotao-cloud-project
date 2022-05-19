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
package com.taotao.cloud.common.utils.common;


import com.github.yitter.idgen.YitIdHelper;
import com.taotao.cloud.common.utils.date.DateUtil;
import java.util.Date;

/**
 * 高效分布式ID生成算法(sequence),基于Snowflake算法优化实现64位自增ID算法。 其中解决时间回拨问题的优化方案如下： 1.
 * 如果发现当前时间少于上次生成id的时间(时间回拨)，着计算回拨的时间差 2. 如果时间差(offset)小于等于5ms，着等待 offset * 2 的时间再生成 3.
 * 如果offset大于5，则直接抛出异常
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 16:42:02
 */
public final class IdGeneratorUtil {

	private IdGeneratorUtil() {
	}

	/**
	 * 获取id
	 *
	 * @return long
	 * @since 2021-09-02 16:43:10
	 */
	public static long getId() {
		return YitIdHelper.nextId();
	}

	/**
	 * 获取id 字符串
	 *
	 * @return id 字符串
	 * @since 2021-09-02 16:43:20
	 */
	public static String getIdStr() {
		return String.valueOf(YitIdHelper.nextId());
	}

	/**
	 * 生成字符，带有前缀
	 *
	 * @param prefix 前缀
	 * @return {@link String }
	 * @since 2022-05-16 17:51:07
	 */
	public static String createStr(String prefix) {
		return prefix + DateUtil.toString(new Date(), "yyyyMMdd") + getId();
	}
}
