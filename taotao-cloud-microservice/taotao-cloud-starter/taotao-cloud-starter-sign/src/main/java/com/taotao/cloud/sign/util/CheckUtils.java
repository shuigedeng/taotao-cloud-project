package com.taotao.cloud.sign.util;


import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sign.exception.EncryptDtguaiException;
import org.springframework.util.StringUtils;

/**
 * <p>辅助检测工具类</p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:43:37
 */
public class CheckUtils {

	/**
	 * 注解的 key 优先级高于 全局配置的 key
	 *
	 * @param k1      全局
	 * @param k2      注解
	 * @param keyName 来源信息
	 * @return key
	 */
	public static String checkAndGetKey(String k1, String k2, String keyName) {
		if (!StringUtils.hasText(k1) && !StringUtils.hasText(k2)) {
			LogUtil.error("{} is not configured (未配置{})", keyName, keyName);
			throw new EncryptDtguaiException(String.format("%s is not configured (未配置%s)", keyName, keyName));
		}
		return StringUtils.hasText(k2) ? k2 : k1;
	}

}
