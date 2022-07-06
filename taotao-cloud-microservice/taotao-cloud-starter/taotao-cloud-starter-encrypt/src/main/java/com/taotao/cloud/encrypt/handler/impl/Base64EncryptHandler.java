package com.taotao.cloud.encrypt.handler.impl;

import com.taotao.cloud.encrypt.handler.EncryptHandler;
import org.springframework.util.Base64Utils;

/**
 * Base64加密处理器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:10:27
 */
public class Base64EncryptHandler implements EncryptHandler {

	@Override
	public byte[] encode(byte[] content) {
		return Base64Utils.encode(content);
	}

	@Override
	public byte[] decode(byte[] content) {
		return Base64Utils.decode(content);
	}
}
