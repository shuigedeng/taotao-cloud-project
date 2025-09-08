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

package com.taotao.cloud.report.biz.util;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.cloud.report.api.enums.SearchTypeEnum;
import com.taotao.cloud.report.api.model.dto.StatisticsQueryParam;
import java.util.Calendar;
import java.util.Date;

/**
 * 统计缓存后缀工具
 */
public class StatisticsDateUtil {

	/**
	 * 快捷搜索，得到开始时间和结束时间
	 *
	 * @param searchTypeEnum
	 * @return
	 */
	public static Date[] getDateArray(SearchTypeEnum searchTypeEnum) {
		Date[] dateArray = new Date[2];

		Calendar calendar = Calendar.getInstance();
		// 时间归到今天凌晨0点
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		switch (searchTypeEnum) {
			case TODAY:
				dateArray[0] = calendar.getTime();

				calendar.set(Calendar.HOUR_OF_DAY, +24);
				calendar.set(Calendar.MILLISECOND, -1);
				dateArray[1] = calendar.getTime();
				break;

			case YESTERDAY:
				// 获取昨天
				calendar.set(Calendar.HOUR_OF_DAY, -24);
				dateArray[0] = calendar.getTime();

				// 昨天结束时间
				calendar.set(Calendar.HOUR_OF_DAY, +24);
				calendar.set(Calendar.MILLISECOND, -1);
				dateArray[1] = calendar.getTime();
				break;
			case LAST_SEVEN:
				calendar.set(Calendar.HOUR_OF_DAY, -24 * 7);
				dateArray[0] = calendar.getTime();

				calendar.set(Calendar.HOUR_OF_DAY, +24 * 7);
				calendar.set(Calendar.MILLISECOND, -1);
				// 获取过去七天
				dateArray[1] = calendar.getTime();
				break;
			case LAST_THIRTY:
				// 获取最近三十天
				calendar.set(Calendar.HOUR_OF_DAY, -24 * 30);
				dateArray[0] = calendar.getTime();

				calendar.set(Calendar.HOUR_OF_DAY, +24 * 30);
				calendar.set(Calendar.MILLISECOND, -1);
				// 获取过去七天
				dateArray[1] = calendar.getTime();
				break;
			default:
				throw new BusinessException(ResultEnum.ERROR);
		}
		return dateArray;
	}

	/**
	 * 获取年月获取开始结束时间
	 *
	 * @param year  年
	 * @param month 月
	 * @return 返回时间
	 */
	public static Date[] getDateArray(Integer year, Integer month) {
		Date[] dateArray = new Date[2];

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 0);
		dateArray[1] = calendar.getTime();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		dateArray[0] = calendar.getTime();
		return dateArray;
	}

	/**
	 * 根据搜索参数获取搜索开始结束时间
	 *
	 * @param statisticsQueryParam
	 * @return
	 */
	public static Date[] getDateArray(StatisticsQueryParam statisticsQueryParam) {
		// 如果快捷搜搜哦
		if (StringUtils.isNotEmpty(statisticsQueryParam.getSearchType())) {
			return getDateArray(SearchTypeEnum.valueOf(statisticsQueryParam.getSearchType()));
		}
		// 按照年月查询
		else if (statisticsQueryParam.getMonth() != null && statisticsQueryParam.getYear() != null) {
			return getDateArray(statisticsQueryParam.getYear(), statisticsQueryParam.getMonth());
		}
		// 默认查询当前月份
		else {
			Calendar calendar = Calendar.getInstance();
			return getDateArray(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
		}
	}
}
