package com.taotao.cloud.open.platform.common.handler.asymmetric;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.taotao.cloud.open.platform.common.handler.AsymmetricCryHandler;
import java.nio.charset.StandardCharsets;

/**
 * SM2非对称加密处理器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:09:53
 */
public class SM2AsymmetricCryHandler implements AsymmetricCryHandler {

	@Override
	public String sign(String privateKey, String content) {
		byte[] data = content.getBytes(StandardCharsets.UTF_8);
		return this.sign(privateKey, data);
	}

	@Override
	public String sign(String privateKey, byte[] content) {
		SM2 sm2 = SmUtil.sm2(privateKey, null);
		return sm2.signHex(HexUtil.encodeHexStr(content));
	}

	@Override
	public boolean verifySign(String publicKey, String content, String sign) {
		byte[] data = content.getBytes(StandardCharsets.UTF_8);
		return this.verifySign(publicKey, data, sign);
	}

	@Override
	public boolean verifySign(String publicKey, byte[] content, String sign) {
		SM2 sm2 = SmUtil.sm2(null, publicKey);
		return sm2.verifyHex(HexUtil.encodeHexStr(content), sign);
	}

	@Override
	public String cry(String publicKey, String content) {
		SM2 sm2 = SmUtil.sm2(null, publicKey);
		return sm2.encryptBcd(content, KeyType.PublicKey);
	}

	@Override
	public byte[] cry(String publicKey, byte[] content) {
		SM2 sm2 = SmUtil.sm2(null, publicKey);
		return sm2.encrypt(content, KeyType.PublicKey);
	}

	@Override
	public String deCry(String privateKey, String content) {
		SM2 sm2 = SmUtil.sm2(privateKey, null);
		return StrUtil.utf8Str(sm2.decryptFromBcd(content, KeyType.PrivateKey));
	}

	@Override
	public byte[] deCry(String privateKey, byte[] content) {
		SM2 sm2 = SmUtil.sm2(privateKey, null);
		return sm2.decrypt(content, KeyType.PrivateKey);
	}
}
