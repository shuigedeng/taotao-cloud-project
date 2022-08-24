package com.taotao.cloud.sign.util;


import com.taotao.cloud.sign.properties.EncryptBodyProperties;

/**
 * 加密解密接口
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:43:41
 */
public interface ISecurity {

	/**
	 * 加密
	 *
	 * @param content  内容
	 * @param password 注解中传入的key 可为null或空字符
	 * @param config   yml配置类
	 * @return String
	 */
	String encrypt(String content, String password, EncryptBodyProperties config);

	/**
	 * 解密
	 *
	 * @param content  内容
	 * @param password 注解中传入的key 可为null或空字符
	 * @param config   yml配置类
	 * @return String
	 */
	String decrypt(String content, String password, EncryptBodyProperties config);
}
