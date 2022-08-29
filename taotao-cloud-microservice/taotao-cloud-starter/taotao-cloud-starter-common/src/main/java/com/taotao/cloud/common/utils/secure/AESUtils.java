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
package com.taotao.cloud.common.utils.secure;

import cn.hutool.core.lang.Assert;
import com.taotao.cloud.common.utils.exception.ExceptionUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AESUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 17:51:17
 */
public class AESUtils {

	private AESUtils() {
	}

	/**
	 * 加密、解密方式
	 */
	private static final String AES = "AES";

	/**
	 * 初始向量值，必须16位
	 */
	private static final String IV_STRING = "16-Bytes--String";

	/**
	 * 默认秘钥，必须16位
	 */
	private static final String DEFAULT_KEY = "70pQxrWV7NWgGRXQ";

	/**
	 * 指定加密的算法、工作模式和填充方式
	 */
	private static final String CIPHER = "AES/CBC/PKCS5Padding";

	/**
	 * AES 使用默认秘钥加密
	 *
	 * @param text 明文
	 * @return 密文
	 * @author shuigedeng
	 * @since 2021-09-02 17:51:25
	 */
	public static String encrypt(String text) {
		return Base64.getEncoder().encodeToString(encrypt(text, DEFAULT_KEY));
	}

	/**
	 * AES 自定义秘钥加密
	 *
	 * @param text 明文
	 * @param key  秘钥（必须16位）
	 * @return 密文
	 * @author shuigedeng
	 * @since 2021-09-02 17:51:25
	 */
	//public static String encrypt(String text, String key) {
	//	if (StringUtils.isAnyBlank(text, key) || 16 != key.length()) {
	//		return null;
	//	}
	//	try {
	//		byte[] byteContent = text.getBytes(StandardCharsets.UTF_8);
	//		byte[] enCodeFormat = key.getBytes();
	//		// 注意，为了能与 iOS 统一这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
	//		SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, AES);
	//		byte[] initParam = IV_STRING.getBytes();
	//		IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
	//		Cipher cipher = Cipher.getInstance(CIPHER);
	//		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
	//		byte[] encryptedBytes = cipher.doFinal(byteContent);
	//		return Base64.getEncoder().encodeToString(encryptedBytes);
	//	} catch (Exception e) {
	//		LogUtil.error(e.getMessage(), e);
	//	}
	//	return null;
	//}

	/**
	 * AES 默认秘钥解密
	 *
	 * @param ciphertext 密文
	 * @return 明文
	 * @author shuigedeng
	 * @since 2021-09-02 17:51:25
	 */
	public static String decrypt(String ciphertext) {
		return decrypt(ciphertext, DEFAULT_KEY);
	}

	/**
	 * AES 自定义秘钥解密
	 *
	 * @param ciphertext 密文
	 * @param key        秘钥（必须16位）
	 * @return 明文
	 * @author shuigedeng
	 * @since 2021-09-02 17:51:25
	 */
	public static String decrypt(String ciphertext, String key) {
		if (org.apache.commons.lang3.StringUtils.isAnyBlank(ciphertext, key) || 16 != key.length()) {
			return null;
		}
		try {
			byte[] encryptedBytes = Base64.getDecoder().decode(ciphertext);
			byte[] enCodeFormat = key.getBytes();
			SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, AES);
			byte[] initParam = IV_STRING.getBytes();
			IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
			Cipher cipher = Cipher.getInstance(CIPHER);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
			byte[] result = cipher.doFinal(encryptedBytes);
			return new String(result, StandardCharsets.UTF_8);
		} catch (Exception e) {
			LogUtils.error(e.getMessage(), e);
		}
		return null;
	}

	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	public static String genAesKey() {
		return StringUtils.random(32);
	}

	public static String encryptToHex(String content, String aesTextKey) {
		return HexUtils.encodeToString(encrypt(content, aesTextKey));
	}

	public static String encryptToHex(byte[] content, String aesTextKey) {
		return HexUtils.encodeToString(encrypt(content, aesTextKey));
	}

	public static String encryptToBase64(String content, String aesTextKey) {
		return Base64Utils.encodeToString(encrypt(content, aesTextKey));
	}

	public static String encryptToBase64(byte[] content, String aesTextKey) {
		return Base64Utils.encodeToString(encrypt(content, aesTextKey));
	}

	public static byte[] encrypt(String content, String aesTextKey) {
		return encrypt(content.getBytes(DEFAULT_CHARSET), aesTextKey);
	}

	public static byte[] encrypt(String content, Charset charset, String aesTextKey) {
		return encrypt(content.getBytes(charset), aesTextKey);
	}

	public static byte[] encrypt(byte[] content, String aesTextKey) {
		return encrypt(content, Objects.requireNonNull(aesTextKey).getBytes(DEFAULT_CHARSET));
	}

	@Nullable
	public static String decryptFormHexToString(@Nullable String content, String aesTextKey) {
		byte[] hexBytes = decryptFormHex(content, aesTextKey);
		if (hexBytes == null) {
			return null;
		}
		return new String(hexBytes, DEFAULT_CHARSET);
	}

	@Nullable
	public static byte[] decryptFormHex(@Nullable String content, String aesTextKey) {
		if (StringUtils.isBlank(content)) {
			return null;
		}
		return decryptFormHex(content.getBytes(DEFAULT_CHARSET), aesTextKey);
	}

	public static byte[] decryptFormHex(byte[] content, String aesTextKey) {
		return decrypt(HexUtils.decode(content), aesTextKey);
	}

	@Nullable
	public static String decryptFormBase64ToString(@Nullable String content, String aesTextKey) {
		byte[] hexBytes = decryptFormBase64(content, aesTextKey);
		if (hexBytes == null) {
			return null;
		}
		return new String(hexBytes, DEFAULT_CHARSET);
	}

	@Nullable
	public static byte[] decryptFormBase64(@Nullable String content, String aesTextKey) {
		if (StringUtils.isBlank(content)) {
			return null;
		}
		return decryptFormBase64(content.getBytes(DEFAULT_CHARSET), aesTextKey);
	}

	public static byte[] decryptFormBase64(byte[] content, String aesTextKey) {
		return decrypt(Base64Utils.decode(content), aesTextKey);
	}

	public static String decryptToString(byte[] content, String aesTextKey) {
		return new String(decrypt(content, aesTextKey), DEFAULT_CHARSET);
	}

	public static byte[] decrypt(byte[] content, String aesTextKey) {
		return decrypt(content, Objects.requireNonNull(aesTextKey).getBytes(DEFAULT_CHARSET));
	}

	public static byte[] encrypt(byte[] content, byte[] aesKey) {
		return aes(Pkcs7Encoder.encode(content), aesKey, Cipher.ENCRYPT_MODE);
	}

	public static byte[] decrypt(byte[] encrypted, byte[] aesKey) {
		return Pkcs7Encoder.decode(aes(encrypted, aesKey, Cipher.DECRYPT_MODE));
	}

	private static byte[] aes(byte[] encrypted, byte[] aesKey, int mode) {
		Assert.isTrue(aesKey.length == 32, "IllegalAesKey, aesKey's length must be 32");
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
			cipher.init(mode, keySpec, iv);
			return cipher.doFinal(encrypted);
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
}
