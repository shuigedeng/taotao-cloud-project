package com.taotao.cloud.web.sign.util.security;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.web.sign.properties.EncryptBodyProperties;
import com.taotao.cloud.web.sign.exception.EncryptDtguaiException;
import com.taotao.cloud.web.sign.util.CheckUtils;
import com.taotao.cloud.web.sign.util.ISecurity;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * <p>DES加密处理工具类</p>
 *
 * @since 2021年4月28日11:19:07
 */
public class DesUtil implements ISecurity {

	private static final String DES = "DES";

	/**
	 * DES加密/解密公共方法
	 *
	 * @param content  字符串内容
	 * @param password 密钥
	 * @param type     加密：{@link Cipher#ENCRYPT_MODE}，解密：{@link Cipher#DECRYPT_MODE}
	 */
	private static String des(String content, String password, int type, String cipherAlgorithm) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

			//算法名称/加密模式/填充方式
			//DES共有四种工作模式-->>ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			cipher.init(type, keyFactory.generateSecret(desKey), random);

			return DesUtil.encryptMode(content, type, cipher);

		} catch (Exception e) {
			LogUtil.error("des解密异常content:{},password:{},type:{},cipherAlgorithm:{},e:{}", content,
				password, type, cipherAlgorithm, e.getMessage());
			throw new EncryptDtguaiException("des解密异常");
		}
	}

	public static String encryptMode(String content, int type, Cipher cipher)
		throws BadPaddingException, IllegalBlockSizeException {

		if (type == Cipher.ENCRYPT_MODE) {

			byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);

			return Hex2Util.parseByte2HexStr(cipher.doFinal(byteContent));

		} else {

			byte[] byteContent = Hex2Util.parseHexStr2Byte(content);

			return new String(cipher.doFinal(byteContent));
		}


	}

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
		String key = CheckUtils.checkAndGetKey(config.getDesKey(), password, "DES-KEY加密");
		return des(content, key, Cipher.ENCRYPT_MODE, config.getDesCipherAlgorithm());
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
		String key = CheckUtils.checkAndGetKey(config.getDesKey(), password, "DES-KEY解密");
		return des(content, key, Cipher.DECRYPT_MODE, config.getDesCipherAlgorithm());
	}
}
