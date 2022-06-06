package com.taotao.cloud.health.alarm.core.loader.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 属性util
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-09 15:13:03
 */
public class PropertiesUtil {

	/**
	 * 读取properties文件的内容
	 *
	 * @param fileName 文件名
	 * @return {@link Properties }
	 * @since 2022-05-09 15:13:03
	 */
	public static Properties read(String fileName) throws IOException {
		try (InputStream inputStream = PropertiesUtil.class.getClassLoader()
			.getResourceAsStream(fileName)) {
			Properties pro = new Properties();
			pro.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			return pro;
		}
	}

	/**
	 * 目前只提供基本属性的拷贝
	 *
	 * @param source
	 * @param dest
	 * @since 2022-05-09 15:13:03
	 */
	public static void copy(Properties source, Object dest) throws IllegalAccessException {
		Field[] fields = dest.getClass().getDeclaredFields();
		for (Field f : fields) {
			// 不修改静态变量
			if (Modifier.isStatic(f.getModifiers())) {
				continue;
			}

			f.setAccessible(true);
			f.set(dest, parseObj(source.getProperty(f.getName()), f.getType()));
		}
	}


	/**
	 * 解析obj
	 *
	 * @param obj obj
	 * @param clz clz
	 * @return {@link T }
	 * @since 2022-05-09 15:13:04
	 */
	private static <T> T parseObj(String obj, Class<T> clz) {
		return ParseFuncEnum.getFunc(clz).apply(obj);
	}

	/**
	 * 解析函数枚举
	 *
	 * @author shuigedeng
	 * @version 2022.05
	 * @since 2022-05-09 15:13:04
	 */
	public enum ParseFuncEnum {

		INT_PARSE(Arrays.asList(int.class, Integer.class)) {
			@Override
			public Function<String, Integer> getFunc() {
				return Integer::valueOf;
			}
		},
		LONG_PARSE(Arrays.asList(long.class, Long.class)) {
			@Override
			public Function<String, Long> getFunc() {
				return Long::valueOf;
			}
		},
		BOOLEAN_PARSE(Arrays.asList(boolean.class, Boolean.class)) {
			@Override
			public Function<String, Boolean> getFunc() {
				return Boolean::valueOf;
			}
		},
		FLOAT_PARSE(Arrays.asList(float.class, Float.class)) {
			@Override
			public Function<String, Float> getFunc() {
				return Float::valueOf;
			}
		},
		DOUBLE_PARSSE(Arrays.asList(double.class, Double.class)) {
			@Override
			public Function<String, Double> getFunc() {
				return Double::valueOf;
			}
		},
		SHORT_PARSE(Arrays.asList(short.class, Short.class)) {
			@Override
			public Function<String, Short> getFunc() {
				return Short::valueOf;
			}
		},
		BYTE_PARSE(Arrays.asList(byte.class, Byte.class)) {
			@Override
			public Function<String, Byte> getFunc() {
				return Byte::valueOf;
			}
		},
		CHAR_PARSE(Arrays.asList(char.class, Character.class)) {
			@Override
			public Function<String, Character> getFunc() {
				return s -> s.charAt(0);
			}
		},
		STRING_PARSE(Arrays.asList(String.class)) {
			@Override
			public Function<String, String> getFunc() {
				return s -> s;
			}
		},
		;


		private List<Class> clzList;


		/**
		 * 获取函数
		 *
		 * @return {@link Function }<{@link String }, {@link T }>
		 * @since 2022-05-09 15:13:04
		 */
		public abstract <T> Function<String, T> getFunc();


		private static Map<Class, ParseFuncEnum> map = new ConcurrentHashMap<>(20);

		static {
			for (ParseFuncEnum enu : ParseFuncEnum.values()) {
				for (Class clz : enu.clzList) {
					map.put(clz, enu);
				}
			}
		}

		/**
		 * 解析函数枚举
		 *
		 * @param clz clz
		 * @return
		 * @since 2022-05-09 15:13:04
		 */
		ParseFuncEnum(List<Class> clz) {
			this.clzList = clz;
		}

		/**
		 * 获取函数
		 *
		 * @param clz clz
		 * @return {@link Function }<{@link String }, {@link T }>
		 * @since 2022-05-09 15:13:04
		 */
		public static <T> Function<String, T> getFunc(Class<T> clz) {
			return map.get(clz).getFunc();
		}

	}

}
