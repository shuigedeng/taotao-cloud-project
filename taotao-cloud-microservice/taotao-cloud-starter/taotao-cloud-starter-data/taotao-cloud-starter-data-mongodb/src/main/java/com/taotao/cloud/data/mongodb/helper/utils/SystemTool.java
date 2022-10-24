package com.taotao.cloud.data.mongodb.helper.utils;

import cn.hutool.system.SystemUtil;

/**
 * SystemTool
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-27 21:55:22
 */
public class SystemTool {

	/**
	 * 得到系统
	 *
	 * @return {@link String }
	 * @since 2022-05-27 21:55:22
	 */
	public static String getSystem() {

		if (SystemUtil.get(SystemUtil.OS_NAME).toLowerCase().contains("windows")) {
			return "Windows";
		} else if (SystemUtil.get(SystemUtil.OS_NAME).toLowerCase().contains("mac os")) {
			return "Mac OS";
		} else {
			return "Linux";
		}
	}

	/**
	 * 是窗户
	 *
	 * @return {@link Boolean }
	 * @since 2022-05-27 21:55:22
	 */
	public static Boolean isWindows() {
		return getSystem().equals("Windows");
	}

	/**
	 * 是mac os
	 *
	 * @return {@link Boolean }
	 * @since 2022-05-27 21:55:22
	 */
	public static Boolean isMacOS() {
		return getSystem().equals("Mac OS");
	}

	/**
	 * 是linux
	 *
	 * @return {@link Boolean }
	 * @since 2022-05-27 21:55:22
	 */
	public static Boolean isLinux() {
		return getSystem().equals("Linux");
	}

	/**
	 * 有根
	 *
	 * @return boolean
	 * @since 2022-05-27 21:55:22
	 */
	public static boolean hasRoot() {
		if (SystemTool.isLinux()) {
			String user = System.getProperties().getProperty(SystemUtil.USER_NAME);
			return "root".equals(user);
		}
		return true;
	}

	/**
	 * 推断主要应用程序类
	 *
	 * @return {@link Class }<{@link ? }>
	 * @since 2022-05-27 21:55:22
	 */
	public static Class<?> deduceMainApplicationClass() {
		try {
			StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
			for (StackTraceElement stackTraceElement : stackTrace) {
				if ("main".equals(stackTraceElement.getMethodName())) {
					return Class.forName(stackTraceElement.getClassName());
				}
			}
		}
		catch (ClassNotFoundException ex) {
			// Swallow and continue
		}
		return null;
	}

	/**
	 * 推断主应用程序类名
	 *
	 * @return {@link String }
	 * @since 2022-05-27 21:55:22
	 */
	public static String deduceMainApplicationClassName(){
		Class<?> aClass = deduceMainApplicationClass();
		assert aClass != null;
		return aClass.getName();
	}
}
