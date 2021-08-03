package com.taotao.cloud.web.base.request;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.taotao.cloud.common.utils.DateUtils;
import java.util.Map;

/**
 * 分页工具类
 *
 * @author zuihou
 * @date 2020/11/26 10:25 上午
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
				extra.put(key, DateUtils.getStartTime(value.toString()));
			}
			if (key.endsWith("_ed")) {
				extra.put(key, DateUtils.getEndTime(value.toString()));
			}
		}
	}
}
