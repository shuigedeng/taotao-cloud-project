package com.taotao.cloud.encrypt.handler;

/**
 * 加密业务接口
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:10:02
 */
public interface EncryptHandler {

	/**
	 * 加密
	 *
	 * @param content 加密内容
	 * @return {@link byte[] }
	 * @since 2022-07-06 15:10:02
	 */
	byte[] encode(byte[] content);

	/**
	 * 解密
	 *
	 * @param content 加密内容
	 * @return {@link byte[] }
	 * @since 2022-07-06 15:10:02
	 */
	byte[] decode(byte[] content);
}
