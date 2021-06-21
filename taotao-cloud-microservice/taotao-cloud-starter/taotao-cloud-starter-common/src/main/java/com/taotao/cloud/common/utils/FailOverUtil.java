package com.taotao.cloud.common.utils;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import lombok.Getter;

/**
 * @创建人 霍钧城
 * @创建时间 2020年12月03日 17:28:00
 * @描述 错误补偿工具类
 */
public class FailOverUtil {

	private final static String name = "补偿工具";

	/**
	 * @描述 错误补偿工具类
	 * @参数 [consumer, c1]
	 * @返回值 T
	 * @创建人 霍钧城
	 * @创建时间 2020/12/25
	 * @修改历史：
	 */
	public static <T> T invoke(Consumer<Result<T>> consumer, Callable<T>... c1) {
		Result<T> result = new Result<>();
		try {
			int times = 0;
			for (int i = 0; i < c1.length; i++) {
				Callable<T> tCallable = c1[i];
				try {
					result.response = tCallable.call();
					result.success = true;
					break;
				} catch (Exception e) {
					result.throwable = e;
					if (i > 0) {
						LogUtil
							.error(name.concat("-失败-补偿次数" + i), ExceptionUtil.getFullStackTrace(e));
					} else {
						LogUtil.error(ExceptionUtil.getFullStackTrace(e));
					}
				}
				times = i + 1;
			}
			if (result.success && times > 0) {
				LogUtil.info(name, "补偿成功，补偿次数：" + times);
			}
		} finally {
			consumer.accept(result);
		}
		return result.response;
	}

	/**
	 * @描述 反馈结果
	 * @参数
	 * @返回值
	 * @创建人 霍钧城
	 * @创建时间 2020/12/25
	 * @修改历史：
	 */
	@Getter
	public static class Result<T> {

		private boolean success = false;
		private Throwable throwable;
		private T response;
	}
}
