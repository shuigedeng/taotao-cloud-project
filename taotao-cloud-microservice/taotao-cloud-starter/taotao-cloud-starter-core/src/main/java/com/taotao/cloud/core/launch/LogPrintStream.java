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
package com.taotao.cloud.core.launch;

import com.taotao.cloud.common.utils.exception.ExceptionUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.apache.commons.io.Charsets;

/**
 * 替换 系统 System.err 和 System.out 为log
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-30 13:28:49
 */
public class LogPrintStream extends PrintStream {

	/**
	 * 错误
	 */
	private final boolean error;

	/**
	 * 日志打印流
	 *
	 * @param error 错误
	 * @since 2022-05-30 13:29:07
	 */
	private LogPrintStream(boolean error) throws UnsupportedEncodingException {
		super(error ? System.err : System.out, false, StandardCharsets.UTF_8);
		this.error = error;
	}

	/**
	 * 日志
	 *
	 * @param isError 是错误
	 * @return {@link LogPrintStream }
	 * @since 2022-05-30 13:29:08
	 */
	public static LogPrintStream log(boolean isError) {
		try {
			return new LogPrintStream(isError);
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 打印
	 *
	 * @param s 年代
	 * @since 2022-05-30 13:29:08
	 */
	@Override
	public void print(String s) {
		if (error) {
			LogUtil.error(s);
		} else {
			LogUtil.info(s);
		}
	}

	/**
	 * 重写掉它，因为它会打印很多无用的新行
	 *
	 * @since 2022-05-30 13:29:08
	 */
	@Override
	public void println() {
	}

	/**
	 * println
	 *
	 * @param x x
	 * @since 2022-05-30 13:29:09
	 */
	@Override
	public void println(String x) {
		if (error) {
			LogUtil.error(x);
		} else {
			LogUtil.info(x);
		}
	}

	/**
	 * printf
	 *
	 * @param format 格式
	 * @param args   arg游戏
	 * @return {@link PrintStream }
	 * @since 2022-05-30 13:29:09
	 */
	@Override
	public PrintStream printf(String format, Object... args) {
		if (error) {
			LogUtil.error(String.format(format, args));
		} else {
			LogUtil.info(String.format(format, args));
		}
		return this;
	}

	/**
	 * printf
	 *
	 * @param l      l
	 * @param format 格式
	 * @param args   arg游戏
	 * @return {@link PrintStream }
	 * @since 2022-05-30 13:29:09
	 */
	@Override
	public PrintStream printf(Locale l, String format, Object... args) {
		if (error) {
			LogUtil.error(String.format(l, format, args));
		} else {
			LogUtil.info(String.format(l, format, args));
		}
		return this;
	}
}
