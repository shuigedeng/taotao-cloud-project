package com.taotao.cloud.sign.util.security.sm;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import com.taotao.cloud.sign.properties.EncryptBodyProperties;
import com.taotao.cloud.sign.util.CheckUtils;
import com.taotao.cloud.sign.util.ISecurity;

/**
 * sm2 加密解密工具类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:44:02
 */
public class Sm2Util implements ISecurity {


	/**
	 * 加密
	 *
	 * @param content  内容
	 * @param password 注解中传入的key 可为null或空字符
	 * @param config   yml配置类
	 * @return String
	 */
	@Override
	public String encrypt(String content, String password, EncryptBodyProperties config) {
		String key = CheckUtils.checkAndGetKey(config.getSm2PubKey(), password, "SM2-KEY-对方公钥-加密");
		return SmUtil.sm2(null, key).encryptBcd(content, KeyType.PublicKey);
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
		String key = CheckUtils.checkAndGetKey(config.getSm2PirKey(), password, "SM2-KEY解密");
		return StrUtil.utf8Str(SmUtil.sm2(key, null).decryptFromBcd(content, KeyType.PrivateKey));
	}
}
