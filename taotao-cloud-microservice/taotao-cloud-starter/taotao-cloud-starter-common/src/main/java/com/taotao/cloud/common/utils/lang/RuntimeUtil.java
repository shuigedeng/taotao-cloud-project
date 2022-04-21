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

package com.taotao.cloud.common.utils.lang;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.taotao.cloud.common.constant.StrPool;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.number.NumberUtil;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * 运行时工具类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class RuntimeUtil {

	private static volatile int pId = -1;

	private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

	/**
	 * 获得当前进程的PID
	 * <p>
	 * 当失败时返回-1
	 *
	 * @return pid
	 */
	public static int getPId() {
		if (pId > 0) {
			return pId;
		}
		// something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
		final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		final int index = jvmName.indexOf(StrPool.AT);
		if (index > 0) {
			pId = NumberUtil.toInt(jvmName.substring(0, index), -1);
			return pId;
		}
		return pId;
	}

	/**
	 * 返回应用启动的时间
	 *
	 * @return {Instant}
	 */
	public static Instant getStartTime() {
		return Instant.ofEpochMilli(ManagementFactory.getRuntimeMXBean().getStartTime());
	}

	/**
	 * 返回应用启动到现在的时间
	 *
	 * @return {Duration}
	 */
	public static Duration getUpTime() {
		return Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
	}

	/**
	 * 返回输入的JVM参数列表
	 *
	 * @return jvm参数
	 */
	public static String getJvmArguments() {
		List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
		return StringUtil.join(vmArguments, StringPool.SPACE);
	}

	/**
	 * 获取CPU核数
	 *
	 * @return cpu count
	 */
	public static int getCpuNum() {
		return CPU_NUM;
	}

}
