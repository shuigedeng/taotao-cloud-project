package com.taotao.cloud.sign.util.security.rsa;


import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sign.properties.EncryptBodyProperties;
import com.taotao.cloud.sign.exception.EncryptDtguaiException;
import com.taotao.cloud.sign.util.CheckUtils;
import com.taotao.cloud.sign.util.ISecurity;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

/**
 * <p>RSA加密处理工具类</p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:43:54
 */
public class RsaUtil implements ISecurity {

	/**
	 * 非对称密钥算法
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 密钥长度，DH算法的默认密钥长度是1024 密钥长度必须是64的倍数，在512到16384位之间
	 */
	public static final int KEY_SIZE = 2048;

	/**
	 * 加密最大数据
	 */
	private static final int DATA_MAX_SIZE = (KEY_SIZE / 8) - 11;


	/**
	 * 公钥加密
	 *
	 * @param data 待加密数据
	 * @param key  密钥
	 * @return byte[] 加密数据
	 */
	public static byte[] encryptByPublicKey(byte[] data, byte[] key) {
		Optional.ofNullable(data)
			.map(x -> x.length)
			.ifPresent(x -> {
				if (x > DATA_MAX_SIZE) {
					throw new EncryptDtguaiException("公钥加密数据超过最大值:" + DATA_MAX_SIZE);
				}
			});

		try {
			//实例化密钥工厂
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			//初始化公钥
			//密钥材料转换
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
			//产生公钥
			PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

			//数据加密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);

			return cipher.doFinal(Optional.ofNullable(data)
				.orElseThrow(() -> new EncryptDtguaiException("待加密数据为null")));

		} catch (Exception e) {
			LogUtil.error("公钥加密出错", e);
			throw new EncryptDtguaiException("公钥加密出错");
		}
	}

	/**
	 * 公钥解密
	 *
	 * @param data 待解密数据
	 * @param key  密钥
	 * @return byte[] 解密数据
	 */
	public static byte[] decryptByPublicKey(byte[] data, byte[] key) {

		try {
			//实例化密钥工厂
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			//初始化公钥
			//密钥材料转换
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
			//产生公钥
			PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
			//数据解密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			LogUtil.error("公钥解密出错", e);
			throw new EncryptDtguaiException("公钥解密出错");
		}
	}

	/**
	 * 私钥解密
	 *
	 * @param data 待解密数据
	 * @param key  密钥
	 * @return byte[] 解密数据
	 */
	public static byte[] decryptByPrivateKey(byte[] data, byte[] key) {
		try {
			//取得私钥
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			//生成私钥
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			//数据解密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			LogUtil.error("私钥解密出错", e);
			throw new EncryptDtguaiException("私钥解密出错");
		}

	}

	/**
	 * 私钥加密
	 *
	 * @param data 待加密数据
	 * @param key  密钥
	 * @return byte[] 加密数据
	 */
	public static byte[] encryptByPrivateKey(byte[] data, byte[] key) {
		Optional.ofNullable(data)
			.map(x -> x.length)
			.ifPresent(x -> {
				if (x > DATA_MAX_SIZE) {
					throw new EncryptDtguaiException("私钥加密数据超过最大值:" + DATA_MAX_SIZE);
				}
			});

		try {
			//取得私钥
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			//生成私钥
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			//数据加密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return null != data ? cipher.doFinal(data) : null;
		} catch (Exception e) {
			LogUtil.error("私钥加密出错", e);
			throw new EncryptDtguaiException("私钥加密出错");
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
		String key = CheckUtils.checkAndGetKey(config.getRsaPirKey(), password, "RSA-KEY加密");
		return Optional.ofNullable(
				encryptByPrivateKey(content.getBytes(), Base64.decodeBase64(key))
			)
			.map(Base64::encodeBase64String)
			.orElse(null);
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
		String key = CheckUtils.checkAndGetKey(config.getRsaPirKey(), password, "RSA-KEY解密");
		return Optional.ofNullable(
				decryptByPrivateKey(Base64.decodeBase64(content), Base64.decodeBase64(key))
			)
			.map(String::new)
			.orElse(null);
	}
}
