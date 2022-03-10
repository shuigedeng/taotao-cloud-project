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
package com.taotao.cloud.core.launch;

import com.taotao.cloud.common.utils.exception.ExceptionUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import org.apache.commons.io.Charsets;

/**
 * 替换 系统 System.err 和 System.out 为log
 *
 */
public class LogPrintStream extends PrintStream {

	private final boolean error;

	private LogPrintStream(boolean error) throws UnsupportedEncodingException {
		super(error ? System.err : System.out, false, Charsets.UTF_8.name());
		this.error = error;
	}

	public static LogPrintStream log(boolean isError) {
		try {
			return new LogPrintStream(isError);
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

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
	 */
	@Override
	public void println() {
	}

	@Override
	public void println(String x) {
		if (error) {
			LogUtil.error(x);
		} else {
			LogUtil.info(x);
		}
	}

	@Override
	public PrintStream printf(String format, Object... args) {
		if (error) {
			LogUtil.error(String.format(format, args));
		} else {
			LogUtil.info(String.format(format, args));
		}
		return this;
	}

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
