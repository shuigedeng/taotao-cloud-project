package com.taotao.cloud.mongodb.helper.utils;

import cn.hutool.system.SystemUtil;

/**
 * SystemTool 
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-10 22:15:12
 */
public class SystemTool {

	public static String getSystem() {

		if (SystemUtil.get(SystemUtil.OS_NAME).toLowerCase().contains("windows")) {
			return "Windows";
		} else if (SystemUtil.get(SystemUtil.OS_NAME).toLowerCase().contains("mac os")) {
			return "Mac OS";
		} else {
			return "Linux";
		}
	}

	public static Boolean isWindows() {
		return getSystem().equals("Windows");
	}

	public static Boolean isMacOS() {
		return getSystem().equals("Mac OS");
	}

	public static Boolean isLinux() {
		return getSystem().equals("Linux");
	}

	public static boolean hasRoot() {
		if (SystemTool.isLinux()) {
			String user = System.getProperties().getProperty(SystemUtil.USER_NAME);
			return "root".equals(user);
		}
		return true;
	}

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

	public static String deduceMainApplicationClassName(){
		Class<?> aClass = deduceMainApplicationClass();
		assert aClass != null;
		return aClass.getName();
	}
}
