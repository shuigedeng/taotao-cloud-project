package com.taotao.cloud.common.utils.lang;


import com.taotao.cloud.common.constant.PunctuationConst;
import com.taotao.cloud.common.utils.collection.ArrayUtil;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.date.DateUtil;
import com.taotao.cloud.common.utils.guava.Guavas;
import java.util.List;

/**
 * 日志工具类
 */
public final class ConsoleUtil {

	private ConsoleUtil() {
	}

	/**
	 * 单行日志信息
	 */
	public static final String LINE = "--------------------------------------------------------";

	/**
	 * 输出文档
	 *
	 * @param className  类名
	 * @param methodName 方法名
	 * @param format     文本格式化
	 * @param args       参数
	 */
	public static void info(final String className,
		final String methodName,
		final String format,
		final Object... args) {
		String formatStr = buildString(format, args);
		log("INFO", className, methodName, formatStr, null);
	}

	/**
	 * 输出文档
	 *
	 * @param format 文本格式化
	 * @param args   参数
	 */
	public static void info(final String format,
		final Object... args) {
		StackTraceElement callMethodElem = Thread.currentThread().getStackTrace()[3];
		String className = callMethodElem.getClassName();
		final String methodNameName = callMethodElem.getMethodName();

		info(className, methodNameName, format, args);
	}

	/**
	 * 格式化信息
	 *
	 * @param format 格式化
	 * @param params 参数
	 * @return 结果
	 */
	private static String buildString(String format, Object[] params) {
		String stringFormat = format;

		for (int i = 0; i < params.length; ++i) {
			stringFormat = stringFormat.replaceFirst("\\{}", "%s");
		}

		return String.format(stringFormat, params);
	}

	/**
	 * 消息打印
	 * <p>
	 * final String threadName = Thread.currentThread().getName();
	 *
	 * @param level     消息等级
	 * @param content   内容
	 * @param throwable 异常
	 */
	private static void log(final String level,
		final String className,
		final String methodName,
		String content,
		Throwable throwable) {
		final String prettyMethod = buildPrettyMethodName(className, methodName);

		String dateStr = DateUtil.getCurrentDateTimeStr();
		String log = String.format("[%s] [%s] [%s] - %s", level, dateStr, prettyMethod, content);
		if ("ERROR".equalsIgnoreCase(level)) {
			System.err.println(log);
		} else {
			System.out.println(log);
		}

		if (throwable != null) {
			throwable.printStackTrace(System.err);
		}
	}

	/**
	 * 构建更加优雅的方法名称 （1）className 只取首字母
	 *
	 * @param className  类名
	 * @param methodName 方法名称
	 * @return 结果
	 */
	private static String buildPrettyMethodName(final String className, final String methodName) {
		String[] classNames = className.split("\\.");
		if (ArrayUtil.isEmpty(classNames)) {
			return methodName;
		}

		final int length = classNames.length;
		if (length == 1) {
			return className + PunctuationConst.DOT + methodName;
		}

		// 类名超过一个
		List<String> classFirstChars = Guavas.newArrayList(length);
		for (int i = 0; i < length - 1; i++) {
			String name = classNames[i];
			classFirstChars.add(String.valueOf(name.charAt(0)));
		}
		classFirstChars.add(classNames[length - 1]);
		String prettyClass = CollectionUtil.join(classFirstChars, PunctuationConst.DOT);
		return prettyClass + PunctuationConst.DOT + methodName;
	}

}
