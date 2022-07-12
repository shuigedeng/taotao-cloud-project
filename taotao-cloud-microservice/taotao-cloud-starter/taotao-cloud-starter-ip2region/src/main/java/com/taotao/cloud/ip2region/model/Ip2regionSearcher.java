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

package com.taotao.cloud.ip2region.model;

import com.taotao.cloud.ip2region.utils.IpInfoUtil;
import java.util.function.Function;
import org.springframework.lang.Nullable;

/**
 * ip 搜索器
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-28 17:41:54
 */
public interface Ip2regionSearcher {

	/**
	 * ip 位置 搜索
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo memorySearch(long ip);

	/**
	 * ip 位置 搜索
	 *
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo memorySearch(String ip);

	/**
	 * 读取 ipInfo 中的信息
	 *
	 * @param ip       ip
	 * @param function Function
	 * @return 地址
	 */
	@Nullable
	default String getInfo(long ip, Function<IpInfo, String> function) {
		return IpInfoUtil.readInfo(memorySearch(ip), function);
	}

	/**
	 * 读取 ipInfo 中的信息
	 *
	 * @param ip       ip
	 * @param function Function
	 * @return 地址
	 */
	@Nullable
	default String getInfo(String ip, Function<IpInfo, String> function) {
		return IpInfoUtil.readInfo(memorySearch(ip), function);
	}

	/**
	 * 获取地址信息
	 *
	 * @param ip ip
	 * @return 地址
	 */
	@Nullable
	default String getAddress(long ip) {
		return getInfo(ip, IpInfo::getAddress);
	}

	/**
	 * 获取地址信息
	 *
	 * @param ip ip
	 * @return 地址
	 */
	@Nullable
	default String getAddress(String ip) {
		return getInfo(ip, IpInfo::getAddress);
	}

	/**
	 * 获取地址信息包含 isp
	 *
	 * @param ip ip
	 * @return 地址
	 */
	@Nullable
	default String getAddressAndIsp(long ip) {
		return getInfo(ip, IpInfo::getAddressAndIsp);
	}

	/**
	 * 获取地址信息包含 isp
	 *
	 * @param ip ip
	 * @return 地址
	 */
	@Nullable
	default String getAddressAndIsp(String ip) {
		return getInfo(ip, IpInfo::getAddressAndIsp);
	}

}
