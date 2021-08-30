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
package com.taotao.cloud.web.base.request;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.taotao.cloud.common.utils.DateUtil;
import java.util.Map;

/**
 * 分页工具类
 *
 * @version 1.0.0
 * @author shuigedeng
 * @since 2021/8/25 08:21
 */
public class PageUtil {

	private PageUtil() {
	}

	/**
	 * 重置时间区间参数
	 *
	 * @param params 分页参数
	 */
	public static <T> void timeRange(PageParams<T> params) {
		if (params == null) {
			return;
		}
		Map<String, Object> extra = params.getExtra();
		if (MapUtil.isEmpty(extra)) {
			return;
		}
		for (Map.Entry<String, Object> field : extra.entrySet()) {
			String key = field.getKey();
			Object value = field.getValue();
			if (ObjectUtil.isEmpty(value)) {
				continue;
			}
			if (key.endsWith("_st")) {
				extra.put(key, DateUtil.getStartTime(value.toString()));
			}
			if (key.endsWith("_ed")) {
				extra.put(key, DateUtil.getEndTime(value.toString()));
			}
		}
	}
}
