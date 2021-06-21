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
package com.taotao.cloud.common.utils;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.experimental.UtilityClass;

/**
 * ExceptionUtils
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/6/2 16:35
 **/
@UtilityClass
public class ExceptionUtil {

	public static String trace2String(Throwable t) {
		if (t == null) {
			return "";
		}
		try {
			try (StringWriter sw = new StringWriter()) {
				try (PrintWriter pw = new PrintWriter(sw, true)) {
					t.printStackTrace(pw);
					return sw.getBuffer().toString();
				}
			}
		} catch (Exception exp) {
			throw new BaseException(ResultEnum.ERROR, exp);
		}
	}

	public static String trace2String(StackTraceElement[] stackTraceElements) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement stackTraceElemen : stackTraceElements) {
			sb.append(stackTraceElemen.toString()).append("\n");
		}
		return sb.toString();
	}

	private static String lineSeparator() {
		return System.getProperty("line.separator");
	}

	public static String getFullMessage(Throwable e) {
		if (e == null) {
			return "";
		}
		return "【详细错误】" + lineSeparator() + getDetailMessage(e) + lineSeparator() + "【堆栈打印】"
			+ lineSeparator() + getFullStackTrace(e);
	}

	public static String getFullStackTrace(Throwable e) {
		if (e == null) {
			return "";
		}
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			// 将出错的栈信息输出到printWriter中
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
		return sw.toString();
	}

	public static String getDetailMessage(Throwable ex) {
		if (ex == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		while (ex != null) {
			sb.append(
				"【" + ex.getClass().getName() + "】→" + StringUtil.nullToEmpty(ex.getMessage())
					+ lineSeparator());
			ex = ex.getCause();
		}
		return sb.toString();
	}

	public static void ignoreException(Runnable runnable, boolean isPrintInfo) {
		try {
			runnable.run();
		} catch (Exception e) {
			if (!isPrintInfo) {
				LogUtil.error(getFullStackTrace(e));
			}
		}
	}

	public static void ignoreException(Runnable runnable) {
		ignoreException(runnable, false);
	}
}
