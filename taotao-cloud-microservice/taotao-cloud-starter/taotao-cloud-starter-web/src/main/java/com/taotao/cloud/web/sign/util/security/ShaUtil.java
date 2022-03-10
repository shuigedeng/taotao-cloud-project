

package com.taotao.cloud.web.sign.util.security;


import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.web.sign.properties.EncryptBodyProperties;
import com.taotao.cloud.web.sign.enums.SHAEncryptType;
import com.taotao.cloud.web.sign.exception.EncryptDtguaiException;
import com.taotao.cloud.web.sign.util.ISecurity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.springframework.util.StringUtils;

/**
 * <p>SHA加密工具类</p>
 *
 * @since 2019年4月16日14:11:20
 */
public class ShaUtil implements ISecurity {

	/**
	 * 加密
	 *
	 * @param content  内容
	 * @param password 这里为 SHAEncryptType "sha-224" "sha-256" "sha-384" "sha-512"
	 * @param config   yml配置类
	 * @return String
	 */
	@Override
	public String encrypt(String content, String password, EncryptBodyProperties config) {

		if (!StringUtils.hasText(content)) {
			return "";
		}

		password = Optional.ofNullable(password).orElse(SHAEncryptType.SHA256.getValue());

		try {
			MessageDigest md5 = MessageDigest.getInstance(password);
			byte[] bytes = md5.digest((content).getBytes());
			StringBuilder result = new StringBuilder();
			for (byte b : bytes) {
				String temp = Integer.toHexString(b & 0xff);
				if (temp.length() == 1) {
					temp = "0" + temp;
				}
				result.append(temp);
			}
			return result.toString();
		} catch (NoSuchAlgorithmException e) {
			LogUtil.error("ShaEncryptUtil->encrypt 异常,string:{},type:{}", password, password);
			throw new EncryptDtguaiException("ShaEncryptUtil->encrypt 异常");
		}
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
		LogUtil.error("SHA摘要加密,无法解密");
		throw new EncryptDtguaiException("SHA摘要加密,无法解密");
	}
}
