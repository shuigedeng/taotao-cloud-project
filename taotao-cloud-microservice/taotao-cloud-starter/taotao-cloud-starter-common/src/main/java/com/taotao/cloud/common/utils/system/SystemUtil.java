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
package com.taotao.cloud.common.utils.system;


import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.SystemConst;
import com.taotao.cloud.common.utils.lang.ObjectUtil;
import java.util.Objects;
import org.springframework.lang.Nullable;

/**
 * <p> 系统工具类 </p>
 */
public final class SystemUtil {

    private SystemUtil(){}

    /**
     * 获取换行符号
     * @return 换行符号
     */
    public static String getLineSeparator() {
        return getProperty(SystemConst.LINE_SEPARATOR);
    }

    /**
     * 获取属性信息
     * @param key 标识
     * @return 结果
     */
    public static String getProperty(final String key) {
        return System.getProperty(key);
    }

	/**
	 * 代码部署于 linux 上，工作默认为 mac 和 Windows
	 */
	private static final String OS_NAME_LINUX = "LINUX";

	/**
	 * 获取 user home
	 */
	@Nullable
	public static final String USER_HOME = getSystemProperty("user.home");

	/**
	 * 获取用户地址
	 */
	@Nullable
	public static final String USER_DIR = getSystemProperty("user.dir");

	/**
	 * 获取用户名
	 */
	@Nullable
	public static final String USER_NAME = getSystemProperty("user.name");

	/**
	 * os 名
	 */
	@Nullable
	public static final String OS_NAME = getSystemProperty("os.name");

	/**
	 * <p>
	 * Gets a System property, defaulting to {@code null} if the property cannot be read.
	 * </p>
	 * <p>
	 * If a {@code SecurityException} is caught, the return value is {@code null} and a message is
	 * written to {@code System.err}.
	 * </p>
	 *
	 * @param property the system property name
	 * @return the system property value or {@code null} if a security problem occurs
	 */
	@Nullable
	private static String getSystemProperty(final String property) {
		try {
			return System.getProperty(property);
		} catch (final SecurityException ex) {
			return null;
		}
	}

	/**
	 * 判断是否为本地开发环境
	 *
	 * @return boolean
	 */
	public static boolean isLinux() {
		return StrUtil.isNotBlank(OS_NAME) && OS_NAME_LINUX.equalsIgnoreCase(OS_NAME);
	}

	/**
	 * 代码部署于 linux 上，工作默认为 mac 和 Windows
	 *
	 * @return boolean
	 */
	public static boolean isLocalDev() {
		return !isLinux();
	}

	/**
	 * 读取 System Property
	 *
	 * @param key key
	 * @return value
	 */
	@Nullable
	public static String getProp(String key) {
		return System.getProperty(key);
	}

	/**
	 * 读取 System Property
	 *
	 * @param key      key
	 * @param defValue 默认值
	 * @return value
	 */
	public static String getProp(String key, String defValue) {
		return System.getProperty(key, defValue);
	}

	/**
	 * 读取 System Property
	 *
	 * @param key          key
	 * @param defaultValue defaultValue
	 * @return value
	 */
	public static int getPropToInt(String key, int defaultValue) {
		return ObjectUtil.toInt(getProp(key), defaultValue);
	}

	/**
	 * 读取 System Property
	 *
	 * @param key          key
	 * @param defaultValue defaultValue
	 * @return value
	 */
	public static boolean getPropToBool(String key, boolean defaultValue) {
		return Objects.requireNonNull(ObjectUtil.toBoolean(getProp(key), defaultValue));
	}

	/**
	 * 读取 System Property 或者 Env
	 *
	 * @param key key
	 * @return value
	 */
	@Nullable
	public static String getPropOrEnv(String key) {
		String value = System.getProperty(key);
		return value == null ? System.getenv(key) : value;
	}

}
