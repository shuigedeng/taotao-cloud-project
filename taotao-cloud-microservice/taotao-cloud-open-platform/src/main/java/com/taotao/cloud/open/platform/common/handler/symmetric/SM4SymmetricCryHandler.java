package com.taotao.cloud.open.platform.common.handler.symmetric;

import cn.hutool.crypto.symmetric.SM4;
import com.taotao.cloud.open.platform.common.handler.SymmetricCryHandler;

/**
 * SM4对称加密处理器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:09:58
 */
public class SM4SymmetricCryHandler implements SymmetricCryHandler {

	@Override
	public String cry(String content, byte[] keyBytes) {
		SM4 sm4 = new SM4(keyBytes);
		return sm4.encryptBase64(content);
	}

	@Override
	public byte[] cry(byte[] content, byte[] keyBytes) {
		SM4 sm4 = new SM4(keyBytes);
		return sm4.encrypt(content);
	}

	@Override
	public String deCry(String content, byte[] keyBytes) {
		SM4 sm4 = new SM4(keyBytes);
		return sm4.decryptStr(content);
	}

	@Override
	public byte[] deCry(byte[] content, byte[] keyBytes) {
		SM4 sm4 = new SM4(keyBytes);
		return sm4.decrypt(content);
	}
}
