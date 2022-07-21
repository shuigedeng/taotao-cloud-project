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

package com.taotao.cloud.ip2region.utils;

import com.taotao.cloud.common.constant.StrPool;
import com.taotao.cloud.ip2region.model.IpInfo;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.springframework.lang.Nullable;

/**
 * ip 信息详情
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-28 17:43:39
 */
public class IpInfoUtil {

	private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");

	/**
	 * 将 DataBlock 转化为 IpInfo
	 *
	 * @param region region
	 * @return IpInfo
	 */
	@Nullable
	public static IpInfo toIpInfo(@Nullable String region) {
		if (region == null) {
			return null;
		}
		IpInfo ipInfo = new IpInfo();
		String[] splitInfos = SPLIT_PATTERN.split(region);
		// 补齐5位
		if (splitInfos.length < 5) {
			splitInfos = Arrays.copyOf(splitInfos, 5);
		}
		ipInfo.setCountry(filterZero(splitInfos[0]));
		ipInfo.setRegion(filterZero(splitInfos[1]));
		ipInfo.setProvince(filterZero(splitInfos[2]));
		ipInfo.setCity(filterZero(splitInfos[3]));
		ipInfo.setIsp(filterZero(splitInfos[4]));
		return ipInfo;
	}

	/**
	 * 数据过滤，因为 ip2Region 采用 0 填充的没有数据的字段
	 *
	 * @param info info
	 * @return info
	 */
	@Nullable
	private static String filterZero(@Nullable String info) {
		// null 或 0 返回 null
		if (info == null || StrPool.ZERO.equals(info)) {
			return null;
		}
		return info;
	}

	/**
	 * 读取 IpInfo
	 *
	 * @param ipInfo   IpInfo
	 * @param function Function
	 * @return info
	 */
	@Nullable
	public static String readInfo(@Nullable IpInfo ipInfo, Function<IpInfo, String> function) {
		if (ipInfo == null) {
			return null;
		}
		return function.apply(ipInfo);
	}
}
