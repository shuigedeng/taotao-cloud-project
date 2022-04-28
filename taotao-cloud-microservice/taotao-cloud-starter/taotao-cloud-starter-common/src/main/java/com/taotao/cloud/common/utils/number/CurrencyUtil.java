package com.taotao.cloud.common.utils.number;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * 金额计算工具
 */
public final class CurrencyUtil {

	/**
	 * 默认除法运算精度
	 */
	private static final int DEF_DIV_SCALE = 2;

	/**
	 * 这个类不能实例化
	 */
	private CurrencyUtil() {
	}

	/**
	 * 提供精确的加法运算。
	 *
	 * @return 累加之和
	 */
	public static BigDecimal add(BigDecimal... params) {
		BigDecimal result = BigDecimal.ZERO;
		for (BigDecimal param : params) {
			result = result.add(param).setScale(2, RoundingMode.HALF_UP);
		}
		return result;
	}

	/**
	 * 提供精确的减法运算。
	 *
	 * @return 第一个参数为被减数，其余数字为减数
	 */
	public static BigDecimal sub(BigDecimal... params) {
		BigDecimal result = params[0];
		params = (BigDecimal[]) Arrays.stream(params).skip(1).toArray();
		for (BigDecimal param : params) {
			result = result.subtract(param).setScale(2, RoundingMode.HALF_UP);
		}
		return result;
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
		return v1.multiply(v2).setScale(2, RoundingMode.HALF_UP);
	}

	public static BigDecimal mul(Integer v1, BigDecimal v2) {
		BigDecimal b1 = BigDecimal.valueOf(v1);
		return b1.multiply(v2).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * @param v1    被乘数
	 * @param v2    乘数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的积
	 */
	public static BigDecimal mul(BigDecimal v1, BigDecimal v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
				"The scale must be a positive integer or zero");
		}
		return v1.multiply(v2).setScale(scale, RoundingMode.HALF_UP);
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
	 *
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	// public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
	// 	return div(v1, v2, DEF_DIV_SCALE);
	// }
	public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
		return v1.divide(v2, DEF_DIV_SCALE, RoundingMode.HALF_UP);
	}

	public static BigDecimal div(BigDecimal v1, Integer v2) {
		return v1.divide(BigDecimal.valueOf(v2), DEF_DIV_SCALE, RoundingMode.HALF_UP);
	}

	/**
	 * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 *
	 * @param v1    被除数
	 * @param v2    除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
				"The scale must be a positive integer or zero");
		}
		//如果被除数等于0，则返回0
		if (v2.equals(BigDecimal.ZERO)) {
			return BigDecimal.ZERO;
		}
		return v1.divide(v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 金额转分
	 *
	 * @param money 金额
	 * @return 转换单位为分
	 */
	public static Integer fen(BigDecimal money) {
		BigDecimal price = mul(money, BigDecimal.valueOf(100));
		return price.intValue();
	}

	/**
	 * 金额转分
	 *
	 * @param money 金额
	 * @return double类型分
	 */
	public static BigDecimal reversalFen(BigDecimal money) {
		return div(money, BigDecimal.valueOf(100));
	}
}
