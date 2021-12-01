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
package com.taotao.cloud.health.utils;


import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * ProcessUtils
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 16:48:07
 */
public class ProcessUtils {

	/**
	 * execCmd
	 *
	 * @param cmd cmd
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-10 16:48:52
	 */
	public static String execCmd(String cmd) {
		if (isWinOs()) {
			return "-1";
		} else {
			try {
				return execCmd(cmd, null);
			} catch (Exception e) {
				return "-2";
			}
		}
	}

	/**
	 * isWinOs
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-10 16:49:54
	 */
	public static boolean isWinOs() {
		String os = System.getProperty("os.name");
		return os.toLowerCase().startsWith("win");
	}

	/**
	 * getProcessID
	 *
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-10 16:50:04
	 */
	public static String getProcessID() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		return runtimeMXBean.getName().split("@")[0];
	}

	/**
	 * 执行系统命令, 返回执行结果
	 *
	 * @param cmd 需要执行的命令
	 * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-10 16:50:24
	 */
	public static String execCmd(String cmd, File dir) {
		StringBuilder result = new StringBuilder();
		Process process = null;
		BufferedReader bufferIn = null;
		try {
			String[] commond = {"sh", "-c", cmd};
			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
			process = Runtime.getRuntime().exec(commond, null, dir);

			// 方法阻塞, 等待命令执行完成（成功会返回0）
			process.waitFor(3, TimeUnit.SECONDS);

			// 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
			bufferIn = new BufferedReader(new InputStreamReader(process.getInputStream(),
				StandardCharsets.UTF_8));

			// 读取输出
			String line;
			while ((line = bufferIn.readLine()) != null) {
				result.append(line).append('\n');
			}

			return result.toString();
		} catch (Exception e) {
			LogUtil.error(StarterName.HEALTH_STARTER, "execCmd", e);
		} finally {
			closeStream(bufferIn);
			// 销毁子进程
			if (process != null) {
				process.destroy();
			}
		}

		return "-3";
	}

	/**
	 * closeStream
	 *
	 * @param stream stream
	 * @author shuigedeng
	 * @since 2021-09-10 16:51:46
	 */
	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception e) {
				LogUtil.error(e);
			}
		}
	}
}

