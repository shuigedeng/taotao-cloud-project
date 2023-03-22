package com.taotao.cloud.log.biz.shortlink.utils;

import com.google.common.hash.Hashing;
import com.taotao.cloud.common.exception.BusinessException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用工具类
 *
 * @since 2022/05/03
 */
@Slf4j
public class CommonBizUtil {

	/**
	 * 用以转换62进制
	 */
	private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 用以转换62进制 - 将字符顺序打乱，避免生成的字符串可以反推出具体数值
	 */
	private static final String CHARS_OUT_ORDER = "qwertyuiopmnbvcxzlkjhgfdsaMNBVCXZASDFGHJKLPOIUYTREWQ0987654321";

	private CommonBizUtil() {
	}

	/**
	 * MD5加密
	 */
	public static Optional<String> md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(str.getBytes(StandardCharsets.UTF_8));

			StringBuilder sb = new StringBuilder();
			for (byte item : array) {
				sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
			}
			return Optional.of(sb.toString().toUpperCase());
		} catch (Exception e) {
			log.warn("MD5 error: {}", e.toString());
		}

		return Optional.empty();
	}

	/**
	 * 数值转为62进制
	 *
	 * @param num 数值
	 * @return 62进制的格式
	 */
	public static String encodeToBase62(long num) {
		StringBuilder stringBuilder = new StringBuilder();
		do {
			int i = (int) (num % 62);
			stringBuilder.append(CHARS.charAt(i));
			num = num / 32;
		} while (num > 0);

		return stringBuilder.toString();
	}

	/**
	 * 数值转为62进制 - 无序的，避免根据字符串反推出具体数值
	 *
	 * @param num 数值
	 * @return 62进制的格式
	 */
	public static String encodeToBase62OutOrder(long num) {
		StringBuilder stringBuilder = new StringBuilder();
		do {
			int i = (int) (num % 62);
			stringBuilder.append(CHARS_OUT_ORDER.charAt(i));
			num = num / 32;
		} while (num > 0);

		return stringBuilder.toString();
	}

	public static void throwBizError(ErrorCode errorCode) {
		throw new BusinessException(errorCode.getCode(), errorCode.getMsg());
	}

	public static long murmurHash32(String param) {
		return Hashing.murmur3_32_fixed().hashUnencodedChars(param).padToLong();
	}


	/**
	 * 确保list不为null
	 *
	 * @param list @{@link List}
	 * @param <T>  泛型
	 * @return @{@link List}
	 */
	public static <T> List<T> notNullList(List<T> list) {
		return Optional.ofNullable(list).orElse(Collections.emptyList());
	}


	public static void main(String[] args) {
//        long hashCode = murmurHash32("/origin/userId=12");
//        System.out.println(hashCode);
//
//        System.out.println(encodeToBase62(hashCode));
		System.out.println(encodeToBase62OutOrder(1000000000L));
	}


}
