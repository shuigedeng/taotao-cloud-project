package com.taotao.cloud.oss.artislong.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * oss路径跑龙套
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:34:02
 */
public class OssPathUtil {

	/**
	 * 有效
	 *
	 * @param basePath 基本路径
	 * @return {@link String }
	 * @since 2022-04-27 17:34:03
	 */
	public static String valid(String basePath) {
		// 根路径未配置时，默认路径为 /
		if (ObjectUtil.isEmpty(basePath)) {
			basePath = StrUtil.SLASH;
		}
		// 将路径分隔符统一转为 /
		basePath = basePath.replaceAll("\\\\", StrUtil.SLASH).replaceAll("//", StrUtil.SLASH);

		// 将配置默认转为绝对路径
		if (!basePath.startsWith(StrUtil.SLASH)) {
			basePath = StrUtil.SLASH + basePath;
		}
		if (!basePath.endsWith(StrUtil.SLASH)) {
			basePath = basePath + StrUtil.SLASH;
		}
		return basePath;
	}

	/**
	 * 路径转换 将路径分隔符转为统一的 / 分隔
	 *
	 * @param key        路径
	 * @param isAbsolute 是否绝对路径 true：绝对路径；false：相对路径
	 * @return {@link String }
	 * @since 2022-04-27 17:34:03
	 */
	public static String convertPath(String key, Boolean isAbsolute) {
		key = key.replaceAll("\\\\", StrUtil.SLASH).replaceAll("//", StrUtil.SLASH);
		if (isAbsolute && !key.startsWith(StrUtil.SLASH)) {
			key = StrUtil.SLASH + key;
		} else if (!isAbsolute && key.startsWith(StrUtil.SLASH)) {
			key = key.replaceFirst(StrUtil.SLASH, "");
		}
		return key;
	}

	/**
	 * 获取相对根路径的绝对路径
	 *
	 * @param path       全路径
	 * @param basePath   根路径
	 * @param isAbsolute 是否绝对路径 true：绝对路径；false：相对路径
	 * @return {@link String }
	 * @since 2022-04-27 17:34:03
	 */
	public static String replaceKey(String path, String basePath, Boolean isAbsolute) {
		String newPath;
		if (StrUtil.SLASH.equals(basePath)) {
			newPath = convertPath(path, isAbsolute);
		} else {
			newPath = convertPath(path, isAbsolute).replaceAll(convertPath(basePath, isAbsolute),
				"");
		}
		return convertPath(newPath, isAbsolute);
	}
}
