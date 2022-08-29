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
package com.taotao.cloud.common.utils.exception;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * ExceptionUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 16:47:00
 */
public class ExceptionUtils {

	/**
	 * trace2String
	 *
	 * @param t 异常信息
	 * @return 异常信息
	 * @since 2021-09-02 16:47:06
	 */
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

	/**
	 * trace2String
	 *
	 * @param stackTraceElements 栈数据
	 * @return 异常信息
	 * @since 2021-09-02 16:47:16
	 */
	public static String trace2String(StackTraceElement[] stackTraceElements) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement stackTraceElemen : stackTraceElements) {
			sb.append(stackTraceElemen.toString()).append("\n");
		}
		return sb.toString();
	}

	/**
	 * lineSeparator
	 *
	 * @return 行分隔符
	 * @since 2021-09-02 16:47:20
	 */
	private static String lineSeparator() {
		return System.getProperty("line.separator");
	}

	/**
	 * getFullMessage
	 *
	 * @param e 异常对象
	 * @return 异常信息
	 * @since 2021-09-02 16:47:23
	 */
	public static String getFullMessage(Throwable e) {
		if (e == null) {
			return "";
		}
		return "【详细错误】" + lineSeparator() + getDetailMessage(e) + lineSeparator() + "【堆栈打印】"
			+ lineSeparator() + getFullStackTrace(e);
	}

	/**
	 * getFullStackTrace
	 *
	 * @param e 异常对象
	 * @return 异常信息
	 * @since 2021-09-02 16:47:27
	 */
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

	/**
	 * getDetailMessage
	 *
	 * @param ex 异常对象
	 * @return 异常信息
	 * @since 2021-09-02 16:47:33
	 */
	public static String getDetailMessage(Throwable ex) {
		if (ex == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		while (ex != null) {
			sb.append(
				"【" + ex.getClass().getName() + "】→" + StringUtils.nullToEmpty(ex.getMessage())
					+ lineSeparator());
			ex = ex.getCause();
		}
		return sb.toString();
	}

	/**
	 * ignoreException
	 *
	 * @param runnable    runnable
	 * @param isPrintInfo 是否打印信息
	 * @since 2021-09-02 16:47:37
	 */
	public static void ignoreException(Runnable runnable, boolean isPrintInfo) {
		try {
			runnable.run();
		} catch (Exception e) {
			if (!isPrintInfo) {
				LogUtils.error(getFullStackTrace(e));
			}
		}
	}

	/**
	 * ignoreException
	 *
	 * @param runnable runnable
	 * @since 2021-09-02 16:47:44
	 */
	public static void ignoreException(Runnable runnable) {
		ignoreException(runnable, false);
	}

	/**
	 * 将CheckedException转换为UncheckedException.
	 *
	 * @param e 异常信息
	 * @return 异常信息对象
	 */
	public static RuntimeException unchecked(Throwable e) {
		if (e instanceof Error) {
			throw (Error) e;
		} else if (e instanceof IllegalAccessException ||
			e instanceof IllegalArgumentException ||
			e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return runtime(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else if (e instanceof InterruptedException) {
			Thread.currentThread().interrupt();
		}
		return runtime(e);
	}

	/**
	 * 不采用 RuntimeException 包装，直接抛出，使异常更加精准
	 *
	 * @param throwable 异常信息
	 * @return 异常信息对象
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Throwable> T runtime(Throwable throwable) throws T {
		throw (T) throwable;
	}

	/**
	 * 代理异常解包
	 *
	 * @param wrapped 包装过得异常
	 * @return 解包后的异常
	 */
	public static Throwable unwrap(Throwable wrapped) {
		Throwable unwrapped = wrapped;
		while (true) {
			if (unwrapped instanceof InvocationTargetException) {
				unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
			} else if (unwrapped instanceof UndeclaredThrowableException) {
				unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
			} else {
				return unwrapped;
			}
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 *
	 * @param ex 异常信息
	 * @return 异常信息
	 */
	public static String getStackTraceAsString(Throwable ex) {
		FastStringPrintWriter printWriter = new FastStringPrintWriter(512);
		ex.printStackTrace(printWriter);
		return printWriter.toString();
	}


	/**
	 * 解包异常
	 *
	 * @param wrapped 异常
	 * @return 被解包的异常
	 */
	public static Throwable unwrapThrowable(Throwable wrapped) {
		Throwable unwrapped = wrapped;
		while (true) {
			if (unwrapped instanceof InvocationTargetException) {
				unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
			} else if (unwrapped instanceof UndeclaredThrowableException) {
				unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
			} else {
				return unwrapped;
			}
		}
	}


	/**
	 * 抛出运行时不支持的操作异常
	 */
	public static void throwUnsupportedOperationException() {
		throw new UnsupportedOperationException();
	}

	///**
	// * 将CheckedException转换为UncheckedException.
	// * @param ex    ex 异常
	// * @return 运行时异常
	// */
	//public static RuntimeException unchecked(Throwable ex) {
	//	if (ex instanceof RuntimeException) {
	//		return (RuntimeException) ex;
	//	} else {
	//		return new RuntimeException(ex);
	//	}
	//}

	///**
	// * 将ErrorStack转化为String.
	// * @param ex 异常
	// * @return 返回异常内容
	// */
	//public static String getStackTraceAsString(Throwable ex) {
	//	StringWriter stringWriter = new StringWriter();
	//	ex.printStackTrace(new PrintWriter(stringWriter));
	//	return stringWriter.toString();
	//}

	/**
	 * 获取组合本异常信息与底层异常信息的异常描述, 适用于本异常为统一包装异常类，底层异常才是根本原因的情况。
	 *
	 * @param ex 异常
	 * @return 异常信息
	 */
	public static String getErrorMessageWithNestedException(Throwable ex) {
		Throwable nestedException = ex.getCause();
		return ex.getMessage() + " nested exception is " +
			nestedException.getClass().getName() + ":" + nestedException.getMessage();
	}


	/**
	 * 获取异常的Root Cause.
	 *
	 * @param ex 异常
	 * @return 异常的 RootCause.
	 */
	public static Throwable getRootCause(Throwable ex) {
		Throwable cause;
		Throwable result = null;
		while ((cause = ex.getCause()) != null) {
			result = cause;
		}
		return result;
	}


	/**
	 * 判断异常是否由某些底层的异常引起.
	 *
	 * @param ex                    异常
	 * @param causeExceptionClasses 导致的异常原因
	 * @return 是否由某个异常引起
	 */
	@SuppressWarnings("unchecked")
	public static boolean isCausedBy(Exception ex,
		Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex;
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}

	/**
	 * 获取确切的异常信息 1. 主要针对代理报错
	 *
	 * @param throwable 异常
	 * @return 确切的异常信息
	 */
	public static Throwable getActualThrowable(final Throwable throwable) {
		if (InvocationTargetException.class.equals(throwable.getClass())) {
			InvocationTargetException exception = (InvocationTargetException) throwable;
			return exception.getTargetException();
		}
		return throwable;
	}
}
