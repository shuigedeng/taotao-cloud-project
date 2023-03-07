package com.taotao.cloud.open.platform.common.util;

import cn.hutool.crypto.KeyUtil;
import com.taotao.cloud.open.platform.common.enums.SymmetricCryEnum;
import java.security.SecureRandom;
import java.util.UUID;
import javax.crypto.SecretKey;

/**
 * 对称加密工具类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:10:39
 */
public class SymmetricCryUtil {

	/**
	 * 获取一个对称加密密钥
	 *
	 * @param symmetricCryEnum 对称加密算法
	 * @return 对称加密密钥
	 */
	public static byte[] getKey(SymmetricCryEnum symmetricCryEnum) {
		//随机数作为种子
		String key = UUID.randomUUID().toString();

		//SM4密钥长度也为128位(可以与AES采用项目算法产生密钥), SecureRandom当种子一样时产生一样的序列
		SecretKey secretKey = KeyUtil.generateKey(symmetricCryEnum.name(), 128,
			new SecureRandom(key.getBytes()));
		return secretKey.getEncoded();
	}
}
