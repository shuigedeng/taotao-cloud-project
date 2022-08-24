package com.taotao.cloud.sign.util.security;

import cn.hutool.crypto.digest.DigestUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sign.properties.EncryptBodyProperties;
import com.taotao.cloud.sign.exception.EncryptDtguaiException;
import com.taotao.cloud.sign.util.ISecurity;

/**
 * <p>MD5加密工具类</p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:44:19
 */
public class Md5Util implements ISecurity {

	/**
	 * MD5加密-32位小写
	 *
	 * @param content  内容
	 * @param password 注解中传入的key 可为null或空字符
	 * @param config   yml配置类
	 * @return String
	 */
	@Override
	public String encrypt(String content, String password, EncryptBodyProperties config) {
		return DigestUtil.md5Hex(content);
	}

	/**
	 * 解密
	 *
	 * @param content  内容
	 * @param password 注解中传入的key 可为null或空字符
	 * @param config   yml配置类
	 * @return String
	 */
	@Override
	public String decrypt(String content, String password, EncryptBodyProperties config) {
		LogUtil.error("MD5消息摘要加密,无法解密");
		throw new EncryptDtguaiException("MD5消息摘要加密,无法解密");
	}
}
