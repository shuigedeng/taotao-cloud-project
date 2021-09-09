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

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.StarterNameConstant;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 * LogUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 16:34:50
 */
public class LogUtil {

	private LogUtil() {
	}

	/**
	 * 空数组
	 */
	private static final Object[] EMPTY_ARRAY = new Object[]{};
	/**
	 * 全类名
	 */
	private static final String FQDN = LogUtil.class.getName();

	/**
	 * 获取栈中类信息
	 *
	 * @return {@link org.slf4j.spi.LocationAwareLogger }
	 * @author shuigedeng
	 * @since 2021-09-02 16:34:58
	 */
	public static LocationAwareLogger getLocationAwareLogger() {
		StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
		StackTraceElement frame = stackTraceElement[stackTraceElement.length - 1];

		return (LocationAwareLogger) LoggerFactory.getLogger(frame.getClassName() + "-" +
			frame.getMethodName().split("\\$")[0] + "-" + frame.getLineNumber());
	}

	/**
	 * Debug级别日志
	 *
	 * @param msg       msg
	 * @param arguments 参数
	 * @author shuigedeng
	 * @since 2021-09-02 16:35:04
	 */
	public static void debug(String msg, Object... arguments) {
		if (isDebugEnabled()) {
			//if (arguments != null && arguments.length > 0) {
			//	msg = MessageFormatter.format(msg, arguments).getMessage();
			//}
			getLocationAwareLogger()
				.log(null, FQDN, LocationAwareLogger.DEBUG_INT, msg, arguments, null);
		}
	}

	/**
	 * Info级别日志
	 *
	 * @param msg       msg
	 * @param arguments 参数
	 * @author shuigedeng
	 * @since 2021-09-02 16:35:18
	 */
	public static void info(String msg, Object... arguments) {
		if (isInfoEnabled()) {
			getLocationAwareLogger()
				.log(null, FQDN, LocationAwareLogger.INFO_INT, msg, arguments, null);
		}
	}

	/**
	 * started
	 *
	 * @param cls     cls
	 * @param project project
	 * @param message message
	 * @author shuigedeng
	 * @since 2021-09-02 16:35:32
	 */
	public static void started(Class<?> cls, String project, String... message) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(project).append("] ");
		sb.append("[").append(cls.getName()).append("] ");

		if (message.length > 0) {
			sb.append(Arrays.toString(message) + " ");
		}

		sb.append(StarterNameConstant.STARTED);
		info(sb.toString());
	}

	/**
	 * Warn级别日志
	 *
	 * @param msg       msg
	 * @param arguments 参数
	 * @author shuigedeng
	 * @since 2021-09-02 16:35:37
	 */
	public static void warn(String msg, Object... arguments) {
		if (isWarnEnabled()) {
			getLocationAwareLogger()
				.log(null, FQDN, LocationAwareLogger.WARN_INT, msg, arguments, null);
		}
	}

	/**
	 * error
	 *
	 * @param error     error
	 * @param msg       msg
	 * @param arguments arguments
	 * @author shuigedeng
	 * @since 2021-09-02 16:35:43
	 */
	public static void error(Throwable error, String msg, Object... arguments) {
		if (isErrorEnabled()) {
			getLocationAwareLogger()
				.log(null, FQDN, LocationAwareLogger.ERROR_INT, msg, arguments, error);
		}
	}

	/**
	 * Error级别日志
	 *
	 * @param error error
	 * @author shuigedeng
	 * @since 2021-09-02 16:35:51
	 */
	public static void error(Throwable error) {
		if (isErrorEnabled()) {
			getLocationAwareLogger()
				.log(null, FQDN, LocationAwareLogger.ERROR_INT, null, EMPTY_ARRAY, error);
		}
	}

	/**
	 * Error级别日志
	 *
	 * @param msg       msg
	 * @param arguments 参数
	 * @author shuigedeng
	 * @since 2021-09-02 16:35:57
	 */
	public static void error(String msg, Object... arguments) {
		if (isErrorEnabled()) {
			getLocationAwareLogger()
				.log(null, FQDN, LocationAwareLogger.ERROR_INT, msg, arguments, null);
		}
	}

	/**
	 * 异常堆栈转字符串
	 *
	 * @param e e
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 16:36:06
	 */
	public static String exceptionToString(Exception e) {
		if (e == null) {
			return "无具体异常信息";
		}
		try (StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);) {
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 获取堆栈信息
	 *
	 * @param throwable throwable
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 16:36:12
	 */
	public static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		try (PrintWriter pw = new PrintWriter(sw)) {
			throwable.printStackTrace(pw);
			return sw.toString();
		}
	}

	/**
	 * 获取操作类型
	 *
	 * @param methodName 方法名称
	 * @return int
	 * @author shuigedeng
	 * @since 2021-09-02 16:36:19
	 */
	public static int getOperateType(String methodName) {
		if (methodName.startsWith("get")) {
			return CommonConstant.OPERATE_TYPE_GET;
		}
		if (methodName.startsWith("query")) {
			return CommonConstant.OPERATE_TYPE_GET;
		}
		if (methodName.startsWith("find")) {
			return CommonConstant.OPERATE_TYPE_GET;
		}
		if (methodName.startsWith("select")) {
			return CommonConstant.OPERATE_TYPE_GET;
		}
		if (methodName.startsWith("add")) {
			return CommonConstant.OPERATE_TYPE_SAVE;
		}
		if (methodName.startsWith("save")) {
			return CommonConstant.OPERATE_TYPE_SAVE;
		}
		if (methodName.startsWith("update")) {
			return CommonConstant.OPERATE_TYPE_UPDATE;
		}
		if (methodName.startsWith("delete")) {
			return CommonConstant.OPERATE_TYPE_DELETE;
		}
		return CommonConstant.OPERATE_TYPE_GET;
	}

	/**
	 * isDebugEnabled
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 17:11:49
	 */
	public static boolean isDebugEnabled() {
		return getLocationAwareLogger().isDebugEnabled();
	}

	/**
	 * isInfoEnabled
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 17:11:53
	 */
	public static boolean isInfoEnabled() {
		return getLocationAwareLogger().isInfoEnabled();
	}

	/**
	 * isErrorEnabled
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 17:11:56
	 */
	public static boolean isErrorEnabled() {
		return getLocationAwareLogger().isErrorEnabled();
	}

	/**
	 * isWarnEnabled
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 17:11:58
	 */
	public static boolean isWarnEnabled() {
		return getLocationAwareLogger().isWarnEnabled();
	}
}
